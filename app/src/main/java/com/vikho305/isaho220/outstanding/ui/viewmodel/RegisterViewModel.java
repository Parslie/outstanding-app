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

public class RegisterViewModel extends BaseViewModel {
    private MutableLiveData<Resource<Boolean>> registerStatus = new MutableLiveData<>();

    private UserRepository userRepository;

    public LiveData<Resource<Boolean>> getRegisterStatus() {
        return registerStatus;
    }

    public RegisterViewModel(Context context) {
        userRepository = new UserRepository(context);
    }

    public void register(String username, String email, String password) {
        registerStatus.postValue(Resource.loading(false));
        addDisposable(
                userRepository.register(username, email, password)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                new Consumer<String>() {
                                    @Override
                                    public void accept(String s) throws Exception {
                                        registerStatus.postValue(Resource.success(true));
                                    }
                                },
                                new Consumer<Throwable>() {
                                    @Override
                                    public void accept(Throwable throwable) throws Exception {
                                        registerStatus.postValue(Resource.error(false));
                                    }
                                }
                        )
        );
    }
}
