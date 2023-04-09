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
    private RecyclerView.RecyclerListener mListener;

    //Creating listener
    public dashboardAdapter(ArrayList<Event> events, RecyclerView.RecyclerListener listener) {
        mEvents = events;
        mListener = listener;
    }
    //Mapping event data to recyclerview
    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mName, mCategory, mLocation, mDate;
        public RecyclerView.RecyclerListener mListener;
        public MyViewHolder(@NonNull View itemView, RecyclerView.RecyclerListener listener) {
            super(itemView);
            mListener = listener;
            mName = itemView.findViewById(R.id.eventName);
            mCategory = itemView.findViewById(R.id.eventCategory);
            mLocation = itemView.findViewById(R.id.eventLocation);
            mDate = itemView.findViewById(R.id.eventDate);
        }

        @Override
        public void onClick(View view) {
        }
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

        // Format date using SimpleDateFormat
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String formattedDate = sdf.format(event.getEventDate());
        holder.mDate.setText(formattedDate);
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
