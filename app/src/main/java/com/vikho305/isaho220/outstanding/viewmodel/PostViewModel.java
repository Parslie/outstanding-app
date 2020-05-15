package com.vikho305.isaho220.outstanding.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.vikho305.isaho220.outstanding.JsonParameterRequest;
import com.vikho305.isaho220.outstanding.OnResponseListener;
import com.vikho305.isaho220.outstanding.R;
import com.vikho305.isaho220.outstanding.database.Post;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PostViewModel extends ViewModel {

    public static final String POSTING_RESPONSE = "post";

    private MutableLiveData<Post> post;

    public PostViewModel() {
        post = new MutableLiveData<>();
    }

    public void setPost(Post post) {
        this.post.setValue(post);
    }

    public void setPostTitle(String title) {
        Post post = this.post.getValue();
        assert post != null;

        post.setTitle(title);
    }

    public void setPostText(String text) {
        Post post = this.post.getValue();
        assert post != null;

        post.setText(text);
    }

    public void setPostMedia(String media) {
        Post post = this.post.getValue();
        assert post != null;

        post.setMedia(media);
    }

    public void setPostContentType(String contentType) {
        Post post = this.post.getValue();
        assert post != null;

        post.setContentType(contentType);
    }

    // Server actions
    public void postPost(final Context context, final String authToken, final OnResponseListener onResponseListener) throws JSONException {
        Post post = this.post.getValue();
        assert post != null;

        JSONObject parameters = new JSONObject();
        parameters.put("title", post.getTitle());
        parameters.put("text", post.getText());
        parameters.put("media", post.getMedia());
        parameters.put("content_type", post.getContentType());
        parameters.put("latitude", post.getLatitude());
        parameters.put("longitude", post.getLongitude());

        JsonParameterRequest request = new JsonParameterRequest(
                Request.Method.POST,
                context.getResources().getString(R.string.post_post_url),
                parameters,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        onResponseListener.onRequestResponse(POSTING_RESPONSE, true);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        onResponseListener.onRequestResponse(POSTING_RESPONSE, false);
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
