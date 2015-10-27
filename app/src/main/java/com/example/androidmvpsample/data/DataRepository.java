package com.example.androidmvpsample.data;

import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.os.RemoteException;

import com.example.androidmvpsample.data.io.CommitsHandler;
import com.example.androidmvpsample.data.io.RepositoryHandler;
import com.example.androidmvpsample.data.rest.RestRepository;
import com.example.androidmvpsample.data.rest.RestResponse;
import com.example.androidmvpsample.data.rest.model.CommitJson;
import com.example.androidmvpsample.data.rest.model.RepositoryJson;
import com.example.androidmvpsample.domain.Repository;
import com.example.androidmvpsample.domain.entities.Commit;
import com.example.androidmvpsample.domain.entities.Repo;
import com.example.androidmvpsample.utils.LogUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

import static com.example.androidmvpsample.data.provider.AppContract.CONTENT_AUTHORITY;
import static com.example.androidmvpsample.data.provider.AppContract.Commits;
import static com.example.androidmvpsample.data.provider.AppContract.Repos;

/**
 * Created by Bill on 16/10/2015.
 */
public class DataRepository implements Repository {

    private static final String TAG = LogUtils.makeLogTag(DataRepository.class);

    private final RestRepository restRepository;
    private final Context context;

    @Inject
    public DataRepository(Context context, RestRepository restRepository) {
        this.restRepository = restRepository;
        this.context = context;
    }

    @Override
    public Observable<List<Repo>> getRepositories(final boolean forceResync) {
        return Observable.create(new Observable.OnSubscribe<List<Repo>>() {
            @Override
            public void call(Subscriber<? super List<Repo>> subscriber) {
                try {
                    RestResponse<List<RepositoryJson>> jsons = restRepository.getRepositories(forceResync);

                    if(!jsons.isFromCache()) {
                        LogUtils.LOGD(TAG, "New data from the Network. Saving them in the database");
                        RepositoryHandler handler = new RepositoryHandler(context);
                        ArrayList<ContentProviderOperation> operations =
                                new ArrayList<ContentProviderOperation>();
                        handler.makeContentProviderOperations(operations, jsons.getData());
                        context.getContentResolver().applyBatch(CONTENT_AUTHORITY, operations);
                    }

                    List<Repo> repos = new ArrayList<Repo>();

                    Cursor cursor = context.getContentResolver().query(
                            Repos.CONTENT_URI,
                            RepoQuery.PROJECTION,
                            null, null, null);
                    if(cursor != null && cursor.moveToFirst()) {
                        do {
                            repos.add(new Repo(
                                    cursor.getLong(RepoQuery.REPO_ID),
                                    cursor.getString(RepoQuery.REPO_NAME),
                                    cursor.getString(RepoQuery.REPO_FULLNAME),
                                    cursor.getString(RepoQuery.REPO_HTML_URL),
                                    cursor.getString(RepoQuery.REPO_DESCRIPTION),
                                    cursor.getInt(RepoQuery.REPO_WATCHERS),
                                    cursor.getString(RepoQuery.OWNER_AVATAR)));
                        } while (cursor.moveToNext());
                    }

                    if(cursor != null) {
                        cursor.close();
                    }

                    subscriber.onNext(repos);
                    subscriber.onCompleted();
                } catch (IOException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                } catch (RemoteException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    LogUtils.LOGE(TAG, e.getMessage(), e);
                }
            }
        });
    }

    @Override
    public Observable<Repo> getRepository(long repoId) {
        return null;
    }

    @Override
    public Observable<List<Commit>> getCommits(final boolean forceResync, final long repoId, final String repoName) {
        return Observable.create(new Observable.OnSubscribe<List<Commit>>() {
            @Override
            public void call(Subscriber<? super List<Commit>> subscriber) {
                try {
                    RestResponse<List<CommitJson>> restResponse = restRepository.getCommits(forceResync, repoName);

                    if(!restResponse.isFromCache()) {
                        CommitsHandler handler = new CommitsHandler(context);
                        ArrayList<ContentProviderOperation> operations =
                                new ArrayList<ContentProviderOperation>();
                        handler.makeContentProviderOperations(operations, restResponse.getData(), repoId);
                        context.getContentResolver().applyBatch(CONTENT_AUTHORITY, operations);
                    }

                    List<Commit> commits = new ArrayList<Commit>();

                    Cursor cursor = context.getContentResolver().query(
                            Commits.CONTENT_URI,
                            CommitQuery.PROJECTION,
                            Commits.REPO_ID + "=?", new String[] { String.valueOf(repoId) },
                            null);

                    if(cursor != null && cursor.moveToFirst()) {
                        do {
                            commits.add(new Commit(
                                    cursor.getString(CommitQuery.COMMIT_SHA),
                                    cursor.getString(CommitQuery.COMMIT_URL),
                                    cursor.getString(CommitQuery.COMMIT_HTML_URL),
                                    cursor.getString(CommitQuery.COMMIT_MESSAGE),
                                    cursor.getString(CommitQuery.COMMIT_AUTHOR_NAME),
                                    cursor.getString(CommitQuery.COMMIT_AUTHOR_DATE)));
                        } while(cursor.moveToNext());
                    }

                    if(cursor != null) {
                        cursor.close();
                    }

                    subscriber.onNext(commits);
                    subscriber.onCompleted();
                } catch (IOException | RemoteException | OperationApplicationException e) {
                    LogUtils.LOGE(TAG, e.getMessage(), e);
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<Commit> getCommit(String sha) {
        return null;
    }

    interface RepoQuery {
        String[] PROJECTION = new String[] {
                Repos.REPO_ID,
                Repos.REPO_NAME,
                Repos.REPO_FULLNAME,
                Repos.REPO_HTML_URL,
                Repos.REPO_DESCRIPTION,
                Repos.REPO_WATCHERS,
                Repos.OWNER_AVATAR
        };

        int REPO_ID = 0;
        int REPO_NAME = 1;
        int REPO_FULLNAME = 2;
        int REPO_HTML_URL = 3;
        int REPO_DESCRIPTION = 4;
        int REPO_WATCHERS = 5;
        int OWNER_AVATAR = 6;
    }

    interface CommitQuery {
        String[] PROJECTION = new String[] {
                Commits.COMMIT_SHA,
                Commits.COMMIT_URL,
                Commits.COMMIT_HTML_URL,
                Commits.COMMIT_MESSAGE,
                Commits.COMMIT_AUTHOR_NAME,
                Commits.COMMIT_AUTHOR_DATE
        };

        int COMMIT_SHA = 0;
        int COMMIT_URL = 1;
        int COMMIT_HTML_URL = 2;
        int COMMIT_MESSAGE = 3;
        int COMMIT_AUTHOR_NAME = 4;
        int COMMIT_AUTHOR_DATE = 5;
    }
}
