package com.vikho305.isaho220.outstanding.old;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public abstract class AuthorizedActivity extends AppCompatActivity {

    private String authToken, authUserId;

    public String getAuthToken() {
        return authToken;
    }

    public String getAuthUserId() {
        return authUserId;
    }

    public void setAuthorization(String authToken, String authUserId) {
        this.authToken = authToken;
        this.authUserId = authUserId;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        authToken = intent.getStringExtra("authToken");
        authUserId = intent.getStringExtra("authUserId");
    }

    protected void goToActivity(Intent intent) {
        intent.putExtra("authToken", authToken);
        intent.putExtra("authUserId", authUserId);
        startActivity(intent);
    }

    protected void goToActivityForResult(Intent intent, int requestCode) {
        intent.putExtra("authToken", authToken);
        intent.putExtra("authUserId", authUserId);
        startActivityForResult(intent, requestCode);
    }
}
