/*
 * Copyright (C) 2016 david.wei (lighters)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.topjet.common.base.net.proxy;

import android.text.TextUtils;
import android.util.Log;

import com.topjet.common.base.listener.ObserverOnNextListener;
import com.topjet.common.base.listener.ObserverOnResultListener;
import com.topjet.common.base.model.Session;
import com.topjet.common.base.net.RetrofitUtil;
import com.topjet.common.base.net.exception.ApiException;
import com.topjet.common.base.net.exception.KeyInvalidException;
import com.topjet.common.base.net.exception.RetryException;
import com.topjet.common.base.net.exception.TokenInvalidException;
import com.topjet.common.base.net.request.CommonParams;
import com.topjet.common.base.net.response.BaseResponse;
import com.topjet.common.common.modle.bean.KeyBean;
import com.topjet.common.common.modle.serverAPI.SystemCommand;
import com.topjet.common.common.modle.serverAPI.SystemCommandAPI;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.config.CPersisData;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.RxHelper;
import com.topjet.common.utils.SPUtils;
import com.topjet.common.utils.StringUtils;
import com.topjet.common.utils.Toaster;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.SocketTimeoutException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import retrofit2.http.Body;

/**
 * Created by david on 16/8/21.
 * Email: huangdiv5@gmail.com
 * GitHub: https://github.com/alighters
 */
public class ProxyHandler implements InvocationHandler {

    private final static String TAG = "Token_Proxy";

    private final static String SESSION_ID = "session_id";

    private final static int REFRESH_TOKEN_VALID_TIME = 2345;
    private static long tokenChangedTime = 0;
    private Throwable mRefreshTokenError = null;
    private boolean mIsTokenNeedRefresh;

    private Object mProxyObject;

