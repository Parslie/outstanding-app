package com.vikho305.isaho220.outstanding.data.api

import android.content.Context
import com.rx2androidnetworking.Rx2AndroidNetworking
import com.vikho305.isaho220.outstanding.data.repositories.PreferenceRepository
import io.reactivex.Single

class TestApi(context: Context) {
    companion object {
        private const val URL = "https://outstanding-server.herokuapp.com/test"
    }

    private val preferences: PreferenceRepository = PreferenceRepository(context)

    fun testAuthToken(): Single<String> {
        return Rx2AndroidNetworking.get(URL)
                .addHeaders("Authorization", "Bearer " + preferences.authToken)
                .build()
                .stringSingle
    }
}