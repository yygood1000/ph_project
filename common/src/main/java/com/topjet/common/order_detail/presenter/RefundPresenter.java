package com.topjet.common.order_detail.presenter;

import android.content.Context;

import com.topjet.common.base.presenter.BasePresenter;
import com.topjet.common.order_detail.modle.bean.H5RefundInfo;
import com.topjet.common.order_detail.view.activity.RefundView;
import com.topjet.common.utils.DataFormatFactory;
import com.topjet.common.utils.Logger;

/**
 * Created by yy on 2017/10/12.
 * 退款相关页面
 */

public class RefundPresenter extends BasePresenter<RefundView> {
    public RefundPresenter(RefundView mView, Context mContext) {
        super(mView, mContext);
    }

    public H5RefundInfo refundInfo;

    @Override
    public void onCreate() {
        super.onCreate();
        refundInfo = (H5RefundInfo) mActivity.getIntentExtra(H5RefundInfo.getExtraName());
        if (refundInfo == null) {
            Logger.i("oye", "refundInfo == null");
        } else {
            Logger.i("oye", DataFormatFactory.toJson(refundInfo));
        }
    }
}
