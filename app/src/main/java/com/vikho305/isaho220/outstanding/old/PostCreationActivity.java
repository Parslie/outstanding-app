package com.vikho305.isaho220.outstanding.old;

import androidx.annotation.Nullable;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.vikho305.isaho220.outstanding.R;
import com.vikho305.isaho220.outstanding.old.database.Post;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PostCreationActivity extends AuthorizedActivity implements View.OnClickListener {

    private static final int CAMERA_REQUEST = 100, GALLERY_REQUEST = 101;

    private String media, mediaType;
    private double latitude, longitude;

    private EditText titleView, textView;
    private Button textButton, imageButton, cancelButton, postButton;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_creation);

        // Get layout views
        titleView = findViewById(R.id.postCreation_title);
        textView = findViewById(R.id.postCreation_text);
        textButton = findViewById(R.id.postCreation_textBtn);
        imageButton = findViewById(R.id.postCreation_imageBtn);
        cancelButton = findViewById(R.id.postCreation_cancelBtn);
        postButton = findViewById(R.id.postCreation_postBtn);
        imageView = findViewById(R.id.postCreation_image);

        // Init activity
        Intent intent = getIntent();
        if (intent.hasExtra("latitude") || intent.hasExtra("longitude")) {
            latitude = intent.getDoubleExtra("latitude", 0);
            longitude = intent.getDoubleExtra("longitude", 0);
        }
        else {
            setResult(RESULT_CANCELED);
            finish();
            // TODO: add more extensive error-handling
        }

        // Init listeners
        textButton.setOnClickListener(this);
        imageButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        postButton.setOnClickListener(this);
    }

    private void chooseMediaImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"}; // TODO: TEEEEEEEEEEEEEEEEEEEEEEEEEEESSSSSSSSSSSSSSSSSSSSSSSST

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose an image source");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
                else if (options[item].equals("Choose from Gallery")) {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, GALLERY_REQUEST);
                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void postPost(String title, String text, String media, String mediaType, double latitude, double longitude) throws JSONException {
        JSONObject parameters = new JSONObject();
        parameters.put("title", title);
        parameters.put("text", text);
        parameters.put("media", media);
        parameters.put("media_type", mediaType);
        parameters.put("latitude", latitude);
        parameters.put("longitude", longitude);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                getResources().getString(R.string.url_post_post),
                parameters,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Intent data = new Intent();
                            data.putExtra("id", response.getString("id"));
                            setResult(RESULT_OK, data);
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            // TODO: add error-handling
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        // TODO: add error-handling
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
    public void onClick(View v) {
        if (v == textButton) {
            mediaType = Post.TEXT_TYPE;
            media = null;
            imageView.setVisibility(View.INVISIBLE);
        }
        else if (v == imageButton) {
            chooseMediaImage();
        }
        else if (v == cancelButton) {
            setResult(RESULT_CANCELED);
            finish();
        }
        else if (v == postButton) {
            String title = titleView.getText().toString();
            String text = textView.getText().toString();

            try {
                postPost(title, text, media, mediaType, latitude, longitude);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK && data != null) {
            System.out.println(data.getData());
            imageView.setVisibility(View.VISIBLE);
            mediaType = Post.IMAGE_TYPE;
        }
        else if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK && data != null) {
            System.out.println(data.getData());
            imageView.setVisibility(View.VISIBLE);
            mediaType = Post.IMAGE_TYPE;
        }
    }

    /*

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
            viewModel.setPostMediaType(Post.TEXT_TYPE); // Change to text post if choosing media is failed
            // TODO: add more extensive error-handling
        }
    }

    private String encodeImage(Uri imageUri) {
        try {
            Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri); // Turn image into bitmap
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, IMAGE_QUALITY, stream); // Turn bitmap into byte array
            return Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT); // Encode image byte array
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

     */
}