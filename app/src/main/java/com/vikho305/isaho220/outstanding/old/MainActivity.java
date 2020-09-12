package com.vikho305.isaho220.outstanding.old;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.mapbox.mapboxsdk.Mapbox;
import com.vikho305.isaho220.outstanding.R;
import com.vikho305.isaho220.outstanding.old.database.Post;
import com.vikho305.isaho220.outstanding.old.database.User;

import java.util.List;

public class MainActivity extends AuthorizedActivity implements View.OnClickListener, LocationListener {

    private static final int POST_CREATION_REQUEST = 10;
    private static final int POST_RADIUS = 1000; // coordinate units
    private static final int LOCATION_UPDATE_INTERVAL = 2000; // milliseconds
    private static final int LOCATION_UPDATE_DISTANCE = 10; // meters

    private double latitude, longitude;
    private LocationManager locationManager;

    private Button profileButton, postButton, feedButton;

    private Fragment currentFragment;
    private UserFragment userFragment;
    private MapFragment mapFragment;
    private FeedFragment feedFragment;

    private UserViewModel userViewModel;
    private MapViewModel mapViewModel;
    private FeedViewModel feedViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, "pk.eyJ1IjoiaXNha2hvcnZhdGgiLCJhIjoiY2s4dnZ3NWoxMDN1azNncXBuNWl0YWttdSJ9.ksNJFJjaQsqnIFzKy3ZCkg");
        setContentView(R.layout.activity_main);

        // Get layout views
        profileButton = findViewById(R.id.main_profileBtn);
        postButton = findViewById(R.id.main_postBtn);
        feedButton = findViewById(R.id.main_feedBtn);

        // Init fragments
        userFragment = UserFragment.newInstance();
        mapFragment = MapFragment.newInstance();
        feedFragment = FeedFragment.newInstance();

        // Init view models
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                userFragment.setUser(user);
            }
        });
        userViewModel.fetchUser(this, getAuthToken(), getAuthUserId());

        mapViewModel = new ViewModelProvider(this).get(MapViewModel.class);
        mapViewModel.getPosts().observe(this, new Observer<List<Post>>() {
            @Override
            public void onChanged(List<Post> posts) {
                mapFragment.setPostSymbols(posts);
            }
        });
        mapViewModel.fetchPosts(this, getAuthToken(), POST_RADIUS); // TODO: implement in location listener as well

        feedViewModel = new ViewModelProvider(this).get(FeedViewModel.class);
        // TODO: implement

        // Init activity
        currentFragment = mapFragment;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_container, currentFragment).commit();

        initLocationManager();

        // Init listeners
        profileButton.setOnClickListener(this);
        postButton.setOnClickListener(this);
        feedButton.setOnClickListener(this);
    }

    private void initLocationManager() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
            return;
        }
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_UPDATE_INTERVAL, LOCATION_UPDATE_DISTANCE, this);
        // TODO: check if location is enabled
    }

    //////////////////////
    // Fragment navigation

    private void showUserFragment() {
        currentFragment = userFragment;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_from_left, R.anim.slide_to_right);
        transaction.replace(R.id.main_container, currentFragment).commit();
    }

    private void hideUserFragment() {
        currentFragment = mapFragment;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_from_right, R.anim.slide_to_left);
        transaction.replace(R.id.main_container, currentFragment).commit();
    }

    private void showFeedFragment() {
        currentFragment = feedFragment;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_from_right, R.anim.slide_to_left);
        transaction.replace(R.id.main_container, currentFragment).commit();
    }

    private void hideFeedFragment() {
        currentFragment = mapFragment;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_from_left, R.anim.slide_to_right);
        transaction.replace(R.id.main_container, currentFragment).commit();
    }

    ////////////
    // Listeners

    @Override
    public void onClick(View v) {
        if (v == profileButton && currentFragment != userFragment) {
            showUserFragment();
        }
        else if (v == postButton && currentFragment == userFragment) {
            hideUserFragment();
        }
        else if (v == postButton && currentFragment == feedFragment) {
            hideFeedFragment();
        }
        else if (v == feedButton && currentFragment != feedFragment) {
            showFeedFragment();
        }
        else if (v == postButton && currentFragment == mapFragment) {
            Intent intent = new Intent(this, PostCreationActivity.class);
            intent.putExtra("latitude", latitude);
            intent.putExtra("longitude", longitude);
            goToActivityForResult(intent, POST_CREATION_REQUEST);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLatitude();
        userViewModel.postCoordinates(this, getAuthToken(), latitude, longitude);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }
    @Override
    public void onProviderEnabled(String provider) {

    }
    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        for (int i = 0; i < permissions.length; i++) {
            String permission = permissions[i];
            int result = grantResults[i];

            if (permission.equals(Manifest.permission.ACCESS_FINE_LOCATION) && result == PackageManager.PERMISSION_GRANTED) {
                initLocationManager();
            }
            else {
                finish(); // TODO: add better permission error-handling
            }
        }
    }

    /////////////////////
    // Life cycle methods

    @Override
    protected void onResume() {
        super.onResume();
        userViewModel.fetchUser(this, getAuthToken(), getAuthUserId());
    }
}