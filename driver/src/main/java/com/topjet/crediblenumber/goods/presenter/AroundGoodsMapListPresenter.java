package com.topjet.crediblenumber.goods.presenter;

import android.content.Context;

import com.topjet.common.base.listener.ObserverOnNextListener;
import com.topjet.common.base.presenter.BaseApiPresenter;
import com.topjet.common.common.modle.bean.AroundMapExtra;
import com.topjet.common.common.modle.bean.GoodsListData;
import com.topjet.common.utils.ListUtils;
import com.topjet.common.utils.Logger;
import com.topjet.crediblenumber.goods.modle.params.AroundGoodsListParams;
import com.topjet.crediblenumber.goods.modle.response.AroundGoodsListResponse;
import com.topjet.crediblenumber.goods.modle.serverAPI.GoodsCommand;
import com.topjet.crediblenumber.goods.view.activity.AroundGoodsMapListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yy on 2017/7/31.
 * <p>
 * 附近货源列表
 */

public class AroundGoodsMapListPresenter extends BaseApiPresenter<AroundGoodsMapListView<GoodsListData>, GoodsCommand> {
    public List<GoodsListData> dataList = new ArrayList<>();// 数据集合

    public boolean isUseLocalListData;// false是否是使用地图页传过来的列表集合进行显示
    private AroundMapExtra extra;

    public AroundGoodsMapListPresenter(AroundGoodsMapListView<GoodsListData> mView, Context mContext, GoodsCommand
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
            Logger.i("oye", "地图列表页 传入数据  ===" + extra.toString());
            if (!ListUtils.isEmpty(extra.getGoodsListData())) {
                dataList = extra.getGoodsListData();
                isUseLocalListData = true;
                mView.showTitle("(共" + dataList.size() + "单)");
                //TODO 高德点聚合跳转过来。Title显示
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
        AroundGoodsListParams params = new AroundGoodsListParams();
        params.setPage(page + "");
        params.setCity_id(extra.departCityId);
        params.setLatitude(extra.lat + "");
        params.setLongitude(extra.lng + "");
        params.setMap_level(extra.zoom);
        params.setDestination_city_ids(extra.getSelectedDestCityIds());
        params.setTruck_type_id(extra.truckSelectedData != null ? extra.truckSelectedData.getSingleTruckTypeId() : "");
        params.setTruck_length_id(extra.truckSelectedData != null ? extra.truckSelectedData.getSingleTruckLengthId()
                : "");

        mApiCommand.getAroundGoodsMapList(params, new ObserverOnNextListener<AroundGoodsListResponse>() {
            @Override
            public void onNext(AroundGoodsListResponse response) {
                mView.loadSuccess(response.getList());
            }

            @Override
            public void onError(String errorCode, String msg) {
                mView.loadFail(msg);
            }
        });
    }

    /**
     * 手动设置页面数据方法，在使用本地数据时使用
     */
    public void refreshData(boolean isLoadEnd) {
        if (isLoadEnd) {
            mView.loadSuccess(new ArrayList<GoodsListData>());
        } else {
            mView.loadSuccess(dataList);
        }
    }
}
