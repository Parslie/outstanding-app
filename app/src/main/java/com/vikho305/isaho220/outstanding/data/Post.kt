package com.vikho305.isaho220.outstanding.data

import com.google.gson.annotations.SerializedName

class Post {
    @SerializedName(value = "is_liked")
    val isLiked = false

    @SerializedName(value = "is_disliked")
    val isDisliked = false
    val id: String? = null
    val title: String? = null
    val text: String? = null
    val media: String? = null

    @SerializedName(value = "media_type")
    val mediaType: String? = null
    val latitude = 0.0
    val longitude = 0.0

    @SerializedName(value = "date_created")
    val dateCreated: String? = null
    val author: User? = null

    @SerializedName(value = "like_count")
    val likeCount = 0

    @SerializedName(value = "dislike_count")
    val dislikeCount = 0

    companion object {
        const val TEXT_TYPE = "text"
        const val IMAGE_TYPE = "image"
    }
}