package com.topjet.shipper.user.presenter;

import android.content.Context;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.topjet.common.base.listener.ObserverOnNextListener;
import com.topjet.common.base.listener.ObserverOnResultListener;
import com.topjet.common.base.presenter.BaseApiPresenter;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.order_detail.modle.extra.CityAndLocationExtra;
import com.topjet.common.resource.bean.AreaInfo;
import com.topjet.common.resource.bean.CityItem;
import com.topjet.common.resource.dict.AreaDataDict;
import com.topjet.common.user.modle.params.UsualContactListItem;
import com.topjet.common.user.modle.response.IdResponse;
import com.topjet.common.utils.ApplyUtils;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.StringUtils;
import com.topjet.common.utils.Toaster;
import com.topjet.shipper.R;
import com.topjet.shipper.user.model.bean.ContactsInputParams;
import com.topjet.shipper.user.model.bean.GetUserInfoByMobileParams;
import com.topjet.shipper.user.model.bean.GetUsualContactInfoParams;
import com.topjet.shipper.user.model.extra.AddContactExtra;
import com.topjet.shipper.user.model.serviceAPI.ContactCommand;
import com.topjet.shipper.user.view.activity.ContactsInputView;

import io.reactivex.functions.Consumer;

/**
 * 联系人添加或编辑
 * Created by tsj004 on 2017/8/28.
 */

public class ContactsInputPresenter extends BaseApiPresenter<ContactsInputView, ContactCommand> {
    private UsualContactListItem oldData;
    private String linkmanId;//联系人ID
    private boolean isAddOrEdit;
    private int resource;

    public ContactsInputPresenter(ContactsInputView mView, Context mContext, ContactCommand mApiCommand) {
        super(mView, mContext, mApiCommand);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AddContactExtra extra = (AddContactExtra) mActivity.getIntentExtra(AddContactExtra.getExtraName());

        if (extra != null) {
            isAddOrEdit = extra.isAddOrEdit();
            resource = extra.getResource();
            mView.setTitleText(isAddOrEdit);
            if (StringUtils.isNotBlank(extra.getLinkmanId())) {
                linkmanId = extra.getLinkmanId();
                selectLinkmanInfoById(linkmanId);
            }
        }
    }

    /**
     * 输入框输入内容后显示清除按扭
     * 点清除按扭，将清空输入框内容
     */
    public void setEditTextWatcher(EditText et, final ImageView ivDelete) {
        // 单个输入框变化监听
        RxTextView.textChanges(et)
                .compose(mActivity.<CharSequence>bindToLifecycle())
                .subscribe(new Consumer<CharSequence>() {
                    @Override
                    public void accept(CharSequence charSequence) throws Exception {
                        RxView.visibility(ivDelete).accept(charSequence.length() > 0);
                    }
                });
    }

    /**
     * 输入框输入手机号后显示清除按扭
     * 点清除按扭，将清空输入框内容
     */
    public void setPhoneWatcher(final EditText et, final ImageView ivDelete) {
        // 单个输入框变化监听
        RxTextView.textChanges(et)
                .compose(mActivity.<CharSequence>bindToLifecycle())
                .subscribe(new Consumer<CharSequence>() {
                    @Override
                    public void accept(CharSequence charSequence) throws Exception {
                        RxView.visibility(ivDelete).accept(charSequence.length() > 0);
                        if (charSequence.length() == 11) {
                            if (!ApplyUtils.validateMobile(et.getText().toString().trim())) {
                                mView.showToast("输入的手机号有误，请检查！");
                            } else {
                                //通过手机号获取常用联系人
                                if (isAddOrEdit) {
                                    selectLinkmanInfoByMobile(et.getText().toString().trim());
                                }
                            }
                        }
                    }
                });
    }

