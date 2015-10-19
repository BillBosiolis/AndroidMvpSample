package com.example.androidmvpsample.data.rest;

/**
 * Created by Bill on 18/10/2015.
 */
public class RestResponse<T> {
    private final boolean isFromCache;

    private final T data;

    public RestResponse(boolean isFromCache, T data) {
        this.isFromCache = isFromCache;
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public boolean isFromCache() {
        return isFromCache;
    }
}
