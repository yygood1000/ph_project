package com.topjet.common.contact;

import android.Manifest;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Build;
import android.support.v4.content.ContextCompat;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.topjet.common.base.CommonProvider;
import com.topjet.common.base.dialog.AutoDialogHelper;
import com.topjet.common.base.listener.ObserverOnResultListener;
import com.topjet.common.base.presenter.BasePresenter;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.ShareHelper;
import com.topjet.common.common.modle.response.ShareResponse;
import com.topjet.common.common.modle.serverAPI.SystemCommand;
import com.topjet.common.common.modle.serverAPI.SystemCommandAPI;
import com.topjet.common.contact.model.ContactBean;
import com.topjet.common.contact.model.ContactModel;
import com.topjet.common.contact.model.InviteTruckResponse;
import com.topjet.common.contact.view.ContactView;
import com.topjet.common.utils.RxHelper;
import com.topjet.common.utils.StringUtils;
import com.topjet.common.utils.Toaster;

import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * creator: zhulunjun
 * time:    2017/10/17
 * describe: 联系人控制
 */

public class ContactPresenter extends BasePresenter<ContactView> {
    private ContactModel mContactModel;

    public ContactPresenter(ContactView mView, Context mContext) {
        super(mView, mContext);
        mContactModel = new ContactModel();
    }


    /**
     * 获取联系人权限
     */
    Disposable mDisposable;
    public void getContactPermissions() {
        RxPermissions rxPermissions = new RxPermissions(mActivity);
        rxPermissions.request(Manifest.permission.READ_CONTACTS)
                .compose(RxHelper.<Boolean>rxSchedulerHelper())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        mDisposable = disposable;
                    }
                })
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            mView.setContactData(getContacts());
                        } else {
                            mView.emptyData();
                        }
                        mDisposable.dispose();
                }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toaster.showLongToast(throwable.getMessage());
                        mDisposable.dispose();
                    }
                });
    }


    /**
     * 获取联系人
     *
     * @return
     */
    public List<ContactBean> getContacts() {
        return mContactModel.getPhoneContacts(mContext);
    }

    /**
     * 邀请熟车
     */
    public void invitation(String mobile) {

        CommonProvider.getInstance()
                .getFamiliarCarProvider()
                .inviteTruck(mActivity, mobile, new ObserverOnResultListener<InviteTruckResponse>() {
                    @Override
                    public void onResult(InviteTruckResponse inviteTruckResponse) {
                        if (inviteTruckResponse != null &&
                                StringUtils.isNotBlank(inviteTruckResponse.getIs_top_user()) &&
                                inviteTruckResponse.getIs_top_user().equals(InviteTruckResponse.NO_USER)) {
                            // 弹框提示
                            AutoDialogHelper
                                    .showContentOneBtn(mContext, "此手机号不是平台用户，已发起邀请。对方注册成功后会自动加入到您的熟车列表")
                                    .show();
                        } else {
                            Toaster.showShortToast(inviteTruckResponse.getMsg_info());
                        }
                    }
                });

    }

    /**
     * 筛选
     *
     * @param str
     * @return
     */
    public List<ContactBean> filterData(String str) {
        return mContactModel.filterData(str);
    }

    /**
     * 短信分享
     */
    public void shareSms(final String mobile) {
        new SystemCommand(mActivity).shareAppOfSms(new ObserverOnResultListener<ShareResponse>() {
            @Override
            public void onResult(ShareResponse shareResponse) {
                if (shareResponse != null && StringUtils.isNotBlank(shareResponse.getSms_content())) {
                    ShareHelper.sendSmsWithBody(mActivity, mobile, shareResponse.getSms_content());
                }
            }
        });
    }
}
