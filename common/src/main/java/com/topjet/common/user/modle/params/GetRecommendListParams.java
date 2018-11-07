package com.topjet.common.user.modle.params;

/**
 * Created by yy on 2017/11/6.
 * 个人中心-我的推荐记录
 */

public class GetRecommendListParams {

    private String time_range;//	查询时间范围	是	String	0全部 1 今天 2近一周 3近一个月 4 近三个月
    private String status;//审核状态	是	String	0全部 1审核成功 2审核中 3审核失败 4无补贴
    private String page;//页数	是	String

    public GetRecommendListParams(String time_range, String status, String page) {
        this.time_range = time_range;
        this.status = status;
        this.page = page;
    }

    @Override
    public String toString() {
        return "GetRecommendListParams{" +
                "time_range='" + time_range + '\'' +
                ", status='" + status + '\'' +
                ", page='" + page + '\'' +
                '}';
    }
}
