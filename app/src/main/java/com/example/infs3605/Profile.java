package com.example.infs3605;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.firebase.auth.FirebaseAuth;

public class Profile extends Fragment {
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        //Setting display name
        String displayName = mAuth.getCurrentUser().getDisplayName();
        TextView name = view.findViewById(R.id.profileName);
        name.setText(displayName);

        //Help Support Button
        Button helpButton = view.findViewById(R.id.helpButton);
        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "recipient@example.com", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Help Support");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
        });

        //Logout Button
        Button logout = view.findViewById(R.id.logoutButton);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(getActivity(), LandingPage.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        TextView accessLevel = view.findViewById(R.id.AccessLevel);

        //Determining access level
        if (mAuth.getCurrentUser().getUid().equals("koGVEACbIRZ8JRLmzGGKgvfhWjs1")) {
            accessLevel.setText("Admin");
        } else {
            accessLevel.setText("Staff");
        }

        //Back to dashboard button
        ImageView backButton = view.findViewById(R.id.leftArrowProfile);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
