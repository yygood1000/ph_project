package com.topjet.shipper.familiar_car.presenter;

import android.content.Context;

import com.topjet.common.base.listener.ObserverOnNextListener;
import com.topjet.common.base.presenter.BaseApiPresenter;
import com.topjet.common.common.modle.bean.AroundMapExtra;
import com.topjet.common.common.modle.bean.TruckInfo;
import com.topjet.common.utils.ListUtils;
import com.topjet.shipper.familiar_car.model.params.AroundTruckMapParams;
import com.topjet.shipper.familiar_car.model.respons.AroundTruckMapResponse;
import com.topjet.shipper.familiar_car.model.serverAPI.TruckCommand;
import com.topjet.shipper.familiar_car.view.activity.AroundTruckMapListView;

/**
 * Created by yy on 2017/7/31.
 * <p>
 * 附近货源列表
 */

public class AroundTruckMapListPresenter extends BaseApiPresenter<AroundTruckMapListView<TruckInfo>, TruckCommand> {
    public boolean isUseLocalListData;// false是否是使用地图页传过来的列表集合进行显示
    public AroundMapExtra extra;

    public AroundTruckMapListPresenter(AroundTruckMapListView<TruckInfo> mView, Context mContext, TruckCommand
            mApiCommand) {
        super(mView, mContext, mApiCommand);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        AroundMapExtra extra = (AroundMapExtra) mActivity.getIntentExtra(AroundMapExtra.getExtraName());
        setParamsOrListData(extra);
    }

    /**
     * 设置请求参数
     */
    private void setParamsOrListData(AroundMapExtra extra) {
        if (extra != null) {
            this.extra = extra;
            if (!ListUtils.isEmpty(extra.getTruckListData())) {
                isUseLocalListData = true;
            } else {
                isUseLocalListData = false;
                mView.showTitle(extra.getTitle());
            }
        }
    }

    /**
     * 获取 附近货源 地图列表数据
     */
    public void getListData(int page) {
        AroundTruckMapParams params = new AroundTruckMapParams();
        params.setPage(page + "");
        params.setCity_id(extra.departCityId);
        params.setDestination_city_id(extra.destinationCityId);
        params.setLatitude(extra.lat + "");
        params.setLongitude(extra.lng + "");
        params.setMap_level(extra.zoom + "");
        params.setTruck_type_id(extra.truckSelectedData != null ? extra.truckSelectedData.getSingleTruckTypeId() : "");
        params.setTruck_length_id(extra.truckSelectedData != null ? extra.truckSelectedData.getSingleTruckLengthId()
                : "");

        mApiCommand.getAroundTruckMapList(params, new ObserverOnNextListener<AroundTruckMapResponse>() {
            @Override
            public void onNext(AroundTruckMapResponse response) {
                mView.loadSuccess(response.getNear_truck_response_list());
            }

            @Override
            public void onError(String errorCode, String msg) {
                mView.loadFail(msg);
            }
        });
    }
}
