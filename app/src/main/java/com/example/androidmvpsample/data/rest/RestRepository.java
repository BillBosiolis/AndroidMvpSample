package com.example.androidmvpsample.data.rest;

import com.example.androidmvpsample.data.rest.model.CommitJson;
import com.example.androidmvpsample.data.rest.model.RepositoryJson;
import com.squareup.okhttp.OkHttpClient;

import java.io.IOException;
import java.util.List;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by Bill on 16/10/2015.
 */
public class RestRepository {

    private final GitHubApi gitHubApi;

    public RestRepository() {
        OkHttpClient client = new OkHttpClient();

        Retrofit gitHubApiAdapter = new Retrofit.Builder()
                .baseUrl(GitHubApi.END_POINT)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        gitHubApi = gitHubApiAdapter.create(GitHubApi.class);
    }

    public List<RepositoryJson> getRepositories() throws IOException {
        Call<List<RepositoryJson>> call = gitHubApi.getRepositories();
        return call.execute().body();
    }

    public List<CommitJson> getCommits(long repoId) throws IOException {
        Call<List<CommitJson>> call = gitHubApi.getCommits(repoId);
        return call.execute().body();
    }
}
