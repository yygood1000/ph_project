package com.topjet.common.utils;

import android.support.annotation.NonNull;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * creator: zhulunjun
 * time:    2017/11/23
 * describe:
 */

public class DelayUtils {

    public interface OnListener{
        void onListener();
    }

    /**
     * 延迟
     * @param time 时间
     * @param listener 回调
     */
    public static void delay(int time, final OnListener listener){
        Observable.timer(time, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(@NonNull Long aLong) throws Exception {
                        if(listener != null){
                            listener.onListener();
                        }
                    }
                });

    }
}
