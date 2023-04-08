package com.example.infs3605;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
    private FirebaseRecyclerAdapter<Event, EventViewHolder> adapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events_by_name, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.eventsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));
        eventsRef = FirebaseDatabase.getInstance().getReference().child("events");

        FirebaseRecyclerOptions<Event> options = new FirebaseRecyclerOptions.Builder<Event>()
                .setQuery(eventsRef, Event.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Event, EventViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull EventViewHolder holder, int position, @NonNull Event model) {
                holder.setEventName(model.getEventName());
//                holder.setEventDate(model.getEventDate());
                holder.setEventLocation(model.getEventLocation());
                holder.setEventCategory(model.getEventCategory());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        launchEventsDetail();
                    }
                });

            }

            @NonNull
            @Override
            public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.recycler_view_item, parent, false);
                return new EventViewHolder(view);
            }
        };

        recyclerView.setAdapter(adapter);

        return view;
    }

    private static class EventViewHolder extends RecyclerView.ViewHolder {
        private TextView eventName;
        private TextView eventDate;
        private TextView eventLocation;
        private TextView eventCategory;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.eventName);
            eventDate = itemView.findViewById(R.id.eventDate);
            eventLocation = itemView.findViewById(R.id.eventLocation);
            eventCategory = itemView.findViewById(R.id.eventCategory);
        }

        private void setEventName(String name) {
            eventName.setText(name);
        }

        private void setEventDate(String date) {
            eventDate.setText(date);
        }

        private void setEventLocation(String location) {
            eventLocation.setText(location);
        }

        private void setEventCategory(String category) {
            eventCategory.setText(category);
        }
    }

    public void launchEventsDetail() {
        Intent intent = new Intent(this, EventsDetail.class);
        startActivity(intent);
    }
}

