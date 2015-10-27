package com.example.androidmvpsample.data.rest;

import com.example.androidmvpsample.data.rest.model.RepositoryJson;
import com.example.androidmvpsample.data.rest.model.RepositoryOwnerJson;
import com.google.gson.Gson;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by Bill on 25/10/2015.
 */
@RunWith(MockitoJUnitRunner.class)
public class RestRepositoryTest {

    GitHubApi api;

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
                .setBody(gson.toJson(repos)));

        RestResponse<List<RepositoryJson>> restResponse = restRepository.getRepositories(true);

        Assert.assertEquals("Repos", 2, restResponse.getData().size());
    }

    @After
    public void tearDown() throws IOException {
        webServer.shutdown();
    }

    private void setupRestRepository() {
        api = new Retrofit.Builder()
                .baseUrl(mockServerUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(GitHubApi.class);
        restRepository = new RestRepository(api);
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
}
