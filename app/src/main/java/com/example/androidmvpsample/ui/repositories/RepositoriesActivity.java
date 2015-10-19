package com.example.androidmvpsample.ui.repositories;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.androidmvpsample.R;
import com.example.androidmvpsample.ReposApplication;
import com.example.androidmvpsample.di.component.DaggerRepositoriesComponent;
import com.example.androidmvpsample.di.module.ActivityModule;
import com.example.androidmvpsample.di.module.RepositoriesModule;
import com.example.androidmvpsample.domain.entities.Repo;
import com.example.androidmvpsample.ui.adapter.RepositoriesAdapter;
import com.example.androidmvpsample.utils.LogUtils;

import java.util.List;

import javax.inject.Inject;

public class RepositoriesActivity extends AppCompatActivity implements
        RepositoriesView, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = LogUtils.makeLogTag(RepositoriesActivity.class);

    @Inject
    RepositoriesPresenter mPresenter;

    private SwipeRefreshLayout mSwipeRefresh;
    private RecyclerView mRecyclerView;
    private View mError;
    private RepositoriesAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSwipeRefresh = (SwipeRefreshLayout) findViewById(R.id.refresh);
        mSwipeRefresh.setOnRefreshListener(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mError = findViewById(R.id.error);

        setupRecyclerView();
        injectDependencies();
        mPresenter.attachView(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.onPause();
    }

    @Override
    public void showLoading() {
        mSwipeRefresh.setRefreshing(true);
        mError.setVisibility(View.GONE);
    }

    @Override
    public void showContent() {
        mSwipeRefresh.setRefreshing(false);
        mError.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showError() {
        mError.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void setData(final List<Repo> data) {
        mAdapter = new RepositoriesAdapter(this, data);
        mAdapter.setCallbacks(new RepositoriesAdapter.Callbacks() {
            @Override
            public void onRepoClicked(int position) {
                LogUtils.LOGD(TAG, "Clickced at postion " + position);
                long repoId = data.get(position).id;
                mPresenter.onRepoClicked(repoId);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public Context getContext() {
        return this;
    }

    private void injectDependencies() {
        ReposApplication reposApplication = (ReposApplication) getApplication();
        DaggerRepositoriesComponent.builder()
                .activityModule(new ActivityModule(this))
                .appComponent(reposApplication.getAppComponent())
                .repositoriesModule(new RepositoriesModule())
                .build()
                .inject(this);
    }

    private void setupRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onRefresh() {
        mPresenter.loadData(true);
    }
}
