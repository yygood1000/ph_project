package com.topjet.common.order_detail.modle.bean;

import com.topjet.common.base.model.BaseExtra;
import com.topjet.common.db.bean.IMUserBean;
import com.topjet.common.utils.StringUtils;

/**
 * Created by yy on 2017/9/9.
 * 货主信息 实体类
 */

public class OwnerInfo extends BaseExtra {

    private String owner_id;//货主id	是	String
    private String owner_name;//货主名字	是	String
    private String owner_icon_url;//	货主头像url	是	String
    private String owner_icon_key;//货主头像key	是	String
    private String owner_comment_level;//	货主信用星级	是	String
    private String owner_mobile;//货主手机电话	是	String
    private String owner_make_a_bargain_sum;    //货主成交总数	是	string
    private String sex;    //	性别 1女2男
    private String is_anonymous;    //是否匿名 0否 1是

    public OwnerInfo() {
    }


    public OwnerInfo(String owner_id, String owner_name, String owner_icon_url, String owner_icon_key, String
            owner_comment_level, String owner_mobile) {
        this.owner_id = owner_id;
        this.owner_name = owner_name;
        this.owner_icon_url = owner_icon_url;
        this.owner_icon_key = owner_icon_key;
        this.owner_comment_level = owner_comment_level;
        this.owner_mobile = owner_mobile;
    }

    /**
     * 获取聊天用的用户信息, 跟货主聊
     */
    public IMUserBean getIMUserInfo(OwnerInfo userInfo) {
        IMUserBean userBean = new IMUserBean();
        userBean.setUsername(userInfo.getOwner_id());
        userBean.setNick(userInfo.getOwner_name());
        userBean.setUserPhone(userInfo.getOwner_mobile());
        userBean.setAvatarKey(userInfo.getOwner_icon_key());
        userBean.setAvatar(userInfo.getOwner_icon_url());
        userBean.setUserId(userInfo.getOwner_id());

        return userBean;
    }

    public String getOwner_id() {

        return owner_id;
    }

    public String getOwner_make_a_bargain_sum() {
        return owner_make_a_bargain_sum;
    }

    public void setOwner_make_a_bargain_sum(String owner_make_a_bargain_sum) {
        this.owner_make_a_bargain_sum = owner_make_a_bargain_sum;
    }

    public void setOwner_id(String owner_id) {
        this.owner_id = owner_id;
    }

    public String getOwner_name() {
        return owner_name;
    }

    public void setOwner_name(String owner_name) {
        this.owner_name = owner_name;
    }

    public String getOwner_icon_url() {
        return owner_icon_url;
    }

    public void setOwner_icon_url(String owner_icon_url) {
        this.owner_icon_url = owner_icon_url;
    }

    public String getOwner_icon_key() {
        return owner_icon_key;
    }

    public void setOwner_icon_key(String owner_icon_key) {
        this.owner_icon_key = owner_icon_key;
    }

    public float getOwner_comment_level() {
        return StringUtils.isBlank(owner_comment_level) ? 0 : Float.parseFloat(owner_comment_level);
    }

    public void setOwner_comment_level(String owner_comment_level) {
        this.owner_comment_level = owner_comment_level;
    }

    public String getOwner_mobile() {
        return owner_mobile;
    }

    public void setOwner_mobile(String owner_mobile) {
        this.owner_mobile = owner_mobile;
    }

    public boolean getIs_anonymous() {
        return StringUtils.isNotBlank(is_anonymous) && is_anonymous.equals("1");
    }

    public boolean getIsMan() {
        if (StringUtils.isNotBlank(sex) && sex.equals("1")) {
            return false;
        } else if (StringUtils.isNotBlank(sex) && sex.equals("2")) {
            return true;
        }
        return true;
    }

    @Override
    public String toString() {
        return "OwnerInfo{" +
                "owner_id='" + owner_id + '\'' +
                ", owner_name='" + owner_name + '\'' +
                ", owner_icon_url='" + owner_icon_url + '\'' +
                ", owner_icon_key='" + owner_icon_key + '\'' +
                ", owner_comment_level='" + owner_comment_level + '\'' +
                ", owner_mobile='" + owner_mobile + '\'' +
                ", owner_make_a_bargain_sum='" + owner_make_a_bargain_sum + '\'' +
                ", sex='" + sex + '\'' +
                ", is_anonymous='" + is_anonymous + '\'' +
                '}';
    }
}
