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

public class dashboardAdapter extends RecyclerView.Adapter<dashboardAdapter.MyViewHolder>  {
    private ArrayList<Event> mEvents;
    private RecyclerViewListener mListener;

    public dashboardAdapter(ArrayList<Event> events, RecyclerViewListener listener) {
        mEvents = events;
        mListener = listener;
    }

    //Linking the xml layout file as the RecyclerView item
    @NonNull
    @Override
    public dashboardAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item, parent, false);
        return new MyViewHolder(view, mListener);
    }

    //Mapping event attributes to the recycler view list
    @Override
    public void onBindViewHolder(@NonNull dashboardAdapter.MyViewHolder holder, int position) {
        Event event = mEvents.get(position);
        holder.mName.setText(event.getEventName());
        holder.mCategory.setText(event.getEventCategory());
        holder.mLocation.setText(event.getEventLocation());
        holder.itemView.setTag(event.getEventID1());

        // Format date using SimpleDateFormat
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String formattedDate = sdf.format(event.getEventDate());
        holder.mDate.setText(formattedDate);
    }

    //Mapping event data to recyclerview
    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mName, mCategory, mLocation, mDate;
        public RecyclerViewListener mListener;
        public MyViewHolder(@NonNull View itemView, RecyclerViewListener listener) {
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
        return mEvents.size();
    }

    //Supplying data to the adapter
    public void setData(ArrayList<Event> data) {
        mEvents.clear();
        mEvents.addAll(data);
        notifyDataSetChanged();
    }
}