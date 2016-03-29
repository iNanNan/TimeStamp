package com.base.message;

import android.os.Bundle;

import com.base.thread.BaseTask;

/**
 * Created by heng on 16-3-28.
 */
public class BaseEvent<FROM, TO> {

    private FROM from;

    private TO to;

    private Bundle data;

    private BaseTask.SchedulerType scheduler;

    public BaseEvent setFrom(FROM from) {
        this.from = from;
        return this;
    }

    public BaseEvent setTo(TO to) {
        this.to = to;
        return this;
    }

    public BaseEvent setData(Bundle data) {
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

    public BaseEvent setScheduler(BaseTask.SchedulerType scheduler) {
        this.scheduler = scheduler;
        return this;
    }

    public BaseTask.SchedulerType getScheduler() {
        return scheduler;
    }
}
