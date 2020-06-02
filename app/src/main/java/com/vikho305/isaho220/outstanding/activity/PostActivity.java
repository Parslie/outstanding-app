package com.vikho305.isaho220.outstanding.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.vikho305.isaho220.outstanding.CustomJsonObjectRequest;
import com.vikho305.isaho220.outstanding.OnClickCallback;
import com.vikho305.isaho220.outstanding.R;
import com.vikho305.isaho220.outstanding.database.Post;
import com.vikho305.isaho220.outstanding.database.User;
import com.vikho305.isaho220.outstanding.fragment.PostFragment;
import com.vikho305.isaho220.outstanding.viewmodel.PostViewModel;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PostActivity extends AuthorizedActivity implements OnClickCallback, View.OnClickListener {

    private Button backButton;
    private PostFragment postFragment;

    private PostViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        // Get views
        backButton = findViewById(R.id.post_back);
        postFragment = (PostFragment) getSupportFragmentManager().findFragmentById(R.id.post_postFrag);

        // Init view model
        viewModel = new ViewModelProvider(this).get(PostViewModel.class);
        viewModel.getPost().observe(this, new Observer<Post>() {
            @Override
            public void onChanged(Post post) {
                postFragment.updateDetails(post);
            }
        });

        // Init activity
        Intent intent = getIntent();
        Post post = intent.getParcelableExtra("post");
        viewModel.setPost(post);

        // Init listeners
        backButton.setOnClickListener(this);
        postFragment.setOnClickCallback(this);
    }

    private void goToAuthor() {
        User user = viewModel.getAuthor().getValue();
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("user", user);
        goToActivity(intent);
    }

    @Override
    public void onClickCallback(String clickKey) {
        switch (clickKey) {
            case PostFragment.AUTHOR_CLICK_KEY:
                goToAuthor();
                break;
            case PostFragment.LIKE_CLICK_KEY:
                viewModel.toggleLikePost(getApplicationContext(), getAuthToken());
                break;
            case PostFragment.DISLIKE_CLICK_KEY:
                viewModel.toggleDislikePost(getApplicationContext(), getAuthToken());
                break;
        }
    }

    @Override
    public void onClick(View v) {
        if (v == backButton) {
            finish();
        }
    }
}
