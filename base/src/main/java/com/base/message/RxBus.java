package com.base.message;

import com.base.thread.BaseTask;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

import rx.Scheduler;
import rx.functions.Action0;

/**
 * Created by heng on 16-3-28.
 */
public class RxBus {

    private static ConcurrentHashMap<Object, Scheduler.Worker> allSubscriberWorks = null;

    private static ConcurrentHashMap<Object, Object> subscriberMap = null;

    private static ConcurrentLinkedQueue<Event> untreatedEvents = null;

    private static RxBus instance = null;

    public static RxBus getInstance() {
        if (instance == null) {
            synchronized (RxBus.class) {
                if (instance == null) {
                    instance = new RxBus();
                }
            }
        }
        return instance;
    }

    private RxBus() {
        subscriberMap = new ConcurrentHashMap<>();
        untreatedEvents = new ConcurrentLinkedQueue<>();
        allSubscriberWorks = new ConcurrentHashMap<>();
    }

    public void register(Object subscriber) {
        if (subscriber != null){
            String key = subscriber.getClass().getName();
            subscriberMap.put(key, subscriber);

            if (untreatedEvents.size() == 0) {
                return;
            }

            for (Event event : untreatedEvents) {
                if (event != null && key.equals(event.getTo())) {
                    this.sendEvent(event);
                }
            }
        }
    }

    public void unregister(Object subscriber) {
        if (subscriber != null) {
            String key = subscriber.getClass().getName();
            if (subscriberMap.containsKey(key)) {
                subscriberMap.remove(key);
                if (allSubscriberWorks.containsKey(key)) {
                    Scheduler.Worker worker = allSubscriberWorks.get(key);
                    if (worker != null && !worker.isUnsubscribed()) {
                        worker.unsubscribe();
                    }
                    allSubscriberWorks.remove(key, worker);
                }
            } else {
                throw new IllegalStateException(subscriber.toString() + "not register by RxBus.");
            }
        }
    }

    public Object getSubscriber(Object subscriberKey) {
        if (subscriberMap != null && subscriberMap.size() != 0) {
            if (subscriberMap.containsKey(subscriberKey)) {
                return subscriberMap.get(subscriberKey);
            }
        }
        return null;
    }

    public void sendEvent(Event event) {
        this.sendEvent(event, 0, null);
    }

    public void sendEvent(Event event, long delayTime, TimeUnit timeUnit) {
        this.sendEvent(event, delayTime, 0, timeUnit);
    }

    public void sendEvent(Event event, long delayTime, long period, TimeUnit timeUnit) {
        if (delayTime < 0 || period < 0) {
            throw new IllegalArgumentException("TimeUnit can not be negative.");
        }

        if (event == null) {
            return;
        }

        Object to = event.getTo();
        if (to == null) {
            return;
        }

        untreatedEvents.offer(event);

        Scheduler scheduler = BaseTask.SchedulerType.getScheduler(event.getScheduler());
        EventAction eventAction = new EventAction(event);
        Scheduler.Worker worker = scheduler.createWorker();
        if (period == 0 && delayTime == 0 && timeUnit == null) {
            worker.schedule(eventAction);
        } else if (period == 0 && delayTime > 0 && timeUnit != null) {
            worker.schedule(eventAction, delayTime, timeUnit);
        } else if (period > 0 && delayTime >= 0 && timeUnit != null) {
            worker.schedulePeriodically(eventAction, delayTime, period, timeUnit);
        } else {
            scheduler.createWorker().schedule(eventAction);
        }

        allSubscriberWorks.put(to, worker);
    }

    public void setEmptyEvent(int what) {
        this.sendEvent(new Event(what));
    }

    private static class EventAction implements Action0 {

        private final ConcurrentLinkedQueue<Object> subscribers;

        private Event event;

        public EventAction(Event event) {
            this.subscribers = new ConcurrentLinkedQueue<>();
            this.event = event;
            if (Event.ANY.equals(event.getTo())) {
                for (Map.Entry<Object, Object> m : subscriberMap.entrySet()) {
                    this.subscribers.offer(m.getValue());
                }
            } else {
                this.subscribers.offer(RxBus.getInstance().getSubscriber(event.getTo()));
            }
        }

        @Override
        public void call() {
            if (subscribers == null || subscribers.size() == 0) {
                return;
            }

            for (Object subscriber : subscribers) {

                if (subscriber == null) {
                    continue;
                }

                try {
                    Method[] methods = subscriber.getClass().getDeclaredMethods();

                    for (Method m : methods) {
                        SubscriberHandMethod shm = m.getAnnotation(SubscriberHandMethod.class);
                        if (shm != null) {
                            Class<?>[] cArray =  m.getParameterTypes();
                            if (cArray != null
                                    && cArray.length == 1
                                    && cArray[0] == Event.class) {
                                m.setAccessible(true);
                                m.invoke(subscriber, event);
                                if (untreatedEvents.contains(event)) {
                                    untreatedEvents.remove(event);
                                }
                            }
                        }
                    }

                } catch (Exception e){
                    e.printStackTrace();
                }
            }

        }

    }
}
