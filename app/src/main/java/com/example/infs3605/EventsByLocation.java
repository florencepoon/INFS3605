package com.example.infs3605;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class EventsByLocation extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private LocationAdapter mAdapter;
    private static final String TAG = "EventsByLocation";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_events_by_location);

        ArrayList<String> locationList = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Events");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                locationList.clear(); // clear the list before adding new items
                for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
                    Event event = eventSnapshot.getValue(Event.class);
                    String location = event.getEventLocation();
                    if (!locationList.contains(location)) { // add only if not already in the list
                        locationList.add(location);
                    }
                }
                Collections.sort(locationList); // sort alphabetically
                mAdapter = new LocationAdapter(locationList, ref);
                mRecyclerView.setAdapter(mAdapter); // set the adapter to the RecyclerView
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "Error fetching data: " + error.getMessage());
            }
        });

        mRecyclerView = findViewById(R.id.eventsLocationRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
