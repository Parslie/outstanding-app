package com.vikho305.isaho220.outstanding.ui.view;

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
import com.vikho305.isaho220.outstanding.ui.viewmodel.RegisterViewModel;
import com.vikho305.isaho220.outstanding.util.Resource;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText usernameInput, emailInput;
    private EditText passwordInput, passwordConfirmationInput;
    private Button backButton, registerButton;
    private ProgressBar progressBar;

    private RegisterViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getWindow().setWindowAnimations(R.style.LoginAnimation);

        initViewModel();

        usernameInput = findViewById(R.id.registerUsername);
        emailInput = findViewById(R.id.registerEmail);
        passwordInput = findViewById(R.id.registerPassword);
        passwordConfirmationInput = findViewById(R.id.registerPasswordConfirmation);
        backButton = findViewById(R.id.registerBackBtn);
        registerButton = findViewById(R.id.registerRegisterBtn);
        progressBar = findViewById(R.id.registerProgressBar);

        backButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
    }

    private void initViewModel() {
        ContextualViewModelFactory contextualViewModelFactory = new ContextualViewModelFactory(this);
        viewModel = new ViewModelProvider(this, contextualViewModelFactory).get(RegisterViewModel.class);
        viewModel.getRegisterStatus().observe(this, new Observer<Resource<Boolean>>() {
            @Override
            public void onChanged(Resource<Boolean> booleanResource) {
                switch (booleanResource.getStatus()) {
                    case SUCCESS:
                        Intent data = new Intent();
                        data.putExtra("username", usernameInput.getText().toString());
                        data.putExtra("password", passwordInput.getText().toString());
                        setResult(RESULT_OK, data);
                        finish();
                        break;
                    case LOADING:
                        progressBar.setVisibility(View.VISIBLE);
                        registerButton.setEnabled(false);
                        break;
                    case ERROR:
                        progressBar.setVisibility(View.INVISIBLE);
                        registerButton.setEnabled(true);
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == backButton) {
            setResult(RESULT_CANCELED);
            finish();
        }
        else if (v == registerButton) {
            String username = usernameInput.getText().toString();
            String email = emailInput.getText().toString();
            String password = passwordInput.getText().toString();
            String passwordConfirmation = passwordConfirmationInput.getText().toString();

            if (passwordConfirmation.equals(password)) // TODO: add more responsive error-handling
                viewModel.register(username, email, password);
        }
    }
}