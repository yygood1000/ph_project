package com.topjet.common.jpush;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.topjet.common.R;
import com.topjet.common.base.dialog.AutoDialog;
import com.topjet.common.base.dialog.AutoDialogHelper;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.common.presenter.TransferPresenter;
import com.topjet.common.config.CPersisData;
import com.topjet.common.utils.AppManager;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.StringUtils;

/**
 * creator: zhulunjun
 * time:    2017/11/23
 * describe: 处理极光推送的逻辑
 * 1. 如果app运行在前台，则显示推送弹窗
 * 2. 如果在后台运行，则显示通知
 */

public class JPushHelper {

    private static JPushHelper instance = null;

    public synchronized static JPushHelper getInstance() {
        if (instance == null) {
            instance = new JPushHelper();
        }
        return instance;
    }

    /**
     * 如果app运行在前台，则显示推送弹窗
     * 如果在后台运行，则显示通知
     *
     * @param activity 上下文
     * @param bean     推送的数据
     */
    void handleData(final Activity activity, final JPushBean bean) {
        AutoDialog dialog = null;
        if (bean.getButton() == null) {
            Logger.d("JPushHelper 前台 getButton 空");
            ((MvpActivity) activity).finishPage();
            return;
        }
        if (bean.getButton().size() == 1) { // 一个按钮
            Logger.d("JPushHelper 前台 一个按钮");
            dialog = onBtnDialog(activity, bean);
        } else if (bean.getButton().size() == 2) { // 两个按钮
            Logger.d("JPushHelper 前台 两个按钮");
            dialog = towBtnDialog(activity, bean);
        }
        if (dialog != null) {
            dialog.setCanceledOnTouchOutside(false);
            // 弹窗关闭，页面也关闭
//            final MvpActivity mvpActivity = (MvpActivity) activity;
//            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//                @Override
//                public void onDismiss(DialogInterface dialog) {
//                    mvpActivity.finishPage();
//                }
//            });
            dialog.toggleShow();
        } else {
            Logger.d("JPushHelper 前台 dialog == null");
        }

    }

    /**
     * 一个按钮 弹窗
     */
    public AutoDialog onBtnDialog(final Activity activity, final JPushBean bean) {
        return AutoDialogHelper.showContentOneBtn(activity,
                bean.getText(),
                bean.getButton().get(0).getText(),
                new AutoDialogHelper.OnConfirmListener() {
                    @Override
                    public void onClick() {
                        jumpUri(activity, bean.getButton().get(0).getAction());
                    }
                });
    }

    /**
     * 两个按钮 弹窗
     */
    public AutoDialog towBtnDialog(final Activity activity, final JPushBean bean) {
        return AutoDialogHelper.showContent(activity,
                bean.getText(),
                bean.getButton().get(0).getText(),
                bean.getButton().get(1).getText(),
                new AutoDialogHelper.OnConfirmListener() {
                    @Override
                    public void onClick() {
                        Logger.d("JPushHelper按钮 " + bean.getButton().toString());
                        jumpUri(bean.getButton().get(0).getAction());
                    }
                }, new AutoDialogHelper.OnConfirmListener() {
                    @Override
                    public void onClick() {
                        Logger.d("JPushHelper按钮 " + bean.getButton().toString() + "  " + bean.getButton().get(0)
                                .getAction() + " " + bean.getButton().get(1).getAction());
                        jumpUri(bean.getButton().get(1).getAction());
                    }
                });


    }

    /**
     * 根据推送传递的uri跳转页面
     *
     * @param activity 上下文
     * @param uri      推送传递的action
     */
    public void jumpUri(Activity activity, String uri) {
        Logger.d("oye", "jumpUri uri跳转 " + uri);
        // 没有登陆就不跳转
        if (CPersisData.getIsLogin() && activity != null && StringUtils.isNotBlank(uri)) {
            Logger.d("oye", "jump");
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            activity.startActivity(intent);
        }
    }

    /**
     * 直接处理uri 不去main页面在处理
     *
     * @param uri 数据
     */
    public void jumpUri(String uri) {
        Logger.d("uri跳转 " + uri);
        // 没有登陆就不跳转
        if (CPersisData.getIsLogin() && StringUtils.isNotBlank(uri)) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            // 直接解析intent跳转页面，不经过main页面跳转，就不会黑屏
            new TransferPresenter(AppManager.getInstance().getTopActivity()).distributePage(intent);
        }
    }

    /**
     * 显示通知
     *
     * @param context 上下文
     * @param bean    数据
     */
    public void getNotification(final Context context, final JPushBean bean) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(bean.getAction()));//将要跳转的界面
        builder.setAutoCancel(true);//点击后消失
//        builder.setLargeIcon()
        builder.setSmallIcon(R.drawable.ic_launcher);//设置通知栏消息标题的头像
        builder.setDefaults(NotificationCompat.DEFAULT_SOUND);//设置通知铃声
        builder.setTicker(bean.getText());
        builder.setWhen(System.currentTimeMillis());//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
        builder.setContentText(bean.getText());//通知内容
        if (StringUtils.isNotBlank(bean.getTitle())) {
            builder.setContentTitle(bean.getTitle());
        }
        builder.setPriority(Notification.PRIORITY_DEFAULT); //设置该通知优先级
        //利用PendingIntent来包装我们的intent对象,使其延迟跳转
        PendingIntent intentPend = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(intentPend);
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }
}
