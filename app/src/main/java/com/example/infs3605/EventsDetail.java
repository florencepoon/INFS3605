package com.example.infs3605;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class EventsDetail extends AppCompatActivity {
    private TextView eventNameText, eventCategoryText, eventParticipationText, eventOrganiserText, eventLocationText, eventStartTimeText, eventDateText;
    private String mEventID;
    private Event Event;
    DatabaseReference eventRef = FirebaseDatabase.getInstance().getReference().child("Events");

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_events_detail);

        //Receiving intent from the dashboard (the eventID string)
        Intent intent = getIntent();
        String eventId = intent.getStringExtra("message");

        eventRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.getKey().equals(eventId)) {
                        String eventName = snapshot.child("eventName").getValue(String.class);
                        String eventOrganiser = snapshot.child("eventOrganiser").getValue(String.class);
                        String eventLocation = snapshot.child("eventLocation").getValue(String.class);
                        String eventCategory = snapshot.child("eventCategory").getValue(String.class);
                        Long eventTime = snapshot.child("eventDate").child("time").getValue(Long.class);

                        // Format date using SimpleDateFormat
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        String formattedDate = sdf.format(eventTime);
                        String eventStartTime = snapshot.child("eventStartTime").getValue(String.class);
                        String eventParticipation = snapshot.child("eventParticipation").getValue(String.class);

                        eventNameText = findViewById(R.id.eventsNameDetail);
                        eventCategoryText = findViewById(R.id.eventsCategoryDetail);
                        eventParticipationText = findViewById(R.id.eventsParticipationDetail);
                        eventOrganiserText = findViewById(R.id.EventsOrganiserDetail);
                        eventLocationText = findViewById(R.id.eventsLocationDetail);
                        eventDateText = findViewById(R.id.eventsDateDetail);
                        eventStartTimeText = findViewById(R.id.eventsTimeDetail);

                        eventNameText.setText(eventName);
                        eventOrganiserText.setText(eventOrganiser);
                        eventDateText.setText(formattedDate);
                        eventStartTimeText.setText(eventStartTime);
                        eventParticipationText.setText(eventParticipation);
                        eventLocationText.setText(eventLocation);
                        eventCategoryText.setText(eventCategory);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });

        //Delete event button
        ImageView deleteEvent = findViewById(R.id.deleteEventButton);
        deleteEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventRef.child(mEventID).removeValue();
                finish(); // close the activity after deleting the event
            }
        });

        //Edit event button
        ImageView editEvent = findViewById(R.id.editEventButton);
        editEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveChanges();
            }
        });
    }

    private void onSaveChanges() {
        EditText mEventNameEditText = findViewById(R.id.eventsNameDetail);
        EditText mEventLocationEditText = findViewById(R.id.eventsLocationDetail);
        EditText mEventCategoryEditText = findViewById(R.id.eventsCategoryDetail);
        EditText mEventTimeEditText = findViewById(R.id.eventsTimeDetail);

        // Update the event object with the new data
        Event.setEventName(mEventNameEditText.getText().toString());
        Event.setEventLocation(mEventLocationEditText.getText().toString());
        Event.setEventCategory(mEventCategoryEditText.getText().toString());
        Event.setEventStartTime(mEventTimeEditText.getText().toString());

        // Save the updated event in Firebase
        eventRef.child(Event.getCreatorID()).setValue(Event)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Handle the success
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle the failure
                    }
                });
    }

}