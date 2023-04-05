package com.example.infs3605;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Analytics extends Fragment {

    DatabaseReference myRef;
    private PieChart piechart;

    TextView topEventTypes;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_analytics, container, false);
        topEventTypes = view.findViewById(R.id.top_three_organisers);

        //method for populating piechart for Event Location Insights
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
                PieChart pieChart = view.findViewById(R.id.testChart);
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

        ImageView backButton = view.findViewById(R.id.leftArrowAnalytics);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

        //Method for generating a list of the top event faculties
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String, Integer> eventTypeCount = new HashMap<>();

                // Count the number of events in each location
                for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
                    String eventFaculty = eventSnapshot.child("eventFaculty").getValue(String.class);
                    eventTypeCount.put(eventFaculty, eventTypeCount.getOrDefault(eventFaculty, 0) + 1);
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
                for (String eventFaculty : topEvents) {
                    int count = eventTypeCount.get(eventFaculty);
                    stringBuilder.append(eventFaculty).append(": ").append(count).append("\n\n");
                }
                topEventTypes.setText(stringBuilder.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Error retrieving event data from Firebase");
            }
        });

        //method for faculty count
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Map to keep track of the faculty counts
                Map<String, Integer> facultyCounts = new HashMap<>();

                // Loop through the event data
                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                    // Retrieve the faculty data for the event
                    String faculty = eventSnapshot.child("eventFaculty").getValue(String.class);

                    // Update the count for the faculty
                    if (faculty != null) {
                        Integer count = facultyCounts.get(faculty);
                        if (count == null) {
                            count = 0;
                        }
                        facultyCounts.put(faculty, count + 1);
                    }
                }

                // Create a list of BarEntry objects for the faculty counts
                List<BarEntry> facultyCountsData = new ArrayList<>();
                int i = 0;
                for (String faculty : facultyCounts.keySet()) {
                    Integer count = facultyCounts.get(faculty);
                    if (count != null) {
                        facultyCountsData.add(new BarEntry(i++, count));
                    }
                }

                // Create a BarDataSet with the faculty counts data and a label
                BarDataSet dataSet = new BarDataSet(facultyCountsData, "Faculty Counts");

                // Set the color for the bars
                dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

                // Set labels for each bar in the chart
                ArrayList<String> labels = new ArrayList<>();
                for (int j = 0; j < facultyCounts.size(); j++) {
                    labels.add((String) facultyCounts.keySet().toArray()[j]);
                }

                // Create a BarData object with the BarDataSet
                BarData barData = new BarData(dataSet);

                barData.setValueTextSize(16f);

                // Set the bar width
                barData.setBarWidth(0.5f);

                // Get the BarChart view from the layout
                BarChart barChart = view.findViewById(R.id.faculty_bar_chart);
                barChart.getDescription().setEnabled(false);
                barChart.setDrawValueAboveBar(false);
                barChart.setMaxVisibleValueCount(60);
                barChart.setPinchZoom(false);
                barChart.setDrawGridBackground(false);

                // Set up the x-axis
                XAxis xAxis = barChart.getXAxis();
                xAxis.setDrawGridLines(false);
                xAxis.setDrawAxisLine(false);
                xAxis.setDrawLabels(false);
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

                // Hide the legend
                Legend legend = barChart.getLegend();
                legend.setEnabled(false);

                // Set labels for each bar in the chart
                barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
                barChart.getXAxis().setDrawLabels(true);
                barChart.getXAxis().setLabelCount(labels.size());

                // Set up the y-axis
                YAxis leftAxis = barChart.getAxisLeft();
                leftAxis.setDrawGridLines(false);
                leftAxis.setAxisMinimum(0f);
                leftAxis.setGranularityEnabled(true);
                leftAxis.setGranularity(1f);
                leftAxis.setLabelCount(5);

                YAxis rightAxis = barChart.getAxisRight();
                rightAxis.setDrawGridLines(false);
                rightAxis.setEnabled(false);

                // Set the chart data and refresh the chart
                barChart.setData(barData);
                barChart.invalidate();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
            }
        });

        //method for event location
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, Integer> locationCounts = new HashMap<>();

                // loop through the event data
                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                    String location = eventSnapshot.child("eventLocation").getValue(String.class);

                    if (location != null) {
                        Integer count = locationCounts.get(location);
                        if (count == null) {
                            count = 0;
                        }
                        locationCounts.put(location, count + 1);
                    }
                }

                List<BarEntry> entries = new ArrayList<>();
                List<String> labels = new ArrayList<>();
                int i = 0;
                for (String location : locationCounts.keySet()) {
                    Integer count = locationCounts.get(location);
                    if (count != null) {
                        entries.add(new BarEntry(i, count));
                        labels.add(location);
                        i++;
                    }
                }

                BarDataSet dataSet = new BarDataSet(entries, "Location Counts");
                dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

                BarData data = new BarData(dataSet);
                data.setValueTextSize(16f);
                data.setBarWidth(0.5f);

                BarChart chart = view.findViewById(R.id.locationBarChart);
                chart.setData(data);
                chart.getDescription().setEnabled(false);
                chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
                chart.getXAxis().setGranularity(1f);
                chart.getXAxis().setGranularityEnabled(true);
                chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                chart.animateY(1000);
                chart.invalidate();

                // Get the X and Y axis objects from the chart
                XAxis xAxis = chart.getXAxis();
                YAxis yAxis = chart.getAxisLeft();

                // Disable the grid lines
                xAxis.setDrawGridLines(false);
                yAxis.setDrawGridLines(false);

                // Get the legend object from the chart
                Legend legend = chart.getLegend();

                // Disable the legend
                legend.setEnabled(false);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // handle errors
            }
        });

        return view;
    }

    private List<PieEntry> convertEntriesToPieEntries(List<Entry> entries) {
        List<PieEntry> pieEntries = new ArrayList<>();
        for (Entry entry : entries) {
            pieEntries.add(new PieEntry(entry.getY(), entry.getData().toString()));
        }
        return pieEntries;
    }
}

