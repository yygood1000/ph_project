package com.topjet.common.user.view.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.topjet.common.R;
import com.topjet.common.R2;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.user.modle.extra.PasswordExtra;
import com.topjet.common.user.modle.serverAPI.UserCommand;
import com.topjet.common.user.modle.serverAPI.UserCommandAPI;
import com.topjet.common.user.presenter.ChangePasswordInputPresenter;
import com.topjet.common.utils.CallPhoneUtils;
import com.topjet.common.utils.EditTextGangedUtils;
import com.topjet.common.utils.ResourceUtils;
import com.topjet.common.widget.MyTitleBar;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 修改密码 输入新密码页面
 * Created by yy on 2017/8/15.
 */

public class ChangePasswordInputActivity extends MvpActivity<ChangePasswordInputPresenter> implements
        ChangePasswordInputView {
    @BindView(R2.id.et_password_new)
    EditText etPasswordNew;
    @BindView(R2.id.iv_clear_new)
    ImageView ivClearNew;
    @BindView(R2.id.et_password_confirm)
    EditText etPasswordConfirm;
    @BindView(R2.id.iv_clear_confirm)
    ImageView ivClearConfirm;
    @BindView(R2.id.tv_confirm)
    TextView tvConfirm;
    public PasswordExtra extra;

    @Override
    public void initTitle() {
        getMyTitleBar()
                .setMode(MyTitleBar.Mode.BACK_TITLE_RIMG)
                .setTitleText(ResourceUtils.getString(R.string.change_password))
                .setRightImg(R.drawable.nav_icon_customerservice_nor_black);
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ChangePasswordInputPresenter(this, this, new UserCommand(UserCommandAPI.class, this));
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_change_password_input;
    }

    @Override
    protected void initView() {
        resetBtnBackGround();
        EditTextGangedUtils.setEtLengthWithDelete((MvpActivity) mContext, etPasswordNew, ivClearNew);
        EditTextGangedUtils.setEtLengthWithDelete((MvpActivity) mContext, etPasswordConfirm, ivClearConfirm);
    }

    @Override
    protected void initData() {
        // 从上级页面传入的旧密码
        extra = getIntentExtra(PasswordExtra.getExtraName());
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

    @OnClick({R2.id.iv_clear_new, R2.id.iv_clear_confirm, R2.id.tv_confirm})
    public void onViewClicked(View view) {
        int i = view.getId();
        if (i == R.id.iv_clear_new) {
            etPasswordNew.setText("");
        } else if (i == R.id.iv_clear_confirm) {
            etPasswordConfirm.setText("");
        } else if (i == R.id.tv_confirm) {
            // 完成提交
            mPresenter.changePassword(etPasswordNew.getText().toString(), etPasswordConfirm.getText().toString());
        }
    }

    @Override
    public void clearEtNewPassword() {
        etPasswordNew.setText("");
    }

    @Override
    public void clearEtConfirmPassword() {
        etPasswordConfirm.setText("");
    }
}
