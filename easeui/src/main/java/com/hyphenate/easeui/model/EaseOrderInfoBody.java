package com.hyphenate.easeui.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.hyphenate.chat.EMMessageBody;
import com.topjet.common.order_detail.modle.bean.ImGoodsInfo;
import com.topjet.common.order_detail.modle.response.GoodsInfoResponse;
import com.topjet.common.order_detail.modle.response.OrderInfoResponse;

/**
 * author: zhulunjun
 * time:   2017/7/24
 * about:  IM发送的订单信息，需要的字段
 */

public class EaseOrderInfoBody extends EMMessageBody {

    private String orderId;//订单id
    private String goodsId;//货源id
    private String goodsVersion;//货源版本
    private boolean isGoods; // 是否货源
    private String departCity;// 出发地
    private String destinationCity;// 目的地
    private String truckInfo;//车辆信息综合


    /**
     * 根据订单详情 获取聊天实体
     */
    public EaseOrderInfoBody getOrderBody(OrderInfoResponse orderInfo) {
        EaseOrderInfoBody orderInfoBody = new EaseOrderInfoBody();
        orderInfoBody.setGoods(false);
        orderInfoBody.setOrderId(orderInfo.getOrder_id());
        orderInfoBody.setGoodsId(orderInfo.getGoods_id());
        orderInfoBody.setGoodsVersion(orderInfo.getGoods_version());
        setGoodsInfo(orderInfoBody, orderInfo.getIm_goods_info());
        return orderInfoBody;
    }

    public void setGoodsInfo(EaseOrderInfoBody orderInfoBody, ImGoodsInfo goodsInfo){
        if (goodsInfo != null) {
            orderInfoBody.setDepartCity(goodsInfo.getSender_city());
            orderInfoBody.setDestinationCity(goodsInfo.getReceiver_city());
            String truckInfo = goodsInfo.getQuantity_type()+" " +
                    goodsInfo.getQuantity_max()+" "+
                    goodsInfo.getTruck_length()+" "+
                    goodsInfo.getTruck_type();
            orderInfoBody.setTruckInfo(truckInfo);
        }
    }

    /**
     * 根据货源详情 获取聊天实体
     */
    public EaseOrderInfoBody getOrderBody(GoodsInfoResponse orderInfo) {
        EaseOrderInfoBody orderInfoBody = new EaseOrderInfoBody();
        orderInfoBody.setGoods(true);
        orderInfoBody.setGoodsId(orderInfo.getGoods_id());
        orderInfoBody.setGoodsVersion(orderInfo.getGoods_version());
        setGoodsInfo(orderInfoBody, orderInfo.getIm_goods_info());
        return orderInfoBody;
    }

    public String getGoodsVersion() {
        return goodsVersion;
    }

    public void setGoodsVersion(String goodsVersion) {
        this.goodsVersion = goodsVersion;
    }

    public boolean isGoods() {
        return isGoods;
    }

    public void setGoods(boolean goods) {
        isGoods = goods;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }


    public String getTruckInfo() {
        return truckInfo;
    }

    public void setTruckInfo(String truckInfo) {
        this.truckInfo = truckInfo;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getDepartCity() {
        return departCity;
    }

    public void setDepartCity(String departCity) {
        this.departCity = departCity;
    }

    public String getDestinationCity() {
        return destinationCity;
    }

    public void setDestinationCity(String destinationCity) {
        this.destinationCity = destinationCity;
    }


    public EaseOrderInfoBody() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.orderId);
        dest.writeString(this.goodsId);
        dest.writeString(this.goodsVersion);
        dest.writeByte(this.isGoods ? (byte) 1 : (byte) 0);
        dest.writeString(this.departCity);
        dest.writeString(this.destinationCity);
        dest.writeString(this.truckInfo);
    }

    protected EaseOrderInfoBody(Parcel in) {
        this.orderId = in.readString();
        this.goodsId = in.readString();
        this.goodsVersion = in.readString();
        this.isGoods = in.readByte() != 0;
        this.departCity = in.readString();
        this.destinationCity = in.readString();
        this.truckInfo = in.readString();
    }

    public static final Creator<EaseOrderInfoBody> CREATOR = new Creator<EaseOrderInfoBody>() {
        @Override
        public EaseOrderInfoBody createFromParcel(Parcel source) {
            return new EaseOrderInfoBody(source);
        }

        @Override
        public EaseOrderInfoBody[] newArray(int size) {
            return new EaseOrderInfoBody[size];
        }
    };
}
