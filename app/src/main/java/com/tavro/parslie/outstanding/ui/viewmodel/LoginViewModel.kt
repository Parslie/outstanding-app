package com.tavro.parslie.outstanding.ui.viewmodel

import android.content.Context
import com.tavro.parslie.outstanding.data.model.AuthorizationData
import com.tavro.parslie.outstanding.data.repository.PreferenceRepository
import com.tavro.parslie.outstanding.data.repository.UserRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

class LoginViewModel(context: Context): BaseViewModel() {

    private val userRepository = UserRepository(context)
    private val prefs = PreferenceRepository(context)

    fun login(email: String, password: String) {
        val onSuccess: Consumer<AuthorizationData> = Consumer {
            prefs.authToken = it.token
        }
        val onError: Consumer<Throwable> = Consumer {

        }

        addDisposable(userRepository.login(email, password)
            .subscribeOn(Schedulers.io())               // TODO: look what does
            .observeOn(AndroidSchedulers.mainThread())  // TODO: look what does
            .subscribe(onSuccess, onError))
    }

    fun register(email: String, username: String, password: String) {
        val onSuccess: Consumer<String> = Consumer {

        }
        val onError: Consumer<Throwable> = Consumer {

        }

        addDisposable(userRepository.register(email, username, password)
            .subscribeOn(Schedulers.io())               // TODO: look what does
            .observeOn(AndroidSchedulers.mainThread())  // TODO: look what does
            .subscribe(onSuccess, onError))
    }
}