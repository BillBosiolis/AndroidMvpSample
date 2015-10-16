package com.example.androidmvpsample.data.rest.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by vbosioli on 10/14/15.
 */
public class CommitJson {

    public final String sha;
    public final String url;
    @SerializedName("html_url")
    public final String htmlUrl;
    public final Commit commit;
    public final Author author;

    public CommitJson(String sha, String url, String htmlUrl, Commit commit, Author author) {
        this.sha = sha;
        this.url = url;
        this.htmlUrl = htmlUrl;
        this.commit = commit;
        this.author = author;
    }

    public static class Commit {
        public final Author author;
        public final String message;

        public Commit(Author author, String message) {
            this.author = author;
            this.message = message;
        }
    }

    public static class Author {
        public final String name;
        public final String date;

        public Author(String name, String date) {
            this.name = name;
            this.date = date;
        }
    }

}
