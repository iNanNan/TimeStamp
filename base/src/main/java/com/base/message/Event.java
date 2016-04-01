package com.base.message;

import android.os.Bundle;

import com.base.thread.BaseTask;

/**
 * Created by heng on 16-3-28.
 */
public final class Event<FROM, TO> {

    public final static String ANY = "-any-";

    public int what = -1;

    public Object obj = null;

    private FROM from;

    private TO to;

    private Bundle data;

    private BaseTask.SchedulerType scheduler;

    public Event(){}

    /** 创建空消息，发送给所有，并且指定在ui线程 */
    public Event(int what) {
        this.to = (TO) ANY;
        this.what = what;
        this.scheduler = BaseTask.SchedulerType.UI;
    }

    public Event setFrom(FROM from) {
        this.from = from;
        return this;
    }

    public Event setTo(TO to) {
        this.to = to;
        return this;
    }

    public Event setData(Bundle data) {
        this.data = data;
        return this;
    }

    public FROM getFrom() {
        return from;
    }

    public TO getTo() {
        return to;
    }

    public Bundle getData() {
        return data;
    }

    public Event setScheduler(BaseTask.SchedulerType scheduler) {
        this.scheduler = scheduler;
        return this;
    }

    public BaseTask.SchedulerType getScheduler() {
        return scheduler;
    }

    public Event setToAny() {
        return setTo((TO) ANY);
    }
}
