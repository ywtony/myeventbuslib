package com.yw.eventbuslib;

import java.lang.reflect.Method;

/**
 * 用于存储Method、ThreadMode、EventType(参数类型)
 * create by yangwei
 * on 2020-03-11 15:10
 */
public class SubscriberMethod {
    private ThreadMode threadMode;
    private Method method;
    private Class<?> eventType;

    public SubscriberMethod() {
    }

    public SubscriberMethod(ThreadMode threadMode, Method method, Class<?> eventType) {
        this.threadMode = threadMode;
        this.method = method;
        this.eventType = eventType;
    }

    public ThreadMode getThreadMode() {
        return threadMode;
    }

    public void setThreadMode(ThreadMode threadMode) {
        this.threadMode = threadMode;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Class<?> getEventType() {
        return eventType;
    }

    public void setEventType(Class<?> eventType) {
        this.eventType = eventType;
    }
}
