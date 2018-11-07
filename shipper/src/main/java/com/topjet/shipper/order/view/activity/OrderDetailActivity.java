package com.topjet.shipper.order.view.activity;

import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;

import com.topjet.common.base.busManger.Subscribe;
import com.topjet.common.base.dialog.AutoDialogHelper;
import com.topjet.common.base.listener.ObserverOnResultListener;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.base.view.adapter.PagerFragmentAdapter;
import com.topjet.common.common.modle.params.TruckParams;
import com.topjet.common.common.view.activity.CommentActivity;
import com.topjet.common.common.view.fragment.ListFragment;
import com.topjet.common.order_detail.modle.bean.DriverInfo;
import com.topjet.common.order_detail.modle.event.CancelOrderDetailEvent;
import com.topjet.common.order_detail.modle.extra.ClickAgreeBtnEvent;
import com.topjet.common.order_detail.modle.extra.OrderExtra;
import com.topjet.common.order_detail.modle.params.UpdatePayTypeParams;
import com.topjet.common.order_detail.modle.response.GetCallListResponse;
import com.topjet.common.order_detail.modle.response.GetShowGoodsListResponse;
import com.topjet.common.order_detail.modle.response.GoodsInfoResponse;
import com.topjet.common.order_detail.modle.response.OrderInfoResponse;
import com.topjet.common.order_detail.modle.state.FreightState;
import com.topjet.common.order_detail.modle.state.OrderState;
import com.topjet.common.order_detail.modle.state.StateBtnMsgShipperProvider;
import com.topjet.common.order_detail.modle.state.StateRightMenuShipperProvider;
import com.topjet.common.order_detail.modle.state.StateTopMsgProvider;
import com.topjet.common.order_detail.presenter.SkipControllerProtocol;
import com.topjet.common.order_detail.view.activity.OrderDetailBaseActivity;
import com.topjet.common.order_detail.view.dialog.PartialCostDialog;
import com.topjet.common.order_detail.view.dialog.PayPasswordDialog;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.StringUtils;
import com.topjet.common.widget.MyScrollView;
import com.topjet.common.widget.RecyclerViewWrapper.LoadMoreManager;
import com.topjet.common.widget.SwipeRefreshLayout.SwipeRefreshLayout;
import com.topjet.shipper.R;
import com.topjet.shipper.deliver.view.activity.DeliverGoodsActivity;
import com.topjet.shipper.familiar_car.model.extra.TruckExtra;
import com.topjet.shipper.familiar_car.view.activity.FindTruckActivity;
import com.topjet.shipper.familiar_car.view.activity.TruckPositionActivity;
import com.topjet.shipper.order.modle.extra.ShowOfferExtra;
import com.topjetpaylib.encrypt.MD5Helper;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

/**
 * creator: zhulunjun
 * time:    2017/9/4
 * describe: 订单详情，货源详情
 */

public class OrderDetailActivity extends OrderDetailBaseActivity implements MyScrollView.ScrollViewListener {

    private ArrayList<Fragment> fragments = new ArrayList<>();
    private PagerFragmentAdapter adapter;

    private ListFragment viewListFragment, callListFragment;
    private int pageCallList = 1;
    private int pageViewList = 1;
    private UpdatePayTypeParams payTypeParamsResult;// 修改支付方式后的结果

    /**
     * 跳转到货源详情
     *
     * @param activity 跳转页面
     * @param goodsId  货源id
     */
    public static void toGoodsDetail(MvpActivity activity, String goodsId) {
        activity.turnToActivity(OrderDetailActivity.class, new OrderExtra(goodsId, OrderExtra.GOODS_DETAIL));
    }

    /**
     * 跳转到订单详情
     *
     * @param activity 跳转页面
     * @param orderId  订单id
     */
    public static void toOrderDetail(MvpActivity activity, String orderId) {
        activity.turnToActivity(OrderDetailActivity.class, new OrderExtra(orderId, OrderExtra.ORDER_DETAIL));
    }

    @Override
    protected int getLayoutResId() {
        return super.getLayoutResId();
    }

    @Override
    protected void initView() {

        initRefresh();
        initBottomBtn();

        if (mDataSource == OrderExtra.GOODS_DETAIL) {
            svContent.setMyScrollViewListener(this);
            getAlreadyTop();
            initViewAlready();
        }

    }

