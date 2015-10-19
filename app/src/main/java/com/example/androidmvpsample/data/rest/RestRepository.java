package com.example.androidmvpsample.data.rest;

import com.example.androidmvpsample.data.rest.model.CommitJson;
import com.example.androidmvpsample.data.rest.model.RepositoryJson;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.CacheControl;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

import static com.example.androidmvpsample.utils.LogUtils.LOGD;
import static com.example.androidmvpsample.utils.LogUtils.makeLogTag;

/**
 * Created by Bill on 16/10/2015.
 */
public class RestRepository {

    private static final String TAG = makeLogTag(RestRepository.class);

    private final GitHubApi gitHubApi;

    private static final long CACHE_SIZE = 5 * 1024 * 1024; // 5MB

    @Inject
    public RestRepository(File cacheDir) {
        Cache cache = new Cache(cacheDir, CACHE_SIZE);

        OkHttpClient client = new OkHttpClient();
        client.setCache(cache);
        client.interceptors().add(new LoggingInterceptor());
        client.interceptors().add(new ForceSyncIntercepter());

        Retrofit gitHubApiAdapter = new Retrofit.Builder()
                .baseUrl(GitHubApi.END_POINT)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        gitHubApi = gitHubApiAdapter.create(GitHubApi.class);
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

    private class ForceSyncIntercepter implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();

            if(request.method().equals("GET") &&
                    request.url().getQuery().contains("nocache")) {



                request = request.newBuilder()
                        .cacheControl(new CacheControl.Builder().noCache().build())
                        .url(cloneUrlWithoutNoCache(request))
                        .method(request.method(), request.body())
                        .build();
            }

            return chain.proceed(request);
        }
    }

    private class LoggingInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();

            long t1 = System.nanoTime();

            LOGD(TAG, "Retrofit Request");
            LOGD(TAG, "URL: " + request.url());
            LOGD(TAG, "Connection: " + chain.connection());
            LOGD(TAG, "Headers: " + request.headers());

            Response response = chain.proceed(request);

            long t2 = System.nanoTime();

            LOGD(TAG, "Received response for " + response.request().url() + " in " +
                    (t2 - t1) / 1e6d);
            LOGD(TAG, "Status Code: " + response.code());
            LOGD(TAG, "Headers: " + response.headers());

            LOGD(TAG, "================");
            if(response.networkResponse() == null) {
                LOGD(TAG, "Response is coming from Cache. " + response.cacheResponse().code());
            }

            if(response.isSuccessful() && response.body() != null) {
                //Buffer buffer = new Buffer();
                //response.body().string()
                //String body = buffer.readUtf8();
                //LOGD(TAG, response.body().string());
            }

            return response;
        }
    }

    private HttpUrl cloneUrlWithoutNoCache(Request request) {
        // get query parameter names of the original request
        Set<String> keys = request.httpUrl().queryParameterNames();

        // get the corresponding values of the original request
        List<String> values = new ArrayList<>();
        for(int i = 0; i < keys.size(); i++) {
            values.add(request.httpUrl().queryParameterValue(i));
        }

        // get the path segments of the original request
        List<String> pathSegments = request.httpUrl().encodedPathSegments();

        // use a Builder to clone the original request
        HttpUrl.Builder builder = new HttpUrl.Builder()
                .scheme(request.httpUrl().scheme())
                .host(request.httpUrl().host())
                .port(request.httpUrl().port());

        // add the same path segments
        for(String segment : pathSegments) {
            builder.addPathSegment(segment);
        }

        // copy all the query parameters except the 'nocache' one
        Iterator<String> it = keys.iterator();
        int valueIdx = 0;
        while(it.hasNext()) {
            String key = it.next();
            if(!key.equals("nocache")) {
                builder.addQueryParameter(key, values.get(valueIdx++));
            }
        }

        return builder.build();
    }

    private boolean isCachedResponse(Response response) {
        return response.networkResponse() == null && response.cacheResponse() != null;
    }
}
