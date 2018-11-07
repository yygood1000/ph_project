package com.topjet.shipper.deliver.view.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.topjet.shipper.R;
import com.topjet.shipper.deliver.modle.bean.UnitAndState;

import java.util.List;

/**
 * 发货单位适配器
 * Created by tsj-004 on 2017/9/1.
 */

public class UnitAdapter extends BaseQuickAdapter<UnitAndState, BaseViewHolder> {
    @Override
    protected void convert(BaseViewHolder viewHolder, UnitAndState item) {
        if (item == null)
            return;

        viewHolder.setText(R.id.tv_unit, item.getItem().getName());
        // 0 绿底 选中  1 绿底 未选中 2 白底 选中 3 白底 未选中
        int state = item.getState();
        if (state == 0) {
            viewHolder.setBackgroundRes(R.id.tv_unit, R.drawable.bg_unit_item_02);
            viewHolder.setTextColor(R.id.tv_unit, mContext.getResources().getColor(R.color.text_color_16CA4E));
        } else if (state == 1) {
            viewHolder.setBackgroundRes(R.id.tv_unit, R.drawable.bg_unit_item_01);
            viewHolder.setTextColor(R.id.tv_unit, mContext.getResources().getColor(R.color.white));
        } else if (state == 2) {
            viewHolder.setBackgroundRes(R.id.tv_unit, R.drawable.bg_unit_item_03);
            viewHolder.setTextColor(R.id.tv_unit, mContext.getResources().getColor(R.color.white));
        } else if (state == 3) {
            viewHolder.setBackgroundRes(R.id.tv_unit, R.drawable.bg_unit_item_04);
            viewHolder.setTextColor(R.id.tv_unit, mContext.getResources().getColor(R.color.text_color_16CA4E));
        }
    }

    public UnitAdapter(@LayoutRes int layoutResId, @Nullable List<UnitAndState> data) {
        super(layoutResId, data);
    }

    public UnitAdapter(@Nullable List<UnitAndState> data) {
        super(data);
    }

    public UnitAdapter(@LayoutRes int layoutResId) {
        super(layoutResId);
    }

    public UnitAdapter() {
        super(R.layout.item_unit);
    }
}
