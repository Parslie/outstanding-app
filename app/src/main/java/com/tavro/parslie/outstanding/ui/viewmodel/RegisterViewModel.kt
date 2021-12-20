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

class RegisterViewModel(context: Context): BaseViewModel() {

    private val userRepository = UserRepository(context)

    private val registerResponse: MutableLiveData<Resource<String>> = MutableLiveData()
    fun getRegisterResponse(): LiveData<Resource<String>> = registerResponse

    fun register(email: String, username: String, password: String) {
        registerResponse.value = Resource(Status.LOADING, null)

        val onSuccess: Consumer<String> = Consumer {
            registerResponse.value = Resource(Status.SUCCESS, it)
        }
        val onError: Consumer<Throwable> = Consumer {
            registerResponse.value = Resource(Status.ERROR, null)
        }

        addDisposable(userRepository.register(email, username, password)
            .subscribeOn(Schedulers.io())               // TODO: look what does
            .observeOn(AndroidSchedulers.mainThread())  // TODO: look what does
            .subscribe(onSuccess, onError))
    }
}