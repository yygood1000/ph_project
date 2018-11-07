package com.topjet.crediblenumber.goods.view.dialog;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.topjet.common.base.busManger.BusManager;
import com.topjet.common.base.listener.DebounceClickListener;
import com.topjet.common.common.modle.bean.DestinationListItem;
import com.topjet.common.common.modle.bean.UsualCityBean;
import com.topjet.common.common.view.dialog.TruckLengthAndTypePopupWindow;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.utils.ListUtils;
import com.topjet.common.utils.LocationUtils;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.ScreenUtils;
import com.topjet.crediblenumber.R;
import com.topjet.crediblenumber.goods.modle.bean.DepartCityBean;
import com.topjet.crediblenumber.goods.modle.bean.DestinationsSelectEvent;
import com.topjet.crediblenumber.goods.view.adapter.DestinationAdapter;

import java.util.ArrayList;

/**
 * 目的地POP
 * 听单设置 出发地目的地弹窗也是这个类
 */
public class DestinationPopup {
    private Activity mActivity = null;
    private PopupWindow popupWindow = null;
    private DestinationAdapter mAdapter;
    private ArrayList<DestinationListItem> destinationList;
    private String tag;
    private onShowCitySelectPopListener listener;

    public static int DEPART_SELECT = 0;// 听单 出发地选择标签
    public static int DESTINATION_SELECT = 1;// 目的地 手动选择标签

    /**
     * 听单页面 出发地信息
     */
    private DepartCityBean departCityItem;// 出发地城市实体类
    private TextView tvDepart;// 出发地  文本框

    private TruckLengthAndTypePopupWindow.OnCustomDismissListener onDismissListener;

    public interface OnCustomDismissListener {
        void onDismiss();
    }

    public void setOnDismissListener(TruckLengthAndTypePopupWindow.OnCustomDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    /**
     * 自选城市点击回调
     */
    public interface onShowCitySelectPopListener {
        void showCitySelectPop(View v, int flag);
        void showLocation(AMapLocation aMapLocation);
    }

    public DestinationPopup(Activity activity, String tag) {
        this.mActivity = activity;
        this.tag = tag;
    }

    /**
     * 听单页面使用的 初始化方法
     *
     * @param destinationList 目的地集合
     * @param isListenOrder   是否是听单
     * @param departCityItem  出发地城市对象
     */
    public DestinationPopup initPop(ArrayList<DestinationListItem> destinationList, boolean isListenOrder,
                                    DepartCityBean departCityItem, onShowCitySelectPopListener listener) {
        this.destinationList = destinationList;
        this.listener = listener;

        this.departCityItem = departCityItem;
        popupWindow = new PopupWindow(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        View view;
        Logger.i("oye", "目的地弹窗页面初始化");
        if (isListenOrder) {
            view = getViewOnListenOrder();
        } else {
            view = getView();
        }
        popupWindow.setContentView(view);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                CMemoryData.setDestinationCityDatas(getDestinationList());
                if (onDismissListener != null) {
                    onDismissListener.onDismiss();
                }
            }
        });

