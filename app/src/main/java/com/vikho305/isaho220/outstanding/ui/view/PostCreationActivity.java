package com.vikho305.isaho220.outstanding.ui.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.vikho305.isaho220.outstanding.R;
import com.vikho305.isaho220.outstanding.data.Post;
import com.vikho305.isaho220.outstanding.data.User;
import com.vikho305.isaho220.outstanding.data.media.ImageMedia;
import com.vikho305.isaho220.outstanding.ui.viewmodel.ContextualViewModelFactory;
import com.vikho305.isaho220.outstanding.ui.viewmodel.PostCreationViewModel;
import com.vikho305.isaho220.outstanding.util.Resource;

public class PostCreationActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int CAMERA_REQUEST = 1, GALLERY_REQUEST = 2;

    private EditText titleInput, contentInput;
    private ImageView imageView;
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
        imageView = findViewById(R.id.postCreationImage);
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
            final CharSequence[] items = new CharSequence[] {"Camera (W.I.P)", "Gallery"};
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Select Image Source");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    CharSequence item = items[which];
                    if (item.equals("Camera")) {
                        // TODO: implement camera feature (IT WAS TOO HARD)
                    }
                    else if (item.equals("Gallery")) {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        startActivityForResult(intent, GALLERY_REQUEST);
                    }
                }
            });
            builder.show();
        }
        else if (v == postButton) {
            String title = titleInput.getText().toString();
            String content = contentInput.getText().toString();
            viewModel.postPost(title, content, media, mediaType, latitude, longitude);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK && data != null) {
            mediaType = Post.IMAGE_TYPE;
        }
        else if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            ImageMedia imageMedia = new ImageMedia();
            media = imageMedia.uriToBase64(getContentResolver(), imageUri);
            mediaType = Post.IMAGE_TYPE;
            imageView.setImageBitmap(imageMedia.base64ToBitmap(media));
        }
    }
}