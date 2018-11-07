package com.topjet.common.base.net.serverAPI;

import com.topjet.common.base.listener.ObserverOnNextListener;
import com.topjet.common.base.listener.ObserverOnResultListener;
import com.topjet.common.base.net.RetrofitUtil;
import com.topjet.common.base.net.request.CommonParams;
import com.topjet.common.base.net.response.BaseResponse;
import com.topjet.common.base.progress.BackgroundObserver;
import com.topjet.common.base.view.activity.IView;
import com.topjet.common.base.view.activity.MvpActivity;

import io.reactivex.Observable;

/**
 * creator: zhulunjun
 * time:    2017/9/5
 * describe: 请求的基类
 * 共有操作放到这里
 * <p>
 * 网络请求代码执行顺序
 * - ProxyHandler invoke
 * - BaseCommand handleOnNextObserver
 * - RetrofitUtil getObservable
 * - RetrofitUtil handleResult
 * - ObservableTransformer apply
 * - ProxyHandler invoke apply
 * - MyGsonResponseBodyConverter convert
 * - BackgroundObserver do onNext
 */

public abstract class BaseCommand<T> {

    public RetrofitUtil retrofitUtil;
    public T mApiService;
    public MvpActivity mActivity;
    public IView mView;
    protected CommonParams mCommonParams;

    /**
     * 需要MvpActivity来显示loading，绑定请求生命周期
     */
    public BaseCommand(Class<T> c, MvpActivity mActivity) {
        initApi(c);
        this.mActivity = mActivity;
    }


    /**
     * 不需要mvpActivity来显示loading，绑定请求生命周期
     */
    public BaseCommand(Class<T> c) {
        initApi(c);
    }


    /**
     * 初始化api相关操作
     */
    private void initApi(Class<T> c) {
        retrofitUtil = RetrofitUtil.getInstance();
        // 对ApiService添加接口代理，在代理中处理Session
        mApiService = retrofitUtil.getProxy(c);
    }


    /*========================================对外提供的方法========================================*/


    /**
     * 处理ApiService返回的Observeble，并绑定观察者（只回调onResult）
     *
     * @param observable    接口返回的被观察者
     * @param listener      最终的回调处理对象，只回调onResult
     * @param isShowLoading 是否显示网络加载弹窗
     * @param <V>           返回体中data实体类
     */
    public <V> void handleOnResultObserver(Observable<BaseResponse<V>> observable, ObserverOnResultListener<V> listener,
                                           boolean isShowLoading) {
        // 显示loading弹窗
        if (isShowLoading) {
            mActivity.showLoadingDialog();
        }
        retrofitUtil
                .getObservable(observable, mActivity)
                .subscribe(new BackgroundObserver<>(listener, mActivity, isShowLoading));
    }

    /**
     * 处理ApiService返回的Observeble，并绑定观察者（只回调onResult，带网络弹窗）
     */
    public <V> void handleOnResultObserver(Observable<BaseResponse<V>> observable,
                                           ObserverOnResultListener<V> listener) {
        handleOnResultObserver(observable, listener, true);
    }

    /**
     * 处理ApiService返回的Observeble，并绑定观察者（回调onNext 和 onResult）
     *
     * @param listener 最终的回调处理对象,回调onNext 和 onResult
     */
    public <V> void handleOnNextObserver(Observable<BaseResponse<V>> observable,
                                         ObserverOnNextListener<V> listener, boolean isShowLoading) {
        // 显示loading弹窗
        if (isShowLoading) {
            mActivity.showLoadingDialog();
        }
        retrofitUtil
                .getObservable(observable, mActivity)
                .subscribe(new BackgroundObserver<>(listener, mActivity, isShowLoading));
    }

    /**
     * 处理ApiService返回的Observeble，并绑定观察者（回调onNext 和 onResult，带网络弹窗）
     */
    public <V> void handleOnNextObserver(Observable<BaseResponse<V>> observable,
                                         ObserverOnNextListener<V> listener) {
        handleOnNextObserver(observable, listener, true);

    }

    /**
     * 处理ApiService返回的Observeble，并绑定观察者
     * 不需要 mvpActivity 对象,不需要绑定生命周期和显示loading弹窗
     * 回调有onnext 和 onResult
     *
     * @param observable 接口返回对象
     * @param listener   回调对象
     * @param <V>        返回类型
     */
    public <V> void handleOnNextObserverNotActivity(Observable<BaseResponse<V>> observable,
                                                    ObserverOnNextListener<V> listener) {
        retrofitUtil
                .getObservable(observable)
                .subscribe(new BackgroundObserver<>(listener));
    }

    /**
     * 不需要 mvpActivity 对象,不需要绑定生命周期和显示loading弹窗
     * 抽离通用的观察者处理逻辑
     * 回调只有onResult
     *
     * @param observable 接口返回对象
     * @param listener   回调对象
     * @param <V>        返回类型
     */
    public <V> void handleOnResultObserverNotActivity(Observable<BaseResponse<V>> observable,
                                                      ObserverOnResultListener<V> listener) {
        retrofitUtil
                .getObservable(observable)
                .subscribe(new BackgroundObserver<>(listener));
    }

    /**
     * 获取请求体
     *
     * @param destination 目标请求
     * @param params      请求参数
     * @param <C>         参数泛型
     * @return 返回需要的请求参数对象
     */
    public <C> CommonParams<C> getParams(String destination, C params) {
        CommonParams<C> commonParams = new CommonParams<>(destination);
        commonParams.setParameter(params);
        return commonParams;
    }

    /**
     * 获取请求体
     * 只需要destination的接口
     *
     * @param destination 目标请求
     * @param <C>         参数泛型
     * @return 返回需要的请求参数对象
     */
    public <C> CommonParams<C> getParams(String destination) {
        return new CommonParams<>(destination);
    }
}
