package com.example.androidmvpsample.data.rest;

import com.example.androidmvpsample.data.rest.model.CommitJson;
import com.example.androidmvpsample.data.rest.model.RepositoryJson;
import com.example.androidmvpsample.data.rest.model.RepositoryOwnerJson;
import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Protocol;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.http.Path;
import retrofit.http.Query;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.when;

/**
 * Created by Bill on 27/10/2015.
 */
@RunWith(MockitoJUnitRunner.class)
public class RestRepositoryTest2 {

    GitHubApiImpl apiImpl;

    MockWebServer webServer;
    String webServerHost;
    int webServerPort;

    Gson gson;

    RestRepository restRepository;

    @Before
    public void setup() throws IOException {
        MockitoAnnotations.initMocks(this);

        gson = new Gson();

        setupMockServer();
        setupRestRepository();
    }

    @Test
    public void testGetRepositories() throws IOException {
        List<RepositoryJson> repos = new ArrayList<>();
        repos.add(createRepoJson(1, 1));
        repos.add(createRepoJson(2, 1));

        webServer.enqueue(new MockResponse().addHeader("Content-Type", "application/json; charset=utf-8")
                .addHeader("Cache-Control", "no-cache")
                .setBody("{}"));

        Request.Builder requestBuilder = new Request.Builder()
                .get()
                .url(mockServerUrl());

        Response.Builder responseBuilder = new Response.Builder()
                .code(200)
                .protocol(Protocol.HTTP_1_1)
                .body(ResponseBody.create(MediaType.parse("application/json"), gson.toJson(repos)))
                .request(requestBuilder.build());

        retrofit.Response<List<RepositoryJson>> response = retrofit.Response.success(repos, responseBuilder.build());
        when(apiImpl.getRepositories(anyBoolean()).execute()).thenReturn(response);
        //doReturn(response).when(apiImpl.getRepositories(anyBoolean()).execute());

        RestResponse<List<RepositoryJson>> restResponse = restRepository.getRepositories(true);

        Assert.assertEquals("Repos", 2, restResponse.getData().size());
    }

    @After
    public void tearDown() throws IOException {
    }

    private void setupRestRepository() {
        apiImpl = Mockito.spy(new GitHubApiImpl());
        restRepository = new RestRepository(apiImpl);
    }

    private void setupMockServer() throws IOException {
        webServer = new MockWebServer();
        webServer.start();
        webServerHost = webServer.getHostName();
        webServerPort = webServer.getPort();
    }

    private String mockServerUrl() {
        return String.format("http://%s:%d", webServerHost, webServerPort);
    }

    private RepositoryJson createRepoJson(int id, int ownerId) {
        return new RepositoryJson(id,
                "repo" + id,
                "Repository " + id,
                "repo" + id,
                "Repository " + id + " description",
                10,
                createOwnerJson(ownerId));
    }

    private RepositoryOwnerJson createOwnerJson(int id) {
        return new RepositoryOwnerJson(1, "avatar");
    }

    private class GitHubApiImpl implements GitHubApi {

        GitHubApi api;

        public GitHubApiImpl() {
            api = new Retrofit.Builder()
                    .baseUrl(mockServerUrl())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(GitHubApi.class);
        }

        @Override
        public Call<List<RepositoryJson>> getRepositories(@Query("nocache") boolean nocache) {
            return api.getRepositories(nocache);
        }

        @Override
        public Call<List<CommitJson>> getCommits(@Path("repoId") long repoId) {
            return api.getCommits(repoId);
        }
    }
}
