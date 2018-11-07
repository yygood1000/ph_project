package com.topjet.shipper.user.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.topjet.common.base.busManger.Subscribe;
import com.topjet.common.base.controller.AreaDataMachiningController;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.resource.bean.AreaInfo;
import com.topjet.common.common.modle.event.AreaSelectedData;
import com.topjet.common.common.view.dialog.CitySelectPopupWindow;
import com.topjet.common.order_detail.modle.extra.CityAndLocationExtra;
import com.topjet.common.user.modle.event.AddLinkManEvent;
import com.topjet.common.user.modle.event.GetLinkManByMobileEvent;
import com.topjet.common.user.modle.event.GetLinkManEvent;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.StringUtils;
import com.topjet.common.widget.MyTitleBar;
import com.topjet.shipper.R;
import com.topjet.shipper.deliver.view.activity.SelectAddressMapActivity;
import com.topjet.shipper.user.model.serviceAPI.ContactCommand;
import com.topjet.shipper.user.model.serviceAPI.ContactCommandAPI;
import com.topjet.shipper.user.presenter.ContactsInputPresenter;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 联系人添加或编辑
 * Created by tsj004 on 2017/8/28.
 */

public class ContactsInputActivity extends MvpActivity<ContactsInputPresenter> implements ContactsInputView {
    private MyTitleBar myTitleBar;
    private CitySelectPopupWindow citySelectPopupWindow = new CitySelectPopupWindow(this.getClass().getName());

    @BindView(R.id.iv_delete_linkman)
    ImageView ivDeleteLinkman;
    @BindView(R.id.iv_delete_phone)
    ImageView ivDeletePhone;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_linkman)
    EditText etLinkman;
    @BindView(R.id.et_city)
    TextView etCity;
    @BindView(R.id.et_address)
    TextView etAddress;
    @BindView(R.id.tv_confirm)
    TextView tvConfirm;

    /**
     * 点击了详细地址输入框
     */
    private final static int requestCode_toMap = 100;   // 跳转地图code
    private CityAndLocationExtra cityAndLocationExtra = null;

    /**
     * 添加的时候用到这个对象
     *
     * @param object
     */
    AreaSelectedData cityData;

    @Override
    public void initTitle() {
        myTitleBar = getMyTitleBar().setMode(MyTitleBar.Mode.BACK_TITLE);
        myTitleBar.setTitleText(R.string.contacts_add);
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ContactsInputPresenter(this, mContext, new ContactCommand(ContactCommandAPI.class, this));
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_contacts_input;
    }

    @Override
    protected void initView() {
        mPresenter.setPhoneWatcher(etPhone, ivDeletePhone);
        mPresenter.setEditTextWatcher(etLinkman, ivDeleteLinkman);
        mPresenter.setTextViewWatcher(etPhone, etLinkman, etCity, tvConfirm);
    }

    /**
     * 确定按扭事件
     */
    @OnClick(R.id.tv_confirm)
    public void clickConfirm() {
        String latitude, longitude, cityId;
        if (cityAndLocationExtra != null && StringUtils.isNotBlank(cityAndLocationExtra.getLatitude()) &&
                StringUtils.isNotBlank(cityAndLocationExtra.getLongitude())) {
            latitude = cityAndLocationExtra.getLatitude();
            longitude = cityAndLocationExtra.getLongitude();
            cityId = cityAndLocationExtra.getAdCode();
        } else {
            latitude = cityData.getAreaInfo().getLastCityItem().getLatitude();
            longitude = cityData.getAreaInfo().getLastCityItem().getLongitude();
            cityId = cityData.getAreaInfo().getLastCityId();
        }
        mPresenter.SubmitData(
                etPhone.getText().toString().trim(),
                etLinkman.getText().toString().trim(),
                cityId,
                etAddress.getText().toString().trim(),
                latitude,
                longitude);
    }

    /**
     * 弹出地图选择地区
     */
    @OnClick(R.id.et_city)
    public void showMapGetCity() {
        citySelectPopupWindow.showCitySelectPopupWindow(this, etCity, true, true, true);
    }





    @OnClick(R.id.et_address)
    public void clickAddress() {
        if (!mPresenter.checkCity(etCity.getText().toString())) {
            if (cityData != null) {
                cityAndLocationExtra = AreaDataMachiningController.machining(cityData);  // 处理城市选择器数据工具类
            }
            turnToActivityForResult(SelectAddressMapActivity.class, requestCode_toMap, cityAndLocationExtra);
        }
    }


    @OnClick(R.id.iv_delete_phone)
    public void ivDeletePhone() {
        etPhone.setText("");
    }

    @OnClick(R.id.iv_delete_linkman)
    public void ivDeleteLinkman() {
        etLinkman.setText("");
    }



    /**
     * 手动新增目的地返回事件
     * 城市选择回调
     */
    @Subscribe
    public void onEvent(AreaSelectedData cityData) {
        if (cityAndLocationExtra != null){
            if (!cityAndLocationExtra.getAdCode().equals(cityData.getAreaInfo().getLastCityId())){
                //选择的城市和之前城市比较，如果不一样则清空详细地址
                setTextViewAddress("");
            }
        }
        this.cityData = cityData;
        AreaInfo areaInfo = cityData.getAreaInfo();
        etCity.setText(areaInfo.getFullCityName());

        if (cityAndLocationExtra != null) {
            cityAndLocationExtra.setAdCode(cityData.getAreaInfo().getLastCityId());
            cityAndLocationExtra.setLatitude(cityData.getAreaInfo().getLastCityItem().getLatitude());
            cityAndLocationExtra.setLongitude(cityData.getAreaInfo().getLastCityItem().getLongitude());
        }
    }


    @Override
    public void setEditTextPhone(CharSequence text) {
        etPhone.setText(text);
    }

    @Override
    public void setEditTextLinkman(CharSequence text) {
        etLinkman.setText(text);
    }

    @Override
    public void setTextViewCity(CharSequence text) {
        etCity.setText(text);
    }

    @Override
    public void setTextViewAddress(CharSequence text) {
        etAddress.setText(text);
    }

    @Override
    public void setTitleText(boolean isAdd) {
        if (isAdd) {
            myTitleBar.setTitleText(R.string.contacts_add);
        } else {
            myTitleBar.setTitleText(R.string.contacts_edit);

        }
    }

    @Override
    public void setCityAndLocationExtra(CityAndLocationExtra cityData) {
        this.cityAndLocationExtra = cityData;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode_toMap == requestCode) {
                // 详细地址回调
                cityAndLocationExtra = getIntentExtra(data, CityAndLocationExtra.getExtraName());
                // 覆盖城市，避免详细地址超出之前选择的区域
                if(StringUtils.isNotBlank(cityAndLocationExtra.getBackwards2Name())){
                    etCity.setText(cityAndLocationExtra.getBackwards2Name());
                }
                String address = cityAndLocationExtra.getAddress();
                if (!StringUtils.isEmpty(address)) {
                    etAddress.setText(address);
                }
            }
        }
    }
}
