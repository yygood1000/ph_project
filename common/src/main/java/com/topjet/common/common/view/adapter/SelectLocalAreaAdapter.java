package com.topjet.common.common.view.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.topjet.common.R;
import com.topjet.common.resource.bean.CityItem;

import java.util.List;

public class SelectLocalAreaAdapter extends BaseQuickAdapter<CityItem, BaseViewHolder> {

	public SelectLocalAreaAdapter(@LayoutRes int layoutResId, @Nullable List<CityItem> data) {
		super(layoutResId, data);
	}

	public SelectLocalAreaAdapter(@Nullable List<CityItem> data) {
		super(data);
	}

	public SelectLocalAreaAdapter(@LayoutRes int layoutResId) {
		super(layoutResId);
	}

	@Override
	protected void convert(BaseViewHolder helper, CityItem item) {
		helper.setText(R.id.tv_city, item.getCityName());
	}
}