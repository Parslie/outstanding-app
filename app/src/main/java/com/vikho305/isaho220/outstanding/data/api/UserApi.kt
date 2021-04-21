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

class UserApi(context: Context) {
    companion object {
        private const val ROOT_URL = "https://outstanding-server.herokuapp.com/user"
    }

    private val preferences: PreferenceRepository = PreferenceRepository(context)

    fun register(username: String?, email: String?, password: String?): Single<String> {
        val jsonParameters = JSONObject()
        try {
            jsonParameters.put("username", username)
            jsonParameters.put("email", email)
            jsonParameters.put("password", password)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return Rx2AndroidNetworking.post(ROOT_URL)
                .addJSONObjectBody(jsonParameters)
                .build()
                .stringSingle
    }

    fun login(username: String?, password: String?): Single<AuthInfo> {
        val jsonParameters = JSONObject()
        try {
            jsonParameters.put("username", username)
            jsonParameters.put("password", password)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return Rx2AndroidNetworking.post(ROOT_URL + "/login")
                .addJSONObjectBody(jsonParameters)
                .build()
                .getObjectSingle(AuthInfo::class.java)
    }

    fun logout(): Single<String> {
        return Rx2AndroidNetworking.post(ROOT_URL + "/logout")
                .addHeaders("Authorization", "Bearer " + preferences.authToken)
                .build()
                .stringSingle
    }

    /////////////////
    // Getter methods

    fun getUser(userId: String): Single<User> {
        return Rx2AndroidNetworking.get(ROOT_URL + "/" + userId)
                .addHeaders("Authorization", "Bearer " + preferences.authToken)
                .build()
                .getObjectSingle(User::class.java)
    }

    fun getFollowers(userId: String, page: Int): Single<List<User>> {
        return Rx2AndroidNetworking.get(ROOT_URL + "/" + userId + "/followers/" + page)
                .addHeaders("Authorization", "Bearer " + preferences.authToken)
                .build()
                .getObjectListSingle(User::class.java)
    }

    fun getFollowings(userId: String, page: Int): Single<List<User>> {
        return Rx2AndroidNetworking.get(ROOT_URL + "/" + userId + "/followings/" + page)
                .addHeaders("Authorization", "Bearer " + preferences.authToken)
                .build()
                .getObjectListSingle(User::class.java)
    }

    fun getPendingFollowers(userId: String, page: Int): Single<List<User>> {
        return Rx2AndroidNetworking.get(ROOT_URL + "/" + userId + "/followers/pending/" + page)
                .addHeaders("Authorization", "Bearer " + preferences.authToken)
                .build()
                .getObjectListSingle(User::class.java)
    }

    fun getPendingFollowings(userId: String, page: Int): Single<List<User>> {
        return Rx2AndroidNetworking.get(ROOT_URL + "/" + userId + "/followings/pending/" + page)
                .addHeaders("Authorization", "Bearer " + preferences.authToken)
                .build()
                .getObjectListSingle(User::class.java)
    }

    fun getBlockings(userId: String, page: Int): Single<List<User>> {
        return Rx2AndroidNetworking.get(ROOT_URL + "/" + userId + "/blockings/" + page)
                .addHeaders("Authorization", "Bearer " + preferences.authToken)
                .build()
                .getObjectListSingle(User::class.java)
    }

    fun getPosts(userId: String, page: Int): Single<List<Post>> {
        return Rx2AndroidNetworking.get(ROOT_URL + "/" + userId + "/posts/" + page)
                .addHeaders("Authorization", "Bearer " + preferences.authToken)
                .build()
                .getObjectListSingle(Post::class.java)
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
        return Rx2AndroidNetworking.post(ROOT_URL + "/account")
                .addJSONObjectBody(jsonParameters)
                .addHeaders("Authorization", "Bearer " + preferences.authToken)
                .build()
                .stringSingle
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
        return Rx2AndroidNetworking.post(ROOT_URL + "/account")
                .addJSONObjectBody(jsonParameters)
                .addHeaders("Authorization", "Bearer " + preferences.authToken)
                .build()
                .stringSingle
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
        return Rx2AndroidNetworking.post(ROOT_URL + "/profile")
                .addJSONObjectBody(jsonParameters)
                .addHeaders("Authorization", "Bearer " + preferences.authToken)
                .build()
                .stringSingle
    }

    fun setCoordinates(latitude: Double, longitude: Double): Single<String> {
        val jsonParameters = JSONObject()
        try {
            jsonParameters.put("latitude", latitude)
            jsonParameters.put("longitude", longitude)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return Rx2AndroidNetworking.post(ROOT_URL + "/coordinates")
                .addJSONObjectBody(jsonParameters)
                .addHeaders("Authorization", "Bearer " + preferences.authToken)
                .build()
                .stringSingle
    }

    ///////////////////////////
    // User interaction methods

    fun followUser(userId: String): Single<String> {
        return Rx2AndroidNetworking.post(ROOT_URL + "/" + userId + "/follow")
                .addHeaders("Authorization", "Bearer " + preferences.authToken)
                .build()
                .stringSingle
    }

    fun unfollowUser(userId: String): Single<String> {
        return Rx2AndroidNetworking.delete(ROOT_URL + "/" + userId + "/follow")
                .addHeaders("Authorization", "Bearer " + preferences.authToken)
                .build()
                .stringSingle
    }

    fun blockUser(userId: String): Single<String> {
        return Rx2AndroidNetworking.post(ROOT_URL + "/" + userId + "/block")
                .addHeaders("Authorization", "Bearer " + preferences.authToken)
                .build()
                .stringSingle
    }

    fun unblockUser(userId: String): Single<String> {
        return Rx2AndroidNetworking.delete(ROOT_URL + "/" + userId + "/block")
                .addHeaders("Authorization", "Bearer " + preferences.authToken)
                .build()
                .stringSingle
    }

    fun acceptFollower(followerId: String): Single<String> {
        return Rx2AndroidNetworking.post(ROOT_URL + "/accept/" + followerId)
                .addHeaders("Authorization", "Bearer " + preferences.authToken)
                .build()
                .stringSingle
    }

    fun rejectFollower(followerId: String): Single<String> {
        return Rx2AndroidNetworking.post(ROOT_URL + "/reject/" + followerId)
                .addHeaders("Authorization", "Bearer " + preferences.authToken)
                .build()
                .stringSingle
    }


}