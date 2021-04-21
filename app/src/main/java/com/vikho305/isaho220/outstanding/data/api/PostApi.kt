package com.vikho305.isaho220.outstanding.data.api

import android.content.Context
import com.rx2androidnetworking.Rx2AndroidNetworking
import com.vikho305.isaho220.outstanding.data.Comment
import com.vikho305.isaho220.outstanding.data.Post
import com.vikho305.isaho220.outstanding.data.repositories.PreferenceRepository
import io.reactivex.Single
import org.json.JSONException
import org.json.JSONObject

class PostApi(context: Context) : Api(context,"post") {
    fun getPost(postId: String): Single<Post> {
        return get(postId, true).getObjectSingle(Post::class.java)
    }

    fun getPostsInRadius(radius: Double): Single<List<Post>> {
        return get("all/$radius", true).getObjectListSingle(Post::class.java)
    }

    fun getComments(postId: String, page: Int): Single<List<Comment>> {
        return get("$postId/comments/$page", true).getObjectListSingle(Comment::class.java)
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

        return post("", true, jsonParameters).stringSingle
    }

    fun postComment(postId: String, text: String?): Single<String> {
        val jsonParameters = JSONObject()

        try {
            jsonParameters.put("text", text)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return post("$postId/comment", true, jsonParameters).stringSingle
    }

    fun likePost(postId: String): Single<String> {
        return post("$postId/like", true).stringSingle
    }

    fun dislikePost(postId: String): Single<String> {
        return post("$postId/dislike", true).stringSingle
    }

    fun unlikePost(postId: String): Single<String> {
        return delete("$postId/like", true).stringSingle
    }

    fun undislikePost(postId: String): Single<String> {
        return delete("$postId/dislike", true).stringSingle
    }
}