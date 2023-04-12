package com.example.infs3605;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EventsByName extends AppCompatActivity {
    private DatabaseReference eventsRef;
    private DatabaseReference mDatabase;
    private FirebaseRecyclerAdapter<Event, RecyclerViewAdapter.EventViewHolder> mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_events_by_name);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("events");

        // Set up the RecyclerView with the FirebaseRecyclerAdapter
        RecyclerView recyclerView = findViewById(R.id.eventsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<Event> options = new FirebaseRecyclerOptions.Builder<Event>()
                .setQuery(mDatabase, Event.class)
                .build();

        mAdapter = new FirebaseRecyclerAdapter<Event, RecyclerViewAdapter.EventViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull RecyclerViewAdapter.EventViewHolder holder, int position, @NonNull Event model) {
                holder.bind(model);
            }

            @NonNull
            @Override
            public RecyclerViewAdapter.EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item, parent, false);
                return new RecyclerViewAdapter.EventViewHolder(view);
            }
        };

        recyclerView.setAdapter(mAdapter);
    }


    public void launchEventsDetail() {
        Intent intent = new Intent(this, EventsDetail.class);
        startActivity(intent);
    }
}
