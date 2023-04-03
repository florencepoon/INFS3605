package com.example.infs3605;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class addNewEvent extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private EditText eventNameText, eventOrganiserText, eventCategoryText, eventParticipationText, eventLocationText, eventDateText, eventStartTimeText, eventEndTimeText;
    private Button addNewEventButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_create_events);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        eventNameText = findViewById(R.id.addEventName);
        eventOrganiserText = findViewById(R.id.editTextEventOrganiser);
        eventCategoryText = findViewById(R.id.editTextEventCategory);
        eventParticipationText = findViewById(R.id.editTextParticipation);
        eventLocationText = findViewById(R.id.editTextLocation);
        eventDateText = findViewById(R.id.editTextDate);
        eventStartTimeText = findViewById(R.id.editTextStartTime);
        eventEndTimeText = findViewById(R.id.editTextEndTime);
        addNewEventButton = findViewById(R.id.button2);

        addNewEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writeNewEvent();
            }
        });
        }

        public void writeNewEvent() {
        Event event = new Event(1, eventNameText.getText().toString(), eventOrganiserText.getText().toString(), eventCategoryText.getText().toString(),
                eventParticipationText.getText().toString(), eventLocationText.getText().toString(), eventDateText.getText().toString(), eventStartTimeText.getText().toString(),
                eventEndTimeText.getText().toString());
        mDatabase.child("Events").child(String.valueOf(event.getEventID())).setValue(event);
        }
    }
