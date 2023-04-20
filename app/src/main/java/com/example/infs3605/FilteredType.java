package com.example.infs3605;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class FilteredType extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private FilteredTypeAdapter mAdapter;
    private DatabaseReference mDatabase;

    private static final String TAG = "FilteredType";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_events_by_type_filtered);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Events");

        ArrayList<Event> eventList = new ArrayList<>();

        mRecyclerView = findViewById(R.id.eventsTypeFilteredRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        FilteredTypeAdapter.RecyclerViewListener listener = new FilteredTypeAdapter.RecyclerViewListener() {
            @Override
            public void onClick(View view, String eventID) {
                Intent i = new Intent(FilteredType.this, EventsDetail.class);
                i.putExtra("message", eventID);
                startActivity(i);
            }
        };

        String type = getIntent().getStringExtra("Category");
        TextView typeTextView = findViewById(R.id.filteredTypeTextView);
        typeTextView.setText(type);

        mAdapter = new FilteredTypeAdapter(eventList, listener, type);
        mRecyclerView.setAdapter(mAdapter); // set the adapter to the RecyclerView

        Query query = mDatabase.orderByChild("EventCategory").equalTo(type);
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
