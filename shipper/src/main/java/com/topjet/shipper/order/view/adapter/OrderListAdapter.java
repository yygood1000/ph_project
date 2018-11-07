package com.topjet.shipper.order.view.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.topjet.common.resource.dict.CommonDataDict;
import com.topjet.common.order_detail.modle.bean.MyOrderListItem;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.NumberFormatUtils;
import com.topjet.common.utils.Toaster;
import com.topjet.shipper.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yy on 2017/7/31.
 * <p>
 * RecyclerView 适配器
 * <p>
 * viewHolder.getLayoutPosition() 获取当前item的position
 */

public class OrderListAdapter extends BaseQuickAdapter<MyOrderListItem, BaseViewHolder> {

    public interface OnBtnClickListener {
        void clickItem(String goodsId);

        // 查看报价
        void clickCheckBidding(String orderId, String goodsId, String goodsVersion, double transportFee, String
                payType);

        // 复制订单
        void clickCloneOrder(String goods_id);

        // 司机定位
        void clickDriverLocation(MyOrderListItem item);

        // 重新找车
        void clickFindTruckAgain(MyOrderListItem item);

        // 支付运费
        void clickPayFreight(MyOrderListItem item);

        // 拨打司机电话
        void clickCallPhone(String name, String phone);

        // 发送验证码
        void clickSendPickUpCode(String orderId);

        // 确认签收
        void clickConfirmTheSign(MyOrderListItem item);

        // 发布货源
        void clickReleaseGood(MyOrderListItem item);

        // 评价赚积分
        void clickComment(MyOrderListItem item);

        // 查看回评
        void clickCheckComment(String orderId, String orderVersion, String commentedUserId, String
                commentedUserName);

        // 删除订单
        void clickDeleteOrder(String goodsId, String goodsVersion);

    }

    // 分享选中监听
    public interface OnCheckListener {
        // 选中个数回调
        void checkNum(int num);
    }

    private OnBtnClickListener onBtnClickListener;
    private OnCheckListener onCheckListener;

    public void setOnCheckListener(OnCheckListener onCheckListener) {
        this.onCheckListener = onCheckListener;
    }

    /**
     * 设置所有按钮的点击事件
     */
    public void setOnBtnClickListener(OnBtnClickListener onBtnClickListener) {
        this.onBtnClickListener = onBtnClickListener;
    }

    public static final int ORDER_DISPLAY = 1; // 显示模式， 我的订单显示
    public static final int ORDER_CHECK = 2; // 选择模式， 货源分享显示
    private int mMode;
    private List<MyOrderListItem> mCheckDate = new ArrayList<>(); // 已选择的数据id

    public OrderListAdapter(Context context) {
        super(R.layout.listitem_my_order);
        this.mContext = context;
        this.mMode = ORDER_DISPLAY;
    }

    public OrderListAdapter(Context context, int mode) {
        super(R.layout.listitem_my_order);
        this.mContext = context;
        this.mMode = mode;
    }

