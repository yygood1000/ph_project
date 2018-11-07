package com.topjet.shipper.deliver.controller;

import com.topjet.common.base.listener.ObserverOnResultListener;
import com.topjet.common.base.controller.BaseController;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.shipper.deliver.modle.params.OwnerGoodsParams;
import com.topjet.shipper.deliver.modle.serverAPI.DeliverCommand;
import com.topjet.shipper.deliver.modle.serverAPI.DeliverCommandAPI;
import com.topjet.shipper.order.modle.response.AddGoodsReponse;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

/**
 * Created by tsj-004 on 2017/9/6.
 * 发货--网络请求
 */

public class OwnerGoodsController extends BaseController {
    private MvpActivity activity;

    public OwnerGoodsController(RxAppCompatActivity activity) {
        this.activity = (MvpActivity) activity;
    }

    /**
     * 发布新货源
     */
    public void addGoods(OwnerGoodsParams ownerGoodsParams, final String tag) {
        addGoods(DeliverCommandAPI.ADD_GOODS, ownerGoodsParams, tag);
    }

    /**
     * 发布定向货源
     */
    public void addAssignedGoods(OwnerGoodsParams ownerGoodsParams, final String tag) {
        new DeliverCommand(DeliverCommandAPI.class, activity)
                .addGoodsAssigned(DeliverCommandAPI.ADD_GOODS_ASSIGNED, ownerGoodsParams, new
                        ObserverOnResultListener<AddGoodsReponse>() {
                    @Override
                    public void onResult(AddGoodsReponse result) {
                        result.setTag(tag);
                        postEvent(result);
                    }
                });
    }

    /**
     * 发布新货源、复制货源
     */
    private void addGoods(String destination, OwnerGoodsParams ownerGoodsParams, final String tag) {
        new DeliverCommand(DeliverCommandAPI.class, activity)
                .addGoods(destination, ownerGoodsParams, new ObserverOnResultListener<AddGoodsReponse>() {
                    @Override
                    public void onResult(AddGoodsReponse result) {
                        result.setTag(tag);
                        postEvent(result);
                    }
                });
    }

    /**
     * 修改货源
     */
    public void updateGoods(OwnerGoodsParams ownerGoodsParams, final String tag) {
        new DeliverCommand(DeliverCommandAPI.class, activity)
                .updateGoods(ownerGoodsParams, new ObserverOnResultListener<AddGoodsReponse>() {
                    @Override
                    public void onResult(AddGoodsReponse result) {
                        result.setTag(tag);
                        postEvent(result);
                    }
                });
    }

    /**
     * 修改定向货源
     */
    public void updateGoodsAssigned(OwnerGoodsParams ownerGoodsParams, final String tag) {
        new DeliverCommand(DeliverCommandAPI.class, activity)
                .updateGoodsAssigned(ownerGoodsParams, new ObserverOnResultListener<AddGoodsReponse>() {
                    @Override
                    public void onResult(AddGoodsReponse result) {
                        result.setTag(tag);
                        postEvent(result);
                    }
                });
    }

    /**
     * 根据id获取发货信息
     */
    public void getParamsFromServerById(String goodOrderId, final String tag) {
        OwnerGoodsParams ownerGoodsParams = new OwnerGoodsParams();
        ownerGoodsParams.setGoods_id(goodOrderId);
        new DeliverCommand(DeliverCommandAPI.class, activity)
                .getParamsFromServerById(ownerGoodsParams, new ObserverOnResultListener<OwnerGoodsParams>() {
                    @Override
                    public void onResult(OwnerGoodsParams result) {
                        result.setTag(tag);
                        postEvent(result);
                    }
                });
    }
}
