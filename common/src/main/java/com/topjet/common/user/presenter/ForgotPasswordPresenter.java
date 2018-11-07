package com.topjet.common.user.presenter;

import android.content.Context;
import android.widget.TextView;

import com.topjet.common.R;
import com.topjet.common.base.listener.ObserverOnResultListener;
import com.topjet.common.common.modle.params.CheckVerificationCodeParams;
import com.topjet.common.user.modle.params.SendCheckCodeParams;
import com.topjet.common.user.modle.response.SendCheckCodeResponse;
import com.topjet.common.user.modle.serverAPI.UserCommand;
import com.topjet.common.user.view.activity.ForgotPasswordView;
import com.topjet.common.user.view.activity.LoginView;
import com.topjet.common.utils.AppManager;
import com.topjet.common.utils.ApplyUtils;
import com.topjet.common.utils.CallPhoneUtils;
import com.topjet.common.utils.StringUtils;
import com.topjet.common.utils.Toaster;

import io.reactivex.disposables.Disposable;

/**
 * 忘记密码
 * Created by tsj004 on 2017/8/14.
 */

public class ForgotPasswordPresenter extends LoginPresenter {

    @Override
    public void onCreate() {
    }

    public ForgotPasswordPresenter(LoginView mView, Context mContext, UserCommand mApiCommand) {
        super(mView, mContext, mApiCommand);
    }

    /**
     * 播打客服电话对话框
     */
    public void showCallPhoneConfirmDialog() {
        new CallPhoneUtils().callCustomerService(mActivity);
    }

    /**
     * 校验验证码
     */
    public void checkCode(String strMobile, String strCode) {
        if (!checkMobile(strMobile)) {
            return;
        }
        if (StringUtils.isEmpty(strCode)) {
            Toaster.showLongToast("请输入验证码");
            return;
        }

        CheckVerificationCodeParams checkCodeParams = new CheckVerificationCodeParams();
        checkCodeParams.setMobile(strMobile);
        checkCodeParams.setcode_type("3");
        checkCodeParams.setCode(strCode);

        mApiCommand.checkCode(checkCodeParams, new ObserverOnResultListener<SendCheckCodeResponse>() {
            @Override
            public void onResult(SendCheckCodeResponse response) {
                if (response != null) {
                    if (StringUtils.isNotBlank(response.getCode())) {
                        if (response.getCode().equals("1")) {
                            //成功
                            ((ForgotPasswordView) mView).goToResetPasswordActivity();
                        } else {
                            //失败
                            Toaster.showShortToast("验证码错误！请重新输入");
                        }
                    }
                }
            }
        });
    }

    /**
     * 获取验证码
     */
    @Override
    public void getVerifyCode(final Disposable disposable, final TextView tvSendCode, final TextView tvVoiceCode) {
        getVerifyCode(disposable, tvSendCode, tvVoiceCode, SendCheckCodeParams.FORGET_PASSWORD);
    }

    /**
     * 检查手机号
     */
    public boolean checkMobile(String mobile) {
        if (StringUtils.isEmpty(mobile)) {
            Toaster.showLongToast("请输入手机号");
            return false;
        }
        if (!ApplyUtils.validateMobile(mobile)) {
            Toaster.showLongToast(R.string.please_input_correct_phone_number);
            return false;
        }
        return true;
    }

    /**
     * 检查验证码
     *
     * @param code
     * @return
     */
    public boolean checkViriyCode(String code) {
        if (StringUtils.isEmpty(code)) {
            Toaster.showLongToast("请输入验证码");
            return false;
        }

        return true;
    }
}
