package com.topjet.common.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;

import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.topjet.common.R;
import com.topjet.common.base.dialog.AutoDialog;
import com.topjet.common.base.listener.ObserverOnResultListener;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.common.view.dialog.CallDialogWithAdv;
import com.topjet.common.config.CConstants;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.config.Config;
import com.topjet.common.user.modle.params.SaveCallPhoneInfoParams;
import com.topjet.common.user.modle.serverAPI.UserCommand;
import com.topjet.common.user.modle.serverAPI.UserCommandAPI;

import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * 拨打电话对话框
 * Created by tsj004 on 2017/8/15.
 */

@SuppressWarnings("FieldCanBeLocal")
public class CallPhoneUtils {
    private TelephonyManager manager = null;
    private PhoneListener phoneListener = null;
    private MvpActivity activity = null;
    private CallPhoneSucceedListener callPhoneSucceedListener;

    //获取本次通话的时长(单位:秒)
    private long time = 0;
    //判断是否正在通话
    private boolean isCalling;
    //通话起始时间
    private String startTime = "";
    //通话结束时间
    private String endTime = "";

    private boolean isInAppCall;// 是否在APP内拨打的

    private SaveCallPhoneInfoParams params = new SaveCallPhoneInfoParams();

    /**
     * 拨打电话成功回调
     */
    @SuppressWarnings("SameParameterValue")
    public interface CallPhoneSucceedListener {
        void isSucceed(boolean isSucceed);
    }

    public CallPhoneUtils() {
    }

    /*=============================对外提供的显示电话弹窗方法============================*/

    /**
     * 拨打客服通用方法
     */
    public void callCustomerService(MvpActivity activity) {
        showCallDialogNotUpload(activity, "", "");
    }

    /**
     * 拨打电话对话框(无广告，不上传)
     */
    public void showCallDialogNotUpload(final MvpActivity activity, String name, String phoneNumber) {
        showCallDialog(activity, name, phoneNumber, "", "", false);
    }

    /**
     * 拨打电话对话框（无广告 要上传）
     */
    public void showCallDialogWithUpload(MvpActivity activity, String name, final String phoneNumber, String
            goodsId, String orderStatus, CallPhoneSucceedListener listener) {
        this.callPhoneSucceedListener = listener;
        showCallDialog(activity, name, phoneNumber, goodsId, orderStatus, true);
    }

    /**
     * 拨打电话对话框(有广告，需上传)
     */
    public void showCallDialogWithAdvUpload(MvpActivity activity, View view, String name, String phoneNumber, String
            bottomText, int flag, String goodsId, String goodsStatus, CallPhoneSucceedListener listener) {
        this.callPhoneSucceedListener = listener;
        showCallDialogWithAdv(activity, view, name, phoneNumber, bottomText, flag, goodsId, goodsStatus, true);
    }

    /**
     * 拨打电话对话框(有广告，不上传，按钮文本外部传入)
     */
    public void showCallDialogWithAdvNotUpload(MvpActivity activity, View view, String name, String phoneNumber,
                                               String bottomText, int flag) {
        showCallDialogWithAdv(activity, view, name, phoneNumber, bottomText, flag, "", "", false);
    }

    /**
     * 拨打电话对话框(有广告，不上传，按钮文本默认显示)
     */
    public void showCallDialogWithAdvNotUpload(MvpActivity activity, View view, String name, String phoneNumber,
                                               int flag) {
        String text = CMemoryData.isDriver() ? "呼叫货主" : "呼叫司机";
        showCallDialogWithAdv(activity, view, name, phoneNumber, text, flag, "", "", false);
    }

    /**
     * 拨打电话对话框(有广告，需上传，按钮文本默认显示)
     */

    public void showCallDialogWithAdvUpload(MvpActivity activity, View view, String name, String phoneNumber, int
            flag, String goodsId, String goodsStatus, CallPhoneSucceedListener listener) {
        this.callPhoneSucceedListener = listener;
        String text = CMemoryData.isDriver() ? "呼叫货主" : "呼叫司机";
        showCallDialogWithAdv(activity, view, name, phoneNumber, text, flag, goodsId, goodsStatus, true);
    }

    /*=============================对外提供的显示电话弹窗方法============================*/

