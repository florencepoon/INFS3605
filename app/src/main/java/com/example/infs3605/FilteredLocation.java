//package com.example.infs3605;
//
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.Query;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.ArrayList;
//
//public class FilteredLocation extends AppCompatActivity {
//
//    private RecyclerView mRecyclerView;
//    private EventAdapter mAdapter;
//    private TextView mLocationTextView;
//
//    private static final String TAG = "EventListActivity";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_event_list);
//
//        String location = getIntent().getStringExtra("location");
//
//        mLocationTextView = findViewById(R.id.locationTextView);
//        mLocationTextView.setText(location);
//
//        ArrayList<Event> eventList = new ArrayList<>();
//        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("events");
//
//        Query query = ref.orderByChild("eventLocation").equalTo(location);
//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                eventList.clear(); // clear the list before adding new items
//                for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
//                    Event event = eventSnapshot.getValue(Event.class);
//                    eventList.add(event);
//                }
//                mAdapter = new EventAdapter(eventList);
//                mRecyclerView.setAdapter(mAdapter); // set the adapter to the RecyclerView
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.d(TAG, "Error fetching data: " + error.getMessage());
//            }
//        });
//    }
//}
