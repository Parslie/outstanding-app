package com.vikho305.isaho220.outstanding.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.vikho305.isaho220.outstanding.CustomJsonObjectRequest;
import com.vikho305.isaho220.outstanding.OnClickCallback;
import com.vikho305.isaho220.outstanding.R;
import com.vikho305.isaho220.outstanding.database.Post;
import com.vikho305.isaho220.outstanding.database.User;
import com.vikho305.isaho220.outstanding.fragment.PostFragment;
import com.vikho305.isaho220.outstanding.viewmodel.PostViewModel;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PostActivity extends AuthorizedActivity implements OnClickCallback {

    private PostFragment postFragment;
    private PostViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        // Get views
        postFragment = (PostFragment) getSupportFragmentManager().findFragmentById(R.id.post_postFrag);

        // Init view model
        viewModel = new ViewModelProvider(this).get(PostViewModel.class);
        viewModel.getPost().observe(this, new Observer<Post>() {
            @Override
            public void onChanged(Post post) {
                postFragment.updateDetails(post);
            }
        });

        // Init activity
        Intent intent = getIntent();
        Post post = intent.getParcelableExtra("post");
        viewModel.setPost(post);

        postFragment.setOnClickCallback(this);
    }

    private void goToAuthor() {
        User user = viewModel.getAuthor().getValue();
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("user", user);
        goToActivity(intent);
    }

    @Override
    public void onClickCallback(String clickKey) {
        switch (clickKey) {
            case PostFragment.AUTHOR_CLICK_KEY:
                goToAuthor();
                break;
            case PostFragment.LIKE_CLICK_KEY:
                viewModel.toggleLikePost(getApplicationContext(), getAuthToken());
                break;
            case PostFragment.DISLIKE_CLICK_KEY:
                viewModel.toggleDislikePost(getApplicationContext(), getAuthToken());
                break;
        }
    }









    public void likePost(String postId){
        String url = getResources().getString(R.string.like_post_url, postId);
        CustomJsonObjectRequest request = new CustomJsonObjectRequest(
                Request.Method.POST,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + getAuthToken());
                return headers;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }

    public void dislikePost(String postId){
        String url = getResources().getString(R.string.dislike_post_url, postId);
        CustomJsonObjectRequest request = new CustomJsonObjectRequest(
                Request.Method.POST,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + getAuthToken());
                return headers;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }

    public void unlikePost(String postId){
        String url = getResources().getString(R.string.like_post_url, postId);
        CustomJsonObjectRequest request = new CustomJsonObjectRequest(
                Request.Method.DELETE,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + getAuthToken());
                return headers;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }

    public void undislikePost(String postId){
        String url = getResources().getString(R.string.dislike_post_url, postId);
        CustomJsonObjectRequest request = new CustomJsonObjectRequest(
                Request.Method.DELETE,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + getAuthToken());
                return headers;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }
}
