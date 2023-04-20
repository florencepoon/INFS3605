package com.example.infs3605;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FilteredLocation extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private FilteredLocationAdapter mAdapter;

    private static final String TAG = "FilteredLocation";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_events_by_location_filtered);

        String location = getIntent().getStringExtra("location");
        TextView locationTextView = findViewById(R.id.filteredLocationTextView);
        locationTextView.setText(location);

        mRecyclerView = findViewById(R.id.eventsLocationFilteredRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<Event> eventList = new ArrayList<>();
        mAdapter = new FilteredLocationAdapter(eventList);
        mRecyclerView.setAdapter(mAdapter); // set the adapter to the RecyclerView

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Events");

        Query query = ref.orderByChild("EventLocation").equalTo(location);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                eventList.clear(); // clear the list before adding new items
                for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
                    Event event = eventSnapshot.getValue(Event.class);
                    event.setEventID1(eventSnapshot.getKey());
                    eventList.add(event);
                }
                mAdapter.notifyDataSetChanged(); // update the RecyclerView with new data
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "Error fetching data: " + error.getMessage());
            }
        });
    }
}
