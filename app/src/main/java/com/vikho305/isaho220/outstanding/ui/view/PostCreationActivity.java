package com.vikho305.isaho220.outstanding.ui.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.vikho305.isaho220.outstanding.R;
import com.vikho305.isaho220.outstanding.data.Post;
import com.vikho305.isaho220.outstanding.data.User;
import com.vikho305.isaho220.outstanding.ui.viewmodel.ContextualViewModelFactory;
import com.vikho305.isaho220.outstanding.ui.viewmodel.PostCreationViewModel;
import com.vikho305.isaho220.outstanding.util.Resource;

public class PostCreationActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText titleInput, contentInput;
    private Button textButton, imageButton, postButton;

    private String media, mediaType;
    private double latitude, longitude;

    private PostCreationViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_creation);

        titleInput = findViewById(R.id.postCreationTitle); // TODO: add title limit
        contentInput = findViewById(R.id.postCreationContent); // TODO: add content limit
        textButton = findViewById(R.id.postCreationTextBtn);
        imageButton = findViewById(R.id.postCreationImageBtn);
        postButton = findViewById(R.id.postCreationPostBtn);
        textButton.setOnClickListener(this);
        imageButton.setOnClickListener(this);
        postButton.setOnClickListener(this);

        media = "";
        mediaType = Post.TEXT_TYPE;

        initViewModel();
    }

    private void initViewModel() {
        ContextualViewModelFactory viewModelFactory = new ContextualViewModelFactory(this);
        viewModel = new ViewModelProvider(this, viewModelFactory).get(PostCreationViewModel.class);

        viewModel.getHasPosted().observe(this, new Observer<Resource<Boolean>>() {
            @Override
            public void onChanged(Resource<Boolean> booleanResource) {
                switch (booleanResource.getStatus()) {
                    case SUCCESS:
                        finish();
                        break;
                    case ERROR:
                        break;
                    case LOADING:
                        break;
                }
            }
        });

        viewModel.getUserSelf().observe(this, new Observer<Resource<User>>() {
            @Override
            public void onChanged(Resource<User> userResource) {
                switch (userResource.getStatus()) {
                    case LOADING:
                        postButton.setEnabled(false);
                        break;
                    case SUCCESS:
                        postButton.setEnabled(true);
                        User user = userResource.getData();
                        latitude = user.getLatitude();
                        longitude = user.getLongitude();
                        break;
                    case ERROR:
                        break; // TODO: add more extensive error-handling
                }
            }
        });

        viewModel.fetchUserSelf();
    }

    @Override
    public void onClick(View v) {
        if (v == textButton) {
            mediaType = Post.TEXT_TYPE;
            media = "";
        }
        else if (v == imageButton) {
            mediaType = Post.IMAGE_TYPE;
        }
        else if (v == postButton) {
            String title = titleInput.getText().toString();
            String content = contentInput.getText().toString();
            viewModel.postPost(title, content, media, mediaType, latitude, longitude);
        }
    }
}