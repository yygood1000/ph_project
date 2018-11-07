package com.topjet.crediblenumber.goods.view.fragment;

import android.app.Activity;
import android.support.design.widget.AppBarLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.topjet.common.base.busManger.Subscribe;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.common.modle.bean.GoodsListData;
import com.topjet.common.common.modle.event.AreaSelectedData;
import com.topjet.common.common.modle.event.TruckTypeLengthSelectedData;
import com.topjet.common.common.view.dialog.CitySelectPopupWindow;
import com.topjet.common.common.view.dialog.TruckLengthAndTypePopupWindow;
import com.topjet.common.config.CConstants;
import com.topjet.common.config.CPersisData;
import com.topjet.common.resource.bean.AreaInfo;
import com.topjet.common.utils.ListUtils;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.PermissionsUtils;
import com.topjet.common.utils.StringUtils;
import com.topjet.common.widget.AutoScrollTextView;
import com.topjet.common.widget.RecyclerViewWrapper.BaseRecyclerViewFragment;
import com.topjet.common.widget.RecyclerViewWrapper.RecyclerViewWrapper;
import com.topjet.crediblenumber.R;
import com.topjet.crediblenumber.goods.modle.serverAPI.GoodsCommand;
import com.topjet.crediblenumber.goods.modle.serverAPI.GoodsCommandAPI;
import com.topjet.crediblenumber.goods.presenter.ProcessGoodsListAdv;
import com.topjet.crediblenumber.goods.presenter.SmartSearchGoodsPresenter;
import com.topjet.crediblenumber.goods.view.activity.AroundGoodsActivity;
import com.topjet.crediblenumber.goods.view.activity.SubscribeRouteActivity;
import com.topjet.crediblenumber.goods.view.adapter.GoodsListAdapter;
import com.topjet.crediblenumber.goods.view.dialog.BidOrAlterDialog;
import com.topjet.crediblenumber.order.view.activity.MyOfferActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by yy on 2017/8/14.
 * 智能找货Fragment
 */

/**
 * TODO 货运经纪人
 * <p>
 * 在列表无更多数据的时候显示
 * 在列表无货源信息的时候显示
 */
