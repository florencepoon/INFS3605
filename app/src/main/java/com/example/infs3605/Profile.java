package com.example.infs3605;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.google.firebase.auth.FirebaseAuth;

public class Profile extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    public void logOut(View view) {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getActivity(), LandingPage.class);
        startActivity(intent);
    }

}