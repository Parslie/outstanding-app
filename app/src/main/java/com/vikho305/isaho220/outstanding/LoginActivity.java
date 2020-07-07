package com.vikho305.isaho220.outstanding;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AuthorizedActivity implements View.OnClickListener {

    private Button loginButton, registerButton;

    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Get layout views
        loginButton = findViewById(R.id.login_loginBtn);
        registerButton = findViewById(R.id.login_registerBtn);

        // Init activity
        currentFragment = LoginFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.login_container, currentFragment);
        transaction.commit();

        // Init listeners
        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
    }

    private void showLoginFragment() { // TODO: add username and password to parameters
        currentFragment = LoginFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_from_left, R.anim.slide_to_right);
        transaction.replace(R.id.login_container, currentFragment).commit();
    }

    private void showRegisterFragment() {
        currentFragment = RegisterFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_from_right, R.anim.slide_to_left);
        transaction.replace(R.id.login_container, currentFragment).commit();
    }

    private void login(String username, String password) {
        JSONObject parameters = new JSONObject();
        try {
            parameters.put("username", username);
            parameters.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

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
                        } catch (JSONException e) {
                            e.printStackTrace();
                            // TODO: add error message for internal app error
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: add error messages
                    }
                }
        );

        Volley.newRequestQueue(this).add(request);
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

        JsonParameterRequest request = new JsonParameterRequest(
                Request.Method.POST,
                getResources().getString(R.string.url_register),
                parameters,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        showLoginFragment();
                        login(username, password);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: add error messages
                    }
                }
        );

        Volley.newRequestQueue(this).add(request);
    }

    @Override
    public void onClick(View v) {
        if (v == loginButton && currentFragment instanceof LoginFragment) {
            String username = ((LoginFragment) currentFragment).getUsername();
            String password = ((LoginFragment) currentFragment).getPassword();

            if (username.length() == 0 || password.length() == 0) {
                // TODO: add error message for empty fields
            }
            else {
                login(username, password);
            }
        }
        else if (v == loginButton && currentFragment instanceof RegisterFragment) {
            showLoginFragment();
        }
        else if (v == registerButton && currentFragment instanceof LoginFragment) {
            showRegisterFragment();
        }
        else if (v == registerButton && currentFragment instanceof RegisterFragment) {
            String username = ((RegisterFragment) currentFragment).getUsername();
            String email = ((RegisterFragment) currentFragment).getEmail();
            String password = ((RegisterFragment) currentFragment).getPassword();
            String passwordConfirmation = ((RegisterFragment) currentFragment).getPasswordConfirmation();

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