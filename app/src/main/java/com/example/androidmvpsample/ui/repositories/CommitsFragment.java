package com.example.androidmvpsample.ui.repositories;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.androidmvpsample.R;
import com.example.androidmvpsample.di.HasComponent;
import com.example.androidmvpsample.di.component.RepositoriesComponent;
import com.example.androidmvpsample.domain.entities.Commit;
import com.example.androidmvpsample.ui.adapter.CommitsAdapter;
import com.example.androidmvpsample.utils.LogUtils;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Bill on 19/10/2015.
 */
public class CommitsFragment extends Fragment implements CommitsView {

    public static CommitsFragment newInstance(long repoId) {
        CommitsFragment fragment = new CommitsFragment();
        Bundle args = new Bundle();
        args.putLong("repoId", repoId);
        fragment.setArguments(args);
        return fragment;
    }


    private static final String TAG = LogUtils.makeLogTag(CommitsFragment.class);

    @Inject
    CommitsPresenter mPresenter;

    private RecyclerView mRecyclerView;
    private View mError;
    private ProgressBar mProgressBar;
    private CommitsAdapter mAdapter;
    private long mRepoId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRepoId = getArguments().getLong("repoId");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_commits, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mError = view.findViewById(R.id.error);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupRecyclerView();
        injectDependencies();
        mPresenter.attachView(this);
        mPresenter.loadData(true, mRepoId);
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.onPause();
    }

    @Override
    public void showLoading() {
        mProgressBar.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
        mError.setVisibility(View.GONE);
    }

    @Override
    public void showContent() {
        mProgressBar.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        mError.setVisibility(View.GONE);
    }

    @Override
    public void showError() {
        mProgressBar.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.GONE);
        mError.setVisibility(View.VISIBLE);
    }

    @Override
    public void setData(List<Commit> data) {
        mAdapter = new CommitsAdapter(getActivity(), data);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void setupRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void injectDependencies() {
        // Option A
        RepositoriesComponent comp = ((HasComponent<RepositoriesComponent>) getActivity()).getComponent();
        comp.inject(this);

        // Option B
        /*ReposApplication reposApplication = (ReposApplication) getActivity().getApplication();
        DaggerRepositoriesComponent.builder()
                .appComponent(reposApplication.getAppComponent())
                .activityModule(new ActivityModule(getActivity()))
                .repositoriesModule(new RepositoriesModule())
                .build()
                .inject(this);*/
    }

    private <C> C getComponent(Class<C> componentType) {
        return componentType.cast(((HasComponent<C>) getActivity()).getComponent());
    }
}
