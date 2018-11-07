package com.topjet.crediblenumber.order.view.adapter;


import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.topjet.common.order_detail.modle.bean.MyOfferList;
import com.topjet.common.utils.TimeUtils;
import com.topjet.crediblenumber.R;


/**
 * creator: zhulunjun
 * time:    2017/9/12
 * describe: 我的报价适配器
 */

public class MyOfferAdapter extends BaseQuickAdapter<MyOfferList, BaseViewHolder> {
    public MyOfferAdapter() {
        super(R.layout.listitem_my_offer);
    }

    @Override
    protected void convert(BaseViewHolder helper, final MyOfferList item) {

        helper.setText(R.id.tv_depart_address, item.getDepart_city());
        helper.setText(R.id.tv_destination_address, item.getDestination_city());
        // 货物信息
        helper.setText(R.id.tv_goods_info, item.getGoods_size() +  item.getTruck_length_type());
        // 事件
        helper.setText(R.id.tv_delivery_time, TimeUtils.displayTime(item.getUpdate_time()));
        // 距离信息
        helper.setText(R.id.tv_distance_info, item.getDisteanceInfo());
        LinearLayout llDeposit = helper.getView(R.id.ll_deposit);
        helper.setText(R.id.tv_offer_cost, item.getTransport_fee());
        // 有定金才显示
        if (!TextUtils.isEmpty(item.getDeposit_fee())) {
            double deposit = Double.parseDouble(item.getDeposit_fee());
            if (deposit > 0) {
                helper.setText(R.id.tv_deposit, item.getDeposit_fee());
                llDeposit.setVisibility(View.VISIBLE);
            } else {
                llDeposit.setVisibility(View.GONE);
            }
        } else {
            llDeposit.setVisibility(View.GONE);
        }
        CheckBox checkBox = helper.getView(R.id.cb_check);
        checkBox.setChecked(item.isCheck());

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (myOfferClickListener != null) {
                    myOfferClickListener.checkClick(isChecked, item);
                }
            }
        });

        helper.getView(R.id.tv_btn_deposit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myOfferClickListener != null) {
                    myOfferClickListener.payDepositClick(v, item);
                }
            }
        });

        helper.getView(R.id.ll_offer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myOfferClickListener != null) {
                    myOfferClickListener.contentClick(v, item);
                }
            }
        });


    }

    private MyOfferClickListener myOfferClickListener;

    public void setMyOfferClickListener(MyOfferClickListener myOfferClickListener) {
        this.myOfferClickListener = myOfferClickListener;
    }

    public interface MyOfferClickListener {

        /**
         * 整体点击事件
         *
         * @param v
         * @param item
         */
        void contentClick(View v, MyOfferList item);

        /**
         * 选择回调
         *
         * @param check
         * @param item
         */
        void checkClick(boolean check, MyOfferList item);

        /**
         * 支付，修改 定金 点击回调
         *
         * @param item
         */
        void payDepositClick(View v, MyOfferList item);
    }

}
