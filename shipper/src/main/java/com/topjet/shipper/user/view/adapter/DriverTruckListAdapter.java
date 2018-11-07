package com.topjet.shipper.user.view.adapter;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.common.modle.bean.TruckInfo;
import com.topjet.common.utils.GlideUtils;
import com.topjet.common.utils.StringUtils;
import com.topjet.shipper.R;
import com.topjet.shipper.familiar_car.view.activity.TruckInfoActivity;


/**
 * creator: zhulunjun
 * time:    2017/11/13
 * describe: 诚信查询，司机车辆信息
 */

public class DriverTruckListAdapter extends BaseQuickAdapter<TruckInfo, BaseViewHolder> {
    public DriverTruckListAdapter() {
        super(R.layout.list_item_driver_truck_list);
    }

    @Override
    protected void convert(BaseViewHolder helper, final TruckInfo item) {
        TextView tvPlateNumber = helper.getView(R.id.tv_plate_number);
        // 车牌号 1 蓝色 2 黄色
        if (item.getPlate_color().equals("1")) {
            tvPlateNumber.setBackgroundResource(R.drawable.shape_blue_r2);
        } else {
            tvPlateNumber.setBackgroundResource(R.drawable.shape_yellow_r2);
        }
        tvPlateNumber.setText(item.getPlateNo());
        // 车辆信息
        helper.setText(R.id.tv_truck_info, item.getTruck_type_name() + " " + item.getTruck_length_name());
        // 车头照
        ImageView ivAvatar = helper.getView(R.id.iv_car);
        GlideUtils.loaderImageRound(mContext,
                item.getTruck_icon_url(),
                item.getTruck_icon_key(),
                R.drawable.icon_car_default,
                R.drawable.icon_car_loading,
                ivAvatar,
                4);
        // 已认证标识
        if(StringUtils.isNotBlank(item.getAudit_status()) && item.getAudit_status().equals(TruckInfo.AUTHENTICATED)){
            helper.getView(R.id.tv_label).setVisibility(View.VISIBLE);
        } else {
            helper.getView(R.id.tv_label).setVisibility(View.GONE);
        }
        // 地址
        if (StringUtils.isEmpty(item.getGps_address())) {
            helper.getView(R.id.tv_address).setVisibility(View.GONE);
            helper.getView(R.id.iv_location).setVisibility(View.GONE);
        } else {
            helper.getView(R.id.tv_address).setVisibility(View.VISIBLE);
            helper.getView(R.id.iv_location).setVisibility(View.VISIBLE);
            helper.setText(R.id.tv_address, item.getGps_address());
        }

        helper.getView(R.id.ll_content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TruckInfoActivity.truckInfo((MvpActivity) mContext, item.getTruck_id());
            }
        });

        helper.getView(R.id.iv_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 去车辆位置
                if(driverTruckListClick != null){
                    driverTruckListClick.locationClick(item);
                }
            }
        });

        helper.getView(R.id.iv_order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(driverTruckListClick != null){
                    driverTruckListClick.orderClick(item);
                }
            }
        });
    }

    private DriverTruckListClick driverTruckListClick;

    public void setDriverTruckListClick(DriverTruckListClick driverTruckListClick) {
        this.driverTruckListClick = driverTruckListClick;
    }

    public interface DriverTruckListClick{

        /**
         * 下单
         */
        void orderClick(TruckInfo item);

        /**
         * 车辆位置
         */
        void locationClick(TruckInfo item);
    }
}
