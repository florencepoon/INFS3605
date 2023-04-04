package com.example.infs3605;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dashboard extends Fragment {
    private PieChart piechart;
    private Button addEventButton;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;

    TextView dashboardGreeting;
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
        return v;
    }

    private List<PieEntry> convertEntriesToPieEntries(List<Entry> entries) {
        List<PieEntry> pieEntries = new ArrayList<>();
        for (Entry entry : entries) {
            pieEntries.add(new PieEntry(entry.getY(), entry.getData().toString()));
        }
        return pieEntries;
    }

//    private void setupPieChart() {
//        piechart.setDrawHoleEnabled(true);
//        piechart.setUsePercentValues(true);
//        piechart.setEntryLabelTextSize(12);
//        piechart.setEntryLabelColor(Color.BLACK);
//        piechart.setCenterText("Test Piechart");
//        piechart.setCenterTextSize(24);
//        piechart.getDescription().setEnabled(false);
//
//        Legend l = piechart.getLegend();
//        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
//        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
//        l.setOrientation(Legend.LegendOrientation.VERTICAL);
//        l.setDrawInside(false);
//        l.setEnabled(true);
//    }
//
//    private void loadPieChartData() {
//        ArrayList<PieEntry> entries = new ArrayList<>();
//        entries.add(new PieEntry(0.20f, "Test 1"));
//        entries.add(new PieEntry(0.15f, "Test 2"));
//        entries.add(new PieEntry(0.10f, "Test 3"));
//        entries.add(new PieEntry(0.25f, "Test 4"));
//        entries.add(new PieEntry(0.3f, "Test 5"));
//
//        ArrayList<Integer> colors = new ArrayList<>();
//        for (int color: ColorTemplate.MATERIAL_COLORS) {
//            colors.add(color);
//        }
//        for (int color: ColorTemplate.VORDIPLOM_COLORS) {
//            colors.add(color);
//        }
//
//        PieDataSet dataSet = new PieDataSet(entries, "test");
//        dataSet.setColors(colors);
//
//        PieData data = new PieData(dataSet);
//        data.setDrawValues(true);
//        data.setValueFormatter(new PercentFormatter(piechart));
//        data.setValueTextSize(12f);
//        data.setValueTextColor(Color.BLACK);
//
//        piechart.setData(data);
//        piechart.invalidate();
//    }
}