    public ProxyHandler(Object proxyObject) {
        mProxyObject = proxyObject;
    }

    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
        return Observable
                .just("guapi")
                .flatMap(new Function<Object, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(Object o) throws Exception {
                        try {
                            if (mIsTokenNeedRefresh) {
                                updateMethodToken(method, args);
                            }
                            return (Observable<?>) method.invoke(mProxyObject, args);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return (Observable<?>) method.invoke(mProxyObject, args);
                    }
                })
                .retryWhen(new Function<Observable<? extends Throwable>, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(final Observable<? extends Throwable> observable) throws
                            Exception {
                        return observable.flatMap(new Function<Throwable, Observable<?>>() {
                            @Override
                            public Observable<?> apply(Throwable throwable) throws Exception {
                                if (throwable instanceof TokenInvalidException) {
                                    Logger.d("加密 session");
                                    return refreshTokenWhenTokenInvalid();
                                } else if (throwable instanceof KeyInvalidException) {
                                    // 这里重新获取key
                                    return getKey();
                                } else if(throwable instanceof RetryException){
                                    return Observable.just(true);
                                }
                                return Observable.error(throwable);
                            }
                        });
                    }
                });
    }

    /**
     * Refresh the token when the current token is invalid.
     * 此处代码工程师 -----> 电话：17612109209 如有不明白的请联系
     *
     * @return Observable
     */
    private Observable<?> refreshTokenWhenTokenInvalid() {
        Log.e("OkHttp", "refreshTokenWhenTokenInvalid  " + CMemoryData.isSessionInvalid());
        if (!CMemoryData.isSessionInvalid()) {
            CMemoryData.setIsSessionInvalid(true);
            synchronized (ProxyHandler.class) {
                Log.e("OkHttp", "开始准备刷新session, time = " + tokenChangedTime + "      " + CMemoryData.getSessionId());
                // Have refreshed the token successfully in the valid time.
                if (new Date().getTime() - tokenChangedTime < REFRESH_TOKEN_VALID_TIME) {
                    Log.e("OkHttp", "tokenChangedTime <<<<<<<<<<<<<<< REFRESH_TOKEN_VALID_TIME");
                    mIsTokenNeedRefresh = true;
                    CMemoryData.setIsSessionInvalid(false);
                    return Observable.just(true);
                } else {
                    Log.e("OkHttp", "tokenChangedTime >>>>>>>>>>>>>>>>> REFRESH_TOKEN_VALID_TIME");
//                 call the refresh token api.
                    CommonParams commonParams = new CommonParams(SystemCommandAPI.GET_SESSION);
                    commonParams.setMobile(CMemoryData.getUserMobile());
                    Observable<BaseResponse<Session>> observable = RetrofitUtil
                            .getInstance()
                            .getmRetrofit()
                            .create(SystemCommandAPI.class)
                            .getSession(commonParams);

                    Observable<Session> ob2 = observable
                            .compose(RxHelper.<BaseResponse<Session>>rxSchedulerHelper())//切换线程
                            .compose(RetrofitUtil.<Session>handleResult());    //从BaseResponse中得到具体的实例

                    ob2.subscribe(new Observer<Session>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Session session) {
                            CMemoryData.setIsSessionInvalid(false);
                            mIsTokenNeedRefresh = true;
                            tokenChangedTime = new Date().getTime();
                            CMemoryData.setSessionId(session.getSession_id());
                            SPUtils.putString("sessionId", session.getSession_id());
                            Log.e("OkHttp", "刷新Session成功, time = " + tokenChangedTime + "      " + session
                                    .getSession_id());
                        }

                        @Override
                        public void onError(Throwable e) {
                            mRefreshTokenError = e;
                            CMemoryData.setIsSessionInvalid(false);
                        }

                        @Override
                        public void onComplete() {
                            CMemoryData.setIsSessionInvalid(false);
                        }
                    });
                    if (mRefreshTokenError != null) {
                        return Observable.error(mRefreshTokenError);
                    } else {
                        return Observable.just(true);
                    }
                }
            }
        }
        return Observable.just(true);
    }

    /**
     * Update the token of the args in the method.
     * 若是 POST 请求，或者使用 Body ，自行替换。因为 参数数组已经知道，进行遍历找到相应的值，进行替换即可（更新为新的 token 值）。
     */
    private void updateMethodToken(Method method, Object[] args) {
        Log.e("OkHttp", "updateMethodToken");
        if (mIsTokenNeedRefresh && !TextUtils.isEmpty(CMemoryData.getSessionId())) {
            Annotation[][] annotationsArray = method.getParameterAnnotations();
            Annotation[] annotations;
            if (annotationsArray != null && annotationsArray.length > 0) {
                for (int i = 0; i < annotationsArray.length; i++) {
                    annotations = annotationsArray[i];
                    for (Annotation annotation : annotations) {
                        if (annotation instanceof Body) {
                            ((CommonParams) args[i]).setSessionId(CMemoryData.getSessionId());
                        }
                    }
                }
            }
            mIsTokenNeedRefresh = false;
        }
    }

    /**
     * 获取session
     *
     * @return 延迟一秒
     */
    private Observable<?> getSession() {
        // 获取session之前需要判断是否有手机号，如果没有手机号，无法获取session
        if(StringUtils.isEmpty(CPersisData.getUserMobile())){
            return Observable.error(new ApiException("","请求失败，请重新请求"));
        }
        new SystemCommand().getSession(new ObserverOnNextListener<Session>() {
            @Override
            public void onNext(Session session) {
                Logger.d("数据加密 获取到sessionid  " + session.getSession_id());
                CMemoryData.setSessionId(session.getSession_id());
                CPersisData.setUserSession(session.getSession_id());
                isGetKey = false;
                getKey();
            }

            @Override
            public void onError(String errorCode, String msg) {
                isGetKey = false;
                Logger.d("数据加密 获取session报错 " + errorCode + " " + msg);
            }
        });
        return Observable.timer(2000,
                TimeUnit.MILLISECONDS);
    }

    /**
     * 获取key需要session
     * 如果session为空，则先获取session
     *
     * @return 延迟一秒
     */
    private boolean isGetKey = false; // 是否在获取key

    private Observable<?> getKey() {
        if (!TextUtils.isEmpty(CPersisData.getKey())) {
            return Observable.just(true);
        }
        if (!isGetKey) {
            isGetKey = true;
            if (TextUtils.isEmpty(CMemoryData.getSessionId())) {
                return getSession();
            } else {
                new SystemCommand().getKey(new ObserverOnNextListener<KeyBean>() {
                    @Override
                    public void onNext(KeyBean keyBean) {
                        Logger.d("数据加密 获取key " + keyBean.getAes_key()+" "+keyBean.getAes_ivcode());
                        CPersisData.setKey(keyBean.getAes_key());
                        CPersisData.setKeyIvCode(keyBean.getAes_ivcode());
                        isGetKey = false;
                    }

                    @Override
                    public void onError(String errorCode, String msg) {
                        Logger.d("数据加密 获取key报错 " + errorCode + " " + msg);
                        isGetKey = false;
                    }
                });
            }
        }
        return Observable.timer(2000,
                TimeUnit.MILLISECONDS);
    }
}