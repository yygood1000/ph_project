package com.topjet.common.im.model.bean;

import com.topjet.common.db.bean.IMUserBean;

/**
 * creator: zhulunjun
 * time:    2017/12/7
 * describe: 历史聊天记录中的用户信息
 */

public class HistoryUserBean {

    private String  talk_id;	// 否	string	聊天对象id 560id
    private String  talk_name;	// 否	string	聊天对象用户名
    private String  talk_phone;	// 否	string	聊天对象手机号
    private String  sex;	// 否	string	聊天对象性别	性别 1 女 ，2 男
    private String  is_anonymous;	// 否	string	是否需要显示匿名	0显示头像，1不显示头像
    private String  img_url;	// 否	string	聊天对象头像	没有匿名或者 有匿名但二者有成交订单，显示头像 ，其余情况不返回头像
    private String  img_key;	// 否	string	头像key


    public IMUserBean getUserBean(HistoryUserBean userBean){
        IMUserBean imUserBean = new IMUserBean();
        imUserBean.setUserId(userBean.getTalk_id());
        imUserBean.setUsername(userBean.getTalk_id());
        imUserBean.setNick(userBean.getTalk_name());
        imUserBean.setSex(userBean.getSex());
        imUserBean.setAvatarKey(userBean.getImg_key());
        imUserBean.setAvatar(userBean.getImg_url());
        imUserBean.setIsAnonymous(userBean.getIs_anonymous());
        return imUserBean;
    }

    public String getTalk_id() {
        return talk_id;
    }

    public void setTalk_id(String talk_id) {
        this.talk_id = talk_id;
    }

    public String getTalk_name() {
        return talk_name;
    }

    public void setTalk_name(String talk_name) {
        this.talk_name = talk_name;
    }

    public String getTalk_phone() {
        return talk_phone;
    }

    public void setTalk_phone(String talk_phone) {
        this.talk_phone = talk_phone;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getIs_anonymous() {
        return is_anonymous;
    }

    public void setIs_anonymous(String is_anonymous) {
        this.is_anonymous = is_anonymous;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getImg_key() {
        return img_key;
    }

    public void setImg_key(String img_key) {
        this.img_key = img_key;
    }
}
