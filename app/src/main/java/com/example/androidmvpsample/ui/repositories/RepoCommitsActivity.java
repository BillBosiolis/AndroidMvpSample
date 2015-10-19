package com.example.androidmvpsample.ui.repositories;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.androidmvpsample.R;

/**
 * Created by Bill on 19/10/2015.
 */
public class RepoCommitsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commits);

        long repoId = getIntent().getLongExtra("repoId", 0);
        String repoName = getIntent().getStringExtra("repoName");
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, RepoCommitsFragment.newInstance(repoId, repoName))
                .commit();
    }
}
