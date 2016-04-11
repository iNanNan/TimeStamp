package com.base.thread;

import rx.Scheduler;
import rx.schedulers.Schedulers;

/**
 * Created by heng on 16-4-11.
 */
public enum SchedulerType {

    /**
     * {@link Schedulers}
     * Creates and returns a Scheduler intended for computational work.
     */
    COMPUTATION {
        public Scheduler get() {
            return Schedulers.computation();
        }
    },
    /** Creates and returns a Scheduler that executes work immediately on the current thread. */
    IMMEDIATE {
        public Scheduler get() {
            return Schedulers.immediate();
        }
    },
    /** Creates and returns a Scheduler intended for IO-bound work. */
    IO {
        public Scheduler get() {
            return Schedulers.io();
        }
    },
    /** Creates and returns a Scheduler that creates a new Thread for each unit of work. */
    NEW {
        public Scheduler get() {
            return Schedulers.newThread();
        }
    },
    /** Creates and returns a Scheduler that queues work on the current thread to be executed after the current work completes. */
    TRAMPOLINE {
        public Scheduler get() {
            return Schedulers.trampoline();
        }
    },
    /** Creates and returns a TestScheduler, which is useful for debugging. */
    TEST {
        public Scheduler get() {
            return Schedulers.test();
        }
    },
    /** Creates and returns a android ui scheduler */
    UI {
        public Scheduler get() {
            return AndroidSchedulers.ui();
        }
    };

    public abstract Scheduler get();
}
