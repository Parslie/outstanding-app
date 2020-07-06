package com.vikho305.isaho220.outstanding;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public abstract class AuthorizedActivity extends AppCompatActivity {

    private static final int AUTHORIZATION_REQUEST = 100;

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

    //<editor-fold desc="Logging in methods TODO: fix back stack issue">
    private void checkAuthorization() {
        if (authUserId == null || authToken == null) {
            goToLogin();
        }
        else {
            StringRequest request = new StringRequest(
                    Request.Method.POST,
                    getResources().getString(R.string.url_register),
                    null,
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error.networkResponse != null && error.networkResponse.statusCode == 401) {
                                goToLogin();
                            }
                        }
                    }
            ) {
                @Override
                public Map<String, String> getHeaders() {
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Bearer " + authToken);
                    return headers;
                }
            };

            Volley.newRequestQueue(this).add(request);
        }
    }

    private void goToLogin() {
        Intent intent = new Intent(AuthorizedActivity.this, LoginActivity.class);
        startActivityForResult(intent, AUTHORIZATION_REQUEST);
    }
    //</editor-fold>

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkAuthorization(); // TODO: call only once per start of app

        Intent intent = getIntent();
        authToken = intent.getStringExtra("authToken");
        authUserId = intent.getStringExtra("authUserId");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AUTHORIZATION_REQUEST && resultCode == RESULT_OK && data != null) {
            String authToken = data.getStringExtra("authToken");
            String authUserId = data.getStringExtra("authUserId");
            setAuthorization(authToken, authUserId);
        }
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
