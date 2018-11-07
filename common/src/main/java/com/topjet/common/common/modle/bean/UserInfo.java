package com.topjet.common.common.modle.bean;

import com.topjet.common.base.model.BaseExtra;
import com.topjet.common.db.bean.IMUserBean;
import com.topjet.common.utils.StringUtils;

/**
 * creator: zhulunjun
 * time:    2017/11/13
 * describe: 用户信息，通过诚信查询得到
 */

public class UserInfo extends BaseExtra{

    private String user_id;    // 用户id	String
    private String icon_image_key;    // 	String
    private String icon_image_url;    // 头像url	String
    private String user_name;    // 用户姓名	String
    private String user_mobile;    // 用户手机号	String
    private String user_type;    // 用户类型	String	1 : 司机 2 : 货主',
    private String use_status;// 实名认证状态	String	0：认证未通过 1：认证通过
    private String user_auth_status;    // 身份认证状态	String	0：认证未通过 1：认证通过
    private String truck_status;    // 车辆认证状态	String	0：认证未通过 1：认证通过 （只有用户是司机才反）
    private String degree_of_praise;    // 好评度	String
    private String evaluation_score;    // 评价得分	String
    private String integrity_value;    // 诚信值	String
    private String integrity_value_level;// 诚信值等级	String
    private String shipments_or_receiving_num = "0";// 发货/接单数	String
    private String clinch_a_deal_num = "0";    // 成交数	String

    public static final String DRIVER = "1"; // 司机
    public static final String SHIPPER = "2"; // 货主

    public static final String PASS = "1"; // 认证通过
    public static final String NOT_PASS = "0"; // 未通过

    /**
     * 获取聊天用的用户信息
     */
    public IMUserBean getIMUserInfo(UserInfo userInfo){
        IMUserBean userBean = new IMUserBean();
        userBean.setUsername(userInfo.getUser_id());
        userBean.setUserId(userInfo.getUser_id());
        userBean.setNick(userInfo.getUser_name());
        userBean.setUserPhone(userInfo.getUser_mobile());
        userBean.setAvatarKey(userInfo.getIcon_image_key());
        userBean.setAvatar(userInfo.getIcon_image_url());

        return userBean;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
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

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_mobile() {
        return user_mobile;
    }

    public void setUser_mobile(String user_mobile) {
        this.user_mobile = user_mobile;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public boolean getUse_status() {
        return StringUtils.isNotBlank(use_status) && use_status.equals(PASS);
    }

    public void setUse_status(String use_status) {
        this.use_status = use_status;
    }

    public boolean getUser_auth_status() {
        return StringUtils.isNotBlank(user_auth_status) && user_auth_status.equals(PASS);
    }

    public void setUser_auth_status(String user_auth_status) {
        this.user_auth_status = user_auth_status;
    }

    public boolean getTruck_status() {
        return StringUtils.isNotBlank(truck_status) && truck_status.equals(PASS);
    }

    public void setTruck_status(String truck_status) {
        this.truck_status = truck_status;
    }

    public float getDegree_of_praise() {
        return StringUtils.getFloatToString(degree_of_praise);
    }

    public void setDegree_of_praise(String degree_of_praise) {
        this.degree_of_praise = degree_of_praise;
    }

    public String getEvaluation_score() {

        return StringUtils.isEmpty(evaluation_score) || evaluation_score.equals("0") ? "0.0" :evaluation_score;
    }

    public void setEvaluation_score(String evaluation_score) {
        this.evaluation_score = evaluation_score;
    }

    public String getIntegrity_value() {
        return integrity_value;
    }

    public void setIntegrity_value(String integrity_value) {
        this.integrity_value = integrity_value;
    }

    public String getIntegrity_value_level() {
        return integrity_value_level;
    }

    public void setIntegrity_value_level(String integrity_value_level) {
        this.integrity_value_level = integrity_value_level;
    }

    public String getShipments_or_receiving_num() {
        return StringUtils.isBlank(shipments_or_receiving_num) ? "0" : shipments_or_receiving_num;
    }

    public void setShipments_or_receiving_num(String shipments_or_receiving_num) {
        this.shipments_or_receiving_num = shipments_or_receiving_num;
    }

    public String getClinch_a_deal_num() {
        return StringUtils.isBlank(clinch_a_deal_num) ? "0" : clinch_a_deal_num;
    }

    public void setClinch_a_deal_num(String clinch_a_deal_num) {
        this.clinch_a_deal_num = clinch_a_deal_num;
    }
}
