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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.vikho305.isaho220.outstanding.R;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AuthorizedActivity implements TextWatcher, View.OnClickListener {

    private EditText usernameInput;
    private EditText emailInput;
    private EditText passwordInput, passwordConfirmationInput;
    private TextView errorText;

    private Button registerButton, backButton;
    private ProgressBar registerProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameInput = findViewById(R.id.register_username);
        emailInput = findViewById(R.id.register_email);
        passwordInput = findViewById(R.id.register_password);
        passwordConfirmationInput = findViewById(R.id.register_passwordConfirmation);
        backButton = findViewById(R.id.register_backButton);
        registerButton = findViewById(R.id.register_registerButton);
        registerProgressBar = findViewById(R.id.register_progressBar);
        errorText = findViewById(R.id.register_errorText);

        usernameInput.addTextChangedListener(this);
        emailInput.addTextChangedListener(this);
        passwordInput.addTextChangedListener(this);
        passwordConfirmationInput.addTextChangedListener(this);
        backButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);

        errorText.setVisibility(View.GONE);
        registerProgressBar.setVisibility(View.GONE);
    }

    private void register() throws JSONException {
        JSONObject parameters = new JSONObject();
        parameters.put("username", usernameInput.getText().toString());
        parameters.put("email", emailInput.getText().toString());
        parameters.put("password", passwordInput.getText().toString());
        parameters.put("password_confirmation", passwordConfirmationInput.getText().toString());

        errorText.setVisibility(View.GONE);
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

                        if (error.networkResponse.statusCode == 409)
                            errorText.setText(R.string.register_error);
                        else if (error.networkResponse.statusCode == 403)
                            errorText.setText(R.string.confirmation_password_error);
                        else if (error.networkResponse.statusCode == 400)
                            errorText.setText(R.string.empty_fields_error);
                        else
                            errorText.setText(R.string.server_error);
                        errorText.setVisibility(View.VISIBLE);

                        registerButton.setEnabled(true);
                        registerProgressBar.setVisibility(View.GONE);
                    }
                }
        );

        Volley.newRequestQueue(this).add(request);
    }

    ////////////
    // Listeners
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
        String passwordConfirmation = passwordConfirmationInput.getText().toString();

        if (username.length() == 0 || email.length() == 0 || password.length() == 0 || passwordConfirmation.length() == 0)
            registerButton.setEnabled(false);
        else
            registerButton.setEnabled(true);
    }

    @Override
    public void onClick(View v) {
        if (v == backButton) {
            finish();
        }
        else if (v == registerButton) {
            // TODO: warn about wrong field formats

            try {
                register();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
