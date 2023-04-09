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
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
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
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Analytics extends Fragment {

    DatabaseReference myRef;
    private PieChart piechart;

    TextView eventsCompletedYTD,topOrganiserLabel1, topOrganiserLabel2, topOrganiserLabel3, topOneOrganiserName, toptwoOrganiserName, topThreeOrganiserName, topOneOrganiserCount, topTwoOrganiserCount, topThreeOrganiserCount, eventsThisWeek, eventsUpcomingEOY;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_analytics, container, false);
        topOrganiserLabel1 = view.findViewById(R.id.top_one_organiser_label);
        topOrganiserLabel2= view.findViewById(R.id.top_two_organiser_label);
        topOrganiserLabel3= view.findViewById(R.id.top_three_organiser_label);
        topOneOrganiserName= view.findViewById(R.id.top_one_organiser_name);
        toptwoOrganiserName= view.findViewById(R.id.top_two_organiser_name2);
        topThreeOrganiserName= view.findViewById(R.id.top_three_organiser_name);
        topOneOrganiserCount= view.findViewById(R.id.top_one_organiser_count);
        topTwoOrganiserCount= view.findViewById(R.id.top_two_organiser_count);
        topThreeOrganiserCount= view.findViewById(R.id.top_three_organiser_count);
        eventsCompletedYTD = view.findViewById(R.id.events_completed_ytd);
        eventsThisWeek = view.findViewById(R.id.events_this_week);
        eventsUpcomingEOY = view.findViewById(R.id.events_upcoming_eoy);

        //method for counting the number of completed events
        // Get a reference to the Firebase database
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        // Get a reference to the "events" node in the database
        myRef = FirebaseDatabase.getInstance().getReference().child("Events");

        // Get the current date and time
        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();

        // Attach a listener to the events reference to read the event data
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int completedEvents = 0;
                // Iterate over all events in the snapshot
                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                    // Get the event date from the snapshot
                    Long eventDateTime = eventSnapshot.child("eventDate").child("time").getValue(Long.class);

                    // Compare the event date with today's date
                    if (eventDateTime != null) {
                        Date eventDate = new Date(eventDateTime);
                        if (eventDate.before(today)) {
                            // Increment the completed events counter
                            completedEvents++;
                        }
                    }
                }
                //Displaying final count after all events have been counted
                eventsCompletedYTD.setText(String.valueOf(completedEvents));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors that occur while reading from the database
            }
        });

        //method for counting events
        // Get a reference to the events node in Firebase
        myRef = FirebaseDatabase.getInstance().getReference().child("Events");

        // Get the current date and time
        Date todayDate = new Date();

        // Get the date of one week from now
        Calendar calendarYear = Calendar.getInstance();
        calendarYear.setTime(todayDate);
        calendarYear.add(Calendar.DATE, 7);
        Date nextWeek = calendarYear.getTime();

        // Attach a listener to the events reference to read the event data
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int upcomingEventsWeek = 0;
                int upcomingEventsYear = 0;

                // Iterate over all events in the snapshot
                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                    // Get the event date from the snapshot
                    Long eventTime = eventSnapshot.child("eventDate").child("time").getValue(Long.class);

                    if (eventTime != null) {
                        Date eventDate = new Date(eventTime);
                        // Compare the event date with today's date
                        if (eventDate != null && eventDate.after(today)) {
                            // Check if the event is within the next week
                            if (eventDate.before(nextWeek)) {
                                // Increment the upcoming events within the next week counter
                                upcomingEventsWeek++;
                            }

                            // Check if the event is within the next year
                            Calendar eventCalendar = Calendar.getInstance();
                            eventCalendar.setTime(eventDate);
                            int eventYear = eventCalendar.get(Calendar.YEAR);
                            int currentYear = calendar.get(Calendar.YEAR);
                            if (eventYear == currentYear) {
                                // Increment the upcoming events within the current year counter
                                upcomingEventsYear++;
                            }
                        }
                    }

                }
                //Displaying final counts after all events have been counted
                eventsThisWeek.setText(String.valueOf(upcomingEventsWeek));
                eventsUpcomingEOY.setText(String.valueOf(upcomingEventsYear));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException());
            }
        });

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
                int totalEvents = 0;
                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                    String eventType = eventSnapshot.child("eventCategory").getValue(String.class);
                    if (eventData.containsKey(eventType)) {
                        eventData.put(eventType, eventData.get(eventType) + 1);
                    } else {
                        eventData.put(eventType, 1);
                    }
                    totalEvents++;
                }


                // Convert the event data to a List<Entry> for use in MPCharts
                List<Entry> entries = new ArrayList<>();
                for (Map.Entry<String, Integer> entry : eventData.entrySet()) {
                    String eventType = entry.getKey();
                    Integer eventCount = entry.getValue();
                    totalEvents += eventCount;
                    entries.add(new Entry(0, eventCount.floatValue(), eventType));
                }

                // Create a pie chart using MPCharts
                PieChart pieChart = view.findViewById(R.id.testChart);
                // Disable chart description
                Description description = new Description();
                description.setText("");
                pieChart.setDescription(description);
                PieDataSet dataSet = new PieDataSet(convertEntriesToPieEntries(entries), "Events");
                dataSet.setValueTextSize(12f);
                dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                dataSet.setValueLineColor(Color.BLACK);
                dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
                PieData data = new PieData(dataSet);
                data.setValueFormatter(new PercentFormatter());
                pieChart.setData(data);
                pieChart.animateXY(1000, 1000);
                pieChart.invalidate();

                // Hide the legend
                Legend legend = pieChart.getLegend();
                legend.setEnabled(false);

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

        //Method for generating a list of the top event organisers
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String, Integer> eventTypeCount = new HashMap<>();

                // Count the number of events in each location
                for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
                    String eventOrganiser = eventSnapshot.child("eventOrganiser").getValue(String.class);
                    eventTypeCount.put(eventOrganiser, eventTypeCount.getOrDefault(eventOrganiser, 0) + 1);
                }

                // Sort the event locations by count in descending order
                List<Map.Entry<String, Integer>> typeList = new ArrayList<>(eventTypeCount.entrySet());
                Collections.sort(typeList, (o1, o2) -> o2.getValue().compareTo(o1.getValue()));

                // Retrieve the top 3 event locations
                List<String> topOrganisers = new ArrayList<>();
                for (int i = 0; i < Math.min(typeList.size(), 3); i++) {
                    topOrganisers.add(typeList.get(i).getKey());
                }

                // Display the top 3 event organizers and counts in TextViews
                for (int i = 0; i < topOrganisers.size(); i++) {
                    String eventOrganizer = topOrganisers.get(i);
                    int count = eventTypeCount.get(eventOrganizer);
                    if (i == 0) {
                        topOneOrganiserName.setText(eventOrganizer);
                        topOneOrganiserCount.setText(String.valueOf(count));
                    } else if (i == 1) {
                        toptwoOrganiserName.setText(eventOrganizer);
                        topTwoOrganiserCount.setText(String.valueOf(count));
                    } else if (i == 2) {
                        topThreeOrganiserName.setText(eventOrganizer);
                        topThreeOrganiserCount.setText(String.valueOf(count));
                    }
                }
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

                barData.setValueTextSize(14f);

                // Set the bar width
                barData.setBarWidth(0.5f);

                // Get the BarChart view from the layout
                BarChart barChart = view.findViewById(R.id.faculty_bar_chart);
                barChart.getDescription().setEnabled(false);
                barChart.setDrawValueAboveBar(true);
                barChart.setMaxVisibleValueCount(60);
                barChart.setPinchZoom(false);
                barChart.setDrawGridBackground(false);

                // Set up the x-axis
                XAxis xAxis = barChart.getXAxis();
                xAxis.setDrawGridLines(false);
                xAxis.setDrawAxisLine(false);
                xAxis.setDrawLabels(true);
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
                leftAxis.setDrawGridLines(true);
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
                data.setValueTextSize(14f);
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
                YAxis rightAxis = chart.getAxisRight();

                // Format the Y axis labels to show whole numbers
                yAxis.setGranularity(1f);
                yAxis.setValueFormatter(new ValueFormatter() {
                    @Override
                    public String getFormattedValue(float value) {
                        // Round the value to the nearest whole number
                        int roundedValue = Math.round(value);
                        // Convert the rounded value to a string
                        return String.valueOf(roundedValue);
                    }
                });

                // Disable the grid lines
                xAxis.setDrawGridLines(false);
                yAxis.setDrawGridLines(true);
                rightAxis.setEnabled(false);

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