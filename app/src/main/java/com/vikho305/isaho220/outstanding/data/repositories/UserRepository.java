package com.vikho305.isaho220.outstanding.data.repositories;

import android.content.Context;

import com.vikho305.isaho220.outstanding.data.AuthInfo;
import com.vikho305.isaho220.outstanding.data.Post;
import com.vikho305.isaho220.outstanding.data.User;
import com.vikho305.isaho220.outstanding.data.api.UserApi;

import java.util.List;

import io.reactivex.Single;

public class UserRepository {

    private UserApi userApi;
    private PreferenceRepository preferences;

    public UserRepository(Context context) {
        userApi = new UserApi(context);
        preferences = new PreferenceRepository(context);
    }

    public Single<String> register(String username, String email, String password) {
        return userApi.register(username, email, password);
    }

    public Single<AuthInfo> login(String username, String password) {
        return userApi.login(username, password);
    }

    public Single<String> logout() {
        return userApi.logout();
    }

    // Getter methods

    public Single<User> getUser(String userId) {
        return userApi.getUser(userId);
    }

    public Single<User> getUserSelf() {
        return userApi.getUser(preferences.getAuthUserId());
    }

    // TODO: add more self methods

    public Single<List<User>> getFollowers(String userId, int page) {
        return userApi.getFollowers(userId, page);
    }

    public Single<List<User>> getFollowings(String userId, int page) {
        return userApi.getFollowings(userId, page);
    }

    public Single<List<User>> getPendingFollowers(String userId, int page) {
        return userApi.getPendingFollowers(userId, page);
    }

    public Single<List<User>> getPendingFollowings(String userId, int page) {
        return userApi.getPendingFollowings(userId, page);
    }

    public Single<List<User>> getBlockings(String userId, int page) {
        return userApi.getBlockings(userId, page);
    }

    public Single<List<Post>> getPosts(String userId, int page) {
        return userApi.getPosts(userId, page);
    }

    // Setter methods

    public Single<String> setAccountDetails(String username, String email) {
        return userApi.setAccountDetails(username, email);
    }

    public Single<String> setAccountDetails(String username, String email, String password) {
        return userApi.setAccountDetails(username, email, password);
    }

    public Single<String> setProfileDetails(String picture, String description,
                                            double primaryH, double primaryS, double primaryL,
                                            double secondaryH, double secondaryS, double secondaryL) {
        return userApi.setProfileDetails(picture, description,
                primaryH, primaryS, primaryL, secondaryH, secondaryS, secondaryL);
    }

    public Single<String> setCoordinates(double latitude, double longitude) {
        return userApi.setCoordinates(latitude, longitude);
    }

    // User interaction methods

    public Single<String> followUser(String userId) {
        return userApi.followUser(userId);
    }

    public Single<String> unfollowUser(String userId) {
        return userApi.unfollowUser(userId);
    }

    public Single<String> blockUser(String userId) {
        return userApi.blockUser(userId);
    }

    public Single<String> unblockUser(String userId) {
        return userApi.unblockUser(userId);
    }

    public Single<String> acceptFollower(String followerId) {
        return userApi.acceptFollower(followerId);
    }

    public Single<String> rejectFollower(String followerId) {
        return userApi.rejectFollower(followerId);
    }
}
