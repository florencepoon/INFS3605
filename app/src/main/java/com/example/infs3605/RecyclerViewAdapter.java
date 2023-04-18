package com.example.infs3605;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private List<Event> mEventList;

    public RecyclerViewAdapter(List<Event> eventList) {
        mEventList = eventList;
        Collections.sort(mEventList, new EventNameComparator());
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
        holder.mEventNameTextView.setText(event.getEventName());
        holder.mEventLocationTextView.setText(event.getEventLocation());
        holder.mEventCategoryTextView.setText(event.getEventCategory());
        holder.mEventDateTextView.setText(event.getEventDate().toString());
    }

    @Override
    public int getItemCount() {
        return mEventList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mEventNameTextView;
        public TextView mEventLocationTextView;
        public TextView mEventCategoryTextView;
        public TextView mEventDateTextView;


        public ViewHolder(View view) {
            super(view);
            mEventNameTextView = view.findViewById(R.id.eventName);
            mEventLocationTextView = view.findViewById(R.id.eventLocation);
            mEventCategoryTextView = view.findViewById(R.id.eventCategory);
            mEventDateTextView = view.findViewById(R.id.eventDate);
        }
    }

    private static class EventNameComparator implements Comparator<Event> {
        @Override
        public int compare(Event event1, Event event2) {
            return event1.getEventName().compareToIgnoreCase(event2.getEventName());
        }
    }
}