    /**
     * 拨打电话对话框 Dialog 形式
     *
     * @param name        姓名
     * @param phoneNumber 电话号码
     * @param goodsId     货源id
     * @param orderStatus 订单状态
     * @param isUpload    是否上传记录
     */
    private void showCallDialog(final MvpActivity activity, String name, final String phoneNumber, String
            goodsId, String orderStatus, final boolean isUpload) {
        this.activity = activity;
        final AutoDialog autoDialog = new AutoDialog(activity);

        // 整理上传请求参数
        if (isUpload) {
            processingParameters(phoneNumber, goodsId, orderStatus);
        }
        // 姓名只显示4个字符
        if(StringUtils.isNotBlank(name) && name.length() > 4){
            name = name.substring(0, 4);
            name = name+"...";
        }
        autoDialog.setTitle(name);
        autoDialog.setContent(applyPhoneNumber(phoneNumber));
        autoDialog.setLeftText(R.string.cancel);
        autoDialog.setRightText(R.string.call);
        autoDialog.setOnBothConfirmListener(new AutoDialog.OnBothConfirmListener() {
            @Override
            public void onLeftClick() {
                autoDialog.toggleShow();
            }

            @Override
            public void onRightClick() {
                directCallPhone(activity, applyPhoneNumber(phoneNumber), isUpload);
                autoDialog.toggleShow();
            }
        });
        autoDialog.toggleShow();
    }


    /**
     * 拨打电话对话框 PopupWindow形式
     *
     * @param name        姓名
     * @param phoneNumber 电话
     * @param flag        提示语显示标签
     * @param goodsId     订单Id
     * @param goodsStatus 订单状态
     * @param isUpload    是否上传通话记录
     */
    private void showCallDialogWithAdv(MvpActivity activity, View view, String name, String phoneNumber, String
            bottomText, int flag, String goodsId, String goodsStatus, boolean isUpload) {
        this.activity = activity;

        // 整理上传请求参数
        processingParameters(phoneNumber, goodsId, goodsStatus);

        String note = "";
        switch (flag) {
            case 0:// 在 司机端 1.【智能找货】，2.【订单详情】，3.【听单页面】，4.【货源列表】，5.【附近货源】，6.【输入提货码或签收码页面】，7，【我要评价】，8.【我的订单】页面呼叫货主
                note = "联系时，请告诉对方是从560交运配货看到的货源\n选择560支付定金，承接托管订单，更安全！";
                break;

            case 1:// 在 司机端 1.【诚信查询】，2.【通话记录】，3.【路线规划】页面呼叫货主
                note = "联系时，请告诉对方信息来自560交运配货\n选择560支付定金，承接托管订单，更安全！";
                break;

            case 2:// 在 货主端 1.【我的订单】，2.【订单详情】，3.【我的报价】，4.【历史订单】5.【我要评价】页面呼叫司机
                note = "联系时，请告诉对方是在560交运配货上发布的货源\n选择560托管运费，交易更安全！";
                break;

            case 3:// 在 货主端 1.【附近车源】，2.【车源列表】，3.【诚信查询】，4.【熟车列表】，5.【车辆详情】页面呼叫司机
                note = "联系时，请告诉对方信息来自560交运配货\n选择560托管运费，交易更安全！";
                break;

        }
        new CallDialogWithAdv(activity,
                new CallDialogWithAdv.onClickCallListener() {
                    @Override
                    public void onClick(Activity activity, String phone, boolean needStatistics) {
                        directCallPhone(activity, phone, needStatistics);
                    }
                })
                .initPop(applyPhoneNumber(phoneNumber), isUpload, name, note, bottomText, Config.getCallPhoneAdvUrl())
                .showPop(view);
    }

    /**
     * 校验手机号码，为空，则默认成客服电话
     */
    private String applyPhoneNumber(String phoneNumber) {
        if (StringUtils.isEmpty(phoneNumber)) {
            phoneNumber = CConstants.SERVICE_PHONE_NUMBER2;
        }
        return phoneNumber;
    }