    /**
     * 底部按钮 默认
     */
    private void initBottomBtn() {
        tvBtnOne.setVisibility(View.VISIBLE);
        llBtnTwo.setVisibility(View.VISIBLE);
        tvBtnOne.setText(getString(R.string.update_order));
        tvBtnTwo.setText(getString(R.string.show_offer));
    }

    /**
     * 获取页面展示数据
     */
    private void getData() {
        if (mDataSource == OrderExtra.GOODS_DETAIL) {
            mPresenter.getGoodsInfoShipper();
        } else {
            mPresenter.getOrderInfoShipper();
        }
    }


    /**
     * 显示货主货源详情特有的UI
     */
    @Override
    public void setGoodsInfo(GoodsInfoResponse data) {
        mSwipeRefreshLayout.setRefreshing(false);
        //隐藏距离和报价人数
        tvOrderQuoteNum.setVisibility(View.GONE);
        rlMapDistance.setVisibility(View.GONE);
        pagerTitle.setVisibility(View.VISIBLE);
        viewPager.setVisibility(View.VISIBLE);
        llDriverInfo.setVisibility(View.GONE);
        llQuote.setVisibility(View.GONE);
        setViewAlready(data.getLooked_sum(), data.getCalled_sum());
        int offNum = StringUtils.getIntToString(data.getOffer_sum());
        // 按钮上显示报价数
        String btnText = StateBtnMsgShipperProvider.getInstance().twoStrShippers[5];
        if (tvBtnTwo.getText().equals(btnText)) {
            if (offNum > 0) {
                tvBtnTwoRight.setVisibility(View.VISIBLE);
                tvBtnTwoRight.setText("(" + (offNum > 99 ? "99+" : "" + offNum) + ")");
            } else {
                tvBtnTwoRight.setVisibility(View.GONE);
            }
        }

        // 顶部显示
        String topMsg = StateTopMsgProvider.getInstance().getTopMessage(data.getGoods_status());
        if (StringUtils.isEmpty(topMsg)) {
            tvRefreshOrder.setVisibility(View.VISIBLE);
        } else {
            tvRefreshOrder.setVisibility(View.GONE);
            setTopMessage(topMsg);
        }

        // 如果是您指派的司机未接单，请重新发布
        // 则两个按钮 重新找车 发布货源

    }

    /**
     * 显示货主订单详情特有的UI
     */
    @Override
    public void setOrderInfo(OrderInfoResponse data) {
        mSwipeRefreshLayout.setRefreshing(false);
        rlMapDistance.setVisibility(View.VISIBLE);
        tvStateDeposit.setVisibility(View.GONE);
        llQuote.setVisibility(View.GONE);
        // 司机信息
        ivMessage.setImageResource(R.drawable.iv_icon_message_shipper);
        if (data.getDriver_info() != null) {
            setUserInfo(data.getDriver_info().getDriver_name(),
                    data.getDriver_info().getDriver_icon_key(),
                    data.getDriver_info().getDriver_icon_url(),
                    data.getDriver_info().getClinch_a_deal_sum(),
                    //这里是车辆信息
                    data.getDriver_info().getLicense_plate_number() + " | " + data.getDriver_info().getTruck_type() +
                            data.getDriver_info().getTruck_length(),
                    data.getDriver_info().getDriver_comment_level());
            // 司机位置信息
            tvDistance.setText(data.getDriver_info().getDriver_gps_detailed_address());
        }

        // 定金
        // 提货前显示待收定金
        // 提货后显示已收定金
        if (data.getOrder_status() > OrderState.ORDER_PICK_UP_GOODS) {
            tvShowDeposit.setText(R.string.payment_deposit_received);
            tvArrivedAccount.setText(R.string.arrived_account);
        } else {
            tvShowDeposit.setText(R.string.payment_deposit_wait);
            tvArrivedAccount.setText(R.string.wait_account);
        }
    }

