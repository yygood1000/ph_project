package com.topjet.common.contact.model;

/**
 * creator: zhulunjun
 * time:    2017/10/16
 * describe: 本地联系人
 */

public class ContactBean {


    private String name;

    private String mobile;

    private String sortLetters;

    public ContactBean(String name) {
        this.name = name;
    }

    public ContactBean(String name, String mobile) {
        this.name = name;
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }
}
