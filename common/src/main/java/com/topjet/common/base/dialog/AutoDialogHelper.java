package com.topjet.common.base.dialog;

import android.content.Context;

import com.topjet.common.config.CMemoryData;
import com.topjet.common.order_detail.modle.state.RefundState;
import com.topjet.common.utils.PermissionsUtils;
import com.topjet.common.utils.StringUtils;

/**
 * creator: zhulunjun
 * time:    2017/9/13
 * describe: 对弹窗的封装操作
 */

public class AutoDialogHelper {

    /**
     * 只需要显示内容的dialog，带确认和取消按钮
     *
     * @param context       上下文
     * @param content       内容
     * @param leftBtnText   左边按钮文本
     * @param rightBtnText  右边按钮文本
     * @param rightListener 确认监听
     * @param leftListener  取消监听
     */
    public static AutoDialog showContent(Context context, String content, String leftBtnText, String rightBtnText,
                                         final OnConfirmListener leftListener, final OnConfirmListener
                                                 rightListener) {
        final AutoDialog autoDialog = new AutoDialog(context);
        autoDialog.setContent(content);
        if (StringUtils.isNotBlank(leftBtnText)) {
            autoDialog.setLeftText(leftBtnText);
        }
        if (StringUtils.isNotBlank(rightBtnText)) {
            autoDialog.setRightText(rightBtnText);
        }
        autoDialog.setOnBothConfirmListener(new AutoDialog.OnBothConfirmListener() {
            @Override
            public void onLeftClick() {
                if (leftListener != null) {
                    leftListener.onClick();
                }
                autoDialog.toggleShow();
            }

            @Override
            public void onRightClick() {
                if (rightListener != null) {
                    rightListener.onClick();
                }
                autoDialog.toggleShow();
            }
        });
        return autoDialog;
    }

    /**
     * 只需要显示内容的dialog，带确认和取消按钮
     *
     * @param context         上下文
     * @param content         内容
     * @param confirmListener 确认监听
     */
    public static AutoDialog showContent(Context context, String content, final OnConfirmListener confirmListener) {
        return showContent(context, content, null, null, confirmListener);
    }

    /**
     * 只需要显示内容的dialog，带确认和取消按钮
     *
     * @param context         上下文
     * @param content         内容
     * @param confirmListener 确认监听
     * @param leftBtnText     左边按钮文本
     * @param rightBtnText    右边按钮文本
     */
    public static AutoDialog showContent(Context context, String content, String leftBtnText, String rightBtnText,
                                         final OnConfirmListener confirmListener) {
        return showContent(context, content, leftBtnText, rightBtnText, null, confirmListener);
    }

    /**
     * 只需要显示内容的dialog
     * 只显示一个按钮，我知道了
     */
    public static AutoDialog showContentOneBtn(Context context, String content, String btnText, final
    OnConfirmListener onConfirmListener) {
        final AutoDialog autoDialog = new AutoDialog(context);
        autoDialog.setContent(content);
        if (StringUtils.isNotBlank(btnText)) {
            autoDialog.setSingleText(btnText);
        }
        autoDialog.setOnSingleConfirmListener(new AutoDialog.OnSingleConfirmListener() {
            @Override
            public void onClick() {
                if (onConfirmListener != null) {
                    onConfirmListener.onClick();
                }
                autoDialog.toggleShow();
            }
        });
        return autoDialog;
    }

    public static AutoDialog showContentOneBtn(Context context, String content) {
        return showContentOneBtn(context, content, null, null);
    }

    public interface OnConfirmListener {
        void onClick();
    }


    /**
     * 输入提货码的提示弹窗
     *
     * @param context         上下文
     * @param refundState     退款状态
     * @param confirmListener 监听
     * @return 弹窗
     */
    public static AutoDialog showPickupDialog(Context context, String isOneself, int refundState, final
    OnConfirmListener confirmListener) {
        // 退款与不退款，显示不一样
        String content;
        if (refundState == RefundState.REFUND_START && isOneself.equals("0")) {
            content = "发送提货码给司机，司机确认提货后将关闭退款申请，您确定发送提货码？";
        } else {
            content = "请先确认司机已到达提货地提货\n" + "确定将提货码发送给司机？";
        }
        return showContent(context, content, confirmListener);
    }

    /**
     * 放弃承接的弹窗
     *
     * @param context         上下文
     * @param confirmListener 监听
     * @return 弹窗
     */
    public static AutoDialog showGiveUpOrderDialog(Context context, OnConfirmListener confirmListener) {
        String content = "确认要放弃当前的运输订单吗？";
        return showContent(context, content, confirmListener);
    }

    /**
     * 设置定位权限的弹窗
     *
     * @return 弹窗
     */
    public static AutoDialog showSettingLocationPermission(final Context context) {
        final AutoDialog autoDialog = new AutoDialog(context);
        String versionName = "";
        if (CMemoryData.isDriver()) {
            versionName = "司机版";
        } else {
            versionName = "货主版";
        }
        autoDialog.setTitle("无法获取您当前位置");
        autoDialog.setContent("请打开手机“系统设置”->“应用程序信息”->“权限管理”打开定位服务，允许“560"+versionName+"”使用您的位置。");
        autoDialog.setRightText("设置");
        autoDialog.setLeftText("暂不");
        autoDialog.setOnBothConfirmListener(new AutoDialog.OnBothConfirmListener() {
            @Override
            public void onLeftClick() {
                autoDialog.dismiss();
            }

            @Override
            public void onRightClick() {
                PermissionsUtils.goToSyetemSetting(context);
            }
        });
        return autoDialog;
    }

}
