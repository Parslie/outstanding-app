package com.vikho305.isaho220.outstanding.data.api

import android.content.Context
import com.rx2androidnetworking.Rx2AndroidNetworking
import com.vikho305.isaho220.outstanding.data.AuthInfo
import com.vikho305.isaho220.outstanding.data.Post
import com.vikho305.isaho220.outstanding.data.User
import com.vikho305.isaho220.outstanding.data.repositories.PreferenceRepository
import io.reactivex.Single
import org.json.JSONException
import org.json.JSONObject

class UserApi(context: Context) : Api(context, "user") {
    fun register(username: String?, email: String?, password: String?): Single<String> {
        val jsonParameters = JSONObject()

        try {
            jsonParameters.put("username", username)
            jsonParameters.put("email", email)
            jsonParameters.put("password", password)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return post("", false, jsonParameters).stringSingle
    }

    fun login(username: String?, password: String?): Single<AuthInfo> {
        val jsonParameters = JSONObject()

        try {
            jsonParameters.put("username", username)
            jsonParameters.put("password", password)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return post("login", false, jsonParameters).getObjectSingle(AuthInfo::class.java)
    }

    fun logout(): Single<String> {
        return post("logout", true).stringSingle
    }

    /////////////////
    // Getter methods

    fun getUser(userId: String): Single<User> {
        return get(userId, true).getObjectSingle(User::class.java)
    }

    fun getFollowers(userId: String, page: Int): Single<List<User>> {
        return get("$userId/followers/$page", true).getObjectListSingle(User::class.java)
    }

    fun getFollowings(userId: String, page: Int): Single<List<User>> {
        return get("$userId/followings/$page", true).getObjectListSingle(User::class.java)
    }

    fun getPendingFollowers(userId: String, page: Int): Single<List<User>> {
        return get("$userId/followers/pending/$page", true).getObjectListSingle(User::class.java)
    }

    fun getPendingFollowings(userId: String, page: Int): Single<List<User>> {
        return get("$userId/followings/pending/$page", true).getObjectListSingle(User::class.java)
    }

    fun getBlockings(userId: String, page: Int): Single<List<User>> {
        return get("$userId/blockings/$page", true).getObjectListSingle(User::class.java)
    }

    fun getPosts(userId: String, page: Int): Single<List<Post>> {
        return get("$userId/posts/$page", true).getObjectListSingle(Post::class.java)
    }

    /////////////////
    // Setter methods

    fun setAccountDetails(username: String?, email: String?): Single<String> {
        val jsonParameters = JSONObject()

        try {
            jsonParameters.put("username", username)
            jsonParameters.put("email", email)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return post("account", true, jsonParameters).stringSingle
    }

    fun setAccountDetails(username: String?, email: String?, password: String?): Single<String> {
        val jsonParameters = JSONObject()

        try {
            jsonParameters.put("username", username)
            jsonParameters.put("email", email)
            jsonParameters.put("password", password)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return post("account", true, jsonParameters).stringSingle
    }

    fun setProfileDetails(picture: String?, description: String?,
                          primaryH: Double, primaryS: Double, primaryL: Double,
                          secondaryH: Double, secondaryS: Double, secondaryL: Double): Single<String> {
        val jsonParameters = JSONObject()

        try {
            jsonParameters.put("picture", picture)
            jsonParameters.put("description", description)
            jsonParameters.put("primary_hue", primaryH)
            jsonParameters.put("primary_saturation", primaryS)
            jsonParameters.put("primary_lightness", primaryL)
            jsonParameters.put("secondary_hue", secondaryH)
            jsonParameters.put("secondary_saturation", secondaryS)
            jsonParameters.put("secondary_lightness", secondaryL)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return post("profile", true, jsonParameters).stringSingle
    }

    fun setCoordinates(latitude: Double, longitude: Double): Single<String> {
        val jsonParameters = JSONObject()

        try {
            jsonParameters.put("latitude", latitude)
            jsonParameters.put("longitude", longitude)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return post("coordinates", true, jsonParameters).stringSingle
    }

    ///////////////////////////
    // User interaction methods

    fun followUser(userId: String): Single<String> {
        return post("$userId/follow", true).stringSingle
    }

    fun unfollowUser(userId: String): Single<String> {
        return delete("$userId/follow", true).stringSingle
    }

    fun blockUser(userId: String): Single<String> {
        return post("$userId/block", true).stringSingle
    }

    fun unblockUser(userId: String): Single<String> {
        return delete("$userId/block", true).stringSingle
    }

    fun acceptFollower(followerId: String): Single<String> {
        return post("accept/$followerId", true).stringSingle
    }

    fun rejectFollower(followerId: String): Single<String> {
        return post("reject/$followerId", true).stringSingle
    }
}