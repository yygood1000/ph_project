package com.topjet.common.common.view.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.topjet.common.R;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.resource.bean.EntiretyInfo;

import java.util.List;

// 整车或拼车适配器
public class EntiretyAdapter extends BaseQuickAdapter<EntiretyInfo, BaseViewHolder> {
	public EntiretyAdapter(@LayoutRes int layoutResId, @Nullable List<EntiretyInfo> data) {
		super(layoutResId, data);
	}

	public EntiretyAdapter(@Nullable List<EntiretyInfo> data) {
		super(data);
	}

	public EntiretyAdapter(@LayoutRes int layoutResId) {
		super(layoutResId);
	}

	public EntiretyAdapter() {
		super(R.layout.griditem_textview);
	}

	@Override
	protected void convert(BaseViewHolder helper, EntiretyInfo item) {
		helper.setText(R.id.tv_text, item.getDisplayName());

		if(item.isSelected())
		{
			if(CMemoryData.isDriver())
			{
				helper.setBackgroundRes(R.id.tv_text, R.drawable.bg_truck_len_type_item_is_select_blue);
			} else {
				helper.setBackgroundRes(R.id.tv_text, R.drawable.bg_truck_len_type_item_is_select_green);
			}
			helper.setTextColor(R.id.tv_text, mContext.getResources().getColor(R.color.white));
		} else {
			helper.setBackgroundRes(R.id.tv_text, R.drawable.bg_truck_len_type_item_no_select);
			helper.setTextColor(R.id.tv_text, mContext.getResources().getColor(R.color.text_color_222222));
		}
	}
}
