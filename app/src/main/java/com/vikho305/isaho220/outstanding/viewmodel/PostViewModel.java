package com.vikho305.isaho220.outstanding.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.vikho305.isaho220.outstanding.database.Post;

public class PostViewModel extends ViewModel {

    private MutableLiveData<Post> post;

    public PostViewModel() {
        post = new MutableLiveData<>();
    }

    public void setPost(Post post) {
        this.post.setValue(post);
    }

    public LiveData<Post> getPost() {
        return post;
    }
}
