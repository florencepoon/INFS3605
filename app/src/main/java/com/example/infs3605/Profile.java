package com.example.infs3605;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class Profile extends Fragment {
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private AlertDialog logoutDialog;

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
                logout();
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

    private void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mAuth.signOut();
                Intent intent = new Intent(getActivity(), LandingPage.class);
                startActivity(intent);
                getActivity().finish();
                // show a success message to the user
                Toast.makeText(getContext(), "You have been logged out successfully.", Toast.LENGTH_SHORT).show();
                dialog.dismiss(); // dismiss the dialog
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); // dismiss the dialog
            }
        });

        logoutDialog = builder.create();
        logoutDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                logoutDialog = null;
            }
        });
        logoutDialog.show();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (logoutDialog != null && logoutDialog.isShowing()) {
            logoutDialog.dismiss();
        }
    }
}
