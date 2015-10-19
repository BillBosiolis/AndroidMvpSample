package com.example.androidmvpsample.ui.mvp;

/**
 * Created by Bill on 16/10/2015.
 */
public interface MvpDataView<T> extends MvpView {

    void showLoading();

    void showContent();

    void showError();

    void setData(T data);
}
