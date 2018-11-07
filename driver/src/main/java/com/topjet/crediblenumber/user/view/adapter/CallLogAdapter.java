package com.topjet.crediblenumber.user.view.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.topjet.common.base.CommonProvider;
import com.topjet.common.base.dialog.AutoDialog;
import com.topjet.common.base.listener.DebounceClickListener;
import com.topjet.common.base.listener.ObserverOnResultListener;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.order_detail.modle.bean.OwnerInfo;
import com.topjet.common.order_detail.modle.params.GoodsIdParams;
import com.topjet.common.order_detail.modle.response.ValidGoodsResponse;
import com.topjet.common.order_detail.modle.serverAPI.OrderDetailCommand;
import com.topjet.common.order_detail.modle.serverAPI.OrderDetailCommandApi;
import com.topjet.common.utils.CallPhoneUtils;
import com.topjet.common.utils.GlideUtils;
import com.topjet.common.utils.ResourceUtils;
import com.topjet.common.utils.ScreenUtils;
import com.topjet.common.utils.TimeUtils;
import com.topjet.crediblenumber.R;
import com.topjet.crediblenumber.order.view.activity.OrderDetailActivity;
import com.topjet.crediblenumber.user.modle.bean.CallLogData;

/**
 * Created by  yy on 2017/8/16.
 * 通话记录 列表适配器
 * <p>
 * Changed by tsj-004 on 2017/11/13.
 * GoodsListData 换成 CallLogData
 */

public class CallLogAdapter extends BaseQuickAdapter<CallLogData, BaseViewHolder> {
    private MvpActivity mActivity;
    private OrderIsUnEnableListener orderIsUnEnableListener = null;

    public CallLogAdapter(MvpActivity mvpActivity) {
        super(R.layout.item_call_log_list);
        this.mActivity = mvpActivity;
    }

    public void setOrderIsUnEnableListener(OrderIsUnEnableListener orderIsUnEnableListener) {
        this.orderIsUnEnableListener = orderIsUnEnableListener;
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, CallLogData item) {
        // 设置地址
        viewHolder.setText(R.id.tv_depart_address, item.getDepart_city());
        viewHolder.setText(R.id.tv_destation_address, item.getDestination_city());

        // 设置拨打电话时间
        viewHolder.setText(R.id.tv_delivery_time, TimeUtils.displayTime(item.getCreate_time()));

        // 展示货源类型
        viewHolder.setText(R.id.tv_goods_info, item.getThe_goods() + " " + item.getTuck_length_type());

        // 展示货主头像
        ImageView iv = viewHolder.getView(R.id.iv_avatar);
        OwnerInfo ownerInfo = item.getOwner_info();
        // 展示货主信息
        if (ownerInfo != null) {
            if (ownerInfo.getIs_anonymous()) {
                if (ownerInfo.getIsMan()) {
                    GlideUtils.loaderImageResource(R.drawable.iv_avatar_man, iv);
                } else {
                    GlideUtils.loaderImageResource(R.drawable.iv_avatar_woman, iv);
                }
            } else {
                String iconUrl = ownerInfo.getOwner_icon_url();
                String iconKey = ownerInfo.getOwner_icon_key();
                GlideUtils.loaderImageRoundWithSize(mContext, iconUrl, iconKey, R.drawable
                        .icon_call_log_default_avatar, R
                        .drawable.icon_call_log_default_avatar, iv, 4, ScreenUtils.dp2px(mActivity, 35), ScreenUtils
                        .dp2px(mActivity, 35));
            }

            // 设置货主姓名
            viewHolder.setText(R.id.tv_shipper_name, ownerInfo.getOwner_name());

            // 添加点击事件
            ImageView ivBid = viewHolder.getView(R.id.iv_chat);
            ivBid.setTag(item);
            ivBid.setOnClickListener(clickListener);

            ImageView ivCallPhone = viewHolder.getView(R.id.iv_call_phone);
            ivCallPhone.setTag(item);
            ivCallPhone.setOnClickListener(clickListener);

            RelativeLayout rl_root = viewHolder.getView(R.id.rl_root);

            // 判断是否已经看过改货源
            if (item.getIs_examine()) {
                rl_root.setBackgroundColor(ResourceUtils.getColor(R.color.transparent_95));//设置列表项变化
            } else {
                rl_root.setBackgroundColor(ResourceUtils.getColor(R.color.white));//设置列表项变化
            }

            rl_root.setTag(item);
            rl_root.setOnClickListener(clickListener);

            // 设置星级评价
            viewHolder.setRating(R.id.rating_bar, ownerInfo.getOwner_comment_level());

            // 已经拨打过电话的订单，拨打按钮背景变更
            if (item.getIs_call()) {
                ivCallPhone.setBackgroundResource(R.drawable.iv_icon_called_up);
            } else {
                ivCallPhone.setBackgroundResource(R.drawable.iv_icon_call_up);
            }
        }
    }

    private DebounceClickListener clickListener = new DebounceClickListener() {
        @Override
        public void performClick(final View v) {
            final int id = v.getId();
            final CallLogData data = (CallLogData) v.getTag();
            new OrderDetailCommand(OrderDetailCommandApi.class, mActivity)
                    .selectValidGoods(new GoodsIdParams(data.getGoods_id(), data.getGoods_version()), new
                            ObserverOnResultListener<ValidGoodsResponse>() {
                                @Override
                                public void onResult(ValidGoodsResponse response) {
                                    // 1.有效 2.失效
                                    if (response.isVallid()) {
                                        processResult(id, v, data);
                                    } else {
                                        showFailDialog();
                                    }
                                }
                            });
        }
    };

    /**
     * 订单有效，处理结果
     */
    private void processResult(int id, View v, final CallLogData data) {
        switch (id) {
            case R.id.iv_chat:
                // 跳转聊天
                CommonProvider.getInstance().getJumpChatPageProvider().jumpChatPage(mContext, data.getOwner_info()
                        .getIMUserInfo(data.getOwner_info()));
                break;
            case R.id.iv_call_phone:
                new CallPhoneUtils().showCallDialogWithAdvUpload(mActivity, v,
                        data.getOwner_info().getOwner_name(),
                        data.getOwner_info().getOwner_mobile(),
                        0,
                        data.getGoods_id(),
                        data.getGoods_status(),
                        new CallPhoneUtils.CallPhoneSucceedListener() {
                            @Override
                            public void isSucceed(boolean isSucceed) {
                                if (isSucceed) {
                                    data.setIs_call("1");
                                    notifyDataSetChanged();
                                }
                            }
                        });
                break;
            case R.id.rl_root:
                data.setIs_examine("1");
                notifyDataSetChanged();
                //  跳转货源详情页面
                OrderDetailActivity.toGoodsDetail((MvpActivity)
                        mContext, data
                        .getGoods_id());
        }
    }

    /**
     * 展示订单失效弹窗
     */
    private void showFailDialog() {
        final AutoDialog alertDialog = new AutoDialog(mContext);
        alertDialog.setTitle("很可惜，该订单已成交/撤销");
        alertDialog.setOnSingleConfirmListener(new AutoDialog
                .OnSingleConfirmListener
                () {
            @Override
            public void onClick() {
                alertDialog.toggleShow();
                if (orderIsUnEnableListener != null) {
                    orderIsUnEnableListener.unable();
                }
            }
        });
        alertDialog.toggleShow();
    }

    /**
     * 失效弹窗按钮点击事件
     */
    public interface OrderIsUnEnableListener {
        void unable();
    }
}