    /**
     * 整理参数
     */
    private void processingParameters(String phoneNumber, String goodsId, String goodsStatus) {
        params.setCall_type(StringUtils.isEmpty(phoneNumber) ? "2" : "1");
        params.setCall_user_mobile(CMemoryData.getUserMobile());
        params.setCalled_user_mobile(phoneNumber);
        params.setCall_time(time + "");
        params.setGoods_id(goodsId);
        params.setGoods_status(goodsStatus);
    }

    /**
     * 跳转拨打电话页面
     *
     * @param needStatistics 是否需要统计
     */
    public void directCallPhone(final Activity activity, final String phoneNumber, final boolean needStatistics) {
        if (StringUtils.isEmpty(phoneNumber)) {
            return;
        }

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                RxPermissions rxPermissions = new RxPermissions(activity);
                rxPermissions.setLogging(true);
                rxPermissions
                        .requestEach(// 写入需要获取的运行时权限
                                Manifest.permission.CALL_PHONE)
                        .subscribe(new Consumer<Permission>() {// 观察者
                                       @Override
                                       public void accept(Permission permission) {// 请求权限的返回结果
                                           if (permission.name.equals(Manifest.permission.CALL_PHONE)) {
                                               //当CAMERA权限获取成功时，permission.granted=true
                                               Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" +
                                                       phoneNumber));
                                               intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                               if (ActivityCompat.checkSelfPermission(activity, Manifest.permission
                                                       .CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                                   return;
                                               }
                                               activity.startActivity(intent);

                                               // 设置通话监听
                                               if (needStatistics) {
                                                   manager = (TelephonyManager) activity.getSystemService(Context
                                                           .TELEPHONY_SERVICE);
                                                   phoneListener = new PhoneListener();
                                                   manager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
                                               }
                                           }
                                       }
                                   }
                                ,
                                new Consumer<Throwable>() {// 获取权限发生异常，必须得写，华为系统当用户拒绝后，第二次请求会直接报error
                                    @Override
                                    public void accept(Throwable t) {
                                    }
                                },
                                new Action() {// 结束
                                    @Override
                                    public void run() {
                                    }
                                }
                        );
            }
        });
    }

    /**
     * 监听通话状态
     */
    private class PhoneListener extends PhoneStateListener {

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            //判断是否是需要监听的手机号
            if (StringUtils.isBlank(params.getCalled_user_mobile()))
                return;
            Logger.i("oye", "onCallStateChanged2");
            //判断是否是在App内拨打的电话
//            if (!isInAppCall)
//                return;
            Logger.i("oye", "onCallStateChanged3");
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE: // 空闲状态，也就是挂电话
                    Logger.i("oye", "onCallStateChanged4");
                    if (isCalling) {
                        Logger.i("oye", "onCallStateChanged5");
                        isCalling = false;
                        endTime = TimeUtils.getFormatDate(System.currentTimeMillis(), TimeUtils.COMMON_TIME_PATTERN);
                        // 获取当前结束节点时间
                        time = TimeUtils.calculationDate(startTime, endTime);// 计算时间间隔
                        if (time < 0) {
                            time = Math.abs(time);// 取正数
                        }
                        time = 0;
//                        isInAppCall = false;

                        /**
                         * 注销监听
                         */
                        if (phoneListener != null && manager != null) {
                            manager.listen(phoneListener, PhoneStateListener.LISTEN_NONE);
                            phoneListener = null;
                            manager = null;
                        }

                        /**
                         * 请求网络，上传时长
                         */
                        new UserCommand(UserCommandAPI.class, activity).saveCallPhoneInfo(params, new
                                ObserverOnResultListener<Object>() {
                                    @Override
                                    public void onResult(Object object) {
                                        if (callPhoneSucceedListener != null) {
                                            callPhoneSucceedListener.isSucceed(true);
                                        }
                                    }
                                });
                    }
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:// 摘机，也就是接电话
                    Logger.i("oye", "onCallStateChanged6");
                    isCalling = true;
                    startTime = TimeUtils.getFormatDate(System.currentTimeMillis(), TimeUtils.COMMON_TIME_PATTERN);//
                    // 获取当前开始节点时间
                    break;
                case TelephonyManager.CALL_STATE_RINGING:// 来电响铃状态
                    Logger.i("oye", "onCallStateChanged7");
                    break;
            }
        }
    }
}