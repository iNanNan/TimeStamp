package com.mobilephonesensor.test;

import android.widget.TextView;

import com.base.BaseActivity;
import com.base.presenter.Presenter;
import com.base.presenter.PresenterFactory;
import com.mobilephonesensor.R;

/**
 * Created by heng on 16-3-21.
 */
public class TestActivity extends BaseActivity implements TestPresenterView {

    @Override
    protected int getContentResId() {
        return R.layout.act_test;
    }

    @Override
    protected PresenterFactory obtainPresenterFactory() {
        return new TestPresenterFactory();
    }

    @Override
    protected void onPresenterComplete(Presenter p) {
        super.onPresenterComplete(p);
        if (p instanceof TestPresenterFactory.TestPresenter) {
            ((TestPresenterFactory.TestPresenter) p).setDataToView();
        }
    }

    @Override
    public void showTestText(String showText) {
        ((TextView)(find(R.id.act_test_text))).setText(showText);
    }
}
