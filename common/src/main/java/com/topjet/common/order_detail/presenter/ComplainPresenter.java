package com.topjet.common.order_detail.presenter;

import android.content.Context;

import com.topjet.common.base.presenter.BasePresenter;
import com.topjet.common.base.view.activity.IView;
import com.topjet.common.config.Config;
import com.topjet.common.order_detail.modle.extra.ComplainExtra;

/**
 * Created by yy on 2017/10/12.
 * 我要投诉
 */

public class ComplainPresenter extends BasePresenter<IView> {
    public ComplainExtra extra;

    public ComplainPresenter(Context mContext) {
        super(mContext);
    }

    @Override
    public void onCreate() {
        extra = (ComplainExtra) mActivity.getIntentExtra(ComplainExtra.getExtraName());
    }

    /**
     * 获取提交评论页面Url
     */
    public String getComplainUrl() {
        return Config.getComplaintUrl() +
                "?order_id=" + extra.getOrderId() +
                "&version=" + extra.getVersion();
    }

}
