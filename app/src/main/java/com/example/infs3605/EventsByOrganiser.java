package com.example.infs3605;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

    private static final String TAG = "EventsByOrganiser";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_events_by_organiser);

        ArrayList<String> eventOrganiserList = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("events");

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
                mAdapter = new EventOrganiserAdapter(eventList);
                mRecyclerView.setAdapter(mAdapter); // set the adapter to the RecyclerView
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "Error fetching data: " + error.getMessage());
            }
        });

    }
}