    /**
     * 设置底部按钮，文字显示
     */
    @Override
    public void setBottomBtnText(int state) {
        String[] str;
        if (mDataSource == OrderExtra.GOODS_DETAIL) {
            str = StateBtnMsgShipperProvider.getInstance().getBottomBtnTextGoods(state);
        } else {
            str = StateBtnMsgShipperProvider.getInstance().getBottomBtnTextOrder(state, mOrderInfo);
        }
        tvBtnOne.setVisibility(TextUtils.isEmpty(str[0]) ? View.GONE : View.VISIBLE);
        tvBtnOne.setText(str[0]);
        tvBtnTwo.setText(str[1]);
    }

    /**
     * 第一个按钮在货主版中的点击事件
     * oneStrShippers = {"修改支付方式", "添加熟车", "修改订单", "重新找车", "删除订单"};
     */
    @Override
    public void onOneBtnClick(String btnText) {
        if (TextUtils.isEmpty(btnText)) {
            return;
        }
        String[] oneStrShippers = StateBtnMsgShipperProvider.getInstance().oneStrShippers;
        if (btnText.equals(oneStrShippers[0])) {
            // 修改支付方式
            new PartialCostDialog(this)
                    .showUpdatePayment(mOrderInfo.getFreight(), new PartialCostDialog.OnConfirmClick
                            () {
                        @Override
                        public void confirmClick(final UpdatePayTypeParams payTypeParams) {
                            payTypeParams.setOrder_id(mOrderInfo.getOrder_id());
                            payTypeParams.setOrder_version(mOrderInfo.getOrder_version());
                            payTypeParams.setPay_style(mOrderInfo.getPay_style());
                            payTypeParamsResult = payTypeParams;
                            SkipControllerProtocol.skipAlterPayType(OrderDetailActivity.this, mOrderInfo.getGoods_id
                                    (), payTypeParams, TAG);
                        }
                    });
        } else if (btnText.equals(oneStrShippers[1])) {
            // 添加熟车
            mPresenter.addOrDeleteCar(mOrderInfo.getDriver_info().getDriver_truck_id(), TruckParams.ADD);
        } else if (btnText.equals(oneStrShippers[2])) {
            // 修改订单 货源
            DeliverGoodsActivity.turnToDeliverGoodsForEdit(this, mGoodsInfo.getGoods_id());
        } else if (btnText.equals(oneStrShippers[3])) {
            // 重新找车 货源 定向订单
            if (mGoodsInfo.getSender_info() != null && mGoodsInfo.getReceiver_info() != null) {
                FindTruckActivity.turnToRefindTruck(this,
                        mGoodsInfo.getSender_info().getCity_id(),
                        mGoodsInfo.getReceiver_info().getCity_id(),
                        mGoodsInfo.getGoods_id());
            }
        } else if (btnText.equals(oneStrShippers[4])) {
            // 删除订单 货源 订单
            if(mDataSource == OrderExtra.GOODS_DETAIL){
                mPresenter.deleteOrder(mGoodsInfo.getGoods_id(), mGoodsInfo.getGoods_version());
            } else {
                mPresenter.deleteOrder(mOrderInfo.getGoods_id(), mOrderInfo.getGoods_version());
            }

        } else if (btnText.equals(oneStrShippers[5])) {
            // 删除熟车
            mPresenter.addOrDeleteCar(mOrderInfo.getDriver_info().getDriver_truck_id(), TruckParams.DELETE);
        }
    }


