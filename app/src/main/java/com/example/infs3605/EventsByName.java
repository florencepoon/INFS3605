package com.example.infs3605;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

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
import java.util.Comparator;

public class EventsByName extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Event> mEventList;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_events_by_name);

        // Initialize Firebase database reference
        mDatabase = FirebaseDatabase.getInstance().getReference("Events");

        // Initialize the event list
        mEventList = new ArrayList<>();

        // Set up the recycler view
        mRecyclerView = findViewById(R.id.eventsRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // Set up the adapter
        mAdapter = new RecyclerViewAdapter(mEventList);
        mRecyclerView.setAdapter(mAdapter);

        // Attach a listener to the Firebase database reference
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mEventList.clear();
                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                    Event event = eventSnapshot.getValue(Event.class);
                    mEventList.add(event);
                }
                Collections.sort(mEventList, new Comparator<Event>() {
                    @Override
                    public int compare(Event event1, Event event2) {
                        return event1.getEventName().compareToIgnoreCase(event2.getEventName());
                    }
                });
                mAdapter.notifyDataSetChanged();
            }
            String TAG;
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });
    }

    public void launchEventsDetail() {
        Intent intent = new Intent(this, EventsDetail.class);
        startActivity(intent);
    }
}
