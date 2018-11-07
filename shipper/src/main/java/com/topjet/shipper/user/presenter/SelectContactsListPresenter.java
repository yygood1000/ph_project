package com.topjet.shipper.user.presenter;

import android.content.Context;

import com.topjet.common.base.listener.ObserverOnNextListener;
import com.topjet.common.base.presenter.BaseApiPresenter;
import com.topjet.common.base.view.activity.IListView;
import com.topjet.common.user.modle.params.GetUsualContactListParams;
import com.topjet.common.user.modle.params.PageNumParams;
import com.topjet.shipper.user.model.serviceAPI.ContactCommand;

/**
 * 选择常用联系人
 * Created by tsj004 on 2017/8/25.
 */

public class SelectContactsListPresenter extends BaseApiPresenter<IListView, ContactCommand> {


    public SelectContactsListPresenter(IListView mView, Context mContext, ContactCommand mApiCommand) {
        super(mView, mContext, mApiCommand);
    }


    /**
     * 获取联系人列表
     */
    public void getLinkmanList(int page) {
        PageNumParams params = new PageNumParams(page + "");
        mApiCommand.selectLinkmanList(params, new ObserverOnNextListener<GetUsualContactListParams>() {
            @Override
            public void onNext(GetUsualContactListParams getUsualContactListParams) {
                if (getUsualContactListParams != null) {
                    mView.loadSuccess(getUsualContactListParams.getSelectLinkmanListDTOs());
                }
            }

            @Override
            public void onError(String errorCode, String msg) {
                mView.loadFail(msg);
            }
        });
    }

}
