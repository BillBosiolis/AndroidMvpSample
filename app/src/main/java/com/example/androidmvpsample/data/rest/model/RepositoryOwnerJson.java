package com.example.androidmvpsample.data.rest.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Bill on 25/10/2015.
 */
public class RepositoryOwnerJson {

    public final long id;
    @SerializedName("avatar_url")
    public final String avatarUrl;

    public RepositoryOwnerJson(long id, String avatarUrl) {
        this.id = id;
        this.avatarUrl = avatarUrl;
    }
}
