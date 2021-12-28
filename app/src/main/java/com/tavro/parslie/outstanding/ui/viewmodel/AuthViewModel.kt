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
        handleApiCall(userRepository.ping(), pingData)
    }

    fun register(email: String, username: String, password: String) {
        handleApiCall(userRepository.register(email, username, password), registerData)
    }

    fun login(email: String, password: String) {
        handleApiCall(userRepository.login(email, password), loginData)
    }

    fun logout() {
        handleApiCall(userRepository.logout(), logoutData)
    }
}