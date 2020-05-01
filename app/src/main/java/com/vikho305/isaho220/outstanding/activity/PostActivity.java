package com.vikho305.isaho220.outstanding.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.vikho305.isaho220.outstanding.R;
import com.vikho305.isaho220.outstanding.fragment.PostFragment;

public class PostActivity extends AppCompatActivity {

    private PostFragment postFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        postFragment = (PostFragment) getSupportFragmentManager().findFragmentById(R.id.post_postFrag);
    }
}
