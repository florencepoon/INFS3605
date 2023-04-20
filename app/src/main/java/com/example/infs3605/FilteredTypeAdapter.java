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

public class FilteredTypeAdapter extends RecyclerView.Adapter<FilteredTypeAdapter.ViewHolder> {
    private ArrayList<Event> mEvents;
    private RecyclerViewAdapter.RecyclerViewListener mListener;
    private String mCategory;

    public FilteredTypeAdapter(ArrayList<Event> events, RecyclerViewAdapter.RecyclerViewListener listener, String category) {
        mEvents = events;
        mListener = listener;
        mCategory = category;
    }

    //Linking the xml layout file as the RecyclerView item
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item, parent, false);
        return new ViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull FilteredTypeAdapter.ViewHolder holder, int position) {
        Event event = mEvents.get(position);
        if (event.getEventCategory().equals(mCategory)) {
            holder.mName.setText(event.getEventName());
            holder.mCategory.setText(event.getEventCategory());
            holder.mLocation.setText(event.getEventLocation());
            holder.itemView.setTag(event.getEventID1());

            // Format date using SimpleDateFormat
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String formattedDate = sdf.format(event.getEventDate());
            holder.mDate.setText(formattedDate);
        }
    }

    //Mapping event data to recyclerview
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mName, mCategory, mLocation, mDate;
        public RecyclerViewAdapter.RecyclerViewListener mListener;

        public ViewHolder(@NonNull View itemView, RecyclerViewAdapter.RecyclerViewListener listener) {
            super(itemView);
            mListener = listener;
            mName = itemView.findViewById(R.id.eventName);
            mCategory = itemView.findViewById(R.id.eventCategory);
            mLocation = itemView.findViewById(R.id.eventLocation);
            mDate = itemView.findViewById(R.id.eventDate);
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

    @Override
    public int getItemCount() {
        int count = 0;
        for (Event event : mEvents) {
            if (event.getEventCategory().equals(mCategory)) {
                count++;
            }
        }
        return count;
    }

    //Supplying data to the adapter
    public void setData(ArrayList<Event> data) {
        mEvents.clear();
        mEvents.addAll(data);
        notifyDataSetChanged();
    }
}
