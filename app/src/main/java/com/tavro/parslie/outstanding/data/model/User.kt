package com.tavro.parslie.outstanding.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Parcelize
data class User(
    @SerializedName("id") val id: Int,
    @SerializedName("email") val email: String,
    @SerializedName("username") val username: String,
    @SerializedName("description") val description: String,
    @SerializedName("date_joined") val dateJoined: String
): Parcelable {
    fun formatDateJoined(pattern: String): String {
        val zdt = ZonedDateTime.parse(dateJoined)
        return zdt.format(DateTimeFormatter.ofPattern(pattern))
    }
}
