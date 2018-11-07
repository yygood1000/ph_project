package com.hyphenate.easeui.widget.chatrow;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessage.ChatType;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.R;
import com.hyphenate.exceptions.HyphenateException;
import com.topjet.common.base.CommonProvider;
import com.topjet.common.base.listener.ObserverOnResultListener;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.order_detail.modle.params.GoodsIdParams;
import com.topjet.common.order_detail.modle.response.OrderIdResponse;
import com.topjet.common.order_detail.modle.response.ValidGoodsResponse;
import com.topjet.common.order_detail.modle.serverAPI.OrderDetailCommand;
import com.topjet.common.order_detail.modle.serverAPI.OrderDetailCommandApi;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.StringUtils;
import com.topjet.common.utils.Toaster;

import static com.topjet.common.R2.id.v;

/**
 * 订单详情的消息UI处理
 */
public class EaseChatRowOrderInfo extends EaseChatRow {

    private TextView tvDepart;
    private TextView tvDestination;
    private TextView tvTruckInfo;
    private TextView tvShowOrder;
    private LinearLayout llShowOrder;

    public EaseChatRowOrderInfo(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
    }

    @Override
    protected void onInflateView() {
        inflater.inflate(message.direct() == EMMessage.Direct.RECEIVE ?
                R.layout.ease_row_received_order_info : R.layout.ease_row_sent_order_info, this);
    }

    @Override
    protected void onFindViewById() {
        tvDepart = (TextView) findViewById(R.id.tv_depart);
        tvDestination = (TextView) findViewById(R.id.tv_destination);
        tvTruckInfo = (TextView) findViewById(R.id.tv_truck_info);
        tvShowOrder = (TextView) findViewById(R.id.tv_show_order);
        llShowOrder = (LinearLayout) findViewById(R.id.ll_show_order);

    }


    @Override
    protected void onSetUpView() {
        // 地图显示白色背景
        bubbleLayout.setBackgroundResource(R.drawable.ease_chatto_bg);
        try {
            tvDepart.setText(message.getStringAttribute(EaseConstant.MESSAGE_ATTR_DEPART));
            tvDestination.setText(message.getStringAttribute(EaseConstant.MESSAGE_ATTR_DESTINATION));
            tvTruckInfo.setText(message.getStringAttribute(EaseConstant.MESSAGE_ATTR_TRUCK_INFO));

        } catch (HyphenateException e) {
            e.printStackTrace();
        }
        tvShowOrder.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onBubbleClick();
            }
        });

        // handle sending message
        if (message.direct() == EMMessage.Direct.SEND) {
            setMessageSendCallback();
            switch (message.status()) {
                case CREATE:
                    progressBar.setVisibility(View.GONE);
                    statusView.setVisibility(View.VISIBLE);
                    resendView.setVisibility(VISIBLE);
                    llShowOrder.setVisibility(GONE);
                    break;
                case SUCCESS:
                    progressBar.setVisibility(View.GONE);
                    statusView.setVisibility(View.GONE);
                    resendView.setVisibility(GONE);
                    llShowOrder.setVisibility(VISIBLE);
                    break;
                case FAIL:
                    progressBar.setVisibility(View.GONE);
                    statusView.setVisibility(View.VISIBLE);
                    resendView.setVisibility(VISIBLE);
                    llShowOrder.setVisibility(GONE);
                    break;
                case INPROGRESS:
                    progressBar.setVisibility(View.VISIBLE);
                    statusView.setVisibility(View.GONE);
                    resendView.setVisibility(GONE);
                    llShowOrder.setVisibility(VISIBLE);
                    break;
                default:
                    break;
            }
        } else {
            // 接收到的订单信息显示白色三角
            imageBubbleTriangle.setImageResource(R.drawable.ic_message_v_left_white);
            if (!message.isAcked() && message.getChatType() == ChatType.Chat) {
                try {
                    EMClient.getInstance().chatManager().ackMessageRead(message.getFrom(), message.getMsgId());
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onUpdateView() {
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onBubbleClick() {
        if(CMemoryData.isDriver()){
            checkGoodsState();
        } else {
            checkGoodsId();
        }



    }

    /**
     * 检查是去订单详情还是货源详情
     */
    private void checkGoodsId(){
        final String goodsId = message.getStringAttribute(EaseConstant.MESSAGE_ATTR_GOODS_ID, "0");
        new OrderDetailCommand(OrderDetailCommandApi.class, (MvpActivity) context).getOrderIdByGoodsId(new GoodsIdParams(goodsId), new ObserverOnResultListener<OrderIdResponse>() {
            @Override
            public void onResult(OrderIdResponse orderIdResponse) {
                if(StringUtils.isNotBlank(orderIdResponse.getOrder_id())) {
                    CommonProvider.getInstance().goOrderDetail((MvpActivity) context, orderIdResponse.getOrder_id());
                } else {
                    CommonProvider.getInstance().goGoodsDetail((MvpActivity) context, goodsId);
                }
            }
        });
    }

    /**
     * 检查货源状态
     */
    private void checkGoodsState(){
        // 司机需要查询是否撤销
        String goodsId = message.getStringAttribute(EaseConstant.MESSAGE_ATTR_GOODS_ID, "0");
        String goodsVersion = message.getStringAttribute(EaseConstant.MESSAGE_ATTR_GOODS_VERSION, "0");
        new OrderDetailCommand(OrderDetailCommandApi.class, (MvpActivity) context)
                .selectValidGoods(new GoodsIdParams(goodsId, goodsVersion), new
                        ObserverOnResultListener<ValidGoodsResponse>() {
                            @Override
                            public void onResult(ValidGoodsResponse response) {
                                // 1.有效 2.失效
                                if (response.isVallid()) {
                                    checkGoodsId();
                                } else {
                                    Toaster.showLongToast("很可惜，该订单已成交/撤销");
                                }
                            }
                        });
    }

}
