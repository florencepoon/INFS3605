package com.example.infs3605;

import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder> {

    private List<String> mLocationList;
    private DatabaseReference mDatabase;
    private RecyclerViewListener mListener;

    public LocationAdapter(ArrayList<String> locationList, DatabaseReference database, RecyclerViewListener listener) {
        mLocationList = locationList;
        mDatabase = database;
        mListener = listener;
        Collections.sort(mLocationList); // sort alphabetically
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item_location, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String location = mLocationList.get(position);
        holder.mLocationTextView.setText(location);

        // add click listener to the location view
        holder.itemView.setOnClickListener(v -> {
            // query the database for events with the selected location
            Query query = mDatabase.orderByChild("EventLocation").equalTo(location);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<Event> events = new ArrayList<>();
                    for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
                        Event event = eventSnapshot.getValue(Event.class);
                        events.add(event);
                    }
                    // launch a new activity to display the list of events for the selected location
                    Intent intent = new Intent(holder.itemView.getContext(), FilteredLocation.class);
                    intent.putExtra("location", location);
                    intent.putParcelableArrayListExtra("events", (ArrayList<? extends Parcelable>) events);
                    holder.itemView.getContext().startActivity(intent);
                }

                String TAG;
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d(TAG, "Error fetching data: " + error.getMessage());
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return mLocationList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mLocationTextView;
        public RecyclerViewListener mListener;

        public ViewHolder(View view) {
            super(view);
            mLocationTextView = view.findViewById(R.id.eventItemLocation);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mListener.onClick(view, getAdapterPosition());
        }
    }

    public interface RecyclerViewListener {
        void onClick(View view, int position);
    }
}
