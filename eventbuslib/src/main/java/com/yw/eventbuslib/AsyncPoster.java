package com.yw.eventbuslib;

/**
 * 在线程池中运行
 * create by yangwei
 * on 2020-03-11 15:11
 */
public class AsyncPoster implements Runnable, Poster {
    private EventBus eventBus;
    private PosterBean posterBean;

    public AsyncPoster(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public void run() {
        if (posterBean != null) {
            eventBus.invokeSubscriber(posterBean.getSubscription(), posterBean.getEventObj());
        }
    }

    @Override
    public void enqueue(Subscription subscription, Object eventObj) {
        posterBean = new PosterBean(subscription, eventObj);
        eventBus.getExecutorService().execute(this);
    }
}
