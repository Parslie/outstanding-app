package com.vikho305.isaho220.outstanding;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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

    private void showLoginFragment() {
        currentFragment = LoginFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_from_right, R.anim.slide_to_left);
        transaction.replace(R.id.main_container, currentFragment).commit();
    }

    private void showRegisterFragment() {
        currentFragment = RegisterFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_from_left, R.anim.slide_to_right);
        transaction.replace(R.id.main_container, currentFragment).commit();
    }

    @Override
    public void onClick(View v) {
        if (v == loginButton && currentFragment instanceof LoginFragment) {
            // TODO: get login details and call login method
        }
        else if (v == loginButton && currentFragment instanceof RegisterFragment) {
            showLoginFragment();
        }
        else if (v == registerButton && currentFragment instanceof LoginFragment) {
            showRegisterFragment();
        }
        else if (v == registerButton && currentFragment instanceof RegisterFragment) {
            // TODO: get register details and call register method
        }
    }
}