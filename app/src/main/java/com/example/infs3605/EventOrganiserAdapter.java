package com.example.infs3605;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class EventOrganiserAdapter extends RecyclerView.Adapter<EventOrganiserAdapter.ViewHolder> {
    private List<Event> mEventList;

    public EventOrganiserAdapter(List<Event> eventList) {
        mEventList = eventList;
        Collections.sort(mEventList, new Comparator<Event>() {
            @Override
            public int compare(Event e1, Event e2) {
                return e1.getEventOrganiser().compareToIgnoreCase(e2.getEventOrganiser()); // sort alphabetically by organiser name
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Event event = mEventList.get(position);
        holder.mOrganiserNameTextView.setText(event.getEventOrganiser());
        holder.mEventNameTextView.setText(event.getEventName());
        holder.mEventLocationTextView.setText(event.getEventLocation());
        holder.mEventCategoryTextView.setText(event.getEventCategory());
        holder.mEventDateTextView.setText((CharSequence) event.getEventDate());
    }

    @Override
    public int getItemCount() {
        return mEventList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mOrganiserNameTextView;
        public TextView mEventNameTextView;
        public TextView mEventLocationTextView;
        public TextView mEventCategoryTextView;
        public TextView mEventDateTextView;

        public ViewHolder(View view) {
            super(view);
            mOrganiserNameTextView = view.findViewById(R.id.editTextEventOrganiser);
            mEventNameTextView = view.findViewById(R.id.eventName);
            mEventLocationTextView = view.findViewById(R.id.eventLocation);
            mEventCategoryTextView = view.findViewById(R.id.eventCategory);
            mEventDateTextView = view.findViewById(R.id.eventDate);
        }
    }
}
