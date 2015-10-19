package com.example.androidmvpsample;

import android.app.Application;

import com.example.androidmvpsample.di.AppComponent;
import com.example.androidmvpsample.di.AppModule;
import com.example.androidmvpsample.di.DaggerAppComponent;

/**
 * Created by Bill on 17/10/2015.
 */
public class ReposApplication extends Application {

    private AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        initializeInjector();
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }

    private void initializeInjector() {
        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }
}
