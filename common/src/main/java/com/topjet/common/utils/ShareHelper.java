package com.topjet.common.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.mob.MobSDK;
import com.topjet.common.config.CMemoryData;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;
import cn.sharesdk.system.text.ShortMessage;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;

public class ShareHelper {
    /**
     * 分享的标题
     */
    private static String title = "560交好运，配好货，一起加入560交运配货吧！";
    /**
     * 分享的内容
     */
    private static String content = "我正在使用560交运配货，安全，价优，配货快，你也下载试试：http://d.566560.com";
    /**
     * 分享图片的url
     */
    private static String imageUrl = "";
    /**
     * 分享后，点击图片、标题、内容跳转页面的链接
     */
    private static String url = "http://d.566560.com";
    /**
     * 分享的评论（暂时没有）
     */
    private static String comment = "";

    private OnShareComplete myOnShareComplete;

    public interface OnShareComplete {
        void shareComple(String s);

        void shareError();
    }

    public OnShareComplete getMyOnShareComple() {
        return myOnShareComplete;
    }

    public void setMyOnShareComple(OnShareComplete myOnShareComple) {
        this.myOnShareComplete = myOnShareComple;
    }

//    /**
//     * @param context
//     */
//    public static void share(final Activity context, String platform, final OnShareComplete sharecomplete) {
//        // 设置分享图片的路径
//        setImageUrl();
//
//        MobSDK.init(context, "MobAppkey", "MobAppsecret");
//        final OnekeyShare oks = new OnekeyShare();
//
//        // 设置是否直接分享
//        oks.setSilent(true);
//        // 设置分享途径
//        if (platform != null) {
//            oks.setPlatform(platform);
//        }
//        oks.setTheme(OnekeyShareTheme.CLASSIC);
//        oks.setDialogMode(false);
//        // 关闭sso授权
//        oks.disableSSOWhenAuthorize();
//        // 隐藏QQ分享
//        oks.addHiddenPlatform(QQ.NAME);
//        // 隐藏QQ空间分享
//        oks.addHiddenPlatform(QZone.NAME);
//        oks.addHiddenPlatform(ShortMessage.NAME);
//
//        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
//        oks.setTitle(title);
//        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
//        oks.setTitleUrl(url);
//        // text是分享文本，所有平台都需要这个字段
//        oks.setText(content);
//        // 设置分享图片的链接
//        oks.setImageUrl(imageUrl);
//        // url仅在微信（包括好友和朋友圈）中使用，url不能是apk。
//        oks.setUrl(url);
//        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
//        oks.setComment(comment);
//        // site是分享此内容的网站名称，仅在QQ空间使用
//        oks.setSite(SystemUtils.getAppName());
//        // siteUrl是分享此内容的网站地址，仅在QQ空间使用s
//        oks.setSiteUrl(url);
//
//        // 设置监听
//        oks.setCallback(new PlatformActionListener() {
//            @Override
//            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
//                Toaster.showLongToast("分享成功");
//                if (sharecomplete != null) {
//                    sharecomplete.shareComple("分享成功");
//                }
//            }
//
//            @Override
//            public void onError(Platform platform, int i, Throwable throwable) {
//                Toaster.showLongToast("微信未安装或微信版本过低，无法分享");
//                Logger.i("oye", "throwable = " + throwable.getMessage());
//                if (sharecomplete != null) {
//                    sharecomplete.shareError();
//                }
//            }
//
//            @Override
//            public void onCancel(Platform platform, int i) {
//                Toaster.showLongToast("已取消分享");
//                if (sharecomplete != null) {
//                    sharecomplete.shareError();
//                }
//            }
//        });
//        // 启动分享GUI
//        oks.show(context);
//
//    }

