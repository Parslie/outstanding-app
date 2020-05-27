package com.vikho305.isaho220.outstanding.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.vikho305.isaho220.outstanding.ResponseListener;
import com.vikho305.isaho220.outstanding.R;
import com.vikho305.isaho220.outstanding.database.Profile;
import com.vikho305.isaho220.outstanding.database.User;
import com.vikho305.isaho220.outstanding.viewmodel.UserViewModel;

import org.json.JSONException;

import java.util.Arrays;

public class EditProfileActivity extends AuthorizedActivity
        implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, TextWatcher, ResponseListener {

    private static final int IMAGE_REQUEST = 0;
    private static final int MAX_DESCRIPTION_LENGTH = 200;

    private View root;
    private EditText descriptionView;
    private TextView descriptionLengthView;
    private SeekBar hueSlider, saturationSlider, lightnessSlider;
    private ImageView profilePictureView;
    private Button backButton, saveButton;

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
        backButton = findViewById(R.id.editProfile_back);
        saveButton = findViewById(R.id.editProfile_save);

        // Init view model
        viewModel = new ViewModelProvider(this).get(UserViewModel.class);
        viewModel.getProfile().observe(this, new Observer<Profile>() {
            @Override
            public void onChanged(Profile profile) {
                root.setBackgroundColor(profile.getPrimaryColor());
                descriptionLengthView.setText(
                        getResources().getString(R.string.description_length, profile.getDescription().length(), MAX_DESCRIPTION_LENGTH)
                );

                Bitmap pictureBitmap;
                if (profile.getPicture() == null) {
                    pictureBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.default_pfp);
                }
                else {
                    byte[] decodedPicture = Base64.decode(profile.getPicture(), Base64.DEFAULT);
                    pictureBitmap = BitmapFactory.decodeByteArray(decodedPicture, 0, decodedPicture.length);
                }

                RoundedBitmapDrawable roundedPicture = RoundedBitmapDrawableFactory.create(getResources(), pictureBitmap);
                roundedPicture.setCircular(true);
                profilePictureView.setImageDrawable(roundedPicture);
            }
        });

        // Init activity
        Intent intent = getIntent();
        User user = intent.getParcelableExtra("user");

        if (user != null) {
            viewModel.setUser(user);
            Profile profile = user.getProfile();
            assert profile != null;

            descriptionView.setText(profile.getDescription());
            hueSlider.setProgress((int) (profile.getPrimaryHue() * hueSlider.getMax()));
            saturationSlider.setProgress((int) (profile.getPrimarySaturation() * saturationSlider.getMax()));
            lightnessSlider.setProgress((int) (profile.getPrimaryLightness() * lightnessSlider.getMax()));
        }
        else {
            finish(); // TODO: add more extensive error-handling for no user
        }

        // Init listeners
        backButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);
        profilePictureView.setOnClickListener(this);

        hueSlider.setOnSeekBarChangeListener(this);
        saturationSlider.setOnSeekBarChangeListener(this);
        lightnessSlider.setOnSeekBarChangeListener(this);

        descriptionView.addTextChangedListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK) {
            assert data != null && data.getExtras() != null;
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            viewModel.setPicture(bitmap);
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
                viewModel.saveProfile(getApplicationContext(), getAuthToken(), this);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if (v == profilePictureView) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (intent.resolveActivity(getPackageManager()) != null) // Make sure an activity can handle the event
                startActivityForResult(intent, IMAGE_REQUEST);
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        double hueProgress = (double) hueSlider.getProgress() / hueSlider.getMax();
        double saturationProgress = (double) saturationSlider.getProgress() / saturationSlider.getMax();
        double lightnessProgress = (double) lightnessSlider.getProgress() / lightnessSlider.getMax();

        viewModel.setPrimaryColor(hueProgress, saturationProgress, lightnessProgress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {}

    @Override
    public void afterTextChanged(Editable s) {
        viewModel.setDescription(s.toString());
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}

    @Override
    public void onRequestResponse(String responseType, boolean successful) {
        if (responseType.equals(UserViewModel.SAVING_RESPONSE) && successful) {
            Intent data = new Intent();
            data.putExtra("user", viewModel.getUser().getValue());
            setResult(RESULT_OK, data);
            finish();
        }
    }
}
