package com.topjet.common.common.modle.response;

/**
 * creator: zhulunjun
 * time:    2017/10/23
 * describe: 分享二维码链接
 */

public class ShareResponse {
    private String share_url;
    private String sms_content;

    public String getSms_content() {
        return sms_content;
    }

    public void setSms_content(String sms_content) {
        this.sms_content = sms_content;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }
}
