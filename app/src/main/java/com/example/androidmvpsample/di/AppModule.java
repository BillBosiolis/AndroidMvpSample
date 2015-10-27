package com.example.androidmvpsample.di;

import android.content.Context;

import com.example.androidmvpsample.Config;
import com.example.androidmvpsample.ReposApplication;
import com.example.androidmvpsample.data.DataRepository;
import com.example.androidmvpsample.data.rest.GitHubApi;
import com.example.androidmvpsample.data.rest.retrofit.interceptors.ForceSyncIntercepter;
import com.example.androidmvpsample.data.rest.retrofit.interceptors.LoggingInterceptor;
import com.example.androidmvpsample.domain.Repository;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;

import java.io.File;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

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
    ReposApplication provideApplication() {
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
    GitHubApi provideGitHubApi(OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(GitHubApi.END_POINT)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
                .create(GitHubApi.class);
    }

    @Singleton
    @Provides
    OkHttpClient provideOkHttpClient(Cache cache) {
        OkHttpClient client = new OkHttpClient();
        client.setCache(cache);
        client.interceptors().add(new LoggingInterceptor());
        client.interceptors().add(new ForceSyncIntercepter());
        return client;
    }

    @Singleton
    @Provides
    Cache provideCache(File cacheDir) {
        return new Cache(cacheDir, Config.CACHE_SIZE);
    }

    @Singleton
    @Provides
    File provideCacheDir(Context context) {
        return new File(context.getCacheDir(), "retrofit-cache");
    }
}
