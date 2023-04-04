package com.example.infs3605;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

public class Events extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events, container, false);

        ImageView createEvent = view.findViewById(R.id.createEventButton);
        Button byName = view.findViewById(R.id.byNameButton);
        Button byType = view.findViewById(R.id.byTypeButton);
        Button byLocation = view.findViewById(R.id.byLocationButton);
        Button byOrganiser = view.findViewById(R.id.byOrganiserButton);

        //Add Event Button
        createEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddNewEvent.class);
                startActivity(intent);
            }
        });

        //by Name Button
        byName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EventsByNameRecyclerView.class);
                startActivity(intent);
            }
        });

        return view;
    }
}