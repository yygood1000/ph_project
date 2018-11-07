package com.topjet.common.order_detail.modle.response;

import com.topjet.common.utils.StringUtils;

/**
 * creator: zhulunjun
 * time:    2017/12/4
 * describe: 消息，确认签收，确认提货 有用到
 */

public class MsgResponse {
    private String code;	// 执行结果	是	String	1.成功 2.失败
    private String msg;	// 错误原因	是	String	:-----

    public boolean getCode() {
        return StringUtils.isNotBlank(code) && code.equals("1");
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
