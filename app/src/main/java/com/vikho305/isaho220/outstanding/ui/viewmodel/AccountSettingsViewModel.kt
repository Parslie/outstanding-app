package com.vikho305.isaho220.outstanding.ui.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vikho305.isaho220.outstanding.data.User
import com.vikho305.isaho220.outstanding.data.repositories.UserRepository
import com.vikho305.isaho220.outstanding.ui.base.BaseViewModel
import com.vikho305.isaho220.outstanding.util.Resource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

class AccountSettingsViewModel(context: Context) : BaseViewModel() {
    private val userRepository: UserRepository = UserRepository(context)
    private val hasSetAccount:  MutableLiveData<Resource<Boolean>> = MutableLiveData()
    private val hasSetProfile:  MutableLiveData<Resource<Boolean>> = MutableLiveData()
    private val user: MutableLiveData<User> = MutableLiveData()

    fun hasSetAccount(): LiveData<Resource<Boolean>> {
        return hasSetAccount
    }

    fun hasSetProfile(): LiveData<Resource<Boolean>> {
        return hasSetProfile
    }

    fun getUser(): LiveData<User> {
        return user
    }

    fun setAccount(username: String, email: String, password: String) {
        val onSuccess: Consumer<String> = Consumer {
            hasSetAccount.postValue(Resource.success(true))
        }
        val onError: Consumer<Throwable> = Consumer {
            hasSetAccount.postValue(Resource.error(false))
        }

        hasSetAccount.postValue(Resource.loading(false))

        if (password.isNotEmpty()) {
            addDisposable(userRepository.setAccountDetails(username, email, password)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(onSuccess, onError))
        } else {
            addDisposable(userRepository.setAccountDetails(username, email)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(onSuccess, onError))
        }
    }

    fun setProfile(pfp: String?, description: String) {
        val onSuccess: Consumer<String> = Consumer {
            hasSetProfile.postValue(Resource.success(true))
        }
        val onError: Consumer<Throwable> = Consumer {
            hasSetProfile.postValue(Resource.error(false))
        }

        hasSetProfile.postValue(Resource.loading(false))

        // TODO: figure out what to do with profile colors
        addDisposable(userRepository.setProfileDetails(pfp, description, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onSuccess, onError))
    }

    fun fetchUser() {
        addDisposable(userRepository.getUserSelf()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Consumer {
                    user.postValue(it)
                }))
    }
}