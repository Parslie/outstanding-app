package com.vikho305.isaho220.outstanding;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.vikho305.isaho220.outstanding.database.User;

public class UserFragment extends Fragment implements View.OnClickListener {

    private ImageView pictureView;
    private TextView usernameView, descriptionView;
    private TextView followerCountView, followingCountView;
    private Button followButton, editAccountButton, editProfileButton;

    private User user;

    public static UserFragment newInstance() {
        return new UserFragment();
    }

    public void setUser(User user) {
        this.user = user;
    }

    private void updateViewDetails() {
        usernameView.setText(user.getUsername());
        descriptionView.setText(user.getDescription());

        followerCountView.setText(getString(R.string.follower_count, user.getFollowerCount()));
        followingCountView.setText(getString(R.string.following_count, user.getFollowingCount()));

        // Decode and round profile picture
        Bitmap bitmap;
        if (user.getPicture() == null) {
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.default_pfp);
        }
        else {
            byte[] bitmapBytes = Base64.decode(user.getPicture(), Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
        }
        RoundedBitmapDrawable roundedPicture = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
        roundedPicture.setCircular(true);
        pictureView.setImageDrawable(roundedPicture);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.user_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get layout views
        pictureView = view.findViewById(R.id.user_picture);
        usernameView = view.findViewById(R.id.user_username);
        descriptionView = view.findViewById(R.id.user_description);
        followerCountView = view.findViewById(R.id.user_followerCount);
        followingCountView = view.findViewById(R.id.user_followingCount);
        followButton = view.findViewById(R.id.user_followBtn);
        // editAccountButton = view.findViewById(R.id.user_picture);
        // editProfileButton = view.findViewById(R.id.user_picture);

        // Init fragment
        updateViewDetails();

        // Init listeners
        followButton.setOnClickListener(this);
        followerCountView.setOnClickListener(this);
        followingCountView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // TODO: add button functionality
    }
}