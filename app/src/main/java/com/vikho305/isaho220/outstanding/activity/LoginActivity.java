package com.vikho305.isaho220.outstanding.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.vikho305.isaho220.outstanding.R;
import com.vikho305.isaho220.outstanding.database.User;
import com.vikho305.isaho220.outstanding.viewmodel.UserViewModel;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AuthorizedActivity {

    private EditText usernameInput, passwordInput;
    private Button loginButton;
    private ProgressBar loginProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameInput = findViewById(R.id.login_username);
        passwordInput = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_login);
        loginProgressBar = findViewById(R.id.login_progressBar);

        loginProgressBar.setVisibility(View.GONE);
        setInputListeners();
        setButtonListeners();
    }

    private void setInputListeners() {
        TextWatcher inputWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

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
        };

        usernameInput.addTextChangedListener(inputWatcher);
        passwordInput.addTextChangedListener(inputWatcher);
    }

    private void setButtonListeners() {
        Button loginButton = findViewById(R.id.login_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logIn();
            }
        });

        Button registerButton = findViewById(R.id.login_register);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameInput.getText().toString();
                String password = passwordInput.getText().toString();
                goToRegistration(username, password);
            }
        });
    }

    private void goToMap() {
        Intent intent = new Intent(LoginActivity.this, MapActivity.class);
        goToActivity(intent);
    }

    private void logIn() {
        String username = usernameInput.getText().toString();
        String password = passwordInput.getText().toString();

        JSONObject parameters = new JSONObject();
        try {
            parameters.put("username", username);
            parameters.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

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

                        loginButton.setEnabled(true);
                        loginProgressBar.setVisibility(View.GONE);
                    }
                }
        );

        Volley.newRequestQueue(this).add(request);
    }

    private void goToRegistration(String username, String password) {
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.putExtra("username", username);
        intent.putExtra("password", password);
        goToActivity(intent);
    }
}
