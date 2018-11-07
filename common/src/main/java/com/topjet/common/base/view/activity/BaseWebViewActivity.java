package com.topjet.common.base.view.activity;


import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.foamtrace.photopicker.ImageModel;
import com.foamtrace.photopicker.PhotoPickerActivity;
import com.topjet.common.R;
import com.topjet.common.base.presenter.BasePresenter;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.order_detail.modle.bean.ImageModelH5;
import com.topjet.common.utils.CameraUtil;
import com.topjet.common.utils.DataFormatFactory;
import com.topjet.common.utils.ImageUtil;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.StringUtils;
import com.topjet.common.widget.MyWebView;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 有WebView的Activity 基类。基于MVP模式开发
 */
public abstract class BaseWebViewActivity<T extends BasePresenter> extends MvpActivity<T> {
    public MyWebView mWebView;
    private String webUrl;
    private View viewStatusbar;

    private boolean isHideTitleBar = true;
    private OnLoadFinishListener loadFinishListener;

    public String isShowCloseDialog = "0";// 是否显示关闭页面弹窗 0不弹1弹

    private Disposable mDisposable;

    /**
     * 页面加载完成回调
     */
    public interface OnLoadFinishListener {
        void onLoadFinish();
    }

    /**
     * 设置加载完成回调
     */
    public void setLoadFinishListener(OnLoadFinishListener loadFinishListener) {
        this.loadFinishListener = loadFinishListener;
    }

    /**
     * 该方法需要是在initView之前调用才生效
     *
     * @param hideTitleBar 是否隐藏标题栏，如果标题栏有H5编写则需要隐藏
     */
    public void setHideTitleBar(boolean hideTitleBar) {
        isHideTitleBar = hideTitleBar;
    }

    public void setIsShowCloseDialog(String isShowCloseDialog) {
        this.isShowCloseDialog = isShowCloseDialog;
    }

    public boolean getIsShowCloseDialog() {
        return isShowCloseDialog.equals("1");
    }

    @Override
    protected void initView() {
        LinearLayout llWebViewParent = (LinearLayout) findViewById(R.id.ll_webview_parent);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mWebView = new MyWebView(mContext);
        mWebView.setLayoutParams(params);
        llWebViewParent.addView(mWebView);

        // 对标题栏进行隐藏，同时会隐藏状态栏，所以需要去获取一个状态栏填充隐藏部分
        if (isHideTitleBar) {
            viewStatusbar = findViewById(R.id.view_statusbar);
            if (CMemoryData.isDriver()) {
                viewStatusbar.setBackgroundResource(R.drawable.shape_bg_gradient_driver);
            } else {
                viewStatusbar.setBackgroundResource(R.drawable.shape_bg_gradient_shipper);
            }
        }

        // 加载结束回调
        mWebView.setOnLoadFinishListener(new MyWebView.OnLoadFinishListener() {
            @Override
            public void onLoadFinish() {
                Logger.i("oye", "show statusBar");
                if (isHideTitleBar) {
                    viewStatusbar.setVisibility(View.VISIBLE);
                    getMyTitleBar().hideTitleBar();
                }
                if (loadFinishListener != null) {
                    loadFinishListener.onLoadFinish();
                }
            }
        });
        Logger.i("oye", "loadUrl："+getUrl());
        // 加载页面
        if (StringUtils.isNotBlank(getUrl())){
            mWebView.loadUrl(webUrl = getUrl());
        }
    }

    /**
     * 设置加载的URL
     */
    public abstract String getUrl();

    /**
     * 获取MyWebView
     */
    public MyWebView getMyWebView() {
        return mWebView;
    }

    /**
     * 相册选择返回
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CameraUtil.REQUEST_CODE.PHOTO_PICKER) {
                final ArrayList<ImageModel> selectedImgList = (ArrayList<ImageModel>) data.getSerializableExtra
                        (PhotoPickerActivity.EXTRA_RESULT);

                showLoadingDialog();
                Observable.just(1)
                        .subscribeOn(Schedulers.newThread())
                        .map(new Function<Integer, ArrayList>() {
                            @Override
                            public ArrayList apply(@NonNull Integer integer) throws Exception {
                                ArrayList<ImageModelH5> photoListH5 = new ArrayList<>();
                                for (ImageModel i : selectedImgList) {
                                    photoListH5.add(new ImageModelH5(
                                            ImageUtil.getBase64String(i.getPath(), 720, 1280, 512)));
                                }
                                return photoListH5;
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(@NonNull Disposable disposable) throws Exception {
                                mDisposable = disposable;
                            }
                        })
                        .subscribe(new Consumer<ArrayList>() {
                            @Override
                            public void accept(@NonNull ArrayList a) throws Exception {
                                cancelShowLoadingDialog();
                                String json = DataFormatFactory.toJson(a);
                                mWebView.loadUrl("javascript:returnPhoto(" + json + ")");
                                mDisposable.dispose();
                            }
                        });
            }
        }
    }

    /**
     * 销毁WebView  防止内存泄漏
     */
    @Override
    protected void onDestroy() {
        if (mWebView != null) {
            mWebView.clearHistory();
            mWebView.stopLoading();
            mWebView.removeAllViews();
            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            if (mWebView.getUrl().equals(webUrl)) {
                mWebView.loadUrl("javascript:returnPhoto()");
                super.onBackPressed();
            } else {
                mWebView.goBack();
            }
        } else {
            mWebView.loadUrl("javascript:returnPhoto()");
            super.onBackPressed();
        }
    }

}
