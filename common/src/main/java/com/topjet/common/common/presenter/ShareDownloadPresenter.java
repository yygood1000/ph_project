package com.topjet.common.common.presenter;

import android.content.Context;
import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.topjet.common.base.listener.ObserverOnResultListener;
import com.topjet.common.base.presenter.BaseApiPresenter;
import com.topjet.common.common.modle.response.ShareResponse;
import com.topjet.common.common.modle.serverAPI.SystemCommand;
import com.topjet.common.common.view.activity.ShareDownloadView;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.ShareHelper;
import com.topjet.common.utils.StringUtils;

import java.util.Hashtable;

import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * creator: zhulunjun
 * time:    2017/10/23
 * describe: 分享下载
 */

public class ShareDownloadPresenter extends BaseApiPresenter<ShareDownloadView, SystemCommand> {
    public ShareDownloadPresenter(ShareDownloadView mView, Context mContext, SystemCommand mApiCommand) {
        super(mView, mContext, mApiCommand);
    }

    private String url;
    /**
     * 二维码分享
     *
     */
    public void shareAppOfQrCode() {
        mApiCommand.shareAppOfQrcode(new ObserverOnResultListener<ShareResponse>() {
            @Override
            public void onResult(ShareResponse shareResponse) {
                if (shareResponse != null && StringUtils.isNotBlank(shareResponse.getShare_url())) {
                    createQRImage(shareResponse.getShare_url());
                    url = shareResponse.getShare_url();
                }
            }
        });
    }

    private int QR_WIDTH = 500;
    private int QR_HEIGHT = 500;

    /**
     * 创建生成二维码
     *
     * @param url
     */
    public void createQRImage(String url) {
        try {
            //判断URL合法性
            if (url == null || "".equals(url) || url.length() < 1) {
                return;
            }
            Logger.d("TTT", "二维码生成分享url::" + url);
            Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            //设置空白边距的宽度
            hints.put(EncodeHintType.MARGIN, 1);
            //图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix = new QRCodeWriter().encode(url, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
            int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
            //下面这里按照二维码的算法，逐个生成二维码的图片，
            //两个for循环是图片横列扫描的结果
            for (int y = 0; y < QR_HEIGHT; y++) {
                for (int x = 0; x < QR_WIDTH; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * QR_WIDTH + x] = 0xff000000;
                    } else {
                        pixels[y * QR_WIDTH + x] = 0xffffffff;
                    }
                }
            }
            //生成二维码图片的格式，使用ARGB_8888
            Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
            //显示到一个ImageView上面
            mView.setQrCode(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    /**
     * 检查url
     * @return url是否为空
     */
    private boolean checkUrl(){
        if(StringUtils.isEmpty(url)){
            shareAppOfQrCode();
            return false;
        }
        return true;
    }

    /**
     * 微信分享
     */
    public void shareWechat(){
        if(checkUrl()) {
            ShareHelper.shareWithUrl(mActivity, Wechat.NAME, url, onShareComplete);
        }
    }
    /**
     * 朋友圈分享
     */
    public void shareWechatCircle(){
        if(checkUrl()) {
            ShareHelper.shareWithUrl(mActivity, WechatMoments.NAME, url, onShareComplete);
        }
    }
//    /**
//     * 短信分享
//     */
//    public void shareSms(){
//        // 跳转联系人
//        mView.turnToActivity(ContactActivity.class, new ContactExtra(ContactExtra.SHARE));
//
//    }

    /**
     * 短信分享
     */
    public void shareSms() {
        new SystemCommand(mActivity).shareAppOfSms(new ObserverOnResultListener<ShareResponse>() {
            @Override
            public void onResult(ShareResponse shareResponse) {
                if (shareResponse != null && StringUtils.isNotBlank(shareResponse.getSms_content())) {
                    ShareHelper.sendSmsWithBody(mActivity, "", shareResponse.getSms_content());
                }
            }
        });
    }

    /**
     * 分享成功的回调
     */
    ShareHelper.OnShareComplete onShareComplete = new ShareHelper.OnShareComplete() {
        @Override
        public void shareComple(String s) {

        }

        @Override
        public void shareError() {

        }
    };
}
