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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private List<Event> mEventList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

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
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String formattedDate = dateFormat.format(event.getEventDate());
        holder.mEventDateTextView.setText(formattedDate);
    }

    @Override
    public int getItemCount() {
        return mEventList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
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
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            Event clickedEvent = mEventList.get(position);
                            Intent intent = new Intent(v.getContext(), EventsDetail.class);
                            intent.putExtra("creatorID", clickedEvent.getCreatorID());
                            v.getContext().startActivity(intent);
                        }
                    }
                }

            });
        }
    }

    private static class EventNameComparator implements Comparator<Event> {
        @Override
        public int compare(Event event1, Event event2) {
            return event1.getEventName().compareToIgnoreCase(event2.getEventName());
        }
    }
}
