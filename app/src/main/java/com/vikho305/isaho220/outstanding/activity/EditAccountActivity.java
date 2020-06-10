package com.vikho305.isaho220.outstanding.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.vikho305.isaho220.outstanding.R;
import com.vikho305.isaho220.outstanding.ResponseListener;
import com.vikho305.isaho220.outstanding.database.Profile;
import com.vikho305.isaho220.outstanding.database.User;
import com.vikho305.isaho220.outstanding.viewmodel.UserViewModel;

import org.json.JSONException;

public class EditAccountActivity extends AuthorizedActivity
        implements View.OnClickListener, TextWatcher, ResponseListener {

    private static final int IMAGE_REQUEST = 0;
    private static final int MAX_DESCRIPTION_LENGTH = 200;

    private View root;
    private EditText usernameInput, emailInput;
    private EditText passwordInput, passwordConfirmationInput;
    private TextView errorView, usernameLengthView;
    private Button backButton, saveButton;

    private UserViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);

        // Get layout views
        root = findViewById(R.id.editAccount_root);
        usernameInput = findViewById(R.id.editAccount_username);
        emailInput = findViewById(R.id.editAccount_email);
        passwordInput = findViewById(R.id.editAccount_password);
        passwordConfirmationInput = findViewById(R.id.editAccount_passwordConfirmation);
        errorView = findViewById(R.id.editAccount_errorText);
        usernameLengthView = findViewById(R.id.editAccount_usernameLength);
        backButton = findViewById(R.id.editAccount_back);
        saveButton = findViewById(R.id.editAccount_save);

        // Init view model
        viewModel = new ViewModelProvider(this).get(UserViewModel.class);
        viewModel.getUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                usernameLengthView.setText(
                        getResources().getString(R.string.length_limit, user.getUsername().length(), getResources().getInteger(R.integer.username_limit))
                );
            }
        });

        // Init activity
        Intent intent = getIntent();
        User user = intent.getParcelableExtra("user");

        if (user != null) {
            viewModel.setUser(user);
            Profile profile = user.getProfile();

            root.setBackgroundColor(profile.getPrimaryColor());
            usernameInput.setText(user.getUsername());
            emailInput.setText(user.getEmail());

            errorView.setVisibility(View.GONE);
        }
        else {
            finish(); // TODO: add more extensive error-handling for no user
        }

        // Init listeners
        backButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);
        usernameInput.addTextChangedListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK) {
            assert data != null && data.getExtras() != null;
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            viewModel.setPicture(bitmap);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == backButton) {
            setResult(RESULT_CANCELED);
            finish();
        }
        else if (v == saveButton) {
            String password = passwordInput.getText().toString();
            String passwordConfirmation = passwordConfirmationInput.getText().toString();

            if (password.length() == 0) {
                password = null;
                passwordConfirmation = null;
            }

            // Check for errors // TODO: add error for unformatted email
            errorView.setText("");
            errorView.setVisibility(View.VISIBLE);
            if (password != null && !password.equals(passwordConfirmation))
                errorView.setText(R.string.confirmation_password_error);

            // Check if an error has occurred
            if (errorView.length() == 0) {
                try {
                    viewModel.saveAccount(getApplicationContext(), getAuthToken(), this, password);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        viewModel.setAccount(usernameInput.getText().toString(), emailInput.getText().toString());
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}

    @Override
    public void onRequestResponse(String responseType, boolean successful) {
        if (responseType.equals(UserViewModel.ACCOUNT_SAVE_RESPONSE) && successful) {
            Intent data = new Intent();
            data.putExtra("user", viewModel.getUser().getValue());
            setResult(RESULT_OK, data);
            finish();
        }
    }
}
