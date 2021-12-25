package com.tavro.parslie.outstanding.ui.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tavro.parslie.outstanding.data.model.AuthorizationData
import com.tavro.parslie.outstanding.data.model.User
import com.tavro.parslie.outstanding.data.repository.PreferenceRepository
import com.tavro.parslie.outstanding.data.repository.UserRepository
import com.tavro.parslie.outstanding.util.Resource
import com.tavro.parslie.outstanding.util.Status
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

class ProfileViewModel(context: Context): BaseViewModel() {

    private val userRepository = UserRepository(context)

    private val userResponse: MutableLiveData<Resource<User>> = MutableLiveData()
    fun getUserResponse(): LiveData<Resource<User>> = userResponse

    fun fetchUser(id: Int) {
        userResponse.value = Resource(Status.LOADING, null)

        val onSuccess: Consumer<User> = Consumer {
            userResponse.value = Resource(Status.SUCCESS, it)
        }
        val onError: Consumer<Throwable> = Consumer {
            userResponse.value = Resource(Status.ERROR, null)
        }

        addDisposable(userRepository.getUser(id)
            .subscribeOn(Schedulers.io())               // TODO: look what does
            .observeOn(AndroidSchedulers.mainThread())  // TODO: look what does
            .subscribe(onSuccess, onError))
    }
}