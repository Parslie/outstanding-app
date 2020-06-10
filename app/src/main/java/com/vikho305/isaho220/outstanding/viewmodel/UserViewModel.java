package com.vikho305.isaho220.outstanding.viewmodel;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.vikho305.isaho220.outstanding.JsonParameterRequest;
import com.vikho305.isaho220.outstanding.ResponseListener;
import com.vikho305.isaho220.outstanding.R;
import com.vikho305.isaho220.outstanding.database.Post;
import com.vikho305.isaho220.outstanding.database.Profile;
import com.vikho305.isaho220.outstanding.database.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserViewModel extends ViewModel {
    public static final String PROFILE_SAVE_RESPONSE = "profile";
    public static final String ACCOUNT_SAVE_RESPONSE = "account";
    public static final String COORDINATES_SAVE_RESPONSE = "coordinates";
    public static final String FOLLOW_RESPONSE = "follow";
    public static final String UNFOLLOW_RESPONSE = "unfollow";

    private MutableLiveData<User> user = new MutableLiveData<>();
    private MutableLiveData<Profile> profile = new MutableLiveData<>();
    private MutableLiveData<List<Post>> posts = new MutableLiveData<>();

    public LiveData<User> getUser() {
        return user;
    }
    public LiveData<Profile> getProfile() {
        return profile;
    }
    public LiveData<List<Post>> getPosts() {
        return posts;
    }

    public void setUser(User user) {
        this.user.setValue(user);
        this.profile.setValue(user.getProfile());
        this.posts.setValue(new ArrayList<Post>());
    }

    // User-altering methods
    public void setDescription(String description) {
        Profile profile = this.profile.getValue();
        assert profile != null;
        profile.setDescription(description);
        this.profile.setValue(profile);
    }

    public void setPicture(Bitmap picture) {
        Profile profile = this.profile.getValue();
        assert profile != null;

        int bitmapWidth = picture.getWidth();
        int bitmapHeight = picture.getHeight();
        int bitmapSize = Math.min(bitmapWidth, bitmapHeight);

        int croppedOffsetX = (bitmapWidth - bitmapSize) / 2;
        int croppedOffsetY = (bitmapHeight - bitmapSize) / 2;
        Bitmap croppedBitmap = Bitmap.createBitmap(picture, croppedOffsetX, croppedOffsetY, bitmapSize, bitmapSize);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        croppedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        profile.setPicture(Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT));

        this.profile.setValue(profile);
    }

    public void setPrimaryColor(double hue, double saturation, double lightness) {
        Profile profile = this.profile.getValue();
        assert profile != null;
        profile.setPrimaryHue(hue);
        profile.setPrimarySaturation(saturation);
        profile.setPrimaryLightness(lightness);
        this.profile.setValue(profile);
    }

    public void setSecondaryColor(double hue, double saturation, double lightness) {
        Profile profile = this.profile.getValue();
        assert profile != null;
        profile.setSecondaryHue(hue);
        profile.setSecondarySaturation(saturation);
        profile.setSecondaryLightness(lightness);
        this.profile.setValue(profile);
    }

    public void setAccount(String username, String email) {
        User user = this.user.getValue();
        assert user != null;
        user.setUsername(username);
        user.setEmail(email);
        this.user.setValue(user);
    }

    public void setPosition(double latitude, double longitude) {
        User user = this.user.getValue();
        assert user != null;
        user.setLatitude(latitude);
        user.setLongitude(longitude);
        this.user.setValue(user);
    }

    public void resetPosts() {
        posts.setValue(new ArrayList<Post>());
    }

    public void addPosts(Post... posts) {
        List<Post> postList = this.posts.getValue();
        assert postList != null;
        postList.addAll(Arrays.asList(posts));
        this.posts.setValue(postList);
    }

    // Server-calling methods
    public void fetchUser(final Context context, final String userID, final String authToken) {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                context.getResources().getString(R.string.get_user_url, userID),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Gson gson = new Gson();
                        User user = gson.fromJson(response.toString(), User.class);
                        setUser(user);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + authToken);
                return headers;
            }
        };

        Volley.newRequestQueue(context).add(request);
    }

    public void saveProfile(final Context context, final String authToken, final ResponseListener responseListener) throws JSONException {
        Profile profile = this.profile.getValue();
        assert profile != null;

        JSONObject parameters = new JSONObject();
        parameters.put("description", profile.getDescription());
        parameters.put("picture", profile.getPicture());
        parameters.put("primary_hue", profile.getPrimaryHue());
        parameters.put("primary_saturation", profile.getPrimarySaturation());
        parameters.put("primary_lightness", profile.getPrimaryLightness());
        parameters.put("secondary_hue", profile.getSecondaryHue());
        parameters.put("secondary_saturation", profile.getSecondarySaturation());
        parameters.put("secondary_lightness", profile.getSecondaryLightness());

        JsonParameterRequest request = new JsonParameterRequest(
                Request.Method.POST,
                context.getResources().getString(R.string.set_profile_url),
                parameters,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        responseListener.onRequestResponse(PROFILE_SAVE_RESPONSE, true);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        responseListener.onRequestResponse(PROFILE_SAVE_RESPONSE, false);
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + authToken);
                return headers;
            }
        };

        Volley.newRequestQueue(context).add(request);
    }

    public void saveAccount(final Context context, final String authToken, final ResponseListener responseListener, String password) throws JSONException {
        User user = this.user.getValue();
        assert user != null;

        JSONObject parameters = new JSONObject();
        parameters.put("username", user.getUsername());
        parameters.put("email", user.getEmail());
        parameters.put("password", password); // TODO: add security with confirmation password

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                context.getResources().getString(R.string.set_account_url),
                parameters,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        responseListener.onRequestResponse(ACCOUNT_SAVE_RESPONSE, true);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        responseListener.onRequestResponse(ACCOUNT_SAVE_RESPONSE, false);
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + authToken);
                return headers;
            }
        };

        Volley.newRequestQueue(context).add(request);
    }

    public void saveCoordinates(final Context context, final String authToken, final ResponseListener responseListener) throws JSONException {
        User user = this.user.getValue();
        assert user != null;

        JSONObject parameters = new JSONObject();
        parameters.put("latitude", user.getLatitude());
        parameters.put("longitude", user.getLongitude());

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                context.getResources().getString(R.string.set_coordinates_url),
                parameters,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        responseListener.onRequestResponse(COORDINATES_SAVE_RESPONSE, true);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        responseListener.onRequestResponse(COORDINATES_SAVE_RESPONSE, false);
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + authToken);
                return headers;
            }
        };

        Volley.newRequestQueue(context).add(request);
    }

    public void follow(Context context, final String authToken, final ResponseListener responseListener){
        User user = this.user.getValue();
        assert user != null;

        JsonParameterRequest request = new JsonParameterRequest(
                Request.Method.POST,
                context.getResources().getString(R.string.follow_url, user.getId()),
                null,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        responseListener.onRequestResponse(FOLLOW_RESPONSE, true);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        responseListener.onRequestResponse(FOLLOW_RESPONSE, false);
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + authToken);
                return headers;
            }
        };

        Volley.newRequestQueue(context).add(request);
    }

    public void unfollow(Context context, final String authToken, final ResponseListener responseListener){
        User user = this.user.getValue();
        assert user != null;

        JsonParameterRequest request = new JsonParameterRequest(
                Request.Method.DELETE,
                context.getResources().getString(R.string.follow_url, user.getId()),
                null,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        responseListener.onRequestResponse(UNFOLLOW_RESPONSE, true);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        responseListener.onRequestResponse(UNFOLLOW_RESPONSE, false);
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + authToken);
                return headers;
            }
        };

        Volley.newRequestQueue(context).add(request);
    }
}
