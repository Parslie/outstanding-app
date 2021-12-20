package com.tavro.parslie.outstanding.data.model

import com.google.gson.annotations.SerializedName

data class AuthorizationData(
    @SerializedName("expiry") val expiry: String,
    @SerializedName("token") val token: String,
    @SerializedName("user") val user: User
)