public class SmartSearchGoodsFragment extends BaseRecyclerViewFragment<SmartSearchGoodsPresenter, GoodsListData>
        implements SmartSearchGoodsView<GoodsListData> {

    @BindView(R.id.rvw_smart_goods)
    RecyclerViewWrapper rvwSmartGoods;
    private int DEPART = 0;
    private int DESTINATION = 1;

    @BindView(R.id.appbar_layout)
    AppBarLayout appBarLayout;
    @BindView(R.id.iv_depart_arrows_down)
    ImageView ivDepartArrowsDown;
    @BindView(R.id.iv_destination_arrows_down)
    ImageView ivDestinationArrowsDown;
    @BindView(R.id.iv_truck_arrows_down)
    ImageView ivTruckArrowsDown;
    @BindView(R.id.tv_subscribr_goods_count)
    TextView tvSubscribrGoodsCount;
    @BindView(R.id.tv_depart)
    TextView tvDepart;
    @BindView(R.id.tv_destation)
    TextView tvDestation;
    @BindView(R.id.tv_truck_type_and_length)
    TextView tvTruckTypeAndLength;
    @BindView(R.id.rl_options)
    RelativeLayout rlOptions;
    @BindView(R.id.rl_permission_fail)
    RelativeLayout rlPermissionFail;
    @BindView(R.id.tv_marquee)
    AutoScrollTextView tv_marquee;
    @BindView(R.id.rl_marquee)
    RelativeLayout rlMarquee;


    private int flag;// 城市选择弹出框 标记
    private TruckTypeLengthSelectedData tld;
    CitySelectPopupWindow citySelectPopupWindow;
    TruckLengthAndTypePopupWindow truckLengthAndTypePopupWindow;

    // 是否是查找货源
    public boolean isChangeToSearchGoods;

    @Override
    protected void initPresenter() {
        mPresenter = new SmartSearchGoodsPresenter(this, mContext, this, new GoodsCommand(GoodsCommandAPI.class,
                mActivity));
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_smart_search_goods;
    }

    @Override
    protected void initView(View v) {
        super.initView();
        // 设置不显示没有更多数据布局
//        recyclerViewWrapper.setShowMoreText(false);
        recyclerViewWrapper.getTvEmpty().setText("找不到合适货源");
        recyclerViewWrapper.getTvBtnEmpty().setVisibility(View.GONE);

        // 为AppBarLayout添加 offset change listener同时根据情况启用或者关闭滑动刷新
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == 0) {
                    recyclerViewWrapper.getSwipeRefreshLayout().setEnabled(true);
                } else {
                    recyclerViewWrapper.getSwipeRefreshLayout().setEnabled(false);
                }
            }
        });
        setFinishListener(setDataFinishListener);
        // 初始化PopWindow，避免多次实例化
        initPopWindow();
        // 初始化跑马灯栏
        tv_marquee.init(mActivity.getWindowManager());
    }

    @Override
    protected void initData() {
        tvDepart.setText(R.string.locating);
        mPresenter.getLocate();
        // 获取列表广告数据
        mPresenter.getGoodsListAdv();
        // 获取跑马灯广告
        mPresenter.getMarqueeAdvertisment();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (CPersisData.getIsChangedGoodsItem()) {
                // 布局变更，重新获取适配器
                recyclerViewWrapper.clearUI();
                super.initView();
                CPersisData.setIsChangedGoodsItem(false);
                refresh();
            }
            // 获取订阅路线总数
            mPresenter.getSubscribeRouteGoodsCount();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isHidden()) {
            // 获取订阅路线总数
            mPresenter.getSubscribeRouteGoodsCount();
        }
    }

    @Override
    public GoodsListAdapter getAdapter() {
        Logger.i("oye","getAdapter");
        GoodsListAdapter mAdapter = new GoodsListAdapter((MvpActivity) mContext, true);
        // 设置接单，报价的接口回调
        mAdapter.setOnPayForDepositResultListener(new BidOrAlterDialog.OnPayForDepositResultListener() {
            @Override
            public void onSucceed() {
                refresh();
            }
        });
        return mAdapter;
    }

    @Override
    public void loadData() {
//        recyclerViewWrapper.getAdapter().removeFooterView(mPresenter.economicView);
        // 刷新数据的同时重置广告的插入状态，否则刷新后无法重新插入广告
        if (page == CConstants.PAGE_START) {
            mPresenter.resetAdvInsertStatus();
        }
        if (isChangeToSearchGoods) {
            mPresenter.searchGoodsList(page);
        } else {
            mPresenter.smartSearchGoodsList(page);
        }
    }


    /**
     * 加载成功,对数据进行判断，判断添加货运经纪人布局类型
     */
    @Override
    public void loadSuccess(final List<GoodsListData> data) {
        super.loadSuccess(data);

    }

    /**
     * 单次请求成功并且设置数据完成回调
     */
    RecyclerViewWrapper.OnSetDataFinishListener setDataFinishListener = new RecyclerViewWrapper
            .OnSetDataFinishListener() {
        @Override
        public void onFinish() {
            if (!ListUtils.isEmpty(mPresenter.advListData)) {
                insertAdvInfoToList();
            }
        }
    };

    /**
     * 插入广告
     */
    @Override
    public void insertAdvInfoToList() {
        ProcessGoodsListAdv.insertAdvInfoToList(getData(), mPresenter.advListData);
        // 刷新列表
        notifyDataSetChanged();
    }

    /**
     * 错误布局点击事件
     */
    @Override
    public void errorClick() {
        refresh();
    }

    /**
     * 重写该方法,避免，相同Activity中，多个Fragment，控件Id相同，通过Id获取控件，后面那次获取会把前一次的覆盖
     */
    @Override
    public RecyclerViewWrapper getRecyclerView() {
        return rvwSmartGoods;
    }

    /**
     * 权限获取失败
     */
    @Override
    public void onPermissionFail() {
        recyclerViewWrapper.setVisibility(View.GONE);
        rlPermissionFail.setVisibility(View.VISIBLE);
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
     * 页面点击事件
     */
    @OnClick({R.id.tv_around_goods_map, R.id.tv_my_bidding, R.id.ll_depart, R.id.ll_destination, R.id
            .ll_truck_type_and_length, R.id.rl_subscribe_route, R.id.rl_permission_fail, R.id.iv_close_marquee})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_around_goods_map:
                // 跳转附近货源 地图页面
                turnToActivity(AroundGoodsActivity.class);
                break;
            case R.id.tv_my_bidding:
                // 我的报价
                turnToActivity(MyOfferActivity.class);
                break;
            case R.id.ll_depart:
                // 出发地选择，弹出城市选择器
                showCitySelectPop(DEPART);
                break;
            case R.id.ll_destination:
                // 目的地选择，弹出城市选择器
                showCitySelectPop(DESTINATION);
                break;
            case R.id.ll_truck_type_and_length:
                // 车型车长弹窗
                ivTruckArrowsDown.setBackgroundResource(R.drawable.arrows_down_blue);
                truckLengthAndTypePopupWindow.showPopupWindow(mActivity, rlOptions, false,
                        TruckLengthAndTypePopupWindow.ENTIRETY_NO_SELECTED, false, tld);
                break;
            case R.id.rl_subscribe_route:
                // 跳转订阅路线列表页面
                turnToActivity(SubscribeRouteActivity.class);
                break;
            case R.id.rl_permission_fail:
                // 跳转设置页面
                PermissionsUtils.goToSyetemSetting(mContext);
                break;
            case R.id.iv_close_marquee:
                // 关闭跑马灯
                rlMarquee.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * 初始化PopWindow
     */
    private void initPopWindow() {
        citySelectPopupWindow = new CitySelectPopupWindow(TAG)
                .setOnDismissListener(new CitySelectPopupWindow.OnCustomDismissListener() {
                    @Override
                    public void onDismiss() {
                        if (flag == DEPART) {
                            ivDepartArrowsDown.setBackgroundResource(R.drawable.arrows_down_gray);
                        } else {
                            ivDestinationArrowsDown.setBackgroundResource(R.drawable.arrows_down_gray);
                        }
                    }
                });

        truckLengthAndTypePopupWindow = new TruckLengthAndTypePopupWindow(TAG, true, true)
                .setOnDismissListener(new TruckLengthAndTypePopupWindow.OnCustomDismissListener() {
                    @Override
                    public void onDismiss() {
                        ivTruckArrowsDown.setBackgroundResource(R.drawable.arrows_down_gray);
                    }
                });
    }


    /**
     * 显示城市选择窗口
     */
    private void showCitySelectPop(final int f) {
        this.flag = f;
        if (flag == DEPART) {
            ivDepartArrowsDown.setBackgroundResource(R.drawable.arrows_down_blue);
        } else {
            ivDestinationArrowsDown.setBackgroundResource(R.drawable.arrows_down_blue);
        }
        citySelectPopupWindow.showCitySelectPopupWindow((Activity) mContext, rlOptions, false, true, true);
    }

    /**
     * 起点/目的地返回事件
     */
    @Subscribe
    public void onEvent(AreaSelectedData data) {
        if (data.getTag().equals(TAG)) {
//            // 设置是否请求货源经纪人为true
//            mPresenter.isNeedToGetEconomic = true;
            AreaInfo areaInfo = data.getAreaInfo();
            if (flag == DEPART) {
                setDepart(areaInfo.getLastCityName());
                mPresenter.startCityId = areaInfo.getLastCityId();
            } else if (flag == DESTINATION) {
                tvDestation.setText(areaInfo.getLastCityName());
                mPresenter.destinationCityCode = areaInfo.getLastCityId();
            }
            isChangeToSearchGoods = true;
            refresh();
        }
    }

    /**
     * 车型车长选择
     */
    @Subscribe
    public void onEvent(TruckTypeLengthSelectedData tld) {
        this.tld = tld;
        if (tld.getTag().equals(TAG)) {
            ivTruckArrowsDown.setBackgroundResource(R.drawable.arrows_down_gray);
            mPresenter.truckTypeId = tld.getTypeList().get(0).getId();
            mPresenter.truckLengthId = tld.getLengthList().get(0).getId();
            isChangeToSearchGoods = true;
            refresh();
        }
    }

    /**
     * 设置出发地城市文本框
     */
    @Override
    public void setDepart(String depart) {
        tvDepart.setText(depart);
    }

    /**
     * 设置订阅路线总货源数
     */
    @Override
    public void setGoodsCount(String goods_count) {
        if (StringUtils.isNotBlank(goods_count)) {
            tvSubscribrGoodsCount.setVisibility(View.VISIBLE);
            tvSubscribrGoodsCount.setText(goods_count);
        } else {
            tvSubscribrGoodsCount.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        citySelectPopupWindow = null;
        truckLengthAndTypePopupWindow = null;
    }
}