package com.vikho305.isaho220.outstanding.data

import com.google.gson.annotations.SerializedName

class Comment {
    val id: String? = null
    val text: String? = null

    @SerializedName(value = "date_created")
    val dateCreated: String? = null
    val post: Post? = null
    val author: User? = null
}