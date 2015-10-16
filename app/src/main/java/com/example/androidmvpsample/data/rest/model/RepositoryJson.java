package com.example.androidmvpsample.data.rest.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by vbosioli on 10/14/15.
 */
public class RepositoryJson {

    public final long id;
    public final String name;
    public final String full_name;
    public final String htmlUrl;
    public final String description;
    public final int watchers;
    public final RepositoryOwnerJson owner;

    public RepositoryJson(long id, String name, String full_name, String htmlUrl,
                          String description, int watchers, RepositoryOwnerJson owner) {
        this.id = id;
        this.name = name;
        this.full_name = full_name;
        this.htmlUrl = htmlUrl;
        this.description = description;
        this.watchers = watchers;
        this.owner = owner;
    }

    public class RepositoryOwnerJson {

        public final long id;
        @SerializedName("avatar_url")
        public final String avatarUrl;

        public RepositoryOwnerJson(long id, String avatarUrl) {
            this.id = id;
            this.avatarUrl = avatarUrl;
        }
    }
}
