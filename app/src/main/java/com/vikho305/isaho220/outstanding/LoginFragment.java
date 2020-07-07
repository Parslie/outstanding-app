package com.vikho305.isaho220.outstanding;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class LoginFragment extends Fragment {

    private EditText usernameView, passwordView;

    public static LoginFragment newInstance() { // TODO: add username and password to parameters
        return new LoginFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get layout views
        usernameView = view.findViewById(R.id.loginFrag_username);
        passwordView = view.findViewById(R.id.loginFrag_password);
    }

    public String getUsername() {
        return usernameView.getText().toString();
    }

    public String getPassword() {
        return passwordView.getText().toString();
    }
}