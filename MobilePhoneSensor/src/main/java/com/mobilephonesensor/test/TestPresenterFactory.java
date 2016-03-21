package com.mobilephonesensor.test;

import com.base.presenter.Presenter;
import com.base.presenter.PresenterFactory;
import com.base.presenter.PresenterView;

/**
 * Created by heng on 16-3-21.
 */
public class TestPresenterFactory implements PresenterFactory {

    @Override
    public Presenter create() {
        return new TestPresenter();
    }

    public static class TestPresenter implements Presenter {

        private TestPresenterView view;

        public void setDataToView() {
            view.showTestText("Hello MVP!");
        }

        @Override
        public void onViewAttach(PresenterView view) {
            this.view = (TestPresenterView) view;
        }

        @Override
        public void onViewDetach() {

        }

        @Override
        public void onDestroy() {
            if (view != null) {
                view = null;
            }
        }
    }
}
