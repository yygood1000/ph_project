package com.topjet.common.db.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * creator: zhulunjun
 * time:    2017/11/28
 * describe: im的用户数据保存
 */
@Entity
public class IMUserBean {

    @Id(autoincrement = true)
    private Long id; // 主键

    protected String avatar; // 头像
    protected String userId;//用户id
    protected String username; // 用户名 环信聊天的ID
    protected String nick; // 昵称
    protected String userPhone;//用户电话
    protected String isAnonymous = "0";//是否匿名 0显示头像，1不显示头像
    protected String avatarKey;//头像的key
    protected String sex = "2";//性别 1 女，2 男
    protected String reserve1; // 预留字段1
    protected String reserve2; // 预留字段2
    protected String reserve3; // 预留字段3

    @Generated(hash = 229972076)
    public IMUserBean(Long id, String avatar, String userId, String username,
            String nick, String userPhone, String isAnonymous, String avatarKey,
            String sex, String reserve1, String reserve2, String reserve3) {
        this.id = id;
        this.avatar = avatar;
        this.userId = userId;
        this.username = username;
        this.nick = nick;
        this.userPhone = userPhone;
        this.isAnonymous = isAnonymous;
        this.avatarKey = avatarKey;
        this.sex = sex;
        this.reserve1 = reserve1;
        this.reserve2 = reserve2;
        this.reserve3 = reserve3;
    }

    @Generated(hash = 610458857)
    public IMUserBean() {
    }


    public void setUserBean(IMUserBean userBean){
        setAvatar(userBean.getAvatar());
        setAvatarKey(userBean.getAvatarKey());
        setIsAnonymous(userBean.getIsAnonymous());
        setNick(userBean.getNick());
        setSex(userBean.getSex());
        setUserId(userBean.getUserId());
        setUsername(userBean.getUsername());
        setUserPhone(userBean.getUserPhone());
    }
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNick() {
        return this.nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getUserPhone() {
        return this.userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getIsAnonymous() {
        return this.isAnonymous;
    }

    public void setIsAnonymous(String isAnonymous) {
        this.isAnonymous = isAnonymous;
    }

    public String getAvatarKey() {
        return this.avatarKey;
    }

    public void setAvatarKey(String avatarKey) {
        this.avatarKey = avatarKey;
    }

    public String getSex() {
        return this.sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getReserve1() {
        return this.reserve1;
    }

    public void setReserve1(String reserve1) {
        this.reserve1 = reserve1;
    }

    public String getReserve2() {
        return this.reserve2;
    }

    public void setReserve2(String reserve2) {
        this.reserve2 = reserve2;
    }

    public String getReserve3() {
        return this.reserve3;
    }

    public void setReserve3(String reserve3) {
        this.reserve3 = reserve3;
    }
}
