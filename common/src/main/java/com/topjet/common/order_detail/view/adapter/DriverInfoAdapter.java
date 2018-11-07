package com.topjet.common.order_detail.view.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.topjet.common.R;
import com.topjet.common.base.CommonProvider;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.order_detail.modle.bean.DriverInfo;
import com.topjet.common.utils.GlideUtils;
import com.topjet.common.utils.StringUtils;
import com.topjet.common.utils.TimeUtils;

/**
 * creator: zhulunjun
 * time:    2017/9/8
 * describe: 司机列表，货主版订单详情中，已查看过的司机列表
 */

public class DriverInfoAdapter extends BaseQuickAdapter<DriverInfo, BaseViewHolder> {
    public DriverInfoAdapter() {
        super(R.layout.listitem_driver_info);
    }

    @Override
    protected void convert(BaseViewHolder helper, final DriverInfo item) {
        if (item == null) {
            return;
        }
        helper.setText(R.id.tv_user_name, item.getDriver_name());
        String orderNumStr = mContext.getString(R.string.order_num_str);
        helper.setText(R.id.tv_order_num, String.format(orderNumStr, item.getClinch_a_deal_sum()));

        ImageView ivAvatar = helper.getView(R.id.iv_user_avatar);
        // 用户头像
        GlideUtils.loaderImageRound(mContext,
                item.getDriver_icon_url(),
                item.getDriver_icon_key(),
                R.drawable.shape_avatar_loading,
                R.drawable.shape_avatar_loading,
                ivAvatar,
                4);
        RatingBar ratingBar = helper.getView(R.id.rb_score);
        ratingBar.setRating(item.getDriver_comment_level());
        helper.getView(R.id.tv_time).setVisibility(View.VISIBLE);
        helper.setText(R.id.tv_time, TimeUtils.displayTime(item.getUpdate_time()));
        if(StringUtils.isEmpty(item.getTruck_length()) && StringUtils.isEmpty(item.getTruck_type())){
            helper.setText(R.id.tv_truck_info, "暂未添加车辆");
        } else {
            helper.setText(R.id.tv_truck_info, item.getTruck_type() + " " + item.getTruck_length());
        }

        helper.getView(R.id.iv_user_avatar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonProvider.getInstance().getJumpShipperProvider().jumpUserInfo((MvpActivity) mContext,
                        item.getDriver_mobile());
            }
        });

        helper.getView(R.id.iv_call).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (driverInfoClick != null) {
                    driverInfoClick.callClick(v, item);
                }
            }
        });

        helper.getView(R.id.iv_message).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (driverInfoClick != null) {
                    driverInfoClick.messageClick(v, item);
                }
            }
        });
    }

    private DriverInfoClick driverInfoClick;

    public void setDriverInfoClick(DriverInfoClick driverInfoClick) {
        this.driverInfoClick = driverInfoClick;
    }

    public interface DriverInfoClick {
        void callClick(View view, DriverInfo item);

        void messageClick(View view, DriverInfo item);
    }

}
