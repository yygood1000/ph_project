package com.topjet.shipper.order.view.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.topjet.common.order_detail.modle.bean.ShowOfferList;
import com.topjet.common.utils.GlideUtils;
import com.topjet.common.utils.Logger;
import com.topjet.shipper.R;


/**
 * creator: zhulunjun
 * time:    2017/9/12
 * describe: 报价列表适配器
 */

public class ShowOfferAdapter extends BaseQuickAdapter<ShowOfferList, BaseViewHolder> {
    public ShowOfferAdapter() {
        super(R.layout.listitem_show_offer);
    }

    private ShowOfferList confirmedOffer = null; // 已经确认过的报价
    private OnShowOfferClickListener showOfferClick;

    /**
     * 获取确认成交对象报价实体类
     */
    public ShowOfferList getConfirmedOffer() {
        return confirmedOffer;
    }

    /**
     * 取消交易操作后需要清空该对象
     */
    public void clearConfirmOffer() {
        confirmedOffer = null;
    }

    /**
     * 列表涉及到的点击事件
     */
    public interface OnShowOfferClickListener {
        void contactClick(View v, ShowOfferList item);

        void callClick(View v, ShowOfferList item);

        void confirmClick(View v, ShowOfferList item);

        void cancelClick(View v, ShowOfferList item);

        void avatarClick(View v, ShowOfferList item);
    }


    public void setShowOfferClick(OnShowOfferClickListener showOfferClick) {
        this.showOfferClick = showOfferClick;
    }

    @Override
    protected void convert(BaseViewHolder helper, final ShowOfferList item) {
        ImageView ivUserAvatarDriver = helper.getView(R.id.iv_user_avatar_driver);
        TextView tvCall = helper.getView(R.id.tv_call);
        TextView tvConfirm = helper.getView(R.id.tv_confirm);
        if (item != null) {
            helper.setText(R.id.tv_user_name, item.getDriver_name());
            String orderNumStr = mContext.getString(com.topjet.common.R.string.order_num_str);
            helper.setText(R.id.tv_order_num, String.format(orderNumStr, item.getDriver_dealorder_count()));
            helper.setText(R.id.tv_truck_info, item.getPlate_no() + " | " + item.getDriver_truck_type() + " " + item
                    .getDriver_truck_length());
            helper.setText(R.id.tv_address, item.getDriver_gps_detail());
            helper.setText(R.id.tv_freight, item.getTransport_fee());
            // 用户头像
            GlideUtils.loaderImageRound(mContext,
                    item.getDriver_icon_url(),
                    item.getDriver_icon_key(),
                    com.topjet.common.R.drawable.shape_avatar_loading,
                    com.topjet.common.R.drawable.shape_avatar_loading,
                    ivUserAvatarDriver,
                    4);

            // 设置星级评价
            helper.setRating(R.id.rb_score, item.getDriver_comment_level());

            // 判断是确认成交，还是取消交易
            if (item.getPre_status().equals("1")) {
                tvConfirm.setText("确认成交");
                tvConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (showOfferClick != null) {
                            showOfferClick.confirmClick(v, item);
                        }
                    }
                });
            } else if (item.getPre_status().equals("2")) {
                confirmedOffer = item;
                Logger.d("ShowOfferAdapter " + item.getPre_status());
                tvConfirm.setText("取消交易");
                tvConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (showOfferClick != null) {
                            showOfferClick.cancelClick(v, item);
                        }
                    }
                });
            }
            tvCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (showOfferClick != null) {
                        showOfferClick.callClick(v, item);
                    }
                }
            });

            helper.getView(R.id.ll_offer).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (showOfferClick != null) {
                        showOfferClick.contactClick(v, item);
                    }
                }
            });

            ivUserAvatarDriver.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (showOfferClick != null) {
                        showOfferClick.avatarClick(v, item);
                    }
                }
            });
        }
    }

}
