package com.vikho305.isaho220.outstanding.viewmodel;

import android.content.Context;

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
import com.vikho305.isaho220.outstanding.database.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PostViewModel extends ViewModel {

    public static final String POSTING_RESPONSE = "post";

    private MutableLiveData<Post> post = new MutableLiveData<>();
    private MutableLiveData<User> author = new MutableLiveData<>();

    public LiveData<Post> getPost() {
        return post;
    }
    public LiveData<User> getAuthor() {
        return author;
    }

    public void setPost(Post post) {
        this.post.setValue(post);
        author.setValue(post.getAuthor());
    }

    public void setPostTitle(String title) {
        Post post = this.post.getValue();
        assert post != null;
        post.setTitle(title);
        updatePost();
    }

    public void setPostText(String text) {
        Post post = this.post.getValue();
        assert post != null;
        post.setText(text);
        updatePost();
    }

    public void setPostMedia(String media) {
        Post post = this.post.getValue();
        assert post != null;
        post.setMedia(media);
        updatePost();
    }

    public void setPostMediaType(String mediaType) {
        Post post = this.post.getValue();
        assert post != null;
        post.setMediaType(mediaType);
        updatePost();
    }

    private void updatePost() {
        post.setValue(post.getValue());
    }

    // Server-calling methods
    public void postPost(final Context context, final String authToken, final ResponseListener responseListener) throws JSONException {
        Post post = this.post.getValue();
        assert post != null;

        JSONObject parameters = new JSONObject();
        parameters.put("title", post.getTitle());
        parameters.put("text", post.getText());
        parameters.put("media", post.getMedia());
        parameters.put("media_type", post.getMediaType());
        parameters.put("latitude", post.getLatitude());
        parameters.put("longitude", post.getLongitude());

        JsonParameterRequest request = new JsonParameterRequest(
                Request.Method.POST,
                context.getResources().getString(R.string.post_post_url),
                parameters,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        responseListener.onRequestResponse(POSTING_RESPONSE, true);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        responseListener.onRequestResponse(POSTING_RESPONSE, false);
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

    public void toggleLikePost(Context context, final String authToken) {
        final Post post = this.post.getValue();
        assert post != null;

        JsonObjectRequest request = new JsonObjectRequest(
                (post.isLiked()) ? Request.Method.DELETE : Request.Method.POST,
                context.getResources().getString(R.string.like_post_url, post.getId()),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response.toString());
                        Gson gson = new Gson();
                        Post updatedPost = gson.fromJson(response.toString(), Post.class);
                        PostViewModel.this.post.setValue(updatedPost);
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

    public void toggleDislikePost(Context context, final String authToken) {
        final Post post = this.post.getValue();
        assert post != null;

        JsonObjectRequest request = new JsonObjectRequest(
                (post.isDisliked()) ? Request.Method.DELETE : Request.Method.POST,
                context.getResources().getString(R.string.dislike_post_url, post.getId()),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response.toString());
                        Gson gson = new Gson();
                        Post updatedPost = gson.fromJson(response.toString(), Post.class);
                        PostViewModel.this.post.setValue(updatedPost);
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
