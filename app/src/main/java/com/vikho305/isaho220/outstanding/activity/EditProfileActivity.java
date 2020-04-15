package com.vikho305.isaho220.outstanding.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.vikho305.isaho220.outstanding.JsonParameterRequest;
import com.vikho305.isaho220.outstanding.R;
import com.vikho305.isaho220.outstanding.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AuthorizedActivity {

    private static final int MAX_DESCRIPTION_LENGTH = 200;

    private User user;

    private View rootView;
    private ImageButton profilePictureButton;
    private EditText descriptionInput;
    private TextView descriptionLengthText;
    private SeekBar hueSlider, saturationSlider, lightnessSlider;

    private String encodedPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        profilePictureButton = findViewById(R.id.editProfilePicture);
        descriptionInput = findViewById(R.id.editDescription);
        descriptionLengthText = findViewById(R.id.descriptionLength);
        hueSlider = findViewById(R.id.hueSlider);
        saturationSlider = findViewById(R.id.saturationSlider);
        lightnessSlider = findViewById(R.id.lightnessSlider);
        rootView = profilePictureButton.getRootView();

        Intent intent = getIntent();
        user = intent.getParcelableExtra("user");

        if (user != null) {
            descriptionInput.setText(user.getDescription());
            hueSlider.setProgress((int) (user.getHue() * hueSlider.getMax()));
            saturationSlider.setProgress((int) (user.getSaturation() * saturationSlider.getMax()));
            lightnessSlider.setProgress((int) (user.getLightness() * lightnessSlider.getMax()));
        }

        setBackgroundColor();
        setListeners();
    }

    private void setListeners() {
        SeekBar.OnSeekBarChangeListener colorChangeListener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setBackgroundColor();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        };

        hueSlider.setOnSeekBarChangeListener(colorChangeListener);
        saturationSlider.setOnSeekBarChangeListener(colorChangeListener);
        lightnessSlider.setOnSeekBarChangeListener(colorChangeListener);

        descriptionInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                descriptionLengthText.setText(getResources().getString(R.string.description_length, s.length(), MAX_DESCRIPTION_LENGTH));

                user.setDescription(s.toString());
            }
        });

        profilePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* Why would you need location access to run the camera?
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                */

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getPackageManager()) != null) // Make sure an activity can handle the event
                    startActivityForResult(intent, 0);
            }
        });

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });
    }

    private void goToProfile() {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("userId", getAuthUserId());
        intent.putExtra("user", user); // Send user to reduce server calls
        goToActivity(intent);
    }

    private void setBackgroundColor() {
        float hue = hueSlider.getProgress() / (float) hueSlider.getMax();
        float saturation = saturationSlider.getProgress() / (float) saturationSlider.getMax();
        float lightness = lightnessSlider.getProgress() / (float) lightnessSlider.getMax();

        System.out.println(hue + " " + saturation + " " + lightness);

        float[] hsl = new float[]{
                360 * hue,
                saturation,
                lightness
        };

        System.out.println(Color.HSVToColor(hsl));
        rootView.setBackgroundColor(Color.HSVToColor(hsl));
    }

    private void updateProfile() {
        final String description = descriptionInput.getText().toString();
        final int hue = hueSlider.getProgress();
        final int saturation = saturationSlider.getProgress();
        final int lightness = lightnessSlider.getProgress();

        final JSONObject parameters = new JSONObject();
        try {
            parameters.put("description", description);
            parameters.put("hue", String.valueOf(hue));
            parameters.put("saturation", String.valueOf(saturation));
            parameters.put("lightness", String.valueOf(lightness));
            parameters.put("picture", encodedPicture);
            // TODO: send in picture
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonParameterRequest request = new JsonParameterRequest(
                Request.Method.POST,
                getResources().getString(R.string.edit_profile_url),
                parameters,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("==========RESPONSE:" + response + "==========");

                        user.setDescription(description);
                        user.setHue(hue);
                        user.setSaturation(saturation);
                        user.setLightness(lightness);
                        user.setPicture(encodedPicture);

                        goToProfile();
                        Toast.makeText(EditProfileActivity.this, getResources().getString(R.string.generic_success), Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(EditProfileActivity.this, getResources().getString(R.string.generic_error), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + getAuthToken());
                return headers;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = (Bitmap)data.getExtras().get("data");

        int currentBitmapWidth = bitmap.getWidth();
        int currentBitmapHeight = bitmap.getHeight();

        int width = profilePictureButton.getWidth();
        int height = (int) Math.floor((double) currentBitmapHeight * ((double) width / currentBitmapWidth));

        Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);

        profilePictureButton.setImageBitmap(newBitmap);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] imageByteArray = stream.toByteArray();
        encodedPicture = Base64.encodeToString(imageByteArray, Base64.DEFAULT);
    }

    /*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestPermission();
    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
    }
    */
}
