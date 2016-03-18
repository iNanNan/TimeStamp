package com.base.presenter;

/**
 * Created by heng on 16-3-18.
 */
public interface PresenterFactory<T extends Presenter> {

    T create();
}
