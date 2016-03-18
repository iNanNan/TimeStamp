package com.mobilephonesensor.fragment;

import com.base.presenter.Presenter;
import com.base.presenter.PresenterFactory;
import com.mobilephonesensor.R;
import com.mobilephonesensor.base.SupperFragment;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends SupperFragment {

    @Override
    protected int getContentResId() {
        return R.layout.fragment_main;
    }

    @Override
    protected void bindView() {

    }

    @Override
    protected PresenterFactory obtainPresenterFactory() {
        return new SimplePresenterFractory();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private class SimplePresenterFractory implements PresenterFactory {

        @Override
        public Presenter create() {
            return new SimplePresenter();
        }
    }

    private class SimplePresenter implements Presenter {

        @Override
        public void onViewAttach(Object view) {

        }

        @Override
        public void onViewDetach() {

        }

        @Override
        public void onDestroy() {

        }
    }
}
