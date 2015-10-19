package com.example.androidmvpsample.ui.repositories;

import android.content.Context;

import com.example.androidmvpsample.domain.entities.Commit;
import com.example.androidmvpsample.domain.interactors.GetCommitsUseCase;
import com.example.androidmvpsample.ui.mvp.MvpView;
import com.example.androidmvpsample.ui.mvp.Presenter;
import com.example.androidmvpsample.utils.LogUtils;

import java.util.List;

import javax.inject.Inject;

import rx.Observer;
import rx.Subscription;

/**
 * Created by Bill on 19/10/2015.
 */
public class RepoCommitsPresenter implements Presenter {

    private static final String TAG = LogUtils.makeLogTag(RepoCommitsPresenter.class);

    Context mContext;
    GetCommitsUseCase mUseCase;
    RepoCommitsView mView;
    Subscription mGetCommitsUseCaseSubscription;

    @Inject
    public RepoCommitsPresenter(Context context, GetCommitsUseCase useCase) {
        this.mContext = context;
        this.mUseCase = useCase;
    }

    @Override
    public void onResume() {
    }

    @Override
    public void onPause() {
        if(mGetCommitsUseCaseSubscription != null) {
            mGetCommitsUseCaseSubscription.unsubscribe();
        }
    }

    @Override
    public void onStop() {

    }

    @Override
    public void attachView(MvpView v) {
        mView = (RepoCommitsView) v;
    }

    @Override
    public void loadData(boolean forceSync, Object... args) {
        mView.showLoading();

        mGetCommitsUseCaseSubscription = mUseCase.execute(forceSync, args)
                .subscribe(new Observer<List<Commit>>() {
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
                    public void onNext(List<Commit> commits) {
                        mView.setData(commits);
                        mView.showContent();
                    }
                });
    }
}