    @Override
    protected void convert(final BaseViewHolder viewHolder, final MyOrderListItem item) {
        // 地址信息
        viewHolder.setText(R.id.tv_depart_address, item.getDepart_city());
        viewHolder.setText(R.id.tv_destination_address, item.getDestination_city());

        int goodsStatus = NumberFormatUtils.strToInt(item.getGoods_status());// 货源状态
        int orderStatus = NumberFormatUtils.strToInt(item.getOrder_status());// 订单状态

        // 展示与订单状态相关的UI
        setUIByStatus(viewHolder, item);

        // 根据状态展示 费用
        setFeeByOrderStatus(viewHolder, goodsStatus, orderStatus, item);

        // 设置提货时间
        viewHolder.setText(R.id.tv_pick_up_time, item.getLoad_date_name());

        // 我的订单显示模式
        if (mMode == ORDER_DISPLAY) {
            // 设置全局点击事件
            LinearLayout llRoot = viewHolder.getView(R.id.ll_root);
            llRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onBtnClickListener != null) {
                        onBtnClickListener.clickItem(item.getGoods_id());
                    }
                }
            });
        } else {
            // 选择货源分享模式
            viewHolder.getView(R.id.rl_btn).setVisibility(View.GONE);
            viewHolder.getView(R.id.cb_order).setVisibility(View.VISIBLE);
            // 选择框
            final CheckBox checkBox = viewHolder.getView(R.id.cb_order);
            // cb_order
            checkBox.setChecked(item.isCheck());
//            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    Logger.d("选中" + isChecked + " " + viewHolder.getLayoutPosition() + " ");
//                    if(mCheckDate.size() > 9 && isChecked) {
//                        // 如果已经选中了10个则不让在选中
//                        checkBox.setChecked(false);
//                    } else {
//                        check(viewHolder.getLayoutPosition(), isChecked);
//                    }
//                }
//            });
            LinearLayout llRoot = viewHolder.getView(R.id.ll_root);
            llRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Logger.d("选中" + item.isCheck() + " " + viewHolder.getLayoutPosition() + " ");
                    check(viewHolder.getPosition(), !item.isCheck());
                }
            });
        }
    }

    /**
     * 设置订单状态标签内容
     */
    private void setUIByStatus(BaseViewHolder viewHolder, MyOrderListItem item) {
        int goodsStatus = NumberFormatUtils.strToInt(item.getGoods_status());// 货源状态
        int orderStatus = NumberFormatUtils.strToInt(item.getOrder_status());// 订单状态

        Button btnLeft = viewHolder.getView(R.id.btn_left);
        Button btnRight = viewHolder.getView(R.id.btn_right);

        switch (goodsStatus) {
            case 1: // 新货源发布
                viewHolder.setText(R.id.tv_order_status, "报价中");
                viewHolder.setBackgroundRes(R.id.tv_order_status, R.drawable.iv_icon_order_status_red);

                btnLeft.setText("查看报价(" + item.getPre_order_count() + ")");
                btnRight.setText("复制订单");
                break;
            case 2: // ** 生成订单，进行订单状态判断 **
                // 根据状态展示 标签
                setLableByOrderStatus(viewHolder, orderStatus);

                // 根据状态展示 按钮
                displayBtnAndSetClickListener(btnLeft, btnRight, item, orderStatus);
                break;
            case 3: // 货主撤销订单
                viewHolder.setText(R.id.tv_order_status, "已撤销");
                viewHolder.setBackgroundRes(R.id.tv_order_status, R.drawable.iv_icon_order_status_glay);
                btnLeft.setText("删除订单");
                btnRight.setText("复制订单");
                break;
            case 4: // 定向订单，被司机拒绝了
                viewHolder.setText(R.id.tv_order_status, "待指派");
                viewHolder.setBackgroundRes(R.id.tv_order_status, R.drawable.iv_icon_order_status_red);
                btnLeft.setText("重新找车");
                btnRight.setText("发布货源");
                break;
            case 5: // 货源过期
                viewHolder.setText(R.id.tv_order_status, "已过期");
                viewHolder.setBackgroundRes(R.id.tv_order_status, R.drawable.iv_icon_order_status_glay);
                btnLeft.setText("删除订单");
                btnRight.setText("复制订单");
                break;
            case 6: // 货源退款
                viewHolder.setText(R.id.tv_order_status, "已退款");
                viewHolder.setBackgroundRes(R.id.tv_order_status, R.drawable.iv_icon_order_status_glay);
                btnLeft.setText("删除订单");
                btnRight.setText("复制订单");
                break;

        }

        // 设置按钮点击事件
        setBtnClickListener(btnLeft, btnRight, item, goodsStatus, orderStatus);
    }

    /**
     * 根据订单状态 设置标签
     */
    private void setLableByOrderStatus(BaseViewHolder viewHolder, int orderStatus) {
        switch (orderStatus) {
            case 1:// 货主已经点击取消交易
                viewHolder.setText(R.id.tv_order_status, "报价中");
                viewHolder.setBackgroundRes(R.id.tv_order_status, R.drawable.iv_icon_order_status_red);
                break;
            case 2:// 货主已经点击确认成交
                viewHolder.setText(R.id.tv_order_status, "待确认");// 这里的“待确认”是货主点击了确认成交，待司机确认承接
                viewHolder.setBackgroundRes(R.id.tv_order_status, R.drawable.iv_icon_order_status_red);
                break;
            case 3:// 待支付定金，某司机带定金报价，但未支付定金。
                viewHolder.setText(R.id.tv_order_status, "报价中");
                viewHolder.setBackgroundRes(R.id.tv_order_status, R.drawable.iv_icon_order_status_red);
                break;
            case 4:// 待支付运费
                viewHolder.setText(R.id.tv_order_status, "待支付");
                viewHolder.setBackgroundRes(R.id.tv_order_status, R.drawable.iv_icon_order_status_red);
                break;
            case 5:// 提过中
                viewHolder.setText(R.id.tv_order_status, "提货中");
                viewHolder.setBackgroundRes(R.id.tv_order_status, R.drawable.iv_icon_order_status_green);
                break;
            case 6:// 承运中
                viewHolder.setText(R.id.tv_order_status, "配送中");
                viewHolder.setBackgroundRes(R.id.tv_order_status, R.drawable.iv_icon_order_status_green);
                break;
            case 7:// 已承运/待评价
            case 9:// 司机已评价货主未评价
                viewHolder.setText(R.id.tv_order_status, "待评价");
                viewHolder.setBackgroundRes(R.id.tv_order_status, R.drawable.iv_icon_order_status_glay);
                break;
            case 8:// 货主已评价司机未评价
            case 10:// 已评价
                viewHolder.setText(R.id.tv_order_status, "已评价");
                viewHolder.setBackgroundRes(R.id.tv_order_status, R.drawable.iv_icon_order_status_glay);
                break;
        }
    }

    /**
     * 根据费用字段展示费用信息
     * 当运费字段有值，并且运费大于0时，显示详细的费用信息，否则显示支付方式
     * <p>
     * 该模块展示分为两种：1：以费用概要信息方式显示 setOverviewFee() 2：以详细的费用信息显示 setDetailFee()
     */
    private void setFeeByOrderStatus(BaseViewHolder viewHolder, int goodsStatus, int orderStatus,
                                     MyOrderListItem item) {
        if (item.getFreight() == null || item.getFreight().getFreight_fee() <= 0) {
            setOverviewFee(viewHolder, goodsStatus, item);
        } else {
            setDetailFee(viewHolder, item);
        }
    }

    /**
     * 以概要形式设置费用模块内容
     */
    private void setOverviewFee(BaseViewHolder viewHolder, int goodsStatus, MyOrderListItem item) {
        if (goodsStatus == 4) {// 定向订单 司机取消交易 “待指派”状态
            // TODO 货到付款丨托管
            viewHolder.setText(R.id.tv_about_fees, CommonDataDict.getData(CommonDataDict.CommonDataType.PAY_WAY, item
                    .getPay_style()) + item.getIsFreightManagedWithLine());
        } else {// 非定向订单 货主选择司机确认，等待司机去人 “待确认”状态
            // TODO 货到付款丨托管丨报价中
            viewHolder.setText(R.id.tv_about_fees, CommonDataDict.getData(CommonDataDict.CommonDataType.PAY_WAY, item
                    .getPay_style()) + item.getIsFreightManagedWithLine() + "丨报价中");
        }
    }

    /**
     * 已详细信息形式设置费用模块内容
     */
    private void setDetailFee(BaseViewHolder viewHolder, MyOrderListItem item) {
        // 判断是否是定向订单，如果是定向订单，则没有定金模块，这个判断可以不需要，因为定向订单肯定没有定金
        if (item.getIs_assigned()) {
            //TODO 运费￥555.00 托管
            viewHolder.setText(R.id.tv_about_fees, "运费￥" + item.getFreight().getFreight_fee() + " " + item
                    .getIsFreightManaged());
        } else {
            //TODO 运费￥300.00 托管丨待收定金￥500.00
            viewHolder.setText(R.id.tv_about_fees, "运费￥" + item.getFreight().getFreight_fee() + " " + item
                    .getIsFreightManaged() + item.getFriendlyAgencyFeeShipper());
        }
    }

    /**
     * 设置按钮显示的文本
     */
    private void displayBtnAndSetClickListener(Button btnLeft, Button btnRight, MyOrderListItem item, int orderStatus) {
        switch (orderStatus) {
            case 1:// 货主取消交易,“报价中”
                btnLeft.setText("查看报价(" + item.getPre_order_count() + ")");
                btnRight.setText("复制订单");
                break;
            case 2:// 货主确认成交，"待确认"
                if (item.getIs_assigned()) {// 定向订单
                    btnLeft.setText("重新找车");
                } else {
                    btnLeft.setText("查看报价(" + item.getPre_order_count() + ")");
                }
                btnRight.setText("司机位置");
                break;
            case 3:// 待支付定金，"报价中"
                btnLeft.setText("查看报价(" + item.getPre_order_count() + ")");
                btnRight.setText("复制订单");
                break;
            case 4:// 待支付运费，"待支付"
                btnLeft.setText("呼叫司机");
                btnRight.setText("支付运费");
                break;
            case 5:// 提货中，"提货中"
                btnLeft.setText("呼叫司机");
                btnRight.setText("发送提货码");
                break;
            case 6:// 承运中， "配送中"
                btnLeft.setText("呼叫司机");
                btnRight.setText("确认签收");
                break;
            case 7:// 已承运/待评价
            case 9:// 司机已评价货主未评价
                btnLeft.setText("复制订单");
                btnRight.setText("我要评价");
                break;
            case 8:// 货主已评价司机未评价
            case 10:// 已评价
                btnLeft.setText("复制订单");
                btnRight.setText("查看回评");
                break;
        }
    }

    /**
     * 设置按钮的点击事件
     */
    private void setBtnClickListener(Button btnLeft, Button btnRight, final MyOrderListItem item, final int goodsStatus,
                                     final int orderStatus) {
        // 左边按钮的点击事件
        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onBtnClickListener != null) {
                    if (goodsStatus == 1) {// 新货源发布,“报价中”
                        // 查看报价
                        onBtnClickListener.clickCheckBidding(item.getOrder_id(), item.getGoods_id(), item
                                .getGoods_version(), item
                                .getFreight_fee(), item.getPay_style());
                    } else if (goodsStatus == 3 | goodsStatus == 5 | goodsStatus == 6) {// 货主撤销订单// 货源过期
                        // 删除订单
                        onBtnClickListener.clickDeleteOrder(item.getGoods_id(), item.getGoods_version());
                    } else if (goodsStatus == 4) {// 定向订单，被司机拒绝了，“待指派”
                        // 重新找车
                        onBtnClickListener.clickFindTruckAgain(item);
                    } else if (goodsStatus == 2) {// 生成订单
                        switch (orderStatus) {
                            case 1:// 货主取消交易,“报价中”
                            case 3:// 待支付定金，"报价中"
                                // 查看报价
                                onBtnClickListener.clickCheckBidding(item.getOrder_id(), item.getGoods_id(), item
                                                .getGoods_version(),
                                        item.getFreight_fee(), item.getPay_style());
                                break;
                            case 2:// 货主确认成交，"待确认"
                                if (item.getIs_assigned()) {
                                    // 重新找车
                                    onBtnClickListener.clickFindTruckAgain(item);
                                } else {
                                    // 查看报价
                                    onBtnClickListener.clickCheckBidding(item.getOrder_id(), item.getGoods_id(), item
                                                    .getGoods_version(),
                                            item.getFreight_fee(), item.getPay_style());
                                }
                                break;
                            case 4:// 待支付运费，"待支付"
                            case 5:// 提货中，"提货中"
                            case 6:// 承运中， "配送中"
                                // 呼叫司机
                                onBtnClickListener.clickCallPhone(item.getDriver_name(), item.getDriver_mobile());
                                break;
                            case 7:// 已承运/待评价
                            case 9:// 司机已评价货主未评价
                            case 8:// 货主已评价司机未评价
                            case 10:// 已评价
                                // 复制订单
                                onBtnClickListener.clickCloneOrder(item.getGoods_id());
                                break;
                        }
                    }
                }
            }
        });

        // 右边按钮的点击事件
        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (goodsStatus == 1 | goodsStatus == 3 | goodsStatus == 5 | goodsStatus == 6) {// 新货源发布,
                    // “报价中”//货主撤销订单//货源过期
                    // 复制订单
                    onBtnClickListener.clickCloneOrder(item.getGoods_id());
                } else if (goodsStatus == 4) {// 定向订单，被司机拒绝了，“待指派”
                    // 发布货源
                    onBtnClickListener.clickReleaseGood(item);
                } else if (goodsStatus == 2) {// 生成订单
                    switch (orderStatus) {
                        case 1:// 货主取消交易,“报价中”
                        case 3:// 待支付定金，"报价中"（货主订单列表不会出现这种状态的订单）
                            // 复制订单
                            onBtnClickListener.clickCloneOrder(item.getGoods_id());
                            break;
                        case 2:// 货主确认成交，"待确认"
                            // 司机位置
                            onBtnClickListener.clickDriverLocation(item);
                            break;
                        case 4:// 待支付运费，"待支付"
                            // 支付运费
                            onBtnClickListener.clickPayFreight(item);
                            break;
                        case 5:// 提货中，"提货中"
                            // 发送提货码
                            onBtnClickListener.clickSendPickUpCode(item.getOrder_id());
                            break;
                        case 6:// 承运中， "配送中"
                            // 确认签收
                            onBtnClickListener.clickConfirmTheSign(item);
                            break;
                        case 7:// 已承运/待评价
                        case 9:// 司机已评价货主未评价
                            // 我要评价
                            onBtnClickListener.clickComment(item);
                            break;
                        case 8:// 货主已评价司机未评价
                        case 10:// 已评价
                            // 查看回评
                            onBtnClickListener.clickCheckComment(item.getOrder_id(), item.getOrder_version(), item
                                    .getDriver_id(), item.getDriver_name());
                            break;
                    }
                }
            }
        });
    }

    /**
     * 选中操作
     */
    private void check(final int position, boolean check) {
        if(mCheckDate.size() > 9 && check){
            // 最多分享10条
            Toaster.showLongToast(mContext.getString(R.string.share_max));
            return;
        }
        if (position >= 0 &&
                position < getItemCount() &&
                getItem(position) != null) {
            getItem(position).setCheck(check);
            if (check) {
                if (!mCheckDate.contains(getItem(position))) {
                    mCheckDate.add(getItem(position));
                }
            } else {
                if (mCheckDate.contains(getItem(position))) {
                    mCheckDate.remove(getItem(position));
                }
            }

            Logger.d("选中2 "+mCheckDate.size());
            // 将个数传递给页面，显示按钮的是否可点击状态
            if (onCheckListener != null) {
                onCheckListener.checkNum(mCheckDate.size());
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        notifyItemChanged(position);
                    }
                });
            }
        }

    }

    public List<MyOrderListItem> getCheckDate() {
        return mCheckDate;
    }

    public void setCheckDate(List<MyOrderListItem> mCheckDate) {
        this.mCheckDate = mCheckDate;
    }

    /**
     * 获取选中的list
     *
     * @return 选中的货源id
     */
    public List<String> getCheckIds() {
        List<String> ids = new ArrayList<>();
        for (MyOrderListItem item : mCheckDate) {
            ids.add(item.getGoods_id());
        }
        return ids;
    }
}
