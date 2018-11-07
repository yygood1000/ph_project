package com.topjet.common.common.view.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.topjet.common.R;
import com.topjet.common.resource.bean.CityItem;

import java.util.List;

//选择 省的适配器
public class SelectLocalProvinceAdapter extends BaseQuickAdapter<CityItem, BaseViewHolder> {
	private CityItem firstLevel;

	public SelectLocalProvinceAdapter(@LayoutRes int layoutResId, @Nullable List<CityItem> data) {
		super(layoutResId, data);
	}

	public SelectLocalProvinceAdapter(@Nullable List<CityItem> data) {
		super(data);
	}

	public SelectLocalProvinceAdapter(@LayoutRes int layoutResId) {
		super(layoutResId);
	}

	public CityItem getFirstLevel() {
		return firstLevel;
	}

	public void setFirstLevel(CityItem firstLevel) {
		this.firstLevel = firstLevel;
	}

	@Override
	protected void convert(BaseViewHolder helper, CityItem item) {
		helper.setText(R.id.tv_city, item.getCityName());
	}
}
