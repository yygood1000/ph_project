package com.topjet.common.base.model;

import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.foamtrace.photopicker.ImageModel;
import com.topjet.common.base.dialog.AutoDialogHelper;
import com.topjet.common.base.view.activity.BaseWebViewActivity;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.order_detail.modle.bean.H5CommonParams;
import com.topjet.common.utils.CallPhoneUtils;
import com.topjet.common.utils.CameraUtil;
import com.topjet.common.utils.DataFormatFactory;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.StringUtils;

import java.util.ArrayList;

/**
 * Created by yy on 2017/10/13.
 * <p>
 * H5 交互接口基类
 */

public class BaseJsInterface {
    public MvpActivity mActivity;
    public WebView mWebView;

    public BaseJsInterface(MvpActivity mActivity, WebView webView) {
        this.mActivity = mActivity;
        this.mWebView = webView;
    }

    /**
     * 用于H5获取请求外层参数
     */
    @JavascriptInterface
    public String obtainRequestParameter() {
        Logger.d("oye ============"+DataFormatFactory.toJson(new H5CommonParams()));
        return DataFormatFactory.toJson(new H5CommonParams());
    }

    /**
     * 关闭页面
     */
    @JavascriptInterface
    public void closePage() {
        mActivity.finish();
    }

    /**
     * 显示加载弹窗
     */
    @JavascriptInterface
    public void showProgress() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mActivity.showLoadingDialog();
            }
        });
    }

    /**
     * 取消加载弹窗
     */
    @JavascriptInterface
    public void cancelProgress() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mActivity.cancelShowLoadingDialog();
            }
        });
    }

    /**
     * 拨打电话（弹出框）
     */
    @JavascriptInterface
    public void callMobile(final String[] telphone) {
        Logger.i("TTT", "callMobile:" + "telphone[0]:" + telphone[0] + "----telphone[1]:" + telphone[1]);
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new CallPhoneUtils().showCallDialogNotUpload(mActivity, "是否拨打电话", telphone[0]);
            }
        });
    }

    /**
     * 拨打客服
     */
    @JavascriptInterface
    public void callCustomerService() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new CallPhoneUtils().callCustomerService(mActivity);
            }
        });
    }

    /**
     * 显示弹窗
     *
     * @param content  内容
     * @param leftBtn  左按钮
     * @param rightBtn 右按钮
     */
    @JavascriptInterface
    public void showDialog(final String content, final String leftBtn, final String rightBtn) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AutoDialogHelper.showContent(mActivity, content, leftBtn, rightBtn,
                        new AutoDialogHelper.OnConfirmListener() {
                            @Override
                            public void onClick() {
                                loadUrl("javascript:leftBtn()");
                            }
                        }, new AutoDialogHelper.OnConfirmListener() {
                            @Override
                            public void onClick() {
                                loadUrl("javascript:rightBtn()");
                            }
                        }).show();
            }
        });
    }


    /**
     * 跳转相册选择页面，带有已选择图片的path集合，最多选择4张
     */
    @JavascriptInterface
    public void addPhotoLimit(final String json) {
        Logger.i("oye", "json ==" + json);
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ArrayList<ImageModel> list = new ArrayList<>();
                if (StringUtils.isNotBlank(json)) {
                    list = DataFormatFactory.jsonToArrayList(json, ImageModel.class);
                }
                CameraUtil.defaultTurnToAlbum(mActivity, 4, list);// 相册
            }
        });
    }

    /**
     * 用于H5获取请求外层参数
     */
    @JavascriptInterface
    public void addPhoto(final String number) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CameraUtil.defaultTurnToAlbum(mActivity, Integer.parseInt(number), new ArrayList<ImageModel>());// 相册
            }
        });
    }

    /**
     * 调用js的方法
     */
    public void loadUrl(final String url) {
        if (mWebView != null) {
            mWebView.post(new Runnable() {
                @Override
                public void run() {
                    mWebView.loadUrl(url);
                }
            });
        }
    }

    /**
     * 跳转相册选择页面，带有已选择图片的path集合，最多选择4张
     */
    @JavascriptInterface
    public void setIsShowCloseDialog(String flag) {
        Logger.i("oye", "setIsShowCloseDialog ==" + flag);
        ((BaseWebViewActivity) mActivity).setIsShowCloseDialog(flag);
    }


}
