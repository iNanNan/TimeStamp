package com.base.thread;

import android.os.Handler;
import android.os.Looper;

import com.base.thread.plugins.RxAndroidPlugins;

import rx.Scheduler;

/**
 * Created by heng on 16-3-23.
 */
public class AndroidSchedulers {

    private AndroidSchedulers() {
        throw new AssertionError("No instances");
    }

    // See https://github.com/ReactiveX/RxAndroid/issues/238
    // https://en.wikipedia.org/wiki/Initialization-on-demand_holder_idiom
    private static class MainThreadSchedulerHolder {
        static final Scheduler MAIN_THREAD_SCHEDULER =
                new HandlerScheduler(new Handler(Looper.getMainLooper()));
    }

    /** A {@link Scheduler} which executes actions on the Android UI thread. */
    public static Scheduler ui() {
        Scheduler scheduler =
                RxAndroidPlugins.getInstance().getSchedulersHook().getMainThreadScheduler();
        return scheduler != null ? scheduler : MainThreadSchedulerHolder.MAIN_THREAD_SCHEDULER;
    }
}
