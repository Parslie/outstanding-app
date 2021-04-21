package com.vikho305.isaho220.outstanding.data.api

import android.content.Context
import com.rx2androidnetworking.Rx2ANRequest
import com.rx2androidnetworking.Rx2AndroidNetworking
import com.vikho305.isaho220.outstanding.data.repositories.PreferenceRepository
import org.json.JSONObject

abstract class Api(context: Context, basePath: String = "") {
    private val preferences = PreferenceRepository(context)
    private val baseUrl = if (basePath == "")
        "https://outstanding-server.herokuapp.com"
    else
        "https://outstanding-server.herokuapp.com/$basePath"

    private fun getUrl(path: String): String {
        return if (path != "")
            "$baseUrl/$path"
        else
            baseUrl
    }

    protected fun get(path: String, requireAuth: Boolean = false): Rx2ANRequest {
        val builder = Rx2AndroidNetworking.get(getUrl(path))

        if (requireAuth)
            builder.addHeaders("Authorization", "Bearer " + preferences.authToken)

        return builder.build()
    }

    protected fun post(path: String, requireAuth: Boolean = false, json: JSONObject? = null): Rx2ANRequest {
        val builder = Rx2AndroidNetworking.post(getUrl(path))

        if (requireAuth)
            builder.addHeaders("Authorization", "Bearer " + preferences.authToken)
        if (json != null)
            builder.addJSONObjectBody(json)

        return builder.build()
    }

    protected fun delete(path: String, requireAuth: Boolean = false, json: JSONObject? = null): Rx2ANRequest {
        val builder = Rx2AndroidNetworking.delete(getUrl(path))

        if (requireAuth)
            builder.addHeaders("Authorization", "Bearer " + preferences.authToken)
        if (json != null)
            builder.addJSONObjectBody(json)

        return builder.build()
    }
}