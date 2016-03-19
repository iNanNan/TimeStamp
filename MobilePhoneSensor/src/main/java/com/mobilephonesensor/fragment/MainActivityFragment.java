package com.mobilephonesensor.fragment;

import com.base.presenter.Presenter;
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
    protected void onPresenterComplete(Presenter presenter) {
    }

}
