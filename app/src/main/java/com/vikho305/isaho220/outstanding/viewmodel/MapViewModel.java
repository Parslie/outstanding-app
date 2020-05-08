package com.vikho305.isaho220.outstanding.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.vikho305.isaho220.outstanding.database.Comment;
import com.vikho305.isaho220.outstanding.database.Post;
import com.vikho305.isaho220.outstanding.database.User;

import java.util.ArrayList;
import java.util.HashMap;

public class MapViewModel extends ViewModel {

    private MutableLiveData<ArrayList<User>> onlineUsers;
    private MutableLiveData<ArrayList<Post>> posts;

    public MapViewModel() {
        onlineUsers = new MutableLiveData<>();
        posts = new MutableLiveData<>();
    }

    public LiveData<ArrayList<User>> getOnlineUsers() {
        return onlineUsers;
    }

}
