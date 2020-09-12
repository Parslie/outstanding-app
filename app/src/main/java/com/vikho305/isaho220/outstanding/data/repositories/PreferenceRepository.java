package com.vikho305.isaho220.outstanding.data.repositories;

import android.content.Context;
import android.content.SharedPreferences;

import com.vikho305.isaho220.outstanding.R;

public class PreferenceRepository {

    private Context context;
    private SharedPreferences preferences;

    public PreferenceRepository(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(context.getString(R.string.preference_file), Context.MODE_PRIVATE);
    }

    public String getAuthToken() {
        return preferences.getString(context.getString(R.string.auth_token_key), "");
    }

    public void setAuthToken(String authToken) {
        preferences.edit().putString(context.getString(R.string.auth_token_key), authToken).apply();
    }
}
