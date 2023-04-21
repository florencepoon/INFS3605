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
    private RecyclerViewListener mListener;

    public FilteredLocationAdapter(ArrayList<Event> eventList, RecyclerViewListener listener) {
        mEventList = eventList;
        mListener = listener;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the view for a single event item
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item_location_filtered, parent, false);
        return new EventViewHolder(itemView, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        // Bind the data for a single event item
        Event event = mEventList.get(position);
        holder.mNameTextView.setText(event.getEventName());
        holder.mCategoryTextView.setText(event.getEventCategory());
        holder.mLocationTextView.setText(event.getEventLocation());
        holder.itemView.setTag(event.getEventID1());

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String formattedDate = dateFormat.format(event.getEventDate());
        holder.mDateTextView.setText(formattedDate);
    }

    @Override
    public int getItemCount() {
        return mEventList.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mNameTextView;
        public TextView mCategoryTextView;
        public TextView mLocationTextView;
        public TextView mDateTextView;
        public RecyclerViewListener mListener;

        public EventViewHolder(@NonNull View itemView, RecyclerViewListener listener) {
            super(itemView);
            mNameTextView = itemView.findViewById(R.id.rvLocationEventName);
            mCategoryTextView = itemView.findViewById(R.id.rvLocationEventCategory);
            mLocationTextView = itemView.findViewById(R.id.rvEventLocation);
            mDateTextView = itemView.findViewById(R.id.rvLocationEventDate);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            mListener.onClick(view, (String) view.getTag());
            String eventID = (String) view.getTag();
        }

    }

    public interface RecyclerViewListener {
        void onClick(View view, String eventID);
    }


}
