package com.topjet.common.user.view.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.topjet.common.R;
import com.topjet.common.R2;
import com.topjet.common.base.view.activity.IView;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.user.modle.serverAPI.UserCommand;
import com.topjet.common.user.modle.serverAPI.UserCommandAPI;
import com.topjet.common.user.presenter.ChangePasswordPresenter;
import com.topjet.common.utils.CallPhoneUtils;
import com.topjet.common.utils.EditTextGangedUtils;
import com.topjet.common.utils.StringUtils;
import com.topjet.common.widget.MyTitleBar;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 修改密码
 * Created by yy on 2017/8/15.
 */

public class ChangePasswordActivity extends MvpActivity<ChangePasswordPresenter> implements IView {
    @BindView(R2.id.et_password)
    EditText etPassword;
    @BindView(R2.id.iv_clear)
    ImageView ivClear;
    @BindView(R2.id.tv_confirm)
    TextView tvConfirm;

    @Override
    public void initTitle() {
        getMyTitleBar()
                .setMode(MyTitleBar.Mode.BACK_TITLE_RIMG)
                .setTitleText("修改密码")
                .setRightImg(R.drawable.nav_icon_customerservice_nor_black);
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ChangePasswordPresenter(this, this, new UserCommand(UserCommandAPI.class, this));
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_change_password;
    }

    @Override
    protected void initView() {
        resetBtnBackGround();
        EditTextGangedUtils.setEtLengthWithDelete((MvpActivity) mContext, etPassword, ivClear);
    }

    /**
     * 设置按钮背景色
     */
    private void resetBtnBackGround() {
        if (CMemoryData.isDriver()) {
            tvConfirm.setBackgroundResource(R.drawable.selector_btn_corner_blue);
        }
    }

    /**
     * 拨打客服电话
     */
    @Override
    protected void onClickRightImg() {
        new CallPhoneUtils().callCustomerService(this);
    }

    @OnClick({R2.id.iv_clear, R2.id.tv_confirm})
    public void onViewClicked(View view) {
        int i = view.getId();
        if (i == R.id.iv_clear) {
            etPassword.setText("");
        } else if (i == R.id.tv_confirm) {
            if (StringUtils.isEmpty(etPassword.getText().toString())) {
                showToast("请输入旧密码");
            } else {
                mPresenter.checOldPassword(etPassword.getText().toString());
            }
        }
    }
}
