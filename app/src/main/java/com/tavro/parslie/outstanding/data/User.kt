package com.tavro.parslie.outstanding.data

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id") val id: Int,
    @SerializedName("email") val email: String,
    @SerializedName("username") val username: String,
    @SerializedName("description") val description: String,
    @SerializedName("date_joined") val dateJoined: String
)