//method for completed events
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                List<Integer> completedEventsCount = new ArrayList<>();
//                int count = 0;
//
//                for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
//                    // Retrieve the event data
//                    Object eventDateObject = eventSnapshot.child("eventDate").getValue();
//                    if (eventDateObject instanceof Long) {
//                        long eventDate = (Long) eventDateObject;
//                        if (eventDate < System.currentTimeMillis()) {
//                            count++;
//                        }
//                    }
//                }
//                completedEventsCount.add(count);
//
//                // Use the completedEventsCount data to populate the bar chart
//                // Get the BarChart view from the layout
//                BarChart barChart = view.findViewById(R.id.barChart);
//                barChart.getDescription().setEnabled(false);
//                barChart.setDrawValueAboveBar(false);
//                barChart.setMaxVisibleValueCount(60);
//                barChart.setPinchZoom(false);
//                barChart.setDrawGridBackground(false);
//
//                // Create a list of BarEntry objects for the completed events count
//                List<BarEntry> completedEventsData = new ArrayList<>();
//                for (int i = 0; i < completedEventsCount.size(); i++) {
//                    completedEventsData.add(new BarEntry(i, completedEventsCount.get(i)));
//                }
//
//                // Create a BarDataSet with the completed events data and a label
//                BarDataSet dataSet = new BarDataSet(completedEventsData, "Completed Events");
//
//                // Set the color for the bars
//                dataSet.setColor(Color.BLUE);
//
//                // Create a BarData object with the BarDataSet
//                BarData barData = new BarData(dataSet);
//
//                barData.setValueTextSize(16f);
//
//                // Set the bar width
//                barData.setBarWidth(0.5f);
//
//                // Set the X-axis labels with the event names
//                XAxis xAxis = barChart.getXAxis();
//                xAxis.setGranularity(1f);
//                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//                xAxis.setDrawLabels(false);
//
//                // Set up the y-axis
//                YAxis leftAxis = barChart.getAxisLeft();
//                leftAxis.setDrawGridLines(true);
//                leftAxis.setAxisMinimum(0f);
//                leftAxis.setGranularityEnabled(true);
//                leftAxis.setGranularity(1f);
//                leftAxis.setLabelCount(5);
//
//                YAxis rightAxis = barChart.getAxisRight();
//                rightAxis.setDrawGridLines(false);
//                rightAxis.setEnabled(false);
//
//                // Set the chart data and refresh the chart
//                barChart.setData(barData);
//                barChart.invalidate();
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                // Handle any errors
//            }
//        });