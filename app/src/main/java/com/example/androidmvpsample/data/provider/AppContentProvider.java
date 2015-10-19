package com.example.androidmvpsample.data.provider;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.androidmvpsample.utils.LogUtils;
import com.example.androidmvpsample.utils.SelectionBuilder;

import java.util.ArrayList;
import java.util.Arrays;

import static com.example.androidmvpsample.data.provider.AppContract.*;
import static com.example.androidmvpsample.data.provider.AppContract.Commits;
import static com.example.androidmvpsample.data.provider.AppContract.Repos;
import static com.example.androidmvpsample.data.provider.AppDatabase.*;
import static com.example.androidmvpsample.utils.LogUtils.LOGV;

/**
 * Created by vbosioli on 10/14/15.
 */
public class AppContentProvider extends ContentProvider {

    private static final String TAG = LogUtils.makeLogTag(AppContentProvider.class);

    private AppDatabase mOpenHelper;

    private AppProviderUriMatcher mUriMatcher;

    @Override
    public boolean onCreate() {
        mOpenHelper = new AppDatabase(getContext());
        mUriMatcher = new AppProviderUriMatcher();
        return true;
    }

    private void deleteDatabase() {
        // TODO: wait for content provider operations to finish, then tear down
        mOpenHelper.close();
        Context context = getContext();
        AppDatabase.deleteDatabase(context);
        mOpenHelper = new AppDatabase(getContext());
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        AppUriEnum matchingUriEnum = mUriMatcher.matchUri(uri);
        return matchingUriEnum.contentType;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = mOpenHelper.getReadableDatabase();

        AppUriEnum matchingUriEnum = mUriMatcher.matchUri(uri);

        if (Log.isLoggable(TAG, Log.VERBOSE)) {
            Log.v(TAG, "uri=" + uri + " code=" + matchingUriEnum.code + " proj=" +
                    Arrays.toString(projection) + " selection=" + selection + " args="
                    + Arrays.toString(selectionArgs) + ")");
        }

        switch (matchingUriEnum) {
            default: {
                // Most cases are handled with simple SelectionBuilder.
                final SelectionBuilder builder = buildExpandedSelection(uri, matchingUriEnum.code);

                Cursor cursor = builder
                        .where(selection, selectionArgs)
                        .query(db, false, projection, sortOrder, null);

                Context context = getContext();
                if (null != context) {
                    cursor.setNotificationUri(context.getContentResolver(), uri);
                }
                return cursor;
            }
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        LOGV(TAG, "insert(uri=" + uri + ", values=" + values.toString()
                + ", account=" + "" + ")");
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        AppUriEnum matchingUriEnum = mUriMatcher.matchUri(uri);
        if (matchingUriEnum.table != null) {
            db.insertOrThrow(matchingUriEnum.table, null, values);
            notifyChange(uri);
        }

        switch (matchingUriEnum) {
            case REPOS: {
                return Repos.buildRepoUri(values.getAsString(Repos.REPO_ID));
            }
            case OWNERS: {
                return Owners.buildOwnerUri(values.getAsString(Owners.OWNER_ID));
            }
            case COMMITS: {
                return Commits.buildCommitUri(values.getAsString(Commits.COMMIT_SHA));
            }
            default: {
                throw new UnsupportedOperationException("Unknown insert uri: " + uri);
            }
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        String accountName = "";
        LOGV(TAG, "update(uri=" + uri + ", values=" + values.toString()
                + ", account=" + accountName + ")");

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        AppUriEnum matchingUriEnum = mUriMatcher.matchUri(uri);

        final SelectionBuilder builder = buildSimpleSelection(uri);
        int retVal = builder.where(selection, selectionArgs).update(db, values);
        notifyChange(uri);
        return retVal;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        String accountName = "";
        LOGV(TAG, "delete(uri=" + uri + ", account=" + accountName + ")");
        if (uri == BASE_CONTENT_URI) {
            // Handle whole database deletes (e.g. when signing out)
            deleteDatabase();
            notifyChange(uri);
            return 1;
        }
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final SelectionBuilder builder = buildSimpleSelection(uri);
        AppUriEnum matchingUriEnum = mUriMatcher.matchUri(uri);

        int retVal = builder.where(selection, selectionArgs).delete(db);
        notifyChange(uri);
        return retVal;
    }

    /**
     * Notifies the system that the given {@code uri} data has changed.
     */
    private void notifyChange(Uri uri) {

    }

    /**
     * Apply the given set of {@link ContentProviderOperation}, executing inside
     * a {@link SQLiteDatabase} transaction. All changes will be rolled back if
     * any single one fails.
     */
    @Override
    public ContentProviderResult[] applyBatch(ArrayList<ContentProviderOperation> operations)
            throws OperationApplicationException {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            final int numOperations = operations.size();
            final ContentProviderResult[] results = new ContentProviderResult[numOperations];
            for (int i = 0; i < numOperations; i++) {
                results[i] = operations.get(i).apply(this, results, i);
            }
            db.setTransactionSuccessful();
            return results;
        } finally {
            db.endTransaction();
        }
    }

    /**
     * Build a simple {@link SelectionBuilder} to match the requested
     * {@link Uri}. This is usually enough to support {@link #insert},
     * {@link #update}, and {@link #delete} operations.
     */
    private SelectionBuilder buildSimpleSelection(Uri uri) {
        final SelectionBuilder builder = new SelectionBuilder();
        AppUriEnum matchingUriEnum = mUriMatcher.matchUri(uri);
        // The main Uris, corresponding to the root of each type of Uri, do not have any selection
        // criteria so the full table is used. The others apply a selection criteria.
        switch (matchingUriEnum) {
            case REPOS:
                return builder.table(Tables.REPOS);
            case REPOS_ID: {
                final String repoId = Repos.getRepoId(uri);
                return builder.table(Tables.REPOS)
                        .where(Repos.REPO_ID + "=?", repoId);
            }
            case OWNERS: {
                return builder.table(Tables.OWNERS);
            }
            case OWNERS_ID: {
                final String ownerId = Owners.getOwnerId(uri);
                return builder.table(Tables.OWNERS)
                        .where(Owners.OWNER_ID + "=?", ownerId);
            }
            case COMMITS:
                return builder.table(matchingUriEnum.table);

            case COMMITS_ID: {
                final String sha = Commits.getSha(uri);
                return builder.table(Tables.COMMITS)
                        .where(Commits.COMMIT_SHA + "=?", sha);
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri for " + uri);
            }
        }
    }

    /**
     * Build an advanced {@link SelectionBuilder} to match the requested
     * {@link Uri}. This is usually only used by {@link #query}, since it
     * performs table joins useful for {@link Cursor} data.
     */
    private SelectionBuilder buildExpandedSelection(Uri uri, int match) {
        final SelectionBuilder builder = new SelectionBuilder();
        AppUriEnum matchingUriEnum = mUriMatcher.matchCode(match);
        if (matchingUriEnum == null) {
            throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        switch (matchingUriEnum) {
            case REPOS: {
                return builder.table(Tables.REPOS_JOIN)
                        .map(Repos.OWNER_ID, Qualified.REPOS_OWNER_ID);
            }
            case REPOS_ID: {
                final String repoId = Repos.REPO_ID;
                return builder.table(Tables.REPOS_JOIN)
                        .map(Repos.OWNER_ID, Qualified.REPOS_OWNER_ID)
                        .where(Repos.REPO_ID + "=?", repoId);
            }
            case OWNERS: {
                return builder.table(Tables.OWNERS);
            }
            case OWNERS_ID: {
                return builder.table(Tables.OWNERS)
                        .where(Owners.OWNER_ID + "=?", Owners.getOwnerId(uri));
            }
            case COMMITS:
            case COMMITS_ID: {
                builder.table(Tables.COMMITS);

                if(matchingUriEnum == AppUriEnum.COMMITS_ID) {
                    String sha = Commits.getSha(uri);
                    builder.where(Commits.COMMIT_SHA + "=?", sha);
                }

                return builder;
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }

    /**
     * {@link AppContract} fields that are fully qualified with a specific
     * parent {@link Tables}. Used when needed to work around SQL ambiguity.
     */
    private interface Qualified {
        String REPOS_OWNER_ID = Tables.REPOS + "." + Repos.OWNER_ID;
    }
}
