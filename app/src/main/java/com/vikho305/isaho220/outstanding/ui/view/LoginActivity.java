package com.vikho305.isaho220.outstanding.ui.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.vikho305.isaho220.outstanding.R;
import com.vikho305.isaho220.outstanding.ui.viewmodel.ContextualViewModelFactory;
import com.vikho305.isaho220.outstanding.ui.viewmodel.LoginViewModel;
import com.vikho305.isaho220.outstanding.util.Resource;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REGISTER_REQUEST = 0;

    private EditText usernameInput, passwordInput;
    private Button registerButton, loginButton;
    private ProgressBar progressBar;

    private LoginViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViewModel();

        usernameInput = findViewById(R.id.loginUsername);
        passwordInput = findViewById(R.id.loginPassword);
        registerButton = findViewById(R.id.loginRegisterBtn);
        loginButton = findViewById(R.id.loginLoginBtn);
        progressBar = findViewById(R.id.loginProgressBar);

        registerButton.setOnClickListener(this);
        loginButton.setOnClickListener(this);
    }

    private void initViewModel() {
        ContextualViewModelFactory contextualViewModelFactory = new ContextualViewModelFactory(this);
        viewModel = new ViewModelProvider(this, contextualViewModelFactory).get(LoginViewModel.class);
        viewModel.getAuthToken().observe(this, new Observer<Resource<String>>() {
            @Override
            public void onChanged(Resource<String> stringResource) {
                switch (stringResource.getStatus()) {
                    case SUCCESS:
                        // TODO: start map activity and clear back stack
                        break;
                    case LOADING:
                        progressBar.setVisibility(View.VISIBLE);
                        registerButton.setEnabled(false);
                        loginButton.setEnabled(false);
                        break;
                    case ERROR:
                        progressBar.setVisibility(View.INVISIBLE);
                        registerButton.setEnabled(true);
                        loginButton.setEnabled(true);
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == registerButton) {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivityForResult(intent, REGISTER_REQUEST);
        }
        else if (v == loginButton) {
            String username = usernameInput.getText().toString();
            String password = passwordInput.getText().toString();

            viewModel.login(username, password);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REGISTER_REQUEST && resultCode == RESULT_OK && data != null) {
            String username = data.getStringExtra("username");
            String password = data.getStringExtra("password");
            usernameInput.setText(username);
            passwordInput.setText(password);

            viewModel.login(username, password);
        }
    }
}