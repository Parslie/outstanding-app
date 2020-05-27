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
        Intent intent = getIntent();
        User user = intent.getParcelableExtra("user");

        if (user != null)
            viewModel.setUser(user);
        // TODO: create error case for not having user

        // Init listeners
        followButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
    }

    private void sendFollowRequest(String userId){
        String url = getResources().getString(R.string.follow_url, userId);
        CustomJsonObjectRequest request = new CustomJsonObjectRequest(
                Request.Method.POST,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        followButton.setText((CharSequence) "Sent Request");
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

    @Override
    public void onClick(View v) {
        if (v == followButton) {
            // TODO: add follow and unfollow functionality
            Intent intent = getIntent();
            User user = intent.getParcelableExtra("user");
            String id = Objects.requireNonNull(user).getId();
            sendFollowRequest(id);
        }
        else if (v == backButton) {
            finish();
        }
    }
}
