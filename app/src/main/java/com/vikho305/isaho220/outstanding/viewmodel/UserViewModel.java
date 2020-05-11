package com.vikho305.isaho220.outstanding.viewmodel;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
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
import com.vikho305.isaho220.outstanding.OnResponseListener;
import com.vikho305.isaho220.outstanding.R;
import com.vikho305.isaho220.outstanding.database.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class UserViewModel extends ViewModel {

    public static final String SAVING_RESPONSE = "save";

    private MutableLiveData<User> user;
    private MutableLiveData<float[]> userColor;
    private MutableLiveData<RoundedBitmapDrawable> userPicture;
    private MutableLiveData<String> userDescription;

    public UserViewModel() {
        user = new MutableLiveData<>();
        userColor = new MutableLiveData<>();
        userPicture = new MutableLiveData<>();
        userDescription = new MutableLiveData<>();
    }

    public void setUser(Context context, User user) {
        this.user.setValue(user);

        Bitmap pictureBitmap;
        if (user.getPicture() == null) {
            pictureBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.default_pfp);
        }
        else {
            byte[] decodedPicture = Base64.decode(user.getPicture(), Base64.DEFAULT);
            pictureBitmap = BitmapFactory.decodeByteArray(decodedPicture, 0, decodedPicture.length);
        }

        RoundedBitmapDrawable roundedPicture = RoundedBitmapDrawableFactory.create(context.getResources(), pictureBitmap);
        roundedPicture.setCircular(true);
        userPicture.setValue(roundedPicture);

        userColor.setValue(new float[] {
                360 * (float) user.getHue(),
                (float) user.getSaturation(),
                (float) user.getLightness()
        });

        userDescription.setValue(user.getDescription());
    }

    public LiveData<User> getUser() {
        return user;
    }

    public void setUserColor(float hue, float saturation, float lightness) {
        User user = this.user.getValue();
        user.setHue(hue);
        user.setSaturation(saturation);
        user.setLightness(lightness);

        this.userColor.setValue(new float[] {
                hue * 360,
                saturation,
                lightness
        });
    }

    public LiveData<float[]> getUserColor() {
        return userColor;
    }

    public void setUserPicture(Bitmap pictureBitmap) {
        int bitmapWidth = pictureBitmap.getWidth();
        int bitmapHeight = pictureBitmap.getHeight();
        int bitmapSize = Math.min(bitmapWidth, bitmapHeight);

        int croppedOffsetX = (bitmapWidth - bitmapSize) / 2;
        int croppedOffsetY = (bitmapHeight - bitmapSize) / 2;
        Bitmap croppedBitmap = Bitmap.createBitmap(pictureBitmap, croppedOffsetX, croppedOffsetY, bitmapSize, bitmapSize);
        RoundedBitmapDrawable roundedPicture = RoundedBitmapDrawableFactory.create(null, croppedBitmap);
        roundedPicture.setCircular(true);
        userPicture.setValue(roundedPicture);

        // Set user field
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        croppedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] imageByteArray = stream.toByteArray();

        User user = this.user.getValue();
        user.setPicture(Base64.encodeToString(imageByteArray, Base64.DEFAULT));
    }

    public LiveData<RoundedBitmapDrawable> getUserPicture() {
        return userPicture;
    }

    public void setUserDescription(String description) {
        userDescription.setValue(description);

        User user = this.user.getValue();
        user.setDescription(description);
    }

    public LiveData<String> getUserDescription() {
        return userDescription;
    }

    // Server actions

    // TODO: check if moving authToken to constructor causes problems
    public void fetchUser(final Context context, String userId, final String authToken) {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                context.getResources().getString(R.string.get_user_url, userId),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Gson gson = new Gson();
                        User newUser = gson.fromJson(response.toString(), User.class);
                        setUser(context, newUser);
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
            public Map<String, String> getHeaders()  {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + authToken);
                return headers;
            }
        };

        Volley.newRequestQueue(context).add(request);
    }

    public void saveUserProfile(Context context, final String authToken, final OnResponseListener onResponseListener) throws JSONException {
        User user = this.user.getValue();
        assert user != null;

        JSONObject parameters = new JSONObject();
        parameters.put("description", user.getDescription());
        parameters.put("picture", user.getPicture());
        parameters.put("hue", user.getHue());
        parameters.put("saturation", user.getSaturation());
        parameters.put("lightness", user.getLightness());

        JsonParameterRequest request = new JsonParameterRequest(
                Request.Method.POST,
                context.getResources().getString(R.string.edit_user_url),
                parameters,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        onResponseListener.onRequestResponse(SAVING_RESPONSE, true);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        onResponseListener.onRequestResponse(SAVING_RESPONSE, false);
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders()  {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + authToken);
                return headers;
            }
        };

        Volley.newRequestQueue(context).add(request);
    }
}
