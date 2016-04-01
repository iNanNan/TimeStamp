package com.mobilephonesensor.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.base.inject.InjectView;
import com.base.message.Event;
import com.base.message.RxBus;
import com.base.message.SubscriberHandMethod;
import com.mobilephonesensor.R;
import com.mobilephonesensor.base.SupperFragment;
import com.mobilephonesensor.test.TestActivity;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends SupperFragment {

    @InjectView(R.id.frag_main_text)
    TextView test;

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
                startActivity(new Intent(getContext(), TestActivity.class));
            }
        });
        RxBus.getInstance().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxBus.getInstance().unregister(this);
    }

    @SuppressWarnings("unused")
    @SubscriberHandMethod
    public void onEvent(Event event) {
        long evtLong = event.getData().getLong("cur_tid");
        Log.e("MainActivityFragment", evtLong + "-" + Thread.currentThread().getId());
    }
}
