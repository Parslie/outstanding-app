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

public class ProfileActivity extends AuthorizedActivity {

    private static final int EDIT_REQUEST = 0;

    private View root;
    private TextView usernameView;
    private ImageView profilePictureView;
    private TextView descriptionView;
    private UserViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Get layout views
        root = findViewById(R.id.profile_root);
        usernameView = findViewById(R.id.profile_username);
        profilePictureView = findViewById(R.id.profile_picture);
        descriptionView = findViewById(R.id.profile_description);
        Button editProfileButton = findViewById(R.id.profile_editProfile);
        Button editUserButton = findViewById(R.id.profile_editUser);
        Button followersButton = findViewById(R.id.profile_followers);
        Button followingsButton = findViewById(R.id.profile_followings);
        Button backButton = findViewById(R.id.profile_back);

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
            public void onChanged(RoundedBitmapDrawable roundedBitmapDrawable) {
                profilePictureView.setImageDrawable(roundedBitmapDrawable);
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
            viewModel.setUser(user);
        else if (viewModel.getUser().getValue() == null) // Prevents unnecessary server calls
            viewModel.fetchUser(getApplicationContext(), getAuthUserId(), getAuthToken());

        // Init listeners
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        followersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToFollowers();
            }
        });
        followingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToFollowings();
            }
        });
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProfile();
            }
        });
        editUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editUser();
            }
        });
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == EDIT_REQUEST) {
            assert data != null;
            User user = data.getParcelableExtra("user");
            viewModel.setUser(user);
        }
    }
}
