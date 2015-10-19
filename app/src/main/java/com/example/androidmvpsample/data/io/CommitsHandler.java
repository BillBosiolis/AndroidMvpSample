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
    public void makeContentProviderOperations(ArrayList<ContentProviderOperation> list,
                                              List<CommitJson> data, Object... args) {
        long repoId = (long) args[0];

        list.add(ContentProviderOperation.newDelete(Commits.CONTENT_URI)
                .withSelection(Commits.REPO_ID + "=?", new String[]{String.valueOf(repoId)})
                .build());

        for(CommitJson c : data) {
            list.add(createCommitInsertOperation(c, repoId));
        }
    }

    private ContentProviderOperation createCommitInsertOperation(CommitJson item, long repoId) {
        ContentProviderOperation.Builder builder =
                ContentProviderOperation.newInsert(Commits.CONTENT_URI);
        builder.withValue(Commits.COMMIT_SHA, item.sha);
        builder.withValue(Commits.COMMIT_URL, item.url);
        builder.withValue(Commits.REPO_HTML_URL, item.htmlUrl);
        builder.withValue(Commits.COMMIT_AUTHOR, item.author);
        builder.withValue(Commits.COMMIT_MESSAGE, item.commit.message);
        builder.withValue(Commits.COMMIT_AUTHOR_NAME, item.author.name);
        builder.withValue(Commits.COMMIT_AUTHOR_DATE, item.author.date);
        builder.withValue(Commits.REPO_ID, repoId);
        return builder.build();
    }
}
