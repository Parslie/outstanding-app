package com.tavro.parslie.outstanding.data.model

import com.google.gson.annotations.SerializedName

data class Post(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("content") val content: String,
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double,
    @SerializedName("author") val author: User,
    @SerializedName("date_created") val dateCreated: String
)
