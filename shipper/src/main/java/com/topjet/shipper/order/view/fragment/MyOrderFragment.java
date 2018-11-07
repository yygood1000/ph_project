package com.topjet.shipper.order.view.fragment;

import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.topjet.common.base.dialog.AutoDialogHelper;
import com.topjet.common.common.view.activity.CommentActivity;
import com.topjet.common.order_detail.modle.bean.MyOrderListItem;
import com.topjet.common.order_detail.modle.params.UpdatePayTypeParams;
import com.topjet.common.order_detail.modle.state.FreightState;
import com.topjet.common.order_detail.view.dialog.PartialCostDialog;
import com.topjet.common.order_detail.view.dialog.PayPasswordDialog;
import com.topjet.common.utils.CallPhoneUtils;
import com.topjet.common.utils.LayoutUtils;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.ResourceUtils;
import com.topjet.common.utils.StringUtils;
import com.topjet.common.widget.AutoScrollTextView;
import com.topjet.common.widget.RecyclerViewWrapper.BaseRecyclerViewFragment;
import com.topjet.common.widget.RecyclerViewWrapper.RecyclerViewWrapper;
import com.topjet.shipper.R;
import com.topjet.shipper.deliver.view.activity.DeliverGoodsActivity;
import com.topjet.shipper.familiar_car.model.extra.TruckExtra;
import com.topjet.shipper.familiar_car.view.activity.FindTruckActivity;
import com.topjet.shipper.familiar_car.view.activity.TruckPositionActivity;
import com.topjet.shipper.order.modle.extra.ShowOfferExtra;
import com.topjet.shipper.order.modle.serverAPI.OrderCommand;
import com.topjet.shipper.order.modle.serverAPI.OrderCommandAPI;
import com.topjet.shipper.order.presenter.MyOrderPresenter;
import com.topjet.shipper.order.view.activity.ShareGoodsActivity;
import com.topjet.shipper.order.view.activity.ShowOfferActivity;
import com.topjet.shipper.order.view.adapter.OrderListAdapter;
import com.topjetpaylib.encrypt.MD5Helper;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by yy on 2017/8/14.
 * 首页 Fragment
 */

