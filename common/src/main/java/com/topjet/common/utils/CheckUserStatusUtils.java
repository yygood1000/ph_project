package com.topjet.common.utils;

import com.topjet.common.base.CommonProvider;
import com.topjet.common.base.dialog.AutoDialog;
import com.topjet.common.base.listener.ObserverOnResultListener;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.common.modle.response.GetUserInfoAtHomeResponse;
import com.topjet.common.common.modle.serverAPI.HomeCommand;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.user.modle.extra.GoToAuthenticationExtra;

/**
 * 判读是否实名认证操作类
 * Created by tsj004 on 2017/8/29.
 * 1.实名认证状态判断
 * 2.车辆认证状态判断
 * <p>
 * 步骤：
 * 1.从CMemoryData中获取userBaseInfo对象
 * 2.若userBaseInfo不为空，判断对应状态值，给出对应相应。
 * 3.若userBaseInfo为空，进行用户参数接口请求，获取userBaseInfo数据。
 * 4.获取到userBaseInfo数据后，再判断对应状态值，给出对应相应。
 */

public class CheckUserStatusUtils {
    /*=====================实名认证状态常量=====================*/
    // 默认 证件未提交
    private static final String USER_STATUS_NOTCOMMIT = "0";
    // 待审核
    public static final String USER_STATUS_WAITAUCIT = "1";
    // 审核通过
    private static final String USER_STATUS_APPROVED = "2";
    // 资料修改待审核
    private static final String USER_STATUS_DATA_WAIT_REVIEWED = "3";
    // 认证失败
    private static final String USER_STATUS_FAILURE = "4";

    /*=====================实名认证状态常量=====================*/
    /*=====================车辆认证状态常量=====================*/

    // 无需认证
    private static final String TRUCK_STATUS_NEEDLESS = "0";
    // 未认证（没有提交过信息）
    private static final String TRUCK_STATUS_NOTCOMMIT = "1";
    // 已认证
    private static final String TRUCK_STATUS_APPROVED = "2";
    // 认证中
    public static final String TRUCK_STATUS_DATA_WAIT_REVIEWED = "3";
    // 认证失败
    private static final String TRUCK_STATUS_FAILURE = "4";

    /*=====================车辆认证状态常量=====================*/

    /**
     * 判断状态通过回调
     */
    public interface OnJudgeResultListener {
        void onSucceed();
    }

    /**
     * 处理实名认证不通过的各种情况
     */
    private static void processingRealNameFailed(final MvpActivity mActivity, String realNameStatus) {
        if(StringUtils.isEmpty(realNameStatus)){
            showRealNameDialog(mActivity, USER_STATUS_NOTCOMMIT);
            return;
        }
        switch (realNameStatus) {
            case USER_STATUS_NOTCOMMIT://证件未提交
                showRealNameDialog(mActivity, USER_STATUS_NOTCOMMIT);
                break;
            case USER_STATUS_WAITAUCIT://待审核
            case USER_STATUS_DATA_WAIT_REVIEWED://资料修改待审核
                Toaster.showShortToast("您的实名认证信息还在认证中，无法进行此操作，请耐心等待认证结果！");
                break;
            case USER_STATUS_FAILURE://认证失败
                showRealNameDialog(mActivity, USER_STATUS_FAILURE);
                break;
        }
    }

    /**
     * 弹出去实名认证对话框
     */
    private static void showRealNameDialog(final MvpActivity mActivity, final String status) {
        final AutoDialog autoDialog = new AutoDialog(mActivity);
        switch (status) {
            case USER_STATUS_NOTCOMMIT://证件未提交
                autoDialog.setContent("通过实名认证，才可进行此操作。");
                autoDialog.setRightText("去认证");
                break;
            case USER_STATUS_FAILURE://认证失败
                autoDialog.setContent("对不起，您的实名认证审核未通过，暂无法使用该功能，请重新提交审核。");
                autoDialog.setRightText("重新认证");
                break;
        }
        autoDialog.setLeftText("稍后再说");
        autoDialog.setOnBothConfirmListener(new AutoDialog.OnBothConfirmListener() {
            @Override
            public void onLeftClick() {
                autoDialog.toggleShow();
            }

            @Override
            public void onRightClick() {
                switch (status) {
                    case USER_STATUS_NOTCOMMIT://证件未提交
                        if (CMemoryData.isDriver()) {
                            CommonProvider.getInstance().getJumpDriverProvider().jumpRealNameAuthentication(mActivity,
                                    GoToAuthenticationExtra.IN_TYPE_ADD);
                        } else {
                            CommonProvider.getInstance().getJumpShipperProvider().jumpRealNameAuthentication(mActivity,
                                    GoToAuthenticationExtra.IN_TYPE_SHOW);
                        }
                        break;
                    case USER_STATUS_FAILURE://认证失败
                        if (CMemoryData.isDriver()) {
                            CommonProvider.getInstance().getJumpDriverProvider().jumpRealNameAuthentication
                                    (mActivity, GoToAuthenticationExtra.IN_TYPE_SHOW);
                        } else {
                            CommonProvider.getInstance().getJumpShipperProvider().jumpRealNameAuthentication
                                    (mActivity, GoToAuthenticationExtra.IN_TYPE_SHOW);
                        }
                        break;
                }
                autoDialog.toggleShow();
            }
        });
        autoDialog.toggleShow();
    }