    /**
     * 监听密码输入是否合法，然后是否显示按扭是否可用
     *
     * @param et
     * @param et2
     * @param tvCity
     * @param tvConfirm
     */
    public void setTextViewWatcher(final EditText et, final EditText et2, final TextView tvCity, final TextView tvConfirm) {

        RxTextView.textChanges(tvCity)
                .compose(mActivity.<CharSequence>bindToLifecycle())
                .subscribe(new Consumer<CharSequence>() {
                    @Override
                    public void accept(CharSequence charSequence) throws Exception {
                        RxView.enabled(tvConfirm).accept(checkData(et.getText().toString().trim(),
                                et2.getText().toString().trim(),
                                tvCity.getText().toString().trim()));
                    }
                });
        RxTextView.textChanges(et)
                .compose(mActivity.<CharSequence>bindToLifecycle())
                .subscribe(new Consumer<CharSequence>() {
                    @Override
                    public void accept(CharSequence charSequence) throws Exception {
                        RxView.enabled(tvConfirm).accept(checkData(et.getText().toString().trim(),
                                et2.getText().toString().trim(),
                                tvCity.getText().toString().trim()));
                    }
                });
        RxTextView.textChanges(et2)
                .compose(mActivity.<CharSequence>bindToLifecycle())
                .subscribe(new Consumer<CharSequence>() {
                    @Override
                    public void accept(CharSequence charSequence) throws Exception {
                        RxView.enabled(tvConfirm).accept(checkData(et.getText().toString().trim(),
                                et2.getText().toString().trim(),
                                tvCity.getText().toString().trim()));
                    }
                });
    }

    /**
     * 检查输入数据的合法性
     *
     * @param phone
     * @param linkMan
     * @param city
     * @return
     */

    private boolean checkData(String phone, String linkMan, String city) {

        if (StringUtils.isBlank(phone)) {
            return false;
        } else {
            if (!ApplyUtils.validateMobile(phone)) {
                return false;
            }
        }

        if (StringUtils.isBlank(linkMan)) {
            return false;
        }

        return !StringUtils.isBlank(city);
    }

    /**
     * 检查所在地区输入
     *
     * @param city
     */
    public boolean checkCity(String city) {
        Logger.i("===city===", "" + city);
        if (StringUtils.isBlank(city)) {
            mView.showToast("请先选择所在地区");
            return true;
        }
        return false;
    }


    /**
     * 接收通过手机号获取的服务端返回数据
     *
     * @param data
     */
    private String userId;

    public void receiveLinkManByMobileData(UsualContactListItem data) {
        if (data == null) {
            userId = "";
            return;
        }
        if (StringUtils.isEmpty(data.getContacts_name())) {
            userId = "";
            return;
        }

        userId = data.getContacts_name_user_id();
        mView.showToast("该手机号是560平台用户：" + data.getContacts_name() + "，\n" + "系统已自动为您补全其他信息.......");
        setText(data);
    }


    /**
     * 请求服务端获取常用联系人信息
     *
     * @param linkman_id
     */
    public void selectLinkmanInfoById(String linkman_id) {
        if (StringUtils.isNotBlank(linkman_id)) {
            mApiCommand.selectLinkmanInfoById(new GetUsualContactInfoParams(linkman_id), new ObserverOnResultListener<UsualContactListItem>() {
                @Override
                public void onResult(UsualContactListItem usualContactListItem) {
                    receiveLinkmanInfoByIdData(usualContactListItem);
                }
            });
        }
    }

    /**
     * 接收通过联系人ID获取的服务端返回数据
     *
     * @param data
     */
    public void receiveLinkmanInfoByIdData(UsualContactListItem data) {
        if (data == null)
            return;

        oldData = data;
        setText(data);
    }

