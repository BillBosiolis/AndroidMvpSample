package com.example.androidmvpsample.data.io;

import android.content.ContentProviderOperation;
import android.content.Context;

import com.example.androidmvpsample.data.rest.model.RepositoryJson;

import java.util.ArrayList;
import java.util.List;

import static com.example.androidmvpsample.data.provider.AppContract.Owners;
import static com.example.androidmvpsample.data.provider.AppContract.Repos;

/**
 * Created by Bill on 16/10/2015.
 */
public class RepositoryHandler extends JSONHandler<List<RepositoryJson>> {

    public RepositoryHandler(Context context) {
        super(context);
    }

    @Override
    public void makeContentProviderOperations(ArrayList<ContentProviderOperation> list,
                                              List<RepositoryJson> data, Object... args) {
        list.add(ContentProviderOperation.newDelete(Repos.CONTENT_URI).build());
        list.add(ContentProviderOperation.newDelete(Owners.CONTENT_URI).build());

        for(RepositoryJson repo : data) {
            list.add(createOwnerOperation(repo));
            list.add(createRepoOperation(repo));
        }
    }

    private ContentProviderOperation createOwnerOperation(RepositoryJson item) {
        ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(Owners.CONTENT_URI);
        builder.withValue(Owners.OWNER_ID, item.owner.id);
        builder.withValue(Owners.OWNER_AVATAR, item.owner.avatarUrl);
        return builder.build();
    }

    private ContentProviderOperation createRepoOperation(RepositoryJson item) {
        ContentProviderOperation.Builder builder =
                ContentProviderOperation.newInsert(Repos.CONTENT_URI);
        builder.withValue(Repos.REPO_ID, item.id);
        builder.withValue(Repos.REPO_NAME, item.name);
        builder.withValue(Repos.REPO_FULLNAME, item.full_name);
        builder.withValue(Repos.REPO_HTML_URL, item.htmlUrl);
        builder.withValue(Repos.REPO_DESCRIPTION, item.description);
        builder.withValue(Repos.REPO_WATCHERS, item.watchers);
        builder.withValue(Repos.OWNER_ID, item.owner.id);
        return builder.build();
    }
}
