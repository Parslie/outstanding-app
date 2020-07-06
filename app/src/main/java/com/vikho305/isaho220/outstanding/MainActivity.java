package com.vikho305.isaho220.outstanding;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mapbox.mapboxsdk.Mapbox;

public class MainActivity extends AuthorizedActivity implements View.OnClickListener {

    private Button profileButton, postButton, feedButton;

    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, "pk.eyJ1IjoiaXNha2hvcnZhdGgiLCJhIjoiY2s4dnZ3NWoxMDN1azNncXBuNWl0YWttdSJ9.ksNJFJjaQsqnIFzKy3ZCkg");
        setContentView(R.layout.activity_main);

        // Get layout views
        profileButton = findViewById(R.id.main_profileBtn);
        postButton = findViewById(R.id.main_postBtn);
        feedButton = findViewById(R.id.main_feedBtn);

        // Init activity
        currentFragment = MapFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_container, currentFragment).commit();

        // Init listeners
        profileButton.setOnClickListener(this);
        postButton.setOnClickListener(this);
        feedButton.setOnClickListener(this);
    }

    private void showUserFragment() {
        currentFragment = UserFragment.newInstance(getAuthUserId());
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_from_left, R.anim.slide_to_right);
        transaction.replace(R.id.main_container, currentFragment).commit();
    }

    private void hideUserFragment() {
        currentFragment = MapFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_from_right, R.anim.slide_to_left);
        transaction.replace(R.id.main_container, currentFragment).commit();
    }

    private void showFeedFragment() {
        currentFragment = FeedFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_from_right, R.anim.slide_to_left);
        transaction.replace(R.id.main_container, currentFragment).commit();
    }

    private void hideFeedFragment() {
        currentFragment = MapFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_from_left, R.anim.slide_to_right);
        transaction.replace(R.id.main_container, currentFragment).commit();
    }

    @Override
    public void onClick(View v) {
        if (v == profileButton && !(currentFragment instanceof UserFragment)) {
            showUserFragment();
        }
        else if (v == postButton && currentFragment instanceof UserFragment) {
            hideUserFragment();
        }
        else if (v == postButton && currentFragment instanceof FeedFragment) {
            hideFeedFragment();
        }
        else if (v == feedButton && !(currentFragment instanceof FeedFragment)) {
            showFeedFragment();
        }
    }
}