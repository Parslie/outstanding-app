package com.tavro.parslie.outstanding.data.repository

import android.content.Context
import com.tavro.parslie.outstanding.data.API
import com.tavro.parslie.outstanding.data.model.AuthorizationData
import io.reactivex.Single
import org.json.JSONObject

class UserRepository(context: Context) {

    private val prefs = PreferenceRepository(context)

    fun login(email: String, password: String): Single<AuthorizationData> {
        val json = JSONObject()
        json.put("email", email)
        json.put("password", password)
        return API.post("login", json).getObjectSingle(AuthorizationData::class.java)
    }

    fun register(email: String, username: String, password: String): Single<String> {
        val json = JSONObject()
        json.put("email", email)
        json.put("username", username)
        json.put("password", password)
        return API.post("register", json).stringSingle
    }

    fun logout(): Single<String> {
        return API.post("logout", null, prefs.authToken).stringSingle
    }
}