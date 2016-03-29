package com.mobilephonesensor.test;

import android.util.Log;

import com.base.thread.BaseTask;
import com.mobilephonesensor.R;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by heng on 16-3-24.
 */
public class TestThread extends BaseTask<String, Integer> {

    @Override
    protected Observable doTask() {
        return Observable.just("test")
                .map(new Func1<String, Integer>() {
                    @Override
                    public Integer call(String params) {
                        Log.e("doTask", "map1=" + params);
                        return 0;
                    }
                })
                .map(new Func1<Integer, Integer>() {
                         @Override
                         public Integer call(Integer o) {
                             long c = System.currentTimeMillis();
                             try {
                                 Thread.sleep(5000);
                             } catch (Exception e) {

                             }
                             Log.e("doTask", "map2=" + (System.currentTimeMillis() - c));
                             return R.mipmap.ic_menu_emoticons;
                         }
                     }

                );
    }

    @Override
    protected void onException(Throwable e) {
        super.onException(e);
        e.printStackTrace();
    }
}
