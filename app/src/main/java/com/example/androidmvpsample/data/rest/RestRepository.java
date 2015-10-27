package com.example.androidmvpsample.data.rest;

import com.example.androidmvpsample.data.rest.model.CommitJson;
import com.example.androidmvpsample.data.rest.model.RepositoryJson;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import retrofit.Call;

import static com.example.androidmvpsample.utils.LogUtils.makeLogTag;

/**
 * Created by Bill on 16/10/2015.
 */
public class RestRepository {

    private static final String TAG = makeLogTag(RestRepository.class);

    private final GitHubApi gitHubApi;

    @Inject
    public RestRepository(GitHubApi api) {
        this.gitHubApi = api;
    }

    public RestResponse<List<RepositoryJson>> getRepositories(boolean forceResync) throws IOException {
        Call<List<RepositoryJson>> call = gitHubApi.getRepositories(forceResync);
        retrofit.Response<List<RepositoryJson>> response = call.execute();
        if(isCachedResponse(response.raw())) {
            return new RestResponse<>(true, null);
        } else {
            return new RestResponse<>(false, response.body());
        }
    }

    public RestResponse<List<CommitJson>> getCommits(long repoId) throws IOException {
        Call<List<CommitJson>> call = gitHubApi.getCommits(repoId);
        retrofit.Response<List<CommitJson>> response = call.execute();
        if(isCachedResponse(response.raw())) {
            return new RestResponse<>(true, null);
        } else {
            return new RestResponse<>(false, response.body());
        }
    }

    private boolean isCachedResponse(Response response) {
        return response.networkResponse() == null && response.cacheResponse() != null;
    }
}
