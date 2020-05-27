package com.vikho305.isaho220.outstanding.activity;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.VideoView;

import com.google.android.material.snackbar.Snackbar;
import com.vikho305.isaho220.outstanding.ResponseListener;
import com.vikho305.isaho220.outstanding.R;
import com.vikho305.isaho220.outstanding.database.Post;
import com.vikho305.isaho220.outstanding.viewmodel.PostViewModel;

import org.json.JSONException;

public class PostCreationActivity extends AuthorizedActivity
        implements View.OnClickListener, ResponseListener, TextWatcher {

    private static final int PICK_IMAGE = 100;
    private static final int PICK_VIDEO = 200;
    private Uri imageUri;
    private Uri videoUri;

    private View rootView;
    private EditText titleView, textView;
    private Button backButton, saveButton;
    private Button textButton, imageButton, videoButton, audioButton;
    private ImageView imageView;
    private VideoView videoView;

    private PostViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_creation);

        // Get layout views
        rootView = findViewById(R.id.postCreation_root);
        titleView = findViewById(R.id.postCreation_title);
        textView = findViewById(R.id.postCreation_text);

        backButton = findViewById(R.id.postCreation_back);
        saveButton = findViewById(R.id.postCreation_save);
        textButton = findViewById(R.id.postCreation_textContent);
        imageButton = findViewById(R.id.postCreation_imageContent);
        videoButton = findViewById(R.id.postCreation_videoContent);
        audioButton = findViewById(R.id.postCreation_audioContent);

        imageView = (ImageView)findViewById(R.id.imagePost_imageView);
        videoView = (VideoView)findViewById(R.id.videoPost_videoView);

        // Get latitude and longitude // TODO: make less sloppy permission-checking
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();

        // Init view model
        viewModel = new ViewModelProvider(this).get(PostViewModel.class);
        viewModel.setPost(new Post(Post.TEXT_TYPE, latitude, longitude)); // TODO: add actual position

        // Init listeners
        backButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);

        textButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setVisibility(View.INVISIBLE);
                videoView.setVisibility(View.INVISIBLE);
                textView.setVisibility(View.VISIBLE);
                viewModel.setPostMediaType(Post.TEXT_TYPE);
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageGallery();
                imageView.setVisibility(View.VISIBLE);
                videoView.setVisibility(View.INVISIBLE);
                textView.setVisibility(View.INVISIBLE);
                viewModel.setPostMediaType(Post.IMAGE_TYPE);
            }
        });

        videoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openVideoGallery();
                imageView.setVisibility(View.INVISIBLE);
                videoView.setVisibility(View.VISIBLE);
                textView.setVisibility(View.INVISIBLE);
                viewModel.setPostMediaType(Post.VIDEO_TYPE);
            }
        });

        audioButton.setOnClickListener(this);

        titleView.addTextChangedListener(this);
        textView.addTextChangedListener(this);
    }

    private void cancelCreation() {
        setResult(RESULT_CANCELED);
        finish();
    }

    private void saveCreation() {
        // TODO: check for empty fields

        try {
            viewModel.postPost(getApplicationContext(), getAuthToken(), this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestResponse(String responseType, boolean successful) {
        if (responseType.equals(PostViewModel.POSTING_RESPONSE)) {
            if (successful)  {
                Snackbar.make(rootView, "Something went totally right!", Snackbar.LENGTH_LONG);
                setResult(RESULT_OK);
                finish();
            }
            else  {
                Snackbar.make(rootView, "Something went wrong...", Snackbar.LENGTH_LONG);
                // TODO: add retry button to snack bar
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v == backButton)
            cancelCreation();
        else if (v == saveButton)
            saveCreation();
        else if (v == textButton)
            viewModel.setPostMediaType(Post.TEXT_TYPE);
        else if (v == imageButton)
            viewModel.setPostMediaType(Post.IMAGE_TYPE);
        else if (v == videoButton)
            viewModel.setPostMediaType(Post.VIDEO_TYPE);
        else if (v == audioButton)
            viewModel.setPostMediaType(Post.AUDIO_TYPE);
    }

    @Override
    public void afterTextChanged(Editable s) {
        viewModel.setPostTitle(titleView.getText().toString());
        viewModel.setPostText(textView.getText().toString());
    }

    private void openImageGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    private void openVideoGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_VIDEO);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
        else if (resultCode == RESULT_OK && requestCode == PICK_VIDEO){
            videoUri = data.getData();
            videoView.setVideoURI(videoUri);
        }

    }
}
