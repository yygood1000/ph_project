package com.topjet.common.user.modle.response;

import com.topjet.common.config.CConstants;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.config.CPersisData;
import com.topjet.common.db.bean.IMUserBean;
import com.topjet.common.utils.StringUtils;

/**
 * Created by tsj-004 on 2017/10/24.
 */

public class UserCenterParameterResponse {
    private String icon_image_key = null;//用户头像key
    private String icon_image_url = null;//用户头像url
    private String icon_image_status = null;//用户头像审核状态
    private String user_name = null;//用户姓名
    private String use_status = null;//用户实名认证状态 0、未认证 1、待审核 2、审核通过 3、资料修改待审核 4、认证失败
    private String sign_status = null;//签到状态  0：未签到 1：已签到
    private String obtain_integral = null;//未签到则返回今日签到积分，已签到则返回第二天的积分
    private String degree_of_praise = null;//好评度
    private String integral_amount = null;//积分总额
    private String wallet_balance = null;// 钱包余额
    private String user_auth_status = null;//身份认证状态 0、未认证 1、认证中 2、认证失败 3、认证通过
    private String anonymity_status = null;// 匿名设置状态
    private String audit_status = null;     //  车辆认证状态	1 未认证 2 已认证 3 认证中 4 认证失败
    private String recommend_name = null;     //  推荐人姓名

    /**
     * 获取保存本人信息的实体，供聊天使用
     */
    public IMUserBean getImUserInfo(UserCenterParameterResponse userInfo){
        IMUserBean userBean = new IMUserBean();
        userBean.setAvatar(userInfo.getIcon_image_url());
        userBean.setAvatarKey(userInfo.getIcon_image_key());
        userBean.setUserPhone(CMemoryData.getUserMobile());
        userBean.setNick(userInfo.getUser_name());
        userBean.setUsername((CMemoryData.isDriver() ?
                CConstants.IM_HEAR_DRIVER :
                CConstants.IM_HEAR_SHIPPER) +
                        CPersisData.getUserID());
        userBean.setUserId(CMemoryData.getUserId());
        return userBean;
    }
    public String getIcon_image_key() {
        return icon_image_key;
    }

    public void setIcon_image_key(String icon_image_key) {
        this.icon_image_key = icon_image_key;
    }

    public String getIcon_image_url() {
        return icon_image_url;
    }

    public void setIcon_image_url(String icon_image_url) {
        this.icon_image_url = icon_image_url;
    }

    public String getIcon_image_status() {
        return icon_image_status;
    }

    public void setIcon_image_status(String icon_image_status) {
        this.icon_image_status = icon_image_status;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUse_status() {
        return use_status;
    }

    public void setUse_status(String use_status) {
        this.use_status = use_status;
    }

    public String getSign_status() {
        return sign_status;
    }

    public void setSign_status(String sign_status) {
        this.sign_status = sign_status;
    }

    public String getObtain_integral() {
        return obtain_integral;
    }

    public void setObtain_integral(String obtain_integral) {
        this.obtain_integral = obtain_integral;
    }

    public String getDegree_of_praise() {
        return degree_of_praise;
    }

    public void setDegree_of_praise(String degree_of_praise) {
        this.degree_of_praise = degree_of_praise;
    }

    public String getIntegral_amount() {
        return integral_amount;
    }

    public void setIntegral_amount(String integral_amount) {
        this.integral_amount = integral_amount;
    }

    public String getWallet_balance() {
        return wallet_balance;
    }

    public void setWallet_balance(String wallet_balance) {
        this.wallet_balance = wallet_balance;
    }

    public String getUser_auth_status() {
        return user_auth_status;
    }

    public void setUser_auth_status(String user_auth_status) {
        this.user_auth_status = user_auth_status;
    }

    public String getAnonymity_status() {
        return anonymity_status;
    }

    public void setAnonymity_status(String anonymity_status) {
        this.anonymity_status = anonymity_status;
    }

    public String getAudit_status() {
        return audit_status;
    }

    public void setAudit_status(String audit_status) {
        this.audit_status = audit_status;
    }

    public String getRecommend_name() {
        return StringUtils.isNotBlank(recommend_name) ? recommend_name : "";
    }
}
