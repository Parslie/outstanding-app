package com.tavro.parslie.outstanding.data.repository

import android.content.Context
import android.content.SharedPreferences

class PreferenceRepository(context: Context) {

    companion object {
        private const val FILE_NAME = "prefs"
        private const val AUTH_TOKEN_KEY = "auth_token"
        private const val AUTH_ID_KEY = "auth_id"
    }

    private val sharedPrefs: SharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)

    var authToken: String?
        get() = sharedPrefs.getString(AUTH_TOKEN_KEY, null)
        set(value) = sharedPrefs.edit().putString(AUTH_TOKEN_KEY, value).apply()

    var authID: Int
        get() = sharedPrefs.getInt(AUTH_ID_KEY, -1)
        set(value) = sharedPrefs.edit().putInt(AUTH_ID_KEY, value).apply()
}