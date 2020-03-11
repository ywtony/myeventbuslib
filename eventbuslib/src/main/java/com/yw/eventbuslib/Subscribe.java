package com.yw.eventbuslib;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定义Subscribe方法注解
 * create by yangwei
 * on 2020-03-11 14:51
 */
//定义注解直到运行时依然有效
@Retention(RetentionPolicy.RUNTIME)
//定义注解只能用在方法上
@Target(ElementType.METHOD)
public @interface Subscribe {
    //这是默认的线程模型，无论在哪个线程中发起事件，都在发送事件的线程中执行
    ThreadMode threadMode() default ThreadMode.POSITION;
}
