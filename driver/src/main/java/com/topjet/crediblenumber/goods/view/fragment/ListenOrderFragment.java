package com.topjet.crediblenumber.goods.view.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.topjet.common.App;
import com.topjet.common.base.busManger.BusManager;
import com.topjet.common.base.busManger.Subscribe;
import com.topjet.common.base.dialog.AutoDialog;
import com.topjet.common.base.view.fragment.BaseMvpFragment;
import com.topjet.common.common.modle.bean.DestinationListItem;
import com.topjet.common.common.modle.bean.GoodsListData;
import com.topjet.common.common.modle.event.AreaSelectedData;
import com.topjet.common.common.view.dialog.CitySelectPopupWindow;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.order_detail.modle.extra.CityAndLocationExtra;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.PermissionsUtils;
import com.topjet.crediblenumber.R;
import com.topjet.crediblenumber.goods.modle.bean.DepartCityBean;
import com.topjet.crediblenumber.goods.modle.bean.DestinationsSelectEvent;
import com.topjet.crediblenumber.goods.modle.event.ChangeListenOrderStatusEvent;
import com.topjet.crediblenumber.goods.modle.response.GetListenSettingResponse;
import com.topjet.crediblenumber.goods.presenter.ListenOrderPresenter;
import com.topjet.crediblenumber.goods.service.ListenOrderService;
import com.topjet.crediblenumber.goods.view.adapter.GoodsListAdapter;
import com.topjet.crediblenumber.goods.view.dialog.BidOrAlterDialog;
import com.topjet.crediblenumber.goods.view.dialog.DestinationPopup;
import com.topjet.crediblenumber.user.modle.response.GetUsualCityListResponse;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import ezy.assist.compat.SettingsCompat;

/**
 * Created by tsj-004 on 2017/9/8.
 * 听单Fragment
 */

