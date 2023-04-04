package com.example.infs3605;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dashboard extends Fragment {
    private PieChart piechart;
    private Button addEventButton;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;

    TextView dashboardGreeting;
    TextView topEventTypes;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_dashboard, container, false);

        //Customised greeting
        String displayName = mAuth.getCurrentUser().getDisplayName();
        TextView name = v.findViewById(R.id.dashboardGreeting);
        name.setText("Welcome, " + displayName + "!");

        // Get a reference to the "events" node in your Firebase Realtime Database
        myRef = FirebaseDatabase.getInstance().getReference().child("Events");
        // Retrieve the event data from Firebase Realtime Database
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Initialize a Map to store the event data
                Map<String, Integer> eventData = new HashMap<>();

                // Iterate through all events and count the number of events for each event type
                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                    String eventType = eventSnapshot.child("eventCategory").getValue(String.class);
                    if (eventData.containsKey(eventType)) {
                        eventData.put(eventType, eventData.get(eventType) + 1);
                    } else {
                        eventData.put(eventType, 1);
                    }
                }

                // Convert the event data to a List<Entry> for use in MPCharts
                List<Entry> entries = new ArrayList<>();
                for (Map.Entry<String, Integer> entry : eventData.entrySet()) {
                    String eventType = entry.getKey();
                    Integer eventCount = entry.getValue();
                    entries.add(new Entry(0, eventCount.floatValue(), eventType));
                }

                // Create a pie chart using MPCharts
                PieChart pieChart = v.findViewById(R.id.testChart);
                PieDataSet dataSet = new PieDataSet(convertEntriesToPieEntries(entries), "Events");
                dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                PieData data = new PieData(dataSet);
                pieChart.setData(data);
                pieChart.animateXY(1000, 1000);
                pieChart.invalidate();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors that occur
            }
        });

        //method for event count
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String, Integer> eventTypeCount = new HashMap<>();

                // Count the number of events in each location
                for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
                    String eventCategory = eventSnapshot.child("eventCategory").getValue(String.class);
                    eventTypeCount.put(eventCategory, eventTypeCount.getOrDefault(eventCategory, 0) + 1);
                }

                // Sort the event locations by count in descending order
                List<Map.Entry<String, Integer>> typeList = new ArrayList<>(eventTypeCount.entrySet());
                Collections.sort(typeList, (o1, o2) -> o2.getValue().compareTo(o1.getValue()));

                // Retrieve the top 3 event locations
                List<String> topEvents = new ArrayList<>();
                for (int i = 0; i < Math.min(typeList.size(), 3); i++) {
                    topEvents.add(typeList.get(i).getKey());
                }

                // Display the top 3 event locations and counts in a TextView
                StringBuilder stringBuilder = new StringBuilder();
                for (String eventType : topEvents) {
                    int count = eventTypeCount.get(eventType);
                    stringBuilder.append(eventType).append(": ").append(count).append("\n\n");
                }
                topEventTypes.setText(stringBuilder.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Error retrieving event data from Firebase");
            }
        });

        //method for completed events
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Integer> completedEventsCount = new ArrayList<>();
                int count = 0;

                for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
                    // Retrieve the event data
                    Event event = eventSnapshot.child("Events").getValue(Event.class);

                    if (event != null && event.getEventEndTime().compareTo(LocalDate.now().toString()) < 0) {
                        count++;
                    }
                }
                completedEventsCount.add(count);

                // Use the completedEventsCount data to populate the bar chart
                // Get the BarChart view from the layout
                BarChart barChart = v.findViewById(R.id.barChart);

                // Create a list of BarEntry objects for the completed events count
                List<BarEntry> completedEventsData = new ArrayList<>();
                for (int i = 0; i < completedEventsCount.size(); i++) {
                    completedEventsData.add(new BarEntry(i, completedEventsCount.get(i)));
                }

                // Create a BarDataSet with the completed events data and a label
                BarDataSet dataSet = new BarDataSet(completedEventsData, "Completed Events");

                // Set the color for the bars
                dataSet.setColor(Color.BLUE);

                // Create a BarData object with the BarDataSet
                BarData barData = new BarData(dataSet);

                // Set the bar width
                barData.setBarWidth(0.5f);

//                // Set the X-axis labels with the event names
//                String[] eventNamesArray = eventNames.toArray(new String[0]);
//                XAxis xAxis = barChart.getXAxis();
//                xAxis.setValueFormatter(new IndexAxisValueFormatter(eventNamesArray));
//                xAxis.setGranularity(1f);
//                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//                xAxis.setLabelCount(eventNamesArray.length);

                // Set the chart data and refresh the chart
                barChart.setData(barData);
                barChart.invalidate();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle any errors
            }
        });


//        piechart = v.findViewById(R.id.testChart);
//        setupPieChart();
//        loadPieChartData();
        addEventButton = v.findViewById(R.id.AddEventButton);
        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddNewEvent.class);
                startActivity(intent);
            }
        });

        topEventTypes = v.findViewById(R.id.top_event_types);
        return v;
    }

    private List<PieEntry> convertEntriesToPieEntries(List<Entry> entries) {
        List<PieEntry> pieEntries = new ArrayList<>();
        for (Entry entry : entries) {
            pieEntries.add(new PieEntry(entry.getY(), entry.getData().toString()));
        }
        return pieEntries;
    }



}