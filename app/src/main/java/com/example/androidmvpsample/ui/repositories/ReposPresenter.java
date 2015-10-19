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
public class ReposPresenter implements Presenter {

    private static final String TAG = LogUtils.makeLogTag(ReposPresenter.class);

    Context mContext;
    GetRepositoriesUseCase mUseCase;
    ReposView mView;
    Subscription mGetRepositoriesSubscription;

    @Inject
    public ReposPresenter(Context context, GetRepositoriesUseCase useCase) {
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
        mView = (ReposView) v;
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

    public void onRepoClicked(long repoId, String name) {
        Intent intent = new Intent(mView.getContext(), RepoCommitsActivity.class);
        intent.putExtra("repoId", repoId);
        intent.putExtra("repoName", name);
        mView.getContext().startActivity(intent);
    }
}
