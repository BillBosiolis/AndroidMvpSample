package com.example.androidmvpsample.data.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import static com.example.androidmvpsample.data.provider.AppContract.*;

/**
 * Created by vbosioli on 10/14/15.
 */
public class AppDatabase extends SQLiteOpenHelper {

    private static final String DB_NAME = "app.db";
    private static final int DB_VERSION = 1;

    interface Tables {
        String OWNERS = "owners";
        String REPOS = "repos";
        String COMMITS = "commits";

        String REPOS_JOIN = REPOS + " inner join " + OWNERS
                + " on " + REPOS + "." + OwnerColumns.OWNER_ID + "=" + OWNERS + "." + OwnerColumns.OWNER_ID;
    }

    private interface References {
        String OWNER_ID = "REFERENCES " + Tables.OWNERS + "(" + OwnerColumns.OWNER_ID + ")";
    }

    public AppDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + Tables.REPOS + " ("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + RepositoryColumns.REPO_ID + " INTEGER NOT NULL,"
                + RepositoryColumns.REPO_NAME + " TEXT NOT NULL,"
                + RepositoryColumns.REPO_FULLNAME + " TEXT NOT NULL,"
                + RepositoryColumns.REPO_HTML_URL + " TEXT NOT NULL,"
                + RepositoryColumns.REPO_DESCRIPTION + " TEXT NOT NULL,"
                + RepositoryColumns.REPO_WATCHERS + " INTEGER DEFAULT 0,"
                + OwnerColumns.OWNER_ID + " INTEGER " + References.OWNER_ID + ","
                + SyncColumns.UPDATED + " NUMBER DEFAULT 0,"
                + "UNIQUE (" + RepositoryColumns.REPO_ID + ") ON CONFLICT REPLACE)");

        db.execSQL("CREATE TABLE " + Tables.OWNERS + " ("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + OwnerColumns.OWNER_ID + " INTEGER NOT NULL,"
                + OwnerColumns.OWNER_AVATAR + " TEXT NOT NULL,"
                + SyncColumns.UPDATED + " NUMBER DEFAULT 0,"
                + "UNIQUE (" + OwnerColumns.OWNER_ID + ") ON CONFLICT REPLACE)");

        db.execSQL("CREATE TABLE " + Tables.COMMITS + " ("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + CommitColumns.COMMIT_SHA + " TEXT NOT NULL,"
                + CommitColumns.COMMIT_URL + " TEXT NOT NULL,"
                + CommitColumns.COMMIT_HTML_URL + " TEXT NOT NULL,"
                + CommitColumns.COMMIT_AUTHOR + " TEXT NOT NULL,"
                + CommitColumns.COMMIT_MESSAGE + " TEXT NOT NULL,"
                + CommitColumns.COMMIT_AUTHOR_NAME + " TEXT NOT NULL,"
                + CommitColumns.COMMIT_AUTHOR_DATE + " TEXT NOT NULL,"
                + SyncColumns.UPDATED + " NUMBER DEFAULT 0,"
                + "UNIQUE (" + CommitColumns.COMMIT_SHA + ") ON CONFLICT REPLACE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Tables.REPOS);
        db.execSQL("DROP TABLE IF EXISTS " + Tables.OWNERS);
        db.execSQL("DROP TABLE IF EXISTS " + Tables.COMMITS);
    }

    public static void deleteDatabase(Context context) {
        context.deleteDatabase(DB_NAME);
    }
}
