package com.vikho305.isaho220.outstanding.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.vikho305.isaho220.outstanding.R;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AuthorizedActivity {

    private EditText usernameInput;
    private EditText passwordInput, passwordConfirmationInput;
    private EditText emailInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);
        passwordConfirmationInput = findViewById(R.id.passwordConfirmationInput);
        emailInput = findViewById(R.id.emailInput);

        setButtonListeners();
    }

    private void setButtonListeners() {
        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameInput.getText().toString();
                String email = emailInput.getText().toString();
                String password = passwordInput.getText().toString();
                String passwordConfirmation = passwordConfirmationInput.getText().toString();

                boolean usernameSufficient = username.length() >= getResources().getInteger(R.integer.min_username_length);
                boolean emailSufficient = email.length() >= getResources().getInteger(R.integer.min_email_length);
                boolean passwordSufficient = password.length() >= getResources().getInteger(R.integer.min_password_length);
                boolean passwordsMatching = password.equals(passwordConfirmation);

                if (usernameSufficient && emailSufficient && passwordSufficient && passwordsMatching) {
                    register();
                }
                else {
                    if (!usernameSufficient) {
                        usernameInput.setError(getResources().getString(R.string.username_length_error));
                    }
                    if (!emailSufficient) {
                        emailInput.setError(getResources().getString(R.string.email_length_error));
                    }
                    if (!passwordSufficient) {
                        passwordInput.setError(getResources().getString(R.string.password_length_error));
                    }
                    else if (!passwordsMatching) {
                        passwordConfirmationInput.setError(getResources().getString(R.string.password_match_error));
                    }
                }
            }
        });
    }

    private void register() {
        String username = usernameInput.getText().toString();
        String password = passwordInput.getText().toString();
        String email = emailInput.getText().toString();

        JSONObject parameters = new JSONObject();
        try {
            parameters.put("username", username);
            parameters.put("email", email);
            parameters.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                getResources().getString(R.string.register_url),
                parameters,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("==========RESPONSE:" + response + "==========");

                        try {
                            String authToken = response.getString("auth_token");
                            String authUserId = response.getString("auth_user_id");
                            setCredentials(authToken, authUserId);
                            goToActivity(new Intent(RegisterActivity.this, MapActivity.class));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Toast.makeText(RegisterActivity.this, getResources().getString(R.string.generic_success), Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(RegisterActivity.this, getResources().getString(R.string.generic_error), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        Volley.newRequestQueue(this).add(request);
    }
}
