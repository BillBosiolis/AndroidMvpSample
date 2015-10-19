package com.example.androidmvpsample.ui.repositories;

import android.content.Context;
import android.content.Intent;

import com.example.androidmvpsample.domain.entities.Repo;
import com.example.androidmvpsample.domain.interactors.GetRepositoriesUseCase;
import com.example.androidmvpsample.ui.mvp.Presenter;
import com.example.androidmvpsample.ui.mvp.MvpView;
import com.example.androidmvpsample.utils.LogUtils;
import java.util.List;
import javax.inject.Inject;
import rx.Observer;
import rx.Subscription;

/**
 * Created by Bill on 16/10/2015.
 */
public class RepositoriesPresenter implements Presenter {

    private static final String TAG = LogUtils.makeLogTag(RepositoriesPresenter.class);

    Context mContext;
    GetRepositoriesUseCase mUseCase;
    RepositoriesView mView;
    Subscription mGetRepositoriesSubscription;

    @Inject
    public RepositoriesPresenter(Context context, GetRepositoriesUseCase useCase) {
        this.mContext = context;
        this.mUseCase = useCase;
    }

    @Override
    public void onResume() {
        loadData(true);
    }

    @Override
    public void onPause() {
        if(mGetRepositoriesSubscription != null) {
            mGetRepositoriesSubscription.unsubscribe();
        }
    }

    @Override
    public void onStop() {

    }

    @Override
    public void attachView(MvpView v) {
        mView = (RepositoriesView) v;
    }

    @Override
    public void loadData(boolean forceResync, Object... args) {
        mView.showLoading();
        mGetRepositoriesSubscription = mUseCase.execute(forceResync)
                .subscribe(new Observer<List<Repo>>() {
                    @Override
                    public void onCompleted() {
                        LogUtils.LOGD(TAG, "rx.onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.LOGE("", e.getMessage());
                        mView.showError();
                    }

                    @Override
                    public void onNext(List<Repo> repos) {
                        mView.setData(repos);
                        mView.showContent();
                    }
                });
    }

    public void onRepoClicked(long repoId) {
        Intent intent = new Intent(mView.getContext(), CommitsActivity.class);
        intent.putExtra("repoId", repoId);
        mView.getContext().startActivity(intent);
    }
}
