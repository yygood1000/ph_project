package com.topjet.crediblenumber.car.view.adapter;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.topjet.common.utils.GlideUtils;
import com.topjet.common.utils.ResourceUtils;
import com.topjet.common.utils.StringUtils;
import com.topjet.crediblenumber.R;
import com.topjet.crediblenumber.car.modle.bean.TruckTeamCar;

/**
 * Created by tsj-004 on 2017/10/16.
 * <p>
 * 我的车队 适配器
 */

public class MyFleetAdapter extends BaseQuickAdapter<TruckTeamCar, BaseViewHolder> {

    public MyFleetAdapter() {
        super(R.layout.item_my_fleet);
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, final TruckTeamCar item) {
        final ImageView ivCarPhoto = viewHolder.getView(R.id.iv_car_photo);
        ivCarPhoto.setImageResource(R.drawable.icon_car_default);
//        ivCarPhoto.post(new Runnable() {
//            @Override
//            public void run() {
                if(StringUtils.isNotBlank(item.getTruck_icon_url()) && StringUtils.isNotBlank(item.getTruck_icon_key())){
                    GlideUtils.loaderImageRound(mContext,
                            item.getTruck_icon_url(),
                            item.getTruck_icon_key(),
                            R.drawable.icon_car_default,
                            R.drawable.icon_car_loading,
                            ivCarPhoto, 4);
            }
//        });
        CheckBox cbCarWorkStatus = viewHolder.getView(R.id.cb_car_work_status);     // 工作状态  1 求货 2 休息

        // 工作状态
        if (item.getTruck_status().equals("1")) {
            cbCarWorkStatus.setChecked(true);
        } else {
            cbCarWorkStatus.setChecked(false);
        }

        String plateColor = item.getPlate_color();      // 车牌颜色 1 蓝色 2 黄色
        TextView tvCarNumber = viewHolder.getView(R.id.tv_car_number);      // 车牌号和车牌颜色
        if (plateColor.equals("1")) {
            tvCarNumber.setBackgroundDrawable(ResourceUtils.getDrawable(R.drawable.shape_bg_btn_gradient_blue));
        } else {
            tvCarNumber.setBackgroundDrawable(ResourceUtils.getDrawable(R.drawable.shape_bg_btn_gradient_orange));
        }
        tvCarNumber.setText(item.getCarNumber());

        TextView tvCarInfo = viewHolder.getView(R.id.tv_car_info);      // 车型车长
        tvCarInfo.setText(item.getTypeLength());

        ImageView ivCarStatus = viewHolder.getView(R.id.iv_car_status);      // 认证状态
        String auditStatus = item.getAudit_status();        // 认证状态 0 无需认证  1 未认证 2 已认证 3 认证中 4 认证失败
        if (auditStatus.equals("0") || auditStatus.equals("1")) {
            ivCarStatus.setVisibility(View.GONE);
        } else {
            ivCarStatus.setVisibility(View.VISIBLE);

            if (auditStatus.equals("2")) {
                ivCarStatus.setImageResource(R.drawable.icon_car_status_adopt);
            } else if (auditStatus.equals("3")) {
                ivCarStatus.setImageResource(R.drawable.icon_car_status_ing);
            } else if (auditStatus.equals("4")) {
                ivCarStatus.setImageResource(R.drawable.icon_car_status_fail);
            }
        }

        TextView tvCarRemarks = viewHolder.getView(R.id.tv_car_remarks);      // 备注
        String name = item.getDriver_name();
        String phone = item.getDriver_mobile();
        if (name == null) {
            name = "";
        }
        if (phone == null) {
            phone = "";
        }
        tvCarRemarks.setText(StringUtils.format(R.string.truck_team_remarks, name, phone));
        if (StringUtils.isEmpty(name) && StringUtils.isEmpty(phone)) {
            tvCarRemarks.setVisibility(View.GONE);
        }

        viewHolder.addOnClickListener(R.id.cb_car_work_status);
    }
}
