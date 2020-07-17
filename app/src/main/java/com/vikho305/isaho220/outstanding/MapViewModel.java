package com.vikho305.isaho220.outstanding;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.vikho305.isaho220.outstanding.database.Post;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapViewModel extends ViewModel {

    private MutableLiveData<List<Post>> posts = new MutableLiveData<>();

    public LiveData<List<Post>> getPosts() {
        return posts;
    }

    private void setPosts(List<Post> posts) {
        this.posts.setValue(posts);
    }

    public Post getPostAt(double latitude, double longitude) {
        List<Post> posts = this.posts.getValue();
        assert posts != null;

        for (Post post : posts) {
            if (post.getLatitude() == latitude && post.getLongitude() == longitude)
                return post;
        }
        return null;
    }

    /////////////////////////
    // Server-calling methods

    public void fetchPosts(Context context, final String authToken, int radius) {
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                context.getResources().getString(R.string.url_get_posts, radius),
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Gson gson = new Gson();
                        List<Post> posts = new ArrayList<>();

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                Post post = gson.fromJson(response.getJSONObject(i).toString(), Post.class);
                                posts.add(post);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        setPosts(posts);
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