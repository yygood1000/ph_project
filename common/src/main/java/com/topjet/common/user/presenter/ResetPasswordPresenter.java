package com.topjet.common.user.presenter;

import android.content.Context;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.topjet.common.R;
import com.topjet.common.base.busManger.BusManager;
import com.topjet.common.base.listener.ObserverOnNextListener;
import com.topjet.common.base.listener.ObserverOnResultListener;
import com.topjet.common.base.presenter.BaseApiPresenter;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.config.CPersisData;
import com.topjet.common.config.RespectiveData;
import com.topjet.common.user.modle.event.CloseLoginActivityEvent;
import com.topjet.common.user.modle.params.LoginParams;
import com.topjet.common.user.modle.params.UpdateUserPassParams;
import com.topjet.common.user.modle.response.LoginResponse;
import com.topjet.common.user.modle.response.RetrievePasswordResponse;
import com.topjet.common.user.modle.serverAPI.UserCommand;
import com.topjet.common.user.view.activity.ResetPasswordView;
import com.topjet.common.utils.ApplyUtils;
import com.topjet.common.utils.ResourceUtils;
import com.topjet.common.utils.Toaster;
import com.topjetpaylib.encrypt.MD5Helper;

import java.security.NoSuchAlgorithmException;

/**
 * 重置密码
 * Created by tsj004 on 2017/8/15.
 */

public class ResetPasswordPresenter extends BaseApiPresenter<ResetPasswordView, UserCommand> {

    public ResetPasswordPresenter(ResetPasswordView mView, Context mContext, UserCommand mApiCommand) {
        super(mView, mContext, mApiCommand);
    }

    /**
     * 设置各种按钮和字体的默认颜色
     * 货主绿色
     * 司机蓝色
     */
    public void setDefaultColor(EditText etPassword, EditText etPassword2, ImageView ivSeePassword, ImageView
            ivSeePassword2, TextView tvOk) {
        if (CMemoryData.isDriver()) {
            etPassword.setBackgroundResource(R.drawable.selector_login_edittext_bg_blue);
            etPassword2.setBackgroundResource(R.drawable.selector_login_edittext_bg_blue);
            ivSeePassword.setImageResource(R.drawable.signin_icon_closeeye_nor_blue);
            ivSeePassword2.setImageResource(R.drawable.signin_icon_closeeye_nor_blue);
            tvOk.setBackgroundResource(R.drawable.selector_btn_corner_blue);
        } else {
            etPassword.setBackgroundResource(R.drawable.selector_login_edittext_bg_green);
            etPassword2.setBackgroundResource(R.drawable.selector_login_edittext_bg_green);
            ivSeePassword.setImageResource(R.drawable.signin_icon_closeeye_nor_green);
            ivSeePassword2.setImageResource(R.drawable.signin_icon_closeeye_nor_green);
            tvOk.setBackgroundResource(R.drawable.selector_btn_corner_green);
        }
    }

    public void updatePass(final String mobile, final String strPass, String strPass2) {
        if (!checkPassData(strPass, strPass2)) {
            return;
        }

        //开始重置密码
        UpdateUserPassParams updateUserPassParams = new UpdateUserPassParams();
        updateUserPassParams.setMobile(mobile);
        try {
            updateUserPassParams.setPwd(MD5Helper.getMD5(strPass));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        mApiCommand.updateUserPass(updateUserPassParams, mobile, new
                ObserverOnResultListener<RetrievePasswordResponse>() {
                    @Override
                    public void onResult(RetrievePasswordResponse result) {
                        if (result.getCode().equals("1")) {
                            Toaster.showShortToast("新密码设置成功");
                            // 做登录操作
                            login(mobile, strPass);
                        } else {
                            Toaster.showShortToast("修改失败！");
                        }
                        mView.finishPage();
                    }
                });
    }

    /**
     * 检查两次密码输入是否符合要求
     */
    private boolean checkPassData(String strPass, String strPass2) {
        if (!strPass.equals(strPass2)) {
            mView.showToast(ResourceUtils.getString(R.string.pass_no_like));
            return false;
        }

        return ApplyUtils.validatePasswordInReset(strPass);
    }


    /**
     * 登录
     */
    private void login(String mobile, String strPass) {
        try {
            LoginParams loginParams = new LoginParams();
            loginParams.setMobile(mobile);
            loginParams.setPass_word(MD5Helper.getMD5(strPass));
            loginParams.setLogin_type("1");//1:密码登录 2:验证码登录
            if (CMemoryData.isDriver()) {
                loginParams.setSystem_type("1");//1 Android司机 2.Android货主
            } else {
                loginParams.setSystem_type("2");
            }

            mApiCommand.login(loginParams, new ObserverOnNextListener<LoginResponse>() {
                @Override
                public void onNext(LoginResponse user) {
                    CPersisData.setIsLogin(true);
                    CPersisData.setUserId(user.getUser_id());
                    BusManager.getBus().post(new CloseLoginActivityEvent());
//                    //跳转到首页
                    mActivity.turnToActivityWithFinish(RespectiveData.getMainActivityClass());
                }

                @Override
                public void onError(String errorCode, String msg) {
                    Toaster.showShortToast(msg);
                }
            });
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
