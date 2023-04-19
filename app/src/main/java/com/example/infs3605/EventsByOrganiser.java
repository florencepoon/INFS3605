package com.example.infs3605;

import android.content.Intent;
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

public class EventsByOrganiser extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private EventOrganiserAdapter mAdapter;
    private RecyclerView.RecyclerListener listener;
    private static final String TAG = "EventsByOrganiser";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_events_by_organiser);

        ArrayList<String> eventOrganiserList = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Events");
        mRecyclerView = findViewById(R.id.eventsOrganiserRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        EventOrganiserAdapter.ViewHolder.RecyclerViewListener listener = new EventOrganiserAdapter.ViewHolder.RecyclerViewListener() {
            @Override
            public void onClick(View view, String eventID1) {
                Intent i = new Intent(EventsByOrganiser.this, EventsDetail.class);
                i.putExtra("message", eventID1);
                startActivity(i);
            }
        };


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Event> eventList = new ArrayList<>();
                eventOrganiserList.clear(); // clear the list before adding new items
                for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
                    Event event = eventSnapshot.getValue(Event.class);
                    eventList.add(event);
                    String organiser = event.getEventOrganiser();
                    if (!eventOrganiserList.contains(organiser)) { // add only if not already in the list
                        eventOrganiserList.add(organiser);
                    }
                }
                Collections.sort(eventOrganiserList); // sort alphabetically
                mAdapter = new EventOrganiserAdapter(eventList, listener);
                mRecyclerView.setAdapter(mAdapter); // set the adapter to the RecyclerView
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "Error fetching data: " + error.getMessage());
            }
        });

    }
}
