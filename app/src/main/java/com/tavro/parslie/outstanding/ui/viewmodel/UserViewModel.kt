package com.tavro.parslie.outstanding.ui.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tavro.parslie.outstanding.data.model.User
import com.tavro.parslie.outstanding.data.repository.UserRepository
import com.tavro.parslie.outstanding.util.Resource
import com.tavro.parslie.outstanding.util.Status
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

class UserViewModel(context: Context): BaseViewModel() {
    private val userRepository = UserRepository(context)

    private val userData: MutableLiveData<Resource<User>> = MutableLiveData()
    fun getUserData(): LiveData<Resource<User>> = userData

    fun fetchUser(id: Int) {
        handleApiCall(userRepository.getUser(id), userData)
    }
}