    /**
     * 第二个按钮在货主版中的点击事件
     * twoStrShippers = {"重新找车", "托管运费", "发送提货码", "货物签收", "我要评价", "查看报价", "发布货源", "查看回评", "复制订单"};
     */
    @Override
    public void onTwoBtnClick(String btnText) {
        if (TextUtils.isEmpty(btnText)) {
            return;
        }
        String[] twoStrShippers = StateBtnMsgShipperProvider.getInstance().twoStrShippers;
        if (btnText.equals(twoStrShippers[0])) { // 重新找车
            // 如果是定向订单，则去找车
            if (mOrderInfo.getIs_assigned().equals("1")) {
                FindTruckActivity.turnToRefindTruck(this,
                        mOrderInfo.getSender_info().getCity_id(),
                        mOrderInfo.getReceiver_info().getCity_id(),
                        mOrderInfo.getGoods_id());
            } else {
                // 跳转到查看报价
                showOffer(mOrderInfo);
            }

        } else if (btnText.equals(twoStrShippers[1])) { // 托管运费
            // 先拿订单号，再去钱包
            // 如果是部分提付费 并且有定金，需要弹出修改支付方式的弹窗，但是不能修改，只能看，不跳转合同页，直接去钱包支付
            if (mOrderInfo.getPay_style().equals("3") && mOrderInfo.getFreight().getAgency_status() > 0) {
                new PartialCostDialog(mContext)
                        .showSetCost(mOrderInfo.getFreight(),
                                true,
                                new PartialCostDialog.OnConfirmClick() {
                                    @Override
                                    public void confirmClick(UpdatePayTypeParams payTypeParams) {
                                        payTypeParams.setOrder_id(mOrderInfo.getOrder_id());
                                        payTypeParams.setOrder_version(mOrderInfo.getOrder_version());
                                        payTypeParams.setPay_style(mOrderInfo.getPay_style());
                                        mPresenter.updatePayType(payTypeParams);
                                    }
                                });
            } else {
                mPresenter.paymentFreight();
            }

        } else if (btnText.equals(twoStrShippers[2])) { // 发送提货码
            AutoDialogHelper.showPickupDialog(this, mOrderInfo.getRefund_info().getIs_oneself_refund(), mOrderInfo
                    .getRefund_info().getRefund_status(), new AutoDialogHelper.OnConfirmListener() {
                @Override
                public void onClick() {
                    mPresenter.sendPickupCode(mOrderInfo.getOrder_id());
                }
            }).show();
        } else if (btnText.equals(twoStrShippers[3])) { // 货物签收
            signOrder();
        } else if (btnText.equals(twoStrShippers[4])) { // 我要评价
            CommentActivity.turnToCommentActivity(this,
                    mOrderInfo.getOrder_id(),
                    mOrderInfo.getOrder_version(),
                    mOrderInfo.getDriver_info().getDriver_id(),
                    mOrderInfo.getDriver_info().getDriver_name());
        } else if (btnText.equals(twoStrShippers[5])) { // 查看报价 货源
            showOffer(mGoodsInfo);
        } else if (btnText.equals(twoStrShippers[6])) { // 发布货源 货源
            // 去发布货源页
            DeliverGoodsActivity.turnToDeliverGoodsForEdit(this, mGoodsInfo.getGoods_id());
        } else if (btnText.equals(twoStrShippers[7])) { // 查看回评
            CommentActivity.turnToCheckCommentActivity(this,
                    mOrderInfo.getOrder_id(),
                    mOrderInfo.getOrder_version());
        } else if (btnText.equals(twoStrShippers[8])) { // 复制订单
            DeliverGoodsActivity.turnToDeliverGoodsForCopy(this,
                    mDataSource == OrderExtra.GOODS_DETAIL ?
                    mGoodsInfo.getGoods_id() : mOrderInfo.getGoods_id());
        }
    }

    /**
     * 设置右边按钮显示
     * 货源详情： 撤销订单 分享货源
     * 订单详情：
     * topMenuTexts = {"撤销订单", "分享货源", "查看合同", "我要退款", "我要投诉", "退款详情"};
     */
    @Override
    public void setRightMenu(int state) {
        String[] menu;
        if (mDataSource == OrderExtra.GOODS_DETAIL) {
            menu = StateRightMenuShipperProvider.getInstance().getRightMenuGoods(mGoodsInfo, state);
        } else {
            menu = StateRightMenuShipperProvider.getInstance().getRightMenuOrder(mOrderInfo, state);
        }
        setTitleMenu(menu);
    }

    @Override
    public void mapClick() {
        if (mDataSource == OrderExtra.ORDER_DETAIL) {
            // 出发地 目的地 经纬度
            TruckExtra extra = new TruckExtra();
            DriverInfo mInfo = mOrderInfo.getDriver_info();
            if (mOrderInfo.getDriver_info() != null) {
                extra.setShowMenu(true);
                extra.setTruckId(mInfo.getDriver_truck_id());
                extra.setLatitude(mInfo.getDriver_latitude());
                extra.setLongitude(mInfo.getDriver_longitude());
                extra.setTruck_plate(mInfo.getLicense_plate_number());
                extra.setTime(mInfo.getDriver_gps_update_time());
                extra.setAddress(mInfo.getDriver_gps_detailed_address());
                extra.setMobile(mInfo.getDriver_mobile());
                extra.setName(mInfo.getDriver_name());
            }
            TruckPositionActivity.truckPosition(this, extra);
        }
    }

