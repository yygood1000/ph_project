package com.topjet.crediblenumber.goods.presenter;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.topjet.common.adv.modle.bean.GoodsListAdvBean;
import com.topjet.common.adv.modle.params.MarqueeParams;
import com.topjet.common.adv.modle.response.GetGoodsListAdvResponse;
import com.topjet.common.adv.modle.response.GetMarqueeResponse;
import com.topjet.common.adv.modle.serverAPI.AdvCommand;
import com.topjet.common.adv.modle.serverAPI.AdvCommandAPI;
import com.topjet.common.base.listener.ObserverOnNextListener;
import com.topjet.common.base.listener.ObserverOnResultListener;
import com.topjet.common.base.presenter.BaseFragmentApiPresenter;
import com.topjet.common.base.view.fragment.RxFragment;
import com.topjet.common.common.modle.bean.GoodsListData;
import com.topjet.common.resource.dict.AreaDataDict;
import com.topjet.common.utils.ListUtils;
import com.topjet.common.utils.LocationUtils;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.ResourceUtils;
import com.topjet.common.utils.Toaster;
import com.topjet.crediblenumber.R;
import com.topjet.crediblenumber.goods.modle.params.SearchGoodsListParams;
import com.topjet.crediblenumber.goods.modle.params.SmartSearchGoodsListParams;
import com.topjet.crediblenumber.goods.modle.response.GetSubscribeRouteCountResponse;
import com.topjet.crediblenumber.goods.modle.response.SearchGoodsResponse;
import com.topjet.crediblenumber.goods.modle.response.SmartSearchGoodsResponse;
import com.topjet.crediblenumber.goods.modle.serverAPI.GoodsCommand;
import com.topjet.crediblenumber.goods.view.fragment.SmartSearchGoodsView;

import java.util.ArrayList;

/**
 * Created by yy on 2017/8/14.
 * 司机版 我的订单Presenter
 */

