package com.topjet.common.base.progress;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.topjet.common.R;
import com.topjet.common.base.listener.ObserverOnNextListener;
import com.topjet.common.base.listener.ObserverOnResultListener;
import com.topjet.common.base.listener.ProgressCancelListener;
import com.topjet.common.base.net.exception.ApiException;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.common.modle.bean.MaintainBean;
import com.topjet.common.common.view.dialog.MaintainDialog;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.config.CPersisData;
import com.topjet.common.config.ErrorConstant;
import com.topjet.common.config.RespectiveData;
import com.topjet.common.user.modle.bean.UserAccountsBeBanned;
import com.topjet.common.user.view.activity.LoginActivity;
import com.topjet.common.utils.AppManager;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.ResourceUtils;
import com.topjet.common.utils.StringUtils;
import com.topjet.common.utils.Toaster;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;

/**
 * @author nanchen
 * @fileName RetrofitRxDemoo
 * @packageName com.nanchen.retrofitrxdemoo
 * @date 2016/12/12  14:48
 */

public class BackgroundObserver<T> implements ProgressCancelListener, Observer<T> {
    private ObserverOnNextListener<T> mListener; // 监听请求

    public MvpActivity mActivity; // 显示弹窗的对象
    public boolean isShowLoading = true;


    public BackgroundObserver(ObserverOnNextListener<T> listener, MvpActivity activity) {
        this.mActivity = activity;
        this.mListener = getListener(listener);
    }

    public BackgroundObserver(ObserverOnResultListener<T> listener, MvpActivity activity) {
        this.mActivity = activity;
        this.mListener = getListener(listener);
    }

    public BackgroundObserver(ObserverOnNextListener<T> listener, MvpActivity activity, boolean isShowLoading) {
        this.mActivity = activity;
        this.mListener = getListener(listener);
        this.isShowLoading = isShowLoading;
    }


    public BackgroundObserver(ObserverOnResultListener<T> listener, MvpActivity activity, boolean isShowLoading) {
        this.mActivity = activity;
        this.mListener = getListener(listener);
        this.isShowLoading = isShowLoading;
    }

    public BackgroundObserver(ObserverOnNextListener<T> listener) {
        this.mListener = getListener(listener);
    }

    public BackgroundObserver(ObserverOnResultListener<T> listener) {
        this.mListener = getListener(listener);
    }


    /**
     * 当错误提示为默认提示时使用getListener
     * OnResult
     *
     * @param listener 监听
     * @return
     */
    public ObserverOnNextListener<T> getListener(final ObserverOnResultListener<T> listener) {
        return new ObserverOnNextListener<T>() {
            @Override
            public void onNext(T t) {
                if (isShowLoading) {
                    cancelShowLoadingDialog();
                }
                listener.onResult(t);
            }

            @Override
            public void onError(String errorCode, String msg) {
                if (isShowLoading) {
                    cancelShowLoadingDialog();
                }
                if(StringUtils.isNotBlank(msg)) {
                    Toaster.showShortToast(msg);
                }
            }
        };
    }

    /**
     * 对网络请求做同一弹窗处理
     * OnNext
     *
     * @param listener
     * @return
     */
    public ObserverOnNextListener<T> getListener(final ObserverOnNextListener<T> listener) {
        return new ObserverOnNextListener<T>() {
            @Override
            public void onNext(T t) {
                if (isShowLoading) {
                    cancelShowLoadingDialog();
                }
                listener.onNext(t);
            }

            @Override
            public void onError(String errorCode, String msg) {
                if (isShowLoading) {
                    cancelShowLoadingDialog();
                }
                listener.onError(errorCode, msg);
            }
        };
    }

    /**
     * 关闭loading弹窗
     */
    private void cancelShowLoadingDialog() {
        if (mActivity != null) {
            mActivity.cancelShowLoadingDialog();
        }
    }


    @Override
    public void onComplete() {

    }

    @Override
    public void onSubscribe(Disposable d) {
    }

