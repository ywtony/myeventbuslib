package com.yw.eventbuslib;

import android.os.Looper;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.annotation.Nullable;

/**
 * 定义EventBus
 * create by yangwei
 * on 2020-03-11 14:58
 */
public class EventBus {
    private static EventBus eventBus = null;

    public static EventBus getDefault() {
        synchronized (EventBus.class) {
            if (eventBus == null) {
                eventBus = new EventBus();
            }
        }
        return eventBus;
    }

    private AsyncPoster asyncPoster;
    private BackgroundPoster backgroundPoster;
    private MainThreadHandlerPoster mainThreadHandlerPoster;
    //线程池
    private ExecutorService executorService;
    //key=事件类型，value=subscription
    private HashMap<Class<?>, CopyOnWriteArrayList<Subscription>> subscriptionsByEventType;
    //key=subscriber，value=eventTypeClass
    private HashMap<Object, List<Class<?>>> typesBySubscriber;
    private ThreadLocal<PostingThreadState> currentPostingThreadState = new ThreadLocal<PostingThreadState>() {
        @Nullable
        @Override
        protected PostingThreadState initialValue() {
            return new PostingThreadState();
        }
    };

    public ExecutorService getExecutorService() {
        return executorService;
    }


    /**
     * 初始化一些参数
     */
    private EventBus() {
        asyncPoster = new AsyncPoster(this);
        backgroundPoster = new BackgroundPoster(this);
        mainThreadHandlerPoster = new MainThreadHandlerPoster(this, Looper.getMainLooper());
        executorService = Executors.newCachedThreadPool();
        subscriptionsByEventType = new HashMap<>();
        typesBySubscriber = new HashMap<>();
    }


    /**
     * 注册
     * 订阅方法
     *
     * @param subscriber
     */
    public void register(Object subscriber) {
        Class clazz = subscriber.getClass();
        List<SubscriberMethod> subscriberMethods = SubscriberMethodFinder.findSubscriberMethods(clazz);
        if (subscriberMethods != null && subscriberMethods.size() > 0) {
            for (SubscriberMethod subscriberMethod : subscriberMethods) {
                subscribe(subscriber, subscriberMethod);
            }
        }
    }

    /**
     * 真正的订阅
     *
     * @param subscriber
     * @param subscriberMethod
     */
    private void subscribe(Object subscriber, SubscriberMethod subscriberMethod) {
        Class eventType = subscriberMethod.getEventType();
        Subscription newSubscription = new Subscription(subscriber, subscriberMethod);
        CopyOnWriteArrayList<Subscription> subscriptions = subscriptionsByEventType.get(eventType);
        if (subscriptions == null) {
            subscriptions = new CopyOnWriteArrayList<>();
            subscriptionsByEventType.put(eventType, subscriptions);
        }
        int size = subscriptions.size();
        for (int i = 0; i <= size; i++) {
            if (i == size) {
                //向订阅集合中加入订阅
                subscriptions.add(i, newSubscription);
                break;
            }
        }

        List<Class<?>> subscribedEvents = typesBySubscriber.get(subscriber);
        if (subscribedEvents == null) {
            subscribedEvents = new ArrayList<>();
            typesBySubscriber.put(subscriber, subscribedEvents);
        }
        subscribedEvents.add(eventType);

    }

    /**
     * 发送事件方法
     *
     * @param eventObj
     */
    public void post(Object eventObj) {
        PostingThreadState postingThreadState = currentPostingThreadState.get();
        List<Object> queue = postingThreadState.getEventQueue();
        queue.add(eventObj);
        postingThreadState.setMainThread(isMainThread());
        //缺少一个发送事件的线程
        Class<?> clazz = eventObj.getClass();
        List<Subscription> subscriptions = subscriptionsByEventType.get(clazz);
        if (subscriptions != null) {
            //循环遍历Subscriptions，
            for (Subscription subscription : subscriptions) {
                //分发事件
                postToSubscription(subscription, eventObj, postingThreadState);

            }
        }

    }

    /**
     * 返回true为主线程，否则为子线程
     *
     * @return
     */
    private boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    /**
     * 对事件进行分发
     *
     * @param subscription
     * @param eventTypeObj
     */
    private void postToSubscription(Subscription subscription, Object eventTypeObj, PostingThreadState postingThreadState) {
        switch (subscription.getSubscriberMethod().getThreadMode()) {
            case POSITION:
                invokeSubscriber(subscription, eventTypeObj);
                break;
            case MAIN:
                if (isMainThread()) {
                    invokeSubscriber(subscription, eventTypeObj);
                } else {
                    mainThreadHandlerPoster.enqueue(subscription, eventTypeObj);
                }
                break;
            case MAIN_ORDERD:
                if (mainThreadHandlerPoster != null) {
                    mainThreadHandlerPoster.enqueue(subscription, eventTypeObj);
                } else {
                    invokeSubscriber(subscription, eventTypeObj);
                }
                break;
            case BACKGROUND:
                if (backgroundPoster != null) {
                    backgroundPoster.enqueue(subscription, eventTypeObj);
                } else {
                    invokeSubscriber(subscription, eventTypeObj);
                }
                break;
            case ASYNC:
                asyncPoster.enqueue(subscription, eventTypeObj);
                break;
        }
    }

    /**
     * 执行事件的具体方法
     *
     * @param subscription
     * @param eventTypeObj
     */
    public void invokeSubscriber(Subscription subscription, Object eventTypeObj) {
        try {
            subscription.getSubscriberMethod().getMethod().setAccessible(true);
            subscription.getSubscriberMethod().getMethod().invoke(subscription.getSubscriber(), eventTypeObj);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 取消注册方法
     * 取消注册其实就清除两个 集合就行了，一个是Subscriptions集合，一个是typesBySubscriber集合
     *
     * @param subscriber
     */
    public void unRegister(Object subscriber) {
        List<Class<?>> eventTypes = typesBySubscriber.get(subscriber);
        if (eventTypes != null) {
            for (Class<?> clazz : eventTypes) {
                CopyOnWriteArrayList<Subscription> subscriptions = subscriptionsByEventType.get(clazz);
                for (Subscription subscription : subscriptions) {
                    if (subscriber == subscription.getSubscriber()) {
                        subscriptions.remove(subscription);
                    }
                }
            }
            typesBySubscriber.remove(subscriber);
        }

    }
}
