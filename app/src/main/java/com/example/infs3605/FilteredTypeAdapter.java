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

public class FilteredTypeAdapter extends RecyclerView.Adapter<FilteredTypeAdapter.FilteredTypeViewHolder> {

    private ArrayList<Event> mEventList;
    private RecyclerViewListener mListener;
    private String mEventType;

    public interface RecyclerViewListener {
        void onClick(View view, String eventID);
    }

    public FilteredTypeAdapter(ArrayList<Event> eventList, RecyclerViewListener listener, String eventType) {
        mEventList = eventList;
        mListener = listener;
        mEventType = eventType;
    }

    @NonNull
    @Override
    public FilteredTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item, parent, false);
        FilteredTypeViewHolder evh = new FilteredTypeViewHolder(v, mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull FilteredTypeViewHolder holder, int position) {
        Event currentEvent = mEventList.get(position);
        holder.mTextViewEventName.setText(currentEvent.getEventName());
        holder.mTextViewEventLocation.setText(currentEvent.getEventLocation());
        holder.mTextViewEventType.setText(currentEvent.getEventCategory());
        holder.itemView.setTag(currentEvent.getEventID1());

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String formattedDate = sdf.format(currentEvent.getEventDate());
        holder.mTextViewEventDate.setText(formattedDate);
    }

    @Override
    public int getItemCount() {
        return mEventList.size();
    }

    public static class FilteredTypeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mTextViewEventName;
        public TextView mTextViewEventType;
        public TextView mTextViewEventDate;
        public TextView mTextViewEventLocation;
        public RecyclerViewListener mListener;

        public FilteredTypeViewHolder(@NonNull View itemView, RecyclerViewListener listener) {
            super(itemView);
            mListener = listener;
            mTextViewEventName = itemView.findViewById(R.id.eventName);
            mTextViewEventDate = itemView.findViewById(R.id.eventDate);
            mTextViewEventType = itemView.findViewById(R.id.eventCategory);
            mTextViewEventLocation = itemView.findViewById(R.id.eventLocation);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mListener.onClick(view, (String) view.getTag());
            String eventID = (String) view.getTag();
        }

    }
}
