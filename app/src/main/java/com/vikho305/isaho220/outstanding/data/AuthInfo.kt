package com.vikho305.isaho220.outstanding.data

import com.google.gson.annotations.SerializedName

class AuthInfo {
    @SerializedName(value = "auth_token")
    val authToken: String? = null

    @SerializedName(value = "auth_user_id")
    val authUserId: String? = null
}