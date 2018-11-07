package com.topjet.shipper.user.view.activity;

import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.shipper.R;
import com.topjet.shipper.user.model.serviceAPI.ShipperCommand;
import com.topjet.shipper.user.model.serviceAPI.ShipperCommandAPI;
import com.topjet.shipper.user.presenter.AnonymousSetPresenter;

import butterknife.BindView;

/**
 * Created by yy on 2017/10/27.
 * <p>
 * 匿名设置
 */

public class AnonymousSetActivity extends MvpActivity<AnonymousSetPresenter> implements AnonymousSetView {
    @BindView(R.id.iv_is_anonymous)
    ImageView ivIsAnonymous;
    @BindView(R.id.cb_is_anonymous)
    CheckBox cbIsAnonymous;

    @Override
    protected void initPresenter() {
        mPresenter = new AnonymousSetPresenter(this, mContext, new ShipperCommand(ShipperCommandAPI.class, this));
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_anonymous_set;
    }

    @Override
    protected void initData() {
        mPresenter.getAnonymous();
    }

    @Override
    protected void initView() {
    }

    /**
     * 初始设置匿名开关
     */
    @Override
    public void setIsAnonymous(boolean b) {
        cbIsAnonymous.setChecked(b);
    }

    /**
     * 设置CheckBox 切换监听
     */
    @Override
    public void setCheckBoxListener() {
        cbIsAnonymous.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mPresenter.settingAnonymous("1");
                } else {
                    mPresenter.settingAnonymous("0");
                }
            }
        });
    }

    /**
     * 设置图片
     */
    @Override
    public void setImage() {
        if (cbIsAnonymous.isChecked()) {
            ivIsAnonymous.setBackgroundResource(R.drawable.iv_icon_anonymous);
        } else {
            ivIsAnonymous.setBackgroundResource(R.drawable.iv_icon_anonymous_close);
        }
    }
}
