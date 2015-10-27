package com.example.androidmvpsample.data.rest.retrofit.interceptors;

import com.squareup.okhttp.CacheControl;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by Bill on 25/10/2015.
 */
public class ForceSyncIntercepter implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        if (request.method().equals("GET") &&
                request.url().getQuery().contains("nocache")) {
            request = request.newBuilder()
                    .cacheControl(new CacheControl.Builder().noCache().build())
                    .url(cloneUrlWithoutNoCache(request))
                    .method(request.method(), request.body())
                    .build();
        }

        return chain.proceed(request);
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
}
