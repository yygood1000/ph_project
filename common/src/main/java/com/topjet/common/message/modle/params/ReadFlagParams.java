package com.topjet.common.message.modle.params;

/**
 * 设置消息为已读
 * Created by tsj028 on 2017/12/8 0008.
 */

public class ReadFlagParams {
    private String message_type;     // 消息类型	是	string	1系统 2 订单 3钱包
    private String setting_type;     //设置类型	是	string	0.单个 1.列表
    private String[] ids;     // 已读的id集合	是	string	ID集合

    public static final String SETTING_TYPE_1 = "1";
    public static final String SETTING_TYPE_0 = "0";

    public ReadFlagParams(String message_type, String setting_type, String[] ids){
        this.message_type = message_type;
        this.setting_type = setting_type;
        this.ids = ids;
    }

    public String getMessage_type() {
        return message_type;
    }

    public void setMessage_type(String message_type) {
        this.message_type = message_type;
    }

    public String getSetting_type() {
        return setting_type;
    }

    public void setSetting_type(String setting_type) {
        this.setting_type = setting_type;
    }

    public String[] getIds() {
        return ids;
    }

    public void setIds(String[] ids) {
        this.ids = ids;
    }
}
