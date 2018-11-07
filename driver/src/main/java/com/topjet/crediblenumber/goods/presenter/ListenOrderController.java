package com.topjet.crediblenumber.goods.presenter;

import com.topjet.common.base.controller.BaseController;
import com.topjet.common.base.listener.ObserverOnNextListener;
import com.topjet.common.base.listener.ObserverOnResultListener;
import com.topjet.common.base.net.request.CommonParams;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.base.view.fragment.RxFragment;
import com.topjet.common.common.modle.bean.GoodsListData;
import com.topjet.common.config.CPersisData;
import com.topjet.common.utils.StringUtils;
import com.topjet.crediblenumber.goods.modle.event.RequestNetOpenOrCloseListenOrderEvent;
import com.topjet.crediblenumber.goods.modle.params.GetListenOrderParams;
import com.topjet.crediblenumber.goods.modle.response.GetListenSettingResponse;
import com.topjet.crediblenumber.goods.modle.serverAPI.GoodsCommand;
import com.topjet.crediblenumber.goods.modle.serverAPI.GoodsCommandAPI;
import com.topjet.crediblenumber.order.modle.bean.ListenOrderCheckBoxStatus;
import com.topjet.crediblenumber.user.modle.response.GetUsualCityListResponse;
import com.topjet.crediblenumber.user.modle.serverAPI.UsualCityCommand;
import com.topjet.crediblenumber.user.modle.serverAPI.UsualCityCommandAPI;

/**
 * Created by tsj-004 on 2017/9/12.
 */

public class ListenOrderController extends BaseController {

    /**
     * 获取听单设置
     */
    public void getListenSetting(RxFragment fragment, MvpActivity activity) {
        new GoodsCommand(GoodsCommandAPI.class, activity)
                .getListenSetting(new ObserverOnNextListener<GetListenSettingResponse>() {
                    @Override
                    public void onNext(GetListenSettingResponse listenSettingResponse) {
                        listenSettingResponse.setSuccess(true);
                        postEvent(listenSettingResponse);
                    }

                    @Override
                    public void onError(String errorCode, String msg) {
                        GetListenSettingResponse listenSettingResponse = new GetListenSettingResponse();
                        listenSettingResponse.setSuccess(false);
                        listenSettingResponse.setErrorCode(errorCode);
                        listenSettingResponse.setMsg(msg);
                        postEvent(listenSettingResponse);
                    }
                });
    }

    /**
     * 保存听单设置
     */
    public void setListenSetting(GetListenSettingResponse listenSettingResponse, RxFragment fragment, MvpActivity activity) {
        new GoodsCommand(GoodsCommandAPI.class, activity).setListenSetting(listenSettingResponse, new ObserverOnResultListener<Object>() {
            @Override
            public void onResult(Object o) {

            }
        });

    }

    /**
     * 获取听单列表
     */
    public void getListenList(String startTime) {
        GetListenOrderParams getListenOrderParams = new GetListenOrderParams();
        getListenOrderParams.setStart_time(startTime);
        if (StringUtils.isEmpty(CPersisData.getListenOrderStartCityId())) {
            return;
        }
        if (CPersisData.getListenOrderDestinationCityIds() == null || CPersisData.getListenOrderDestinationCityIds().size() == 0) {
            return;
        }

        getListenOrderParams.setStart_city_id(CPersisData.getListenOrderStartCityId());
        getListenOrderParams.setDestination_city_ids(CPersisData.getListenOrderDestinationCityIds());

        new GoodsCommand(GoodsCommandAPI.class).getListenList(getListenOrderParams, new ObserverOnResultListener<GoodsListData>() {
            @Override
            public void onResult(GoodsListData goodsListData) {
                postEvent(goodsListData);
            }
        });

    }

    /**
     * 司机-获取听单开关状态
     */
    public void getCheckBoxStatus(MvpActivity activity, final String tag) {
        new GoodsCommand(GoodsCommandAPI.class, activity).getCheckBoxStatus(new ObserverOnResultListener<ListenOrderCheckBoxStatus>() {
            @Override
            public void onResult(ListenOrderCheckBoxStatus listenOrderCheckBoxStatus) {
                listenOrderCheckBoxStatus.setTag(tag);
                postEvent(listenOrderCheckBoxStatus);
            }
        });

    }

    /**
     * 司机-设置听单开关状态
     */
    public void setCheckBoxStatus(String s) {
        ListenOrderCheckBoxStatus status = new ListenOrderCheckBoxStatus(s);
        new GoodsCommand(GoodsCommandAPI.class).setCheckBoxStatus(status, new ObserverOnNextListener<Object>() {
            @Override
            public void onNext(Object object) {
                postEvent(new RequestNetOpenOrCloseListenOrderEvent());
            }

            @Override
            public void onError(String errorCode, String msg) {
                postEvent(new RequestNetOpenOrCloseListenOrderEvent());
            }
        });
    }

    /**
     * 获取常跑城市列表
     */
    public void getUsualCityList(MvpActivity activity) {
        CommonParams commonParams = new CommonParams(UsualCityCommandAPI.GET_USUAL_CITY_LIST_IN_CENTER);
        new UsualCityCommand(UsualCityCommandAPI.class, activity).getUsualCityListInCenter(commonParams, new ObserverOnNextListener<GetUsualCityListResponse>() {
            @Override
            public void onNext(GetUsualCityListResponse response) {
                postEvent(response);
            }

            @Override
            public void onError(String errorCode, String msg) {
                postEvent(new GetUsualCityListResponse());
            }
        });
    }
}
