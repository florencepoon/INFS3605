package com.example.infs3605;

import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class EventsByType extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private TypeAdapter mAdapter;
    private static final String TAG = "EventsByType";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_events_by_type);

        ArrayList<String> eventTypeList = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Events");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Event> eventList = new ArrayList<>();
                eventTypeList.clear(); // clear the list before adding new items
                for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
                    Event event = eventSnapshot.getValue(Event.class);
                    eventList.add(event);
                    String type = event.getEventCategory();
                    if (!eventTypeList.contains(type)) { // add only if not already in the list
                        eventTypeList.add(type);
                    }
                }
                Collections.sort(eventTypeList); // sort alphabetically
                mAdapter = new TypeAdapter(eventTypeList, ref, new TypeAdapter.RecyclerViewListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Intent intent = new Intent(EventsByType.this, FilteredType.class);
                        intent.putExtra("type", eventList.get(position));
                        startActivity(intent);
                    }
                });

                mRecyclerView.setAdapter(mAdapter); // set the adapter to the RecyclerView
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "Error fetching data: " + error.getMessage());
            }
        });

        mRecyclerView = findViewById(R.id.eventsTypeRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
