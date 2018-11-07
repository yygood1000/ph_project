package com.topjet.crediblenumber.user.presenter;

import android.content.Context;

import com.topjet.common.base.listener.ObserverOnNextListener;
import com.topjet.common.base.listener.ObserverOnResultListener;
import com.topjet.common.base.net.request.CommonParams;
import com.topjet.common.base.presenter.BaseApiPresenter;
import com.topjet.common.common.modle.bean.GoodsListData;
import com.topjet.common.utils.Logger;
import com.topjet.crediblenumber.user.modle.bean.CallLogData;
import com.topjet.crediblenumber.user.modle.params.GetCallRecordsParams;
import com.topjet.crediblenumber.user.modle.response.GetCollLogListResponse;
import com.topjet.crediblenumber.user.modle.serverAPI.DriverCommand;
import com.topjet.crediblenumber.user.modle.serverAPI.DriverCommandAPI;
import com.topjet.crediblenumber.user.view.activity.CallLogView;

/**
 * Created by yy on 2017/10/16.
 * 通话记录
 */

public class CallLogPresenter extends BaseApiPresenter<CallLogView<CallLogData>, DriverCommand> {
    public CallLogPresenter(CallLogView<CallLogData> mView, Context mContext) {
        super(mView, mContext);
        mApiCommand = new DriverCommand(DriverCommandAPI.class, mActivity);
    }

    /**
     * 获取通讯记录
     */
    public void getCallPhoneInfo(int page) {
        GetCallRecordsParams params = new GetCallRecordsParams(page + "");
        mApiCommand.getCallPhoneInfo(params, new ObserverOnNextListener<GetCollLogListResponse>() {
            @Override
            public void onNext(GetCollLogListResponse response) {
                mView.loadSuccess(response.getList());
            }

            @Override
            public void onError(String errorCode, String msg) {
                Logger.i("oye", "qingqiu buchuzai ");
                mView.loadFail(msg);
            }
        });
    }

    /**
     * 清空通话记录
     */
    public void clearCallPhoneInfo() {
        mApiCommand.clearCallPhoneInfo(new CommonParams(), new ObserverOnResultListener<Object>() {
            @Override
            public void onResult(Object o) {
                mView.refresh();
            }
        });
    }


}
