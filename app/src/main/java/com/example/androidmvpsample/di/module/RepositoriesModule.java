package com.example.androidmvpsample.di.module;

import com.example.androidmvpsample.di.scope.PerActivity;
import com.example.androidmvpsample.domain.Repository;
import com.example.androidmvpsample.domain.interactors.GetCommitsUseCase;
import com.example.androidmvpsample.domain.interactors.GetRepositoriesUseCase;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Bill on 17/10/2015.
 */
@Module
public class RepositoriesModule {

    @Provides
    @PerActivity
    GetRepositoriesUseCase provideGetRepositoriesUseCase(Repository repository) {
        return new GetRepositoriesUseCase(repository);
    }

    @Provides
    @PerActivity
    GetCommitsUseCase provideGetCommitsUseCase(Repository repository) {
        return new GetCommitsUseCase(repository);
    }

}
