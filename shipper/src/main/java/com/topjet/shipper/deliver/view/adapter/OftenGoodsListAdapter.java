package com.topjet.shipper.deliver.view.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.topjet.shipper.R;
import com.topjet.shipper.deliver.modle.bean.OftenGoodsListItem;

/**
 * 常发货源适配器
 * Created by tsj004 on 2017/8/29.
 */

public class OftenGoodsListAdapter extends BaseQuickAdapter<OftenGoodsListItem, BaseViewHolder> {

    @Override
    protected void convert(BaseViewHolder viewHolder, OftenGoodsListItem item) {
        if (item == null)
            return;

        ImageView iv = viewHolder.getView(R.id.iv_listitem_select);
        // 订单号
        viewHolder.setText(R.id.tv_orderno, item.getGoodsNo());

        // 发布时间
        viewHolder.setText(R.id.tv_time, item.getupdate_time());

        // 地址设置
        viewHolder.setText(R.id.tv_depart, item.getdepart_city());
        viewHolder.setText(R.id.tv_destination, item.getdestination_city());

        // 货物数量
        viewHolder.setText(R.id.tv_goods_info, item.getgoods_size());

        // 车型车长
        viewHolder.setText(R.id.tv_truck_info, "需"+item.gettruck_length_type());


        if (item.getIsSelect()) {
            iv.setBackgroundResource(R.drawable.approve_icon_check_green_sel);
        } else {
            iv.setBackgroundResource(R.drawable.approve_icon_uncheck_nor);
        }
        viewHolder.addOnClickListener(R.id.rl_goodsinfo);
    }

    public OftenGoodsListAdapter(Context context) {
        super(R.layout.listitem_often_goods);
    }

}
