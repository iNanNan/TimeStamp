package com.base.message;

import com.base.thread.BaseTask;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import rx.Scheduler;
import rx.functions.Action0;

/**
 * Created by heng on 16-3-28.
 */
public class RxBus {

    private ConcurrentHashMap<Object, Object> subscriberMap = null;

    private ConcurrentLinkedQueue<BaseEvent> evtQueue = null;

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

    public void register(Object subscriber) {
        if (subscriber == null)
            return;
        String key = subscriber.getClass().getName();
        subscriberMap.put(key, subscriber);
    }

    public void unregister(Object subscriber) {
        if (subscriber != null) {
            String key = subscriber.getClass().getName();
            subscriberMap.remove(key);
        }
    }

    public Object getEventListener(Object subscriberKey) {
        if (subscriberMap != null && subscriberMap.size() != 0) {
            return subscriberMap.get(subscriberKey);
        }
        return null;
    }

    public void sendEvent(BaseEvent event) {
        if (event == null)
            return;
        Object to = event.getTo();
        if (to == null) {
            return;
        }
        evtQueue.offer(event);
        BaseTask.SchedulerType schedulerType = event.getScheduler();
        final Object subscriber = subscriberMap.get(to);
        if (subscriber == null) {
            return;
        }
        final BaseEvent evt = evtQueue.poll();
        if (evt == null) {
            return;
        }
        Scheduler scheduler = BaseTask.SchedulerType.getScheduler(schedulerType);
        scheduler.createWorker().schedule(new EventAction(evt));
    }

    private RxBus() {
        subscriberMap = new ConcurrentHashMap<>();
        evtQueue = new ConcurrentLinkedQueue<>();
    }

    private static class EventAction implements Action0{

        private BaseEvent event;

        private Object subscriber;

        public EventAction(BaseEvent event) {
            this.event = event;
            this.subscriber = RxBus.getInstance().getEventListener(event.getTo());
        }

        @Override
        public void call() {
            if (subscriber == null) {
                return;
            }
            try {
                Method[] methods = subscriber.getClass().getDeclaredMethods();

                for (Method m : methods) {
                    SubscriberHandMethod shm = m.getAnnotation(SubscriberHandMethod.class);
                    if(shm == null) {
                        continue;
                    }

                    Class<?>[] clsArray =  m.getParameterTypes();
                    if(clsArray == null || clsArray.length != 1){
                        continue;
                    }

                    if(clsArray[0] != BaseEvent.class){
                        continue;
                    }

                    m.setAccessible(true);
                    m.invoke(subscriber,event);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }
}
