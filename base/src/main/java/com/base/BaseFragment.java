package com.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.base.presenter.Presenter;
import com.base.presenter.PresenterFactory;
import com.base.presenter.PresenterLoader;
import com.base.presenter.PresenterView;

/**
 * Created by heng on 16-3-16.
 */
public abstract class BaseFragment extends Fragment implements PresenterView, LoaderManager.LoaderCallbacks<Presenter> {

    private final static int FRAG_LOADER_ID = 2;

    private PresenterFactory mPresenterFactory;

    private Presenter mPresenter;

    private View mContentView;

    protected abstract int getContentResId();

    protected void onPresenterComplete(Presenter p){}

    protected PresenterFactory obtainPresenterFactory() {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initContentView(inflater, container);
        return mContentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        mPresenterFactory = obtainPresenterFactory();
        if (mPresenterFactory != null) {
            getActivity().getSupportLoaderManager().initLoader(FRAG_LOADER_ID, null, this);
        }
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStop() {
        if (mPresenter != null) {
            mPresenter.onViewDetach();
        }
        super.onStop();
    }

    public <T extends View> T find(int resId) {
        return (T) (mContentView.findViewById(resId));
    }

    private void initContentView(LayoutInflater inflater, ViewGroup container) {
        mContentView = inflater.inflate(getContentResId(), container, false);
    }

    @Override
    public Loader<Presenter> onCreateLoader(int id, Bundle args) {
        return new PresenterLoader<>(getActivity(),  mPresenterFactory);
    }

    @Override
    public void onLoadFinished(Loader<Presenter> loader, Presenter data) {
        if (mPresenter == null) {
            this.mPresenter = data;
            this.mPresenter.onViewAttach(this);
            onPresenterComplete(mPresenter);
        }
    }

    @Override
    public void onLoaderReset(Loader<Presenter> loader) {
    }
}
