package com.example.androidmvpsample.data.provider;

import android.content.UriMatcher;
import android.net.Uri;
import android.util.SparseArray;

/**
 * Created by vbosioli on 10/14/15.
 */
public class AppProviderUriMatcher {

    private UriMatcher mUriMatcher;

    private SparseArray<AppUriEnum> mEnumsMap = new SparseArray<>();

    public AppProviderUriMatcher() {
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        buildUriMatcher();
    }

    private void buildUriMatcher() {
        final String authority = AppContract.CONTENT_AUTHORITY;

        AppUriEnum[] uris = AppUriEnum.values();
        for (int i = 0; i < uris.length; i++) {
            mUriMatcher.addURI(authority, uris[i].path, uris[i].code);
        }

        buildEnumsMap();
    }

    private void buildEnumsMap() {
        AppUriEnum[] uris = AppUriEnum.values();
        for(int i = 0; i < uris.length; i++) {
            mEnumsMap.put(uris[i].code, uris[i]);
        }
    }

    /**
     * Matches a {@code uri} to a {@link AppUriEnum}.
     *
     * @return the {@link AppUriEnum}, or throws new UnsupportedOperationException if no match.
     */
    public AppUriEnum matchUri(Uri uri){
        final int code = mUriMatcher.match(uri);
        try {
            return matchCode(code);
        } catch (UnsupportedOperationException e){
            throw new UnsupportedOperationException("Unknown uri " + uri);
        }
    }

    /**
     * Matches a {@code code} to a {@link AppUriEnum}.
     *
     * @return the {@link AppUriEnum}, or throws new UnsupportedOperationException if no match.
     */
    public AppUriEnum matchCode(int code){
        AppUriEnum appUriEnum = mEnumsMap.get(code);
        if (appUriEnum != null){
            return appUriEnum;
        } else {
            throw new UnsupportedOperationException("Unknown uri with code " + code);
        }
    }
}
