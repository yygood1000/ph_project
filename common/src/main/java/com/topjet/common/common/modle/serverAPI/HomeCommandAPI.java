package com.topjet.common.common.modle.serverAPI;

import com.topjet.common.base.net.request.CommonParams;
import com.topjet.common.base.net.response.BaseResponse;
import com.topjet.common.common.modle.params.GetScrollBtnsParams;
import com.topjet.common.common.modle.params.GetSwitchKeyParams;
import com.topjet.common.common.modle.response.GetScrollBtnsResponse;
import com.topjet.common.common.modle.response.GetSwitchKeyResponse;
import com.topjet.common.common.modle.response.GetTabLayoutBtnsResponse;
import com.topjet.common.common.modle.response.GetUserInfoAtHomeResponse;
import com.topjet.common.common.modle.response.GoodsRefreshInfoResponse;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by tsj-004 on 2017/7/25.
 * 首页相关
 */

public interface HomeCommandAPI {

    // 首页获取用户参数
    String GET_THE_USER_PARAMETER = "home.getTheUserParameter";
    @POST("user-service/home/getTheUserParameter")
    Observable<BaseResponse<GetUserInfoAtHomeResponse>> getTheUserParameter(@Body CommonParams commonParams);

    // 首页 获取中间滑动按钮组
    String GET_SCROLL_BTNS = "resourcehome.middlehomeoptions";
    @POST("resource-service/resourcehome/middlehomeoptions")
    Observable<BaseResponse<GetScrollBtnsResponse>> getScrollBtns(@Body CommonParams<GetScrollBtnsParams> commonParams);

    // 首页-下方导航
    String GET_TABLAYOUT_BTNS = "resourcehome.belowhomenavigation";
    @POST("resource-service/resourcehome/belowhomenavigation")
    Observable<BaseResponse<GetTabLayoutBtnsResponse>> getTabLayoutBtns(@Body CommonParams<GetTabLayoutBtnsResponse> commonParams);

    // 首页 切换app前获取key
    String GET_SWITCH_KEY = "appswitch.getswitchkey";
    @POST("user-service/appswitch/getswitchkey")
    Observable<BaseResponse<GetSwitchKeyResponse>> getSwitchKey(@Body CommonParams<GetSwitchKeyParams> commonParams);

    // 货主-获取货源刷新信息
    String GET_GOODS_REFRESH_INFO = "goodsshowinfocontroll.goodsrefreshinfo";
    @POST("order-service/goodsshowinfocontroll/goodsrefreshinfo")
    Observable<BaseResponse<GoodsRefreshInfoResponse>> getGoodsRefreshInfo(@Body CommonParams commonParams);
}
