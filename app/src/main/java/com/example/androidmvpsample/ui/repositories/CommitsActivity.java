package com.example.androidmvpsample.ui.repositories;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.androidmvpsample.R;
import com.example.androidmvpsample.ReposApplication;
import com.example.androidmvpsample.di.AppComponent;
import com.example.androidmvpsample.di.HasComponent;
import com.example.androidmvpsample.di.component.DaggerRepositoriesComponent;
import com.example.androidmvpsample.di.component.RepositoriesComponent;
import com.example.androidmvpsample.di.module.ActivityModule;
import com.example.androidmvpsample.di.module.RepositoriesModule;

/**
 * Created by Bill on 19/10/2015.
 */
public class CommitsActivity extends AppCompatActivity implements HasComponent<RepositoriesComponent> {

    private RepositoriesComponent mRepositoriesComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commits);

        initComponent();

        long repoId = getIntent().getLongExtra("repoId", 0);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, CommitsFragment.newInstance(repoId));
    }

    @Override
    public RepositoriesComponent getComponent() {
        return mRepositoriesComponent;
    }

    private void initComponent() {
        AppComponent appComponent = ((ReposApplication) getApplication()).getAppComponent();
        this.mRepositoriesComponent = DaggerRepositoriesComponent.builder()
                .appComponent(appComponent)
                .activityModule(new ActivityModule(this))
                .repositoriesModule(new RepositoriesModule())
                .build();
    }
}
