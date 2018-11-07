package com.topjet.common.common.view.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.topjet.common.R;
import com.topjet.common.common.modle.bean.SearchAddressListItem;

/**
 * Created by yy on 2017/7/31.
 * <p>
 * 附近货源搜索页面 下拉列表 适配器
 */

public class SearchAddressAdapter extends BaseQuickAdapter<SearchAddressListItem, BaseViewHolder> {
    @Override
    protected void convert(BaseViewHolder viewHolder, SearchAddressListItem item) {

        if (item.isSearch()) {
            viewHolder.setImageResource(R.id.iv_icon, R.drawable.iv_icon_locat_address);
        } else {
            viewHolder.setImageResource(R.id.iv_icon, R.drawable.iv_icon_search_address);
        }

        viewHolder.setText(R.id.tv_content, item.getAddress());
    }


    public SearchAddressAdapter() {
        super(R.layout.listitem_seach);
    }
}
