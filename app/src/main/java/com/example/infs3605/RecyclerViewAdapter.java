package com.example.infs3605;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.infs3605.Event;
import com.example.infs3605.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private List<Event> mEventList;
    private RecyclerViewListener mListener;

    public RecyclerViewAdapter(ArrayList<Event> eventList, RecyclerViewListener listener) {
        mEventList = eventList;
        mListener = listener;
        Collections.sort(mEventList, new EventNameComparator());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item, parent, false);
        return new ViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Event event = mEventList.get(position);
        holder.mEventNameTextView.setText(event.getEventName());
        holder.mEventLocationTextView.setText(event.getEventLocation());
        holder.mEventCategoryTextView.setText(event.getEventCategory());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String formattedDate = dateFormat.format(event.getEventDate());
        holder.mEventDateTextView.setText(formattedDate);
        holder.itemView.setTag(event.getEventID1());
    }

    @Override
    public int getItemCount() {
        return mEventList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mEventNameTextView;
        public TextView mEventLocationTextView;
        public TextView mEventCategoryTextView;
        public TextView mEventDateTextView;
        public RecyclerViewListener mListener;

        public ViewHolder(@NonNull View view, RecyclerViewListener listener) {
            super(view);
            mListener = listener;
            mEventNameTextView = view.findViewById(R.id.eventName);
            mEventLocationTextView = view.findViewById(R.id.eventLocation);
            mEventCategoryTextView = view.findViewById(R.id.eventCategory);
            mEventDateTextView = view.findViewById(R.id.eventDate);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mListener.onClick(view, (String) view.getTag());
            String eventID = (String) view.getTag();
        }
    }

    public interface RecyclerViewListener {
        void onClick(View view, String eventID1);
    }

    private static class EventNameComparator implements Comparator<Event> {
        @Override
        public int compare(Event event1, Event event2) {
            return event1.getEventName().compareToIgnoreCase(event2.getEventName());
        }
    }

    public void setData(ArrayList<Event> data) {
        mEventList.clear();
        mEventList.addAll(data);
        notifyDataSetChanged();
    }
}

