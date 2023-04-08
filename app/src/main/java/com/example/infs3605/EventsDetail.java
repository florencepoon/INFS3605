package com.example.infs3605;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class EventsDetail extends Fragment {
    private ValueEventListener eventListener;
    private TextView eventNameText, eventCategoryText, eventParticipationText, eventOrganiserText, eventLocationText, eventStartTimeText, eventDateText;
    private String eventId = getArguments().getString("eventId");
    DatabaseReference eventRef = FirebaseDatabase.getInstance().getReference().child("events").child(eventId);
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events_detail, container, false);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference eventsRef = database.getReference("events");

        eventNameText = view.findViewById(R.id.eventsNameDetail);
        eventCategoryText = view.findViewById(R.id.eventsCategoryDetail);
        eventParticipationText = view.findViewById(R.id.eventsParticipationDetail);
        eventOrganiserText = view.findViewById(R.id.EventsOrganiserDetail);
        eventLocationText = view.findViewById(R.id.eventsLocationDetail);
        eventDateText = view.findViewById(R.id.eventsDateTimeDetail);
        eventStartTimeText = view.findViewById(R.id.eventsDateTimeDetail);

        // Attach a value event listener to the event node to populate the UI with data
        eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Event event = dataSnapshot.getValue(Event.class);
                    eventNameText.setText(event.getEventName());
                    eventLocationText.setText(event.getEventLocation());
                    eventCategoryText.setText(event.getEventCategory());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        };
        eventRef.addValueEventListener(eventListener);

        //Delete event button
        ImageView deleteEvent = view.findViewById(R.id.deleteEventButton);
        deleteEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView eventName = view.findViewById(R.id.eventsNameDetail);

                Query query = eventsRef.orderByChild("eventName").equalTo(eventName.toString());
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                            eventSnapshot.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle error
                    }
                });
            }
        });

        //Edit event button
        ImageView editEvent = view.findViewById(R.id.editEventButton);
        editEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Remove the value event listener when the fragment is destroyed
        eventRef.removeEventListener(eventListener);
    }
}