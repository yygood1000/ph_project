package com.topjet.common.base.net.response;

import java.io.Serializable;

/**
 * 服务端返回的内层子数据基类
 * Created by tsj004 on 2017/8/1.
 */

public class Result<T> implements Serializable{
    protected Error error;
    public Error getError() {
        return error;
    }

    protected T data;
    public T getData() {
        return data;
    }

    @Override
    public String toString() {
        return "Result{" +
                "error='" + error + '\'' +
                "data='" + data + '\'' +
                '}';
    }
}
