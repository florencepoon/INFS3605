package com.example.infs3605;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

import java.text.SimpleDateFormat;
import java.util.Locale;

public class EventsDetail extends AppCompatActivity {
    private ValueEventListener eventListener;
    private TextView eventNameText, eventCategoryText, eventParticipationText, eventOrganiserText, eventLocationText, eventStartTimeText, eventDateText;
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

//        eventRef.addValueEventListener(eventListener);

        //Delete event button
        ImageView deleteEvent = findViewById(R.id.deleteEventButton);
        deleteEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView eventName = findViewById(R.id.eventsNameDetail);
                Query query = eventRef.orderByChild("eventName").equalTo(eventName.toString());
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
//        ImageView editEvent = findViewById(R.id.editEventButton);
//        editEvent.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), MainActivity.class);
//                startActivity(intent);
//            }
//        });

    }
}