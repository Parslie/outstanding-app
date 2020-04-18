package com.vikho305.isaho220.outstanding.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
    private Button registerButton;
    private ProgressBar registerProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);
        passwordConfirmationInput = findViewById(R.id.passwordConfirmationInput);
        emailInput = findViewById(R.id.emailInput);
        registerButton = findViewById(R.id.registerButton);
        registerProgressBar = findViewById(R.id.registerProgressBar);

        registerProgressBar.setVisibility(View.GONE);
        setInputListeners();
        setButtonListeners();
    }

    private void setInputListeners() {
        TextWatcher canRegisterListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                String username = usernameInput.getText().toString();
                String email = emailInput.getText().toString();
                String password = passwordInput.getText().toString();
                String confirmationPassword = passwordConfirmationInput.getText().toString();

                if (!isValidUsername(username) || !isValidEmail(email) || !isValidPassword(password) || !isValidConfirmation(password, confirmationPassword)) {
                    registerButton.setEnabled(false);
                }
                else {
                    registerButton.setEnabled(true);
                }
            }
        };
        usernameInput.addTextChangedListener(canRegisterListener);
        emailInput.addTextChangedListener(canRegisterListener);
        passwordInput.addTextChangedListener(canRegisterListener);
        passwordConfirmationInput.addTextChangedListener(canRegisterListener);

        usernameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                String username = usernameInput.getText().toString();

                if (!isValidUsername(username)) {
                    usernameInput.setError(getResources().getString(R.string.username_length_error));
                }
                else {
                    usernameInput.setError(null);
                }
            }
        });

        emailInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                String email = emailInput.getText().toString();

                if (!isValidEmail(email)) {
                    emailInput.setError(getResources().getString(R.string.email_format_error));
                }
                else {
                    emailInput.setError(null);
                }
            }
        });

        passwordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                String password = passwordInput.getText().toString();
                String confirmationPassword = passwordConfirmationInput.getText().toString();

                if (!isValidPassword(password)) {
                    passwordInput.setError(getResources().getString(R.string.password_length_error));
                }
                else {
                    passwordInput.setError(null);
                }

                if (!isValidConfirmation(password, confirmationPassword)) {
                    passwordConfirmationInput.setError(getResources().getString(R.string.password_match_error));
                }
                else {
                    passwordConfirmationInput.setError(null);
                }
            }
        });

        passwordConfirmationInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                String password = passwordInput.getText().toString();
                String confirmationPassword = passwordConfirmationInput.getText().toString();

                if (!isValidConfirmation(password, confirmationPassword)) {
                    passwordConfirmationInput.setError(getResources().getString(R.string.password_match_error));
                }
                else {
                    passwordConfirmationInput.setError(null);
                }
            }
        });
    }

    private void setButtonListeners() {
        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
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

        registerButton.setEnabled(false);
        registerProgressBar.setVisibility(View.VISIBLE);
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

                        registerButton.setEnabled(true);
                        registerProgressBar.setVisibility(View.GONE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();

                        registerButton.setEnabled(true);
                        registerProgressBar.setVisibility(View.GONE);
                    }
                }
        );

        Volley.newRequestQueue(this).add(request);
    }

    private boolean isValidUsername(String username) {
        return username.length() >= getResources().getInteger(R.integer.min_username_length);
    }

    private boolean isValidEmail(String email) {
        boolean correctFormat = email.contains("@") && email.contains(".") && !email.endsWith("."); // TODO: should be improved
        return email.length() >= getResources().getInteger(R.integer.min_email_length) && correctFormat;
    }

    private boolean isValidPassword(String password) {
        return password.length() >= getResources().getInteger(R.integer.min_password_length);
    }

    private boolean isValidConfirmation(String password, String confirmationPassword) {
        return confirmationPassword.equals(password);
    }
}
