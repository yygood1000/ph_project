package com.topjet.common.order_detail.presenter;

import android.content.Context;

import com.topjet.common.base.listener.ObserverOnNextListener;
import com.topjet.common.base.presenter.BaseApiPresenter;
import com.topjet.common.config.Config;
import com.topjet.common.order_detail.modle.extra.ProtocolExtra;
import com.topjet.common.order_detail.modle.params.GoodsIdParams;
import com.topjet.common.order_detail.modle.response.ProtocolResponse;
import com.topjet.common.order_detail.modle.serverAPI.OrderDetailCommand;
import com.topjet.common.order_detail.view.activity.ProtocolView;
import com.topjet.common.utils.DataFormatFactory;
import com.topjet.common.utils.Logger;

/**
 * Created by yy on 2017/9/22.
 * 交易协议Presenter
 */

public class ProtocolPresenter extends BaseApiPresenter<ProtocolView, OrderDetailCommand> {
    public ProtocolExtra extra;
    public String contentJson;

    public ProtocolPresenter(ProtocolView mView, Context mContext, OrderDetailCommand mApiCommand) {
        super(mView, mContext, mApiCommand);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 获取外部传入的信息
        extra = (ProtocolExtra) mActivity.getIntentExtra(ProtocolExtra.getExtraName());
        mView.initBtn();
    }


    public String getUrl() {
        switch (extra.getPageType()) {
            case ProtocolExtra.TYPE_DELIVER_GOODS:// 发布定向货源
                return Config.getProtocolUrl() + "?type=1";
            case ProtocolExtra.TYPE_CONFIRM_HAS_NOT_DIALOG://确认成交
            case ProtocolExtra.TYPE_CONFIRM_HAS_DIALOG://确认成交
            case ProtocolExtra.TYPE_ALTER_PAY_TYPE:// 修改支付方式
                return Config.getProtocolUrl() + "?type=2";
            case ProtocolExtra.TYPE_ACCEPT_ORDER:// 确认承接
                return Config.getProtocolUrl() + "?type=3";
            case ProtocolExtra.TYPE_CHECK_PROTOCOL://查看合同
                return Config.getProtocolUrl() + "?type=4";
        }
        return "";
    }

    /**
     * 获取协议显示信息
     */
    public void getProtocolInfo() {
        GoodsIdParams params = new GoodsIdParams(extra.getGoods_id());
        mApiCommand.getProtocolInfo(params, new ObserverOnNextListener<ProtocolResponse>() {
            @Override
            public void onNext(ProtocolResponse response) {
                // 拿到合同信息，根据不同跳转入口，对信息进行补全
                creatContent(response);
            }

            @Override
            public void onError(String errorCode, String msg) {
                mView.showToast(msg);
            }
        });
    }

    /**
     * 构造传给H5页面的数据
     * <p>
     * 注意：进入协议页面，数据的处理总的分为3种
     * 1、（查看合同）与（司机点击我要承接）与（修改支付方式按钮）进入交易协议，此时订单已经生成，协议页面显示的所有数据都由服务端返回
     * 2、通过支付明细弹窗进入协议页面的，需要将运费明细进行赋值（确认成交-提付部分订）
     * 3、不经过支付明细直接进入交易协议页面的，只需要将总运费进行赋值（确认成交-非提付部分订单）
     */
    private void creatContent(ProtocolResponse response) {
        if (extra.getPageType() == ProtocolExtra.TYPE_CONFIRM_HAS_NOT_DIALOG
                || extra.getPageType() == ProtocolExtra.TYPE_CONFIRM_HAS_DIALOG) {
            // 补全车辆/司机信息
            setTruckAndDriverInfo(response);

            // 设置费用明细
            response.setFreight_fee(extra.getFreight_fee());
            response.setAhead_fee(extra.getPayTypeParams().getAhead_fee());
            response.setDelivery_fee(extra.getPayTypeParams().getDelivery_fee());
            response.setBack_fee(extra.getPayTypeParams().getBack_fee());

        }
        contentJson = DataFormatFactory.toJson(response);
        Logger.i("oye", "传给H5的Json =========   " + contentJson);
        // 通知View加载H5
        mView.loadWeb();
    }

    /**
     * 补全车辆/司机信息
     */
    private void setTruckAndDriverInfo(ProtocolResponse response) {
        response.setTruck_type(extra.getTruck_type());
        response.setTruck_length(extra.getTruck_length());

        response.setDriver_name(extra.getDriver_name());
        response.setDriver_mobile(extra.getDriver_mobile());
        response.setDriver_id_card(extra.getDriver_id_card());
        response.setDriver_license_plate_number(extra.getDriver_license_plate_number());
    }
}
