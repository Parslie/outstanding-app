package com.vikho305.isaho220.outstanding.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.widget.TextView;

import com.vikho305.isaho220.outstanding.R;
import com.vikho305.isaho220.outstanding.database.Post;
import com.vikho305.isaho220.outstanding.fragment.PostFragment;

import java.util.Objects;

public class PostActivity extends AppCompatActivity {

    private PostFragment postFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        Bundle extras = getIntent().getExtras();
        Post post = (Post) Objects.requireNonNull(extras).get("post");

        postFragment = (PostFragment) getSupportFragmentManager().findFragmentById(R.id.post_postFrag);

        ((TextView) Objects.requireNonNull(Objects.requireNonNull(postFragment).getView()).findViewById(R.id.postFrag_title)).setText(Objects.requireNonNull(post).getTitle());
        ((TextView) Objects.requireNonNull(Objects.requireNonNull(postFragment).getView()).findViewById(R.id.postFrag_text)).setText(Objects.requireNonNull(post).getText());

    }
}
