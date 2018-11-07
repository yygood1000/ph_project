package com.topjet.common.common.modle.response;

import com.topjet.common.config.CConstants;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.config.CPersisData;
import com.topjet.common.db.bean.IMUserBean;
import com.topjet.common.utils.StringUtils;

/**
 * Created by tsj-004 on 2017/7/21.
 * 首页-获取用户参数,返回类
 */

public class GetUserInfoAtHomeResponse {
    private String is_exist;// 	是否存在常跑路线 1.存在 2.不存在 （司机专属）
    private String truck_status;// 车辆认证状态
    private String user_status;// 实名认证状态
    private String unread_sum;// 	未读消息总数
    private String img_key;	// 头像key	String
    private String img_url;	// 头像url	String
    private String user_name; // 昵称


    /**
     * 转换聊天用户信息
     */
    public IMUserBean getImUserBean(GetUserInfoAtHomeResponse userInfo){
        IMUserBean userBean = new IMUserBean();
        userBean.setAvatar(userInfo.getImg_url());
        userBean.setAvatarKey(userInfo.getImg_key());
        userBean.setNick(userInfo.getUser_name());
        userBean.setUsername((CMemoryData.isDriver() ?
                CConstants.IM_HEAR_DRIVER :
                CConstants.IM_HEAR_SHIPPER) +
                        CPersisData.getUserID());
        userBean.setUserId(CMemoryData.getUserId());
        userBean.setUserPhone(CMemoryData.getUserMobile());
        return userBean;
    }
    public boolean getIs_exist() {
        return StringUtils.isNotBlank(is_exist) && is_exist.equals("1");
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getImg_key() {
        return img_key;
    }

    public void setImg_key(String img_key) {
        this.img_key = img_key;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getTruck_status() {
        return truck_status;
    }

    public String getUser_status() {
        return user_status;
    }

    public void setIs_exist(String is_exist) {
        this.is_exist = is_exist;
    }

    public void setTruck_status(String truck_status) {
        this.truck_status = truck_status;
    }

    public void setUser_status(String user_status) {
        this.user_status = user_status;
    }

    public String getUnread_sum() {
        return unread_sum;
    }

    public void setUnread_sum(String unread_sum) {
        this.unread_sum = unread_sum;
    }

    @Override
    public String toString() {
        return "GetUserInfoAtHomeResponse{" +
                "is_exist='" + is_exist + '\'' +
                ", truck_status='" + truck_status + '\'' +
                ", user_status='" + user_status + '\'' +
                ", unread_sum='" + unread_sum + '\'' +
                '}';
    }
}