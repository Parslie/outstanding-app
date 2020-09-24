package com.vikho305.isaho220.outstanding.data.api;

import android.content.Context;

import com.rx2androidnetworking.Rx2AndroidNetworking;
import com.vikho305.isaho220.outstanding.data.User;
import com.vikho305.isaho220.outstanding.data.repositories.PreferenceRepository;

import io.reactivex.Single;

public class TestApi {

    private static final String URL = "https://outstanding-server.herokuapp.com/test";
    private PreferenceRepository preferences;

    public TestApi(Context context) {
        preferences = new PreferenceRepository(context);
    }

    public Single<String> testAuthToken() {
        return Rx2AndroidNetworking.get(URL)
                .addHeaders("Authorization", "Bearer " + preferences.getAuthToken())
                .build()
                .getStringSingle();
    }
}
