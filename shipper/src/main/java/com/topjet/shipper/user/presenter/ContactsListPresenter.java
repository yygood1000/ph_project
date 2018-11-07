package com.topjet.shipper.user.presenter;

import android.content.Context;

import com.topjet.common.base.listener.ObserverOnNextListener;
import com.topjet.common.base.listener.ObserverOnResultListener;
import com.topjet.common.base.presenter.BaseApiPresenter;
import com.topjet.common.user.modle.params.GetUsualContactListParams;
import com.topjet.common.user.modle.params.PageNumParams;
import com.topjet.common.user.modle.params.UsualContactListItem;
import com.topjet.common.user.modle.response.DeleteLinkmanResponse;
import com.topjet.shipper.user.model.bean.DeteleContactParams;
import com.topjet.shipper.user.model.serviceAPI.ContactCommand;
import com.topjet.shipper.user.view.activity.ContactsListView;

/**
 * 常用联系人列表
 * Created by tsj004 on 2017/8/22.
 */

public class ContactsListPresenter extends BaseApiPresenter<ContactsListView, ContactCommand> {

    public ContactsListPresenter(ContactsListView mView, Context mContext, ContactCommand mApiCommand) {
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

    /**
     * 删除联系人
     *
     * @param item
     */
    public void deleteLinkmanByid(final UsualContactListItem item, final int position) {
        DeteleContactParams params = new DeteleContactParams();
        params.setLinkman_id(item.getLinkman_id());
        mApiCommand.deleteLinkmanByid(params, new ObserverOnResultListener<DeleteLinkmanResponse>() {
            @Override
            public void onResult(DeleteLinkmanResponse deleteLinkmanResponse) {
                mView.showToast("删除成功");
                mView.deleteItem(item, position);
            }
        });
    }
}