    /**
     * 回填页面信息方法
     *
     * @param data
     */
    private void setText(UsualContactListItem data) {
        if (data == null)
            return;

        if (StringUtils.isNotBlank(data.getContacts_name_user_id())) {
            userId = data.getContacts_name_user_id();
        }
        if (StringUtils.isNotBlank(data.getContacts_name())) {
            mView.setEditTextLinkman(data.getContacts_name());
        }

        if (StringUtils.isNotBlank(data.getContacts_mobile())) {
            mView.setEditTextPhone(data.getContacts_mobile());
        }
        if (StringUtils.isNotBlank(data.getContacts_address())) {
            mView.setTextViewAddress(data.getContacts_address());
        }
        if (StringUtils.isNotBlank(data.getContacts_city())) {
            mView.setTextViewCity(data.getContacts_city());
        } else if (StringUtils.isNotBlank(data.getContacts_city_code())) {
            CityItem item = AreaDataDict.getCityItemByAdcode(data.getContacts_city_code());
            if (item != null) {
                mView.setTextViewCity(item.getCityFullName());
            }
        }
        // 设置城市选择信息
        CityAndLocationExtra cityAndLocationExtra = new CityAndLocationExtra();
        AreaInfo areaInfo = AreaDataDict.getAreaInfoByAdCode(data.getContacts_city_code());
        if (areaInfo != null && areaInfo.getSecondLevel() != null) {
            Logger.i("oye", "areaInfo = " + areaInfo.toString());
            CityItem secoundCityItem = areaInfo.getSecondLevel();
            cityAndLocationExtra.setCityName(secoundCityItem.getCityFullName());
        }
        cityAndLocationExtra.setAdCode(data.getContacts_city_code());
        cityAndLocationExtra.setLatitude(data.getLatitude());
        cityAndLocationExtra.setLongitude(data.getLongitude());

        mView.setCityAndLocationExtra(cityAndLocationExtra);
    }

    /**
     * 确认提交数据
     *
     * @param phone
     * @param linkName
     * @param city
     * @param address
     * @param latitude
     * @param longitude
     */
    public void SubmitData(String phone, String linkName, String city, String address, String latitude, String longitude) {
        if (!checkData(phone, linkName, city)) {
            mView.showToast("请填写正确信息后再提交！");
            return;
        }
        if (CMemoryData.getUserMobile().equals(phone)) {
            Toaster.showShortToast(mContext.getString(R.string.self_contact));
            return;
        }

        ContactsInputParams params = new ContactsInputParams();
        params.setLinkman_id(linkmanId);
        params.setContacts_address(address);
        params.setContacts_city_code(city);
        params.setContacts_mobile(phone);
        params.setContacts_name(linkName);
        params.setLatitude(latitude);
        params.setLongitude(longitude);
        if (StringUtils.isNotBlank(userId)) {
            params.setContacts_name_user_id(userId);
        }
        if (!isAddOrEdit) {
            //编辑
            if (isUpdateData(oldData, params)) {
                //没有改过数据
                mActivity.finish();//关闭当前页面
            } else {
                //修改了数据
                updateLinkmanByid(params);
            }
        } else {
            //添加
            insertLinkman(params);
        }
    }

    /**
     * 新老数据是否相等
     *
     * @param oldData
     * @param newData
     * @return
     */
    private boolean isUpdateData(UsualContactListItem oldData, ContactsInputParams newData) {
        if (oldData != null && newData != null) {
            return oldData.getContacts_mobile().equals(newData.getContacts_mobile()) && oldData.getContacts_name()
                    .equals(newData.getContacts_name()) && oldData.getContacts_city_code().equals(newData
                    .getContacts_city_code()) && oldData.getContacts_address().equals(newData.getContacts_address());
        } else {
            return false;
        }
    }

    /**
     * 通过手机查找联系人
     *
     * @param mobile
     */
    private void selectLinkmanInfoByMobile(String mobile) {

        mApiCommand.selectLinkmanInfoByMobile(new GetUserInfoByMobileParams(mobile), new ObserverOnResultListener<UsualContactListItem>() {
            @Override
            public void onResult(UsualContactListItem usualContactListItem) {
                receiveLinkManByMobileData(usualContactListItem);
            }
        });
    }

    /**
     * 修改常用联系人
     *
     * @param params
     */
    private void updateLinkmanByid(ContactsInputParams params) {
        mApiCommand.updateLinkmanByid(params, new ObserverOnResultListener<IdResponse>() {
            @Override
            public void onResult(IdResponse idResponse) {
                mView.showToast("修改成功");
                mView.finishPage();
            }
        });
    }

    /**
     * 添加常用联系人
     *
     * @param params
     */
    private void insertLinkman(ContactsInputParams params) {
        mApiCommand.insertLinkman(params, new ObserverOnNextListener<IdResponse>() {
            @Override
            public void onNext(IdResponse idResponse) {
                mView.showToast("添加成功");
                mView.finishPage();
            }

            @Override
            public void onError(String errorCode, String msg) {
                Toaster.showLongToast(msg);
            }
        });
    }
}
