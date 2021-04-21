package com.vikho305.isaho220.outstanding.data.repositories

import android.content.Context
import android.content.SharedPreferences

class PreferenceRepository(context: Context) {
    companion object {
        private const val FILE_NAME = "preferences"
        private const val AUTH_TOKEN_KEY = "auth_token"
        private const val AUTH_USER_ID_KEY = "auth_user_id"
    }

    private val preferences: SharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)

    var authToken: String?
        get() = preferences.getString(AUTH_TOKEN_KEY, "")
        set(authToken) {
            preferences.edit().putString(AUTH_TOKEN_KEY, authToken).apply()
        }
    var authUserId: String?
        get() = preferences.getString(AUTH_USER_ID_KEY, "")
        set(authUserId) {
            preferences.edit().putString(AUTH_USER_ID_KEY, authUserId).apply()
        }
}