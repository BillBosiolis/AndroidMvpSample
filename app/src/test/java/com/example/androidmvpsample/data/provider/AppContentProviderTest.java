package com.example.androidmvpsample.data.provider;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;

import com.example.androidmvpsample.BuildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.shadows.ShadowContentResolver;

import static com.example.androidmvpsample.data.provider.AppContract.CONTENT_AUTHORITY;
import static com.example.androidmvpsample.data.provider.AppContract.Owners;
import static com.example.androidmvpsample.data.provider.AppContract.Repos;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * Created by Bill on 24/10/2015.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class AppContentProviderTest {

    ShadowApplication app;
    ShadowContentResolver resolver;
    AppContentProvider provider;

    static final int OWNER_ID = 1;
    static final int REPO_1_ID = 1;
    static final int REPO_2_ID = 2;
    static final int REPO_3_ID = 3;

    @Before
    public void setup() {
        app = ShadowApplication.getInstance();
        resolver = Shadows.shadowOf(app.getContentResolver());

        provider = new AppContentProvider();
        provider.onCreate();

        ShadowContentResolver.registerProvider(CONTENT_AUTHORITY, provider);
    }

    @Test
    public void testQueryRepos() {
        prepareData(resolver);

        Cursor cursor = resolver.query(Repos.CONTENT_URI, null, null, null, null);
        cursor.moveToFirst();

        assertEquals("Count", 2, cursor.getCount());
        assertEquals("Repo Id", REPO_1_ID, cursor.getInt(
                cursor.getColumnIndex(Repos.REPO_ID)));
    }

    @Test
    public void testInsertRepo() {
        Uri uri = resolver.insert(Repos.CONTENT_URI, createRepoValues(REPO_3_ID, "Repo3", "Repository 3",
                "http://www.github.com/repo3", "Repository 3 Desc", 10, OWNER_ID));
        assertNotNull("Uri of Inserted Repo", uri);
        assertEquals("Uri of Inserted Repo", Repos.buildRepoUri(String.valueOf(REPO_3_ID)), uri);
    }

    private void prepareData(ShadowContentResolver resolver) {
        resolver.insert(Owners.CONTENT_URI, createOwnerValues(OWNER_ID, "avatar"));
        resolver.insert(Repos.CONTENT_URI, createRepoValues(REPO_1_ID, "Repo 1", "Repository 1",
                "http://github.com/repo1", "Repo 1 Description", 10, OWNER_ID));
        resolver.insert(Repos.CONTENT_URI, createRepoValues(REPO_2_ID, "Repo 2", "Repository 2",
                "http://github.com/repo2", "Repo 2 Description", 10, OWNER_ID));
    }

    private ContentValues createOwnerValues(int ownerId, String avatar) {
        ContentValues values = new ContentValues();
        values.put(Owners.OWNER_ID, ownerId);
        values.put(Owners.OWNER_AVATAR, avatar);
        return values;
    }

    private ContentValues createRepoValues(int repoId, String name, String fullName, String htmlUrl,
                                           String description, int watchers, int ownerId) {
        ContentValues values = new ContentValues();
        values.put(Repos.REPO_ID, repoId);
        values.put(Repos.REPO_NAME, name);
        values.put(Repos.REPO_FULLNAME, fullName);
        values.put(Repos.REPO_HTML_URL, htmlUrl);
        values.put(Repos.REPO_DESCRIPTION, description);
        values.put(Repos.REPO_WATCHERS, watchers);
        values.put(Repos.OWNER_ID, ownerId);
        return values;
    }
}
