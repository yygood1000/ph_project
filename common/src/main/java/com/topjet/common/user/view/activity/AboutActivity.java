package com.topjet.common.user.view.activity;

import android.widget.ImageView;
import android.widget.TextView;

import com.topjet.common.R;
import com.topjet.common.R2;
import com.topjet.common.base.view.activity.IView;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.utils.CallPhoneUtils;
import com.topjet.common.utils.SystemUtils;
import com.topjet.common.widget.MyTitleBar;

import butterknife.BindView;

/**
 * 修改头像
 * Created by tsj-004 on 2017/11/9.
 */

public class AboutActivity extends MvpActivity implements IView {
    @BindView(R2.id.iv_show_logo)
    ImageView ivShowLogo;
    @BindView(R2.id.tv_show_version)
    TextView tvShowVersion;

    @Override
    public void initTitle() {
        getMyTitleBar()
                .setMode(MyTitleBar.Mode.BACK_TITLE_RIMG)
                .setTitleText(R.string.about_us)
        .setRightImg(R.drawable.icon_call_customer_service);
    }

    @Override
    protected void initPresenter() {
    }

    @Override
    protected void onClickRightImg() {
        new CallPhoneUtils().callCustomerService(this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_about;
    }

    @Override
    protected void initView() {
        if (CMemoryData.isDriver()) {
            ivShowLogo.setImageResource(R.mipmap.ic_launcher_driver);
        } else {
            ivShowLogo.setImageResource(R.mipmap.ic_launcher_shipper);
        }

        tvShowVersion.setText(tvShowVersion.getText() + SystemUtils.getVersionName(this));
    }
}