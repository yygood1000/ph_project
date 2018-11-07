package com.topjet.common.user.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.topjet.common.R;
import com.topjet.common.base.dialog.AutoDialog;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.common.modle.response.AppUpgradeResponse;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.user.modle.extra.PhoneExtra;
import com.topjet.common.user.modle.serverAPI.UserCommand;
import com.topjet.common.user.modle.serverAPI.UserCommandAPI;
import com.topjet.common.user.presenter.ForgotPasswordPresenter;
import com.topjet.common.user.presenter.RegisterPresenter;
import com.topjet.common.utils.EditTextGangedUtils;
import com.topjet.common.utils.ResourceUtils;
import com.topjet.common.widget.MyTitleBar;

import butterknife.ButterKnife;

/**
 * 忘记密码
 * Created by tsj004 on 2017/8/14.
 */

public class ForgotPasswordActivity extends MvpActivity<ForgotPasswordPresenter> implements ForgotPasswordView {

    private String phoneNumber;
    // 手机号输入框
    private EditText etPhonenumber;
    // 一键清空手机号
    private ImageView ivDeletePhonenumber;
    // 一键清空验证码
    private ImageView ivDeleteCode;
    // 验证码输入框
    private EditText etCode;
    // 发送验证码
    private TextView tvSendCode;
    //下一步
    private TextView tvNext;
    // 发送语音验证码
    private LinearLayout llSendVoice;
    // 语音验证
    private TextView tvVoiceCode;

    @Override
    public void initTitle() {
        getMyTitleBar()
                .setMode(MyTitleBar.Mode.BACK_TITLE_RIMG)
                .setRightImg(R.drawable.nav_icon_customerservice_nor_black);
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ForgotPasswordPresenter(this, this, new UserCommand(UserCommandAPI.class, this));
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_forgot_password;
    }

    @Override
    protected void initView() {
        tvSendCode = ButterKnife.findById(this, R.id.tv_send_code);
        etPhonenumber = ButterKnife.findById(this, R.id.et_phonenumber);
        ivDeletePhonenumber = ButterKnife.findById(this, R.id.iv_delete_phonenumber);
        ivDeleteCode = ButterKnife.findById(this, R.id.iv_delete_code);
        etCode = ButterKnife.findById(this, R.id.et_check_code);
        tvNext = ButterKnife.findById(this, R.id.tv_next);
        llSendVoice = ButterKnife.findById(this, R.id.ll_send_voice);
        tvVoiceCode = ButterKnife.findById(this, R.id.tv_voice_code);

        tvSendCode.setOnClickListener(this);
        ivDeletePhonenumber.setOnClickListener(this);
        ivDeleteCode.setOnClickListener(this);
        tvNext.setOnClickListener(this);
        tvVoiceCode.setOnClickListener(this);

        setDefaultColor(etPhonenumber, etCode, tvNext, tvSendCode, tvVoiceCode, etCode);
        //设置按钮、文本框默认颜色
        tvNext.setEnabled(false);

        EditTextGangedUtils.setEtLengthWithDelete((MvpActivity) mContext, etPhonenumber, ivDeletePhonenumber);
        EditTextGangedUtils.setEtLengthWithDelete((MvpActivity) mContext, etCode, ivDeleteCode);
        EditTextGangedUtils.setEtLengthWithSendEnable((MvpActivity) mContext, etPhonenumber, tvSendCode);
        EditTextGangedUtils.setEtLengthWithSendEnable((MvpActivity) mContext, etPhonenumber, tvVoiceCode);
        EditTextGangedUtils.setEnableWithDoubleEt((MvpActivity) mContext, etPhonenumber, etCode, tvNext);
    }


