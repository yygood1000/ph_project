package com.topjet.crediblenumber.order.view.dialog;

import android.content.Context;

import com.topjet.common.common.modle.extra.RouteExtra;
import com.topjet.common.order_detail.modle.response.OrderInfoResponse;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.StringUtils;
import com.topjet.crediblenumber.R;

/**
 * creator: zhulunjun
 * time:    2017/10/16
 * describe: 司机拨打发货人，货主，收货人信息封装
 * 确认提货弹窗中，拨打电话需要使用的参数
 * 地图导航页需要用到
 */

public class CallOwnerInfoBean {
    private String owner_name;
    private String owner_mobile;
    private String order_id;
    private String order_state;
    private String sender_name;
    private String sender_mobile;
    private String receiver_mobile;
    private String receiver_name;

    // 最终显示的电话和名字
    private String mobile;
    private String name;

    private int type;
    public static final int PICK_UP = 1; // 提货
    public static final int SIGN = 2; // 签收

    public CallOwnerInfoBean() {
    }

    public CallOwnerInfoBean(String owner_name, String owner_mobile, String order_id, String order_state) {
        this.owner_name = owner_name;
        this.owner_mobile = owner_mobile;
        this.order_id = order_id;
        this.order_state = order_state;
    }

    /**
     * 初始化，是呼叫发货人，还是呼叫货主，呼叫收货人
     */
    public String getCallText(Context mContext) {
        String text = "";
        if (StringUtils.isNotBlank(getOwner_mobile())) {
            // 呼叫货主
            mobile = getOwner_mobile();
            name = getOwner_name();
            text = mContext.getString(R.string.call_ower);
            Logger.d("签收码弹窗 呼叫货主");
            if (getType() == CallOwnerInfoBean.PICK_UP) { // 签收
                if (StringUtils.isNotBlank(getSender_mobile()) &&
                        !getOwner_mobile().equals(getSender_mobile())) {
                    // 呼叫发货人
                    Logger.d("签收码弹窗 呼叫发货人");
                    mobile = getSender_mobile();
                    name = getSender_name();
                    text = mContext.getString(R.string.call_sender);
                }
            } else { // 提货
                if (StringUtils.isNotBlank(getReceiver_mobile()) &&
                        !getOwner_mobile().equals(getReceiver_mobile())) {
                    // 呼叫收货人
                    Logger.d("签收码弹窗 呼叫收货人");
                    mobile = getReceiver_mobile();
                    name = getReceiver_name();
                    text = mContext.getString(R.string.call_receiver);
                }
            }
        }
        return text;
    }

    /**
     * 设置拨打电话的显示信息
     * Mobile name text
     */
    public CallOwnerInfoBean setCallInfo(OrderInfoResponse mOrderInfo ,int type){
        setOrder_id(mOrderInfo.getOrder_id());
        setType(type);
        setOrder_state(mOrderInfo.getOrder_status() + "");
        if (mOrderInfo.getSender_info() != null) {
            setSender_mobile(mOrderInfo.getSender_info().getMobile());
            setSender_name(mOrderInfo.getSender_info().getName());
        }
        if (mOrderInfo.getOwner_info() != null) {
            setOwner_mobile(mOrderInfo.getOwner_info().getOwner_mobile());
            setOwner_name(mOrderInfo.getOwner_info().getOwner_name());
        }
        if(mOrderInfo.getReceiver_info() != null){
            setReceiver_mobile(mOrderInfo.getReceiver_info().getMobile());
            setReceiver_name(mOrderInfo.getReceiver_info().getName());
        }
        return this;
    }

    /**
     * 设置导航信息
     */
    public RouteExtra getRouteExtra(RouteExtra extra, Context context){
        if(extra != null){
            extra.setCallText(getCallText(context));
            extra.setMobile(getMobile());
            extra.setName(getName());
        }
        return extra;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getReceiver_mobile() {
        return receiver_mobile;
    }

    public void setReceiver_mobile(String receiver_mobile) {
        this.receiver_mobile = receiver_mobile;
    }

    public String getReceiver_name() {
        return receiver_name;
    }

    public void setReceiver_name(String receiver_name) {
        this.receiver_name = receiver_name;
    }

    public String getOwner_name() {
        return owner_name;
    }

    public String getSender_name() {
        return sender_name;
    }

    public CallOwnerInfoBean(String owner_name, String owner_mobile, String order_id, String order_state, String sender_name, String sender_mobile) {
        this.owner_name = owner_name;
        this.owner_mobile = owner_mobile;
        this.order_id = order_id;
        this.order_state = order_state;
        this.sender_name = sender_name;
        this.sender_mobile = sender_mobile;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public String getSender_mobile() {
        return sender_mobile;
    }

    public void setSender_mobile(String sender_mobile) {
        this.sender_mobile = sender_mobile;
    }

    public void setOwner_name(String owner_name) {
        this.owner_name = owner_name;
    }

    public String getOwner_mobile() {
        return owner_mobile;
    }

    public void setOwner_mobile(String owner_mobile) {
        this.owner_mobile = owner_mobile;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrder_state() {
        return order_state;
    }

    public void setOrder_state(String order_state) {
        this.order_state = order_state;
    }

    @Override
    public String toString() {
        return "CallOwnerInfoBean{" +
                "owner_name='" + owner_name + '\'' +
                ", owner_mobile='" + owner_mobile + '\'' +
                ", order_id='" + order_id + '\'' +
                ", order_state='" + order_state + '\'' +
                ", sender_name='" + sender_name + '\'' +
                ", sender_mobile='" + sender_mobile + '\'' +
                ", receiver_mobile='" + receiver_mobile + '\'' +
                ", receiver_name='" + receiver_name + '\'' +
                ", type=" + type +
                '}';
    }
}
