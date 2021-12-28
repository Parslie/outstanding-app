package com.tavro.parslie.outstanding.data

import com.rx2androidnetworking.Rx2ANRequest
import com.rx2androidnetworking.Rx2AndroidNetworking
import org.json.JSONObject

object API {
    private const val baseURL = "https://outstanding-server.herokuapp.com/"

    fun get(path: String, token: String? = null): Rx2ANRequest {
        val builder = Rx2AndroidNetworking.get(baseURL + path)

        if (token != null)
            builder.addHeaders("Authorization", "Token $token")

        return builder.build()
    }

    fun post(path: String, json: JSONObject? = null, token: String? = null): Rx2ANRequest {
        val builder = Rx2AndroidNetworking.post(baseURL + path)

        if (token != null)
            builder.addHeaders("Authorization", "Token $token")
        if (json != null) {
            builder.addHeaders("Content-Type", "application/json")
            builder.addJSONObjectBody(json)
        }

        return builder.build()
    }

    fun put(path: String, json: JSONObject? = null, token: String? = null): Rx2ANRequest {
        val builder = Rx2AndroidNetworking.put(baseURL + path)

        if (token != null)
            builder.addHeaders("Authorization", "Token $token")
        if (json != null) {
            builder.addHeaders("Content-Type", "application/json")
            builder.addJSONObjectBody(json)
        }

        return builder.build()
    }

    fun delete(path: String, json: JSONObject? = null, token: String? = null): Rx2ANRequest {
        val builder = Rx2AndroidNetworking.delete(baseURL + path)

        if (token != null)
            builder.addHeaders("Authorization", "Token $token")
        if (json != null) {
            builder.addHeaders("Content-Type", "application/json")
            builder.addJSONObjectBody(json)
        }

        return builder.build()
    }
}