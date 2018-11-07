package com.topjet.crediblenumber.goods.presenter;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.topjet.common.base.listener.ObserverOnResultListener;
import com.topjet.common.base.presenter.BaseApiPresenter;
import com.topjet.common.resource.dict.AreaDataDict;
import com.topjet.common.utils.LocationUtils;
import com.topjet.common.utils.StringUtils;
import com.topjet.crediblenumber.goods.modle.params.AddSubscribeRouteParams;
import com.topjet.crediblenumber.goods.modle.serverAPI.SubscribeRouteCommand;
import com.topjet.crediblenumber.goods.view.activity.AddSubscribeRouteView;

/**
 * Created by yy on 2017/7/31.
 * <p>
 * 添加订阅路线
 */

public class AddSubscribeRoutePresenter extends BaseApiPresenter<AddSubscribeRouteView, SubscribeRouteCommand> {
    public AddSubscribeRoutePresenter(AddSubscribeRouteView mView, Context mContext, SubscribeRouteCommand
            mApiCommand) {
        super(mView, mContext, mApiCommand);
    }

    /**
     * 定位获取当前位置
     */
    public void getLocation() {
        LocationUtils.location(mContext, new LocationUtils.OnLocationListener() {
            @Override
            public void onLocated(AMapLocation aMapLocation) {
                if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {// 定位成功
                    mView.setDepartTextView(AreaDataDict.getFullCityNameByLocation(aMapLocation), aMapLocation
                            .getAdCode());
                }
            }

            @Override
            public void onLocationPermissionfaild() {

            }
        });
    }


    /**
     * 添加订阅路线
     */
    public void addSubscribeRoute(String departId, String destinationId, String truckTypeId, String truckLengthId) {
        if (StringUtils.isEmpty(departId)) {
            mView.showToast("请选择出发地");
            return;
        }

        if (StringUtils.isEmpty(destinationId)) {
            mView.showToast("请选择目的地");
            return;
        }

        AddSubscribeRouteParams params = new AddSubscribeRouteParams();
        params.setDepart_city_code(departId);
        params.setDestination_city_code(destinationId);
        params.setTruck_type_id(truckTypeId);
        params.setTruck_length_id(truckLengthId);

        mApiCommand.addSubscribeRouteList(params, new ObserverOnResultListener<Object>() {
            @Override
            public void onResult(Object o) {
                mView.showToast("添加成功");
                mActivity.finish();
            }
        });

    }
}
