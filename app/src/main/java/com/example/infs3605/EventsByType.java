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

public class EventsByType extends AppCompatActivity implements EventTypeAdapter.OnItemClickListener {

    private RecyclerView mRecyclerView;
    private EventTypeAdapter mAdapter;

    private static final String TAG = "EventsByType";
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;
    private ArrayList<Event> mEventList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_events_by_type);

        mEventList = new ArrayList<>();
        mAdapter = new EventTypeAdapter();

        mRecyclerView = findViewById(R.id.eventsTypeRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Events");

        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mEventList.clear();
                for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
                    Event event = eventSnapshot.getValue(Event.class);
                    mEventList.add(event);
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "Error fetching data: " + error.getMessage());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBListener);
    }

    @Override
    public void onItemClick(int position) {
        Event clickedEvent = mEventList.get(position);
        ArrayList<Event> filteredList = new ArrayList<>();
        for (Event event : mEventList) {
            if (event.getEventCategory().equals(clickedEvent.getEventCategory())) {
                filteredList.add(event);
            }
        }
    }

}
