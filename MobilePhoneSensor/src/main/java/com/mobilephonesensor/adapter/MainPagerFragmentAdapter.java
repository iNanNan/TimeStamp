package com.mobilephonesensor.adapter;

import android.support.v4.app.FragmentManager;

import com.base.adapter.BaseFragmentPagerAdapter;
import com.mobilephonesensor.fragment.MainActivityAboutFragment;
import com.mobilephonesensor.fragment.MainActivityDocFragment;
import com.mobilephonesensor.fragment.MainActivityFragment;

/**
 * Created by heng on 16-3-18.
 */
public class MainPagerFragmentAdapter extends BaseFragmentPagerAdapter {

    public MainPagerFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    protected void createFragments() {
        addFragment(new MainActivityFragment());
        addFragment(new MainActivityDocFragment());
        addFragment(new MainActivityAboutFragment());
    }
}
