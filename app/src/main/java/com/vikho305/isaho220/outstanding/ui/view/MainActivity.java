package com.vikho305.isaho220.outstanding.ui.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mapbox.mapboxsdk.Mapbox;
import com.vikho305.isaho220.outstanding.R;
import com.vikho305.isaho220.outstanding.data.repositories.PreferenceRepository;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private FrameLayout fragmentContainer;
    private BottomNavigationView navigationView;

    private ProfileFragment profileFragment;
    private MapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, "pk.eyJ1IjoiaXNha2hvcnZhdGgiLCJhIjoiY2s4dnZ3NWoxMDN1azNncXBuNWl0YWttdSJ9.ksNJFJjaQsqnIFzKy3ZCkg");
        setContentView(R.layout.activity_main);

        PreferenceRepository preferences = new PreferenceRepository(this);
        profileFragment = ProfileFragment.newInstance(preferences.getAuthUserId());
        mapFragment = MapFragment.newInstance();

        fragmentContainer = findViewById(R.id.mainContainer);
        navigationView = findViewById(R.id.mainNavBar);

        navigationView.setOnNavigationItemSelectedListener(this);
        navigationView.setSelectedItemId(R.id.menuMap);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuMap:
                getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, mapFragment).commit();
                return true;
            case R.id.menuProfile:
                getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, profileFragment).commit();
                return true;
        }
        return false;
    }
}