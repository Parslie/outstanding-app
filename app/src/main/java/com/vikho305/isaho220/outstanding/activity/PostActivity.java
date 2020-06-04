package com.vikho305.isaho220.outstanding.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Space;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.vikho305.isaho220.outstanding.fragment.CommentFragment;
import com.vikho305.isaho220.outstanding.OnClickCallback;
import com.vikho305.isaho220.outstanding.R;
import com.vikho305.isaho220.outstanding.database.Comment;
import com.vikho305.isaho220.outstanding.database.Post;
import com.vikho305.isaho220.outstanding.database.User;
import com.vikho305.isaho220.outstanding.fragment.PostFragment;
import com.vikho305.isaho220.outstanding.viewmodel.PostViewModel;

import org.json.JSONException;

import java.util.ArrayList;

public class PostActivity extends AuthorizedActivity implements OnClickCallback, View.OnClickListener {

    private LinearLayout contentLayout;
    private EditText commentTextView;
    private Button backButton, postCommentButton;
    private PostFragment postFragment;

    private PostViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        // Get views
        contentLayout = findViewById(R.id.post_content);
        commentTextView = findViewById(R.id.post_comment);
        backButton = findViewById(R.id.post_back);
        postCommentButton = findViewById(R.id.post_postComment);
        postFragment = (PostFragment) getSupportFragmentManager().findFragmentById(R.id.post_postFrag);

        // Init view model
        viewModel = new ViewModelProvider(this).get(PostViewModel.class);
        viewModel.getPost().observe(this, new Observer<Post>() {
            @Override
            public void onChanged(Post post) {
                postFragment.updateDetails(post);
            }
        });
        viewModel.getComments().observe(this, new Observer<ArrayList<Comment>>() {
            @Override
            public void onChanged(ArrayList<Comment> comments) {
                for (int i = 0; i < comments.size(); i++) {
                    Comment comment = comments.get(i);
                    CommentFragment commentFragment = CommentFragment.newInstance(comment);

                    String fragmentTag = "fragment" + i;
                    if (getSupportFragmentManager().findFragmentByTag(fragmentTag) == null) {
                        getSupportFragmentManager().beginTransaction().add(R.id.post_content, commentFragment, fragmentTag).commit();
                    }
                }
            }
        });

        // Init activity
        Intent intent = getIntent();
        Post post = intent.getParcelableExtra("post");
        viewModel.setPost(post);
        viewModel.fetchComments(getApplicationContext(), getAuthToken());

        // Init listeners
        postCommentButton.setOnClickListener(this);
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
        else if (v == postCommentButton) {
            try {
                viewModel.postComment(getApplicationContext(), getAuthToken(), commentTextView.getText().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
