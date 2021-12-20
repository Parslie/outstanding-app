package com.tavro.parslie.outstanding.data.repository

import android.content.Context
import android.content.SharedPreferences

class PreferenceRepository(context: Context) {

    companion object {
        private const val FILE_NAME = "prefs"
        private const val AUTH_TOKEN_KEY = "auth_token"
    }

    private val sharedPrefs: SharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)

    var authToken: String?
        get() = sharedPrefs.getString(AUTH_TOKEN_KEY, null)
        set(value) = sharedPrefs.edit().putString(AUTH_TOKEN_KEY, value).apply()
}