package com.vikho305.isaho220.outstanding.data

import com.google.gson.annotations.SerializedName

class User {
    @SerializedName(value = "is_self")
    val isSelf = false

    @SerializedName(value = "is_following")
    val isFollowing = false

    @SerializedName(value = "is_pending_following")
    val isPendingFollowing = false

    @SerializedName(value = "is_blocking")
    val isBlocking = false
    val id: String? = null
    val username: String? = null
    val email: String? = null
    val picture: String? = null
    val description: String? = null

    @SerializedName(value = "primary_hue")
    val primaryHue = 0.0

    @SerializedName(value = "primary_saturation")
    val primarySaturation = 0.0

    @SerializedName(value = "primary_lightness")
    val primaryLightness = 0.0

    @SerializedName(value = "secondary_hue")
    val secondaryHue = 0.0

    @SerializedName(value = "secondary_saturation")
    val secondarySaturation = 0.0

    @SerializedName(value = "secondary_lightness")
    val secondaryLightness = 0.0
    val latitude = 0.0
    val longitude = 0.0

    @SerializedName(value = "date_created")
    val dateCreated: String? = null

    @SerializedName(value = "follower_count")
    val followerCount = 0

    @SerializedName(value = "following_count")
    val followingCount = 0

    @SerializedName(value = "pending_follower_count")
    val pendingFollowerCount = 0

    @SerializedName(value = "pending_following_count")
    val pendingFollowingCount = 0
}