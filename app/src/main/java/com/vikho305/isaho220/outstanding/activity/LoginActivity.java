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

        usernameInput = findViewById(R.id.login_username);
        passwordInput = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_login);
        registerButton = findViewById(R.id.login_register);
        errorText = findViewById(R.id.login_errorText);
        loginProgressBar = findViewById(R.id.login_progressBar);

        usernameInput.addTextChangedListener(this);
        passwordInput.addTextChangedListener(this);
        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);

        errorText.setVisibility(View.GONE);
        loginProgressBar.setVisibility(View.GONE);
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

    private void logIn() throws JSONException {
        JSONObject parameters = new JSONObject();
        parameters.put("username", usernameInput.getText().toString());
        parameters.put("password", passwordInput.getText().toString());

        errorText.setVisibility(View.GONE);
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

                        loginButton.setEnabled(true);
                        loginProgressBar.setVisibility(View.GONE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();

                        if (error.networkResponse.statusCode == 403)
                            errorText.setText(R.string.login_error);
                        else
                            errorText.setText(R.string.timeout_error);
                        errorText.setVisibility(View.VISIBLE);

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
                logIn();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        String username = usernameInput.getText().toString();
        String password = passwordInput.getText().toString();

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
