package com.topjet.shipper.deliver.view.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.topjet.common.R;
import com.topjet.shipper.deliver.modle.bean.TypeAndPackingItem;

import java.util.List;

/**
 * 包装方式和货物类型
 */

public class TypeAndPackingAdapter extends BaseQuickAdapter<TypeAndPackingItem, BaseViewHolder> {
    public TypeAndPackingAdapter(@LayoutRes int layoutResId, @Nullable List<TypeAndPackingItem> data) {
        super(layoutResId, data);
    }

    public TypeAndPackingAdapter(@Nullable List<TypeAndPackingItem> data) {
        super(data);
    }

    public TypeAndPackingAdapter(@LayoutRes int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, TypeAndPackingItem item) {
        if (item.isFromUser()) {
            if (TextUtils.isEmpty(item.getItem().getName())) {
                helper.setVisible(R.id.tv_item_content, false);
                helper.setVisible(R.id.iv_from_user, false);
                helper.setVisible(R.id.iv_add, true);
                helper.addOnClickListener(R.id.iv_add);
            } else {
                helper.setVisible(R.id.iv_from_user, true);
                helper.setVisible(R.id.tv_item_content, true);
                helper.setVisible(R.id.iv_add, false);
                helper.addOnClickListener(R.id.iv_from_user);
                helper.setText(R.id.tv_item_content, item.getItem().getName());
            }
        } else {
            helper.setVisible(R.id.iv_from_user, false);
            helper.setVisible(R.id.tv_item_content, true);
            helper.setVisible(R.id.iv_add, false);
            helper.setText(R.id.tv_item_content, item.getItem().getName());
        }

        if (item.isSelected()) {
            helper.setBackgroundRes(R.id.ll_item_content, R.drawable.bg_pop_item_sel_shipper);
            helper.setTextColor(R.id.tv_item_content, mContext.getResources().getColor(R.color.white));
        } else {
            helper.setBackgroundRes(R.id.ll_item_content, R.drawable.bg_pop_item_def_shipper);
            helper.setTextColor(R.id.tv_item_content, mContext.getResources().getColor(R.color.text_color_222222));
        }
    }
}