    /**
     * 报价列表， 需要运费，是否提付部分运费
     */
    private void showOffer(GoodsInfoResponse data) {
        showOffer(data.getGoods_id(), data.getGoods_version(), data.getPay_style());
    }

    private void showOffer(OrderInfoResponse data) {
        showOffer(data.getGoods_id(), data.getGoods_version(), data.getPay_style());
    }

    private void showOffer(String goodsId, String goodsVersion, String payStyle) {
        ShowOfferExtra extra = new ShowOfferExtra();
        extra.setGoodsId(goodsId);
        extra.setGoodsVersion(goodsVersion);
        extra.setAhead(payStyle.equals("3")); // 是否提付部分运费
        ShowOfferActivity.toShowOffer(this, extra);
    }

    /**
     * 查看过列表
     */

    private void setViewAlready(String viewNum, String callNum) {
        if (pagerTitle == null || viewPager == null) {
            return;
        }
        viewListFragment.setGoodsInfo(mGoodsInfo);
        callListFragment.setGoodsInfo(mGoodsInfo);

        pagerTitle.setVisibility(View.VISIBLE);
        viewPager.setVisibility(View.VISIBLE);
        pagerTitle.setTitles(new String[]{StringUtils.format(R.string.view_list_num,
                StringUtils.getIntToString(viewNum)),
                StringUtils.format(R.string.call_list_num,
                        StringUtils.getIntToString(callNum))});

        getCallAndViewList();

    }


