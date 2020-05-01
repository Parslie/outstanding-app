package com.vikho305.isaho220.outstanding.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.vikho305.isaho220.outstanding.R;

public class PostCreationActivity extends AppCompatActivity {

    private EditText titleView, textView;
    private Button backButton, saveButton;
    private Button textButton, imageButton, videoButton, audioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_creation);
    }
}
