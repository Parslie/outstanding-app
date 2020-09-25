package com.vikho305.isaho220.outstanding.data.repositories;

import android.content.Context;

import com.rx2androidnetworking.Rx2AndroidNetworking;
import com.vikho305.isaho220.outstanding.data.Comment;
import com.vikho305.isaho220.outstanding.data.Post;
import com.vikho305.isaho220.outstanding.data.api.PostApi;
import com.vikho305.isaho220.outstanding.data.api.UserApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import io.reactivex.Single;

public class PostRepository {

    private PostApi postApi;

    public PostRepository(Context context) {
        postApi = new PostApi(context);
    }

    public Single<Post> getPost(String postId) {
        return postApi.getPost(postId);
    }

    public Single<List<Post>> getPostsInRadius(double radius) {
        return postApi.getPostsInRadius(radius);
    }

    public Single<List<Comment>> getComments(String postId, int page) {
        return postApi.getComments(postId, page);
    }

    public Single<String> postPost(String title, String text, String media, String mediaType,
                                   double latitude, double longitude) {
        return postApi.postPost(title, text, media, mediaType, latitude, longitude);
    }

    public Single<String> postComment(String postId, String text) {
        return postApi.postComment(postId, text);
    }

    public Single<String> likePost(String postId) {
        return postApi.likePost(postId);
    }

    public Single<String> dislikePost(String postId) {
        return postApi.dislikePost(postId);
    }

    public Single<String> unlikePost(String postId) {
        return postApi.unlikePost(postId);
    }

    public Single<String> undislikePost(String postId) {
        return postApi.undislikePost(postId);
    }
}
