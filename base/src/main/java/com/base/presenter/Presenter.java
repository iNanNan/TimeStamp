package com.base.presenter;

/**
 * Created by heng on 16-3-18.
 */
public interface Presenter<T> {

    void onViewAttach(T view);

    void onViewDetach();

    void onDestroy();
}
