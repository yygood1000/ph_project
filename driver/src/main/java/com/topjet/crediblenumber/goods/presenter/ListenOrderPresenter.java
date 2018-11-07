package com.topjet.crediblenumber.goods.presenter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.amap.api.location.AMapLocation;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.topjet.common.base.controller.AreaDataMachiningController;
import com.topjet.common.base.presenter.BaseFragmentPresenter;
import com.topjet.common.base.view.fragment.RxFragment;
import com.topjet.common.common.modle.bean.DestinationListItem;
import com.topjet.common.common.modle.bean.GoodsListData;
import com.topjet.common.common.modle.bean.UsualCityBean;
import com.topjet.common.common.modle.event.AreaSelectedData;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.config.CPersisData;
import com.topjet.common.order_detail.modle.extra.CityAndLocationExtra;
import com.topjet.common.utils.ACache;
import com.topjet.common.utils.LocationUtils;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.StringUtils;
import com.topjet.crediblenumber.goods.modle.bean.DepartCityBean;
import com.topjet.crediblenumber.goods.modle.response.GetListenSettingResponse;
import com.topjet.crediblenumber.goods.modle.response.ListenSettingObject;
import com.topjet.crediblenumber.goods.service.FloatViewService;
import com.topjet.crediblenumber.goods.view.fragment.ListenOrderView;
import com.topjet.crediblenumber.user.modle.response.GetUsualCityListResponse;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by tsj-004 on 2017/9/11.
 * 听单Presenter
 */

