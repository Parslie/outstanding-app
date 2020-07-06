package com.vikho305.isaho220.outstanding;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vikho305.isaho220.outstanding.database.User;

public class UserFragment extends Fragment {

    private static final String USER_ID_ARG = "userId";

    private UserViewModel userViewModel;

    public static UserFragment newInstance(String userId) {
        UserFragment userFragment = new UserFragment();
        Bundle arguments = new Bundle();
        arguments.putString(USER_ID_ARG, userId);
        userFragment.setArguments(arguments);
        return userFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.user_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get layout views

        // Init view model
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {

            }
        });
    }

}