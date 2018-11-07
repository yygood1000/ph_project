package com.topjet.common.common.modle.response;


import com.topjet.common.utils.DataFormatFactory;
import com.topjet.common.utils.ListUtils;

import java.util.ArrayList;

/**
 * Created by yy on 2017/9/28.
 * <p>
 * 获取支付信息 返回体
 */

public class GetPayForInfoResponse {
    private String type = "";//	内部指定类型	 	1为支付运费、2为支付信息费、3为支付补贴、8为身份证核查
    private String transactionType = "";//	交易类型	 	1担保交易、2为实时到账（先默认传1）
    private String orderMoney = "";//		金额
    private String reMark = "";//	备注
    private String transportOrderId = "";//		订单号（传订单列表跳转订单详情的orderid）	否	String
    private String outUserId = "";//	付款的userid
    private String inUserId = "";//	收款的userid
    private String inUserName;//收款方姓名
    private String inUserMobile;//	收款方手机号码
    private String inUserIconKey;//收款方头像Key
    private String inUserIconUrl;//	收款方头像Url
    private String sex = "";//		性别 1 女 , 2 男
    private String isAnonymous = "";//	是否开启匿名 0关1 开
    private String createTime = "";// 创建时间
    private ArrayList<Splitinfo> splitinfo;//	子订单数组	 	若只有一笔订单就无需传递【具体结构见下表】


    /**
     * 子账单实体类
     */
    private class Splitinfo {
        private String merchantorderid;//	子账单号
        private String amt;//		金额
        private String feemsg;//	 费用描述

        public String getMerchantorderid() {
            return merchantorderid;
        }

        public String getAmt() {
            return amt;
        }

        public String getFeemsg() {
            return feemsg;
        }
    }

    public String getType() {
        return type;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public String getOrderMoney() {
        return orderMoney;
    }

    public String getReMark() {
        return reMark;
    }

    public String getTransportOrderId() {
        return transportOrderId;
    }

    public String getOutUserId() {
        return outUserId;
    }

    public String getInUserId() {
        return inUserId;
    }

    public String getSplitinfo() {
        return ListUtils.isEmpty(splitinfo) ? "" : DataFormatFactory.toJson(splitinfo);
    }

    public String getSex() {
        return sex;
    }

    public String getIsAnonymous() {
        return isAnonymous;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getInUserName() {
        return inUserName;
    }

    public String getInUserMobile() {
        return inUserMobile;
    }

    public String getInUserIconKey() {
        return inUserIconKey;
    }

    public String getInUserIconUrl() {
        return inUserIconUrl;
    }

    @Override
    public String toString() {
        return "GetPayForInfoResponse{" +
                "type='" + type + '\'' +
                ", transactionType='" + transactionType + '\'' +
                ", orderMoney='" + orderMoney + '\'' +
                ", reMark='" + reMark + '\'' +
                ", transportOrderId='" + transportOrderId + '\'' +
                ", outUserId='" + outUserId + '\'' +
                ", inUserId='" + inUserId + '\'' +
                ", sex='" + sex + '\'' +
                ", isAnonymous='" + isAnonymous + '\'' +
                ", createTime='" + createTime + '\'' +
                ", splitinfo=" + splitinfo +
                '}';
    }
}
