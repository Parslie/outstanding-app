package com.vikho305.isaho220.outstanding.data.api

import android.content.Context
import com.rx2androidnetworking.Rx2AndroidNetworking
import com.vikho305.isaho220.outstanding.data.repositories.PreferenceRepository
import io.reactivex.Single

class TestApi(context: Context) : Api(context, "test") {
    fun testAuthToken(): Single<String> {
        return get("", true).stringSingle
    }
}