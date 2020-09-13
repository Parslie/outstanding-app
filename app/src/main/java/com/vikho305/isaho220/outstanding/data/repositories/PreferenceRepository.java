package com.vikho305.isaho220.outstanding.data.repositories;

import android.content.Context;
import android.content.SharedPreferences;

import com.vikho305.isaho220.outstanding.R;

public class PreferenceRepository {
    private static final String FILE_NAME = "preferences";
    private static final String AUTH_TOKEN_KEY = "auth_token";
    private static final String AUTH_USER_ID_KEY = "auth_user_id";

    private Context context;
    private SharedPreferences preferences;

    public PreferenceRepository(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }

    public String getAuthToken() {
        return preferences.getString(AUTH_TOKEN_KEY, "");
    }

    public void setAuthToken(String authToken) {
        preferences.edit().putString(AUTH_TOKEN_KEY, authToken).apply();
    }

    public String getAuthUserId() {
        return preferences.getString(AUTH_USER_ID_KEY, "");
    }

    public void setAuthUserId(String authUserId) {
        preferences.edit().putString(AUTH_USER_ID_KEY, authUserId).apply();
    }
}
