package com.tavro.parslie.outstanding.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tavro.parslie.outstanding.data.model.Post
import com.tavro.parslie.outstanding.util.Resource
import com.tavro.parslie.outstanding.util.Status
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

abstract class BaseViewModel: ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    private fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    protected fun <T> handleApiCall(single: Single<T>, data: MutableLiveData<Resource<T>>) {
        data.value = Resource(Status.LOADING, null)

        val onSuccess: Consumer<T> = Consumer {
            data.value = Resource(Status.SUCCESS, it)
        }
        val onError: Consumer<Throwable> = Consumer {
            data.value = Resource(Status.ERROR, null)
        }

        addDisposable(single
            .subscribeOn(Schedulers.io())               // TODO: look what does
            .observeOn(AndroidSchedulers.mainThread())  // TODO: look what does
            .subscribe(onSuccess, onError))
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}