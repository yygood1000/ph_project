package com.topjet.common.message.presenter;

import android.content.Context;

import com.topjet.common.R;
import com.topjet.common.base.CommonProvider;
import com.topjet.common.base.listener.ObserverOnNextListener;
import com.topjet.common.base.listener.ObserverOnResultListener;
import com.topjet.common.base.presenter.BaseApiPresenter;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.message.modle.bean.MessageListInfo;
import com.topjet.common.message.modle.extra.MessageListExtra;
import com.topjet.common.message.modle.params.MessageListParams;
import com.topjet.common.message.modle.params.ReadFlagParams;
import com.topjet.common.message.modle.response.GetMessageListResponse;
import com.topjet.common.message.modle.response.ReadFlagResponse;
import com.topjet.common.message.modle.serverAPI.MessageCommand;
import com.topjet.common.message.view.activity.MessageView;
import com.topjet.common.order_detail.modle.params.GoodsIdParams;
import com.topjet.common.order_detail.modle.response.OrderIdResponse;
import com.topjet.common.order_detail.modle.serverAPI.OrderDetailCommand;
import com.topjet.common.order_detail.modle.serverAPI.OrderDetailCommandApi;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.ResourceUtils;
import com.topjet.common.utils.StringUtils;
import com.topjet.wallet.model.BridgingCenter;

/**
 * 消息列表Presenter
 * Created by tsj028 on 2017/12/4 0004.
 */

public class MessagePresenter extends BaseApiPresenter<MessageView<MessageListInfo>, MessageCommand> {
    public MessageListExtra message_extra;

    public MessagePresenter(MessageView<MessageListInfo> mView, Context mContext, MessageCommand mApiCommand) {
        super(mView, mContext, mApiCommand);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        message_extra = (MessageListExtra) mActivity.getIntentExtra(MessageListExtra.getExtraName());
        if (message_extra == null) {
            // 默认是系统消息
            message_extra = new MessageListExtra(MessageListExtra.MESSAGE_LIST_SYSTEM);
        }
        setTitle();
    }

    /**
     * 设置标题
     */
    private void setTitle() {
        if (message_extra.getType().equals(MessageListExtra.MESSAGE_LIST_ORDER)) {
            mView.setTitle(ResourceUtils.getString(R.string.order_message));
        } else if (message_extra.getType().equals(MessageListExtra.MESSAGE_LIST_WALLET)) {
            mView.setTitle(ResourceUtils.getString(R.string.wallet_message));
        }
    }

    /**
     * 获取消息列表方法
     */
    public void getMessageListData(int type, int page) {
        MessageListParams params = new MessageListParams(type + "", page + "");

        mApiCommand.getMessageList(params, new ObserverOnNextListener<GetMessageListResponse>() {
            @Override
            public void onNext(GetMessageListResponse myMessageResponse) {
                if (myMessageResponse != null) {
                    mView.loadSuccess(myMessageResponse.getList());
                    // 消息列表数据请求成功，调用 设置消息为已读 接口，将该类型消息全部置为已读
                    if (null != message_extra && StringUtils.isNotBlank(message_extra.getType())) {
                        setReadFlag(message_extra.getType(), ReadFlagParams.SETTING_TYPE_1, null);
                    }

                }
            }

            @Override
            public void onError(String errorCode, String msg) {
                mView.loadFail(msg);
            }
        });
    }

    /**
     * 设置消息为已读
     */
    public void setReadFlag(String message_type, String setting_type, String[] ids) {
        ReadFlagParams params = new ReadFlagParams(message_type, setting_type, ids);
        mApiCommand.setReadFlag(params, new ObserverOnResultListener<ReadFlagResponse>() {
            @Override
            public void onResult(ReadFlagResponse readFlagResponse) {
                Logger.d("oye", "消息已设置为已读");
            }
        });
    }


    /**
     * 判断是进入订单详情还是货源详情
     */
    public void jumpOrderDetail(final String goodsId) {
        new OrderDetailCommand(OrderDetailCommandApi.class, mActivity).getOrderIdByGoodsId(new GoodsIdParams(goodsId)
                , new ObserverOnResultListener<OrderIdResponse>() {
                    @Override
                    public void onResult(OrderIdResponse orderIdResponse) {
                        if (StringUtils.isNotBlank(orderIdResponse.getOrder_id())) {
                            CommonProvider.getInstance().goOrderDetail(mActivity, orderIdResponse.getOrder_id());
                        } else {
                            CommonProvider.getInstance().goGoodsDetail(mActivity, goodsId);
                        }
                    }
                });
    }

    /**
     * 进入钱包账单
     */
    public void jumpWallet(){
        new BridgingCenter().OrderListJumpWallet(mActivity, CMemoryData.getUserId());
    }

}
