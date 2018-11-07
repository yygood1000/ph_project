package com.topjet.common.common.view.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.topjet.common.R;
import com.topjet.common.resource.bean.CityItem;

import java.util.List;

//选择 市 的适配器
public class SelectLocalCityAdapter extends BaseQuickAdapter<CityItem, BaseViewHolder> {
	private CityItem mSecondLevel;

	public SelectLocalCityAdapter(@LayoutRes int layoutResId, @Nullable List<CityItem> data) {
		super(layoutResId, data);
	}

	public SelectLocalCityAdapter(@Nullable List<CityItem> data) {
		super(data);
	}

	public SelectLocalCityAdapter(@LayoutRes int layoutResId) {
		super(layoutResId);
	}

	public CityItem getSecondLevel(){
		return mSecondLevel;
	}

	public void setSecondLevel(CityItem secondLevel){
		this.mSecondLevel = secondLevel;
	}


	@Override
	protected void convert(BaseViewHolder helper, CityItem item) {
		helper.setText(R.id.tv_city, item.getCityName());
	}
}