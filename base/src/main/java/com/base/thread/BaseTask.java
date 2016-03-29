package com.base.thread;


import rx.Observable;
import rx.Scheduler;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by heng on 16-3-21.
 */
public abstract class BaseTask<P, R>{

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
        this.mScheduler = SchedulerType.getScheduler(schedulerType);
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
    }

    public void destroy() {
        onDestroy();
    }

    public void execute() {
        onPrepare();
        doTask().subscribeOn(mScheduler)
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

    public enum SchedulerType {
        /**
         * {@link Schedulers}
         * Creates and returns a Scheduler intended for computational work.
         */
        COMPUTATION,
        /** Creates and returns a Scheduler that executes work immediately on the current thread. */
        IMMEDIATE,
        /** Creates and returns a Scheduler intended for IO-bound work. */
        IO,
        /** Creates and returns a Scheduler that creates a new Thread for each unit of work. */
        NEW,
        /** Creates and returns a Scheduler that queues work on the current thread to be executed after the current work completes. */
        TRAMPOLINE,
        /** Creates and returns a TestScheduler, which is useful for debugging. */
        TEST,
        /** Creates and returns a android ui scheduler */
        UI;

        public static Scheduler getScheduler(SchedulerType schedulerType) {

            Scheduler scheduler;

            switch (schedulerType) {
                case COMPUTATION:
                    scheduler = Schedulers.computation();
                    break;
                case IMMEDIATE:
                    scheduler = Schedulers.immediate();
                    break;
                case IO:
                    scheduler = Schedulers.io();
                    break;
                case NEW:
                    scheduler = Schedulers.newThread();
                    break;
                case TRAMPOLINE:
                    scheduler = Schedulers.trampoline();
                    break;
                case TEST:
                    scheduler = Schedulers.test();
                    break;
                case UI:
                    scheduler = AndroidSchedulers.ui();
                    break;
                default:
                    scheduler = Schedulers.immediate();
                    break;
            }

            return scheduler;
        }
    }
}
