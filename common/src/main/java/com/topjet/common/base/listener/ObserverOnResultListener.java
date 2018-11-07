package com.topjet.common.base.listener;

/**
 * 只返回结果集
 * 错误返回交给默认实现ObserverListenerUtils实现
 */

public interface ObserverOnResultListener<T> {
    void onResult(T t);
}
