package com.vikho305.isaho220.outstanding;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

public class AuthorizedFragment extends Fragment {

    public final AuthorizedActivity requireAuthActivity() {
        AuthorizedActivity activity = (AuthorizedActivity) getActivity();
        if (activity == null) {
            throw new IllegalStateException("Fragment " + this + " not attached to an activity.");
        }
        return activity;
    }
}
