package com.example.androidmvpsample.data.provider;

/**
 * Created by vbosioli on 10/14/15.
 */
public enum AppUriEnum {

    REPOS(100, "repos", AppContract.Repos.CONTENT_TYPE_ID, false, AppDatabase.Tables.REPOS),
    REPOS_ID(101, "repos/*", AppContract.Repos.CONTENT_TYPE_ID, true, null),
    OWNERS(200, "owners", AppContract.Owners.CONTENT_TYPE_ID, false, AppDatabase.Tables.OWNERS),
    OWNERS_ID(201, "owners/*", AppContract.Owners.CONTENT_TYPE_ID, true, null),
    COMMITS(300, "commits", AppContract.Commits.CONTENT_TYPE_ID, false, AppDatabase.Tables.COMMITS),
    COMMITS_ID(301, "repos/*", AppContract.Commits.CONTENT_TYPE_ID, true, null);

    public int code;

    /**
     * The path to the {@link android.content.UriMatcher} will use to match. * may be used as a
     * wild card for any text, and # may be used as a wild card for numbers.
     */
    public String path;

    public String contentType;

    public String table;

    AppUriEnum(int code, String path, String contentTypeId, boolean item, String table) {
        this.code = code;
        this.path = path;
        this.contentType = item ? AppContract.makeContentItemType(contentTypeId)
                : AppContract.makeContentType(contentTypeId);
        this.table = table;
    }
}
