package com.topjet.crediblenumber.goods.view.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.common.modle.bean.GoodsListData;
import com.topjet.common.common.view.activity.SimpleWebViewActivity;
import com.topjet.common.config.CPersisData;
import com.topjet.common.order_detail.modle.bean.OwnerInfo;
import com.topjet.common.utils.CallPhoneUtils;
import com.topjet.common.utils.CheckUserStatusUtils;
import com.topjet.common.utils.GlideUtils;
import com.topjet.common.utils.LayoutUtils;
import com.topjet.common.utils.ScreenUtils;
import com.topjet.common.utils.StringUtils;
import com.topjet.common.utils.TimeUtils;
import com.topjet.crediblenumber.R;
import com.topjet.crediblenumber.goods.view.dialog.BidOrAlterDialog;
import com.topjet.crediblenumber.order.view.activity.OrderDetailActivity;

/**
 * Created by yy on 2017/7/31.
 * <p>
 * 货源列表 适配器
 */

public class GoodsListAdapter extends BaseQuickAdapter<GoodsListData, BaseViewHolder> {
    private MvpActivity mActivity;
    private BidOrAlterDialog.OnPayForDepositResultListener onPayForDepositResultListener;

    public String biddingGoodsId;// 正在报价的货源Id

    public GoodsListAdapter(MvpActivity mvpActivity, boolean isAllowLargeLayout) {
        super((CPersisData.getIsLargeGoodsItem() && isAllowLargeLayout) ? R.layout.listitem_goods_list_large : R
                .layout.listitem_goods_list);
        this.mActivity = mvpActivity;
    }