public class ListenOrderPresenter extends BaseFragmentPresenter<ListenOrderView> implements LocationUtils
        .OnLocationListener {
    private List<GoodsListData> listOrderList = new ArrayList<>();     // 听单列表
    private ListenOrderController listenOrderController = null;     // 网络接口访问类
    private GetListenSettingResponse listenSettingResponse = null;  // “获取听单设置”的返回参数
    private GetListenSettingResponse listenSettingRequest = null;  // “保存听单设置”的传入参数
    private ListenSettingObject optional = new ListenSettingObject();                      // 自选目的地
    private ListenSettingObject depart = new ListenSettingObject();                        // 出发地，服务器交互参数
    private ArrayList<DestinationListItem> destinationCityList = new ArrayList<>();// 目的地列表数据
    private DepartCityBean departCityBean = new DepartCityBean();    // 出发地，弹窗用到

    private boolean isGetUsual = false;     // 获取常跑城市完成
    private boolean isGetListen = false;    // 获取听单设置完成
    private ArrayList<UsualCityBean> usualCityList = null;

    private final static String cacheName = "cache_data";       // 缓存的名字

    public ListenOrderPresenter(ListenOrderView mView, Context mContext, RxFragment mFragment) {
        super(mView, mContext, mFragment);

        listenOrderController = new ListenOrderController();

        ACache mCache = ACache.get(mContext);
        String cacheStr = mCache.getAsString(cacheName);
        if (StringUtils.isNotBlank(cacheStr)) {
            listOrderList = new Gson().fromJson(cacheStr,
                    new TypeToken<List<GoodsListData>>() {
                    }.getType());
        }
    }

    /**
     * 从服务器拉数据
     * 获取听单设置、常跑城市数据
     */
    public void getViewData() {
        isGetUsual = isGetListen = false;
        listenOrderController.getUsualCityList(mActivity);  // 获取常跑城市
        listenOrderController.getListenSetting(mFragment, mActivity);     // 获取听单设置
    }

    /**
     * 是否是第一次进入听单界面
     * 是：开启听单，开启听单快捷菜单，终点默认勾选用户的全部常跑城市
     */
    public void isFirstRun() {
        boolean isFirstListenOrder = CPersisData.getIsFirstListenOrder();
        if (isFirstListenOrder) {
            CMemoryData.setIsOrderOpen(true);
            mView.requestOverlayPermission(); // 申请悬浮窗权限
            CPersisData.setIsFirstListenOrder(false);
        }
    }

    /**
     * 开启floatview服务
     */
    public void startFloatViewService() {
        CPersisData.setFloatViewStatus("true");
        mContext.startService(new Intent(mContext, FloatViewService.class));    // 开启悬浮窗服务（可控制语音是否播报）
    }

    /**
     * 处理“获取听单设置”数据
     */
    public void makeListenOrderSet(GetListenSettingResponse listenSettingResponse) {
        isGetListen = true;
        this.listenSettingResponse = listenSettingResponse;
        makeData();     // 真正的处理数据
    }

    /**
     * 处理“常跑城市”数据
     */
    public void makeUsualCityData(GetUsualCityListResponse getUsualCityListResponse) {
        isGetUsual = true;
        usualCityList = getUsualCityListResponse.getList();
        makeData(); // 真正的处理数据
    }

    /**
     * 真正的处理数据
     */
    private void makeData() {
        if (isGetListen && isGetUsual && listenSettingResponse != null) {
            List<ListenSettingObject> common = listenSettingResponse.getCommon();
            ListenSettingObject optional = listenSettingResponse.getOptional();

            String destinationStr = "";
            if (usualCityList != null) {
                destinationCityList.clear();
                if (common != null && common.size() > 0) {
                    for (int us = 0; us < usualCityList.size(); us++) {
                        boolean isHave = false; // 是否存在
                        for (int i = 0; i < common.size(); i++) {
                            if (usualCityList.get(us).getBusinessLineCitycode().equals(common.get(i).getCity_id())) {
                                isHave = true;
                                break;
                            } else {
                                isHave = false;
                            }
                        }
                        destinationCityList.add(new DestinationListItem(usualCityList.get(us).getBusinessLineCitycode(),
                                usualCityList.get(us).getBusinessLineCityName(),
                                isHave,
                                DestinationListItem.IS_NOT_SELF));
                    }
                } else {
                    for (int us = 0; us < usualCityList.size(); us++) {
                        destinationCityList.add(new DestinationListItem(usualCityList.get(us).getBusinessLineCitycode(),
                                usualCityList.get(us).getBusinessLineCityName(),
                                false,
                                DestinationListItem.IS_NOT_SELF));
                    }
                }
            }

            // 最后一项，可以手动选择的
            if (optional != null && optional.getCity_name() != null) {
                destinationCityList.add(new DestinationListItem(optional.getCity_id(), optional.getCity_name(), true,
                        DestinationListItem.IS_SELF));
            } else {
                destinationCityList.add(new DestinationListItem("", "手动选择城市", false, DestinationListItem.IS_SELF));
            }

            for (int i = 0; i < destinationCityList.size(); i++) {
                if (destinationCityList.get(i).isSelected()) {
                    if (!TextUtils.isEmpty(destinationStr)) {
                        destinationStr = destinationStr + "/" + destinationCityList.get(i).getDestinationName();
                    } else {
                        destinationStr = destinationCityList.get(i).getDestinationName();
                    }
                }
            }
            saveDestination2SP();  // 保存目的地至SP

            /**
             * 出发地
             */
            if (listenSettingResponse.getDepart() != null) {
                mView.setStartText(listenSettingResponse.getDepart().getCity_name(), false, listenSettingResponse
                        .isNeedLocation());
                Logger.d("TTT","aaaaalistenSettingResponse.isNeedLocation():"+listenSettingResponse.isNeedLocation());
                CPersisData.setListenOrderStartCityId(listenSettingResponse.getDepart().getCity_id());
                departCityBean.setDepartCityId(listenSettingResponse.getDepart().getCity_id());
                departCityBean.setDepartCityName(listenSettingResponse.getDepart().getCity_name());
            } else {
                mView.setStartText("", true, listenSettingResponse.isNeedLocation());
                Logger.d("TTT","bbbbbbbbbbbbbblistenSettingResponse.isNeedLocation():"+listenSettingResponse.isNeedLocation());
            }

            /**
             * 目的地
             */
            if (!TextUtils.isEmpty(destinationStr)) {
                mView.setEndText(destinationStr);
            }
        }
    }

    /**
     * 处理“城市选择器”数据
     */
    public void makeCitySelectData(AreaSelectedData selectedData) {
        CityAndLocationExtra extra = AreaDataMachiningController.machining(selectedData);  // 处理城市选择器数据工具类
        mView.setPopAddress(extra);
    }

    /**
     * 点击确定按钮
     */
    public void onClickConfirm() {
        listenSettingRequest = new GetListenSettingResponse();
        if (depart != null) {
            listenSettingRequest.setDepart(depart);
        }
        destinationCityList = CMemoryData.getDestinationCityDatas();
        for (int i = 0; i < destinationCityList.size(); i++) {
        }
        if (destinationCityList != null && destinationCityList.size() > 0) {
            if (destinationCityList.get(destinationCityList.size() - 1).getIsSelectedBySelf()) {
                if (destinationCityList.get(destinationCityList.size() - 1).isSelected()) {
                    optional = new ListenSettingObject();
                    DestinationListItem curItem = destinationCityList.get(destinationCityList.size() - 1);
                    optional.setCity_id(curItem.getId());
                    optional.setCity_name(curItem.getDestinationName());
                    optional.setType("3");
                    listenSettingRequest.setOptional(optional);
                } else {
                    listenSettingRequest.setOptional(null);
                }
            }

            List<ListenSettingObject> newCom = new ArrayList<>();
            for (int i = 0; i < destinationCityList.size(); i++) {
                if (destinationCityList != null && destinationCityList.size() > 1 && destinationCityList.get(i)
                        .isSelected() && i != destinationCityList.size() - 1) {
                    newCom.add(new ListenSettingObject("2", destinationCityList.get(i).getId(), destinationCityList
                            .get(i).getDestinationName()));
                }
            }
            listenSettingRequest.setCommon(newCom);
        }
        listenOrderController.setListenSetting(listenSettingRequest, mFragment, mActivity);     // 请求保存设置接口

        saveDestination2SP();   // 保存目的地至SP
        destinationCityList.clear();
        listenSettingRequest.setNeedLocation(false);   // 确定后不再进行定位，否则定位信息会覆盖选好的城市
        makeListenOrderSet(listenSettingRequest);
    }

    /**
     * 保存目的地至SP
     */
    private void saveDestination2SP() {
        Set<String> destinationCityIds = new HashSet<>();
        for (int i = 0; destinationCityList != null && i < destinationCityList.size(); i++) {
            if (destinationCityList.get(i).isSelected()) {
                destinationCityIds.add(destinationCityList.get(i).getId());
            }
        }
        CPersisData.setListenOrderDestinationCityIds(destinationCityIds);
    }

    /**
     * 定位
     */
    public void doLocation() {
        Logger.d("TTT","doLocation---------------------");
        LocationUtils.location(mContext, this);     // 请求定位
    }

    @Override
    public void onLocated(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (!TextUtils.isEmpty(aMapLocation.getCity()) && !TextUtils.isEmpty(aMapLocation.getAdCode())) {
                setLocation(aMapLocation);
            }
        }
    }

    /**
     * 设置出发地信息（利用地图回调设置出发地时用该方法，在）
     * @param aMapLocation
     */
    public void setLocation(AMapLocation aMapLocation){
        if (listenSettingResponse != null) {
            listenSettingResponse.setNeedLocation(false);
            depart = new ListenSettingObject("1", aMapLocation.getAdCode(), aMapLocation.getDistrict());
            listenSettingResponse.setDepart(depart);
            listenSettingResponse.getDepart().setCity_id(aMapLocation.getAdCode());
            makeListenOrderSet(listenSettingResponse);      // 处理“获取听单设置”数据
        }
    }

    @Override
    public void onLocationPermissionfaild() {
        mView.showOrHidePermissionsFail(true);
    }

    public void setListOrderList(List<GoodsListData> listOrderList) {
        this.listOrderList = listOrderList;
    }

    public List<GoodsListData> getListOrderList() {
        return listOrderList;
    }

    /**
     * 添加到list最前面
     * 存入缓存
     */
    public void add2ListOrderList(GoodsListData goodsListData) {
        boolean isHaveSame = false;
        if (listOrderList != null) {
            // 不能添加重复的货源
            if (listOrderList.size() != 0){
                for (GoodsListData mgood: listOrderList){
                    if(goodsListData.getGoods_id().equals(mgood.getGoods_id())){
                        isHaveSame = true;
                        break;
                    }
                }
            }
            if (!isHaveSame){
                listOrderList.add(0, goodsListData);
            }
            if (listOrderList.size() > 20) {
                listOrderList.remove(listOrderList.size() - 1);
            }
            ACache mCache = ACache.get(mContext);
            mCache.put(cacheName, new Gson().toJson(listOrderList));
        }
    }

    /**
     * 清空list和缓存
     */
    public void clearListOrderList() {
        if (listOrderList != null) {
            listOrderList.clear();
            ACache mCache = ACache.get(mContext);
            mCache.remove(cacheName);
        }
    }

    public ArrayList<DestinationListItem> getDestinationCityList() {
        return destinationCityList;
    }

    public void setDestinationCityList(ArrayList<DestinationListItem> destinationCityList) {
        this.destinationCityList = destinationCityList;
    }

    public ListenOrderController getListenOrderController() {
        return listenOrderController;
    }

    public void setListenOrderController(ListenOrderController listenOrderController) {
        this.listenOrderController = listenOrderController;
    }

    public GetListenSettingResponse getListenSettingResponse() {
        return listenSettingResponse;
    }

    public void setListenSettingResponse(GetListenSettingResponse listenSettingResponse) {
        this.listenSettingResponse = listenSettingResponse;
    }

    public ListenSettingObject getOptional() {
        return optional;
    }

    public void setOptional(ListenSettingObject optional) {
        this.optional = optional;
    }

    public void setOptionalFromCityAndLocationExtra(CityAndLocationExtra extra) {
        ListenSettingObject optional = new ListenSettingObject();
        optional = new ListenSettingObject();
        optional.setCity_id(extra.getAdCode());
        optional.setCity_name(extra.getCityName());
        optional.setType("3");
        this.optional = optional;
    }

    public ListenSettingObject getDepart() {
        return depart;
    }

    public void setDepart(ListenSettingObject depart) {
        this.depart = depart;
    }

    public void setDepartFromCityAndLocationExtra(CityAndLocationExtra extra) {
        ListenSettingObject depart = new ListenSettingObject();
        depart.setType("1");
        depart.setCity_name(extra.getLastName());
        depart.setCity_id(extra.getAdCode());
        this.depart = depart;
    }

    public DepartCityBean getDepartCityBean() {
        return departCityBean;
    }

    public void setDepartCityBean(DepartCityBean departCityBean) {
        this.departCityBean = departCityBean;
    }
}
