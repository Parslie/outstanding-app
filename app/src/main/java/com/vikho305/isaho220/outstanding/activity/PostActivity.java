package com.vikho305.isaho220.outstanding.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.vikho305.isaho220.outstanding.R;
import com.vikho305.isaho220.outstanding.database.Post;
import com.vikho305.isaho220.outstanding.database.User;
import com.vikho305.isaho220.outstanding.fragment.PostFragment;

import java.util.Objects;

public class PostActivity extends AuthorizedActivity {

    private PostFragment postFragment;
    private Post post;

    private void goToLockedProfile(User user) {
        Intent intent = new Intent(this, LockedProfileActivity.class);
        intent.putExtra("user", user);
        goToActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        Bundle extras = getIntent().getExtras();
        post = (Post) Objects.requireNonNull(extras).get("post");

        postFragment = (PostFragment) getSupportFragmentManager().findFragmentById(R.id.post_postFrag);

        ((TextView) Objects.requireNonNull(Objects.requireNonNull(postFragment).getView()).findViewById(R.id.postFrag_title)).setText(Objects.requireNonNull(post).getTitle());

        ((TextView) Objects.requireNonNull(Objects.requireNonNull(postFragment).getView()).findViewById(R.id.postFrag_text)).setText(Objects.requireNonNull(post).getText());

        TextView authorTextView = (TextView) Objects.requireNonNull(Objects.requireNonNull(postFragment).getView()).findViewById(R.id.postFrag_author);
        authorTextView.setText(Objects.requireNonNull(post).getAuthor().getUsername());
        authorTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View viewIn) {
                goToLockedProfile(Objects.requireNonNull(post).getAuthor());
            }
        });

    }
}
