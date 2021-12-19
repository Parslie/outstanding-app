package com.tavro.parslie.outstanding.data.api

import android.content.Context
import com.rx2androidnetworking.Rx2ANRequest
import com.rx2androidnetworking.Rx2AndroidNetworking
import org.json.JSONObject

abstract class API(context: Context, basePath: String = "") {

    private val baseURL: String = if (basePath == "")
        "https://outstanding-server.herokuapp.com"
    else
        "https://outstanding-server.herokuapp.com/$basePath"

    private fun getURL(path: String): String {
        return if (path != "")
            "$baseURL/$path/"
        else
            "$baseURL/"
    }

    protected fun get(path: String, requireAuth: Boolean): Rx2ANRequest {
        val builder = Rx2AndroidNetworking.get(getURL(path))
        if (requireAuth)
            builder.addHeaders("Authorization", "Token ASDASFASDAAS")  // TODO: add way to get token
        return builder.build()
    }

    protected fun post(path: String, requireAuth: Boolean, json: JSONObject?): Rx2ANRequest {
        val builder = Rx2AndroidNetworking.post(getURL(path))

        if (json != null) {
            builder.addHeaders("Content-Type", "application/json")
            builder.addJSONObjectBody(json)
        }
        if (requireAuth)
            builder.addHeaders("Authorization", "Token ASDASFASDAAS")  // TODO: add way to get token

        return builder.build()
    }

    protected fun put(path: String, requireAuth: Boolean, json: JSONObject?): Rx2ANRequest {
        val builder = Rx2AndroidNetworking.put(getURL(path))

        if (json != null) {
            builder.addHeaders("Content-Type", "application/json")
            builder.addJSONObjectBody(json)
        }
        if (requireAuth)
            builder.addHeaders("Authorization", "Token ASDASFASDAAS")  // TODO: add way to get token

        return builder.build()
    }

    protected fun delete(path: String, requireAuth: Boolean, json: JSONObject?): Rx2ANRequest {
        val builder = Rx2AndroidNetworking.delete(getURL(path))

        if (json != null) {
            builder.addHeaders("Content-Type", "application/json")
            builder.addJSONObjectBody(json)
        }
        if (requireAuth)
            builder.addHeaders("Authorization", "Token ASDASFASDAAS")  // TODO: add way to get token

        return builder.build()
    }
}