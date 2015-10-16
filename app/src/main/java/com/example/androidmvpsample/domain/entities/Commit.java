package com.example.androidmvpsample.domain.entities;

/**
 * Created by Bill on 16/10/2015.
 */
public class Commit {

    public final String sha;
    public final String url;
    public final String htmlUrl;
    public final String commitMessage;
    public final String authorName;
    public final String authorDate;

    public Commit(String sha, String url, String htmlUrl, String commitMessage,
                  String authorName, String authorDate) {
        this.sha = sha;
        this.url = url;
        this.htmlUrl = htmlUrl;
        this.commitMessage = commitMessage;
        this.authorName = authorName;
        this.authorDate = authorDate;
    }
}
