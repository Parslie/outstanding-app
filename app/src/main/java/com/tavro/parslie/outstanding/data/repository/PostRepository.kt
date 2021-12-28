package com.tavro.parslie.outstanding.data.repository

import android.content.Context
import com.google.android.gms.location.LocationServices
import com.tavro.parslie.outstanding.data.API
import com.tavro.parslie.outstanding.data.model.Post
import io.reactivex.Single
import org.json.JSONObject

class PostRepository(context: Context) {
    private val prefs = PreferenceRepository(context)

    fun createPost(title: String, content: String, latitude: Double, longitude: Double): Single<Post> {
        val data = JSONObject()
        data.put("title", title)
        data.put("content", content)
        data.put("latitude", latitude)
        data.put("longitude", longitude)
        return API.post("posts/", data, prefs.authToken).getObjectSingle(Post::class.java)
    }
}