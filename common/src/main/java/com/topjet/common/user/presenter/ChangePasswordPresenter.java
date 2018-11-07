package com.topjet.common.user.presenter;

import android.content.Context;

import com.topjet.common.base.dialog.AutoDialogHelper;
import com.topjet.common.base.listener.ObserverOnNextListener;
import com.topjet.common.base.presenter.BaseApiPresenter;
import com.topjet.common.base.view.activity.IView;
import com.topjet.common.user.modle.extra.PasswordExtra;
import com.topjet.common.user.modle.params.PasswordParams;
import com.topjet.common.user.modle.response.RetrievePasswordResponse;
import com.topjet.common.user.modle.serverAPI.UserCommand;
import com.topjet.common.user.view.activity.ChangePasswordInputActivity;
import com.topjet.common.user.view.activity.ResetPasswordActivity;
import com.topjetpaylib.encrypt.MD5Helper;

import java.security.NoSuchAlgorithmException;

/**
 * 修改密码
 * Created by yy on 2017/8/14.
 */

public class ChangePasswordPresenter extends BaseApiPresenter<IView, UserCommand> {

    public ChangePasswordPresenter(IView mView, Context mContext, UserCommand mApiCommand) {
        super(mView, mContext, mApiCommand);
    }

    /**
     * 校验旧密码，成功关闭页面，跳转到录入新密码页面
     */
    public void checOldPassword(final String oldPassword) {
        try {
            mApiCommand.checkOldPassword(new PasswordParams(MD5Helper.getMD5(oldPassword)), new
                    ObserverOnNextListener<RetrievePasswordResponse>() {
                        @Override
                        public void onNext(RetrievePasswordResponse response) {
                            switch (response.getStatus()) {
                                case "0":// 通过
                                    mActivity.turnToActivity(ChangePasswordInputActivity.class, new PasswordExtra
                                            (oldPassword));
                                    mActivity.finishPage();
                                    break;
                                case "1": // 错误
                                    mView.showToast(response.getMsg());
                                    break;
                                case "2": // 错误6次
                                    AutoDialogHelper.showContent(mActivity, response.getMsg(), new
                                            AutoDialogHelper.OnConfirmListener() {
                                                @Override
                                                public void onClick() {
                                                    mActivity.turnToActivity(ResetPasswordActivity.class);
                                                }
                                            }).show();
                                    break;
                            }
                        }

                        @Override
                        public void onError(String errorCode, String msg) {
                            mView.showToast(msg);
                        }
                    });
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
