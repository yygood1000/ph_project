package com.topjet.common.user.view.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.topjet.common.R;
import com.topjet.common.user.modle.bean.ReferrerInfoBean;
import com.topjet.common.utils.StringUtils;

/**
 * Created by  tsj-004 on 2017/10/23.
 * <p>
 * 我的推荐记录 适配器
 */

public class RecommendLogAdapter extends BaseQuickAdapter<ReferrerInfoBean, BaseViewHolder> {

    public RecommendLogAdapter() {
        super(R.layout.listitem_recommend_log);
    }

    @Override
    protected void convert(BaseViewHolder helper, ReferrerInfoBean item) {
        helper.setText(R.id.tv_name, item.getRecommend_name());
        helper.setText(R.id.tv_status_result, item.getRecommend_status() + item.getRecommend_result());

        helper.setVisible(R.id.tv_remark, StringUtils.isNotBlank(item.getRecommend_remark()));
        helper.setText(R.id.tv_remark, item.getRecommend_remark());
        helper.setText(R.id.tv_time, item.getRecommend_time());
    }
}