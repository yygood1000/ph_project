package com.topjet.crediblenumber.user.view.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.topjet.common.base.listener.DebounceClickListener;
import com.topjet.common.common.modle.bean.UsualCityBean;
import com.topjet.crediblenumber.R;

/**
 * Created by  yy on 2017/8/16.
 * <p>
 * 常跑城市 列表适配器
 */

public class UsualCityAdapter extends BaseQuickAdapter<UsualCityBean, BaseViewHolder> {

    private OnClickListener mListener;

    public interface OnClickListener {
        void onClickDelete(UsualCityBean item, int position);

        void onClickItem(UsualCityBean item, int position);
    }

    public void setOnClickListener(OnClickListener mListener) {
        this.mListener = mListener;
    }

    public UsualCityAdapter() {
        super(R.layout.listitem_usual_city);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final UsualCityBean item) {
        // 设置箭头是否显示
        helper.setVisible(R.id.iv_icon_arrows, (helper.getLayoutPosition() == 0));
        // 设置删除按钮是否显示
        helper.setVisible(R.id.iv_icon_delete, (helper.getLayoutPosition() != 0));
        // 设置分割线是否显示
        helper.setVisible(R.id.parding_line, helper.getLayoutPosition() != (mData.size() - 1));

        helper.setText(R.id.tv_usual_city, item.getBusinessLineCityName());

        helper.getView(R.id.rl_root).setOnClickListener(new DebounceClickListener() {
            @Override
            public void performClick(View v) {
                mListener.onClickItem(item, helper.getLayoutPosition());
            }
        });

        helper.getView(R.id.iv_icon_delete).setOnClickListener(new DebounceClickListener() {
            @Override
            public void performClick(View v) {
                mListener.onClickDelete(item, helper.getLayoutPosition());
            }
        });
    }
}