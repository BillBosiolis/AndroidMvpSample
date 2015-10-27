package com.example.androidmvpsample.data.rest;

import com.example.androidmvpsample.data.rest.model.CommitJson;
import com.example.androidmvpsample.data.rest.model.RepositoryJson;

import java.util.List;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Bill on 16/10/2015.
 */
public interface GitHubApi {

    String END_POINT = "https://api.github.com/";
    // https://api.github.com/orgs/google/repos?page=5
    // https://api.github.com/repos/google/iosched/commits

    @GET("orgs/google/repos?page=5")
    Call<List<RepositoryJson>> getRepositories(@Query("nocache") boolean nocache);

    @GET("repos/google/{repoName}/commits")
    Call<List<CommitJson>> getCommits(@Path("repoName") String repoName, @Query("nocache") boolean nocache);
}