        return this;
    }

    /**
     * 附近货源/智能找货 页面使用的初始化方法
     */
    public DestinationPopup initPop(ArrayList<DestinationListItem> destinationList, onShowCitySelectPopListener
            listener) {
        initPop(destinationList, false, null, listener);
        return this;
    }

    /**
     * ====================获取智能找货/附近货源 目的地 弹窗布局=====================
     */

    private View getView() {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.ppw_show_destination, null);
        // RecyclerView 相关代码
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        ViewGroup.LayoutParams layoutParams = recyclerView.getLayoutParams();

        //根据列表长度，动态设置RecylerView 高度
        if (destinationList.size() < 3) {
            layoutParams.height = ScreenUtils.dp2px(mActivity, 48.5f * 3);
        } else if (destinationList.size() > 8) {
            layoutParams.height = ScreenUtils.dp2px(mActivity, 48.5f * 8);
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mAdapter = new DestinationAdapter();
        mAdapter.setOnItemClickListener(onItemClickListener);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setNewData(destinationList);

        // 确认按钮点击
        Button btnConfirm = (Button) view.findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener(clickListener);

        // 背景布局
        RelativeLayout llParents = (RelativeLayout) view.findViewById(R.id.rl_parents);
        llParents.setOnClickListener(clickListener);
        return view;
    }

    /**
     * ==================== 获取听单页面 出发地 目的地 弹窗界面=====================
     */

    private View getViewOnListenOrder() {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.ppw_show_destination_listen_order, null);
        // RecyclerView 相关代码
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        tvDepart = (TextView) view.findViewById(R.id.tv_depart);

        ViewGroup.LayoutParams layoutParams = recyclerView.getLayoutParams();
        //根据列表长度，动态设置RecylerView 高度
        if (destinationList.size() > 4) {
            layoutParams.height = ScreenUtils.dp2px(mActivity, 48.5f * 4);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mAdapter = new DestinationAdapter();
        mAdapter.setOnItemClickListener(onItemClickListener);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setNewData(destinationList);

        // 设置初始出发地
        tvDepart.setText(departCityItem.getDepartCityName());

        // 确认按钮点击
        Button btnConfirm = (Button) view.findViewById(R.id.btn_confirm);
        Button btnLocate = (Button) view.findViewById(R.id.btn_locate);
        RelativeLayout rlDepart = (RelativeLayout) view.findViewById(R.id.rl_depart);
        btnConfirm.setOnClickListener(clickListener);
        btnLocate.setOnClickListener(clickListener);
        rlDepart.setOnClickListener(clickListener);

        // 背景布局
        RelativeLayout rlParents = (RelativeLayout) view.findViewById(R.id.rl_parents);
        rlParents.setOnClickListener(clickListener);
        return view;
    }

    /**
     * 页面内点击事件
     */
    private DebounceClickListener clickListener = new DebounceClickListener() {
        @Override
        public void performClick(View view) {
            int id = view.getId();
            switch (id) {
                case R.id.btn_confirm:
                    // 发送确认选择目的地事件
                    BusManager.getBus().post(new DestinationsSelectEvent(tag));
                    closePop();
                    break;
                case R.id.rl_parents:
                    closePop();
                    break;
                case R.id.btn_locate:
                    // 定位 出发地
                    LocationUtils.location(mActivity, new LocationUtils.OnLocationListener() {
                        @Override
                        public void onLocated(AMapLocation aMapLocation) {
                            if (aMapLocation != null) {
                                tvDepart.setText(aMapLocation.getCity());
                                departCityItem.setDepartCityId(aMapLocation.getAdCode());
                                if (listener != null) {
                                    listener.showLocation(aMapLocation);
                                }
                            }
                        }

                        @Override
                        public void onLocationPermissionfaild() {
                        }
                    });
                    break;
                case R.id.rl_depart:
                    // 城市选择框 设置出发地
                    if (listener != null) {
                        listener.showCitySelectPop(view, DEPART_SELECT);
                    }
                    break;
            }
        }
    };

    /**
     * 目的地点击事件
     */
    private BaseQuickAdapter.OnItemClickListener onItemClickListener = new BaseQuickAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            Logger.i("oye", "position + 1 = " + (position + 1) + " size==   " + destinationList.size());

            if (position + 1 == destinationList.size()) {
                // 点击了最后一项, 手动选择城市
                if (listener != null) {
                    listener.showCitySelectPop(view, DESTINATION_SELECT);
                }
            } else {
                if (destinationList.get(position).isSelected()) {
                    destinationList.get(position).setSelected(false);
                } else {
                    destinationList.get(position).setSelected(true);
                }
            }
            mAdapter.notifyDataSetChanged();
            Logger.i("oye", "destinationList == " + destinationList);
        }
    };

    /**
     * 显示POP
     */
    public boolean showPop(View parent) {
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
            popupWindow.showAsDropDown(parent, 0, 0);
            return true;
        }
        return false;
    }

    /**
     * 关闭POP
     */
    private void closePop() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }

    /*=====================对外提供的API=====================*/

    /**
     * 通过常跑城市列表数据 创建出 目的地弹窗列表数据
     *
     * @param usualCityList 常跑城市集合
     */
    public static ArrayList<DestinationListItem> creatDestinationCityList(ArrayList<UsualCityBean> usualCityList) {
        ArrayList<DestinationListItem> destinationCityList = new ArrayList<>();// 目的地列表数据
        if (!ListUtils.isEmpty(usualCityList)) {
            for (int i = 0; i < usualCityList.size(); i++) {
                // 将常跑城市 转换为 目的地信息
                destinationCityList.add(new DestinationListItem(
                        usualCityList.get(i).getBusinessLineCitycode(),
                        usualCityList.get(i).getBusinessLineCityName(),
                        false,
                        DestinationListItem.IS_NOT_SELF));
            }
        }
        // 添加手动选择城市数据
        destinationCityList.add(new DestinationListItem("", "手动选择城市", false, DestinationListItem.IS_SELF));
        return destinationCityList;
    }

    /**
     * 获取上传服务端需要的，被选中目的地城市Id集合
     */
    public ArrayList<String> getSelectedDestIds() {
        ArrayList<String> selectedDestList = new ArrayList<>();
        if (destinationList != null) {
            for (int i = 0; i < destinationList.size(); i++) {
                if (destinationList.get(i).isSelected()) {
                    selectedDestList.add(destinationList.get(i).getId());
                }
            }
        }
        return selectedDestList;
    }

    /**
     * 获取出发地城市ID
     */
    public String getDepartCityId() {
        return departCityItem.getDepartCityId();
    }

    /**
     * 设置手动选择城市
     */
    public void setDestinationCityBySelf(DestinationListItem item) {
        int lastIndex = destinationList.size() - 1;
        destinationList.remove(lastIndex);
        destinationList.add(item);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 设置出发地城市
     */
    public void setDepartCityItem(DepartCityBean item) {
        this.departCityItem = item;
        tvDepart.setText(item.getDepartCityName());
    }

    /**
     * 获取目的地 显示列表数据
     */
    public ArrayList<DestinationListItem> getDestinationList() {
        return destinationList;
    }
}
