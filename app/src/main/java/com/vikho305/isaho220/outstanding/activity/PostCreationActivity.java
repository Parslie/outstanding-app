package com.vikho305.isaho220.outstanding.activity;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.VideoView;

import com.vikho305.isaho220.outstanding.ResponseListener;
import com.vikho305.isaho220.outstanding.R;
import com.vikho305.isaho220.outstanding.database.Post;
import com.vikho305.isaho220.outstanding.viewmodel.PostViewModel;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class PostCreationActivity extends AuthorizedActivity
        implements View.OnClickListener, ResponseListener, TextWatcher {

    private static final int PICK_IMAGE = 0;
    private static final int IMAGE_QUALITY = 15;

    private Button backButton, saveButton;
    private EditText titleView, textView;
    private Button textButton, imageButton;
    private ImageView imageView;

    private PostViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_creation);

        // Get layout views
        titleView = findViewById(R.id.postCreation_title);
        textView = findViewById(R.id.postCreation_text);

        backButton = findViewById(R.id.postCreation_back);
        saveButton = findViewById(R.id.postCreation_save);
        textButton = findViewById(R.id.postCreation_textContent);
        imageButton = findViewById(R.id.postCreation_imageContent);

        imageView = findViewById(R.id.postCreation_imageView);

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

        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Explain to the user why we need to read the contacts
            }

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PICK_IMAGE);

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

        textButton.setOnClickListener(this);
        imageButton.setOnClickListener(this);

        titleView.addTextChangedListener(this);
        textView.addTextChangedListener(this);
    }

    private void openImageGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    public void onRequestResponse(String responseType, boolean successful) {
        if (responseType.equals(PostViewModel.POSTING_RESPONSE)) {
            if (successful)  {
                setResult(RESULT_OK);
                finish();
            }
            else  {
                // TODO: add error case
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v == backButton) {
            setResult(RESULT_CANCELED);
            finish();
        }
        else if (v == saveButton) {
            try {
                viewModel.postPost(getApplicationContext(), getAuthToken(), this);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if (v == textButton) {
            imageView.setVisibility(View.GONE);
            viewModel.setPostMediaType(Post.TEXT_TYPE);
        }
        else if (v == imageButton) {
            openImageGallery();
            imageView.setVisibility(View.VISIBLE);
            viewModel.setPostMediaType(Post.IMAGE_TYPE);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        viewModel.setPostTitle(titleView.getText().toString());
        viewModel.setPostText(textView.getText().toString());
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

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PICK_IMAGE:
                    Uri imageUri = data.getData();
                    imageView.setImageURI(imageUri);
                    viewModel.setPostMedia(encodeImage(imageUri));
                    break;
                default:
                    break;
            }
        }
        else if (requestCode == RESULT_CANCELED) {
            viewModel.setPostMediaType(Post.TEXT_TYPE);
            // TODO: add more extensive error-handling
        }
    }

    private String encodeImage(Uri imageUri) {
        try {
            Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, IMAGE_QUALITY, stream);
            return Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
