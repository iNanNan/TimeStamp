package com.mobilephonesensor.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.base.inject.InjectView;
import com.base.message.Event;
import com.base.message.RxBus;
import com.base.message.Subscriber;
import com.mobilephonesensor.R;
import com.mobilephonesensor.base.SupperFragment;
import com.mobilephonesensor.test.Test2Activity;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends SupperFragment {

    @InjectView(R.id.frag_main_text)
    TextView test;

    @InjectView(R.id.frag_main_frame)
    FrameLayout frameLayout;

    @Override
    protected int getContentResId() {
        return R.layout.fragment_main;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Test2Activity.class));
            }
        });
        RxBus.getInstance().register(this);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.frag_main_frame, new MainActivityAboutFragment());
        transaction.commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxBus.getInstance().unregister(this);
    }

    @SuppressWarnings("unused")
    @Subscriber
    public void onEvent(Event event) {
        long evtLong = event.getData().getLong("cur_tid");
        Log.e("MainActivityFragment", evtLong + "-" + Thread.currentThread().getId());
    }
}
