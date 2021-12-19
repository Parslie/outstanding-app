package com.tavro.parslie.outstanding.data.api

import android.content.Context
import com.tavro.parslie.outstanding.data.AuthorizationData
import io.reactivex.Single
import org.json.JSONObject

class UserAPI(context: Context): API(context, "accounts") {

    fun login(email: String, password: String): Single<AuthorizationData> {
        val jsonParameters = JSONObject()
        jsonParameters.put("email", email)
        jsonParameters.put("password", password)
        return post("login", false, jsonParameters).getObjectSingle(AuthorizationData::class.java)
    }

    fun register(email: String, username: String, password: String): Single<String> {
        val jsonParameters = JSONObject()
        jsonParameters.put("email", email)
        jsonParameters.put("username", username)
        jsonParameters.put("password", password)
        return post("register", false, jsonParameters).stringSingle
    }

    fun logout(): Single<String> {
        return post("logout", true, null).stringSingle
    }
}