    private void initRefresh() {

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });


    }

    /**
     * 查看过的，拨打过电话的
     */
    private void initViewAlready() {
        viewListFragment = new ListFragment();
        // 添加顶部填充用\n实现
        viewListFragment.setEmptyStr("查看过货源的司机会显示在这里");

        callListFragment = new ListFragment();
        callListFragment.setEmptyStr("拨打过电话的司机会显示在这里");

        fragments.add(viewListFragment);
        fragments.add(callListFragment);

        viewListFragment.setLoadMore(new LoadMoreManager.OnLoadMore() {
            @Override
            public void loadMore() {

                getCallList();
            }
        });

        callListFragment.setLoadMore(new LoadMoreManager.OnLoadMore() {
            @Override
            public void loadMore() {

                getViewList();
            }
        });

        adapter = new PagerFragmentAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(adapter);
        pagerTitle.initData(new String[]{StringUtils.format(R.string.view_list_num, 0), StringUtils.format(R.string
                .call_list_num, 0)}, viewPager, 0);
    }


    /**
     * 查看过的司机列表
     * 拨打过电话的司机列表
     */
    private void getCallAndViewList() {
        //刷新
        pageCallList = 1;
        pageViewList = 1;
        if (callListFragment != null) {
            callListFragment.clearDate();
        }
        if (viewListFragment != null) {
            viewListFragment.clearDate();
        }
        getCallList();
        getViewList();
    }

    private void getCallList() {
        if (mGoodsInfo == null) {
            return;
        }
        mPresenter.getCallPhoneList(mGoodsInfo.getGoods_id(),
                mGoodsInfo.getGoods_version(),
                pageCallList + "",
                new ObserverOnResultListener<GetCallListResponse>() {
                    @Override
                    public void onResult(GetCallListResponse getCallListResponse) {
                        if (getCallListResponse != null) {
                            callListFragment.addData(getCallListResponse.getList(), pageCallList);
                        }
                    }
                }
        );
    }

    private void getViewList() {
        if (mGoodsInfo == null) {
            return;
        }
        mPresenter.getShowGoodsList(mGoodsInfo.getGoods_id(),
                mGoodsInfo.getGoods_version(),
                pageViewList + "",
                new ObserverOnResultListener<GetShowGoodsListResponse>() {
                    @Override
                    public void onResult(GetShowGoodsListResponse getShowGoodsListResponse) {
                        if (getShowGoodsListResponse != null) {
                            viewListFragment.addData(getShowGoodsListResponse.getCheck_on_supply_of_goodss(),
                                    pageViewList);
                        }
                    }
                });
    }


    /**
     * 获取查看列表的头部坐标，来控制悬浮
     */
    private int floatViewTop;
    private boolean isFloat = false;

    private void getAlreadyTop() {
        ViewTreeObserver vto = llViewAlready.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (llViewAlready != null) {
                    floatViewTop = llViewAlready.getTop();
                    Logger.d(" floatViewTop " + floatViewTop);
                }
            }
        });
    }

    /**
     * 监听滑动距离，来控制悬浮
     */
    @Override
    public void onScrollChanged(MyScrollView scrollView, int x, int y, int oldx, int oldy) {
        Logger.d("floatViewHeight y " + y);
        if (y >= floatViewTop) {
            isFloat = true;
            if (pagerTitle.getParent() != llFloat) {
                llPageTitle.removeView(pagerTitle);
                llFloat.addView(pagerTitle);
                pagerTitle.setViewPagerListener();
            }


        } else if (y <= floatViewTop && isFloat) {
            isFloat = false;
            if (pagerTitle.getParent() != llPageTitle) {
                llFloat.removeView(pagerTitle);
                llPageTitle.addView(pagerTitle);
                pagerTitle.setViewPagerListener();
            }
        }
    }

    /**
     * 签收货物
     * 1，运费为托管且到付款不为0；
     * 提示：
     * <p>
     * 确认签收后，运费将直接支付给司机，请确保货物已安全送达。
     * 【取消】/【确定】
     * 点击确定，若订单运费为托管且到付款不为0，需要重新输入支付密码。
     * 货主点击确认签收，开始调取钱包支付，弹出支付密码输入框。密码输入正确后，支付成功，运费支付至司机账户。
     * 只有支付密码输入正确，才算签收成功。否则签收失败。支付成功后，弹出toast提示：签收成功。提示2秒消失后，页面跳转至历史订单--待评价。
     * <p>
     * 2，运费不托管或到付款为0；
     * 提示：
     * <p>
     * 请确保货物已安全送达后
     * 再确认签收
     * 【取消】/【确定】
     * 点击确定，签收成功，订单状态变为已签收状态。弹出toast提示：签收成功。提示2秒消失后，页面跳转至历史订单--待评价。
     * 2，运费不托管或到付款为0；
     */
    private void signOrder() {

        String toast;
        // 到付费状态 为已支付给平台
        final boolean isArrivalsPay = mOrderInfo.getFreight().getDelivery_fee_status() == FreightState.PAY_ALREADY;

        if (isArrivalsPay) {
            toast = "确认签收后，运费将直接支付给司机，请确保货物已安全送达。";
        } else {
            toast = "请确保货物已安全送达后再确认签收";
        }

        AutoDialogHelper.showContent(this, toast, new AutoDialogHelper.OnConfirmListener() {
            @Override
            public void onClick() {
                //  这里需要调用输入支付密码密码的框
                if (isArrivalsPay) {
                    new PayPasswordDialog(OrderDetailActivity.this)
                            .setConfirmListener(new PayPasswordDialog.ConfirmListener() {
                                @Override
                                public void onConfirm(String text) {
                                    // 这里需要调接口
                                    try {
                                        mPresenter.confirmReceivingShipper(mOrderInfo.getOrder_id(), mOrderInfo
                                                .getOrder_version(), MD5Helper.getMD5(text));
                                    } catch (NoSuchAlgorithmException e) {
                                        e.printStackTrace();
                                    }
                                }
                            })
                            .show();
                } else {
                    mPresenter.confirmReceivingShipper(mOrderInfo.getOrder_id(), mOrderInfo.getOrder_version());
                }
            }
        }).show();

    }


    /**
     * 协议页面点击同意
     * 进行修改支付方式接口请求
     */
    @Subscribe
    public void onEvent(ClickAgreeBtnEvent event) {
        if (event.getTag().equalsIgnoreCase(TAG)) {
            // 修改支付方式
            mPresenter.updatePayTypeOrder(payTypeParamsResult);
        }
    }

    /**
     * 关闭页面
     *
     * @param event 事件
     */
    @Subscribe
    public void finishOrderDetail(CancelOrderDetailEvent event) {
        if (event.isCancel()) {
            finishPage();
        }
    }
}
