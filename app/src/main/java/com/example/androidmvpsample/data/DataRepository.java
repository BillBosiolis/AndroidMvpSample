package com.example.androidmvpsample.data;

import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.os.RemoteException;

import com.example.androidmvpsample.data.io.RepositoryHandler;
import com.example.androidmvpsample.data.rest.RestRepository;
import com.example.androidmvpsample.data.rest.model.RepositoryJson;
import com.example.androidmvpsample.domain.Repository;
import com.example.androidmvpsample.domain.entities.Commit;
import com.example.androidmvpsample.domain.entities.Repo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

import static com.example.androidmvpsample.data.provider.AppContract.CONTENT_AUTHORITY;
import static com.example.androidmvpsample.data.provider.AppContract.Repos;

/**
 * Created by Bill on 16/10/2015.
 */
public class DataRepository implements Repository {

    private final RestRepository restRepository;
    private final Context context;

    public DataRepository(Context context, RestRepository restRepository) {
        this.restRepository = restRepository;
        this.context = context;
    }

    @Override
    public Observable<List<Repo>> getRepositories() {
        return Observable.create(new Observable.OnSubscribe<List<Repo>>() {
            @Override
            public void call(Subscriber<? super List<Repo>> subscriber) {
                try {
                    List<RepositoryJson> jsons = restRepository.getRepositories();

                    RepositoryHandler handler = new RepositoryHandler(context);
                    ArrayList<ContentProviderOperation> operations =
                            new ArrayList<ContentProviderOperation>();
                    handler.makeContentProviderOperations(operations, jsons);
                    context.getContentResolver().applyBatch(CONTENT_AUTHORITY, operations);

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

                    subscriber.onNext(repos);
                    subscriber.onCompleted();
                } catch (IOException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                } catch (RemoteException e) {
                    e.printStackTrace();
                } catch (OperationApplicationException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<Repo> getRepository(long repoId) {
        return null;
    }

    @Override
    public Observable<List<Commit>> getCommits(long repoId) {
        return null;
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
}
