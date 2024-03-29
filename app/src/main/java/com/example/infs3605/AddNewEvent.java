package com.example.infs3605;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public class AddNewEvent extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private EditText eventNameText, eventOrganiserText, eventFacultyText, eventLocationText, eventStartTimeText, eventEndTimeText;
    private Spinner eventCategorySpinner, eventParticipationSpinner;
    private EditText eventDateText;
    private Button addNewEventButton;

    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_create_events);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        eventNameText = findViewById(R.id.addEventName);
        eventOrganiserText = findViewById(R.id.editTextEventOrganiser);
        eventFacultyText = findViewById(R.id.editTextEventFaculty);
        eventCategorySpinner = findViewById(R.id.editTextEventCategory);
        eventParticipationSpinner = findViewById(R.id.editTextParticipation);
        eventLocationText = findViewById(R.id.editTextLocation);
        eventDateText = findViewById(R.id.editTextDate);
        eventStartTimeText = findViewById(R.id.editTextStartTime);
        eventEndTimeText = findViewById(R.id.editTextEndTime);
        addNewEventButton = findViewById(R.id.button2);
        backButton = findViewById(R.id.leftArrowCreateEvent);

        addNewEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writeNewEvent();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        }

        public void writeNewEvent() {

        String eventDateString = eventDateText.getText().toString();

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date;
            try {
                date = dateFormat.parse(eventDateString);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            String creatorID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            Event event = new Event(creatorID, eventNameText.getText().toString(), eventOrganiserText.getText().toString(), eventFacultyText.getText().toString(), eventCategorySpinner.getSelectedItem().toString(),
                eventParticipationSpinner.getSelectedItem().toString(), eventLocationText.getText().toString(), date, eventStartTimeText.getText().toString(),
                eventEndTimeText.getText().toString());
        mDatabase.child("Events").push().setValue(event);
        }

}
