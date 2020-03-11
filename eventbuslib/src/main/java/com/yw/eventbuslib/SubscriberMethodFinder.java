package com.yw.eventbuslib;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用于查找订阅者Class对象中的用subscriber注解标注的方法，并取出方法中的各个属性，如：ThreadMode、Method、eventTypeClass
 * create by yangwei
 * on 2020-03-11 15:10
 */
public class SubscriberMethodFinder {
    private static final int BRIDGE = 0x40;
    private static final int SYNTHETIC = 0x1000;
    private static final int MODIFIERS_IGNORE = Modifier.ABSTRACT | Modifier.STATIC | BRIDGE | SYNTHETIC;
    //定义一个缓存提升查询效率
    private static final Map<Class<?>, List<SubscriberMethod>> METHOD_CACHE = new ConcurrentHashMap<>();

    /**
     * 从clazz中取出用subscriber注解标注的方法集合
     *
     * @param subscriberClass
     * @return
     */
    public static List<SubscriberMethod> findSubscriberMethods(Class<?> subscriberClass) {
        List<SubscriberMethod> subscriberMethods = METHOD_CACHE.get(subscriberClass);
        if (subscriberMethods != null) {
            return subscriberMethods;
        }

        return getMethods(subscriberClass);
    }

    private static List<SubscriberMethod> getMethods(Class<?> subscriberClass) {
        List<SubscriberMethod> subscriberMethods = new ArrayList<>();
        Method[] methods = null;
        try {
            //利用反射获取subscriber的方法集合
            methods = subscriberClass.getDeclaredMethods();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (Method method : methods) {
            int modifiers = method.getModifiers();
            //过滤public标注的方法
            if ((modifiers & Modifier.PUBLIC) != 0 && (modifiers & MODIFIERS_IGNORE) == 0) {
                Class<?>[] parameterTypes = method.getParameterTypes();//获取参数类型
                //找出只有一个参数的方法
                if (parameterTypes.length == 1) {
                    //找出有subscribe注解的方法
                    Subscribe subscribeAnnotation = method.getAnnotation(Subscribe.class);
                    if (subscribeAnnotation != null) {
                        //获取事件类型，由于只有一个参数所以parameterTypes取第一个就行了
                        Class<?> eventType = parameterTypes[0];
                        //获取线程模型，这个模型用于标注，用户自定义的事件是在主线程中执行还是在子线程中执行
                        ThreadMode threadMode = subscribeAnnotation.threadMode();
                        //把订阅者的方法存入subscriberMethod，并把SubscriberMethod存入方法集合中
                        subscriberMethods.add(new SubscriberMethod(threadMode, method, eventType));
                    }
                }
            }
        }
        return subscriberMethods;
    }
}
