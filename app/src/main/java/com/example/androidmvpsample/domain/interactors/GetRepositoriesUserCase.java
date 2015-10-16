package com.example.androidmvpsample.domain.interactors;

import com.example.androidmvpsample.domain.Repository;
import com.example.androidmvpsample.domain.entities.Repo;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Bill on 16/10/2015.
 */
public class GetRepositoriesUserCase implements Usecase<List<Repo>> {

    private final Repository repository;

    public GetRepositoriesUserCase(Repository repository) {
        this.repository = repository;
    }

    @Override
    public Observable<List<Repo>> execute() {
        return repository.getRepositories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
