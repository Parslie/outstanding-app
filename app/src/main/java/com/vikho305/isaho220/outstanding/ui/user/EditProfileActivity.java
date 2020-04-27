package com.vikho305.isaho220.outstanding.ui.user;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.vikho305.isaho220.outstanding.R;
import com.vikho305.isaho220.outstanding.activity.AuthorizedActivity;
import com.vikho305.isaho220.outstanding.database.tables.User;

import org.json.JSONException;

public class EditProfileActivity extends AuthorizedActivity {

    private static final int IMAGE_REQUEST = 0;

    private View root;
    private EditText descriptionView;
    private TextView descriptionLengthView;
    private SeekBar hueSlider, saturationSlider, lightnessSlider;
    private ImageView profilePictureView;

    private UserViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Get layout views
        root = findViewById(R.id.editProfile_root);
        descriptionView = findViewById(R.id.editProfile_description);
        descriptionLengthView = findViewById(R.id.editProfile_descriptionLength);
        hueSlider = findViewById(R.id.editProfile_hueSlider);
        saturationSlider = findViewById(R.id.editProfile_saturationSlider);
        lightnessSlider = findViewById(R.id.editProfile_lightnessSlider);
        profilePictureView = findViewById(R.id.editProfile_picture);
        Button backButton = findViewById(R.id.editProfile_back);
        Button saveButton = findViewById(R.id.editProfile_save);

        // Init view model // TODO: create EditProfileViewModel
        viewModel = new ViewModelProvider(this).get(UserViewModel.class);
        viewModel.getUserColor().observe(this, new Observer<float[]>() {
            @Override
            public void onChanged(float[] hsl) {
                root.setBackgroundColor(Color.HSVToColor(hsl));
            }
        });
        viewModel.getUserPicture().observe(this, new Observer<RoundedBitmapDrawable>() {
            @Override
            public void onChanged(RoundedBitmapDrawable roundedBitmapDrawable) {
                profilePictureView.setImageDrawable(roundedBitmapDrawable);
            }
        });

        Intent intent = getIntent();
        User user = intent.getParcelableExtra("user");

        if (user != null)
            viewModel.setUser(user);
        else
            finish(); // TODO: add more extensive error-handling for no user

        // Init buttons
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    viewModel.saveUserProfile(getApplicationContext(), getAuthToken());

                    Intent data = new Intent();
                    data.putExtra("user", viewModel.getUser().getValue());
                    setResult(RESULT_OK, data);
                    finish(); // TODO (?): wait for server callback
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        profilePictureView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getPackageManager()) != null) // Make sure an activity can handle the event
                    startActivityForResult(intent, IMAGE_REQUEST);
            }
        });

        // Init seek bars
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK) {
            assert data != null && data.getExtras() != null;
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            viewModel.setUserPicture(bitmap);
        }
    }
}
