package com.vikho305.isaho220.outstanding.data.api;

import android.content.Context;

import com.rx2androidnetworking.Rx2AndroidNetworking;
import com.vikho305.isaho220.outstanding.data.AuthInfo;
import com.vikho305.isaho220.outstanding.data.Comment;
import com.vikho305.isaho220.outstanding.data.Post;
import com.vikho305.isaho220.outstanding.data.User;
import com.vikho305.isaho220.outstanding.data.repositories.PreferenceRepository;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import io.reactivex.Single;

public class PostApi {
    private static final String ROOT_URL = "https://outstanding-server.herokuapp.com/post";

    private PreferenceRepository preferences;

    public PostApi(Context context) {
        preferences = new PreferenceRepository(context);
    }

    public Single<Post> getPost(String postId) {
        return Rx2AndroidNetworking.get(ROOT_URL + "/" + postId)
                .addHeaders("Authorization", "Bearer " + preferences.getAuthToken())
                .build()
                .getObjectSingle(Post.class);
    }

    public Single<List<Post>> getPostsInRadius(double radius) {
        return Rx2AndroidNetworking.get(ROOT_URL + "/all/" + radius)
                .addHeaders("Authorization", "Bearer " + preferences.getAuthToken())
                .build()
                .getObjectListSingle(Post.class);
    }

    public Single<List<Comment>> getComments(String postId, int page) {
        return Rx2AndroidNetworking.get(ROOT_URL + "/" + postId + "/comments/" + page)
                .addHeaders("Authorization", "Bearer " + preferences.getAuthToken())
                .build()
                .getObjectListSingle(Comment.class);
    }

    public Single<String> postPost(String title, String text, String media, String mediaType,
                                   double latitude, double longitude) {
        JSONObject jsonParameters = new JSONObject();
        try {
            jsonParameters.put("title", title);
            jsonParameters.put("text", text);
            jsonParameters.put("media", media);
            jsonParameters.put("media_type", mediaType);
            jsonParameters.put("latitude", latitude);
            jsonParameters.put("longitude", longitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return Rx2AndroidNetworking.post(ROOT_URL)
                .addHeaders("Authorization", "Bearer " + preferences.getAuthToken())
                .addJSONObjectBody(jsonParameters)
                .build()
                .getStringSingle();
    }

    public Single<String> postComment(String postId, String text) {
        JSONObject jsonParameters = new JSONObject();
        try {
            jsonParameters.put("text", text);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return Rx2AndroidNetworking.post(ROOT_URL + "/" + postId + "/comment")
                .addHeaders("Authorization", "Bearer " + preferences.getAuthToken())
                .addJSONObjectBody(jsonParameters)
                .build()
                .getStringSingle();
    }

    public Single<String> likePost(String postId) {
        return Rx2AndroidNetworking.post(ROOT_URL + "/" + postId + "/like")
                .addHeaders("Authorization", "Bearer " + preferences.getAuthToken())
                .build()
                .getStringSingle();
    }

    public Single<String> dislikePost(String postId) {
        return Rx2AndroidNetworking.post(ROOT_URL + "/" + postId + "/dislike")
                .addHeaders("Authorization", "Bearer " + preferences.getAuthToken())
                .build()
                .getStringSingle();
    }

    public Single<String> unlikePost(String postId) {
        return Rx2AndroidNetworking.delete(ROOT_URL + "/" + postId + "/like")
                .addHeaders("Authorization", "Bearer " + preferences.getAuthToken())
                .build()
                .getStringSingle();
    }

    public Single<String> undislikePost(String postId) {
        return Rx2AndroidNetworking.delete(ROOT_URL + "/" + postId + "/dislike")
                .addHeaders("Authorization", "Bearer " + preferences.getAuthToken())
                .build()
                .getStringSingle();
    }
}
