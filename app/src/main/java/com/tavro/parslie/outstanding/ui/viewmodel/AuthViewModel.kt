package com.tavro.parslie.outstanding.ui.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tavro.parslie.outstanding.data.model.AuthorizationData
import com.tavro.parslie.outstanding.data.repository.UserRepository
import com.tavro.parslie.outstanding.util.Resource
import com.tavro.parslie.outstanding.util.Status
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

class AuthViewModel(context: Context): BaseViewModel() {
    private val userRepository = UserRepository(context)

    private val pingData: MutableLiveData<Resource<String>> = MutableLiveData()
    fun getPingData(): LiveData<Resource<String>> = pingData

    private val registerData: MutableLiveData<Resource<String>> = MutableLiveData()
    fun getRegisterData(): LiveData<Resource<String>> = registerData

    private val loginData: MutableLiveData<Resource<AuthorizationData>> = MutableLiveData()
    fun getLoginData(): LiveData<Resource<AuthorizationData>> = loginData

    private val logoutData: MutableLiveData<Resource<String>> = MutableLiveData()
    fun getLogoutData(): LiveData<Resource<String>> = logoutData

    fun ping() {
        pingData.value = Resource(Status.LOADING, null)

        val onSuccess: Consumer<String> = Consumer {
            pingData.value = Resource(Status.SUCCESS, it)
        }
        val onError: Consumer<Throwable> = Consumer {
            pingData.value = Resource(Status.ERROR, null)
        }

        addDisposable(userRepository.ping()
            .subscribeOn(Schedulers.io())               // TODO: look what does
            .observeOn(AndroidSchedulers.mainThread())  // TODO: look what does
            .subscribe(onSuccess, onError))
    }

    fun register(email: String, username: String, password: String) {
        registerData.value = Resource(Status.LOADING, null)

        val onSuccess: Consumer<String> = Consumer {
            registerData.value = Resource(Status.SUCCESS, it)
        }
        val onError: Consumer<Throwable> = Consumer {
            registerData.value = Resource(Status.ERROR, null)
        }

        addDisposable(userRepository.register(email, username, password)
            .subscribeOn(Schedulers.io())               // TODO: look what does
            .observeOn(AndroidSchedulers.mainThread())  // TODO: look what does
            .subscribe(onSuccess, onError))
    }

    fun login(email: String, password: String) {
        loginData.value = Resource(Status.LOADING, null)

        val onSuccess: Consumer<AuthorizationData> = Consumer {
            loginData.value = Resource(Status.SUCCESS, it)
        }
        val onError: Consumer<Throwable> = Consumer {
            loginData.value = Resource(Status.ERROR, null)
        }

        addDisposable(userRepository.login(email, password)
            .subscribeOn(Schedulers.io())               // TODO: look what does
            .observeOn(AndroidSchedulers.mainThread())  // TODO: look what does
            .subscribe(onSuccess, onError))
    }

    fun logout() {
        logoutData.value = Resource(Status.LOADING, null)

        val onSuccess: Consumer<String> = Consumer {
            logoutData.value = Resource(Status.SUCCESS, it)
        }
        val onError: Consumer<Throwable> = Consumer {
            logoutData.value = Resource(Status.ERROR, null)
        }

        addDisposable(userRepository.logout()
            .subscribeOn(Schedulers.io())               // TODO: look what does
            .observeOn(AndroidSchedulers.mainThread())  // TODO: look what does
            .subscribe(onSuccess, onError))
    }
}