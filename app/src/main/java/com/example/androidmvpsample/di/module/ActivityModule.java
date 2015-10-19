package com.example.androidmvpsample.di.module;

import android.content.Context;

import com.example.androidmvpsample.di.scope.PerActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Bill on 17/10/2015.
 */
@Module
public class ActivityModule {

    private final Context mContext;

    public ActivityModule(Context context) {
        this.mContext = context;
    }

    @Provides
    @PerActivity
    Context provideActivityContext() {
        return mContext;
    }
}