    /**
     * 处理车辆认证不通过的各种情况
     */
    private static void processingTruckFailed(final MvpActivity mActivity, String realNameStatus) {
        switch (realNameStatus) {
            case TRUCK_STATUS_NOTCOMMIT://证件未提交
                showTruckDialog(mActivity, TRUCK_STATUS_NOTCOMMIT);
                break;
            case TRUCK_STATUS_DATA_WAIT_REVIEWED://认证中
                Toaster.showShortToast("您的车辆认证信息还在认证中，无法进行此操作，请耐心等待认证结果！");
                break;
            case TRUCK_STATUS_FAILURE://认证失败
                showTruckDialog(mActivity, TRUCK_STATUS_FAILURE);
                break;
            default:
                showTruckDialog(mActivity, TRUCK_STATUS_NOTCOMMIT);
                break;
        }
    }

    /**
     * 弹出去实名认证对话框
     */
    private static void showTruckDialog(final MvpActivity mActivity, final String status) {
        final AutoDialog autoDialog = new AutoDialog(mActivity);
        switch (status) {
            case TRUCK_STATUS_NOTCOMMIT://证件未提交
                autoDialog.setContent("通过车辆认证，才可进行此操作");
                autoDialog.setRightText("去认证");
                break;
            case TRUCK_STATUS_FAILURE://认证失败
                autoDialog.setContent("对不起，您的车辆认证审核未通过，暂无法使用该功能，请重新提交审核。");
                autoDialog.setRightText("重新认证");
                break;
        }
        autoDialog.setLeftText("稍后再说");
        autoDialog.setOnBothConfirmListener(new AutoDialog.OnBothConfirmListener() {
            @Override
            public void onLeftClick() {
                autoDialog.toggleShow();
            }

            @Override
            public void onRightClick() {
                switch (status) {
                    case TRUCK_STATUS_NOTCOMMIT://证件未提交
                        CommonProvider.getInstance().getJumpDriverProvider().jumpCarAuthentication(mActivity,
                                GoToAuthenticationExtra.IN_TYPE_SHOW);
                        break;
                    case TRUCK_STATUS_FAILURE://认证失败
                        CommonProvider.getInstance().getJumpDriverProvider().jumpCarAuthentication(mActivity,
                                GoToAuthenticationExtra.IN_TYPE_SHOW);
                        break;
                }
                autoDialog.toggleShow();
            }
        });
        autoDialog.toggleShow();
    }

    /*========================对外提供的方法========================*/

    /**
     * 判断用户是否已通过实名认证
     */
    public static void isRealNameAuthentication(final MvpActivity mActivity, final OnJudgeResultListener listener) {
        if (null != CMemoryData.getUserBaseInfo()) {
            if (USER_STATUS_APPROVED.equals(CMemoryData.getUserBaseInfo().getUser_status())) {// 认证通过
                listener.onSucceed();
            } else {
                getUserParameter(mActivity, listener, USER_STATUS);
            }
        } else {
            getUserParameter(mActivity, listener, USER_STATUS);
        }
    }

    /**
     * 判断用户是否已通过车辆认证
     */
    private static void isTruckAuthentication(final MvpActivity mActivity, final OnJudgeResultListener listener) {
        if (CMemoryData.getUserBaseInfo() != null ) {
            if (TRUCK_STATUS_APPROVED.equals(CMemoryData.getUserBaseInfo().getTruck_status())) {// 认证通过
                listener.onSucceed();
            } else {
                getUserParameter(mActivity, listener, TRUCK_STATUS);
            }
        } else {
            getUserParameter(mActivity, listener, TRUCK_STATUS);
        }
    }

    /**
     * 获取用户信息，认证状态
     */
    private static final int TRUCK_STATUS = 1;
    private static final int USER_STATUS = 2;
    private static void getUserParameter(final MvpActivity mActivity, final OnJudgeResultListener listener, final int statusType){
        mActivity.showLoadingDialog();
        new HomeCommand(mActivity).getTheUserParameter(true, new
                ObserverOnResultListener<GetUserInfoAtHomeResponse>() {
                    @Override
                    public void onResult(GetUserInfoAtHomeResponse getUserInfoAtHomeResponse) {
                        CMemoryData.setUserBaseInfo(getUserInfoAtHomeResponse);
                        if (getUserInfoAtHomeResponse.getUser_status().equals(USER_STATUS_APPROVED)) {// 认证通过
                            listener.onSucceed();
                        } else {
                            if(statusType == USER_STATUS) {
                                processingRealNameFailed(mActivity, getUserInfoAtHomeResponse.getUser_status());
                            } else {
                                processingRealNameFailed(mActivity, getUserInfoAtHomeResponse.getTruck_status());
                            }
                        }
                        mActivity.cancelShowLoadingDialog();
                    }
                });
    }

    /**
     * 同时校验实名认证状态和车辆认证状态
     */
    public static void isRealNameAndTruckApproved(final MvpActivity mActivity, final OnJudgeResultListener listener) {
        // 先进行实名认证状态的判断
        isRealNameAuthentication(mActivity, new OnJudgeResultListener() {
            @Override
            public void onSucceed() {
                // 在进行车辆认证状态的判断
                isTruckAuthentication(mActivity, listener);
            }
        });
    }

    /*========================对外提供的方法========================*/
}
