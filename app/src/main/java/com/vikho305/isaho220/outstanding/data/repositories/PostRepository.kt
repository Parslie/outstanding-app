package com.vikho305.isaho220.outstanding.data.repositories

import android.content.Context
import com.vikho305.isaho220.outstanding.data.Comment
import com.vikho305.isaho220.outstanding.data.Post
import com.vikho305.isaho220.outstanding.data.api.PostApi
import io.reactivex.Single

class PostRepository(context: Context) {
    private val postApi: PostApi = PostApi(context)

    fun getPost(postId: String): Single<Post> {
        return postApi.getPost(postId)
    }

    fun getPostsInRadius(radius: Double): Single<List<Post>> {
        return postApi.getPostsInRadius(radius)
    }

    fun getComments(postId: String, page: Int): Single<List<Comment>> {
        return postApi.getComments(postId, page)
    }

    fun postPost(title: String?, text: String?, media: String?, mediaType: String?,
                 latitude: Double, longitude: Double): Single<String> {
        return postApi.postPost(title, text, media, mediaType, latitude, longitude)
    }

    fun postComment(postId: String, text: String?): Single<String> {
        return postApi.postComment(postId, text)
    }

    fun likePost(postId: String): Single<String> {
        return postApi.likePost(postId)
    }

    fun dislikePost(postId: String): Single<String> {
        return postApi.dislikePost(postId)
    }

    fun unlikePost(postId: String): Single<String> {
        return postApi.unlikePost(postId)
    }

    fun undislikePost(postId: String): Single<String> {
        return postApi.undislikePost(postId)
    }

}