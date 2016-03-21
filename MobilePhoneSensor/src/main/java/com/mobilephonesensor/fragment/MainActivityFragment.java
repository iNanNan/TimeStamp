package com.mobilephonesensor.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.base.presenter.Presenter;
import com.mobilephonesensor.R;
import com.mobilephonesensor.base.SupperFragment;
import com.mobilephonesensor.test.TestActivity;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends SupperFragment {

    @Override
    protected int getContentResId() {
        return R.layout.fragment_main;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        View test = find(R.id.frag_main_text);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), TestActivity.class));
            }
        });
    }
}
