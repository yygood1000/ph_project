package com.topjet.crediblenumber.order.view.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.topjet.common.order_detail.modle.bean.MyOrderListItem;
import com.topjet.common.utils.GlideUtils;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.NumberFormatUtils;
import com.topjet.common.utils.TimeUtils;
import com.topjet.common.utils.Toaster;
import com.topjet.crediblenumber.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yy on 2017/7/31.
 * 司机版 订单详情适配器
 */

public class MyOrderListAdapter extends BaseQuickAdapter<MyOrderListItem, BaseViewHolder> {
    public interface OnBtnClickListener {
        // 呼叫货主
        void clickCallPhone(View v, String shipperName, String mobile);

        // 地图导航
        void clickNavigation(MyOrderListItem item);

        // 确认签收
        void clickConfirmSign(MyOrderListItem item);

        // 确认提货
        void clickConfirmPickUp(MyOrderListItem item);

        // 放弃承接
        void clickGiveUpUndertake(String order_id, String order_version);

        // 我要承接
        void clickDoUndertake(String goodsId, String orderId, String orderVersion);

        // 我要评价
        void clickComment(MyOrderListItem item);

        // 我要分享
        void clickShare(String orderId);

        void clickItem(MyOrderListItem item);

        // 查看回评
        void clickCheckComment(String orderId, String orderVersion);
    }


    private OnBtnClickListener onBtnClickListener;

    /**
     * 设置所有按钮的点击事件
     */
    public void setOnBtnClickListener(OnBtnClickListener onBtnClickListener) {
        this.onBtnClickListener = onBtnClickListener;
    }

    public MyOrderListAdapter(Context context) {
        super(R.layout.listitem_my_order);
        this.mContext = context;
    }


    @Override
    protected void convert(final BaseViewHolder viewHolder, final MyOrderListItem item) {
        if (item == null) {
            return;
        }

        setMyOrderDisplay(viewHolder, item);

    }

