package com.topjet.common.user.modle.response;

/**
 * Created by tsj-004 on 2017/7/21.
 * 验证码发送状态
 */

public class SendCheckCodeResponse {
    private String status;  //1.成功 2.失败
    private String code;  //1.成功 2.失败

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }



    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
