package com.vikho305.isaho220.outstanding.old;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.vikho305.isaho220.outstanding.R;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AuthorizedActivity implements View.OnClickListener {

    private EditText usernameView, emailView, passwordView, passwordConfirmationView;
    private Button backButton, registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Get layout views
        usernameView = findViewById(R.id.register_username);
        emailView = findViewById(R.id.register_email);
        passwordView = findViewById(R.id.register_password);
        passwordConfirmationView = findViewById(R.id.register_passwordConfirmation);
        backButton = findViewById(R.id.register_backBtn);
        registerButton = findViewById(R.id.register_registerBtn);

        // Init listeners
        backButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
    }

    private void register(final String username, String email, final String password) {
        JSONObject parameters = new JSONObject();
        try {
            parameters.put("username", username);
            parameters.put("email", email);
            parameters.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // TODO: disable views

        JsonParameterRequest request = new JsonParameterRequest(
                Request.Method.POST,
                getResources().getString(R.string.url_register),
                parameters,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Intent data = new Intent();
                        data.putExtra("username", username);
                        data.putExtra("password", password);
                        setResult(RESULT_OK, data);
                        finish();
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
        if (v == backButton) {
            setResult(RESULT_CANCELED);
            finish();
        }
        else if (v == registerButton) {
            String username = usernameView.getText().toString();
            String email = emailView.getText().toString();
            String password = passwordView.getText().toString();
            String passwordConfirmation = passwordConfirmationView.getText().toString();

            if (username.length() == 0 || email.length() == 0 || password.length() == 0 || passwordConfirmation.length() == 0) {
                // TODO: add error message for empty fields
            }
            else if (password.equals(passwordConfirmation)) {
                register(username, email, password);
            }
            else {
                // TODO: add error message for wrong password confirmation
            }
        }
    }
}