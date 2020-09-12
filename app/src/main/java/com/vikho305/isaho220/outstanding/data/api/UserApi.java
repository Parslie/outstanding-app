package com.vikho305.isaho220.outstanding.data.api;

import android.content.Context;
import com.vikho305.isaho220.outstanding.data.Post;
import com.vikho305.isaho220.outstanding.data.User;
import com.vikho305.isaho220.outstanding.data.repositories.PreferenceRepository;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import com.rx2androidnetworking.Rx2AndroidNetworking;
import io.reactivex.Single;

public class UserApi {
    private static final String ROOT_URL = "https://outstanding-server.herokuapp.com/user";

    private PreferenceRepository preferences;

    public UserApi(Context context) {
        preferences = new PreferenceRepository(context);
    }

    public Single<String> register(String username, String email, String password) {
        JSONObject jsonParameters = new JSONObject();
        try {
            jsonParameters.put("username", username);
            jsonParameters.put("email", email);
            jsonParameters.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return Rx2AndroidNetworking.post(ROOT_URL)
                .addJSONObjectBody(jsonParameters)
                .build()
                .getStringSingle();
    }

    public Single<String> login(String username, String password) {
        JSONObject jsonParameters = new JSONObject();
        try {
            jsonParameters.put("username", username);
            jsonParameters.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return Rx2AndroidNetworking.post(ROOT_URL + "/login")
                .addJSONObjectBody(jsonParameters)
                .build()
                .getStringSingle();
    }

    public Single<String> logout() {
        return Rx2AndroidNetworking.post(ROOT_URL + "/logout")
                .addHeaders("Authorization", "Bearer " + preferences.getAuthToken())
                .build()
                .getStringSingle();
    }

    // Getter methods

    public Single<User> getUser(String userId) {
        return Rx2AndroidNetworking.get(ROOT_URL + "/" + userId)
                .addHeaders("Authorization", "Bearer " + preferences.getAuthToken())
                .build()
                .getObjectSingle(User.class);
    }

    public Single<List<User>> getFollowers(String userId, int page) {
        return Rx2AndroidNetworking.get(ROOT_URL + "/" + userId + "/followers/" + page)
                .addHeaders("Authorization", "Bearer " + preferences.getAuthToken())
                .build()
                .getObjectListSingle(User.class);
    }

    public Single<List<User>> getFollowings(String userId, int page) {
        return Rx2AndroidNetworking.get(ROOT_URL + "/" + userId + "/followings/" + page)
                .addHeaders("Authorization", "Bearer " + preferences.getAuthToken())
                .build()
                .getObjectListSingle(User.class);
    }

    public Single<List<User>> getPendingFollowers(String userId, int page) {
        return Rx2AndroidNetworking.get(ROOT_URL + "/" + userId + "/followers/pending/" + page)
                .addHeaders("Authorization", "Bearer " + preferences.getAuthToken())
                .build()
                .getObjectListSingle(User.class);
    }

    public Single<List<User>> getPendingFollowings(String userId, int page) {
        return Rx2AndroidNetworking.get(ROOT_URL + "/" + userId + "/followings/pending/" + page)
                .addHeaders("Authorization", "Bearer " + preferences.getAuthToken())
                .build()
                .getObjectListSingle(User.class);
    }

    public Single<List<User>> getBlockings(String userId, int page) {
        return Rx2AndroidNetworking.get(ROOT_URL + "/" + userId + "/blockings/" + page)
                .addHeaders("Authorization", "Bearer " + preferences.getAuthToken())
                .build()
                .getObjectListSingle(User.class);
    }

    public Single<List<Post>> getPosts(String userId, int page) {
        return Rx2AndroidNetworking.get(ROOT_URL + "/" + userId + "/posts/" + page)
                .addHeaders("Authorization", "Bearer " + preferences.getAuthToken())
                .build()
                .getObjectListSingle(Post.class);
    }

    // Setter methods

    public Single<String> setAccountDetails(String username, String email) {
        JSONObject jsonParameters = new JSONObject();
        try {
            jsonParameters.put("username", username);
            jsonParameters.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return Rx2AndroidNetworking.post(ROOT_URL + "/account")
                .addJSONObjectBody(jsonParameters)
                .addHeaders("Authorization", "Bearer " + preferences.getAuthToken())
                .build()
                .getStringSingle();
    }

    public Single<String> setAccountDetails(String username, String email, String password) {
        JSONObject jsonParameters = new JSONObject();
        try {
            jsonParameters.put("username", username);
            jsonParameters.put("email", email);
            jsonParameters.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return Rx2AndroidNetworking.post(ROOT_URL + "/account")
                .addJSONObjectBody(jsonParameters)
                .addHeaders("Authorization", "Bearer " + preferences.getAuthToken())
                .build()
                .getStringSingle();
    }

    public Single<String> setProfileDetails(String picture, String description,
                                            double primaryH, double primaryS, double primaryL,
                                            double secondaryH, double secondaryS, double secondaryL) {
        JSONObject jsonParameters = new JSONObject();
        try {
            jsonParameters.put("picture", picture);
            jsonParameters.put("description", description);
            jsonParameters.put("primary_hue", primaryH);
            jsonParameters.put("primary_saturation", primaryS);
            jsonParameters.put("primary_lightness", primaryL);
            jsonParameters.put("secondary_hue", secondaryH);
            jsonParameters.put("secondary_saturation", secondaryS);
            jsonParameters.put("secondary_lightness", secondaryL);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return Rx2AndroidNetworking.post(ROOT_URL + "/profile")
                .addJSONObjectBody(jsonParameters)
                .addHeaders("Authorization", "Bearer " + preferences.getAuthToken())
                .build()
                .getStringSingle();
    }

    public Single<String> setCoordinates(double latitude, double longitude) {
        JSONObject jsonParameters = new JSONObject();
        try {
            jsonParameters.put("latitude", latitude);
            jsonParameters.put("longitude", longitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return Rx2AndroidNetworking.post(ROOT_URL + "/coordinates")
                .addJSONObjectBody(jsonParameters)
                .addHeaders("Authorization", "Bearer " + preferences.getAuthToken())
                .build()
                .getStringSingle();
    }

    // User interaction methods

    public Single<String> followUser(String userId) {
        return Rx2AndroidNetworking.post(ROOT_URL + "/" + userId + "/follow")
                .addHeaders("Authorization", "Bearer " + preferences.getAuthToken())
                .build()
                .getStringSingle();
    }

    public Single<String> unfollowUser(String userId) {
        return Rx2AndroidNetworking.delete(ROOT_URL + "/" + userId + "/follow")
                .addHeaders("Authorization", "Bearer " + preferences.getAuthToken())
                .build()
                .getStringSingle();
    }

    public Single<String> blockUser(String userId) {
        return Rx2AndroidNetworking.post(ROOT_URL + "/" + userId + "/block")
                .addHeaders("Authorization", "Bearer " + preferences.getAuthToken())
                .build()
                .getStringSingle();
    }

    public Single<String> unblockUser(String userId) {
        return Rx2AndroidNetworking.delete(ROOT_URL + "/" + userId + "/block")
                .addHeaders("Authorization", "Bearer " + preferences.getAuthToken())
                .build()
                .getStringSingle();
    }

    public Single<String> acceptFollower(String followerId) {
        return Rx2AndroidNetworking.post(ROOT_URL + "/accept/" + followerId)
                .addHeaders("Authorization", "Bearer " + preferences.getAuthToken())
                .build()
                .getStringSingle();
    }

    public Single<String> rejectFollower(String followerId) {
        return Rx2AndroidNetworking.post(ROOT_URL + "/reject/" + followerId)
                .addHeaders("Authorization", "Bearer " + preferences.getAuthToken())
                .build()
                .getStringSingle();
    }
}
