package com.base.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heng on 16-3-18.
 */
public abstract class  BaseFragmentPagerAdapter<F extends Fragment> extends FragmentPagerAdapter {

    private List<F> fragments;

    public BaseFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        if (fragments == null) {
            fragments = new ArrayList<>();
        }
        createFragments();
    }

    /** 添加所有的fragment */
    protected abstract void createFragments();

    /**
     * 该方法不是必须使用的，你可以直接在
     * createFragments方法中创建所有的
     * fragment，但是如果你想使用该方法
     * 就必须在createFragments中调用
     * @param fragment
     */
    protected void addFragment(F fragment) {
        fragments.add(fragment);
    }

    public List<F> getFragments() {
        return fragments;
    }

    @Override
    public int getCount() {
        return fragments == null ? 0 : fragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        if (fragments == null || fragments.size() == 0)
            return null;
        else
            return fragments.get(position);
    }
}
