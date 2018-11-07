package com.topjet.common.common.view.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.topjet.common.R;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.resource.bean.TruckLengthInfo;

import java.util.List;

// 车长适配器
public class TruckLengthAdapter extends BaseQuickAdapter<TruckLengthInfo, BaseViewHolder> {
	public TruckLengthAdapter(@LayoutRes int layoutResId, @Nullable List<TruckLengthInfo> data) {
		super(layoutResId, data);
	}

	public TruckLengthAdapter(@Nullable List<TruckLengthInfo> data) {
		super(data);
	}

	public TruckLengthAdapter(@LayoutRes int layoutResId) {
		super(layoutResId);
	}

	public TruckLengthAdapter() {
		super(R.layout.griditem_textview);
	}

	@Override
	protected void convert(BaseViewHolder helper, TruckLengthInfo item) {
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
