package com.mobilephonesensor.model;

import android.view.View;

/**
 * Created by heng on 16-3-18.
 */
public class Tab {

    private boolean isChecked = false;

    private int themeNormalColor = 0xFF999999;

    private int themeActiveColor = 0xFF18B0F4;

    private View tabView;

    public Tab(){}

    public Tab(boolean isChecked, View tabView) {
        this.isChecked = isChecked;
        this.tabView = tabView;
    }

    public int getThemeNormalColor() {
        return themeNormalColor;
    }

    public void setThemeNormalColor(int themeNormalColor) {
        this.themeNormalColor = themeNormalColor;
    }

    public int getThemeActiveColor() {
        return themeActiveColor;
    }

    public void setThemeActiveColor(int themeActiveColor) {
        this.themeActiveColor = themeActiveColor;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public View getTabView() {
        return tabView;
    }

    public void setTabView(View tabView) {
        this.tabView = tabView;
    }
}
