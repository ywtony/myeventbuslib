package com.yw.eventbuslib;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * 主线程Handler，调用enqueue方法后想自己发送消息，把执行逻辑切换到主线程中执行
 * create by yangwei
 * on 2020-03-11 15:03
 */
public class MainThreadHandlerPoster extends Handler implements Poster {
    private EventBus eventBus;

    /**
     * 构造函数，把EventBus传递进来，再把主线程Looper传递进来
     *
     * @param eventBus
     * @param looper
     */
    public MainThreadHandlerPoster(EventBus eventBus, Looper looper) {
        super(looper);
        this.eventBus = eventBus;
    }


    @Override
    public void handleMessage(Message msg) {
        PosterBean posterBean = (PosterBean) msg.obj;
        eventBus.invokeSubscriber(posterBean.getSubscription(), posterBean.getEventObj());
    }

    @Override
    public void enqueue(Subscription subscription, Object eventObj) {
        PosterBean posterBean = new PosterBean(subscription, eventObj);
        Message msg = new Message();
        msg.obj = posterBean;
        sendMessage(msg);
    }
}
