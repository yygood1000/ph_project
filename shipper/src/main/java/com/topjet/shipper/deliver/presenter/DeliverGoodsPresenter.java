package com.topjet.shipper.deliver.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.amap.api.location.AMapLocation;
import com.amap.api.services.core.LatLonPoint;
import com.topjet.common.base.busManger.Subscribe;
import com.topjet.common.base.controller.AreaDataMachiningController;
import com.topjet.common.base.presenter.BasePresenter;
import com.topjet.common.common.modle.bean.GpsInfo;
import com.topjet.common.common.modle.event.AreaSelectedData;
import com.topjet.common.common.modle.event.TruckTypeLengthSelectedData;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.config.CPersisData;
import com.topjet.common.order_detail.modle.extra.CityAndLocationExtra;
import com.topjet.common.resource.bean.AreaInfo;
import com.topjet.common.resource.bean.CityItem;
import com.topjet.common.resource.bean.EntiretyInfo;
import com.topjet.common.resource.bean.OptionItem;
import com.topjet.common.resource.bean.TruckLengthInfo;
import com.topjet.common.resource.bean.TruckTypeInfo;
import com.topjet.common.resource.dict.CommonDataDict;
import com.topjet.common.utils.LocationUtils;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.NumberFormatUtils;
import com.topjet.common.utils.ResourceUtils;
import com.topjet.common.utils.StringUtils;
import com.topjet.shipper.R;
import com.topjet.shipper.deliver.controller.OwnerGoodsController;
import com.topjet.shipper.deliver.modle.bean.UnitAndState;
import com.topjet.shipper.deliver.modle.dirtyDataProcess.DeliverGoodsDirtyDataProcess;
import com.topjet.shipper.deliver.modle.extra.OtherInfoExtra;
import com.topjet.shipper.deliver.modle.params.OwnerGoodsParams;
import com.topjet.shipper.deliver.view.activity.DeliverGoodsActivity;
import com.topjet.shipper.deliver.view.activity.DeliverGoodsView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tsj-004 on 2017/8/22.
 * 发货
 */

