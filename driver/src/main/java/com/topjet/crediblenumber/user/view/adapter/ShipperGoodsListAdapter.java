package com.topjet.crediblenumber.user.view.adapter;


import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.topjet.common.common.modle.bean.UserInfoGoods;
import com.topjet.common.utils.StringUtils;
import com.topjet.common.utils.TimeUtils;
import com.topjet.crediblenumber.R;


/**
 * creator: zhulunjun
 * time:    2017/11/13
 * describe: 诚信查询，货主货源信息
 */

public class ShipperGoodsListAdapter extends BaseQuickAdapter<UserInfoGoods, BaseViewHolder> {
    public ShipperGoodsListAdapter() {
        super(R.layout.list_item_shipper_goods);
    }

    @Override
    protected void convert(BaseViewHolder helper, final UserInfoGoods item) {
        helper.setText(R.id.tv_depart_address, item.getDepart_city());
        helper.setText(R.id.tv_destination_address, item.getDestination_city());
        helper.setText(R.id.tv_time, TimeUtils.displayTime(item.getCreate_time()));
        helper.setText(R.id.tv_truck_info, item.getGoods_size() + " " + item.getTruck_length_type());


        if (StringUtils.isNotBlank(item.getPre_goods_id()) && !item.getPre_goods_id().equals("0")) {
            helper.getView(R.id.tv_btn_order).setVisibility(View.GONE);
            helper.getView(R.id.tv_btn_undo_quote).setVisibility(View.VISIBLE);
            helper.getView(R.id.tv_btn_update_quote).setVisibility(View.VISIBLE);
        } else {
            helper.getView(R.id.tv_btn_order).setVisibility(View.VISIBLE);
            helper.getView(R.id.tv_btn_undo_quote).setVisibility(View.GONE);
            helper.getView(R.id.tv_btn_update_quote).setVisibility(View.GONE);
        }

        helper.getView(R.id.ll_content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (goodsListClick != null) {
                    goodsListClick.contentClick(item);
                }
            }
        });

        helper.getView(R.id.tv_btn_order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (goodsListClick != null) {
                    goodsListClick.ordersClick(v, item);
                }
            }
        });
        helper.getView(R.id.tv_btn_undo_quote).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (goodsListClick != null) {
                    goodsListClick.undoQuoteClick(v, item);
                }
            }
        });
        helper.getView(R.id.tv_btn_update_quote).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (goodsListClick != null) {
                    goodsListClick.updateQuoteClick(v, item);
                }
            }
        });
    }


    private GoodsListClick goodsListClick;

    public void setGoodsListClick(GoodsListClick goodsListClick) {
        this.goodsListClick = goodsListClick;
    }

    public interface GoodsListClick {
        /**
         * item点击
         */
        void contentClick(UserInfoGoods item);

        /**
         * 接单点击
         */
        void ordersClick(View view, UserInfoGoods item);

        /**
         * 撤销订单
         */
        void undoQuoteClick(View view, UserInfoGoods item);

        /**
         * 修改订单
         */
        void updateQuoteClick(View view, UserInfoGoods item);
    }
}
