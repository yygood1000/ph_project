package com.topjet.common.user.view.activity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.topjet.common.R;
import com.topjet.common.base.dialog.AutoDialog;
import com.topjet.common.base.listener.DebounceClickListener;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.user.modle.serverAPI.UserCommand;
import com.topjet.common.user.modle.serverAPI.UserCommandAPI;
import com.topjet.common.user.presenter.RegisterPresenter;
import com.topjet.common.utils.ApplyUtils;
import com.topjet.common.utils.EditTextGangedUtils;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.ResourceUtils;
import com.topjet.common.utils.StringUtils;
import com.topjet.common.utils.Toaster;
import com.topjet.common.widget.MyTitleBar;

/**
 * 注册界面
 */
public class RegisterActivity extends MvpActivity<RegisterPresenter> implements RegisterView {
    private TextView tvServiceContract;// 服务协议
    private EditText etPhoneNum;// 手机号
    private EditText etVerifyCode;// 验证码
    private EditText etLoginPassword;// 登陆密码
    private Button btnVerifyCode;// 获取验证码s
    private Button btnRegister;// 下一步按钮，相当于注册
    private TextView tvVoiceVerifyCode;// 语音验证码

    @Override
    protected void initPresenter() {
        mPresenter = new RegisterPresenter(this, this, new UserCommand(UserCommandAPI.class, this));
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_register;
    }

    @Override
    protected void initTitle() {
        getMyTitleBar().setMode(MyTitleBar.Mode.BACK_ONLY).hideLine();

    }

    @Override
    protected void initView() {
        tvServiceContract = (TextView) findViewById(R.id.tv_service_contract);
        etPhoneNum = (EditText) findViewById(R.id.et_phone_num);
        etVerifyCode = (EditText) findViewById(R.id.et_verification_code);
        etLoginPassword = (EditText) findViewById(R.id.et_login_password);
        btnRegister = (Button) findViewById(R.id.btn_register);
        btnVerifyCode = (Button) findViewById(R.id.btn_verifi_code);
        tvVoiceVerifyCode = (TextView) findViewById(R.id.tv_voice_verify_code);

        tvServiceContract.setOnClickListener(clickListener);// 服务协议
        btnVerifyCode.setOnClickListener(clickListener);// 获取验证码
        tvVoiceVerifyCode.setOnClickListener(clickListener);// 语音验证码
        btnRegister.setOnClickListener(clickListener);// 下一步

        // 设置监听
        setWatcher();

        // 更改按钮/文本框/输入框 背景样式
        setViewStyle();

        // 设置电话号码
        if (mPresenter.phoneExtra != null && StringUtils.isNotBlank(mPresenter.phoneExtra.getPhone())) {
            setPhone(mPresenter.phoneExtra.getPhone());
        }
    }

    @Override
    protected void initData() {
        mPresenter.getLocate();
    }

    /**
     * 根据app版本，更改按钮/文本框/输入框 背景样式
     */
    private void setViewStyle() {
        if (CMemoryData.isDriver()) {
            btnVerifyCode.setBackgroundResource(R.drawable.selector_btn_corner_blue);
            btnRegister.setBackgroundResource(R.drawable.selector_btn_corner_blue);
            tvServiceContract.setTextColor(ResourceUtils.getColor(R.color.text_color_6E90FF));
            tvVoiceVerifyCode.setTextColor(getResources().getColorStateList(R.color
                    .selector_text_color_register_driver));

            etPhoneNum.setBackgroundResource(R.drawable.selector_login_edittext_bg_blue);
            etLoginPassword.setBackgroundResource(R.drawable.selector_login_edittext_bg_blue);
            etVerifyCode.setBackgroundResource(R.drawable.selector_login_edittext_bg_blue);
        }
    }

    /**
     * 设置监听
     */
    private void setWatcher() {
        EditTextGangedUtils.setEtLengthWithSendEnable((MvpActivity) mContext, etPhoneNum, btnVerifyCode);
        EditTextGangedUtils.setEtLengthWithSendEnable((MvpActivity) mContext, etPhoneNum, tvVoiceVerifyCode);
        EditTextGangedUtils.setEnableWithDoubleEt(this, etPhoneNum, etVerifyCode, btnRegister);
    }

    DebounceClickListener clickListener = new DebounceClickListener() {
        @Override
        public void performClick(View v) {
            int id = v.getId();
            if (id == R.id.tv_service_contract) {
                // 服务协议
                turnToActivity(ServiceContractActivity.class);
            } else if (id == R.id.btn_verifi_code) {
                // 获取验证码
                if (ApplyUtils.validateMobile(etPhoneNum.getText().toString())) {
                    CMemoryData.setUserMobile(etPhoneNum.getText().toString());
                    mPresenter.startCountDown(RegisterPresenter.GET_VERIFY_CODE);
                } else {
                    Toaster.showShortToast(getString(R.string.phone_number_error_warning));
                }
            } else if (id == R.id.btn_register) {
                // 下一步
                mPresenter.doRegister(etPhoneNum.getText().toString(), etLoginPassword.getText().toString(),
                        etVerifyCode.getText().toString());
            } else if (id == R.id.tv_voice_verify_code) {
                // 语音验证码
                Logger.i("oye", "click");
                showVoiceVerifyCodeDialog();
            }
        }
    };

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
                mPresenter.startCountDown(RegisterPresenter.GET_VOICE_VERIFY_CODE);
                dialog.toggleShow();
            }
        });
        dialog.toggleShow();
    }

    /**
     * 设置按钮是否可点击
     */
    @Override
    public void setBtnVerifyCodeEnable(boolean b) throws Exception {
        RxView.enabled(btnVerifyCode).accept(b);
    }

    /**
     * 刷新倒计时文字
     */
    @Override
    public void setBtnVerifyCodeText(String str) {
        btnVerifyCode.setText(str);
    }

    @Override
    public void showUserAlreadyRegisterDialog(final String mobile) {
        final AutoDialog dialog = new AutoDialog(mContext);
        dialog.setTitle("您输入的手机号已经注册\n是否直接登录？");
        dialog.setLeftText(R.string.cancel);
        dialog.setRightText(R.string.immediate_login);
        dialog.setOnBothConfirmListener(new AutoDialog.OnBothConfirmListener() {
            @Override
            public void onLeftClick() {
                dialog.toggleShow();
            }

            @Override
            public void onRightClick() {
                LoginActivity.turnToCodeLogin(RegisterActivity.this, mobile);
            }
        });
        dialog.toggleShow();
    }

    @Override
    public void setPhone(String phone) {
        etPhoneNum.setText(phone);
    }
}