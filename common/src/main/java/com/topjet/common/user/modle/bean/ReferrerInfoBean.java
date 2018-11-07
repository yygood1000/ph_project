package com.topjet.common.user.modle.bean;

import com.topjet.common.utils.StringUtils;
import com.topjet.common.utils.TimeUtils;

/**
 * Created by yy on 2017/11/6.
 * <p>
 * 推荐相关
 */

public class ReferrerInfoBean {
    private String recommend_name;//被邀请人姓名	是	string
    private String recommended_mobile;//被邀请人手机号	是	string
    private String recommend_status;//审核状态	是	string
    private String recommend_result;//审核结果	是	string
    private String recommend_remark;//审核备注	是	string
    private String recommend_time;//邀请时间	是	string

    // 我的推荐人 相关
    private String recommend_icon_url;//推荐人头像	是	string
    private String recommend_icon_key;//被邀请人姓名	是	string

    public String getRecommend_name() {
        return recommend_name;
    }

    public String getRecommended_mobile() {
        return recommended_mobile;
    }

    public String getRecommend_status() {
        return StringUtils.isNotBlank(recommend_status) ? "状态： " + recommend_status : "";
    }

    public String getRecommend_result() {
        return StringUtils.isNotBlank(recommend_result) ? "        结果： " + recommend_result : "";
    }

    public String getRecommend_remark() {
        return StringUtils.isNotBlank(recommend_remark) ? "        原因： " + recommend_remark : "";
    }

    public String getRecommend_time() {
        return StringUtils.isNotBlank(recommend_time) ? TimeUtils.getFormatDate(Long.parseLong(recommend_time),
                "yyyy-MM-dd") : "";
    }

    public String getRecommend_icon_url() {
        return recommend_icon_url;
    }

    public String getRecommend_icon_key() {
        return recommend_icon_key;
    }

    @Override
    public String toString() {
        return "ReferrerInfoBean{" +
                "recommend_name='" + recommend_name + '\'' +
                ", recommended_mobile='" + recommended_mobile + '\'' +
                ", recommend_status='" + recommend_status + '\'' +
                ", recommend_result='" + recommend_result + '\'' +
                ", recommend_remark='" + recommend_remark + '\'' +
                ", recommend_time='" + recommend_time + '\'' +
                ", recommend_icon_url='" + recommend_icon_url + '\'' +
                ", recommend_icon_key='" + recommend_icon_key + '\'' +
                '}';
    }
}
