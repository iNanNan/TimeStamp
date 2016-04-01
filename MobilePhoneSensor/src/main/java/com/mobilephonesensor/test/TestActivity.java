package com.mobilephonesensor.test;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.base.inject.InjectView;
import com.base.message.Event;
import com.base.message.RxBus;
import com.base.message.SubscriberHandMethod;
import com.base.presenter.Presenter;
import com.base.presenter.PresenterFactory;
import com.base.thread.BaseTask;
import com.mobilephonesensor.R;
import com.mobilephonesensor.base.SupperActivity;

import java.util.concurrent.TimeUnit;


/**
 * Created by heng on 16-3-21.
 */
public class TestActivity extends SupperActivity implements TestPresenterView {

    @InjectView(R.id.act_test_text)
    private TextView textView;

    @InjectView(R.id.act_test_image)
    private ImageView imageView;

    @InjectView(R.id.act_test_switcher)
    private ImageSwitcher switcher;

    private int[] images = {R.mipmap.themebg_d_2, R.mipmap.themebg_dn_2};

    private int current = 0;

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
        Log.e("showTestText", showText);
        textView.setText(showText);
        switcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView view = new ImageView(TestActivity.this);
                ImageSwitcher.LayoutParams params = new ImageSwitcher.LayoutParams(ImageSwitcher.LayoutParams.MATCH_PARENT,
                        ImageSwitcher.LayoutParams.MATCH_PARENT);
                view.setLayoutParams(params);
                view.setScaleType(ImageView.ScaleType.FIT_XY);
                return view;
            }
        });
        switcher.setImageResource(images[current]);
        current = 1;
        switcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (current >= images.length) {
                    current = 0;
                }
                switcher.setImageResource(images[current]);
                current++;
            }
        });

        new TestThread(){

            @Override
            protected void onSuccess(Integer result) {
                super.onSuccess(result);
                imageView.setImageResource(result);
                Toast.makeText(getApplicationContext(), "testThread", Toast.LENGTH_LONG).show();
            }
        }.execute();


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxBus.getInstance().register(this);
        Bundle bundle = new Bundle();
        bundle.putLong("cur_tid", Thread.currentThread().getId());
        RxBus.getInstance().sendEvent(new Event().setTo(this.getClass().getName()).setData(bundle).setScheduler(BaseTask.SchedulerType.UI), 0, 2, TimeUnit.SECONDS);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.getInstance().unregister(this);
    }

    @SuppressWarnings("unused")
    @SubscriberHandMethod
    public void executeEvent(Event evt) {
        long evtLong = evt.getData().getLong("cur_tid");
        Log.e("TestActivity", evtLong + "-" +Thread.currentThread().getId());
    }
}
