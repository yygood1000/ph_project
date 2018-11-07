package com.topjet.common.resource.serverAPI;

import com.topjet.common.base.listener.ObserverOnResultListener;
import com.topjet.common.base.net.serverAPI.BaseCommand;
import com.topjet.common.resource.params.CheckResourceParams;
import com.topjet.common.resource.params.UpdataResourceParams;
import com.topjet.common.resource.response.CheckReourceResponse;
import com.topjet.common.resource.response.UpdataResourceResponse;
import com.topjet.common.resource.response.UpdataResourceUrlResponse;

/**
 * Created by tsj-004 on 2017/7/25.
 * 资源文件相关
 */
public class ResourceCommand extends BaseCommand<ResourceCommandAPI> {

    public ResourceCommand() {
        super(ResourceCommandAPI.class);
    }

    /**
     * 校验资源文件更新
     */
    public void getTheUserParameter(CheckResourceParams params, ObserverOnResultListener<CheckReourceResponse>
            listener) {
        mCommonParams = getParams(ResourceCommandAPI.CHECK_RESOURCE, params);
        handleOnResultObserverNotActivity(mApiService.checkResource(mCommonParams), listener);
    }

    /**
     * 更新城市数据
     */
    public void updataCities(UpdataResourceParams params, ObserverOnResultListener<UpdataResourceUrlResponse>
            listener) {
        mCommonParams = getParams(ResourceCommandAPI.UPDATA_CITIES, params);
        handleOnResultObserverNotActivity(mApiService.updataCities(mCommonParams), listener);
    }

    /**
     * 更新车型数据
     */
    public void updataTruckType(UpdataResourceParams params, ObserverOnResultListener<UpdataResourceUrlResponse>
            listener) {
        mCommonParams = getParams(ResourceCommandAPI.UPDATA_TRUCK_TYPE, params);
        handleOnResultObserverNotActivity(mApiService.updataTruckType(mCommonParams), listener);
    }

    /**
     * 更新车长数据
     */
    public void updataTruckLength(UpdataResourceParams params, ObserverOnResultListener<UpdataResourceUrlResponse>
            listener) {
        mCommonParams = getParams(ResourceCommandAPI.UPDATA_TRUCK_LENGTH, params);
        handleOnResultObserverNotActivity(mApiService.updataTruckLength(mCommonParams), listener);
    }

    /**
     * 更新包装方式数据
     */
    public void updataPackingType(UpdataResourceParams params, ObserverOnResultListener<UpdataResourceUrlResponse>
            listener) {
        mCommonParams = getParams(ResourceCommandAPI.UPDATA_PACKING_TYPE, params);
        handleOnResultObserverNotActivity(mApiService.updataPackingType(mCommonParams), listener);
    }

    /**
     * 更新装卸方式数据
     */
    public void updataLoadType(UpdataResourceParams params, ObserverOnResultListener<UpdataResourceUrlResponse>
            listener) {
        mCommonParams = getParams(ResourceCommandAPI.UPDATA_LOAD_TYPE, params);
        handleOnResultObserverNotActivity(mApiService.updataLoadType(mCommonParams), listener);
    }

    /**
     * 更新货物类型数据
     */
    public void updataGoodsType(UpdataResourceParams params, ObserverOnResultListener<UpdataResourceUrlResponse>
            listener) {
        mCommonParams = getParams(ResourceCommandAPI.UPDATA_GOODS_TYPE, params);
        handleOnResultObserverNotActivity(mApiService.updataGoodsType(mCommonParams), listener);
    }

    /**
     * 更新货物单位数据
     */
    public void updataGoodsUnit(UpdataResourceParams params, ObserverOnResultListener<UpdataResourceUrlResponse>
            listener) {
        mCommonParams = getParams(ResourceCommandAPI.UPDATA_GOODS_UNIT, params);
        handleOnResultObserverNotActivity(mApiService.updataGoodsUnit(mCommonParams), listener);
    }


}


