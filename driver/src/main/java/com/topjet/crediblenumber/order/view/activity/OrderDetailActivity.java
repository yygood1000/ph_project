package com.topjet.crediblenumber.order.view.activity;

import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.topjet.common.base.busManger.Subscribe;
import com.topjet.common.base.dialog.AutoDialogHelper;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.common.modle.extra.RouteExtra;
import com.topjet.common.common.view.activity.CommentActivity;
import com.topjet.common.common.view.activity.ShareDownloadActivity;
import com.topjet.common.order_detail.modle.extra.ClickAgreeBtnEvent;
import com.topjet.common.order_detail.modle.extra.OrderExtra;
import com.topjet.common.order_detail.modle.response.GoodsInfoResponse;
import com.topjet.common.order_detail.modle.response.OrderInfoResponse;
import com.topjet.common.order_detail.modle.state.OrderState;
import com.topjet.common.order_detail.modle.state.StateBtnMsgDriverProvider;
import com.topjet.common.order_detail.modle.state.StateRightMenuDriverProvider;
import com.topjet.common.order_detail.presenter.SkipControllerProtocol;
import com.topjet.common.order_detail.view.activity.OrderDetailBaseActivity;
import com.topjet.common.utils.CheckUserStatusUtils;
import com.topjet.common.utils.GlideUtils;
import com.topjet.common.utils.StringUtils;
import com.topjet.common.widget.SwipeRefreshLayout.SwipeRefreshLayout;
import com.topjet.crediblenumber.R;
import com.topjet.crediblenumber.goods.view.dialog.BidOrAlterDialog;
import com.topjet.crediblenumber.order.view.dialog.CallOwnerInfoBean;
import com.topjet.crediblenumber.order.view.dialog.PickUpCodeDialog;
import com.topjet.crediblenumber.order.view.map.RouteActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * creator: zhulunjun
 * time:    2017/9/5
 * describe: 订单详情，货源详情
 */

public class OrderDetailActivity extends OrderDetailBaseActivity {

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

    /**
     * @param showPickUp 弹出提货码弹窗
     */
    public static void toOrderDetail(MvpActivity activity, String orderId, String showPickUp) {
        activity.turnToActivity(OrderDetailActivity.class, new OrderExtra(orderId, OrderExtra.ORDER_DETAIL,
                showPickUp));
    }

    private OrderExtra extra;

    @Override
    protected int getLayoutResId() {
        extra = getIntentExtra(OrderExtra.getExtraName());
        mDataSource = extra.getDataSource();
        return super.getLayoutResId();

    }

    @Override
    protected void initView() {
        initBottomBtn();
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
    }

    private void getData() {
        if (mDataSource == OrderExtra.GOODS_DETAIL) {
            mPresenter.getGoodsInfoDriver();
        } else {
            mPresenter.getOrderInfoDriver();
        }
    }

