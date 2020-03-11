package com.yw.eventbuslib;

import java.util.ArrayList;
import java.util.List;

/**
 * 当前发送事件所在线程的线程状态
 * create by yangwei
 * on 2020-03-11 15:46
 */
public class PostingThreadState {
    //是否是主线程
    private boolean isMainThread;
    //消息队列
    private List<Object> eventQueue = new ArrayList<>();
    //subscription，存储subscriber和subscriberMethod对象
    private Subscription subscription;
    //事件对象
    private Object event;

    public boolean isMainThread() {
        return isMainThread;
    }

    public void setMainThread(boolean mainThread) {
        isMainThread = mainThread;
    }

    public List<Object> getEventQueue() {
        return eventQueue;
    }

    public void setEventQueue(List<Object> eventQueue) {
        this.eventQueue = eventQueue;
    }

    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    public Object getEvent() {
        return event;
    }

    public void setEvent(Object event) {
        this.event = event;
    }
}
