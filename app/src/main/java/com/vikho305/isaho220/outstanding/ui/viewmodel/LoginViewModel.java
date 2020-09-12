package com.vikho305.isaho220.outstanding.ui.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.vikho305.isaho220.outstanding.data.repositories.UserRepository;
import com.vikho305.isaho220.outstanding.ui.base.BaseViewModel;
import com.vikho305.isaho220.outstanding.util.Resource;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class LoginViewModel extends BaseViewModel {
    private MutableLiveData<Resource<String>> authToken = new MutableLiveData<>();

    private UserRepository userRepository;

    public LiveData<Resource<String>> getAuthToken() {
        return authToken;
    }

    public LoginViewModel(Context context) {
        userRepository = new UserRepository(context);
    }

    public void login(String username, String password) {
        authToken.postValue(Resource.loading(""));
        addDisposable(
                userRepository.login(username, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Consumer<String>() {
                            @Override
                            public void accept(String s) throws Exception {
                                authToken.postValue(Resource.success(s));
                            }
                        },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                authToken.postValue(Resource.error(""));
                            }
                        }
                )
        );
    }
}
