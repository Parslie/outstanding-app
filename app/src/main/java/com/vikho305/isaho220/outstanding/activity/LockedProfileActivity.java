package com.vikho305.isaho220.outstanding.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.vikho305.isaho220.outstanding.R;
import com.vikho305.isaho220.outstanding.database.User;
import com.vikho305.isaho220.outstanding.viewmodel.UserViewModel;

public class LockedProfileActivity extends AuthorizedActivity implements View.OnClickListener {

    private UserViewModel viewModel;

    private View root;
    private ImageView profilePictureView;
    private TextView usernameView, descriptionView;

    private Button followButton, backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_locked);

        // Get layout views
        root = findViewById(R.id.lockedProfile_root);
        profilePictureView = findViewById(R.id.lockedProfile_picture);
        usernameView = findViewById(R.id.lockedProfile_username);
        descriptionView = findViewById(R.id.lockedProfile_description);
        followButton = findViewById(R.id.lockedProfile_follow);
        backButton = findViewById(R.id.lockedProfile_back);

        // Init view model
        viewModel = new ViewModelProvider(this).get(UserViewModel.class);
        viewModel.getUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                usernameView.setText(user.getUsername());
                descriptionView.setText(user.getDescription());
            }
        });
        viewModel.getUserPicture().observe(this, new Observer<RoundedBitmapDrawable>() {
            @Override
            public void onChanged(RoundedBitmapDrawable profilePicture) {
                profilePictureView.setImageDrawable(profilePicture);
            }
        });
        viewModel.getUserColor().observe(this, new Observer<float[]>() {
            @Override
            public void onChanged(float[] hsl) {
                root.setBackgroundColor(Color.HSVToColor(hsl));
            }
        });

        // Init activity
        Intent intent = getIntent();
        User user = intent.getParcelableExtra("user");

        if (user != null)
            viewModel.setUser(getApplicationContext(), user);
        // TODO: create error case for not having user

        // Init listeners
        followButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == followButton) {
            // TODO: add follow and unfollow functionality
        }
        else if (v == backButton) {
            finish();
        }
    }
}
