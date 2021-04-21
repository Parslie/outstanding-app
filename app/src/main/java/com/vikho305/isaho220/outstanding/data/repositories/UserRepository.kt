package com.vikho305.isaho220.outstanding.data.repositories

import android.content.Context
import com.vikho305.isaho220.outstanding.data.api.UserApi
import com.vikho305.isaho220.outstanding.data.AuthInfo
import com.vikho305.isaho220.outstanding.data.Post
import com.vikho305.isaho220.outstanding.data.User
import io.reactivex.Single

class UserRepository(context: Context) {
    private val userApi: UserApi = UserApi(context)
    private val preferences: PreferenceRepository = PreferenceRepository(context)

    fun register(username: String?, email: String?, password: String?): Single<String> {
        return userApi.register(username, email, password)
    }

    fun login(username: String?, password: String?): Single<AuthInfo> {
        return userApi.login(username, password)
    }

    fun logout(): Single<String> {
        return userApi.logout()
    }

    /////////////////
    // Getter methods

    fun getUser(userId: String): Single<User> {
        return userApi.getUser(userId)
    }

    fun getUserSelf(): Single<User> {
        return userApi.getUser(preferences.authUserId!!)
    }

    fun getFollowers(userId: String, page: Int): Single<List<User>> {
        return userApi.getFollowers(userId, page)
    }

    fun getFollowings(userId: String, page: Int): Single<List<User>> {
        return userApi.getFollowings(userId, page)
    }

    fun getPendingFollowers(userId: String, page: Int): Single<List<User>> {
        return userApi.getPendingFollowers(userId, page)
    }

    fun getPendingFollowings(userId: String, page: Int): Single<List<User>> {
        return userApi.getPendingFollowings(userId, page)
    }

    fun getBlockings(userId: String, page: Int): Single<List<User>> {
        return userApi.getBlockings(userId, page)
    }

    fun getPosts(userId: String, page: Int): Single<List<Post>> {
        return userApi.getPosts(userId, page)
    }

    /////////////////
    // Setter methods

    fun setAccountDetails(username: String?, email: String?): Single<String> {
        return userApi.setAccountDetails(username, email)
    }

    fun setAccountDetails(username: String?, email: String?, password: String?): Single<String> {
        return userApi.setAccountDetails(username, email, password)
    }

    fun setProfileDetails(picture: String?, description: String?,
                          primaryH: Double, primaryS: Double, primaryL: Double,
                          secondaryH: Double, secondaryS: Double, secondaryL: Double): Single<String> {
        return userApi.setProfileDetails(picture, description,
                primaryH, primaryS, primaryL, secondaryH, secondaryS, secondaryL)
    }

    fun setCoordinates(latitude: Double, longitude: Double): Single<String> {
        return userApi.setCoordinates(latitude, longitude)
    }

    ///////////////////////////
    // User interaction methods

    fun followUser(userId: String): Single<String> {
        return userApi.followUser(userId)
    }

    fun unfollowUser(userId: String): Single<String> {
        return userApi.unfollowUser(userId)
    }

    fun blockUser(userId: String): Single<String> {
        return userApi.blockUser(userId)
    }

    fun unblockUser(userId: String): Single<String> {
        return userApi.unblockUser(userId)
    }

    fun acceptFollower(followerId: String): Single<String> {
        return userApi.acceptFollower(followerId)
    }

    fun rejectFollower(followerId: String): Single<String> {
        return userApi.rejectFollower(followerId)
    }
}