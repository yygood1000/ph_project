package com.topjet.common.user.view.activity;

import android.widget.ImageView;
import android.widget.TextView;

import com.topjet.common.R;
import com.topjet.common.R2;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.user.modle.bean.ReferrerInfoBean;
import com.topjet.common.user.modle.serverAPI.UserCommand;
import com.topjet.common.user.modle.serverAPI.UserCommandAPI;
import com.topjet.common.user.presenter.ReferrerPresenter;
import com.topjet.common.utils.GlideUtils;
import com.topjet.common.widget.MyTitleBar;

import butterknife.BindView;

/**
 * Created by yy on 2017/11/6.
 * 推荐人
 */

public class ReferrerActivity extends MvpActivity<ReferrerPresenter> implements ReferrerView {

    @BindView(R2.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R2.id.tv_name)
    TextView tvName;
    @BindView(R2.id.tv_mobile)
    TextView tvMobile;

    @Override
    protected void initTitle() {
        getMyTitleBar().setMode(MyTitleBar.Mode.BACK_TITLE).setTitleText("推荐人");
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ReferrerPresenter(this, mContext, new UserCommand(UserCommandAPI.class, this));
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_referrer;
    }


    @Override
    protected void initView() {
    }

    @Override
    protected void initData() {
        mPresenter.getReferrerInfo();
    }

    @Override
    public void showRefrererInfo(ReferrerInfoBean data) {
        tvName.setText(data.getRecommend_name());
        tvMobile.setText(data.getRecommended_mobile());
        GlideUtils.loaderImageCircle(mContext, data.getRecommend_icon_url(), data.getRecommend_icon_key(), R.drawable
                .icon_default_avatar, R.drawable.icon_default_avatar, ivAvatar);
    }
}
