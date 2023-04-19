package com.example.infs3605;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class EventOrganiserAdapter extends RecyclerView.Adapter<EventOrganiserAdapter.ViewHolder> {
    private List<Event> mEventList;
    private RecyclerViewAdapter.RecyclerViewListener mListener;

    public EventOrganiserAdapter(List<Event> eventList, RecyclerViewAdapter.RecyclerViewListener listener) {
        mEventList = eventList;
        mListener = listener;

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
                .inflate(R.layout.recycler_view_item_organiser, parent, false);
        return new ViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Event event = mEventList.get(position);
        holder.mOrganiserNameTextView.setText(event.getEventOrganiser());
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

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mOrganiserNameTextView;
        public TextView mEventNameTextView;
        public TextView mEventLocationTextView;
        public TextView mEventCategoryTextView;
        public TextView mEventDateTextView;
        public RecyclerViewAdapter.RecyclerViewListener mListener;

        public ViewHolder(View view, RecyclerViewAdapter.RecyclerViewListener listener) {
            super(view);
            mListener = listener;
            mOrganiserNameTextView = view.findViewById(R.id.eventOrganiser);
            mEventNameTextView = view.findViewById(R.id.eventNameOrganiser);
            mEventLocationTextView = view.findViewById(R.id.eventLocationOrganiser);
            mEventCategoryTextView = view.findViewById(R.id.eventCategoryOrganiser);
            mEventDateTextView = view.findViewById(R.id.eventDateOrganiser);
            itemView.setOnClickListener(this);
        }

        public void onClick(View view) {
            mListener.onClick(view, (String) view.getTag());
            String eventID = (String) view.getTag();
        }

        public interface RecyclerViewListener extends RecyclerViewAdapter.RecyclerViewListener {
            void onClick(View view, String eventID1);
        }
    }
}
