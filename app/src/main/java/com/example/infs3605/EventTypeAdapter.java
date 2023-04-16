package com.example.infs3605;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class EventTypeAdapter extends RecyclerView.Adapter<EventTypeAdapter.EventTypeViewHolder> {

    private ArrayList<String> mEventTypeList;

    public static class EventTypeViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;

        public EventTypeViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.eventItemType);
        }
    }

    public EventTypeAdapter(ArrayList<String> eventTypeList) {
        mEventTypeList = eventTypeList;
    }

    @NonNull
    @Override
    public EventTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item_type, parent, false);
        EventTypeViewHolder viewHolder = new EventTypeViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull EventTypeViewHolder holder, int position) {
        String eventType = mEventTypeList.get(position);
        holder.mTextView.setText(eventType);
    }

    @Override
    public int getItemCount() {
        return mEventTypeList.size();
    }
}
