package com.example.infs3605;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EventsByNameRecyclerView extends Fragment {
    private DatabaseReference eventsRef;
    private FirebaseRecyclerAdapter<Event, EventViewHolder> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events_by_name, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.eventsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        eventsRef = FirebaseDatabase.getInstance().getReference().child("events");

        FirebaseRecyclerOptions<Event> options = new FirebaseRecyclerOptions.Builder<Event>()
                .setQuery(eventsRef, Event.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Event, EventViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull EventViewHolder holder, int position, @NonNull Event model) {
                holder.setEventName(model.getEventName());
                holder.setEventDate(model.getEventDate());
                holder.setEventLocation(model.getEventLocation());
                holder.setEventCategory(model.getEventCategory());
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

}
