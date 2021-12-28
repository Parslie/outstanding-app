package com.tavro.parslie.outstanding.ui.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tavro.parslie.outstanding.data.model.Post
import com.tavro.parslie.outstanding.data.model.User
import com.tavro.parslie.outstanding.data.repository.PostRepository
import com.tavro.parslie.outstanding.util.Resource
import com.tavro.parslie.outstanding.util.Status
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

class PostViewModel(context: Context) : BaseViewModel() {
    private val postRepository = PostRepository(context)

    private val creationData: MutableLiveData<Resource<Post>> = MutableLiveData()
    fun getCreationData(): LiveData<Resource<Post>> = creationData

    fun createPost(title: String, content: String, latitude: Double, longitude: Double) {
        creationData.value = Resource(Status.LOADING, null)

        val onSuccess: Consumer<Post> = Consumer {
            creationData.value = Resource(Status.SUCCESS, it)
        }
        val onError: Consumer<Throwable> = Consumer {
            creationData.value = Resource(Status.ERROR, null)
        }

        addDisposable(postRepository.createPost(title, content, latitude, longitude)
            .subscribeOn(Schedulers.io())               // TODO: look what does
            .observeOn(AndroidSchedulers.mainThread())  // TODO: look what does
            .subscribe(onSuccess, onError))
    }
}