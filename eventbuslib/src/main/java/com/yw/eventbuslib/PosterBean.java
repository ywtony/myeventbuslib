package com.yw.eventbuslib;

/**
 * create by yangwei
 * on 2020-03-11 16:04
 */
public class PosterBean {
    private Subscription subscription;
    private Object eventObj;

    public PosterBean() {
    }

    public PosterBean(Subscription subscription, Object eventObj) {
        this.subscription = subscription;
        this.eventObj = eventObj;
    }

    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    public Object getEventObj() {
        return eventObj;
    }

    public void setEventObj(Object eventObj) {
        this.eventObj = eventObj;
    }
}
