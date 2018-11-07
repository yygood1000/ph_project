package com.topjet.common.common.view.activity;


import android.app.Activity;
import android.content.Intent;

import com.foamtrace.photopicker.ImageModel;
import com.foamtrace.photopicker.PhotoPickerActivity;
import com.topjet.common.R;
import com.topjet.common.base.dialog.AutoDialog;
import com.topjet.common.base.dialog.AutoDialogHelper;
import com.topjet.common.base.view.activity.BaseWebViewActivity;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.common.modle.extra.CommentExtra;
import com.topjet.common.common.modle.js_interface.CommentJSInterface;
import com.topjet.common.common.presenter.CommentPresenter;
import com.topjet.common.order_detail.modle.bean.ImageModelH5;
import com.topjet.common.utils.CameraUtil;
import com.topjet.common.utils.DataFormatFactory;
import com.topjet.common.utils.ImageUtil;
import com.topjet.common.utils.Logger;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * creator: yy
 * <p>
 * 提交评价页面
 */

public class CommentActivity extends BaseWebViewActivity<CommentPresenter> implements CommentView {
    private Disposable mDisposable;
    private AutoDialog autoDialog;

    @Override
    protected void initPresenter() {
        mPresenter = new CommentPresenter(this, mContext);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_common_webview;
    }

    @Override
    public String getUrl() {
        Logger.i("oye", "url == " + mPresenter.getUrl());
        return mPresenter.getUrl();
    }

    @Override
    protected void initView() {
        super.initView();
        // 设置H5 交互接口
        mWebView.addJavascriptInterface(new CommentJSInterface(this, mWebView), "App");
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
                                    photoListH5.add(new ImageModelH5(i.getPath(),
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
     * 跳转提交评价页面
     */
    public static void turnToCommentActivity(MvpActivity mActivity, String orderId, String orderVersion, String
            commentedUserId, String commentedUserName) {
        CommentExtra extra = new CommentExtra(orderId, orderVersion, commentedUserId, commentedUserName);
        mActivity.turnToActivity(CommentActivity.class, extra);
    }

    /**
     * 跳转查看回评页面
     */
    public static void turnToCheckCommentActivity(MvpActivity mActivity, String orderId, String orderVersion) {
        CommentExtra extra = new CommentExtra(orderId, orderVersion);
        mActivity.turnToActivity(CommentActivity.class, extra);
    }

    /**
     * 跳转查看自身评论列表页面
     */
    public static void turnToCommentListSelfActivity(MvpActivity mActivity) {
        CommentExtra extra = new CommentExtra();
        mActivity.turnToActivity(CommentActivity.class, extra);
    }

    /**
     * 跳转查看他人评论列表页面
     */
    public static void turnToCommentListOthersActivity(MvpActivity mActivity, String userId) {
        CommentExtra extra = new CommentExtra(userId);
        mActivity.turnToActivity(CommentActivity.class, extra);
    }

    @Override
    public void onBackPressed() {
        if (getIsShowCloseDialog()) {
            autoDialog = AutoDialogHelper.showContent(mContext, "未发布的评价内容不会被保存\n是否退出？", "退出评价", "取消",
                    new AutoDialogHelper.OnConfirmListener() {
                        @Override
                        public void onClick() {
                            finish();
                        }
                    },null);
            autoDialog.show();
        } else {
            super.onBackPressed();
        }
    }
}
