package com.topjet.common.common.view.adapter;

import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.topjet.common.R;
import com.topjet.common.resource.bean.OptionItem;

/**
 * Created by yy on 2017/7/31.
 * <p>
 * 简单筛选下拉列表弹窗适配器
 */

public class SimpleOptionsAdapter extends BaseQuickAdapter<OptionItem, BaseViewHolder> {
    @Override
    protected void convert(BaseViewHolder viewHolder, OptionItem item) {
        TextView tvContent = viewHolder.getView(R.id.tv_text);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(tvContent.getLayoutParams());
        params.gravity = Gravity.CENTER;
        tvContent.setLayoutParams(params);
        viewHolder.setText(R.id.tv_text, item.getName());
    }

    public SimpleOptionsAdapter() {
        super(R.layout.listitem_just_textview);
    }
}
