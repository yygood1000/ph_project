package com.topjet.common.common.modle.bean;

/**
 * creator: zhulunjun
 * time:    2018/3/13
 * describe: 加密数据
 */

public class KeyBean {

    private String aes_key;	// 加密key	string
    private String aes_ivcode; //	加密偏移量	string

    public String getAes_key() {
        return aes_key;
    }

    public void setAes_key(String aes_key) {
        this.aes_key = aes_key;
    }

    public String getAes_ivcode() {
        return aes_ivcode;
    }

    public void setAes_ivcode(String aes_ivcode) {
        this.aes_ivcode = aes_ivcode;
    }
}
