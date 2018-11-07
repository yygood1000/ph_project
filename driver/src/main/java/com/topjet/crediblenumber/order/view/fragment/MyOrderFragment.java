package com.topjet.crediblenumber.order.view.fragment;

import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.topjet.common.base.busManger.Subscribe;
import com.topjet.common.base.dialog.AutoDialogHelper;
import com.topjet.common.base.listener.ObserverOnResultListener;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.common.modle.extra.RouteExtra;
import com.topjet.common.common.view.activity.CommentActivity;
import com.topjet.common.common.view.activity.ShareDownloadActivity;
import com.topjet.common.order_detail.modle.bean.MyOrderListItem;
import com.topjet.common.order_detail.modle.extra.ClickAgreeBtnEvent;
import com.topjet.common.order_detail.modle.params.OrderIdParams;
import com.topjet.common.order_detail.modle.serverAPI.OrderDetailCommand;
import com.topjet.common.order_detail.modle.serverAPI.OrderDetailCommandApi;
import com.topjet.common.order_detail.modle.state.OrderState;
import com.topjet.common.order_detail.presenter.SkipControllerProtocol;
import com.topjet.common.order_detail.view.dialog.ShareDialog;
import com.topjet.common.utils.CallPhoneUtils;
import com.topjet.common.utils.DelayUtils;
import com.topjet.common.utils.LayoutUtils;
import com.topjet.common.utils.ShareHelper;
import com.topjet.common.utils.StringUtils;
import com.topjet.common.widget.RecyclerViewWrapper.BaseRecyclerViewFragment;
import com.topjet.common.widget.RecyclerViewWrapper.RecyclerViewWrapper;
import com.topjet.crediblenumber.R;
import com.topjet.crediblenumber.order.modle.serverAPI.OrderCommand;
import com.topjet.crediblenumber.order.modle.serverAPI.OrderCommandAPI;
import com.topjet.crediblenumber.order.presenter.MyOrderPresenter;
import com.topjet.crediblenumber.order.view.activity.MyOfferActivity;
import com.topjet.crediblenumber.order.view.activity.OrderDetailActivity;
import com.topjet.crediblenumber.order.view.adapter.MyOrderListAdapter;
import com.topjet.crediblenumber.order.view.dialog.CallOwnerInfoBean;
import com.topjet.crediblenumber.order.view.dialog.PickUpCodeDialog;
import com.topjet.crediblenumber.order.view.map.RouteActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;


/**
 * Created by yy on 2017/8/14.
 * 司机版 我的订单 Fragment
 */

