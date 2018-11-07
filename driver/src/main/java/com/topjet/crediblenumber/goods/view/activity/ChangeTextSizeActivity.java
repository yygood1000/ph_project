package com.topjet.crediblenumber.goods.view.activity;

import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;

import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.config.CPersisData;
import com.topjet.common.widget.MyTitleBar;
import com.topjet.crediblenumber.R;
import com.topjet.crediblenumber.goods.presenter.ChangeTextSizePresenter;

import butterknife.BindView;

/**
 * Created by yy on 2017/9/14.
 * <p>
 * 字体大小切换 页面
 */

public class ChangeTextSizeActivity extends MvpActivity<ChangeTextSizePresenter> implements
        ChangeTextSizeView {

    @BindView(R.id.rl_layout_default)
    RelativeLayout rlLayoutDefault;
    @BindView(R.id.rl_layout_large)
    RelativeLayout rlLayoutLarge;
    @BindView(R.id.cb_text_size)
    CheckBox cbTextSize;

    @Override
    protected void initTitle() {
        getMyTitleBar().setMode(MyTitleBar.Mode.BACK_TITLE).setTitleText(getString(R.string.text_size_change));
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_change_text_size;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ChangeTextSizePresenter(this, mContext);
    }

    @Override
    public void initView() {
        cbTextSize.setChecked(CPersisData.getIsLargeGoodsItem());
        changeLayout(CPersisData.getIsLargeGoodsItem());
        cbTextSize.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                changeLayout(isChecked);
                CPersisData.setIsLargeGoodsItem(isChecked);
                CPersisData.setIsChangedGoodsItem(true);
            }
        });
    }

    @Override
    public void changeLayout(boolean isChecked) {
        if (isChecked) {
            rlLayoutDefault.setVisibility(View.GONE);
            rlLayoutLarge.setVisibility(View.VISIBLE);
        } else {
            rlLayoutDefault.setVisibility(View.VISIBLE);
            rlLayoutLarge.setVisibility(View.GONE);
        }
    }

}

