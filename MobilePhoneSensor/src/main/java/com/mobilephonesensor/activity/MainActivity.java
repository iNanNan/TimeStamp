package com.mobilephonesensor.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base.presenter.Presenter;
import com.base.util.ScreenUtil;
import com.mobilephonesensor.R;
import com.mobilephonesensor.adapter.MainPagerFragmentAdapter;
import com.mobilephonesensor.base.SupperActivity;
import com.mobilephonesensor.model.Tab;
import com.mobilephonesensor.widgets.ViewPagerIndicator;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends SupperActivity {

    private Toolbar mToolbar;

    private ViewPager mViewPager;

    private DrawerLayout mDrawerLayout;

    private ViewPagerIndicator mPageIndicator;

    @Override
    protected int getContentResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onPresenterComplete(Presenter presenter) {

    }

    protected void bindView() {
        mViewPager = find(R.id.act_main_view_page);
        mDrawerLayout = find(R.id.act_main_drawer);
        mPageIndicator = find(R.id.act_main_indicator);
    }

    @Override
    protected Toolbar onCreateToolbar() {
        mToolbar = find(R.id.act_main_toolbar);
        mToolbar.setNavigationIcon(R.mipmap.ic_menu_emoticons);
        return mToolbar;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindView();
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initToggle();
        calculateToolbarHeight();
        mViewPager.setAdapter(new MainPagerFragmentAdapter(getSupportFragmentManager()));
        createBottomTabs();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        mToggle.syncState();
    }

    private ActionBarDrawerToggle mToggle;

    private void initToggle() {
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                if (slideOffset > 0) {
                    setMenuNavMarginTop(false);
                } else {
                    setMenuNavMarginTop(true);
                }
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

        };
        mDrawerLayout.addDrawerListener(mToggle);
    }

    private int mToolbarHeight;

    private int mBottomTabHeight;

    private void calculateToolbarHeight() {
        mBottomTabHeight = ScreenUtil.dip2px(this, 55);
        mToolbar.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mToolbar.getViewTreeObserver().removeOnPreDrawListener(this);
                mToolbarHeight = mToolbar.getHeight();
                return true;
            }
        });
    }

    private void setMenuNavMarginTop(boolean showToolbar) {
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) mDrawerLayout.getLayoutParams();
        if (showToolbar) {
            params.topMargin = mToolbarHeight;
            params.bottomMargin = mBottomTabHeight;
        } else {
            params.topMargin = 0;
            params.bottomMargin = 0;
        }
        mDrawerLayout.setLayoutParams(params);
    }

    private void createBottomTabs() {
        final int highColor = Color.parseColor("#FF0000");
        final int normalColor = Color.parseColor("#FFFFFF");
        LayoutInflater inflater = loaderInflater();
        List<Tab> tabViews = new ArrayList<>(3);
        for (int i = 0; i< 3; i++) {
            LinearLayout tabLayout = (LinearLayout) inflater.inflate(R.layout.layout_main_tab_item, getViewGroup());
            final ImageView icon = (ImageView) tabLayout.findViewById(R.id.layout_main_tab_icon);
            final TextView text = (TextView) tabLayout.findViewById(R.id.layout_main_tab_text);
            Tab tab = new Tab(false, tabLayout);
            tab.setOnTabListener(new Tab.OnTabListener() {
                @Override
                public void onTab(boolean isChecked) {
                    if (isChecked) {
                        text.setTextColor(highColor);
                    } else {
                        text.setTextColor(normalColor);
                    }
                }
            });
            tabViews.add(tab);
        }
        mPageIndicator.setTabs(tabViews);
        mPageIndicator.setViewPager(mViewPager, 0);
    }
}
