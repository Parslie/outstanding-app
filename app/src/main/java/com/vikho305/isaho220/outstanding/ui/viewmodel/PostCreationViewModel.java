package com.vikho305.isaho220.outstanding.ui.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.vikho305.isaho220.outstanding.data.Post;
import com.vikho305.isaho220.outstanding.data.User;
import com.vikho305.isaho220.outstanding.data.repositories.PostRepository;
import com.vikho305.isaho220.outstanding.data.repositories.UserRepository;
import com.vikho305.isaho220.outstanding.ui.base.BaseViewModel;
import com.vikho305.isaho220.outstanding.util.Resource;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class PostCreationViewModel extends BaseViewModel {

    private MutableLiveData<Resource<Boolean>> hasPosted = new MutableLiveData<>();
    private MutableLiveData<Resource<User>> userSelf = new MutableLiveData<>();

    public LiveData<Resource<User>> getUserSelf() {
        return userSelf;
    }

    public LiveData<Resource<Boolean>> getHasPosted() {
        return hasPosted;
    }

    private UserRepository userRepository;
    private PostRepository postRepository;

    public PostCreationViewModel(Context context) {
        userRepository = new UserRepository(context);
        postRepository = new PostRepository(context);
    }

    public void fetchUserSelf() {
        userSelf.postValue(Resource.<User>loading(null));
        addDisposable(
                userRepository.getUserSelf()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Consumer<User>() {
                            @Override
                            public void accept(User user) throws Exception {
                                userSelf.postValue(Resource.success(user));
                            }
                        },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                userSelf.postValue(Resource.<User>error(null));
                            }
                        }
                )
        );
    }

    public void postPost(String title, String text, String media, String mediaType, double latitude, double longitude) {
        hasPosted.postValue(Resource.loading(false));
        addDisposable(
                postRepository.postPost(title, text, media, mediaType, latitude, longitude)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Consumer<String>() {
                            @Override
                            public void accept(String p) throws Exception {
                                hasPosted.postValue(Resource.success(true));
                            }
                        },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                hasPosted.postValue(Resource.error(false));
                            }
                        }
                )
        );
    }
}
