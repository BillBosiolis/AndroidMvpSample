package com.example.androidmvpsample.data.rest.retrofit.interceptors;

import com.example.androidmvpsample.utils.LogUtils;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import static com.example.androidmvpsample.utils.LogUtils.LOGD;

/**
 * Created by Bill on 25/10/2015.
 */
public class LoggingInterceptor implements Interceptor {

    private static final String TAG = LogUtils.makeLogTag(LoggingInterceptor.class);

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
        if (response.networkResponse() == null) {
            LOGD(TAG, "Response is coming from Cache. " + response.cacheResponse().code());
        }

        if (response.isSuccessful() && response.body() != null) {
            //Buffer buffer = new Buffer();
            //response.body().string()
            //String body = buffer.readUtf8();
            //LOGD(TAG, response.body().string());
        }

        return response;
    }
}
