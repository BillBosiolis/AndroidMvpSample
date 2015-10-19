package com.example.androidmvpsample.domain.interactors;

import com.example.androidmvpsample.domain.Repository;
import com.example.androidmvpsample.domain.entities.Commit;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Bill on 19/10/2015.
 */
public class GetCommitsUseCase implements Usecase<List<Commit>> {

    private final Repository repository;

    public GetCommitsUseCase(Repository repository) {
        this.repository = repository;
    }

    @Override
    public Observable<List<Commit>> execute(boolean forceResync, Object... args) {
        long repoId = (long) args[0];
        String repoName = (String) args[1];
        return repository.getCommits(forceResync, repoId, repoName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
