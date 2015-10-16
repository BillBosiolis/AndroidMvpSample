package com.example.androidmvpsample.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.androidmvpsample.R;
import com.example.androidmvpsample.data.DataRepository;
import com.example.androidmvpsample.data.rest.RestRepository;
import com.example.androidmvpsample.domain.entities.Repo;
import com.example.androidmvpsample.domain.interactors.GetRepositoriesUserCase;
import com.example.androidmvpsample.ui.views.RepositoriesView;
import com.example.androidmvpsample.utils.LogUtils;

import java.util.List;

import rx.Observer;

public class MainActivity extends AppCompatActivity implements RepositoriesView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        GetRepositoriesUserCase userCase =
                new GetRepositoriesUserCase(new DataRepository(this, new RestRepository()));
        userCase.execute().subscribe(new Observer<List<Repo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                LogUtils.LOGE("", e.getMessage());
            }

            @Override
            public void onNext(List<Repo> repos) {
                for(Repo repo : repos) {
                    LogUtils.LOGD("", repo.name);
                }
            }
        });
    }


}
