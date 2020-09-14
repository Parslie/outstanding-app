package com.vikho305.isaho220.outstanding.ui.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.vikho305.isaho220.outstanding.data.AuthInfo;
import com.vikho305.isaho220.outstanding.data.Post;
import com.vikho305.isaho220.outstanding.data.User;
import com.vikho305.isaho220.outstanding.data.repositories.UserRepository;
import com.vikho305.isaho220.outstanding.ui.base.BaseViewModel;
import com.vikho305.isaho220.outstanding.util.Resource;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ProfileViewModel extends BaseViewModel {

    private MutableLiveData<Resource<User>> user = new MutableLiveData<>();
    private MutableLiveData<Resource<List<Post>>> posts = new MutableLiveData<>();
    private int currentPostPage = 0;

    private UserRepository userRepository;

    public LiveData<Resource<User>> getUser() {
        return user;
    }

    public LiveData<Resource<List<Post>>> getPosts() {
        return posts;
    }

    public ProfileViewModel(Context context) {
        userRepository = new UserRepository(context);
    }

    public void fetchUser(String userId) {
        user.postValue(Resource.<User>loading(null));
        addDisposable(
                userRepository.getUser(userId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                new Consumer<User>() {
                                    @Override
                                    public void accept(User u) throws Exception {
                                        user.postValue(Resource.success(u));
                                    }
                                },
                                new Consumer<Throwable>() {
                                    @Override
                                    public void accept(Throwable throwable) throws Exception {
                                        user.postValue(Resource.<User>error(null));
                                    }
                                }
                        )
        );
    }

    public void fetchPosts(String userId) {
        posts.postValue(Resource.<List<Post>>loading(null));
        addDisposable(
                userRepository.getPosts(userId, currentPostPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Consumer<List<Post>>() {
                            @Override
                            public void accept(List<Post> p) throws Exception {
                                posts.postValue(Resource.success(p));
                                currentPostPage++;
                            }
                        },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                posts.postValue(Resource.<List<Post>>error(null));
                            }
                        }
                )
        );
    }
}
