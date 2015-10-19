package com.example.androidmvpsample.di;

import com.example.androidmvpsample.ReposApplication;
import com.example.androidmvpsample.domain.Repository;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Bill on 17/10/2015.
 */
@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
    ReposApplication app();
    Repository repository();
}
