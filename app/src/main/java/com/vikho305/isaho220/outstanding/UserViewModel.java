package com.vikho305.isaho220.outstanding;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.vikho305.isaho220.outstanding.database.User;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserViewModel extends ViewModel {

    private MutableLiveData<User> user = new MutableLiveData<>();

    public LiveData<User> getUser() {
        return user;
    }

    public void setUser(User newUser) {
        user.setValue(newUser);
    }

    /////////////////////////
    // Server-calling methods

    public void fetchUser(Context context, final String authToken, final String userId) {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                context.getResources().getString(R.string.url_get_user, userId),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Gson gson = new Gson();
                        User newUser = gson.fromJson(response.toString(), User.class);
                        user.setValue(newUser);
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
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + authToken);
                return headers;
            }
        };

        Volley.newRequestQueue(context).add(request);
    }
}