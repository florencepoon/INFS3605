package com.example.infs3605;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapter extends AppCompatActivity {

    private RecyclerView eventNameRecyclerView, eventOrganiserRecyclerView;
    private RecyclerViewAdapter1 eventNameAdapter;
    private RecyclerViewAdapter2 eventOrganiserAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up first RecyclerView
        eventNameRecyclerView = findViewById(R.id.eventsRecyclerView);
        eventNameRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventNameAdapter = new RecyclerViewAdapter1(getData());
        eventNameRecyclerView.setAdapter(eventNameAdapter);

        // Set up second RecyclerView
        eventOrganiserRecyclerView = findViewById(R.id.eventsOrganiserRecyclerView);
        eventOrganiserRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        eventOrganiserAdapter = new RecyclerViewAdapter2(getData());
        eventOrganiserRecyclerView.setAdapter(eventOrganiserAdapter);
    }

    // First RecyclerView adapter
    private class RecyclerViewAdapter1 extends RecyclerView.Adapter<RecyclerViewAdapter1.EventViewHolder> {
        @NonNull
        @Override
        public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item_1, parent, false);
            return new RecyclerView.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
            holder.textView.setText(data.get(position));
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public static class EventViewHolder extends RecyclerView.ViewHolder {
            private TextView mNameView;
            private TextView mLocationView;
            private TextView mDateView;

            public EventViewHolder(View itemView) {
                super(itemView);

                mNameView = itemView.findViewById(R.id.eventName);
                mLocationView = itemView.findViewById(R.id.eventLocation);
                mDateView = itemView.findViewById(R.id.eventDate);
            }

            public void bind(Event event) {
                mNameView.setText(event.getEventName());
                mLocationView.setText(event.getEventLocation());
                mDateView.setText((CharSequence) event.getEventDate());
            }
        }
    }

    // Second RecyclerView adapter
    private class RecyclerViewAdapter2 extends RecyclerView.Adapter<RecyclerViewAdapter2.ViewHolder> {

        private List<String> data;

        public RecyclerViewAdapter2(List<String> data) {
            this.data = data;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item_2, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.textView.setText(data.get(position));
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView textView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.textView2);
            }
        }
    }
}