    /**
     * @param context
     */
    public static void shareWithUrl(final Activity context, String platform, String Url, final OnShareComplete
            sharecomple) {
        // 设置分享图片的路径
        setImageUrl();
        title = "560交好运，配好货，一起加入560交运配货吧！";
        url = Url;
        OnekeyShare oks = new OnekeyShare();

        // 设置分享途径
        if (platform != null) {
            oks.setPlatform(platform);
        }
        // 关闭sso授权
        oks.disableSSOWhenAuthorize();

        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(title);
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(url);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(content);
        // 设置分享图片的链接
        oks.setImageUrl(imageUrl);
        // url仅在微信（包括好友和朋友圈）中使用，url不能是apk。
        oks.setUrl(url);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment(comment);

        // 设置监听
        oks.setCallback(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                Toaster.showLongToast("分享成功");
                if (sharecomple != null) {
                    sharecomple.shareComple("分享成功");
                }
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                Toaster.showLongToast("微信未安装或微信版本过低，无法分享");
                Logger.i("oye", "throwable = " + throwable.getMessage());
                if (sharecomple != null) {
                    sharecomple.shareError();
                }
            }

            @Override
            public void onCancel(Platform platform, int i) {
                Toaster.showLongToast("已取消分享");
                if (sharecomple != null) {
                    sharecomple.shareError();
                }
            }
        });
        // 启动分享GUI
        oks.show(context);

    }


    /**
     * 调用系统界面，给指定的号码发送短信，并附带短信内容
     *
     * @param context
     * @param number
     * @param body
     */
    public static void sendSmsWithBody(Context context, String number, String body) {
        Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
        sendIntent.setData(Uri.parse("smsto:" + number));
        sendIntent.putExtra("sms_body", body);
        context.startActivity(sendIntent);
    }

    /**
     * 动态设置分享图片的url
     */
    private static void setImageUrl() {
        if (CMemoryData.isDriver()) {
            imageUrl = "http://www.566560.com/images/appdown/driver200.png";
        } else {
            imageUrl = "http://www.566560.com/images/appdown/shipper200.png";
        }
    }

    /**
     * 图片分享
     *
     * @param context
     */
    public static void shareImage(final Activity context, String platform, String imagePath, final OnShareComplete
            sharecomple) {
        // 设置分享图片的路径
        setImageUrl();
        final OnekeyShare oks = new OnekeyShare();

        // 设置是否直接分享
        oks.setSilent(true);
        // 设置分享途径
        if (platform != null) {
            oks.setPlatform(platform);
        }
        oks.setTheme(OnekeyShareTheme.CLASSIC);
        oks.setDialogMode(false);
        // 关闭sso授权
        oks.disableSSOWhenAuthorize();
        // 隐藏QQ分享
        oks.addHiddenPlatform(QQ.NAME);
        // 隐藏QQ空间分享
        oks.addHiddenPlatform(QZone.NAME);
        oks.addHiddenPlatform(ShortMessage.NAME);

        oks.setImagePath(imagePath);
//		// 启动分享GUI
        oks.setCallback(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                Toaster.showLongToast("分享成功");
                if (sharecomple != null) {
                    sharecomple.shareComple("分享成功");
                }
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                Toaster.showLongToast("微信未安装或微信版本过低，无法分享");
                if (sharecomple != null) {
                    sharecomple.shareError();
                }
            }

            @Override
            public void onCancel(Platform platform, int i) {
                Toaster.showLongToast("已取消分享");
                if (sharecomple != null) {
                    sharecomple.shareError();
                }
            }
        });
        oks.show(context);

    }
    /**
     * 分享图片
     * @param imagePath 图片的本地地址
     * @param shareMode 分享模式，微信好友，或者微信朋友圈
     * @param sharecomplete 回调
     */
    public static void shareImage(String imagePath, String shareMode, final OnShareComplete sharecomplete) {
        share(null, imagePath, Platform.SHARE_IMAGE, shareMode, sharecomplete);
    }

    /**
     * 分享网页
     * @param url 网页地址
     * @param shareMode 分享模式，微信好友，或者微信朋友圈
     * @param sharecomplete 回调
     */
    public static void shareUrl( String url, String shareMode, final OnShareComplete sharecomplete) {
        share(url, null, Platform.SHARE_WEBPAGE, shareMode, sharecomplete);
    }

    /**
     * 微信分享
     * @param path 网页地址
     * @param imagePath 图片的本地地址
     * @param shareType 分享类型，图片，或者网页
     * @param shareMode 分享模式，微信好友，或者微信朋友圈
     * @param sharecomplete 回调
     */
    public static void share( String path, String imagePath, int shareType, String shareMode, final OnShareComplete sharecomplete) {
        if(StringUtils.isNotBlank(url)){
            url = path;
        }
        Wechat.ShareParams sp = new Wechat.ShareParams();
        sp.setTitle(title);
        sp.setUrl(url);
        sp.setText(content);
        sp.setImageUrl(imageUrl);
        if (StringUtils.isNotBlank(imagePath)) {
            sp.setImagePath(imagePath);
        }
        sp.setShareType(shareType); //https://ws1.sinaimg.cn/large/610dc034ly1fp9qm6nv50j20u00miacg.jpg
        Platform weChat = ShareSDK.getPlatform(shareMode);
        // 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
        weChat.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                Logger.d("ShareSDK", "onComplete ---->  分享成功");
                Toaster.showLongToast("分享成功");
                if (sharecomplete != null) {
                    sharecomplete.shareComple("分享成功");
                }
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                Toaster.showLongToast("微信未安装或微信版本过低，无法分享");
                Logger.d("ShareSDK", "onError ---->  分享失败" + throwable.getStackTrace().toString());
                Logger.d("ShareSDK", "onError ---->  分享失败" + throwable.getMessage());
                throwable.getMessage();
                throwable.printStackTrace();
                if (sharecomplete != null) {
                    sharecomplete.shareError();
                }
            }

            @Override
            public void onCancel(Platform platform, int i) {
                Toaster.showLongToast("已取消分享");
                Logger.d("ShareSDK", "onCancel ---->  分享取消");
                if (sharecomplete != null) {
                    sharecomplete.shareError();
                }
            }
        });
        // 执行图文分享
        weChat.share(sp);
    }

}
