package com.topjet.common.base.net;

import com.topjet.common.App;
import com.topjet.common.utils.SystemUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * creator: zhulunjun
 * time:    2018/3/20
 * describe: 添加自定义请求头
 */

public class MyAddHeadInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        // 添加请求头
        Request newRequest = request.newBuilder()
                .addHeader("topjet-version", SystemUtils.getVersionName(App.getInstance()))
                .build();

        return chain.proceed(newRequest);
    }
}