public class MyOrderFragment extends BaseRecyclerViewFragment<MyOrderPresenter, MyOrderListItem> implements
        MyOrderView<MyOrderListItem> {
    @BindView(R.id.tv_share_goods)
    TextView tvShareGoods;
    @BindView(R.id.ll_titlebar)
    RelativeLayout llTitlebar;
    @BindView(R.id.my_order_tab_layout)
    TabLayout myOrderTabLayout;
    @BindView(R.id.histroy_order_tab_layout)
    TabLayout histroyOrderTabLayout;
    @BindView(R.id.rg_title)
    RadioGroup rgTitle;
    @BindView(R.id.rvw_order)
    RecyclerViewWrapper rvwOrder;
    @BindView(R.id.tv_marquee)
    AutoScrollTextView tv_marquee;
    @BindView(R.id.rl_marquee)
    RelativeLayout rlMarquee;

    // 是否是第一次进入页面
    private boolean isFirst = true;
    // 我的订单标识
    public final static int MY_ORDER = 0;
    // 历史订单标识
    public final static int HISTROY_ORDER = 1;
    // 顶部标识
    private int flag = MY_ORDER;
    //我的订单页卡标题集合
    private ArrayList<String> mMyOrderTitleList = new ArrayList<>();
    //历史订单页卡标题集合
    private ArrayList<String> mHistroyOrderTitleList = new ArrayList<>();
    // 订单状态0:全部 1：新货源 2:待成交 3:待支付 4:承运中
    private int orderStatus;
    // 订单状态0:待评价 1：已评价 2:已失效
    private int historyStatus;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_my_order;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new MyOrderPresenter(this, mContext, this, new OrderCommand(OrderCommandAPI.class, mActivity));
    }

    @Override
    protected void initView(View v) {
        super.initView();
        recyclerViewWrapper.getTvBtnEmpty().setVisibility(View.GONE);
        recyclerViewWrapper.getTvEmpty().setText(ResourceUtils.getString(R.string.list_is_empty));
        initTopRadioGroup();
        initTabLayout(myOrderTabLayout, histroyOrderTabLayout);
        // 初始化跑马灯栏
        tv_marquee.init(mActivity.getWindowManager());
    }

    @Override
    protected void initData() {
        refresh();
        // 获取跑马灯广告
        mPresenter.getMarqueeAdvertisment();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            refresh();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isFirst) {
            isFirst = false;
        } else {
            refresh();
        }
    }

    @Override
    public BaseQuickAdapter getAdapter() {
        OrderListAdapter mMyOrderAdapter = new OrderListAdapter(mContext);
        mMyOrderAdapter.setOnBtnClickListener(onBtnClickListener);
        return mMyOrderAdapter;
    }

    @Override
    public void loadData() {
        Logger.i("oye", "refresh");
        // 刷新我的订单
        if (flag == MY_ORDER) {
            mPresenter.getMyOrderListData(orderStatus, page);
        } else {
            mPresenter.getHistoryOrderListData(historyStatus, page);
        }
    }


     /* ==============================Title 大标签相关 satrt==============================*/

    /**
     * 初始化顶部RadioGroup
     */
    public void initTopRadioGroup() {
        rgTitle.setOnCheckedChangeListener(onCheckedChangeListener);// 顶部标题切换监听
    }

    /**
     * 顶部RadioGroup 点击监听
     */
    private RadioGroup.OnCheckedChangeListener onCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.rb_my_order:// 切换到我的订单
                    flag = MY_ORDER;
                    // 切换大标签，刷新我的订单数据
                    break;
                case R.id.rb_history_order:// 切换到历史订单
                    flag = HISTROY_ORDER;
                    // 初次请求历史订单接口
                    break;
            }
            recyclerViewWrapper.clearUI();
            refresh();
            // 切换标签布局
            changePage(flag);
        }
    };

    /**
     * 大标签切换，更换列表布局
     */
    public void changePage(int flag) {
        switch (flag) {
            case MY_ORDER:
                if (orderStatus == 0 | orderStatus == 1 | orderStatus == 2) {
                    setTvShareGoodsVisiable(View.VISIBLE);
                }
                myOrderTabLayout.setVisibility(View.VISIBLE);
                histroyOrderTabLayout.setVisibility(View.GONE);
                break;
            case HISTROY_ORDER:
                setTvShareGoodsVisiable(View.GONE);
                myOrderTabLayout.setVisibility(View.GONE);
                histroyOrderTabLayout.setVisibility(View.VISIBLE);
                break;
        }
    }

    /* ==============================Title 大标签相关 end==============================*/

    /* ==============================TanLayout 相关初始化与监听 satrt==============================*/

    /**
     * 初始化TabLayout
     */
    public void initTabLayout(TabLayout myOrderTabLayout, TabLayout histroyTabLayout) {
        mMyOrderTitleList.add("全部");
        mMyOrderTitleList.add("新货源");
        mMyOrderTitleList.add("待成交");
        mMyOrderTitleList.add("待支付");
        mMyOrderTitleList.add("承运中");

        mHistroyOrderTitleList.add("待评价");
        mHistroyOrderTitleList.add("已评价");
        mHistroyOrderTitleList.add("已失效");

        setTabLayoutContent(myOrderTabLayout, mMyOrderTitleList, 0);
        setTabLayoutContent(histroyTabLayout, mHistroyOrderTitleList, 0);

        // 设置下划线长度
        LayoutUtils.setIndicator(myOrderTabLayout, 15, 15);
        LayoutUtils.setIndicator(histroyTabLayout, 30, 30);

        // 订单状态标签切换监听回调
        myOrderTabLayout.addOnTabSelectedListener(onMyOrderTabSelectedListener);
        histroyTabLayout.addOnTabSelectedListener(onHistroyOrderTabSelectedListener);
    }

    /**
     * 设置TabLayout 中的内容
     */
    private void setTabLayoutContent(TabLayout tabLayout, List<String> tabLayoutStringList, int selected) {
        // 刷新TanLayout中的内容
        for (int i = 0; i < tabLayoutStringList.size(); i++) {
            if (i == selected) {
                tabLayout.addTab(tabLayout.newTab().setText(tabLayoutStringList.get(i)), true);
            } else {
                tabLayout.addTab(tabLayout.newTab().setText(tabLayoutStringList.get(i)));
            }
        }
    }

    /**
     * 我的订单 小标签 切换回调
     */
    private TabLayout.OnTabSelectedListener onMyOrderTabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            // 小标签为全部与待成交小标签时才显示分享货源
            if (tab.getPosition() == 0 | tab.getPosition() == 1 | tab.getPosition() == 2) {
                setTvShareGoodsVisiable(View.VISIBLE);
            } else {
                setTvShareGoodsVisiable(View.GONE);
            }
            // 更改请求的列表参数
            orderStatus = tab.getPosition();
            recyclerViewWrapper.clearUI();
            refresh();
            // 我的订单刷新操作
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
        }
    };

    /**
     * 历史订单 小标签 切换回调
     */
    private TabLayout.OnTabSelectedListener onHistroyOrderTabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            // 更改请求的列表参数
            historyStatus = tab.getPosition();
            recyclerViewWrapper.clearUI();
            refresh();
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
        }
    };

    /**
     * 切换我的订单小标签
     */
    @Override
    public void changeOrderListTabLayout(int index) {
        if (index < myOrderTabLayout.getTabCount()) {
            myOrderTabLayout.getTabAt(index).select();
        }
    }
    /* ==============================TanLayout 相关初始化与监听 end==============================*/

    /**
     * 是否显示分享货源文本框
     */
    @Override
    public void setTvShareGoodsVisiable(int isVisiable) {
        tvShareGoods.setVisibility(isVisiable);
    }

    /**
     * 展示跑马灯数据
     */
    @Override
    public void showMarquee(String text) {
        if (StringUtils.isEmpty(text)) {
            rlMarquee.setVisibility(View.GONE);
        } else {

            rlMarquee.setVisibility(View.VISIBLE);
            tv_marquee.setText(text);
            tv_marquee.init(mActivity.getWindowManager());
            tv_marquee.startScroll();
        }
    }

    /**
     * 分享货源
     */
    @OnClick({R.id.tv_share_goods, R.id.iv_close_marquee})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_share_goods:
                // 分享货源
                turnToActivity(ShareGoodsActivity.class);
                break;
            case R.id.iv_close_marquee:
                // 关闭跑马灯
                rlMarquee.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void errorClick() {
        refresh();
    }

    @Override
    public void emptyClick() {
        refresh();
    }

    /**
     * 列表按钮的点击事件
     */
    private OrderListAdapter.OnBtnClickListener onBtnClickListener = new OrderListAdapter.OnBtnClickListener() {
        @Override
        public void clickItem(String goodsId) {
            // 调用接口判断订单状态
            mPresenter.getOrderIdbyGoodsId(goodsId);
        }

        @Override
        public void clickCheckBidding(String orderId, String goodsId, String goodsVersion, double transportFee,
                                      String payType) {
            // 查看报价
            ShowOfferExtra extra = new ShowOfferExtra();
            extra.setGoodsId(goodsId);
            extra.setOrderId(orderId);
            extra.setGoodsVersion(goodsVersion);
            extra.setAhead(payType.equals("3")); // 是否提付部分运费
            ShowOfferActivity.toShowOffer(mActivity, extra);
        }

        @Override
        public void clickCloneOrder(String goodsid) {
            // 复制订单
            DeliverGoodsActivity.turnToDeliverGoodsForCopy(mActivity, goodsid);
        }

        @Override
        public void clickDriverLocation(MyOrderListItem item) {
            // 司机位置
            TruckExtra extra = new TruckExtra();
            extra.setShowMenu(true);
            extra.setName(item.getDriver_name());
            extra.setMobile(item.getDriver_mobile());
            extra.setLatitude(item.getDriver_latitude());
            extra.setLongitude(item.getDriver_longitude());
            extra.setAddress(item.getDriver_gps_detailed_address());
            extra.setTime(item.getDriver_gps_update_time());
            extra.setTruck_plate(item.getLicense_plate_number());
            extra.setTruckId(item.getDriver_truck_id());
            TruckPositionActivity.truckPosition(mActivity, extra);
        }

        @Override
        public void clickFindTruckAgain(MyOrderListItem item) {
            // 重新找车,非定向订单进入报价列表页面
            if (item.getIs_assigned()) {
                FindTruckActivity.turnToRefindTruck(mActivity, item.getDepart_city_id(), item.getDestination_city_id
                        (), item.getGoods_id());
            } else { // 查看报价
                ShowOfferExtra extra = new ShowOfferExtra();
                extra.setGoodsId(item.getGoods_id());
                extra.setOrderId(item.getOrder_id());
                extra.setGoodsVersion(item.getGoods_version());
                extra.setAhead(item.getPay_style().equals("3")); // 是否提付部分运费
                ShowOfferActivity.toShowOffer(mActivity, extra);
            }
        }

        @Override
        public void clickPayFreight(final MyOrderListItem item) {
            // 支付运费
            // 如果是部分提付费 并且有定金，需要弹出修改支付方式的弹窗，但是不能修改，只能看，不跳转合同页，直接去钱包支付
            if (item.getPay_style().equals("3") && item.getFreight().getAgency_status() > 0) {
                new PartialCostDialog(mContext)
                        .showSetCost(item.getFreight(),
                                true,
                                new PartialCostDialog.OnConfirmClick() {
                                    @Override
                                    public void confirmClick(UpdatePayTypeParams payTypeParams) {
                                        payTypeParams.setOrder_id(item.getOrder_id());
                                        payTypeParams.setOrder_version(item.getOrder_version());
                                        payTypeParams.setPay_style(item.getPay_style());
                                        mPresenter.updatePayType(payTypeParams, item.getOrder_id(), item
                                                .getOrder_version());
                                    }
                                });
            } else {
                mPresenter.paymentFreight(item.getOrder_id(), item.getOrder_version());
            }
        }

        @Override
        public void clickCallPhone(String name, String phone) {
            // 呼叫司机
            new CallPhoneUtils().showCallDialogWithAdvNotUpload(mActivity, rvwOrder, name, phone, 2);
        }

        @Override
        public void clickSendPickUpCode(final String orderId) {
            // 发送提货码，先获取退款信息再进行弹窗
            mPresenter.lookRefund(orderId);
        }

        @Override
        public void clickConfirmTheSign(MyOrderListItem item) {
            // 确认签收
            signOrder(item);
        }

        @Override
        public void clickReleaseGood(MyOrderListItem item) {
            // 发布货源，定向货源转非定向
            DeliverGoodsActivity.turnToDeliverGoodsForEdit(mActivity, item.getGoods_id());
        }

        @Override
        public void clickComment(MyOrderListItem item) {
            // 评价赚积分
            CommentActivity.turnToCommentActivity(mActivity,
                    item.getOrder_id(),
                    item.getOrder_version(),
                    item.getDriver_id(),
                    item.getDriver_name());
        }

        @Override
        public void clickCheckComment(String orderId, String orderVersion, String commentedUserId, String
                commentedUserName) {
            // 查看回评
            CommentActivity.turnToCheckCommentActivity(mActivity, orderId, orderVersion);
        }


        @Override
        public void clickDeleteOrder(final String goodsId, final String goodsVersion) {
            // 删除订单
            mPresenter.deleteOrder(goodsId, goodsVersion);
        }
    };

    @Override
    public RecyclerViewWrapper getRecyclerView() {
        return rvwOrder;
    }

    /**
     * 签收货物
     */
    private void signOrder(final MyOrderListItem item) {
        String toast;
        // 到付费状态 为已支付给平台
        int deliveryState = item.getFreight().getDelivery_fee_status();
        final boolean isArrivalsPay = deliveryState == FreightState.PAY_ALREADY;

        if (isArrivalsPay) {
            toast = "确认签收后，运费将直接支付给司机，请确保货物已安全送达。";
        } else {
            toast = "请确保货物已安全送达后再确认签收";
        }

        AutoDialogHelper.showContent(getActivity(), toast, new AutoDialogHelper.OnConfirmListener() {
            @Override
            public void onClick() {
                //  这里需要调用输入支付密码密码的框
                if (isArrivalsPay) {
                    new PayPasswordDialog(getActivity())
                            .setConfirmListener(new PayPasswordDialog.ConfirmListener() {
                                @Override
                                public void onConfirm(String text) {
                                    // 这里需要调接口
                                    try {
                                        mPresenter.confirmReceivingShipper(item.getOrder_id(), item.getOrder_version
                                                (), MD5Helper.getMD5(text));
                                    } catch (NoSuchAlgorithmException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).show();
                } else {
                    mPresenter.confirmReceivingShipper(item.getOrder_id(), item.getOrder_version());
                }
            }
        }).show();
    }

}
