package com.example.infs3605;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Dashboard extends Fragment {

    private Button addEventButton;
    private RecyclerView recyclerView;
    private dashboardAdapter adapter;
    private RecyclerView.RecyclerListener listener;
    private List<Event> eventData;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    TextView dashboardGreeting;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Setting view to dashboard fragment
        View v = inflater.inflate(R.layout.fragment_dashboard, container, false);

        //Create handle for RecyclerView
        recyclerView = v.findViewById(R.id.recycler_view);

        //Initiate a linear recyclerview layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        dashboardAdapter.RecyclerViewListener listener = new dashboardAdapter.RecyclerViewListener() {
            @Override
            public void onClick(View view, String eventID1) {
                Intent i = new Intent(getActivity(), EventsDetail.class);
                i.putExtra("message", eventID1);
                startActivity(i);
            }
        };

        //Initiate adapter and pass on list of events
        adapter = new dashboardAdapter(new ArrayList<Event>(), listener);
        recyclerView.setAdapter(adapter);

        //Setting click listener for Event Recyclerview

        //Returning the list of events stored in Firebase
        myRef = FirebaseDatabase.getInstance().getReference().child("Events");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Event> events = new ArrayList<Event>();
                String eventName = null;
                String eventLocation = null;
                String eventCategory = null;
                Date eventDate = null;

                //Implementing admin check to filter out events
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    if (snapshot.child("eventName").getValue() != null) {
                        eventName = snapshot.child("eventName").getValue(String.class);
                    }
                    if (snapshot.child("eventLocation").getValue() != null) {
                        eventLocation = snapshot.child("eventLocation").getValue(String.class);
                    }
                    if (snapshot.child("eventCategory").getValue() != null) {
                        eventCategory = snapshot.child("eventCategory").getValue(String.class);
                    }
                    if (snapshot.child("eventDate").getValue() != null) {
                        Long eventTime = snapshot.child("eventDate").child("time").getValue(Long.class);
                        eventDate = new Date(eventTime);
                    }
                    Event event = new Event();
                    event.setEventID1(snapshot.getKey());
                    event.setCreatorID(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    event.setEventName(eventName);
                    event.setEventLocation(eventLocation);
                    event.setEventCategory(eventCategory);
                    event.setEventDate(eventDate);
                    events.add(event);
                }

                // Filter events by dates that fall within the upcoming week
                long currentTime = Calendar.getInstance().getTimeInMillis();
                long upcomingWeekTime = currentTime + TimeUnit.DAYS.toMillis(7);
                List<Event> upcomingEvents = new ArrayList<>();
                for (Event event : events) {
                    //Checking if the user is an admin or not
                    if (mAuth.getCurrentUser().getUid().equals("koGVEACbIRZ8JRLmzGGKgvfhWjs1")) {
                        if (event.getEventDate().getTime() >= currentTime && event.getEventDate().getTime() <= upcomingWeekTime) {
                            upcomingEvents.add(event);
                        }
                    } else {
                        if (event.getCreatorID().equals(mAuth.getCurrentUser().getUid()) && event.getEventDate().getTime() >= currentTime && event.getEventDate().getTime() <= upcomingWeekTime) {
                            upcomingEvents.add(event);
                        } else {
                            continue;
                        }
                    }
                    adapter.setData((ArrayList<Event>) upcomingEvents);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        //Customised greeting
        String displayName = mAuth.getCurrentUser().getDisplayName();
        TextView name = v.findViewById(R.id.dashboardGreeting);
        name.setText("Welcome back, " + displayName + "!");

        return v;
    }
}