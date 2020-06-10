package com.vikho305.isaho220.outstanding.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.vikho305.isaho220.outstanding.R;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AuthorizedActivity implements View.OnClickListener, TextWatcher {

    private EditText usernameInput, passwordInput;
    private TextView errorText;

    private Button loginButton, registerButton;
    private ProgressBar loginProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Get layout views
        usernameInput = findViewById(R.id.login_username);
        passwordInput = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_login);
        registerButton = findViewById(R.id.login_register);
        errorText = findViewById(R.id.login_errorText);
        loginProgressBar = findViewById(R.id.login_progressBar);

        // Init activity
        errorText.setVisibility(View.GONE);
        loginProgressBar.setVisibility(View.GONE);

        // Init listeners
        usernameInput.addTextChangedListener(this);
        passwordInput.addTextChangedListener(this);
        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
    }

    private void goToMap() {
        Intent intent = new Intent(LoginActivity.this, MapActivity.class);
        goToActivity(intent);
    }

    private void goToRegistration(String username, String password) {
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.putExtra("username", username);
        intent.putExtra("password", password);
        goToActivity(intent);
    }

    private void logIn(String username, String password) throws JSONException {
        JSONObject parameters = new JSONObject();
        parameters.put("username", username);
        parameters.put("password", password);

        // Disable possibility of logging in twice at once
        loginButton.setEnabled(false);
        loginProgressBar.setVisibility(View.VISIBLE);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                getResources().getString(R.string.login_url),
                parameters,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("==========RESPONSE:" + response + "==========");

                        try {
                            String authToken = response.getString("auth_token");
                            String authUserId = response.getString("auth_user_id");

                            setCredentials(authToken, authUserId);

                            goToMap();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        // Re-enable possibility to log in
                        loginButton.setEnabled(true);
                        loginProgressBar.setVisibility(View.GONE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();

                        // Show specific error
                        if (error.networkResponse == null)
                            errorText.setText(R.string.timeout_error);
                        else if (error.networkResponse.statusCode == 403)
                            errorText.setText(R.string.login_error);
                        else
                            errorText.setText(R.string.server_error);
                        errorText.setVisibility(View.VISIBLE);

                        // Re-enable possibility to log in
                        loginButton.setEnabled(true);
                        loginProgressBar.setVisibility(View.GONE);
                    }
                }
        );

        Volley.newRequestQueue(this).add(request);
    }

    @Override
    public void onClick(View v) {
        if (v == registerButton) {
            String username = usernameInput.getText().toString();
            String password = passwordInput.getText().toString();
            goToRegistration(username, password);
        }
        else if (v == loginButton) {
            try {
                String username = usernameInput.getText().toString();
                String password = passwordInput.getText().toString();
                logIn(username, password);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        String username = usernameInput.getText().toString();
        String password = passwordInput.getText().toString();

        // Disable logging in when a field is empty
        if (username.length() == 0 || password.length() == 0)
            loginButton.setEnabled(false);
        else
            loginButton.setEnabled(true);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }
}
