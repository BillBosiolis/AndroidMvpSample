package com.example.androidmvpsample.ui.repositories;

import com.example.androidmvpsample.domain.entities.Commit;
import com.example.androidmvpsample.ui.mvp.MvpDataView;

import java.util.List;

/**
 * Created by Bill on 19/10/2015.
 */
public interface RepoCommitsView extends MvpDataView<List<Commit>> {
}
