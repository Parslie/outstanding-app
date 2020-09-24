package com.vikho305.isaho220.outstanding.ui.view;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.vikho305.isaho220.outstanding.R;
import com.vikho305.isaho220.outstanding.data.api.TestApi;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SplashActivity extends AppCompatActivity {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        TestApi testApi = new TestApi(this);
        compositeDisposable.add(
                testApi.testAuthToken()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                new Consumer<String>() {
                                    @Override
                                    public void accept(String s) throws Exception {
                                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                },
                                new Consumer<Throwable>() {
                                    @Override
                                    public void accept(Throwable throwable) throws Exception {
                                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                        )
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }
}