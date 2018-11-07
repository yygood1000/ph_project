package com.topjet.common.common.modle.serverAPI;

import com.topjet.common.base.CommonProvider;
import com.topjet.common.base.listener.ObserverOnResultListener;
import com.topjet.common.base.net.serverAPI.BaseCommand;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.common.modle.params.GetScrollBtnsParams;
import com.topjet.common.common.modle.params.GetSwitchKeyParams;
import com.topjet.common.common.modle.params.GetTabLayoutBtnsParams;
import com.topjet.common.common.modle.response.GetScrollBtnsResponse;
import com.topjet.common.common.modle.response.GetSwitchKeyResponse;
import com.topjet.common.common.modle.response.GetTabLayoutBtnsResponse;
import com.topjet.common.common.modle.response.GetUserInfoAtHomeResponse;
import com.topjet.common.common.modle.response.GoodsRefreshInfoResponse;

/**
 * Created by tsj-004 on 2017/7/25.
 * 用户相关
 */
public class HomeCommand extends BaseCommand<HomeCommandAPI> {

    public HomeCommand(MvpActivity activity) {
        super(HomeCommandAPI.class, activity);
    }

    /**
     * 首页-获取用户参数
     */
    public void getTheUserParameter(boolean isShowLoading, final ObserverOnResultListener<GetUserInfoAtHomeResponse>
            listener) {
        mCommonParams = getParams(HomeCommandAPI.GET_THE_USER_PARAMETER);
        handleOnResultObserver(mApiService.getTheUserParameter(mCommonParams), new ObserverOnResultListener<GetUserInfoAtHomeResponse>() {
            @Override
            public void onResult(GetUserInfoAtHomeResponse getUserInfoAtHomeResponse) {
                // 保存用户信息到本地数据库
                CommonProvider.getInstance().getUserInfoProvider()
                        .saveUserInfo(getUserInfoAtHomeResponse.getImUserBean(getUserInfoAtHomeResponse));
                listener.onResult(getUserInfoAtHomeResponse);
            }
        }, isShowLoading);
    }

    /**
     * 首页-中间选项(滑动按钮组)
     */
    public void getScrollBtns(GetScrollBtnsParams params,
                              ObserverOnResultListener<GetScrollBtnsResponse> listener) {
        mCommonParams = getParams(HomeCommandAPI.GET_SCROLL_BTNS, params);
        handleOnResultObserver(mApiService.getScrollBtns(mCommonParams), listener, false);

    }

    /**
     * 首页-下方导航
     */
    public void getTabLayoutBtns(GetTabLayoutBtnsParams params,
                                 ObserverOnResultListener<GetTabLayoutBtnsResponse> listener) {
        mCommonParams = getParams(HomeCommandAPI.GET_TABLAYOUT_BTNS, params);
        handleOnResultObserver(mApiService.getTabLayoutBtns(mCommonParams), listener, false);

    }

    /**
     * 首页- 切换app
     */
    public void getSwitchKey(String packageName, ObserverOnResultListener<GetSwitchKeyResponse> listener) {
        GetSwitchKeyParams params = new GetSwitchKeyParams(packageName, null);
        mCommonParams = getParams(HomeCommandAPI.GET_SWITCH_KEY, params);
        handleOnResultObserver(mApiService.getSwitchKey(mCommonParams), listener);
    }

    /**
     * 货主-获取货源刷新信息
     */
    public void getGoodsRefreshInfo(ObserverOnResultListener<GoodsRefreshInfoResponse> listener) {
        mCommonParams = getParams(HomeCommandAPI.GET_GOODS_REFRESH_INFO);
        handleOnResultObserver(mApiService.getGoodsRefreshInfo(mCommonParams), listener,false);
    }
}


