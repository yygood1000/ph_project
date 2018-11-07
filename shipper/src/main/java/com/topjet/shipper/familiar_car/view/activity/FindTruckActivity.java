package com.topjet.shipper.familiar_car.view.activity;

import android.app.Activity;
import android.support.design.widget.AppBarLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.topjet.common.base.CommonProvider;
import com.topjet.common.base.busManger.Subscribe;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.common.modle.bean.AroundMapExtra;
import com.topjet.common.common.modle.bean.TruckInfo;
import com.topjet.common.common.modle.event.AreaSelectedData;
import com.topjet.common.common.modle.event.TruckTypeLengthSelectedData;
import com.topjet.common.common.modle.params.TruckParams;
import com.topjet.common.common.view.dialog.CitySelectPopupWindow;
import com.topjet.common.common.view.dialog.TruckLengthAndTypePopupWindow;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.resource.bean.AreaInfo;
import com.topjet.common.resource.dict.AreaDataDict;
import com.topjet.common.utils.CallPhoneUtils;
import com.topjet.common.utils.CheckUserStatusUtils;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.PermissionsUtils;
import com.topjet.common.utils.ResourceUtils;
import com.topjet.common.widget.MyTitleBar;
import com.topjet.common.widget.RecyclerViewWrapper.BaseRecyclerViewActivity;
import com.topjet.shipper.R;
import com.topjet.shipper.deliver.view.activity.DeliverGoodsActivity;
import com.topjet.shipper.familiar_car.model.extra.RefindTruckExtra;
import com.topjet.shipper.familiar_car.model.extra.TruckExtra;
import com.topjet.shipper.familiar_car.model.serverAPI.TruckCommand;
import com.topjet.shipper.familiar_car.model.serverAPI.TruckCommandAPI;
import com.topjet.shipper.familiar_car.presenter.FindTruckPresenter;
import com.topjet.shipper.familiar_car.view.adapter.TruckListAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by yy on 2017/10/18.
 * 我要用车
 */

