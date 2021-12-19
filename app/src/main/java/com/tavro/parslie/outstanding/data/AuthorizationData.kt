package com.tavro.parslie.outstanding.data

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class AuthorizationData(
    @SerializedName("expiry") val expiry: String,
    @SerializedName("token") val token: String,
    @SerializedName("user") val user: User
)