    /**
     * 司机版货源详情特有显示
     */
    @Override
    public void setGoodsInfo(GoodsInfoResponse data) {
        mSwipeRefreshLayout.setRefreshing(false);
        //司机显示报价人数,距离,司机信息
        tvRefreshOrder.setVisibility(View.GONE);
        tvOrderQuoteNum.setVisibility(View.VISIBLE);
        rlMapDistance.setVisibility(View.VISIBLE);
        tvBtnTwoRight.setVisibility(View.GONE);

        ViewGroup.LayoutParams layoutParamsPageTitle = pagerTitle.getLayoutParams();
        layoutParamsPageTitle.height = 1;
        ViewGroup.LayoutParams layoutParamsPageView = viewPager.getLayoutParams();
        layoutParamsPageView.height = 1;
        pagerTitle.setLayoutParams(layoutParamsPageTitle);
        viewPager.setLayoutParams(layoutParamsPageView);
        llDriverInfo.setVisibility(View.VISIBLE);
        String quoteNum = "已有<font color='#FF5E5E'>"+data.getOffer_sum()+"</font>人报价。";
        tvOrderQuoteNum.setText(Html.fromHtml(quoteNum));
        // 距离
        String distanceTotal = TextUtils.isEmpty(data.getThe_total_distance()) ? "0" : data.getThe_total_distance();
        String distanceE = TextUtils.isEmpty(data.getDistance()) ? "0" : data.getDistance();
        String displayDistance = StringUtils.format(R.string.order_distance, distanceTotal);
        String displayDistanceE = "";
        // 提货地距离为0则不显示
        if(!distanceE.equals("0")) {
            displayDistanceE = StringUtils.format(R.string.order_pick_up_distance, distanceE);
        }
        tvDistance.setText(displayDistance + displayDistanceE);
        if (data.getOwner_info() != null) {
            // 匿名设置
            String avatarUrl = data.getOwner_info().getOwner_icon_url();
            String name = data.getOwner_info().getOwner_name();
            if (data.getOwner_info().getIs_anonymous()) {
                avatarUrl = "";
                name = "匿名用户";
                if (data.getOwner_info().getIsMan()) {
                    GlideUtils.loaderImageResource(R.drawable.iv_avatar_man, ivUserAvatar);
                } else {
                    GlideUtils.loaderImageResource(R.drawable.iv_avatar_woman, ivUserAvatar);
                }
            }
            // 货主信息
            setUserInfo(name,
                    data.getOwner_info().getOwner_icon_key(),
                    avatarUrl,
                    data.getOwner_info().getOwner_make_a_bargain_sum(),
                    data.getOwner_info().getOwner_mobile(),
                    data.getOwner_info().getOwner_comment_level());
        }
        // 底部按钮，已报价情况的显示
        if (!TextUtils.isEmpty(data.getIs_offer()) && data.getIs_offer().equals("1")) { // 已报价
            llQuote.setVisibility(View.VISIBLE);
            tvQuote.setText(data.getTransport_fee() + "");
            // 待付定金额大于0 则显示出来
            if (StringUtils.getDoubleToString(data.getDeposit_fee()) > 0) {
                llWaitPayDeposit.setVisibility(View.VISIBLE);
                tvWaitPayDeposit.setText(data.getDeposit_fee());
            } else {
                llWaitPayDeposit.setVisibility(View.GONE);
            }
        } else {
            llQuote.setVisibility(View.GONE);
        }

    }

    /**
     * 司机版订单详情特有显示
     */
    @Override
    public void setOrderInfo(OrderInfoResponse data) {
        mSwipeRefreshLayout.setRefreshing(false);
        rlMapDistance.setVisibility(View.GONE);
        llQuote.setVisibility(View.GONE);
        // 货主信息
        ivMessage.setImageResource(R.drawable.iv_icon_message_driver);
        if (data.getOwner_info() != null) {
            setUserInfo(data.getOwner_info().getOwner_name(),
                    data.getOwner_info().getOwner_icon_key(),
                    data.getOwner_info().getOwner_icon_url(),
                    data.getOwner_info().getOwner_make_a_bargain_sum(),
                    data.getOwner_info().getOwner_mobile(),
                    data.getOwner_info().getOwner_comment_level());
        }

        // 隐藏到账标识
        tvArrivedAccount.setVisibility(View.GONE);

        // 自动显示提货码弹窗
        if (extra.getShowPickUp().equals(OrderExtra.SHOW)) {
            showPickUpDialog();
        }
    }

    /**
     * 设置底部按钮，文字显示
     */
    @Override
    public void setBottomBtnText(int orderState) {
        String[] str;
        if (mDataSource == OrderExtra.GOODS_DETAIL) {
            str = StateBtnMsgDriverProvider.getInstance().getBottomBtnTextGoods(mGoodsInfo.getIs_offer());
        } else {
            str = StateBtnMsgDriverProvider.getInstance().getBottomBtnTextOrder(orderState);
        }
        tvBtnOne.setVisibility(TextUtils.isEmpty(str[0]) ? View.GONE : View.VISIBLE);
        tvBtnOne.setText(str[0]);
        tvBtnTwo.setText(str[1]);
    }

