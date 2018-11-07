package com.topjet.common.common.view.activity;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.topjet.common.R;
import com.topjet.common.R2;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.common.modle.serverAPI.SystemCommand;
import com.topjet.common.common.presenter.ShareDownloadPresenter;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * creator: zhulunjun
 * time:    2017/10/20
 * describe: 分享下载页面
 */

public class ShareDownloadActivity extends MvpActivity<ShareDownloadPresenter> implements ShareDownloadView {
    @BindView(R2.id.iv_qr)
    ImageView ivQr;


    @Override
    protected void initPresenter() {
        mPresenter = new ShareDownloadPresenter(this,this,new SystemCommand(this));
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_share_download;
    }

    @Override
    protected void initTitle() {
        super.initTitle();
        getMyTitleBar().setTitleText(getString(R.string.share_download));
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter.shareAppOfQrCode();
    }

    @Override
    public void setQrCode(Bitmap bitmap) {
        ivQr.setImageBitmap(bitmap);
    }

    /**
     * 微信分享
     */
    @OnClick(R2.id.iv_wechat)
    public void wechatClick(){
        mPresenter.shareWechat();
    }

    /**
     * 朋友圈分享
     */
    @OnClick(R2.id.iv_wechat_circle)
    public void wechatCircleClick(){
        mPresenter.shareWechatCircle();
    }

    /**
     * 短信分享
     */
    @OnClick(R2.id.iv_sms)
    public void smsClick(){
        mPresenter.shareSms();
    }




}
