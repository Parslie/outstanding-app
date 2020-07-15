package com.vikho305.isaho220.outstanding;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.mapbox.mapboxsdk.Mapbox;
import com.vikho305.isaho220.outstanding.database.User;

public class MainActivity extends AuthorizedActivity implements View.OnClickListener {

    private Button profileButton, postButton, feedButton;

    private Fragment currentFragment;
    private UserFragment userFragment;
    private MapFragment mapFragment;
    private FeedFragment feedFragment;

    private UserViewModel userViewModel;
    private MapViewModel mapViewModel; // TODO: implement view models here, and update fragments accordingly
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
        // TODO: implement

        feedViewModel = new ViewModelProvider(this).get(FeedViewModel.class);
        // TODO: implement

        // Init activity
        currentFragment = mapFragment;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_container, currentFragment).commit();

        // Init listeners
        profileButton.setOnClickListener(this);
        postButton.setOnClickListener(this);
        feedButton.setOnClickListener(this);
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
    }
}