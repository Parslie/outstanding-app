package com.vikho305.isaho220.outstanding.data;

import com.google.gson.annotations.SerializedName;

public class AuthInfo {
    @SerializedName(value = "auth_token")
    private String authToken;
    @SerializedName(value = "auth_user_id")
    private String authUserId;

    public String getAuthToken() {
        return authToken;
    }

    public String getAuthUserId() {
        return authUserId;
    }
}
