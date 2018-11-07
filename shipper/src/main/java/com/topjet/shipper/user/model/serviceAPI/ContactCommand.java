package com.topjet.shipper.user.model.serviceAPI;

import com.topjet.common.base.listener.ObserverOnNextListener;
import com.topjet.common.base.listener.ObserverOnResultListener;
import com.topjet.common.base.net.serverAPI.BaseCommand;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.shipper.user.model.bean.ContactsInputParams;
import com.topjet.common.user.modle.response.DeleteLinkmanResponse;
import com.topjet.shipper.user.model.bean.DeteleContactParams;
import com.topjet.common.user.modle.response.IdResponse;
import com.topjet.shipper.user.model.bean.GetUsualContactInfoParams;
import com.topjet.common.user.modle.params.GetUsualContactListParams;
import com.topjet.common.user.modle.params.UsualContactListItem;
import com.topjet.shipper.user.model.bean.GetUserInfoByMobileParams;
import com.topjet.common.user.modle.params.PageNumParams;


/**
 * 用户联系人相关
 * Created by tsj004 on 2017/8/24.
 */

public class ContactCommand extends BaseCommand<ContactCommandAPI>{

    public ContactCommand(Class<ContactCommandAPI> c, MvpActivity mActivity) {
        super(c, mActivity);
    }


    /**
     * 查看常用联系人列表
     */
    public void selectLinkmanList(PageNumParams params,
                                  ObserverOnNextListener<GetUsualContactListParams> listener) {
        mCommonParams = getParams(ContactCommandAPI.SELECT_LINKMAN_LIST, params);
        handleOnNextObserver(mApiService.selectLinkmanList(mCommonParams), listener, false);
    }

    /**
     * 查看常用联系人列表
     */
    public void selectLinkmanList(PageNumParams params,
                                  ObserverOnResultListener<GetUsualContactListParams> listener) {
        mCommonParams = getParams(ContactCommandAPI.SELECT_LINKMAN_LIST, params);
        handleOnResultObserver(mApiService.selectLinkmanList(mCommonParams), listener);
    }

    /**
     * 删除常用联系人
     */
    public void deleteLinkmanByid(DeteleContactParams params,
                                  ObserverOnResultListener<DeleteLinkmanResponse> listener) {

        mCommonParams = getParams(ContactCommandAPI.DELETE_LINKMAN_BY_ID, params);
        handleOnResultObserver(mApiService.deleteLinkmanByid(mCommonParams), listener);


    }

    /**
     * 添加常用联系人
     */
    public void insertLinkman(ContactsInputParams params,
                              ObserverOnNextListener<IdResponse> listener) {
        mCommonParams = getParams(ContactCommandAPI.INSERT_LINKMAN, params);
        handleOnNextObserver(mApiService.insertLinkman(mCommonParams), listener);

    }

    /**
     * 编辑常用联系人
     */
    public void updateLinkmanByid(ContactsInputParams params,
                                  ObserverOnResultListener<IdResponse> listener) {
        mCommonParams = getParams(ContactCommandAPI.UPDATE_LINKMAN_BY_ID, params);
        handleOnResultObserver(mApiService.updateLinkmanByid(mCommonParams), listener);


    }

    /**
     * 获取常用联系人信息
     */
    public void selectLinkmanInfoById(GetUsualContactInfoParams params,
                                      ObserverOnResultListener<UsualContactListItem> listener) {

        mCommonParams = getParams(ContactCommandAPI.SELECT_LINKMAN_INFO_BY_ID, params);
        handleOnResultObserver(mApiService.selectLinkmanInfoById(mCommonParams), listener);


    }

    /**
     * 通过手机号获取常用联系人信息
     */
    public void selectLinkmanInfoByMobile(GetUserInfoByMobileParams params,
                                          ObserverOnResultListener<UsualContactListItem> listener) {

        mCommonParams = getParams(ContactCommandAPI.SELECT_LINKMAN_INFO_BY_MOBILE, params);
        handleOnResultObserver(mApiService.selectLinkmanInfoByMobile(mCommonParams), listener, false);

    }

}
