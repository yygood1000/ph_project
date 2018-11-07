package com.hyphenate.easeui.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.amap.api.services.core.PoiItem;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.topjet.common.R;

import java.util.List;

// IM附近 poi地址适配器
public class IMNearPoiAddressAdapter extends BaseQuickAdapter<PoiItem, BaseViewHolder> {
    private String curItemId = null;

    public IMNearPoiAddressAdapter(@LayoutRes int layoutResId, @Nullable List<PoiItem> data) {
        super(layoutResId, data);
    }

    public IMNearPoiAddressAdapter(@Nullable List<PoiItem> data) {
        super(data);
    }

    public IMNearPoiAddressAdapter(@LayoutRes int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, PoiItem item) {
        helper.setText(R.id.tv_name, item.getTitle());
        helper.setText(R.id.tv_address, item.getSnippet());
        if (item.getPoiId().equals(curItemId)) {
            helper.setVisible(R.id.iv_select, true);
        } else {
            helper.setVisible(R.id.iv_select, false);
        }
    }

    public String getCurItemId() {
        return curItemId;
    }

    public void setCurItemId(String curItemId) {
        this.curItemId = curItemId;
    }
}
