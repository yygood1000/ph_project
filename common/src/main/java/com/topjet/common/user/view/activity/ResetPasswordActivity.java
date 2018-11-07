package com.topjet.common.user.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.topjet.common.R;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.user.modle.serverAPI.UserCommand;
import com.topjet.common.user.modle.serverAPI.UserCommandAPI;
import com.topjet.common.user.presenter.ResetPasswordPresenter;
import com.topjet.common.utils.CallPhoneUtils;
import com.topjet.common.utils.EditTextGangedUtils;
import com.topjet.common.widget.MyTitleBar;

import butterknife.ButterKnife;

/**
 * 密码找回
 */

public class ResetPasswordActivity extends MvpActivity<ResetPasswordPresenter> implements ResetPasswordView {

    private String phoneNumber;
    private EditText etPassword;
    private EditText etPassword2;
    private ImageView ivSeePassword;        // 显示或隐藏密码
    private ImageView ivSeePassword2;        // 显示或隐藏密码
    private TextView tvComplete;

    @Override
    public void initTitle() {
        getMyTitleBar()
                .setMode(MyTitleBar.Mode.BACK_TITLE_RIMG)
                .setRightImg(R.drawable.nav_icon_customerservice_nor_black);
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ResetPasswordPresenter(this, this, new UserCommand(UserCommandAPI.class, this));
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_reset_password;
    }

    @Override
    protected void initView() {
        etPassword = ButterKnife.findById(this, R.id.et_password);
        etPassword2 = ButterKnife.findById(this, R.id.et_password2);
        ivSeePassword = ButterKnife.findById(this, R.id.iv_see_password);
        ivSeePassword2 = ButterKnife.findById(this, R.id.iv_see_password2);
        tvComplete = ButterKnife.findById(this, R.id.tv_complete);

        tvComplete.setOnClickListener(this);
        tvComplete.setEnabled(false);
        ivSeePassword.setOnClickListener(this);
        ivSeePassword2.setOnClickListener(this);

        // 设置按钮、文本框默认颜色
        mPresenter.setDefaultColor(etPassword, etPassword2, ivSeePassword, ivSeePassword2, tvComplete);
        // 监听密码输入是否合法,然后是否显示按扭是否可用
        EditTextGangedUtils.setEnableWithDoubleEtPassword(this, etPassword, etPassword2, tvComplete);
    }

    public void onClick(View v) {
        super.onClick(v);

        int id = v.getId();
        if (id == R.id.tv_complete) {
            //完成
            mPresenter.updatePass(phoneNumber,
                    etPassword.getText().toString().trim(),
                    etPassword2.getText().toString().trim());
        } else if (id == R.id.iv_see_password) {
            //切换密码是否可见
            EditTextGangedUtils.setEtInputTypeWithImge(etPassword, ivSeePassword);
        } else if (id == R.id.iv_see_password2) {
            //切换密码是否可见
            EditTextGangedUtils.setEtInputTypeWithImge(etPassword2, ivSeePassword2);
        }
    }

    /**
     * 拨打客服电话
     */
    @Override
    protected void onClickRightImg() {
        new CallPhoneUtils().callCustomerService(this);
    }

    /**
     * 接收传过来的手机号
     */
    public void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            phoneNumber = bundle.getString("phoneNumber");
        }
    }
}
