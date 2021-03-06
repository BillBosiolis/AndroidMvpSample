package com.example.androidmvpsample.domain;

import com.example.androidmvpsample.domain.entities.Commit;
import com.example.androidmvpsample.domain.entities.Repo;

import java.util.List;

import rx.Observable;

/**
 * Created by Bill on 16/10/2015.
 */
public interface Repository {

    Observable<List<Repo>> getRepositories(boolean forceResync);
    Observable<Repo> getRepository(long repoId);
    Observable<List<Commit>> getCommits(boolean forceResync, long repoId, String repoName);
    Observable<Commit> getCommit(String sha);

}
