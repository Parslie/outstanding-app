package com.vikho305.isaho220.outstanding.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.vikho305.isaho220.outstanding.R;
import com.vikho305.isaho220.outstanding.database.User;
import com.vikho305.isaho220.outstanding.viewmodel.UserViewModel;

public class ProfileActivity extends AuthorizedActivity implements View.OnClickListener {

    private static final int EDIT_REQUEST = 0;

    private View root;
    private ImageView profilePictureView;
    private TextView usernameView, descriptionView;

    private Button editProfileButton, editUserButton;
    private Button followersButton, followingsButton;
    private Button backButton;

    private UserViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Get layout views
        root = findViewById(R.id.profile_root);
        profilePictureView = findViewById(R.id.profile_picture);
        usernameView = findViewById(R.id.profile_username);
        descriptionView = findViewById(R.id.profile_description);
        editProfileButton = findViewById(R.id.profile_editProfile);
        editUserButton = findViewById(R.id.profile_editUser);
        followersButton = findViewById(R.id.profile_followers);
        followingsButton = findViewById(R.id.profile_followings);
        backButton = findViewById(R.id.profile_back);

        // Init view model
        viewModel = new ViewModelProvider(this).get(UserViewModel.class);
        viewModel.getUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                usernameView.setText(user.getUsername());
                descriptionView.setText(user.getDescription());
                // TODO: set follower and following count
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

        if (user != null) // Prevents unnecessary server calls
            viewModel.setUser(getApplicationContext(), user);
        else if (viewModel.getUser().getValue() == null) // Prevents unnecessary server calls
            viewModel.fetchUser(getApplicationContext(), getAuthUserId(), getAuthToken());

        // Init listeners
        backButton.setOnClickListener(this);
        followersButton.setOnClickListener(this);
        followingsButton.setOnClickListener(this);
        editProfileButton.setOnClickListener(this);
        editUserButton.setOnClickListener(this);
    }

    private void goToFollowers() {
        Intent intent = new Intent(this, FollowerActivity.class);
        intent.putExtra("user", viewModel.getUser().getValue());
        goToActivity(intent);
    }

    private void goToFollowings() {
        Intent intent = new Intent(this, FollowingActivity.class);
        intent.putExtra("user", viewModel.getUser().getValue());
        goToActivity(intent);
    }

    private void editProfile() {
        Intent intent = new Intent(this, EditProfileActivity.class);
        intent.putExtra("user", viewModel.getUser().getValue());
        goToActivityForResult(intent, EDIT_REQUEST);
    }

    private void editUser() {
        Intent intent = new Intent(this, EditProfileActivity.class); // TODO: create EditUserActivity
        intent.putExtra("user", viewModel.getUser().getValue());
        goToActivityForResult(intent, EDIT_REQUEST);
    }

    // Listeners
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == EDIT_REQUEST) {
            assert data != null;
            User user = data.getParcelableExtra("user");
            viewModel.setUser(getApplicationContext(), user);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == backButton) {
            finish();
        }
        else if (v == editProfileButton) {
            editProfile();
        }
        else if (v == editUserButton) {
            editUser();
        }
        else if (v == followersButton) {
            goToFollowers();
        }
        else if (v == followingsButton) {
            goToFollowings();
        }
    }
}