public class ListenOrderFragment extends BaseMvpFragment<ListenOrderPresenter> implements ListenOrderView,
        DestinationPopup.onShowCitySelectPopListener,
        BidOrAlterDialog.OnPayForDepositResultListener {

    @BindView(R.id.cb_listen)
    CheckBox cbListen;
    @BindView(R.id.tv_start)
    TextView tvStart;
    @BindView(R.id.iv_order_list_icon_blueline)
    ImageView ivOrderListIconBlueline;
    @BindView(R.id.tv_end)
    TextView tvEnd;
    @BindView(R.id.tv_listen_order_status)
    TextView tvLlistenOrderStatus;
    @BindView(R.id.rv_contacts)
    RecyclerView rvContacts;
    @BindView(R.id.include)
    View include_location_permissions_fail;

    private boolean isToSystemSetPage = false;      // 是否点击了权限设置按钮，跳转到系统设置界面

    private GoodsListAdapter goodsListAdapter = null;
    private DestinationPopup destinationPopup = null;            // 点击“设置”后的弹窗
    private int popClickViewFlag;       // 出发或目的地
    private AutoDialog permissionHintDialog = null;    // 提示开启悬浮窗权限弹窗
    private AutoDialog closeHintDialog = null;  // 提示是否关闭弹窗

    @Override
    protected void initPresenter() {
        mPresenter = new ListenOrderPresenter(this, mContext, this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_listen_order;
    }

    @Override
    protected void initView(View v) {
        goodsListAdapter = new GoodsListAdapter(mActivity, true);
        goodsListAdapter.setNewData(mPresenter.getListOrderList());
        rvContacts.setAdapter(goodsListAdapter);
        goodsListAdapter.setOnPayForDepositResultListener(this);
        rvContacts.setLayoutManager(new LinearLayoutManager(mContext));
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.getViewData();
        setCheckBoxStatus();

        if (isToSystemSetPage) {
            isToSystemSetPage = false;

            // 检测悬浮窗是否授权
            boolean canDrawOverlays = SettingsCompat.canDrawOverlays(mContext);
            if (canDrawOverlays) {
                mPresenter.startFloatViewService();  // 开启悬浮窗
            } else {
                Toast.makeText(mContext, "悬浮窗权限已被拒绝", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {// 不在最前端界面显示

        } else {// 重新显示到最前端中
            mPresenter.getViewData();
            setCheckBoxStatus();
        }
    }

    @Override
    protected void initData() {
        mPresenter.isFirstRun();
    }

    /**
     * 申请悬浮窗权限
     */
    public void requestOverlayPermission() {
        // 检测悬浮窗是否授权
        boolean canDrawOverlays = SettingsCompat.canDrawOverlays(mContext);
        boolean canWriteSettings = SettingsCompat.canWriteSettings(mContext);

        if (!canDrawOverlays) {
            if (canWriteSettings) {
                // 设置授权状态，仅在未深度定制的 Android 4.3/4.4 上可用
                SettingsCompat.setDrawOverlays(mContext, true);
                SettingsCompat.setWriteSettings(mContext, true);
            }
        }

        if (!canDrawOverlays) {
            permissionHintDialog = new AutoDialog(mContext);
            permissionHintDialog.setTitle("为了更好的体验本应用\n需要您开启悬浮窗权限");
            permissionHintDialog.setRightText("去开启");
            permissionHintDialog.setCanceledOnTouchOutside(false);
            permissionHintDialog.setCancelable(false);
            permissionHintDialog.setOnBothConfirmListener(new AutoDialog.OnBothConfirmListener() {
                @Override
                public void onLeftClick() {
                    permissionHintDialog.toggleShow();
                }

                @Override
                public void onRightClick() {
                    permissionHintDialog.toggleShow();
                    isToSystemSetPage = true;
                    // 跳转到悬浮窗权限设置页
                    SettingsCompat.manageDrawOverlays(mContext);
                }
            });
            permissionHintDialog.toggleShow();
            return;
        }


        mPresenter.startFloatViewService();  // 开启悬浮窗
    }

    @OnClick({R.id.cb_listen, R.id.tv_set_permissions, R.id.tv_set})
    public void onClickViews(View view) {
        int id = view.getId();
        if (id == R.id.cb_listen) {
            boolean isChecked = cbListen.isChecked();
            if (!isChecked) {
                showCloseHint();    // 显示是否关闭听单弹窗
            } else {
                tvLlistenOrderStatus.setText("听单已开启");
                mContext.startService(new Intent(mContext, ListenOrderService.class));  // 开启听单服务（包含网络请求和播报）
                BusManager.getBus().post(new ChangeListenOrderStatusEvent(ChangeListenOrderStatusEvent
                        .CHANGE_FLOATVIEW_STATUS));        // 修改悬浮窗图标状态
                /**
                 * 清空之前的历史列表
                 */
                mPresenter.clearListOrderList();
                goodsListAdapter.setNewData(mPresenter.getListOrderList());
            }
        } else if (id == R.id.tv_set_permissions) {
            PermissionsUtils.goToSyetemSetting(mContext);          // 去权限设置页
        } else if (id == R.id.tv_set) {
            destinationPopup = new DestinationPopup(mActivity, TAG);
            destinationPopup.initPop(mPresenter.getDestinationCityList(), true, mPresenter.getDepartCityBean(), this)
                    .showPop(tvStart);     // 设置出发地目的地
        }
    }

    /**
     * 显示是否关闭听单弹窗
     */
    private void showCloseHint() {
        closeHintDialog = new AutoDialog(getActivity());
        closeHintDialog.setTitle("");
        closeHintDialog.setContent("为保证您能及时收到新货源，建议您打开听单");
        closeHintDialog.setLeftText(R.string.clolse_listen_order);
        closeHintDialog.setRightText(R.string.cancel);
        closeHintDialog.setOnBothConfirmListener(new AutoDialog.OnBothConfirmListener() {
            @Override
            public void onRightClick() {
                closeHintDialog.toggleShow();
                cancel();   // 暂停听单---》点击取消
            }

            @Override
            public void onLeftClick() {
                closeHintDialog.toggleShow();
                close();    // 暂停听单---》点击暂停
            }
        });
        if (!CMemoryData.isDriver()) {
            closeHintDialog.setRightTextColor(App.getInstance().getResources().getColor(com.topjet.common.R.color.shippercolors));
        }
//        closeHintDialog.getWindow().setType((WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));
        closeHintDialog.toggleShow();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (closeHintDialog != null && closeHintDialog.isShowing()) {
            closeHintDialog.dismiss();
        }

        if (permissionHintDialog != null && permissionHintDialog.isShowing()) {
            permissionHintDialog.dismiss();
        }
    }

    /**
     * 暂停听单---》点击暂停
     */
    public void close() {
        BusManager.getBus().post(new ChangeListenOrderStatusEvent(ChangeListenOrderStatusEvent.CLOSE_LISTEN_ORDER));
        // 关闭播报
        BusManager.getBus().post(new ChangeListenOrderStatusEvent(ChangeListenOrderStatusEvent
                .CHANGE_FLOATVIEW_STATUS));        // 修改悬浮窗图标状态
        tvLlistenOrderStatus.setText("听单已暂停");
    }

    /**
     * 暂停听单---》点击取消
     */
    public void cancel() {
        cbListen.setChecked(true);
    }

    @Override
    public void setStartText(String text, boolean isNull, boolean needLocation) {
        if (!isNull) {
            tvStart.setText(text);
            showOrHidePermissionsFail(false);
        }
        if (needLocation) {
            Logger.d("TTT","ccccccccccccclistenSettingResponse.isNeedLocation()");
            mPresenter.doLocation();
        }
    }

    /**
     * 显示或隐藏无法获得定位权限
     */
    public void showOrHidePermissionsFail(boolean show) {
        if (show) {
            include_location_permissions_fail.setVisibility(View.VISIBLE);
        } else {
            include_location_permissions_fail.setVisibility(View.GONE);
        }
    }

    @Override
    public void setEndText(String text) {
        tvEnd.setText(text);
    }

    /**
     * 弹出城市选择控件
     */
    @Override
    public void showCitySelectPop(View v, int flag) {
        popClickViewFlag = flag;
        if (flag == DestinationPopup.DEPART_SELECT) {
            new CitySelectPopupWindow(TAG).showCitySelectPopupWindow(mActivity, tvStart, false, false, true);
        } else if (flag == DestinationPopup.DESTINATION_SELECT) {
            new CitySelectPopupWindow(TAG).showCitySelectPopupWindow(mActivity, tvStart, false, false, true);
        }
    }

    /**
     * 出发地目的地pop中定位回调
     * @param aMapLocation
     */
    @Override
    public void showLocation(AMapLocation aMapLocation) {
        mPresenter.setLocation(aMapLocation);
    }

    /**
     * 设置弹窗的出发地址和自选目的地
     */
    public void setPopAddress(CityAndLocationExtra extra) {
        if (popClickViewFlag == DestinationPopup.DEPART_SELECT) {
            DepartCityBean departCityBean = mPresenter.getDepartCityBean();
            departCityBean.setDepartCityId(extra.getAdCode());
            departCityBean.setDepartCityName(extra.getLastName());
            mPresenter.setDepartCityBean(departCityBean);
            destinationPopup.setDepartCityItem(departCityBean);

            mPresenter.setDepartFromCityAndLocationExtra(extra);
        } else if (popClickViewFlag == DestinationPopup.DESTINATION_SELECT) {
            DestinationListItem destinationListItem = new DestinationListItem();
            destinationListItem.setDestinationName(extra.getCityName());
            destinationListItem.setId(extra.getAdCode());
            destinationListItem.setSelected(true);
            destinationListItem.setIsSelectedBySelf(DestinationListItem.IS_SELF);
            mPresenter.setOptionalFromCityAndLocationExtra(extra);

            destinationPopup.setDestinationCityBySelf(destinationListItem);
        }
    }

    /**
     * 获取常跑城市  的返回参数
     */
    @Subscribe
    public void onEvent(GetUsualCityListResponse getUsualCityListResponse) {
        mPresenter.makeUsualCityData(getUsualCityListResponse);
    }

    /**
     * 获取听单设置  的返回参数
     */
    @Subscribe
    public void onEvent(GetListenSettingResponse listenSettingResponse) {
        mPresenter.makeListenOrderSet(listenSettingResponse);
    }

    /**
     * 点击弹窗的确定按钮
     */
    @Subscribe
    public void onEvent(DestinationsSelectEvent event) {
        mPresenter.clearListOrderList();
        mPresenter.onClickConfirm();
    }

    /**
     * 城市选择器返回数据
     */
    @Subscribe
    public void onEvent(AreaSelectedData selectedData) {
        if (TAG.equals(selectedData.getTag())) {
            mPresenter.makeCitySelectData(selectedData);
        }
    }

    /**
     * 听单列表
     */
    @Subscribe
    public void onEvent(GoodsListData goodsListData) {
        if (goodsListData != null && goodsListData.getGoods_id() != null) {
            mPresenter.add2ListOrderList(goodsListData);
            goodsListAdapter.setNewData(mPresenter.getListOrderList());
        }
    }

    /**
     * 修改听单语音播报状态
     */
    @Subscribe
    public void onEvent(ChangeListenOrderStatusEvent event) {
        String str = event.getType();
        if (str.equals(ChangeListenOrderStatusEvent.CHANGE_CHECKBOX_STATUS)) {
            setCheckBoxStatus();
        }
    }

    @Override
    public void onSucceed() {
        List<GoodsListData> tempList = mPresenter.getListOrderList();
        for (int i = 0; i < tempList.size(); i++) {
            if (tempList.get(i).getGoods_id().equals(goodsListAdapter.biddingGoodsId)) {
                tempList.remove(i);
            }
        }

        mPresenter.setListOrderList(tempList);
        goodsListAdapter.setNewData(tempList);
    }

    /**
     * 设置开关状态
     */
    public void setCheckBoxStatus() {
        boolean sta = CMemoryData.isOrderOpen();
        cbListen.setChecked(sta);

        if (sta) {
            tvLlistenOrderStatus.setText("听单已开启");
        } else {
            tvLlistenOrderStatus.setText("听单已暂停");
        }
    }
}