package com.base.thread;


import rx.Observable;
import rx.Scheduler;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by heng on 16-3-21.
 */
public abstract class BaseThread<P, R>{

    private P mParams;

    protected Scheduler mScheduler;

    public P getParams() {
        return mParams;
    }

    public void setParams(P mParams) {
        this.mParams = mParams;
    }

    public BaseThread() {
        this.mScheduler = SchedulerType.getScheduler(SchedulerType.NEW);
    }

    public BaseThread(SchedulerType schedulerType) {
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
        doTask().subscribe(new Action1<R>() {
            @Override
            public void call(R result) {
                onSuccess(result);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                onException(throwable);
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
        TEST;

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
                default:
                    scheduler = Schedulers.newThread();
                    break;
            }

            return scheduler;
        }
    }
}
