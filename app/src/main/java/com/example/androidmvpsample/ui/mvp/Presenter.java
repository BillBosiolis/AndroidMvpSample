/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.example.androidmvpsample.ui.mvp;

public interface Presenter {

    void onResume();

    void onPause();

    void onStop();

    void attachView(MvpView v);

    void loadData(boolean forceSync, Object... args);
}