    /**
     * 对网络请求底层抛出的一些异常做统一处理（SocketTimeoutException，ConnectException）
     * 对自定义异常做特殊处理（ApiException）
     * 对异常进行下层分发（mListener.onError()）
     */
    @Override
    public void onError(Throwable e) {
        if (mActivity != null) {
            Logger.d("okhttp 错误页面" + mActivity.getClass().toString());
        }
        if (e instanceof SocketTimeoutException || e instanceof ConnectException || e instanceof UnknownHostException) {
            mListener.onError(ErrorConstant.APP_ERROR_TIMEOUT, ResourceUtils.getString(R.string
                    .request_service_timeout));
        } else if (e instanceof ApiException) {
            String errorCode = ((ApiException) e).getErrorCode();
            // 对自定义异常做特殊处理
            if (errorCode.equals(ErrorConstant.E_107) || errorCode.equals(ErrorConstant.E_USER_19)) {
                if (System.currentTimeMillis() - CMemoryData.getStartLoginPageTime() >= 2 * 1000) {
                    CMemoryData.setStartLoginPageTime(System.currentTimeMillis());

                    Activity activity = AppManager.getInstance().currentActivity();
                    activity.startActivity(new Intent(activity, RespectiveData.getLoginActivityClass()));
                    if(StringUtils.isNotBlank(((ApiException) e).getErrorMsg())) {
                        Toaster.showLongToast(((ApiException) e).getErrorMsg());
                    }
                }
            } else if (errorCode.equals(ErrorConstant.E_109)) {  // 倒霉催的，帐号被冻结
                if (System.currentTimeMillis() - CMemoryData.getStartLoginPageTime() >= 2 * 1000) {
                    CPersisData.setIsLogin(false);
                    CMemoryData.setStartLoginPageTime(System.currentTimeMillis());

                    String msg = ((ApiException) e).getErrorMsg();
                    UserAccountsBeBanned userAccountsBeBanned = new Gson().fromJson(msg, UserAccountsBeBanned.class);
                    Activity activity = AppManager.getInstance().currentActivity();
                    Intent intent = new Intent(activity, RespectiveData.getLoginActivityClass());
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(LoginActivity.ACCOUNTS_BE_BANNED_INFO, userAccountsBeBanned);
                    intent.putExtras(bundle);
                    activity.startActivity(intent);
                }
            } else if (errorCode.equals(ErrorConstant.E_111)) {     // 停机维护
                if (!CMemoryData.isShowMaintainBeanDialog()) {
                    CMemoryData.setShowMaintainBeanDialog(true);

                    String msg = ((ApiException) e).getErrorMsg();
                    MaintainBean maintainBean = new Gson().fromJson(msg, MaintainBean.class);
                    if (maintainBean == null) {
                        return;
                    }
                    if (mActivity != null) {
                        new MaintainDialog(mActivity, maintainBean).show();
                    } else {
                        if (!CPersisData.getIsBackground()) {
                            new MaintainDialog(AppManager.getInstance().getTopActivity(), maintainBean).show();
                        }
                    }
                }
            } else if (errorCode.equals(ErrorConstant.E_ORDER_15) && mActivity.getClass().toString().contains
                    ("OrderDetailActivity")) {
                Toaster.showLongToast("很可惜，该订单已成交/撤销");
                // 订单撤销关闭页面, 订单详情页才生效
                if (mActivity != null) {
                    mActivity.finishPage();
                }
            } else {
                mListener.onError(((ApiException) e).getErrorCode(), ((ApiException) e).getErrorMsg());
            }

        } else if (e instanceof HttpException) {
            mListener.onError(ErrorConstant.APP_ERROR_HTTPEXCEPTION, ResourceUtils.getString(R.string
                    .request_service_error));
        } else {
            if(StringUtils.isNotBlank(e.getMessage())) {
                Toaster.showLongToast(e.getMessage());
            }
            mListener.onError(ErrorConstant.APP_ERROR_FAIL, e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onNext(T t) {
        if (mListener != null) {
            mListener.onNext(t);
        }
    }

    @Override
    public void onCancelProgress() {

    }
}
