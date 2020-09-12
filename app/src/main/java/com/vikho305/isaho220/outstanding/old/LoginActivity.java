package com.vikho305.isaho220.outstanding.old;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.vikho305.isaho220.outstanding.R;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AuthorizedActivity implements View.OnClickListener {

    private static final int REGISTER_REQUEST = 10;

    private EditText usernameView, passwordView;
    private Button loginButton, registerButton;

    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Get layout views
        usernameView = findViewById(R.id.login_username);
        passwordView = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_loginBtn);
        registerButton = findViewById(R.id.login_registerBtn);

        // Init listeners
        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
    }

    private void login(final String username, final String password) {
        JSONObject parameters = new JSONObject();
        try {
            parameters.put("username", username);
            parameters.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // TODO: disable views

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                getResources().getString(R.string.url_login),
                parameters,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String authToken = response.getString("auth_token");
                            String authUserId = response.getString("auth_user_id");
                            setAuthorization(authToken, authUserId);
                            goToActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish(); // Necessary for credentials to be saved for auto-fill
                        } catch (JSONException e) {
                            e.printStackTrace();
                            // TODO: add error message for internal app error
                            // TODO: enable views
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        // TODO: add error messages
                        // TODO: enable views
                    }
                }
        );

        Volley.newRequestQueue(this).add(request);
    }

    @Override
    public void onClick(View v) {
        if (v == loginButton) {
            String username = usernameView.getText().toString();
            String password = passwordView.getText().toString();

            if (username.length() > 0 && password.length() > 0) {
                login(username, password);
            }
            else {
                // TODO: add error message for empty fields
            }
        }
        else if (v == registerButton) {
            Intent intent = new Intent(this, RegisterActivity.class);
            goToActivityForResult(intent, REGISTER_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REGISTER_REQUEST && resultCode == RESULT_OK && data != null) {
            String username = data.getStringExtra("username");
            String password = data.getStringExtra("password");
            login(username, password);
        }
    }
}