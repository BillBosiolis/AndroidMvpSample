package com.example.androidmvpsample.di.component;

import android.content.Context;

import com.example.androidmvpsample.di.AppComponent;
import com.example.androidmvpsample.di.module.ActivityModule;
import com.example.androidmvpsample.di.scope.PerActivity;

import dagger.Component;

/**
 * Created by Bill on 17/10/2015.
 */
@Component(dependencies = AppComponent.class, modules = ActivityModule.class)
@PerActivity
public interface ActivityComponent {
    Context context();
}