public class FindTruckActivity extends BaseRecyclerViewActivity<FindTruckPresenter, TruckInfo> implements
        FindTruckView<TruckInfo> {
    @BindView(R.id.tv_depart)
    TextView tvDepart;
    @BindView(R.id.iv_depart_arrows_down)
    ImageView ivDepartArrowsDown;
    @BindView(R.id.tv_destation)
    TextView tvDestation;
    @BindView(R.id.iv_destination_arrows_down)
    ImageView ivDestinationArrowsDown;
    @BindView(R.id.tv_truck_type_and_length)
    TextView tvTruckTypeAndLength;
    @BindView(R.id.iv_truck_arrows_down)
    ImageView ivTruckArrowsDown;
    @BindView(R.id.rl_options)
    RelativeLayout rlOptions;
    @BindView(R.id.appbar_layout)
    AppBarLayout appbarLayout;
    @BindView(R.id.rl_permission_fail)
    RelativeLayout rlPermissionFail;

    private boolean isFirstIn = true;// 是否是第一次进入该页面
    private int flag;
    private int SELECT_DEPART = 1;
    private int SELECT_DESTINITION = 2;
    CitySelectPopupWindow citySelectPopupWindow;
    TruckLengthAndTypePopupWindow truckLengthAndTypePopupWindow;


    @Override
    protected void initPresenter() {
        mPresenter = new FindTruckPresenter(this, this, new TruckCommand(TruckCommandAPI.class, this));
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_find_truck;
    }

    @Override
    protected void initTitle() {
        super.initTitle();
        getMyTitleBar()
                .setMode(MyTitleBar.Mode.BACK_TITLE_RIMG)
                .setTitleText(ResourceUtils.getString(R.string.use_truck))
                .setRightImg(R.drawable.iv_icon_around_map);
    }

    @Override
    protected void initView() {
        super.initView();
        recyclerViewWrapper.getIvEmpty().setImageResource(R.drawable.icon_empty_familiar_car);
        recyclerViewWrapper.getTvEmpty().setText("符合条件的共有0辆车\n请重新选择条件查找！");
        recyclerViewWrapper.getTvEmpty().setTextSize(13);
        recyclerViewWrapper.getIvEmpty().setBackgroundResource(R.drawable.icon_empty);
        recyclerViewWrapper.getTvBtnEmpty().setVisibility(View.GONE);
        recyclerViewWrapper.getTvBtnError().setVisibility(View.GONE);
        // 为AppBarLayout添加 offset change listener同时根据情况启用或者关闭滑动刷新
        appbarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == 0) {
                    recyclerViewWrapper.getSwipeRefreshLayout().setEnabled(true);
                } else {
                    recyclerViewWrapper.getSwipeRefreshLayout().setEnabled(false);
                }
            }
        });

        // 初始化PopWindow，避免多次实例化
        initPopWindow();

    }

    @Override
    protected void initData() {
        if (mPresenter.isNeedLocat) {
            tvDepart.setText(R.string.locating);
            mPresenter.getLocate();
        } else {
            refresh();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //  从内存中获取 可能在附近车源你页面设置的参数
        if (isFirstIn) {
            isFirstIn = false;
        } else {
            if (CMemoryData.getAroundMapExtra() != null && CMemoryData.getAroundMapExtra().isNeedGetParams()) {
                // 获取过一次数据后，将标签置为false
                CMemoryData.getAroundMapExtra().setNeedGetParams(false);
                mPresenter.startCityId = CMemoryData.getAroundMapExtra().departCityId;
                mPresenter.destinationCityId = CMemoryData.getAroundMapExtra().destinationCityId;
                mPresenter.truckSelectedData = CMemoryData.getAroundMapExtra().truckSelectedData;
                tvDepart.setText(AreaDataDict.getCityItemByAdcode(mPresenter.startCityId).getCityFullName()
                        .replace("市", "")
                        .replace("省", "")
                );
                tvDestation.setText(CMemoryData.getAroundMapExtra().destinationCityName);
                if (mPresenter.truckSelectedData != null) {
                    setTruckInfo(mPresenter.truckSelectedData.getLengthList().get(0).getDisplayName(),
                            mPresenter.truckSelectedData.getTypeList().get(0).getDisplayName());
                }
                refresh();
            }
        }
    }

    @Override
    public BaseQuickAdapter getAdapter() {
        TruckListAdapter mAdapter = new TruckListAdapter(this);
        mAdapter.setFamiliarCarClick(new TruckListAdapter.FamiliarCarClick() {
            @Override
            public void contentClick(TruckInfo item) {
                // 车辆详情
                TruckInfoActivity.truckInfo((MvpActivity) mContext, item.getTruck_id());
            }

            @Override
            public void deleteClick(TruckInfo item) {
                // 删除熟车
                mPresenter.addOrDeleteCar(item.getTruck_id(), TruckParams.DELETE);
            }

            @Override
            public void addClick(TruckInfo item) {
                // 添加熟车
                mPresenter.addOrDeleteCar(item.getTruck_id(), TruckParams.ADD);
            }

            @Override
            public void placeOrderClick(final TruckInfo item) {
                // 给他下单存在两种情况
                //1.直接进入我要用车页面点击给他下单
                //2.从重新找车入口进入我要用车页面点击给他下单
                CheckUserStatusUtils.isRealNameAuthentication(FindTruckActivity.this, new CheckUserStatusUtils
                        .OnJudgeResultListener() {
                    @Override
                    public void onSucceed() {
                        ArrayList<String> truckTypeId = new ArrayList<>();
                        ArrayList<String> truckLehgthId = new ArrayList<>();
                        truckTypeId.add(item.getTruck_type_id());
                        truckLehgthId.add(item.getTruck_length_id());
                        if (null == mPresenter.extra) {
                            DeliverGoodsActivity.turnToDeliverGoodsForAddAssigned((MvpActivity)
                                    mContext, item.getDriver_id(), item.getTruck_id(), truckTypeId, truckLehgthId);
                        } else {
                            DeliverGoodsActivity.turnToDeliverGoodsForAddAssignedIncidentalGoodsId((MvpActivity)
                                    mContext, item.getDriver_id(), item.getTruck_id(), truckTypeId, truckLehgthId);
                        }
                    }
                });
            }

            @Override
            public void callClick(TruckInfo item) {
                // 拨打电话
                new CallPhoneUtils().showCallDialogWithAdvNotUpload((MvpActivity) mContext, recyclerViewWrapper,
                        item.getDriver_name(),
                        item.getDriver_mobile(),
                        3);
            }

            @Override
            public void messageClick(TruckInfo item) {
                // 聊天
                CommonProvider.getInstance().getJumpChatPageProvider()
                        .jumpChatPage(FindTruckActivity.this, item.getUserBean(item));
            }

            @Override
            public void clickDriverLocation(TruckInfo item) {
                // 跳转司机位置地图页面
                TruckExtra extra = new TruckExtra();
                extra.setTruckId(item.getTruck_id());
                extra.setLatitude(item.getGps_latitude());
                extra.setLongitude(item.getGps_longitude());
                extra.setTruck_plate(item.getPlateNo());
                extra.setAddress(item.getGps_address());
                extra.setName(item.getDriver_name());
                extra.setMobile(item.getDriver_mobile());
                extra.setTime(item.getGps_update_time());
                TruckPositionActivity.truckPosition((MvpActivity) mContext, extra, true);
            }
        });
        return mAdapter;
    }

    @Override
    public void loadData() {
        mPresenter.getTruckList(page);
    }

    @Override
    protected void onClickRightImg() {
        // 跳转附近车源页面
        AroundMapExtra extra = new AroundMapExtra(
                mPresenter.lat,
                mPresenter.lng,
                mPresenter.startCityId,
                mPresenter.destinationCityId,
                mPresenter.truckSelectedData,
                mPresenter.departCityNmae,
                mPresenter.destinitionCityNmae
        );
        Logger.i("oye", "跳转地图页面前的参数设置 == " + extra.toString());
        CMemoryData.setAroundMapExtra(extra);
        turnToActivity(AroundTruckMapActivity.class);
    }

    @Override
    public void onPermissionFail() {
        recyclerViewWrapper.setVisibility(View.GONE);
        rlPermissionFail.setVisibility(View.VISIBLE);
    }

    /**
     * 设置出发地文本框
     */
    @Override
    public void setDepart(String depart) {
        tvDepart.setText(depart);
    }

    /**
     * 设置目的地文本框
     */
    @Override
    public void setDestination(String destination) {
        tvDestation.setText(destination);
    }

    /**
     * 设置车型车长文本框
     */
    @Override
    public void setTruckInfo(String truckLength, String truckType) {
        tvTruckTypeAndLength.setText(truckLength + ("/" + truckType));
    }

    @Override
    public void hideTitleRightImage() {
        getMyTitleBar()
                .setMode(MyTitleBar.Mode.BACK_TITLE)
                .setTitleText(ResourceUtils.getString(R.string.use_truck));
    }

    @OnClick({R.id.ll_depart, R.id.ll_destination, R.id.ll_truck_type_and_length, R.id.rl_permission_fail})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_depart:
                showCitySelectPop(SELECT_DEPART);
                break;
            case R.id.ll_destination:
                showCitySelectPop(SELECT_DESTINITION);
                break;
            case R.id.ll_truck_type_and_length:
                ivTruckArrowsDown.setBackgroundResource(R.drawable.arrows_down_green);
                truckLengthAndTypePopupWindow.showPopupWindow((Activity) mContext, rlOptions, false,
                        TruckLengthAndTypePopupWindow.ENTIRETY_NO_SELECTED, false, mPresenter.truckSelectedData);
                break;
            case R.id.rl_permission_fail:
                // 跳转设置页面
                PermissionsUtils.goToSyetemSetting(mContext);
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
                        if (flag == SELECT_DEPART) {
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
        if (flag == SELECT_DEPART) {
            ivDepartArrowsDown.setBackgroundResource(R.drawable.arrows_down_green);
        } else {
            ivDestinationArrowsDown.setBackgroundResource(R.drawable.arrows_down_green);
        }
        citySelectPopupWindow.showCitySelectPopupWindow(this, rlOptions, false, false, true);
    }

    /**
     * 起点/目的地返回事件
     */
    @Subscribe
    public void onEvent(AreaSelectedData data) {
        if (data.getTag().equals(TAG)) {
            AreaInfo areaInfo = data.getAreaInfo();
            if (flag == SELECT_DEPART) {
                mPresenter.departCityNmae = areaInfo.getFullCityName();
                setDepart(areaInfo.getLastCityName());
                //TODO 设置出发地参数，用于请求，也用于参数共享
                mPresenter.startCityId = areaInfo.getLastCityId();
                mPresenter.lat = Double.parseDouble(areaInfo.getLastCityItem().getLatitude());
                mPresenter.lng = Double.parseDouble(areaInfo.getLastCityItem().getLongitude());
            } else if (flag == SELECT_DESTINITION) {
                tvDestation.setText(areaInfo.getLastCityName());
                //TODO 设置目的地参数，用于请求，也用于参数共享
                mPresenter.destinitionCityNmae = areaInfo.getFullCityName();
                mPresenter.destinationCityId = areaInfo.getLastCityId();
            }
            refresh();
        }
    }

    /**
     * 车型车长选择
     */
    @Subscribe
    public void onEvent(TruckTypeLengthSelectedData tld) {
        if (tld.getTag().equals(TAG)) {
            //TODO 设置车辆参数，用于请求，也用于参数共享
            mPresenter.truckSelectedData = tld;
            setTruckInfo(tld.getLengthList().get(0).getDisplayName(), tld.getTypeList().get(0).getDisplayName());
            refresh();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        citySelectPopupWindow = null;
        truckLengthAndTypePopupWindow = null;
    }

    /**
     * 重新找车跳转
     */
    public static void turnToRefindTruck(MvpActivity activity, String departCityId, String destinationCityId, String
            goodsId) {
        //在内存中记录重新找车的货源的id
        CMemoryData.setTempGoodsId(goodsId);
        RefindTruckExtra extra = new RefindTruckExtra();
        extra.setDepartCityId(departCityId);
        extra.setDestinationCityId(destinationCityId);
        extra.setDepartCityName(AreaDataDict.replaceCityAndProvinceString(AreaDataDict.getCityItemByAdcode
                (departCityId).getCityName()));
        extra.setDestinationCityName(AreaDataDict.replaceCityAndProvinceString(AreaDataDict.getCityItemByAdcode
                (destinationCityId).getCityName()));
        extra.setRefund(true);
        activity.turnToActivity(FindTruckActivity.class, extra);
    }

}
