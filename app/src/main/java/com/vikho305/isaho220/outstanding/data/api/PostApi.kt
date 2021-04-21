package com.vikho305.isaho220.outstanding.data.api

import android.content.Context
import com.rx2androidnetworking.Rx2AndroidNetworking
import com.vikho305.isaho220.outstanding.data.Comment
import com.vikho305.isaho220.outstanding.data.Post
import com.vikho305.isaho220.outstanding.data.repositories.PreferenceRepository
import io.reactivex.Single
import org.json.JSONException
import org.json.JSONObject

class PostApi(context: Context) {
    companion object {
        private const val ROOT_URL = "https://outstanding-server.herokuapp.com/post"
    }

    private val preferences: PreferenceRepository = PreferenceRepository(context)

    fun getPost(postId: String): Single<Post> {
        return Rx2AndroidNetworking.get(ROOT_URL + "/" + postId)
                .addHeaders("Authorization", "Bearer " + preferences.authToken)
                .build()
                .getObjectSingle(Post::class.java)
    }

    fun getPostsInRadius(radius: Double): Single<List<Post>> {
        return Rx2AndroidNetworking.get(ROOT_URL + "/all/" + radius)
                .addHeaders("Authorization", "Bearer " + preferences.authToken)
                .build()
                .getObjectListSingle(Post::class.java)
    }

    fun getComments(postId: String, page: Int): Single<List<Comment>> {
        return Rx2AndroidNetworking.get(ROOT_URL + "/" + postId + "/comments/" + page)
                .addHeaders("Authorization", "Bearer " + preferences.authToken)
                .build()
                .getObjectListSingle(Comment::class.java)
    }

    fun postPost(title: String?, text: String?, media: String?, mediaType: String?,
                 latitude: Double, longitude: Double): Single<String> {
        val jsonParameters = JSONObject()
        try {
            jsonParameters.put("title", title)
            jsonParameters.put("text", text)
            jsonParameters.put("media", media)
            jsonParameters.put("media_type", mediaType)
            jsonParameters.put("latitude", latitude)
            jsonParameters.put("longitude", longitude)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return Rx2AndroidNetworking.post(ROOT_URL)
                .addHeaders("Authorization", "Bearer " + preferences.authToken)
                .addJSONObjectBody(jsonParameters)
                .build()
                .stringSingle
    }

    fun postComment(postId: String, text: String?): Single<String> {
        val jsonParameters = JSONObject()
        try {
            jsonParameters.put("text", text)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return Rx2AndroidNetworking.post(ROOT_URL + "/" + postId + "/comment")
                .addHeaders("Authorization", "Bearer " + preferences.authToken)
                .addJSONObjectBody(jsonParameters)
                .build()
                .stringSingle
    }

    fun likePost(postId: String): Single<String> {
        return Rx2AndroidNetworking.post(ROOT_URL + "/" + postId + "/like")
                .addHeaders("Authorization", "Bearer " + preferences.authToken)
                .build()
                .stringSingle
    }

    fun dislikePost(postId: String): Single<String> {
        return Rx2AndroidNetworking.post(ROOT_URL + "/" + postId + "/dislike")
                .addHeaders("Authorization", "Bearer " + preferences.authToken)
                .build()
                .stringSingle
    }

    fun unlikePost(postId: String): Single<String> {
        return Rx2AndroidNetworking.delete(ROOT_URL + "/" + postId + "/like")
                .addHeaders("Authorization", "Bearer " + preferences.authToken)
                .build()
                .stringSingle
    }

    fun undislikePost(postId: String): Single<String> {
        return Rx2AndroidNetworking.delete(ROOT_URL + "/" + postId + "/dislike")
                .addHeaders("Authorization", "Bearer " + preferences.authToken)
                .build()
                .stringSingle
    }
}