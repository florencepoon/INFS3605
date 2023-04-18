package com.example.infs3605;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class EventsByType extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private EventTypeAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_events_by_type);

        mRecyclerView = findViewById(R.id.eventsTypeRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<String> eventTypeList = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Events");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                eventTypeList.clear(); // clear the list before adding new items
                for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
                    Event event;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