    /**
     * 第一个按钮在司机版中的操作
     * oneStrDrivers = {"放弃承接", "地图导航", "撤销报价", "我要分享"};
     */
    @Override
    public void onOneBtnClick(String btnText) {
        if (TextUtils.isEmpty(btnText)) {
            return;
        }
        String[] oneStrDrivers = StateBtnMsgDriverProvider.getInstance().oneStrDrivers;
        if (btnText.equals(oneStrDrivers[0])) { // 放弃承接

            AutoDialogHelper.showGiveUpOrderDialog(this, new AutoDialogHelper.OnConfirmListener() {
                @Override
                public void onClick() {
                    mPresenter.giveUpOrder(mOrderInfo.getOrder_id(), mOrderInfo.getOrder_version());
                }
            }).show();
        } else if (btnText.equals(oneStrDrivers[1])) { // 地图导航
            // 去地图页
            RouteExtra extra;
            CallOwnerInfoBean bean = new CallOwnerInfoBean();
            if (mOrderInfo.getOrder_status() == OrderState.ORDER_DELIVERING) {
                // 送货
                bean.setCallInfo(mOrderInfo, CallOwnerInfoBean.SIGN);
                extra = RouteExtra.getRouteExtraByUserInfo(mOrderInfo.getReceiver_info(), RouteExtra.DELIVERY);
            } else {
                // 提货
                bean.setCallInfo(mOrderInfo, CallOwnerInfoBean.PICK_UP);
                extra = RouteExtra.getRouteExtraByUserInfo(mOrderInfo.getSender_info(), RouteExtra.PICK_UP);
            }
            RouteActivity.toRoute(this, bean.getRouteExtra(extra, this));

        } else if (btnText.equals(oneStrDrivers[2])) { // 撤销报价 货源
            List<String> ids = new ArrayList<>();
            ids.add(mGoodsInfo.getPre_goods_id());
            mPresenter.cancelOffer(ids);
        } else if (btnText.equals(oneStrDrivers[3])) { // 我要分享/
            turnToActivity(ShareDownloadActivity.class);
        }
    }

    /**
     * 第二个按钮在司机版中的操作
     * twoStrDrivers = {"我要承接", "确认提货", "确认签收", "我要评价", "我要接单", "修改报价", "查看回评"};
     */
    @Override
    public void onTwoBtnClick(String btnText) {
        if (TextUtils.isEmpty(btnText)) {
            return;
        }
        String[] twoStrDrivers = StateBtnMsgDriverProvider.getInstance().twoStrDrivers;
        if (btnText.equals(twoStrDrivers[0])) {
            // 我要承接,跳转协议页面
            SkipControllerProtocol.skipAcceptOrder(this, mOrderInfo.getGoods_id(), TAG);
        } else if (btnText.equals(twoStrDrivers[1])) {
            // 确认提货
            showPickUpDialog();
        } else if (btnText.equals(twoStrDrivers[2])) {
            // 确认签收,这里要输入签收码
            CallOwnerInfoBean bean = new CallOwnerInfoBean();
            new PickUpCodeDialog(this, bean.setCallInfo(mOrderInfo, CallOwnerInfoBean.SIGN)).setPickUpCodeListener(new PickUpCodeDialog.PickUpCodeListener() {
                @Override
                public void onPickUpConfirmClick(String pickUpCode) {
                    mPresenter.confirmReceivingDriver(mOrderInfo.getOrder_id(), pickUpCode, mOrderInfo
                            .getOrder_version());
                }
            }).showSignCode();
        } else if (btnText.equals(twoStrDrivers[3])) {
            // 我要评价
            CommentActivity.turnToCommentActivity(this,
                    mOrderInfo.getOrder_id(),
                    mOrderInfo.getOrder_version(),
                    mOrderInfo.getOwner_info().getOwner_id(),
                    mOrderInfo.getOwner_info().getOwner_name());
        } else if (btnText.equals(twoStrDrivers[4])) {
            // 我要接单 货源,跳弹窗，填报价
            CheckUserStatusUtils.isRealNameAndTruckApproved(this, new CheckUserStatusUtils.OnJudgeResultListener() {
                @Override
                public void onSucceed() {
                    new BidOrAlterDialog(OrderDetailActivity.this).showBidView(mGoodsInfo.getGoods_id(),
                            mGoodsInfo.getGoods_version(), new BidOrAlterDialog.OnPayForDepositResultListener() {
                                @Override
                                public void onSucceed() {
                                    mPresenter.getGoodsInfoDriver();
                                }
                            });
                }
            });
        } else if (btnText.equals(twoStrDrivers[5])) {
            // 修改报价 货源,跳弹窗，修改报价
            new BidOrAlterDialog(this).showAlterView(
                    mGoodsInfo.getGoods_id(),
                    mGoodsInfo.getTransport_fee() + "",
                    mGoodsInfo.getDeposit_fee(),
                    mGoodsInfo.getPre_goods_id(),
                    mGoodsInfo.getPre_goods_version(),
                    new BidOrAlterDialog.OnPayForDepositResultListener() {
                        @Override
                        public void onSucceed() {
                            mPresenter.getGoodsInfoDriver();
                        }
                    });
        } else if (btnText.equals(twoStrDrivers[6])) {
            // 查看回评
            CommentActivity.turnToCheckCommentActivity(this,
                    mOrderInfo.getOrder_id(),
                    mOrderInfo.getOrder_version());
        }
    }

