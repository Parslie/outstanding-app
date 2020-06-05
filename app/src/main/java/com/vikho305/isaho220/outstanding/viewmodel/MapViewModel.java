package com.vikho305.isaho220.outstanding.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vikho305.isaho220.outstanding.JsonParameterRequest;
import com.vikho305.isaho220.outstanding.R;
import com.vikho305.isaho220.outstanding.ResponseListener;
import com.vikho305.isaho220.outstanding.database.Post;
import com.vikho305.isaho220.outstanding.database.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapViewModel extends ViewModel {

    private MutableLiveData<List<User>> onlineFollowings = new MutableLiveData<>();
    private MutableLiveData<List<Post>> posts = new MutableLiveData<>();

    public LiveData<List<User>> getOnlineFollowings() {
        return onlineFollowings;
    }
    public LiveData<List<Post>> getPosts() {
        return posts;
    }

    public void setOnlineFollowings(List<User> users) {
        onlineFollowings.setValue(users);
    }
    public void setPosts(List<Post> posts) {
        this.posts.setValue(posts);
    }

    public User getFollowingAt(double latitude, double longitude) {
        List<User> onlineFollowings = this.onlineFollowings.getValue();
        assert onlineFollowings != null;
        User tmp = null;

        for (User user : onlineFollowings) {
            if (user.getLatitude() == latitude && user.getLongitude() == longitude)
                tmp = user;
        }

        return tmp;
    }

    public Post getPostAt(double latitude, double longitude) {
        List<Post> posts = this.posts.getValue();
        assert posts != null;
        Post tmp = null;

        for (Post post : posts) {
            if (post.getLatitude() == latitude && post.getLongitude() == longitude)
                tmp = post;
        }

        return tmp;
    }

    // Server-calling methods
    public void fetchPosts(Context context, final String authToken, int radius) {
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                context.getResources().getString(R.string.get_posts_url, radius),
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Post>>() {}.getType();
                        List<Post> nearbyPosts = gson.fromJson(response.toString(), listType);
                        setPosts(nearbyPosts);
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

    public void fetchFollowings(Context context, final String authToken) {
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                context.getResources().getString(R.string.get_online_followings_url),
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<User>>() {}.getType();
                        List<User> onlineFollowings = gson.fromJson(response.toString(), listType);
                        setOnlineFollowings(onlineFollowings);
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
}
