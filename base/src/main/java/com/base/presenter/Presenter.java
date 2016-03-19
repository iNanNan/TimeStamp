package com.base.presenter;

/**
 * Created by heng on 16-3-18.
 */
public interface Presenter<T extends PresenterView> {

    void onViewAttach(T view);

    void onViewDetach();

    void onDestroy();
}
