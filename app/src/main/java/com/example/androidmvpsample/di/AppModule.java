package com.example.androidmvpsample.di;

import android.content.Context;

import com.example.androidmvpsample.ReposApplication;
import com.example.androidmvpsample.data.DataRepository;
import com.example.androidmvpsample.domain.Repository;

import java.io.File;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Bill on 17/10/2015.
 */
@Module
public class AppModule {

    private final ReposApplication mReposApplication;

    public AppModule(ReposApplication reposApplication) {
        this.mReposApplication = reposApplication;
    }

    @Singleton
    @Provides
    ReposApplication provideApplicationContenxt() {
        return mReposApplication;
    }

    @Singleton
    @Provides
    Context provideAppContext() {
        return mReposApplication.getApplicationContext();
    }

    @Singleton
    @Provides
    Repository provideDataRepository(DataRepository repository) {
        return repository;
    }

    @Singleton
    @Provides
    File provideCacheDir(Context context) {
        return new File(context.getCacheDir(), "retrofit-cache");
    }
}
