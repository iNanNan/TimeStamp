package com.base.presenter;

import android.content.Context;
import android.support.v4.content.Loader;

/**
 * Created by heng on 16-3-18.
 */
public class PresenterLoader<T extends Presenter> extends Loader<T> {

    private PresenterFactory<T> mFactory;

    private T mPresenter;

    /**
     * Stores away the application context associated with context.
     * Since Loaders can be used across multiple activities it's dangerous to
     * store the context directly; always use {@link #getContext()} to retrieve
     * the Loader's Context, don't use the constructor argument directly.
     * The Context returned by {@link #getContext} is safe to use across
     * Activity instances.
     *
     * @param context used to retrieve the application context.
     */
    public PresenterLoader(Context context, PresenterFactory factory) {
        super(context);
        this.mFactory = factory;
    }

    @Override
    protected void onStartLoading() {
        if (mPresenter == null)
            forceLoad();
        else
            deliverResult(mPresenter);

    }

    @Override
    protected void onForceLoad() {
        if (mFactory != null) {
            mPresenter = mFactory.create();
            deliverResult(mPresenter);
        }
    }

    @Override
    protected void onReset() {
        if (mPresenter != null) {
            mPresenter.onDestroy();
            mPresenter = null;
        }
    }
}
