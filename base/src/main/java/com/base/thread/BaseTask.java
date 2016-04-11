package com.base.thread;

import rx.Observable;
import rx.Scheduler;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Action1;

/**
 * Created by heng on 16-3-21.
 */
public abstract class BaseTask<P, R>{

    private Subscription mSubscription;

    private P mParams;

    protected Scheduler mScheduler;

    public P getParams() {
        return mParams;
    }

    public void setParams(P mParams) {
        this.mParams = mParams;
    }

    public BaseTask() {
        this(SchedulerType.NEW);
    }

    public BaseTask(SchedulerType schedulerType) {
        this.mScheduler = schedulerType.get();
    }

    /** 添加Observable操作符如：map,filter */
    protected abstract Observable doTask();

    protected void onPrepare() {
    }

    protected void onFinally() {
    }

    protected void onSuccess(R result) {
    }

    protected void onException(Throwable e) {
    }

    protected void onDestroy() {
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
    }

    public void destroy() {
        onDestroy();
    }

    public void execute() {
        onPrepare();
        mSubscription  = doTask()
                .subscribeOn(mScheduler)
                .observeOn(AndroidSchedulers.ui())
                .subscribe(new Action1<R>() {
            @Override
            public void call(R result) {
                onSuccess(result);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                onException(throwable);
                throwable.printStackTrace();
            }
        }, new Action0() {
            @Override
            public void call() {
                onFinally();
            }
        });
    }

}