public class MyOrderFragment extends BaseRecyclerViewFragment<MyOrderPresenter, MyOrderListItem>
        implements MyOrderFragmentView<MyOrderListItem> {
    @BindView(R.id.ll_titlebar)
    RelativeLayout llTitlebar;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.iv_gif)
    ImageView ivGif;
    @BindView(R.id.rvw_order)
    RecyclerViewWrapper rvwOrder;
    private boolean isFirst = true;// 是否是第一次进入页面
    //我的订单页  标题集合
    private ArrayList<String> mMyOrderTitleList = new ArrayList<>();
    // 订单状态0:全部 1：待承接 2:承运中 3:已签收
    public int status = 0;
    private String clickItemOrderId;//被点击项的订单id
    private String clickItemOrderVersion;// 被点击项的订单版本号

    @Override
    protected void initPresenter() {
        mPresenter = new MyOrderPresenter(this, mContext, this, new OrderCommand(OrderCommandAPI.class, mActivity));
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_my_order;
    }

    @Override
    protected void initView(View v) {
        super.initView();
        recyclerViewWrapper.getTvBtnEmpty().setVisibility(View.GONE);
        recyclerViewWrapper.getTvEmpty().setText("暂无货源");
        initTabLayout();
    }

    @Override
    protected void initData() {
        refresh();
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

    /**
     * 跳转 我的报价
     */
    @OnClick(R.id.tv_my_bidding)
    public void onViewClicked() {
        turnToActivity(MyOfferActivity.class);
    }

    @Override
    public void errorClick() {
        refresh();
    }

    @Override
    public void emptyClick() {
        refresh();
    }

    @Override
    public BaseQuickAdapter getAdapter() {
        MyOrderListAdapter mAdapter = new MyOrderListAdapter(mContext);
        mAdapter.setOnBtnClickListener(onBtnClickListener);
        return mAdapter;
    }

    @Override
    public RecyclerViewWrapper getRecyclerView() {
        return rvwOrder;
    }

    @Override
    public void loadData() {
        mPresenter.getMyOrderListData(status, page);
    }


    /**
     * 我的订单 按钮点击
     */
    private MyOrderListAdapter.OnBtnClickListener onBtnClickListener = new MyOrderListAdapter.OnBtnClickListener() {
        // 呼叫货主
        @Override
        public void clickCallPhone(View v, String shipper, String mobile) {
            new CallPhoneUtils().showCallDialogWithAdvNotUpload(mActivity, v, shipper, mobile, 0);
        }

        // 地图导航
        @Override
        public void clickNavigation(MyOrderListItem item) {
            // 去地图页
            RouteExtra extra;
            CallOwnerInfoBean bean;
            int orderState = StringUtils.getIntToString(item.getOrder_status());
            if (orderState == OrderState.ORDER_DELIVERING) {
                // 送货
                bean = getPickUpBean(item, CallOwnerInfoBean.SIGN);
                extra = RouteExtra.getRouteExtraByUserInfo(item.getReceiver_info(), RouteExtra.DELIVERY);
            } else {
                // 提货
                bean = getPickUpBean(item, CallOwnerInfoBean.PICK_UP);
                extra = RouteExtra.getRouteExtraByUserInfo(item.getSender_info(), RouteExtra.PICK_UP);
            }
            RouteActivity.toRoute((MvpActivity) getActivity(), bean.getRouteExtra(extra, getActivity()));
        }

        // 确认签收
        @Override
        public void clickConfirmSign(final MyOrderListItem item) {

            new PickUpCodeDialog(mActivity, getPickUpBean(item, CallOwnerInfoBean.SIGN))
                    .setPickUpCodeListener(new PickUpCodeDialog.PickUpCodeListener() {
                        @Override
                        public void onPickUpConfirmClick(String pickUpCode) {
                            mPresenter.uploadSignCode(item.getOrder_id(), item.getOrder_version(), pickUpCode, item
                                            .getDelivery_fee(),
                                    item.getDelivery_fee_status());
                        }
                    }).showSignCode();

        }

        // 确认提货
        @Override
        public void clickConfirmPickUp(final MyOrderListItem item) {
            PickUpCodeDialog dialog = new PickUpCodeDialog(mContext, getPickUpBean(item, CallOwnerInfoBean.PICK_UP))
                    .setPickUpCodeListener(new PickUpCodeDialog.PickUpCodeListener() {
                        @Override
                        public void onPickUpConfirmClick(String pickUpCode) {
                            mPresenter.uploadPickUpCode(item.getOrder_id(), item.getOrder_version(), pickUpCode);
                        }
                    });

            if (StringUtils.isNotBlank(item.getPickup_code())) {
                dialog.showAutoCode(item.getPickup_code());
            } else {
                dialog.showDefault();
            }
        }

        // 放弃承接
        @Override
        public void clickGiveUpUndertake(final String order_id, final String order_version) {
            AutoDialogHelper.showGiveUpOrderDialog(mActivity, new AutoDialogHelper.OnConfirmListener() {
                @Override
                public void onClick() {
                    new OrderDetailCommand(OrderDetailCommandApi.class, mActivity)
                            .giveUpOrder(new OrderIdParams(order_id, order_version),
                                    new ObserverOnResultListener<Object>() {
                                        @Override
                                        public void onResult(Object o) {
                                            refresh();
                                        }
                                    });
                }
            }).show();
        }

        // 我要承接
        @Override
        public void clickDoUndertake(String goodsId, final String orderId, final String orderVersion) {
            clickItemOrderId = orderId;
            clickItemOrderVersion = orderVersion;
            SkipControllerProtocol.skipAcceptOrder(mActivity, goodsId, TAG);
        }

        // 评价赚积分
        @Override
        public void clickComment(MyOrderListItem item) {
            CommentActivity.turnToCommentActivity(mActivity,
                    item.getOrder_id(),
                    item.getOrder_version(),
                    item.getOwner_id(),
                    item.getOwner_name());
        }

        // 好友分享
        @Override
        public void clickShare(String orderId) {
            turnToActivity(ShareDownloadActivity.class);
        }

        @Override
        public void clickItem(MyOrderListItem item) {
            // 跳转订单详情
            OrderDetailActivity.toOrderDetail(mActivity, item.getOrder_id());
        }

        @Override
        public void clickCheckComment(String orderId, String orderVersion) {
            CommentActivity.turnToCheckCommentActivity(mActivity, orderId, orderVersion);
        }
    };

    /**
     * 设置显示弹窗的数据
     */
    private CallOwnerInfoBean getPickUpBean(MyOrderListItem item, int type) {
        CallOwnerInfoBean bean = new CallOwnerInfoBean();
        bean.setOrder_id(item.getOrder_id());
        bean.setOrder_state(item.getOrder_status() + "");
        bean.setType(type);
        if (item.getReceiver_info() != null) {
            bean.setReceiver_mobile(item.getReceiver_info().getMobile());
            bean.setReceiver_name(item.getReceiver_info().getName());
        }
        if (item.getSender_info() != null) {
            bean.setSender_mobile(item.getSender_info().getMobile());
            bean.setSender_name(item.getSender_info().getName());
        }
        if (item.getOwner_mobile() != null) {
            bean.setOwner_mobile(item.getOwner_mobile());
            bean.setOwner_name(item.getOwner_name());
        }
        return bean;
    }

    /**
     * 协议页面点击同意
     * 进行我要承接接口请求
     */
    @Subscribe
    public void onEvent(ClickAgreeBtnEvent event) {
        if (event.getTag().equalsIgnoreCase(TAG)) {
            // 我要承接
            mPresenter.accepyOrder(clickItemOrderId, clickItemOrderVersion, event.getTruckId());
        }
    }

    /**
     * 分享图片
     */
    @Override
    public void shareImage(final String path) {
        new ShareDialog(mContext)
                .setShareClickListener(new ShareDialog.ShareClickListener() {
                    @Override
                    public void weChatClick() {
                        ShareHelper.shareImage(mActivity, Wechat.NAME, path, mPresenter.shareComplete);
                    }

                    @Override
                    public void circleClick() {
                        ShareHelper.shareImage(mActivity, WechatMoments.NAME, path, mPresenter.shareComplete);
                    }
                }).show();
    }


    /**
     * 初始化TabLayout
     */
    public void initTabLayout() {
        mMyOrderTitleList.add("全部");
        mMyOrderTitleList.add("待承接");
        mMyOrderTitleList.add("承运中");
        mMyOrderTitleList.add("已签收");

        // 设置TabLayout 标签内容
        setTabLayoutContent();
        // 设置 订单状态标签 切换 监听回调
        tabLayout.addOnTabSelectedListener(onTabSelectedListener);
    }

    /**
     * 设置TabLayout 标签内容
     */
    private void setTabLayoutContent() {
        // 刷新TanLayout中的内容
        for (int i = 0; i < mMyOrderTitleList.size(); i++) {
            if (i == 0) {
                tabLayout.addTab(tabLayout.newTab().setText(mMyOrderTitleList.get(i)), true);
            } else {
                tabLayout.addTab(tabLayout.newTab().setText(mMyOrderTitleList.get(i)));
            }
        }
        // 设置下划线长度
        LayoutUtils.setIndicator(tabLayout, 22, 22);

    }

    /**
     * TabLayout标签切换回调监听,请求服务端
     */
    private TabLayout.OnTabSelectedListener onTabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            // 更改请求的列表参数
            status = tab.getPosition();
            // 请求服务端获取相应状态的我的订单列表
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
     * 切换标签
     *
     * @param index 下标
     */
    @Override
    public void changePage(int index) {
        if (index != -1) {
            if (!tabLayout.getTabAt(index).isSelected()) {
                tabLayout.getTabAt(index).select();
            } else {
                refresh();
            }
        }
    }

    @Override
    public void showGif() {
        if (ivGif != null) {
            ivGif.setVisibility(View.VISIBLE);
            Glide.with(this).load(com.topjet.common.R.drawable.money_down).asGif()
                    .listener(new RequestListener<Integer, GifDrawable>() {
                        @Override
                        public boolean onException(Exception e, Integer model, Target<GifDrawable> target, boolean
                                isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GifDrawable resource, Integer model, Target<GifDrawable>
                                target, boolean isFromMemoryCache, boolean isFirstResource) {
                            hiddenGifView();
                            return false;
                        }
                    }).into(ivGif);
        }

    }

    /**
     * 隐藏gif
     */
    private void hiddenGifView() {
        DelayUtils.delay(1000 * 3, new DelayUtils.OnListener() {
            @Override
            public void onListener() {
                ivGif.setVisibility(View.GONE);
            }
        });
    }
}
