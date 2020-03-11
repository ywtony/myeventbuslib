package com.yw.eventbuslib;

/**
 * 用于存储subscriber和SubscriberMethod
 * create by yangwei
 * on 2020-03-11 15:10
 */
public class Subscription {
    private Object subscriber;
    private SubscriberMethod subscriberMethod;

    public Subscription() {
    }

    public Subscription(Object subscriber, SubscriberMethod subscriberMethod) {
        this.subscriber = subscriber;
        this.subscriberMethod = subscriberMethod;
    }

    public Object getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(Object subscriber) {
        this.subscriber = subscriber;
    }

    public SubscriberMethod getSubscriberMethod() {
        return subscriberMethod;
    }

    public void setSubscriberMethod(SubscriberMethod subscriberMethod) {
        this.subscriberMethod = subscriberMethod;
    }
}