    public void setOnPayForDepositResultListener(BidOrAlterDialog.OnPayForDepositResultListener
                                                         onPayForDepositResultListener) {
        this.onPayForDepositResultListener = onPayForDepositResultListener;
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, final GoodsListData item) {

        // 显示广告信息
        if (item.getAdvInfo() != null) {
            viewHolder.setVisible(R.id.iv_adv, true);
            viewHolder.setVisible(R.id.rl_goods_info, false);
            ImageView iv = viewHolder.getView(R.id.iv_adv);
            GlideUtils.loaderImage(item.getAdvInfo().getPicture_url(), item.getAdvInfo().getPicture_key(),
                    R.drawable.iv_banner_loading, R.drawable.iv_banner_loading, iv);

            // 广告点击事件
            viewHolder.getView(R.id.iv_adv).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (StringUtils.isNotBlank(item.getAdvInfo().getTurn_url())) {
                        // 进入广告显示页面
                        SimpleWebViewActivity.turnToSimpleWebViewActivity(mActivity, item.getAdvInfo()
                                        .getTurn_url(), item.getAdvInfo().getWeb_title());
                    }
                }
            });
        } else {
            viewHolder.setVisible(R.id.iv_adv, false);
            viewHolder.setVisible(R.id.rl_goods_info, true);

            // 设置地址
            viewHolder.setText(R.id.tv_depart_address, item.getDepart_city());
            viewHolder.setText(R.id.tv_destation_address, item.getDestination_city());

            // 设置货源更新时间
            viewHolder.setText(R.id.tv_delivery_time, TimeUtils.displayTime(item.getUpdate_time()));

            // 展示货源类型
            viewHolder.setText(R.id.tv_goods_info, item.getThe_goods() + " " + item.getTuck_length_type() + " " +
                    item.getIs_carpool());

            // 展示货主信息
            setOwnerInfo(viewHolder, item.getOwner_info());

            // 判断是否已经看过改货源
            if (item.getIs_examine()) {
                LayoutUtils.setAllItem(mContext, (ViewGroup) viewHolder.getView(R.id.rl_root), R.color
                        .text_color_969696);
            } else {
                LayoutUtils.setAllItem(mContext, (ViewGroup) viewHolder.getView(R.id.rl_root), R.color
                        .text_color_222222);
                LayoutUtils.setTextColorByResource((TextView) viewHolder.getView(R.id.tv_goods_info), R.color
                        .text_color_666666);
                LayoutUtils.setTextColorByResource((TextView) viewHolder.getView(R.id.tv_delivery_time), R.color
                        .text_color_666666);
            }

            // 已经拨打过电话的订单，拨打按钮背景变更
            if (item.getIs_call()) {
                viewHolder.setBackgroundRes(R.id.iv_call_phone, R.drawable.iv_icon_called_up);
            } else {
                viewHolder.setBackgroundRes(R.id.iv_call_phone, R.drawable.iv_icon_call_up);
            }

            // 设置好货节活动图片
            ImageView ivActivity = viewHolder.getView(R.id.iv_activity);
            if (StringUtils.isNotBlank(item.getIcon_image_url()) && StringUtils.isNotBlank(item.getIcon_image_key())) {
                ivActivity.setVisibility(View.VISIBLE);
                GlideUtils.loaderImage(item.getIcon_image_url(), item.getIcon_image_key(),
                        R.drawable.shape_avatar_loading, R.drawable.shape_avatar_loading, ivActivity);
            } else {
                ivActivity.setVisibility(View.GONE);
            }

            // 添加点击事件
            viewHolder.getView(R.id.iv_bid).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    // 跳转报价列表页面
                    CheckUserStatusUtils.isRealNameAndTruckApproved(mActivity, new CheckUserStatusUtils
                            .OnJudgeResultListener() {
                        @Override
                        public void onSucceed() {
                            biddingGoodsId = item.getGoods_id();
                            new BidOrAlterDialog(mActivity).showBidView(item.getGoods_id(), item.getGoods_version(),
                                    onPayForDepositResultListener);
                        }
                    });
                }
            });

            // 拨打电话
            viewHolder.getView(R.id.iv_call_phone).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    CheckUserStatusUtils.isRealNameAuthentication(mActivity,
                            new CheckUserStatusUtils.OnJudgeResultListener() {
                                @Override
                                public void onSucceed() {
                                    new CallPhoneUtils().showCallDialogWithAdvUpload(mActivity, v, item.getOwner_info
                                                    ().getOwner_name(), item.getOwner_info().getOwner_mobile(), 0, item
                                                    .getGoods_id(), item.getGoods_status(),
                                            new CallPhoneUtils.CallPhoneSucceedListener() {
                                                @Override
                                                public void isSucceed(boolean isSucceed) {
                                                    if (isSucceed) {
                                                        item.setIs_call("1");
                                                        notifyDataSetChanged();
                                                    }
                                                }
                                            });
                                }
                            });
                }
            });

            // 全局点击事件
            viewHolder.getView(R.id.rl_root).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item.getAdvInfo() != null) {
                        // 进入广告显示页面
                        SimpleWebViewActivity.turnToSimpleWebViewActivity(mActivity, item.getAdvInfo().getTurn_url(),
                                item.getAdvInfo().getWeb_title());
                    } else {
                        item.setIs_examine("1");
                        notifyDataSetChanged();
                        //  跳转货源详情页面
                        OrderDetailActivity.toGoodsDetail((MvpActivity) mContext, item.getGoods_id());
                    }
                }
            });

        }
    }

    /**
     * 展示货主信息
     */
    private void setOwnerInfo(BaseViewHolder viewHolder, final OwnerInfo ownerInfo) {
        ImageView iv = viewHolder.getView(R.id.iv_avatar);

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
                GlideUtils.loaderImageRoundWithSize(mContext, iconUrl, iconKey, R.drawable.iv_avatar_man, R
                        .drawable.shape_avatar_loading, iv, 4, ScreenUtils.dp2px(mActivity, 35), ScreenUtils.dp2px
                        (mActivity, 35));
            }

            // 设置货主姓名
            viewHolder.setText(R.id.tv_shipper_name, ownerInfo.getOwner_name());

            // 设置星级评价
            viewHolder.setRating(R.id.rating_bar, ownerInfo.getOwner_comment_level());

            // 这里不跳转
//            iv.setOnClickListener(new DebounceClickListener() {
//                @Override
//                public void performClick(View v) {
//                    // 跳转个人主页
//                    if (ownerInfo.getIs_anonymous()) {
//                        Toaster.showShortToast("对方设置了匿名，无法查看主页。");
//                    } else {
//                        mActivity.turnToActivity(UserInfoActivity.class, new PhoneExtra(ownerInfo.getOwner_mobile()));
//                    }
//                }
//            });
        }
    }
}
