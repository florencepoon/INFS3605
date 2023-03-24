package com.example.infs3605;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    Home home = new Home();
    Insights insights = new Insights();
    Profile profile = new Profile();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,home).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected( MenuItem item) {
                switch (item.getItemId()){
                    case R.id.Home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,home).commit();
                        return true;
                    case R.id.insights:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,insights).commit();
                        return true;
                    case R.id.profile:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,profile).commit();
                        return true;
                }

        BottomNavigationView mBottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        mBottomNavigationView.getMenu().findItem(R.id.Home).setChecked(true);

                return false;
            }
        });
    }
}