    /**
     * 我的订单的数据绑定
     *
     * @param viewHolder 视图
     * @param item       数据
     */
    private void setMyOrderDisplay(final BaseViewHolder viewHolder, final MyOrderListItem item) {
        // 地址信息
        if (item.getSender_info() != null) {
            viewHolder.setText(R.id.tv_depart_address, item.getSender_info().getCity());
        }
        if (item.getReceiver_info() != null) {
            viewHolder.setText(R.id.tv_destination_address, item.getReceiver_info().getCity());
        }
        // 展示与订单状态相关的UI
        setUIByStatus(viewHolder, item);
        // 设置提货时间
        viewHolder.setText(R.id.tv_pick_up_time, item.getPickup_start_time());
        // 展示费用相关信息
        setAboutFeeByDetail(viewHolder, item);
        // 订单更新时间
        viewHolder.setText(R.id.tv_delivery_time, TimeUtils.displayTime(item.getOrder_update_time()));
        // 设置按钮布局可见
        viewHolder.getView(R.id.rl_btn).setVisibility(View.VISIBLE);
        // 设置货主布局可见
        viewHolder.getView(R.id.ll_user_info).setVisibility(View.VISIBLE);
        // 设置货主相关信息
        setOwnerInfo(viewHolder, item);
        // 设置Item项点击事件
        viewHolder.getView(R.id.rl_order_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBtnClickListener.clickItem(item);
            }
        });
    }


    /**
     * 设置货主相关信息
     */
    private void setOwnerInfo(BaseViewHolder viewHolder, MyOrderListItem item) {
        // 货主相关信息
        viewHolder.setText(R.id.tv_shipper_name, item.getOwner_name());
        // 货主星级评价
        RatingBar rb = viewHolder.getView(R.id.rating_bar);
        rb.setRating(item.getOwner_comment_level());
        // 货主头像
        ImageView ivAvatar = viewHolder.getView(R.id.iv_avatar);
        GlideUtils.loaderImageRound(mContext, item.getOwner_icon_url(), item.getOwner_icon_key(), R.drawable
                .shape_avatar_loading, R.drawable.shape_avatar_loading, ivAvatar, 4);
    }

    /**
     * 设置订单状态标签内容
     */
    private void setUIByStatus(BaseViewHolder viewHolder, MyOrderListItem item) {
        int goodsStatus = NumberFormatUtils.strToInt(item.getGoods_status());// 货源状态
        int orderStatus = NumberFormatUtils.strToInt(item.getOrder_status());// 订单状态

        Button btnLeft = viewHolder.getView(R.id.btn_left);
        Button btnCenter = viewHolder.getView(R.id.btn_center);
        Button btnRight = viewHolder.getView(R.id.btn_right);

        switch (goodsStatus) {
            case 2: //TODO ** 生成订单，进行订单状态判断 **
                // 展示订单状态标签
                setLableByOrderStatus(viewHolder, orderStatus);

                // 根据状订单态展示按钮
                displayBtnByOrderStatus(btnLeft, btnCenter, btnRight, orderStatus);
                break;
            case 6: // 货源退款
                setBtnVisibleCount(btnLeft, btnCenter, btnRight, 2);
                viewHolder.setText(R.id.tv_order_status, "已退款");
                viewHolder.setBackgroundRes(R.id.tv_order_status, R.drawable.iv_icon_order_status_glay);
                btnLeft.setText("查看回评");
                btnRight.setText("我要分享");
                break;
        }

        // 设置点击事件
        setBtnClickListener(btnLeft, btnRight, btnCenter, goodsStatus, orderStatus, item);
    }

    /**
     * 展示订单状态标签
     */
    private void setLableByOrderStatus(BaseViewHolder viewHolder, int orderStatus) {
        switch (orderStatus) {
            case 1:// 货主取消交易.该状态订单不会出现在我的订单列表中
            case 3:// 待支付定金.该状态订单不会出现在我的订单列表中
                break;
            case 2:// 货主已确认成交
                viewHolder.setText(R.id.tv_order_status, "待承接");// 这里的“待确认”是货主点击了确认成交，待司机确认承接
                viewHolder.setBackgroundRes(R.id.tv_order_status, R.drawable.iv_icon_order_status_red);
                break;
            case 4:// 待支付运费
                viewHolder.setText(R.id.tv_order_status, "支付中");
                viewHolder.setBackgroundRes(R.id.tv_order_status, R.drawable.iv_icon_order_status_blue);
                break;
            case 5:// 提货中
                viewHolder.setText(R.id.tv_order_status, "提货中");
                viewHolder.setBackgroundRes(R.id.tv_order_status, R.drawable.iv_icon_order_status_blue);
                break;
            case 6:// 承运中
                viewHolder.setText(R.id.tv_order_status, "配送中");
                viewHolder.setBackgroundRes(R.id.tv_order_status, R.drawable.iv_icon_order_status_blue);
                break;
            case 7:// 已签收“待评价”
            case 8:// 货主已评价司机未评价
                viewHolder.setText(R.id.tv_order_status, "待评价");
                viewHolder.setBackgroundRes(R.id.tv_order_status, R.drawable.iv_icon_order_status_glay);
                break;
            case 9:// 司机已评价货主未评价
            case 10:// 已评价
                viewHolder.setText(R.id.tv_order_status, "已评价");
                viewHolder.setBackgroundRes(R.id.tv_order_status, R.drawable.iv_icon_order_status_glay);
                break;
        }
    }

    /**
     * 已详细信息形式设置费用模块内容
     */
    private void setAboutFeeByDetail(BaseViewHolder viewHolder, MyOrderListItem item) {
        //TODO 运费￥300.00 托管丨定金￥500.00
        viewHolder.setText(R.id.tv_about_fees, "运费￥" + item.getFreight_fee() + " " + item
                .getIsFreightManaged() + item.getFriendlyAgencyFeeDriver());
    }

    /**
     * 设置按钮显示的文本
     */
    private void displayBtnByOrderStatus(Button btnLeft, Button btnCenter, Button btnRight, int orderStatus) {
        switch (orderStatus) {
            case 1:// 货主取消交易,“报价中”
            case 3:// 待支付定金，"报价中"
                break;
            case 2:// 货主已确认成交，"待承接"
                setBtnVisibleCount(btnLeft, btnCenter, btnRight, 3);
                btnLeft.setText("呼叫货主");
                btnCenter.setText("放弃承接");
                btnRight.setText("我要承接");
                break;
            case 4:// 待支付运费，"待支付"
                setBtnVisibleCount(btnLeft, btnCenter, btnRight, 1);
                btnRight.setText("呼叫货主");
                break;
            case 5:// 提货中，"提货中"
                setBtnVisibleCount(btnLeft, btnCenter, btnRight, 3);
                btnLeft.setText("呼叫货主");
                btnCenter.setText("地图导航");
                btnRight.setText("确认提货");
                break;
            case 6:// 承运中， "配送中"
                setBtnVisibleCount(btnLeft, btnCenter, btnRight, 3);
                btnLeft.setText("呼叫货主");
                btnCenter.setText("地图导航");
                btnRight.setText("确认签收");
                break;
            case 7:// 已签收， "待评价"
            case 8:// 货主已评价司机未评价
                setBtnVisibleCount(btnLeft, btnCenter, btnRight, 2);
                btnLeft.setText("呼叫货主");
                btnRight.setText("我要评价");
                break;
            case 9:// 司机已评价货主未评价
            case 10:// 已评价
                setBtnVisibleCount(btnLeft, btnCenter, btnRight, 2);
                btnLeft.setText("查看回评");
                btnRight.setText("我要分享");
                break;
        }
    }

    /**
     * 设置按钮的点击事件
     */
    private void setBtnClickListener(final Button btnLeft, final Button btnRight, final Button btnCenter, final int
            goodsStatus, final int orderStatus, final MyOrderListItem item) {
        // 左边按钮的点击事件
        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (goodsStatus == 2) {// 生成订单
                    switch (orderStatus) {
                        case 1:// 货主取消交易,“报价中”
                        case 3:// 待支付定金，"报价中"
                            break;
                        case 2:// 货主已确认成交，"待承接"
                        case 4:// 待支付运费，"待支付"
                        case 5:// 提货中，"提货中"
                        case 6:// 承运中， "配送中"
                        case 7:// 已签收，"待评价"
                        case 8:// 货主已评价司机未评价
                            // 呼叫货主
                            if (onBtnClickListener != null) {
                                onBtnClickListener.clickCallPhone(btnLeft, item.getOwner_name(), item.getOwner_mobile
                                        ());
                            }
                            break;
                        case 9:// 司机已评价货主未评价
                        case 10:// 已评价
                            if (onBtnClickListener != null) {
                                onBtnClickListener.clickCheckComment(item.getOrder_id(), item.getOrder_version());
                            }
                    }

                } else if (goodsStatus == 6) {// 已退款
                    if (onBtnClickListener != null) {
                        onBtnClickListener.clickCheckComment(item.getOrder_id(), item.getOrder_version());
                    }

                }
            }
        });

        // 中间按钮的点击事件
        btnCenter.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                switch (orderStatus) {
                    case 1:// 货主取消交易,“报价中”
                    case 3:// 待支付定金，"报价中"
                        break;
                    case 2:// 货主已确认成交，"待承接"
                        // 放弃承接
                        if (onBtnClickListener != null) {
                            onBtnClickListener.clickGiveUpUndertake(item.getOrder_id(), item.getOrder_version());
                        }
                        break;
                    case 4:// 待支付运费，"待支付"
                        // 支付运费
                        break;
                    case 5:// 承运中，"提货中"
                    case 6:// 承运中， "配送中"
                        // 地图导航
                        if (onBtnClickListener != null) {
                            onBtnClickListener.clickNavigation(item);
                        }
                        break;
                }
            }
        });

        // 右边按钮的点击事件
        btnRight.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                if (goodsStatus == 2) {// 生成订单
                    switch (orderStatus) {
                        case 1:// 货主取消交易,“报价中”
                        case 3:// 待支付定金，"报价中"
                            break;
                        case 2:// 货主已确认成交，"待承接"
                            // 我要承接
                            if (onBtnClickListener != null) {
                                onBtnClickListener.clickDoUndertake(item.getGoods_id(), item.getOrder_id(), item
                                        .getOrder_version());
                            }
                            break;
                        case 4:// 待支付运费，"待支付"
                            // 呼叫货主
                            if (onBtnClickListener != null) {
                                onBtnClickListener.clickCallPhone(btnRight, item.getOwner_name(), item
                                        .getOwner_mobile());
                            }
                            break;
                        case 5:// 承运中，"提货中"
                            // 确认提货
                            if (onBtnClickListener != null) {
                                onBtnClickListener.clickConfirmPickUp(item);
                            }
                            break;
                        case 6:// 承运中， "配送中"
                            // 确认签收
                            if (onBtnClickListener != null) {
                                onBtnClickListener.clickConfirmSign(item);
                            }
                            break;
                        case 7:// 已签收， "待评价"
                        case 8:// 货主已评价司机未评价
                            // 确认签收
                            if (onBtnClickListener != null) {
                                onBtnClickListener.clickComment(item);
                            }
                            break;
                        case 9:// 司机已评价货主未评价
                        case 10:// 已评价
                            // 我要分享
                            if (onBtnClickListener != null) {
                                onBtnClickListener.clickShare(item.getOrder_id());
                            }
                            break;
                    }
                } else if (goodsStatus == 6) {// 已退款
                    // 我要分享
                    if (onBtnClickListener != null) {
                        onBtnClickListener.clickShare(item.getOrder_id());
                    }
                }

            }
        });
    }

    /**
     * 设置按钮可见个数
     */
    private void setBtnVisibleCount(Button btnLeft, Button btnCenter, Button btnRight, int count) {
        if (count == 1) {
            btnLeft.setVisibility(View.GONE);
            btnCenter.setVisibility(View.GONE);
            btnRight.setVisibility(View.VISIBLE);
        } else if (count == 2) {
            btnLeft.setVisibility(View.VISIBLE);
            btnCenter.setVisibility(View.GONE);
            btnRight.setVisibility(View.VISIBLE);
        } else if (count == 3) {
            btnLeft.setVisibility(View.VISIBLE);
            btnCenter.setVisibility(View.VISIBLE);
            btnRight.setVisibility(View.VISIBLE);
        }
    }


}
