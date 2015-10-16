package com.example.androidmvpsample.data.provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by vbosioli on 10/14/15.
 */
public class AppContract {

    public static final String CONTENT_TYPE_APP_BASE = "androidmvpsample.";

    public static final String CONTENT_TYPE_BASE = "vnd.android.cursor.dir/vnd."
            + CONTENT_TYPE_APP_BASE;

    public static final String CONTENT_ITEM_TYPE_BASE = "vnd.android.cursor.item/vnd."
            + CONTENT_TYPE_APP_BASE;

    public interface SyncColumns {
        /** Last time this entry was updated or synchronized. */
        String UPDATED = "updated";
    }

    interface RepositoryColumns {
        String REPO_ID = "repo_id";
        String REPO_NAME = "repo_name";
        String REPO_FULLNAME = "repo_fullname";
        String REPO_HTML_URL = "repo_htmlurl";
        String REPO_DESCRIPTION = "repo_desc";
        String REPO_WATCHERS = "repo_watchers";
    }

    interface OwnerColumns {
        String OWNER_ID = "owner_id";
        String OWNER_AVATAR = "owner_avatar";
    }

    interface CommitColumns {
        String COMMIT_SHA = "commit_sha";
        String COMMIT_URL = "commit_url";
        String COMMIT_HTML_URL = "commit_html_url";
        String COMMIT_AUTHOR = "commit_author";
        String COMMIT_MESSAGE = "commit_message";
        String COMMIT_AUTHOR_NAME = "commit_author_name";
        String COMMIT_AUTHOR_DATE = "commit_author_date";
    }

    public static final String CONTENT_AUTHORITY = "com.example.androidmvpsample";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    private static final String PATH_REPOS = "repos";

    private static final String PATH_OWNERS = "owners";

    private static final String PATH_COMMITS = "commits";

    public static String makeContentType(String id) {
        if (id != null) {
            return CONTENT_TYPE_BASE + id;
        } else {
            return null;
        }
    }

    public static String makeContentItemType(String id) {
        if(id != null) {
            return CONTENT_ITEM_TYPE_BASE + id;
        } else {
            return null;
        }
    }

    public static class Repos implements RepositoryColumns, OwnerColumns, BaseColumns, SyncColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_REPOS).build();

        public static final String CONTENT_TYPE_ID = "repo";

        /** Build {@link Uri} for requested {@link #REPO_ID}. */
        public static Uri buildRepoUri(String repoId) {
            return CONTENT_URI.buildUpon().appendPath(repoId).build();
        }

        /** Read {@link #REPO_ID} from {@link Repos} {@link Uri}. */
        public static String getRepoId(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

    public static class Owners implements OwnerColumns, BaseColumns, SyncColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_OWNERS).build();

        public static final String CONTENT_TYPE_ID = "owner";

        /** Build {@link Uri} for requested {@link #OWNER_ID}. */
        public static Uri buildOwnerUri(String ownerId) {
            return CONTENT_URI.buildUpon().appendPath(ownerId).build();
        }

        /** Read {@link #OWNER_ID} from {@link Owners} {@link Uri}. */
        public static String getOwnerId(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

    public static class Commits implements CommitColumns, BaseColumns, SyncColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_COMMITS).build();

        public static final String CONTENT_TYPE_ID = "commit";

        /** Build {@link Uri} for requested {@link #COMMIT_SHA}. */
        public static Uri buildCommitUri(String sha) {
            return CONTENT_URI.buildUpon().appendPath(sha).build();
        }

        /** Read {@link #COMMIT_SHA} from {@link Commits} {@link Uri}. */
        public static String getSha(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }
}
