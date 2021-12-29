package com.tavro.parslie.outstanding.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Parcelize
data class Post(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("content") val content: String,
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double,
    @SerializedName("author") val author: User,
    @SerializedName("date_created") val dateCreated: String
): Parcelable {
    fun formatDateCreated(pattern: String): String {
        val zdt = ZonedDateTime.parse(dateCreated)
        return zdt.format(DateTimeFormatter.ofPattern(pattern))
    }
}
