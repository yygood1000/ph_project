package com.topjet.common.user.presenter;

import android.content.Context;

import com.topjet.common.base.listener.ObserverOnResultListener;
import com.topjet.common.base.presenter.BaseApiPresenter;
import com.topjet.common.user.modle.params.PasswordParams;
import com.topjet.common.user.modle.response.PasswordResponse;
import com.topjet.common.user.modle.serverAPI.UserCommand;
import com.topjet.common.user.view.activity.ChangePasswordInputActivity;
import com.topjet.common.user.view.activity.ChangePasswordInputView;
import com.topjet.common.utils.ApplyUtils;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.StringUtils;
import com.topjetpaylib.encrypt.MD5Helper;

import java.security.NoSuchAlgorithmException;

/**
 * 修改密码 录入新密码页面
 * Created by yy on 2017/8/14.
 */

public class ChangePasswordInputPresenter extends BaseApiPresenter<ChangePasswordInputView, UserCommand> {

    public ChangePasswordInputPresenter(ChangePasswordInputView mView, Context mContext, UserCommand mApiCommand) {
        super(mView, mContext, mApiCommand);
    }

    /**
     * 上传新密码
     */
    public void changePassword(String newPassword, String confirmPassword) {
        Logger.i("oye", "newPass" + newPassword + "confirm = " + confirmPassword);
        if (StringUtils.isBlank(newPassword)) {
            mView.showToast("请输入新密码");
            return;
        }
        if (StringUtils.isBlank(confirmPassword)) {
            mView.showToast("请再次确认新密码");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            mView.showToast("两次密码输入不一致，请重新输入");
            mView.clearEtConfirmPassword();
            return;
        }

        if (newPassword.equals(((ChangePasswordInputActivity) mActivity).extra.getPassword())) {
            mView.showToast("您输入的新密码与旧密码相同，请重新输入");
            mView.clearEtNewPassword();
            mView.clearEtConfirmPassword();
            return;
        }


        // 校验新密码格式
        if (ApplyUtils.validatePassword(newPassword)) {
            // 进行上传操作
            try {
                PasswordParams params = new PasswordParams(
                        MD5Helper.getMD5(((ChangePasswordInputActivity) mActivity).extra.getPassword()),
                        MD5Helper.getMD5(newPassword));

                mApiCommand.changePassword(
                        params, new ObserverOnResultListener<PasswordResponse>() {
                            @Override
                            public void onResult(PasswordResponse response) {
                                if (response.getCode().equals("1")) {
                                    mView.showToast("密码修改成功");
                                    mActivity.finishPage();
                                }
                            }
                        });
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
    }
}
