package com.example.androidmvpsample.domain.interactors;

import com.example.androidmvpsample.domain.Repository;
import com.example.androidmvpsample.domain.entities.Repo;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Bill on 16/10/2015.
 */
public class GetRepositoriesUseCase implements Usecase<List<Repo>> {

    private final Repository repository;

    @Inject
    public GetRepositoriesUseCase(Repository repository) {
        this.repository = repository;
    }

    @Override
    public Observable<List<Repo>> execute(boolean forceResync, Object... args) {
        return repository.getRepositories(forceResync)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
