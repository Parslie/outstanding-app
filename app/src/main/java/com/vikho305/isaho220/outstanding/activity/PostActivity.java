package com.vikho305.isaho220.outstanding.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.vikho305.isaho220.outstanding.OnClickCallback;
import com.vikho305.isaho220.outstanding.R;
import com.vikho305.isaho220.outstanding.database.Post;
import com.vikho305.isaho220.outstanding.database.User;
import com.vikho305.isaho220.outstanding.fragment.PostFragment;
import com.vikho305.isaho220.outstanding.viewmodel.PostViewModel;

public class PostActivity extends AuthorizedActivity implements OnClickCallback {

    private PostFragment postFragment;
    private PostViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        // Get views
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
        if (clickKey.equals(PostFragment.AUTHOR_CLICK_KEY)) {
            goToAuthor();
        }
    }
}