public class SmartSearchGoodsPresenter extends BaseFragmentApiPresenter<SmartSearchGoodsView<GoodsListData>,
        GoodsCommand> {
    // 	车型ID
    public String truckTypeId;
    // 车长ID
    public String truckLengthId;
    // 目的地ID
    public String startCityId;
    // 出发地ID
    public String destinationCityCode;
    // 广告集合
    public ArrayList<GoodsListAdvBean> advListData = new ArrayList<>();

//    // 是否需要请求货运经纪人数量
//    public boolean isNeedToGetEconomic = true;
//    // 是否有货运经纪人
//    private boolean isEconomic;
//    public View economicView;


    public SmartSearchGoodsPresenter(SmartSearchGoodsView<GoodsListData> mView, Context mContext, RxFragment
            mFragment, GoodsCommand mApiCommand) {
        super(mView, mContext, mFragment, mApiCommand);
    }

    /**
     * 获取订阅路线总货源数
     */

    public void getSubscribeRouteGoodsCount() {
        mApiCommand.getSubscribeGoodsCount(new ObserverOnResultListener<GetSubscribeRouteCountResponse>() {
            @Override
            public void onResult(GetSubscribeRouteCountResponse response) {
                mView.setGoodsCount(response.getGoods_count());
            }
        });
    }

    /**
     * 定位当前位置，这里需要添加
     */
    public void getLocate() {
        LocationUtils.location(mContext, new LocationUtils.OnLocationListener() {
            @Override
            public void onLocated(AMapLocation aMapLocation) {
                if (aMapLocation.getErrorCode() == LocationUtils.LOCATE_SUCCEED) {
                    startCityId = aMapLocation.getAdCode();
                    mView.setDepart(AreaDataDict.getLastCityNameByLocation(aMapLocation));
                } else {
                    Toaster.showShortToast("定位失败");
                    startCityId = AreaDataDict.DEFAULT_CITY_CODE;
                    mView.setDepart(AreaDataDict.DEFAULT_CITY_NAME);
                }
                // 获取智能找货列表数据
                mView.refresh();
            }

            // 定位权限获取失败
            @Override
            public void onLocationPermissionfaild() {
                mView.onPermissionFail();
            }
        });
    }

    /**
     * 智能找货  货源列表数据请求
     */
    public void smartSearchGoodsList(int page) {
        SmartSearchGoodsListParams params = new SmartSearchGoodsListParams(page + "", startCityId);
        mApiCommand.smartSearchGoods(params, new ObserverOnNextListener<SmartSearchGoodsResponse>() {
            @Override
            public void onNext(SmartSearchGoodsResponse response) {
                mView.loadSuccess(response.getGoodsList());
            }

            @Override
            public void onError(String errorCode, String msg) {
                Logger.e("oye", errorCode);
                mView.loadFail(ResourceUtils.getString(R.string.request_error_click_to_request));
            }
        });

    }

    /**
     * 查找货源 列表数据请求
     */
    public void searchGoodsList(int page) {
        SearchGoodsListParams params = new SearchGoodsListParams();
        params.setPage(page + "");
        params.setTruck_type_id(truckTypeId);
        params.setTruck_length_id(truckLengthId);
        params.setStart_city_id(startCityId);
        params.setDestination_city_code(destinationCityCode);

        mApiCommand.searchGoods(params, new ObserverOnNextListener<SearchGoodsResponse>() {
            @Override
            public void onNext(SearchGoodsResponse response) {
                mView.loadSuccess(response.getGoodslist());
            }

            @Override
            public void onError(String errorCode, String msg) {
                Logger.e("oye", errorCode);
                mView.loadFail(ResourceUtils.getString(R.string.request_error_click_to_request));
            }
        });
    }

    // 广告相关代码 ==============================================================start

    /**
     * 获取列表广告数据
     */
    public void getGoodsListAdv() {
        new AdvCommand(AdvCommandAPI.class, mActivity).getGoodsListAdv(new ObserverOnResultListener<GetGoodsListAdvResponse>() {
            @Override
            public void onResult(GetGoodsListAdvResponse response) {
                advListData.addAll(response.getList());
                // 货源列表不为空，往货源集合中插入数据
                mView.insertAdvInfoToList();
            }
        });
    }

    /**
     * 重置广告的插入状态
     */
    public void resetAdvInsertStatus() {
        if (!ListUtils.isEmpty(advListData)) {
            for (GoodsListAdvBean a : advListData) {
                a.setInserted(false);
            }
        }
    }

    /**
     * 获取跑马灯广告数据
     */
    public void getMarqueeAdvertisment() {
        new AdvCommand(AdvCommandAPI.class, mActivity)
                .getMarqueeAdvertisment(new MarqueeParams(MarqueeParams.TYPE_DIRVER),
                        new ObserverOnResultListener<GetMarqueeResponse>() {
                            @Override
                            public void onResult(GetMarqueeResponse response) {
                                mView.showMarquee(response.getMarqueeText());
                            }
                        });
    }
    // 广告相关代码 ==============================================================end


    //    /**
//     * 获取是否有货运经纪人
//     */
//    private void getIsEconomic() {
//        GetEconomicParams params = new GetEconomicParams(startCityId, destinationCityCode);
//        mApiCommand.getIsEconomic(params, new ObserverOnResultListener<GetIsEconomicResponse>() {
//            @Override
//            public void onResult(GetIsEconomicResponse response) {
//                isEconomic = StringUtils.isNotBlank(response.getEconomic_count()) && StringUtils.getIntToString
// (response
//                        .getEconomic_count()) > 0;
//
//                Logger.d("货运经纪人 "+isEconomic+" "+response.toString());
//                initEconomicView();
//                isNeedToGetEconomic = false;
//            }
//        });
//    }
//
//    /**
//     * 初始化货运经纪人视图
//     */
//    private void initEconomicView() {
//        economicView = View.inflate(mContext, R.layout.layout_economic, null);
//        TextView tvWarning = (TextView) economicView.findViewById(R.id.tv_warning);
//        TextView tvLeft = (TextView) economicView.findViewById(R.id.tv_left);
//        TextView tvRight = (TextView) economicView.findViewById(R.id.tv_right);
//        if (isEconomic) {
//            tvWarning.setText(ResourceUtils.getString(R.string.warning_economic));
//            tvRight.setText(ResourceUtils.getString(R.string.economic));
//        } else {
//            tvWarning.setText(ResourceUtils.getString(R.string.warning_matching_center));
//            tvRight.setText(ResourceUtils.getString(R.string.matching_center));
//        }
//
//        tvLeft.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mActivity.turnToActivity(AroundGoodsMapActivity.class);
//            }
//        });
//
//        tvRight.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (isEconomic) {
//                    //TODO 跳转货运经纪人
//                    EconomicListActivity.turnToEconomicActivity(mActivity, new GetEconomicParams(startCityId,
//                            destinationCityCode));
//                } else {
//                    //TODO 跳转匹配中心
//                }
//            }
//        });
//        mView.addEconomicView();
//    }
}
