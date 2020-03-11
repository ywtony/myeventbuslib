package com.yw.eventbuslib;

/**
 * 定义一个异步回调的接口
 * 其实现类是：AsyncPoster、BackgroundPoster、MainThreadHandlerPoster
 * create by yangwei
 * on 2020-03-11 15:03
 */
public interface Poster {
    void enqueue(Subscription subscription,Object eventObj);
}
