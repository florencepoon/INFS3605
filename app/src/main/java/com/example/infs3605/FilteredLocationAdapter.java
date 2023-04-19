package com.example.infs3605;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class FilteredLocationAdapter extends RecyclerView.Adapter<FilteredLocationAdapter.EventViewHolder> {

    private ArrayList<Event> mEventList;

    public FilteredLocationAdapter(ArrayList<Event> eventList) {
        mEventList = eventList;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the view for a single event item
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item, parent, false);
        return new EventViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        // Bind the data for a single event item
        Event event = mEventList.get(position);
        holder.mNameTextView.setText(event.getEventName());
        holder.mCategoryTextView.setText(event.getEventCategory());
        holder.mLocationTextView.setText(event.getEventLocation());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String formattedDate = dateFormat.format(event.getEventDate());
        holder.mDateTextView.setText(formattedDate);
    }

    @Override
    public int getItemCount() {
        return mEventList.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        public TextView mNameTextView;
        public TextView mCategoryTextView;
        public TextView mLocationTextView;
        public TextView mDateTextView;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            mNameTextView = itemView.findViewById(R.id.eventName);
            mCategoryTextView = itemView.findViewById(R.id.eventCategory);
            mLocationTextView = itemView.findViewById(R.id.eventLocation);
            mDateTextView = itemView.findViewById(R.id.eventDate);
        }
    }
}
