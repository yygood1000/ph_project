package com.topjet.common.base.listener;

/**
 * @author nanchen
 * @fileName RetrofitRxDemoo
 * @packageName com.nanchen.retrofitrxdemoo
 * @date 2016/12/12  14:48
 */

public interface ObserverOnNextListener<T> {
    void onNext(T t);

    void onError(String errorCode, String msg);
}
