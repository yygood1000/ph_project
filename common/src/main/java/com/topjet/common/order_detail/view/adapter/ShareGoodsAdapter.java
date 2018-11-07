package com.topjet.common.order_detail.view.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.topjet.common.R;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.order_detail.modle.bean.ShareGoodsBean;

/**
 * creator: zhulunjun
 * time:    2017/10/26
 * describe: 分享货源图片的货源列表的适配器
 */

public class ShareGoodsAdapter extends BaseQuickAdapter<ShareGoodsBean, BaseViewHolder> {
    public ShareGoodsAdapter() {
        super(R.layout.listitem_share_goods);
    }

    @Override
    protected void convert(BaseViewHolder helper, ShareGoodsBean item) {
        helper.setText(R.id.tv_depart_address, item.getDepart_city());
        helper.setText(R.id.tv_destination_address, item.getDestination_city());
        helper.setText(R.id.tv_order_type, item.getLoad_date());
        String orderInfo= item.getThe_goods()+" "+item.getTuck_length_type()+" "+item.getIs_freight_fee_managed();
        helper.setText(R.id.tv_order_info, orderInfo);
        ImageView imageView = helper.getView(R.id.iv_icon);
        if(CMemoryData.isDriver()){
            imageView.setImageResource(R.drawable.order_list_icon_blueline);
        }else {
            imageView.setImageResource(R.drawable.order_list_icon_greenline);
        }
    }
}
