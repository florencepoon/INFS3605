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

public class TypeAdapter extends RecyclerView.Adapter<TypeAdapter.ViewHolder> {

    private List<String> mTypeList;
    private DatabaseReference mDatabase;
    private RecyclerViewListener mListener;

    public TypeAdapter(ArrayList<String> typeList, DatabaseReference database, RecyclerViewListener listener) {
        mTypeList = typeList;
        mListener = listener;
        mDatabase = database;
        Collections.sort(mTypeList); // sort alphabetically
    }

    public interface RecyclerViewListener {
        void onClick(View view, int position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item_type, parent, false);
        return new ViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String type = mTypeList.get(position);
        holder.mTypeTextView.setText(type);

        // add click listener to the category view
        holder.itemView.setOnClickListener(v -> {
            // query the database for events with the selected category
            Query query = mDatabase.orderByChild("eventCategory").equalTo(type);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<Event> events = new ArrayList<>();
                    for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
                        Event event = eventSnapshot.getValue(Event.class);
                        events.add(event);
                    }
                    // launch a new activity to display the list of events for the selected location
                    Intent intent = new Intent(holder.itemView.getContext(), FilteredType.class);
                    intent.putExtra("type", type);
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
        return mTypeList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mTypeTextView;
        public RecyclerViewListener mListener;

        public ViewHolder(@NonNull View view, RecyclerViewListener listener) {
            super(view);
            mTypeTextView = view.findViewById(R.id.eventItemType);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mListener.onClick(view, getAdapterPosition());
        }
    }
}
