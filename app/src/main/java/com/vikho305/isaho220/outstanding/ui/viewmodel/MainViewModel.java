package com.vikho305.isaho220.outstanding.ui.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.vikho305.isaho220.outstanding.data.Post;
import com.vikho305.isaho220.outstanding.data.User;
import com.vikho305.isaho220.outstanding.data.repositories.UserRepository;
import com.vikho305.isaho220.outstanding.ui.base.BaseViewModel;
import com.vikho305.isaho220.outstanding.util.Resource;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainViewModel extends BaseViewModel {

    private MutableLiveData<Resource<Boolean>> hasUpdatedLocation = new MutableLiveData<>();

    private UserRepository userRepository;

    public LiveData<Resource<Boolean>> getHasUpdatedLocation() {
        return hasUpdatedLocation;
    }

    public MainViewModel(Context context) {
        userRepository = new UserRepository(context);
    }

    public void updateLocation(double latitude, double longitude) {
        hasUpdatedLocation.postValue(Resource.loading(false));
        addDisposable(
                userRepository.setCoordinates(latitude, longitude)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                new Consumer<String>() {
                                    @Override
                                    public void accept(String u) throws Exception {
                                        hasUpdatedLocation.postValue(Resource.success(true));
                                    }
                                },
                                new Consumer<Throwable>() {
                                    @Override
                                    public void accept(Throwable throwable) throws Exception {
                                        hasUpdatedLocation.postValue(Resource.error(false));
                                    }
                                }
                        )
        );
    }
}
