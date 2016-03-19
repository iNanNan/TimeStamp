package com.mobilephonesensor.fragment;

import com.base.presenter.Presenter;
import com.mobilephonesensor.R;
import com.mobilephonesensor.base.SupperFragment;

/**
 * Created by heng on 16-3-17.
 */
public class MainActivityDocFragment extends SupperFragment {

    @Override
    protected int getContentResId() {
        return R.layout.fragment_main_doc;
    }

    @Override
    protected void onPresenterComplete(Presenter presenter) {
    }

}