    /**
     * 设置右边按钮显示
     */
    @Override
    public void setRightMenu(int state) {
        String[] menu;
        if (mDataSource == OrderExtra.GOODS_DETAIL) {
            menu = StateRightMenuDriverProvider.getInstance().getRightMenuGoods(state);
        } else {
            menu = StateRightMenuDriverProvider.getInstance().getRightMenuOrder(mOrderInfo, state);
        }
        setTitleMenu(menu);
    }

    @Override
    public void mapClick() {
        if (mDataSource == OrderExtra.GOODS_DETAIL) {
            // 出发地 目的地 经纬度
            RouteActivity.toRoute(this, RouteExtra.getRouteExtraByGoodsInfo(mGoodsInfo));
        }
    }


    /**
     * 底部按钮 默认
     */
    private void initBottomBtn() {
        llBtnTwo.setVisibility(View.VISIBLE);
        tvBtnOne.setTextColor(getResources().getColor(R.color.color_6e90ff));
        tvBtnOne.setVisibility(View.GONE);
        llBtnTwo.setBackgroundResource(R.drawable.selector_btn_square_blue);
        tvBtnTwo.setText(getString(R.string.order_receiving));
    }

    /**
     * 显示提货码的弹窗
     */
    private void showPickUpDialog() {
        CallOwnerInfoBean bean = new CallOwnerInfoBean();
        PickUpCodeDialog dialog = new PickUpCodeDialog(this, bean.setCallInfo(mOrderInfo, CallOwnerInfoBean.PICK_UP));
        dialog.setPickUpCodeListener(new PickUpCodeDialog.PickUpCodeListener() {
            @Override
            public void onPickUpConfirmClick(String pickUpCode) {
                mPresenter.confirmPickUpGoods(mOrderInfo.getOrder_id(), pickUpCode, mOrderInfo.getOrder_version());
            }
        });
        // 这里要输入提货码，
        if (TextUtils.isEmpty(mOrderInfo.getPickup_code())) {
            dialog.showDefault();
        } else { // 这里自动带入提货码
            dialog.showAutoCode(mOrderInfo.getPickup_code());
        }
    }

    /**
     * 协议页面点击同意
     * 进行我要承接接口请求
     */
    @Subscribe
    public void onEvent(ClickAgreeBtnEvent event) {
        if (event.getTag().equalsIgnoreCase(TAG)) {
            // 我要承接
            mPresenter.accepyOrder(mOrderInfo.getOrder_id(), mOrderInfo.getOrder_version(), event.getTruckId());
        }
    }

}
