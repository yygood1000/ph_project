package com.topjet.common.db.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * creator: zhulunjun
 * time:    2017/12/29
 * describe: 保存诚信查询的手机号
 */
@Entity
public class PhoneNumberBean {
    @Id(autoincrement = true)
    private Long id; // 主键

    private String phoneNumber; //	手机号

    @Generated(hash = 1329506575)
    public PhoneNumberBean(Long id, String phoneNumber) {
        this.id = id;
        this.phoneNumber = phoneNumber;
    }

    @Generated(hash = 206364539)
    public PhoneNumberBean() {
    }

    public PhoneNumberBean(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
