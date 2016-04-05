package com.base.message;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.base.thread.BaseTask;

/**
 * Created by heng on 16-3-28.
 */
public final class Event implements Parcelable {

    public final static String ANY = "-any-";

    public int what = -1;

    public Object obj = null;

    private String from;

    private String to;

    private Bundle data;

    private BaseTask.SchedulerType scheduler;

    public Event(){}

    /** 创建空消息，发送给所有，并且指定在ui线程 */
    public Event(int what) {
        this.to = ANY;
        this.what = what;
        this.scheduler = BaseTask.SchedulerType.UI;
    }

    public Event(String to, int what) {
        this.to = to;
        this.what = what;
    }

    public Event setFrom(String from) {
        this.from = from;
        return this;
    }

    public Event setTo(String to) {
        this.to = to;
        return this;
    }

    public Event setData(Bundle data) {
        this.data = data;
        return this;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
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
        return setTo(ANY);
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(what);
        dest.writeBundle(data);
        writeObj(dest, flags, to);
        writeObj(dest, flags, obj);
        writeObj(dest, flags, from);
    }

    private void writeObj(Parcel dest, int flags, Object obj) {
        if (obj != null) {
            try {
                Parcelable p = (Parcelable)obj;
                dest.writeInt(1);
                dest.writeParcelable(p, flags);
            } catch (ClassCastException e) {
                throw new RuntimeException(
                        "Can't marshal non-Parcelable objects across processes.");
            }
        }
    }

    private Event (Parcel in) {
        what = in.readInt();
        data = in.readBundle();
        if (obj != null) {
            obj = in.readParcelable(getClass().getClassLoader());
        }
        if (from != null) {
            from = in.readParcelable(getClass().getClassLoader());
        }
        to = in.readParcelable(getClass().getClassLoader());
    }
}
