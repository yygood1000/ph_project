package com.topjet.common.common.presenter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.topjet.common.base.CommonProvider;
import com.topjet.common.base.listener.ObserverOnNextListener;
import com.topjet.common.base.presenter.BaseApiPresenter;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.common.modle.params.PhoneParams;
import com.topjet.common.common.modle.response.UserInfoResponse;
import com.topjet.common.common.modle.serverAPI.IntegrityInquiryCommand;
import com.topjet.common.common.modle.serverAPI.IntegrityInquiryCommandAPI;
import com.topjet.common.common.modle.serverAPI.UserInfoCommand;
import com.topjet.common.common.modle.serverAPI.UserInfoCommandAPI;
import com.topjet.common.common.view.activity.IntegrityInquiryView;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.contact.model.ContactBean;
import com.topjet.common.contact.model.ContactModel;
import com.topjet.common.db.DBManager;
import com.topjet.common.db.bean.PhoneNumberBean;
import com.topjet.common.db.greendao.PhoneNumberBeanDao;
import com.topjet.common.utils.RxHelper;
import com.topjet.common.utils.Toaster;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * creator: zhulunjun
 * time:    2017/11/9
 * describe: 诚信查询
 */

public class IntegrityInquiryPresenter extends BaseApiPresenter<IntegrityInquiryView, IntegrityInquiryCommand> {
    private ContactModel mContactModel;

    public IntegrityInquiryPresenter(IntegrityInquiryView mView, Context mContext) {
        super(mView, mContext, new IntegrityInquiryCommand(IntegrityInquiryCommandAPI.class, (MvpActivity) mContext));
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
                        getContacts();
                        mDisposable.dispose();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mDisposable.dispose();
                    }
                });
    }

    @Override
    public void onCreate() {
        super.onCreate();
        getContactPermissions();
    }

    /**
     * 获取联系人
     */
    public List<ContactBean> getContacts() {
        return mContactModel.getPhoneContacts(mContext);
    }

    /**
     * 筛选
     */
    public List<ContactBean> filterData(String str) {
        // 未读取联系人时，输入数字联想出历史记录
        // 已读取联系人时，输入数字联想联系人列表
        List<ContactBean> datas = mContactModel.filterData(str);
        if(datas.size() == 0){
            datas = getHistoryData();
        }
        return datas;
    }

    /**
     * 获取系统联系人
     * @return 手机号
     */
    public String getContactFormSystem(Intent intent){
        return mContactModel.getContactFormSystem(mContext, intent);
    }


    /**
     * 查询
     * @param mobile 手机
     */
    public void query(String mobile){
        if(CMemoryData.isDriver()) {
            queryShipper(mobile);
        }else {
            queryDriver(mobile);
        }
    }

    /**
     * 查询司机
     */
    public void queryDriver(final String mobile){
        new UserInfoCommand(UserInfoCommandAPI.class, mActivity).queryDriverInfo(new PhoneParams(mobile), new ObserverOnNextListener<UserInfoResponse>() {
            @Override
            public void onNext(UserInfoResponse userInfoResponse) {
                if(userInfoResponse != null) {
                    CommonProvider.getInstance().getJumpShipperProvider().jumpUserInfo(mActivity,
                            userInfoResponse.getQuery_integrity_user_info());
                }
            }

            @Override
            public void onError(String errorCode, String msg) {
                Toaster.showLongToast(msg);
                mView.clear();
            }
        });
    }

    /**
     * 查询货主
     */
    public void queryShipper(final String mobile){
        new UserInfoCommand(UserInfoCommandAPI.class, mActivity).queryShipperInfo(new PhoneParams(mobile), new ObserverOnNextListener<UserInfoResponse>() {
            @Override
            public void onNext(UserInfoResponse userInfoResponse) {
                if(userInfoResponse != null) {
                    CommonProvider.getInstance().getJumpDriverProvider().jumpUserInfo(mActivity, userInfoResponse.getQuery_integrity_user_info());
                }
            }

            @Override
            public void onError(String errorCode, String msg) {
                Toaster.showLongToast(msg);
                mView.clear();
            }
        });
    }

    /**
     * 获取历史列表
     * @return
     */
    public List<ContactBean> getHistoryData(){
        List<PhoneNumberBean> phoneNumberBeans = DBManager
                .getInstance()
                .getDaoSession()
                .getPhoneNumberBeanDao()
                .loadAll();
        List<ContactBean> datas = new ArrayList<>();
        for (PhoneNumberBean bean : phoneNumberBeans){
            ContactBean contactBean = new ContactBean("");
            contactBean.setMobile(bean.getPhoneNumber());
            datas.add(contactBean);
        }
        return datas;
    }
    /**
     * 将查询保存到历史纪录
     */
    public void saveContact(String mobile){
        if(queryHistory(mobile).size() == 0) {
            DBManager
                    .getInstance()
                    .getDaoSession()
                    .getPhoneNumberBeanDao()
                    .save(new PhoneNumberBean(mobile));
        }
    }

    /**
     * 查询历史记录中是否有了
     */
    public List queryHistory(String mobile) {
        QueryBuilder qb = DBManager
                .getInstance()
                .getDaoSession()
                .getPhoneNumberBeanDao()
                .queryBuilder();
        return qb.where(PhoneNumberBeanDao.Properties.PhoneNumber.eq(mobile)).list();
    }
}