    /**
     * 设置各种按钮和字体的默认颜色
     * 货主绿色
     * 司机蓝色
     */
    public void setDefaultColor(EditText etPhonenumber, EditText etPassword, TextView tvNext, TextView tvSendCode,
                                TextView tvVoiceCode, EditText etCode) {
        if (CMemoryData.isDriver()) {
            etPhonenumber.setBackgroundResource(R.drawable.selector_login_edittext_bg_blue);
            etPassword.setBackgroundResource(R.drawable.selector_login_edittext_bg_blue);
            etCode.setBackgroundResource(R.drawable.selector_login_edittext_bg_blue);
            tvNext.setBackgroundResource(R.drawable.selector_btn_corner_blue);
            tvSendCode.setBackgroundResource(R.drawable.selector_btn_corner_blue);
            tvVoiceCode.setTextColor(mContext.getResources().getColor(R.color.v3_common_blue));
        } else {
            etPhonenumber.setBackgroundResource(R.drawable.selector_login_edittext_bg_green);
            etPassword.setBackgroundResource(R.drawable.selector_login_edittext_bg_green);
            etCode.setBackgroundResource(R.drawable.selector_login_edittext_bg_green);
            tvNext.setBackgroundResource(R.drawable.selector_btn_corner_green);
            tvSendCode.setBackgroundResource(R.drawable.selector_btn_corner_green);
            tvVoiceCode.setTextColor(mContext.getResources().getColor(R.color.color_17B745));
        }
    }

    public void onClick(View v) {
        super.onClick(v);

        int id = v.getId();
        if (id == R.id.tv_next) {
            //下一步
            if (mPresenter.checkViriyCode(etPhonenumber.getText().toString().trim())) {
                mPresenter.checkCode(etPhonenumber.getText().toString().trim(), etCode.getText().toString().trim());
            }
        } else if (id == R.id.iv_delete_phonenumber) {
            //清空手机号
            etPhonenumber.setText("");
        } else if (id == R.id.iv_delete_code) {
            //清空验证码
            etCode.setText("");
        } else if (id == R.id.tv_send_code) {
            if (mPresenter.checkMobile(etPhonenumber.getText().toString().trim())) {
                //发送验证码
                CMemoryData.setUserMobile(etPhonenumber.getText().toString().trim());
                mPresenter.startCountDown(tvSendCode, tvVoiceCode, RegisterPresenter.GET_VERIFY_CODE);
            }
        } else if (id == R.id.tv_voice_code) {
            if (mPresenter.checkMobile(etPhonenumber.getText().toString().trim())) {
                //发送语音验证码
                CMemoryData.setUserMobile(etPhonenumber.getText().toString().trim());
                showVoiceVerifyCodeDialog();
            }
        }
    }


    /**
     * 点击标题栏右侧图片
     */
    @Override
    protected void onClickRightImg() {
        mPresenter.showCallPhoneConfirmDialog();
    }

    /**
     * 显示语音验证码弹窗
     */
    public void showVoiceVerifyCodeDialog() {
        final AutoDialog dialog = new AutoDialog(mContext);
        dialog.setTitle(ResourceUtils.getString(R.string.is_get_voice_verify_code));
        dialog.setOnBothConfirmListener(new AutoDialog.OnBothConfirmListener() {
            @Override
            public void onLeftClick() {
                dialog.toggleShow();
            }

            @Override
            public void onRightClick() {
                mPresenter.startCountDown(tvSendCode, tvVoiceCode, RegisterPresenter.GET_VOICE_VERIFY_CODE);
                dialog.toggleShow();
            }
        });
        dialog.toggleShow();
    }

    /**
     * 接收传过来的手机号
     */
    public void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            phoneNumber = bundle.getString("phoneNumber");
        }
        etPhonenumber.setText(phoneNumber);
    }

    /**
     * 跳转到重置密码页面
     */
    @Override
    public void goToResetPasswordActivity() {
        Bundle bundle = new Bundle();
        bundle.putString("phoneNumber", etPhonenumber.getText().toString());
        turnToActivityWithFinish(ResetPasswordActivity.class, bundle);
        finish();
    }

    @Override
    public void showUserNotRegisterDialog(String title) {
        final AutoDialog dialog = new AutoDialog(mContext);
        dialog.setTitle(title);
        dialog.setLeftText(R.string.cancel);
        dialog.setRightText(R.string.register_now);
        dialog.setOnBothConfirmListener(new AutoDialog.OnBothConfirmListener() {
            @Override
            public void onLeftClick() {
                dialog.toggleShow();
            }

            @Override
            public void onRightClick() {
                turnToActivity(RegisterActivity.class, new PhoneExtra(etPhonenumber.getText().toString()));
                dialog.toggleShow();
            }
        });
        dialog.toggleShow();
    }

    @Override
    public void showUserBeBanned(String time, String remark) {

    }

    @Override
    public void showUpdateDialog(AppUpgradeResponse result) {

    }

    @Override
    public void changeLoginWay(int curLoinType) {
    }

    @Override
    public void autoSendCode() {
    }

}
