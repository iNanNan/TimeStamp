package com.mobilephonesensor.model;

import android.view.View;

/**
 * Created by heng on 16-3-18.
 */
public class Tab {

    private boolean isChecked = false;

    private View tabView;

    public Tab(){}

    public Tab(boolean isChecked, View tabView) {
        this.isChecked = isChecked;
        this.tabView = tabView;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
        if (mListener != null) {
            mListener.onTab(isChecked);
        }
    }

    public View getTabView() {
        return tabView;
    }

    public void setTabView(View tabView) {
        this.tabView = tabView;
    }

    private OnTabListener mListener;

    public void setOnTabListener(OnTabListener listener) {
        this.mListener = listener;
    }

    public interface OnTabListener {
        void onTab(boolean isChecked);
    }
}
