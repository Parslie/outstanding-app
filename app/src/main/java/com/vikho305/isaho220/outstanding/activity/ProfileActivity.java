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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.vikho305.isaho220.outstanding.R;
import com.vikho305.isaho220.outstanding.User;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AuthorizedActivity{

    private String userId;
    private User user;

    private View rootView;
    private ImageView profilePicture;
    private TextView usernameText, descriptionText;
    private TextView followerText, followeeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();
        User user = intent.getParcelableExtra("user");  // Get user to reduce server calls
        userId = intent.getStringExtra("userId");

        profilePicture = findViewById(R.id.profilePicture);
        usernameText = findViewById(R.id.username);
        descriptionText = findViewById(R.id.description);
        followerText = findViewById(R.id.followersCount);
        followeeText = findViewById(R.id.followeeCount);
        rootView = profilePicture.getRootView();

        // TODO: make method for setting user (to be used here and in setProfileDetails)
        if (user != null) {
            setUser(user);
        }
        else {
            setProfileDetails();
        }

        setButtonListeners();
    }

    private void setButtonListeners() {
        Button editProfileButton = findViewById(R.id.editProfileButton); // TODO: disable if !selfUserId.equals(userId)
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                intent.putExtra("user", user);
                goToActivity(intent);
            }
        });

        Button editUserButton = findViewById(R.id.editUserButton); // TODO: disable if !selfUserId.equals(userId)
        editUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                intent.putExtra("user", user);
                goToActivity(intent);
            }
        });

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button followersButton = findViewById(R.id.followersButton);
        followersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToActivity(new Intent(ProfileActivity.this, FollowerActivity.class));
            }
        });

        Button followingButton = findViewById(R.id.followingButton);
        followingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToActivity(new Intent(ProfileActivity.this, FolloweeActivity.class));
            }
        });
    }

    private void setUser(User user) {
        this.user = user;

        float[] hsl = new float[]{
                360 * (float) user.getHue(),
                (float) user.getSaturation(),
                (float) user.getLightness()
        };
        System.out.println(Color.HSVToColor(hsl));
        rootView.setBackgroundColor(Color.HSVToColor(hsl));

        usernameText.setText(user.getUsername());
        descriptionText.setText(user.getDescription());

        if(user.getPicture() != null){
            byte[] byteData = Base64.decode(user.getPicture().getBytes(),Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(byteData, 0, byteData.length);
            profilePicture.setImageBitmap(bitmap);
        }

        // TODO: set follower and following count
        // TODO: set posts
    }

    private void setProfileDetails() {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                getResources().getString(R.string.get_user_url, userId),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("==========RESPONSE:" + response + "==========");

                        Gson gson = new Gson();
                        User user = gson.fromJson(String.valueOf(response), User.class);
                        setUser(user);

                        Toast.makeText(ProfileActivity.this, getResources().getString(R.string.generic_success), Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(ProfileActivity.this, getResources().getString(R.string.generic_error), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + getAuthToken());
                return headers;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }

}
