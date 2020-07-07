package com.vikho305.isaho220.outstanding;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class RegisterFragment extends Fragment {

    private EditText usernameView, emailView, passwordView, passwordConfirmationView;

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get layout views
        usernameView = view.findViewById(R.id.registerFrag_username);
        emailView = view.findViewById(R.id.registerFrag_email);
        passwordView = view.findViewById(R.id.registerFrag_password);
        passwordConfirmationView = view.findViewById(R.id.registerFrag_passwordConfirmation);
    }

    public String getUsername() {
        return usernameView.getText().toString();
    }

    public String getEmail() {
        return emailView.getText().toString();
    }

    public String getPassword() {
        return passwordView.getText().toString();
    }

    public String getPasswordConfirmation() {
        return passwordConfirmationView.getText().toString();
    }
}