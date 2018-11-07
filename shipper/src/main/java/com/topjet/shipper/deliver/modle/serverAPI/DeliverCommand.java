package com.topjet.shipper.deliver.modle.serverAPI;

import com.topjet.common.base.listener.ObserverOnNextListener;
import com.topjet.common.base.listener.ObserverOnResultListener;
import com.topjet.common.base.net.serverAPI.BaseCommand;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.user.modle.params.PageNumParams;
import com.topjet.shipper.deliver.modle.params.OwnerGoodsParams;
import com.topjet.shipper.order.modle.response.AddGoodsReponse;
import com.topjet.shipper.order.modle.response.OftenGoodsListReponse;

/**
 * Created by tsj-004 on 2017/7/25.
 * 货源/订单相关请求方法
 */

public class DeliverCommand extends BaseCommand<DeliverCommandAPI> {

    public DeliverCommand(Class<DeliverCommandAPI> c, MvpActivity activity) {
        super(c, activity);
    }

    /**
     * 货主-常发货源列表
     */
    public void oftenGoodsList(PageNumParams params,
                               ObserverOnNextListener<OftenGoodsListReponse> listener) {
        mCommonParams = getParams(DeliverCommandAPI.OFTEN_GOODS_LIST, params);
        handleOnNextObserver(mApiService.oftenGoodsList(mCommonParams), listener, false);
    }

    /**
     * 货主-发布新货源
     */
    public void addGoods(String destination, OwnerGoodsParams params,
                         ObserverOnResultListener<AddGoodsReponse> listener) {
        mCommonParams = getParams(destination, params);
        handleOnResultObserver(mApiService.addGoods(mCommonParams), listener);
    }

    /**
     * 货主-修改货源
     */
    public void updateGoods(OwnerGoodsParams params,
                            ObserverOnResultListener<AddGoodsReponse> listener) {
        mCommonParams = getParams(DeliverCommandAPI.UPDATE_GOODS, params);
        handleOnResultObserver(mApiService.updateGoods(mCommonParams), listener);
    }

    /**
     * 货主-修改定向货源
     */
    public void updateGoodsAssigned(OwnerGoodsParams params,
                            ObserverOnResultListener<AddGoodsReponse> listener) {
        mCommonParams = getParams(DeliverCommandAPI.UPDATE_GOODS_ASSIGNED, params);
        handleOnResultObserver(mApiService.updateGoodsAssigned(mCommonParams), listener);
    }

    /**
     * 货主-货源信息（修改和复制用）
     */
    public void getParamsFromServerById(OwnerGoodsParams params,
                                        ObserverOnResultListener<OwnerGoodsParams> listener) {
        mCommonParams = getParams(DeliverCommandAPI.GET_PARAMS_FROM_SERVER_BY_ID, params);
        handleOnResultObserver(mApiService.getParamsFromServerById(mCommonParams), listener);
    }

    /**
     * 货主-发布定向货源
     */
    public void addGoodsAssigned(String destination, OwnerGoodsParams params,
                                 ObserverOnResultListener<AddGoodsReponse> listener) {
        mCommonParams = getParams(destination, params);
        handleOnResultObserver(mApiService.addGoodsAssigned(mCommonParams), listener);
    }
}