public class DeliverGoodsPresenter extends BasePresenter<DeliverGoodsView> implements LocationUtils
        .OnGetDistanceListener
        , LocationUtils.OnLocationListener {
    private String tag = this.getClass().getName();
    private boolean isSetPersistenceDataed = false; // 是否设置了持久化的信息。false 没设置过，true 设置过

    private String goodsId = ""; // 货源id
    private String goodsVersion;// 货源version
    private String driverId = "";   // 司机id
    private String driver_truck_id = "";  // 车辆id
    private int inType = 0; // 进入页面类型

    private CityItem deliverCityItem;        // 发货地址
    private CityItem receiptCityItem;        // 收货地址
    private CityAndLocationExtra deliverExtra = new CityAndLocationExtra(); // 发货
    private CityAndLocationExtra receiptExtra = new CityAndLocationExtra(); // 收货
    private OtherInfoExtra otherInfoExtra = new OtherInfoExtra();           // 其他信息

    private List<UnitAndState> unitList = new ArrayList<>();    // 单位

    private String distance = "";       // 距离
    private List<String> truckLengthInfoList = new ArrayList<>();       // 车长list
    private List<String> truckTypeInfoList = new ArrayList<>();         // 车型list
    private int isCarPool;      // 是否整车
    private List<String> payList = null;        // 支付方式

    private TruckTypeLengthSelectedData truckTypeLengthSelectedData = null;            // 车型车长勾选数据


    public DeliverGoodsPresenter(DeliverGoodsView mView, Context mContext) {
        super(mView, mContext);

        otherInfoExtra.setLoad("一装一卸");
        otherInfoExtra.setType("普货");

        mView.setOtherInfo(otherInfoExtra);
    }

    /**
     * 获取车型车长文字拼接结果
     */
    private String getTypeAndLengthAndPoolStr(OwnerGoodsParams params) {
        List<String> types = params.getTruck_type_ids();
        List<String> lengths = params.getTruck_length_ids();
        truckTypeInfoList = types;
        truckLengthInfoList = lengths;

        String tempTypeStr = DeliverGoodsDirtyDataProcess.getTypeStr(types);           // 获取车型文字  空格拼接
        String tempLengthStr = DeliverGoodsDirtyDataProcess.getLengthStr(lengths);    // 获取车长文字  空格拼接
        String is_carpool = DeliverGoodsDirtyDataProcess.getIsCarpoolStr(params);     // 获取是否整车文字

        truckTypeLengthSelectedData = new TruckTypeLengthSelectedData();
        truckTypeLengthSelectedData.setIs_carpool(new EntiretyInfo(is_carpool, true, Integer.parseInt
                (DeliverGoodsDirtyDataProcess
                        .getIsCarpoolId(params))));
        List<TruckLengthInfo> lengthList = new ArrayList<>();
        if (truckLengthInfoList != null) {
            for (int i = 0; i < truckLengthInfoList.size(); i++) {
                TruckLengthInfo truckLengthInfo = new TruckLengthInfo();
                truckLengthInfo.setSelected(true);
                truckLengthInfo.setId(truckLengthInfoList.get(i));
                lengthList.add(truckLengthInfo);
            }
        }

        List<TruckTypeInfo> truckTypeInfos = new ArrayList<>();
        if (truckTypeInfoList != null) {
            for (int i = 0; i < truckTypeInfoList.size(); i++) {
                TruckTypeInfo truckTypeInfo = new TruckTypeInfo();
                truckTypeInfo.setSelected(true);
                truckTypeInfo.setId(truckTypeInfoList.get(i));
                truckTypeInfos.add(truckTypeInfo);
            }
        }
        truckTypeLengthSelectedData.setLengthList(lengthList);
        truckTypeLengthSelectedData.setTypeList(truckTypeInfos);

        return DeliverGoodsDirtyDataProcess.getTypeLengthPoolStr(is_carpool, tempTypeStr, tempLengthStr);
    }

    /**
     * 请求定位
     */
    private void requestMapLocation() {
        if (deliverExtra == null) {
            deliverExtra = new CityAndLocationExtra();
        }
        deliverExtra.setName(CPersisData.getUserName());
        deliverExtra.setPhone(CMemoryData.getUserMobile());
        mView.showAddress(DeliverGoodsActivity.TYPE_DEPART_ADDRESS, deliverExtra, true);
        LocationUtils.location(mContext, this); // 请求定位
    }

    /**
     * 初始化支付方式数据
     */
    public void initPayWayList() {
        payList = new ArrayList<>();
        ArrayList<OptionItem> tempList = CommonDataDict.getPayTypeOptionItems();
        for (int i = 0; i < tempList.size(); i++) {
            payList.add(tempList.get(i).getName());
        }
    }

    public List<String> getPayList() {
        return payList;
    }

    /**
     * 设置持久化参数，进入页面修改一次，后面不会再修改
     */
    public void setPersistenceData(OwnerGoodsParams params) {
        if (!isSetPersistenceDataed) {
            isSetPersistenceDataed = true;
            if (params != null) {
                inType = params.getInType();
                driverId = params.getDriver_id();
                driver_truck_id = params.getDriver_truck_id();
                goodsId = params.getGoods_id();
            }
        }
        Logger.i("oye", "setPersistenceData driver_truck_id =" + driver_truck_id);
    }

    /**
     * 请求服务器
     * 根据id获取订单数据
     */
    public void getParamsFromServerById(OwnerGoodsParams params) {
        String goodOrderId = "";
        int inType = 0;
        if (params != null) {
            goodOrderId = params.getGoods_id();
            inType = params.getInType();
        }
        switch (inType) {
            case OwnerGoodsParams.IN_TYPE_COPY:
            case OwnerGoodsParams.IN_TYPE_EDIT:
                if (StringUtils.isNotBlank(goodOrderId)) {
                    // 获取货源订单详情
                    new OwnerGoodsController(mActivity).getParamsFromServerById(goodOrderId, tag);
                }
                break;
            case OwnerGoodsParams.IN_TYPE_ADD_ASSIGNED_REFIND_TRUCK:
            case OwnerGoodsParams.IN_TYPE_EDIT_ASSIGNED:
                goodsId = goodOrderId = CMemoryData.getTempGoodsId();
                if (StringUtils.isNotBlank(goodOrderId)) {
                    // 获取货源订单详情
                    new OwnerGoodsController(mActivity).getParamsFromServerById(goodOrderId, tag);
                }
                break;
            case OwnerGoodsParams.IN_TYPE_ADD_ASSIGNED:
                String typeAndlengthAndPoolStr = getTypeAndLengthAndPoolStr(params);
                // 设置车型车长整车文字
                mView.setTypeAndLengthText(typeAndlengthAndPoolStr);
                requestMapLocation();    // 请求定位

                break;
            default:
                requestMapLocation();    // 请求定位

                break;
        }
    }

    /**
     * 从城市选择器中获取数据
     */
    public void selectAddress(int curAddressType, AreaSelectedData areaSelectedData) {
        if (areaSelectedData != null) {
            AreaInfo areaInfo = areaSelectedData.getAreaInfo();
            if (areaInfo != null) {
                CityItem third = areaInfo.getThirdLevel();
                CityItem second = areaInfo.getSecondLevel();
                CityItem first = areaInfo.getFirstLevel();

                if (third != null) {
                    // 填入对应的变量中
                    judgeAddress(curAddressType, third);
                }
                if (second != null) {
                    // 填入对应的变量中
                    judgeAddress(curAddressType, second);
                }
                if (first != null) {
                    // 填入对应的变量中
                    judgeAddress(curAddressType, first);
                }
            }
        }

        CityAndLocationExtra extra = AreaDataMachiningController.machining(areaSelectedData);  // 处理城市选择器数据工具类
        if (curAddressType == DeliverGoodsActivity.TYPE_DEPART_ADDRESS) {
            extra.setPhone(deliverExtra.getPhone());
            extra.setName(deliverExtra.getName());
        } else if (curAddressType == DeliverGoodsActivity.TYPE_DESTINATION_ADDRESS) {
            extra.setPhone(receiptExtra.getPhone());
            extra.setName(receiptExtra.getName());
        }
        mView.showAddress(curAddressType, extra, true);
    }

    /**
     * 将地址填入对应的变量中
     */
    private void judgeAddress(int curAddressType, CityItem cur) {
        if (curAddressType == DeliverGoodsActivity.TYPE_DEPART_ADDRESS) {
            deliverCityItem = cur;
        } else if (curAddressType == DeliverGoodsActivity.TYPE_DESTINATION_ADDRESS) {
            receiptCityItem = cur;
        }

        // 计算两地距离
        if (deliverCityItem != null && receiptCityItem != null) {
            LocationUtils.getLineDistance(mContext, new LatLonPoint(Double.valueOf(deliverCityItem.getLatitude()),
                            Double.valueOf(deliverCityItem.getLongitude())),
                    new LatLonPoint(Double.valueOf(receiptCityItem.getLatitude()), Double.valueOf(receiptCityItem
                            .getLongitude())), this);
            formatDisTanceStr(0);
        }
    }

    /**
     * 距离计算回调
     */
    @Override
    public void onGetDistance(int distanceNumber) {
        distance = NumberFormatUtils.changeToStr(distanceNumber + "");
        formatDisTanceStr(1);        // 距离转成“预计里程约………”
    }

    /**
     * 距离转成“预计里程约………”
     */
    private void formatDisTanceStr(int type) {
        String tempStr = "";
        String distance_str = ResourceUtils.getString(R.string.distance_str);
        String distance_str0 = ResourceUtils.getString(R.string.distance_str0);  // 计算中
        if (type == 0) {   // 计算中
            tempStr = distance_str0;
        } else if (type == 1) {     // 计算完成
            tempStr = String.format(distance_str, NumberFormatUtils.removeDecimal(distance));
        }

        if (StringUtils.isNotBlank(tempStr) && mView != null) {
            // 显示计算结果
            mView.showDistance(tempStr);
        }
    }

    /**
     * 从车型车长选择器中获取数据
     */
    public void selectTypeAndLength(TruckTypeLengthSelectedData tld) {
        this.truckTypeLengthSelectedData = tld;
        StringBuffer stringBuffer = new StringBuffer();
        if (tld.getIs_carpool() != null) {
            stringBuffer.append(tld.getIs_carpool().getDisplayName() + " ");
            isCarPool = tld.getIs_carpool().getId();
        }

        truckTypeInfoList.clear();
        truckLengthInfoList.clear();

        List<TruckTypeInfo> typeList = tld.getTypeList();
        if (typeList != null) {
            for (int i = 0; i < typeList.size(); i++) {
                stringBuffer.append(typeList.get(i).getDisplayName() + " ");
                truckTypeInfoList.add(typeList.get(i).getId());
            }
        }

        List<TruckLengthInfo> lengthList = tld.getLengthList();
        if (lengthList != null) {
            for (int i = 0; i < lengthList.size(); i++) {
                stringBuffer.append(lengthList.get(i).getDisplayName());
                truckLengthInfoList.add(lengthList.get(i).getId());
                if (i == lengthList.size()) {
                    break;
                }
                stringBuffer.append(" ");
            }
        }

        mView.setTypeAndLengthText(stringBuffer.toString());
    }

    /**
     * 设置单位数据
     */
    public void setUnitData() {
        unitList = new ArrayList<>();
        ArrayList<OptionItem> tempUnitList = CommonDataDict.getGoodsUnitList();
        for (int i = 0; i < tempUnitList.size(); i++) {
            UnitAndState us = new UnitAndState();
            us.setItem(tempUnitList.get(i));
            unitList.add(us);
        }
    }

    /**
     * 整理发货参数
     */
    public void disposeData(String loadDateType, String loadDate, int curUnitType, String goodsQuantity, String max,
                            String min, String ahead_fee, String delivery_fee, String back_fee, String freight_fee,
                            boolean ahead_fee_is_managed, boolean delivery_fee_is_managed) {
        OwnerGoodsParams params = new OwnerGoodsParams();
        params.setGoods_id(goodsId);
        params.setInType(inType);
        params.setSender_name(deliverExtra.getName());
        params.setSender_mobile(deliverExtra.getPhone());
        params.setDepart_city_id(deliverExtra.getAdCode());
        params.setDepart_detail(deliverExtra.getAddress());
        params.setDepart_lng(deliverExtra.getLongitude());
        params.setDepart_lat(deliverExtra.getLatitude());

        if (StringUtils.isEmpty(deliverExtra.getLatitude())) {
            mView.onClickSumbmitError(false, mActivity.getString(R.string.please_input_deliver_address), null);
            return;
        }

        params.setReceiver_name(receiptExtra.getName());
        params.setReceiver_mobile(receiptExtra.getPhone());
        params.setDestination_city_id(receiptExtra.getAdCode());
        params.setDestination_detail(receiptExtra.getAddress());
        params.setDestination_lng(receiptExtra.getLongitude());
        params.setDestination_lat(receiptExtra.getLatitude());
        if (StringUtils.isEmpty(receiptExtra.getLatitude())) {
            mView.onClickSumbmitError(false, mActivity.getString(R.string.please_input_receipt_address), null);
            return;
        }

        params.setDistance(this.distance + "");

        params.setPay_style((mView.getSelectedPayWayIndex() + 1) + "");

        params.setIs_carpool(isCarPool + "");

        params.setLoad_date_type(loadDateType);
        params.setLoad_date(loadDate);

        params.setCategory(otherInfoExtra.getType());
        params.setPack_type(otherInfoExtra.getPack());
        params.setLoad_type(otherInfoExtra.getLoad());
        boolean isRefre = otherInfoExtra.getRefre();
        if (isRefre) {
            params.setFlush_num(CPersisData.getSpKeyGoodsrefreshinfoRefreshCount());
        } else {
            params.setFlush_num("0");
        }
        // 设置文本备注
        params.setText_remark(otherInfoExtra.getRemark());
        // 设置图片备注
        params.setPhoto_remark(otherInfoExtra.getPhoto());
        // 设置图片备注key
        params.setPhoto_remark_key(otherInfoExtra.getPhotoKey());
        // 设置货物类型
        params.setQuantity_type(curUnitType + "");

        if (curUnitType == DeliverGoodsActivity.FLAG_UNITS_EXACT) {
            if (StringUtils.isEmpty(goodsQuantity)) {
                mView.onClickSumbmitError(false, mActivity.getString(R.string.please_input_goods_number2), null);
                return;
            }
            if (Float.parseFloat(goodsQuantity) == 0) {
                mView.onClickSumbmitError(false, mActivity.getString(R.string.please_input_goods_number2), null);
                return;
            }
            params.setQuantity_max(goodsQuantity);
        } else {
            if (TextUtils.isEmpty(max) && TextUtils.isEmpty(min)) {
                mView.onClickSumbmitError(false, mActivity.getString(R.string.please_input_goods_number2), null);
                return;
            } else {
                if (Float.parseFloat(max) == 0) {
                    mView.onClickSumbmitError(false, mActivity.getString(R.string.please_input_goods_number2), null);
                    return;
                }
                if (Float.parseFloat(min) == 0) {
                    mView.onClickSumbmitError(false, mActivity.getString(R.string.please_input_goods_number2), null);
                    return;
                }
            }

            if (TextUtils.isEmpty(max)) {
                mView.onClickSumbmitError(false, mActivity.getString(R.string.please_input_goods_number2), null);
                return;
            }
            if (TextUtils.isEmpty(min)) {
                mView.onClickSumbmitError(false, mActivity.getString(R.string.please_input_goods_number2), null);
                return;
            }

            float maxFloat = Float.parseFloat(max);
            float minFloat = Float.parseFloat(min);
            if (minFloat > maxFloat) {
                mView.onClickSumbmitError(false, "最大值要大于最小值", null);
                mView.clearetAmountText();
                return;
            }

            if (!TextUtils.isEmpty(max) && !TextUtils.isEmpty(min)) {
                if (max.equalsIgnoreCase(min)) {
                    params.setQuantity_max(max);
                    params.setQuantity_type(DeliverGoodsActivity.FLAG_UNITS_EXACT + "");
                } else {
                    params.setQuantity_min(min);
                    params.setQuantity_max(max);
                }
            }
        }
        params.setUnit(unitList.get(mView.getSelectedUnitId()).getItem().getName());

        GpsInfo order_gps_info = new GpsInfo();
        if (CMemoryData.getLocationInfo() != null) {
            order_gps_info.setGps_address_city_id(CMemoryData.getLocationInfo().getCity_id());
            order_gps_info.setGps_detail(CMemoryData.getLocationInfo().getDetail());
            order_gps_info.setGps_latitude(CMemoryData.getLocationInfo().getLatitude() + "");
            order_gps_info.setGps_longitude(CMemoryData.getLocationInfo().getLongitude() + "");
            order_gps_info.setGps_remark("接单");
            params.setOrder_gps_info(order_gps_info);
        }

        if (truckTypeInfoList == null || truckTypeInfoList.size() == 0) {
            mView.onClickSumbmitError(false, mActivity.getString(R.string.please_select_type_length), null);
            return;
        }
        if (truckLengthInfoList == null || truckLengthInfoList.size() == 0) {
            mView.onClickSumbmitError(false, mActivity.getString(R.string.please_select_type_length), null);
            return;
        }

        if (params.getInType() == OwnerGoodsParams.IN_TYPE_ADD_ASSIGNED // 发布定向订单
                || params.getInType() == OwnerGoodsParams.IN_TYPE_EDIT_ASSIGNED // 修改定向订单
                || params.getInType() == OwnerGoodsParams.IN_TYPE_ADD_ASSIGNED_REFIND_TRUCK) { // 定向订单-重新找车
            params.setDriver_id(driverId);
            params.setDriver_truck_id(driver_truck_id);
            params.setGoods_id(goodsId);
            params.setGoods_version(goodsVersion);
            if (StringUtils.isEmpty(ahead_fee) && StringUtils.isEmpty(delivery_fee) && StringUtils.isEmpty(back_fee)) {
                mView.onClickSumbmitError(false, mActivity.getString(R.string.please_input_trusteeship_amount), null);
                return;
            }

            params.setAhead_fee(ahead_fee);
            params.setDelivery_fee(delivery_fee);
            params.setBack_fee(back_fee);
            params.setFreight_fee(freight_fee);

            // 0:不托管 1:托管
            if (ahead_fee_is_managed) {
                params.setAhead_fee_is_managed("1");
            } else {
                params.setAhead_fee_is_managed("0");
            }
            if (delivery_fee_is_managed) {
                params.setDelivery_fee_is_managed("1");
            } else {
                params.setDelivery_fee_is_managed("0");
            }
        }

        params.setTruck_type_ids(truckTypeInfoList);
        params.setTruck_length_ids(truckLengthInfoList);

        mView.onClickSumbmitError(true, "", params);
    }

    /**
     * 发布定向货源
     */
    public void addAssignedGoods(OwnerGoodsParams params, String tag) {
        new OwnerGoodsController(mActivity).addAssignedGoods(params, tag);
    }

    /**
     * 修改定向货源
     */
    public void updateGoodsAssigned(OwnerGoodsParams params, String tag) {
        new OwnerGoodsController(mActivity).updateGoodsAssigned(params, tag);
    }

    /**
     * 回单付 发布货源
     */
    public void addGoodsWithBackOrder(OwnerGoodsParams params, String tag) {
        new OwnerGoodsController(mActivity).addGoods(params, tag);
    }

    /**
     * 根据goodsId查询货源/订单信息，返回事件
     */
    @Subscribe
    public void onEvent(OwnerGoodsParams params) {
        if (params != null) {
            // 非 默认发货，和 发布定向订单 时需要设置车辆信息，货源版本
            if(!(inType == OwnerGoodsParams.IN_TYPE_ADD || inType == OwnerGoodsParams.IN_TYPE_ADD_ASSIGNED)) {
                // 定向订单-重新找车时这两个有值，不需要设置
                if(inType != OwnerGoodsParams.IN_TYPE_ADD_ASSIGNED_REFIND_TRUCK) {
                    driverId = params.getDriver_id();
                    driver_truck_id = params.getDriver_truck_id();
                }
                goodsVersion = params.getGoods_version();
            }
            // 将params中的数据赋值到各个extra与参数中
            deliverExtra = DeliverGoodsDirtyDataProcess.params2deliverExtra(params);
            receiptExtra = DeliverGoodsDirtyDataProcess.params2receiptExtra(params);
            otherInfoExtra = DeliverGoodsDirtyDataProcess.params2otherInfoExtra(params);

            deliverCityItem = DeliverGoodsDirtyDataProcess.cityAndLocationExtra2CityItem(deliverExtra);
            receiptCityItem = DeliverGoodsDirtyDataProcess.cityAndLocationExtra2CityItem(receiptExtra);
            mView.showAddress(DeliverGoodsActivity.TYPE_DEPART_ADDRESS, deliverExtra, false);
            mView.showAddress(DeliverGoodsActivity.TYPE_DESTINATION_ADDRESS, receiptExtra, false);
            mView.setOtherInfo(otherInfoExtra);

            distance = params.getDistance();
            // 距离转成“预计里程约………”
            formatDisTanceStr(1);

            // 设置车型车长整车文字
            mView.setTypeAndLengthText(getTypeAndLengthAndPoolStr(params));
            // 设置支付方式文字 等操作
            mView.setPayWayText(Integer.parseInt(params.getPay_style()) - 1);

            // 根据参数刷新单位模块界面
            mView.refreViewsByParams(params.getQuantity_type(),
                    params.getQuantity_min(),
                    params.getQuantity_max(),
                    params.getUnit());
        }
    }

    /**
     * 定位成功
     */
    @Override
    public void onLocated(AMapLocation aMapLocation) {
        deliverCityItem = DeliverGoodsDirtyDataProcess.aMapLocation2CityItem(aMapLocation);     //
        // AMapLocation转换成CityItem
        deliverExtra = DeliverGoodsDirtyDataProcess.aMapLocation2CityAndLocationExtra(aMapLocation);
        deliverExtra.setName(CPersisData.getUserName());
        deliverExtra.setPhone(CMemoryData.getUserMobile());
        mView.showAddress(DeliverGoodsActivity.TYPE_DEPART_ADDRESS, deliverExtra, true);
    }

    @Override
    public void onLocationPermissionfaild() {

    }

    public CityAndLocationExtra getDeliverExtra() {
        deliverExtra.setPageTitle("deliver");
        return deliverExtra;
    }

    public void setDeliverExtra(CityAndLocationExtra deliverExtra) {
        this.deliverExtra = deliverExtra;
    }

    public CityAndLocationExtra getReceiptExtra() {
        receiptExtra.setPageTitle("receipt");
        return receiptExtra;
    }

    public void setReceiptExtra(CityAndLocationExtra receiptExtra) {
        this.receiptExtra = receiptExtra;
    }

    public OtherInfoExtra getOtherInfoExtra() {
        return otherInfoExtra;
    }

    public void setOtherInfoExtra(OtherInfoExtra otherInfoExtra) {
        this.otherInfoExtra = otherInfoExtra;
    }

    public List<UnitAndState> getUnitList() {
        return unitList;
    }

    public TruckTypeLengthSelectedData getTruckTypeLengthSelectedData() {
        return truckTypeLengthSelectedData;
    }

    public void setTruckTypeLengthSelectedData(TruckTypeLengthSelectedData truckTypeLengthSelectedData) {
        this.truckTypeLengthSelectedData = truckTypeLengthSelectedData;
    }
}