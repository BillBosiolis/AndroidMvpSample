package com.example.androidmvpsample.data.io;

import android.content.ContentProviderOperation;
import android.content.Context;

import com.example.androidmvpsample.data.rest.model.CommitJson;

import java.util.ArrayList;
import java.util.List;

import static com.example.androidmvpsample.data.provider.AppContract.Commits;

/**
 * Created by Bill on 16/10/2015.
 */
public class CommitsHandler extends JSONHandler<List<CommitJson>> {

    public CommitsHandler(Context context) {
        super(context);
    }

    @Override
    public void makeContentProviderOperations(ArrayList<ContentProviderOperation> list, List<CommitJson> data) {
        list.add(ContentProviderOperation.newDelete(Commits.CONTENT_URI).build());
        for(CommitJson item : data) {
            list.add(createCommitOperation(item));
        }
    }

    private ContentProviderOperation createCommitOperation(CommitJson item) {
        ContentProviderOperation.Builder builder =
                ContentProviderOperation.newInsert(Commits.CONTENT_URI);
        builder.withValue(Commits.COMMIT_SHA, item.sha);
        builder.withValue(Commits.COMMIT_URL, item.url);
        builder.withValue(Commits.COMMIT_HTML_URL, item.htmlUrl);
        builder.withValue(Commits.COMMIT_AUTHOR, item.commit.author);
        builder.withValue(Commits.COMMIT_MESSAGE, item.commit.message);
        builder.withValue(Commits.COMMIT_AUTHOR_DATE, item.author.date);
        builder.withValue(Commits.COMMIT_AUTHOR_NAME, item.author.name);
        return builder.build();
    }
}
