package com.hyphenate.easeui.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.amap.api.services.help.Tip;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.topjet.common.R;

import java.util.List;

// poi地址适配器
public class PoiAddressAdapter extends BaseQuickAdapter<Tip, BaseViewHolder> {

	public PoiAddressAdapter(@LayoutRes int layoutResId, @Nullable List<Tip> data) {
		super(layoutResId, data);
	}

	public PoiAddressAdapter(@Nullable List<Tip> data) {
		super(data);
	}

	public PoiAddressAdapter(@LayoutRes int layoutResId) {
		super(layoutResId);
	}

	@Override
	protected void convert(BaseViewHolder helper, Tip item) {
		helper.setText(R.id.tv_text, item.getName());
	}
}
