package com.example.androidmvpsample.di.component;

import com.example.androidmvpsample.di.AppComponent;
import com.example.androidmvpsample.di.module.ActivityModule;
import com.example.androidmvpsample.di.module.RepositoriesModule;
import com.example.androidmvpsample.di.scope.PerActivity;
import com.example.androidmvpsample.domain.interactors.GetRepositoriesUseCase;
import com.example.androidmvpsample.ui.repositories.RepoCommitsFragment;
import com.example.androidmvpsample.ui.repositories.ReposActivity;

import dagger.Component;

/**
 * Created by Bill on 17/10/2015.
 */
@Component(dependencies = AppComponent.class, modules = {RepositoriesModule.class, ActivityModule.class})
@PerActivity
public interface RepositoriesComponent extends ActivityComponent {
    void inject(ReposActivity activity);
    void inject(RepoCommitsFragment fragment);
    GetRepositoriesUseCase getRepositoriesUseCase();
}
