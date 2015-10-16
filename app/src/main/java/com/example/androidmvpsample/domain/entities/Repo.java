package com.example.androidmvpsample.domain.entities;

/**
 * Created by Bill on 16/10/2015.
 */
public class Repo {

    public final long id;
    public final String name;
    public final String full_name;
    public final String htmlUrl;
    public final String description;
    public final int watchers;
    public final String avatarUrl;

    public Repo(long id, String name, String full_name, String htmlUrl,
                String description, int watchers, String avatarUrl) {
        this.id = id;
        this.name = name;
        this.full_name = full_name;
        this.htmlUrl = htmlUrl;
        this.description = description;
        this.watchers = watchers;
        this.avatarUrl = avatarUrl;
    }
}
