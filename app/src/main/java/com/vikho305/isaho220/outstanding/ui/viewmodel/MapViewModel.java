package com.vikho305.isaho220.outstanding.ui.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.vikho305.isaho220.outstanding.data.Post;
import com.vikho305.isaho220.outstanding.data.repositories.PostRepository;
import com.vikho305.isaho220.outstanding.ui.base.BaseViewModel;
import com.vikho305.isaho220.outstanding.util.Resource;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MapViewModel extends BaseViewModel {

    private MutableLiveData<Resource<List<Post>>> posts = new MutableLiveData<>();

    public LiveData<Resource<List<Post>>> getPosts() {
        return posts;
    }

    private PostRepository postRepository;

    public MapViewModel(Context context) {
        postRepository = new PostRepository(context);
    }

    public void fetchPosts(double radius) {
        posts.postValue(Resource.<List<Post>>loading(null));
        addDisposable(
                postRepository.getPostsInRadius(radius)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Consumer<List<Post>>() {
                            @Override
                            public void accept(List<Post> p) throws Exception {
                                posts.postValue(Resource.success(p));
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
