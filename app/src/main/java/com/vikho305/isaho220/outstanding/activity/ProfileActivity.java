package com.vikho305.isaho220.outstanding.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.vikho305.isaho220.outstanding.CustomJsonObjectRequest;
import com.vikho305.isaho220.outstanding.R;
import com.vikho305.isaho220.outstanding.database.Profile;
import com.vikho305.isaho220.outstanding.database.User;
import com.vikho305.isaho220.outstanding.viewmodel.UserViewModel;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ProfileActivity extends AuthorizedActivity implements View.OnClickListener {

    private static final int EDIT_REQUEST = 0;

    private View root;
    private ImageView profilePictureView;
    private TextView usernameView, descriptionView;
    private TextView followerCountView, followingCountView;

    private LinearLayout postLayout;

    private Button editProfileButton, editAccountButton;
    private Button followButton;
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
        followerCountView = findViewById(R.id.profile_followers);
        followingCountView = findViewById(R.id.profile_followings);

        postLayout = findViewById(R.id.profile_posts);

        editProfileButton = findViewById(R.id.profile_editProfile);
        editAccountButton = findViewById(R.id.profile_editAccount);
        followButton = findViewById(R.id.profile_follow);
        backButton = findViewById(R.id.profile_back);


        // Init view model
        viewModel = new ViewModelProvider(this).get(UserViewModel.class);
        viewModel.getUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                usernameView.setText(user.getUsername());
                followerCountView.setText(getResources().getString(R.string.follower_count, user.getFollowerCount()));
                followingCountView.setText(getResources().getString(R.string.following_count, user.getFollowingCount()));

                // If user is the one logged in
                if (user.getId().equals(getAuthUserId())) {
                    followButton.setVisibility(View.GONE);
                }
                else {
                    editProfileButton.setVisibility(View.GONE);
                    editAccountButton.setVisibility(View.GONE);

                    if(viewModel.getUser().getValue().isFollowing()){
                        String s = "unfollow";
                        followButton.setText(s);
                    }

                }
            }
        });
        viewModel.getProfile().observe(this, new Observer<Profile>() {
            @Override
            public void onChanged(Profile profile) {
                descriptionView.setText(profile.getDescription());
                root.setBackgroundColor(profile.getPrimaryColor());

                Bitmap pictureBitmap;
                if (profile.getPicture() == null) {
                    pictureBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.default_pfp);
                }
                else {
                    byte[] decodedPicture = Base64.decode(profile.getPicture(), Base64.DEFAULT);
                    pictureBitmap = BitmapFactory.decodeByteArray(decodedPicture, 0, decodedPicture.length);
                }

                RoundedBitmapDrawable roundedPicture = RoundedBitmapDrawableFactory.create(getResources(), pictureBitmap);
                roundedPicture.setCircular(true);
                profilePictureView.setImageDrawable(roundedPicture);
            }
        });

        // Init activity
        Intent intent = getIntent(); // TODO: send a user ID so you can view more profiles than your own
        User user = intent.getParcelableExtra("user");

        if (user != null) // Prevents unnecessary server calls
            viewModel.setUser(user);
        else if (viewModel.getUser().getValue() == null)
            viewModel.fetchUser(getApplicationContext(), getAuthUserId(), getAuthToken());

        // Init listeners
        backButton.setOnClickListener(this);
        followerCountView.setOnClickListener(this);
        followingCountView.setOnClickListener(this);
        editProfileButton.setOnClickListener(this);
        editAccountButton.setOnClickListener(this);
        followButton.setOnClickListener(this);
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
        Intent intent = new Intent(this, EditAccountActivity.class);
        intent.putExtra("user", viewModel.getUser().getValue());
        goToActivityForResult(intent, EDIT_REQUEST);
    }

    ////////////
    // Listeners
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == EDIT_REQUEST) {
            assert data != null;
            User user = data.getParcelableExtra("user");
            viewModel.setUser(user);
        }
    }

    private void follow(String userId){
        String url = getResources().getString(R.string.follow_url, userId);
        CustomJsonObjectRequest request = new CustomJsonObjectRequest(
                Request.Method.POST,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String s = "sent request";
                        followButton.setText(s);
                        followButton.setEnabled(false);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + getAuthToken());
                return headers;
            }
        };
        Volley.newRequestQueue(this).add(request);
    }

    private void unfollow(String userId){
        String url = getResources().getString(R.string.follow_url, userId);
        CustomJsonObjectRequest request = new CustomJsonObjectRequest(
                Request.Method.DELETE,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String s = "follow";
                        followButton.setText(s);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + getAuthToken());
                return headers;
            }
        };
        Volley.newRequestQueue(this).add(request);
    }


    @Override
    public void onClick(View v) {
        if (v == backButton) {
            finish();
        }
        else if (v == editProfileButton) {
            editProfile();
        }
        else if (v == editAccountButton) {
            editUser();
        }
        else if (v == followerCountView) {
            goToFollowers();
        }
        else if (v == followingCountView) {
            goToFollowings();
        }
        else if (v == followButton) {
            if(Objects.requireNonNull(viewModel.getUser().getValue()).isFollowing()){
                unfollow(Objects.requireNonNull(viewModel.getUser().getValue()).getId());
            }
            else{
                follow(Objects.requireNonNull(viewModel.getUser().getValue()).getId());
            }
        }
    }
}
