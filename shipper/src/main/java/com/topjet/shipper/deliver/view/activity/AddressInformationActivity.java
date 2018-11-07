package com.topjet.shipper.deliver.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.topjet.common.base.busManger.Subscribe;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.common.modle.event.AreaSelectedData;
import com.topjet.common.common.view.dialog.CitySelectPopupWindow;
import com.topjet.common.order_detail.modle.extra.CityAndLocationExtra;
import com.topjet.common.resource.bean.CityItem;
import com.topjet.common.resource.dict.AreaDataDict;
import com.topjet.common.user.modle.params.UsualContactListItem;
import com.topjet.common.utils.ApplyUtils;
import com.topjet.common.utils.CheckUserStatusUtils;
import com.topjet.common.utils.LocationUtils;
import com.topjet.common.utils.StringUtils;
import com.topjet.common.utils.Toaster;
import com.topjet.common.widget.MyTitleBar;
import com.topjet.shipper.R;
import com.topjet.shipper.deliver.modle.dirtyDataProcess.DeliverGoodsDirtyDataProcess;
import com.topjet.shipper.deliver.presenter.AddressInformationPresenter;
import com.topjet.shipper.deliver.view.adapter.SelectContactsListAdapter;
import com.topjet.shipper.user.model.extra.AddContactExtra;
import com.topjet.shipper.user.view.activity.ContactsInputActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

import static com.topjet.common.resource.dict.AreaDataDict.getCityItemByAdcode;
import static com.topjet.shipper.R.id.et_name;
import static com.topjet.shipper.R.id.et_phone;

/**
 * 发货--发货信息页
 * 发货--收货信息页
 * 货主版
 */
public class AddressInformationActivity extends MvpActivity<AddressInformationPresenter> implements
        AddressInformationView, LocationUtils.OnLocationListener {
    @BindView(R.id.tv_location)
    TextView tvLocation;
    @BindView(R.id.tv_location_detailed)
    TextView tvLocationDetailed;
    @BindView(R.id.ll_do_location)
    LinearLayout llDoLocation;
    @BindView(et_name)
    EditText etName;
    @BindView(et_phone)
    EditText etPhone;
    @BindView(R.id.tv_confirm)
    TextView tvConfirm;
    @BindView(R.id.empty_contacts)
    View emptyContacts;
    @BindView(R.id.rv_contacts)
    RecyclerView rvContacts;
    private SelectContactsListAdapter selectContactsListAdapter = null;       // 联系人适配器

    private final static int requestCode_toMap = 100;   // 跳转地图code

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_address_information;
    }

    @Override
    protected void initTitle() {
        getMyTitleBar().setMode(MyTitleBar.Mode.BACK_TITLE).setTitleText(R.string.input_deliver_info);
    }

    @Override
    protected void initPresenter() {
        mPresenter = new AddressInformationPresenter(this, mContext);
    }

    @Override
    public void initView() {
        rvContacts.setLayoutManager(new LinearLayoutManager(this));

        CityAndLocationExtra cityAndLocationExtra = getIntentExtra(CityAndLocationExtra.getExtraName());
        if ("deliver".equalsIgnoreCase(cityAndLocationExtra.getPageTitle())) {
            getMyTitleBar().setMode(MyTitleBar.Mode.BACK_TITLE).setTitleText(R.string.input_deliver_info);
            tvLocation.setHint(R.string.please_input_deliver_address);
            etName.setHint(R.string.contacts_input_hint_username);
            etPhone.setHint(R.string.please_input_deliver_phone);
        } else {
            getMyTitleBar().setMode(MyTitleBar.Mode.BACK_TITLE).setTitleText(R.string.input_receipt_info);
        }
        mPresenter.setCityAndLocationExtra(cityAndLocationExtra);
        if (cityAndLocationExtra != null) {
            if (cityAndLocationExtra.getBackwards2Name() != null) {
                tvLocation.setText(cityAndLocationExtra.getBackwards2Name());
            }
            if (cityAndLocationExtra.getAddress() != null) {
                tvLocationDetailed.setText(cityAndLocationExtra.getAddress());
            }
            if (cityAndLocationExtra.getPhone() != null) {
                etPhone.setText(cityAndLocationExtra.getPhone());
            }
            if (cityAndLocationExtra.getName() != null) {
                etName.setText(cityAndLocationExtra.getName());
            }
        }

        selectContactsListAdapter = new SelectContactsListAdapter(mContext, mPresenter.getUsualContactListItems());
        selectContactsListAdapter.setSelectContactClick(new SelectContactsListAdapter.SelectContactClick() {
            @Override
            public void selectClick(UsualContactListItem item) {
                setContact2Activity(item);
            }
        });
        rvContacts.setAdapter(selectContactsListAdapter);
        mPresenter.requestNetBackContact();     // 请求网络，获取常用联系人

        setAddressPhoneTextChangeListener(etPhone);
        setAddressPhoneTextChangeListener(tvLocation);
    }

    /**
     * 设置地址和电话的文字变化监听
     */
    public void setAddressPhoneTextChangeListener(final TextView textView) {
        RxTextView.textChanges(textView).compose(this.<CharSequence>bindToLifecycle())
                .subscribe(new Consumer<CharSequence>() {
                    @Override
                    public void accept(CharSequence charSequence) throws Exception {
                        String str = charSequence.toString();
                        if (!TextUtils.isEmpty(str.trim())) {
                            setSubmitAble();    // 设置确定按钮是否可点击
                        }
                    }
                });
    }

    /**
     * 设置确定按钮是否可点击
     */
    private void setSubmitAble() {
        String location = tvLocation.getText().toString();
        String phoneNumber = etPhone.getText().toString();

        /**
         * 发货信息 归属地和手机号必填
         */
        CityAndLocationExtra cityAndLocationExtra = getIntentExtra(CityAndLocationExtra.getExtraName());
        if ("deliver".equalsIgnoreCase(cityAndLocationExtra.getPageTitle())) {
            if (StringUtils.isNotBlank(location) && StringUtils.isNotBlank(phoneNumber) && ApplyUtils.validateMobile
                    (phoneNumber)) {
                tvConfirm.setEnabled(true);
            } else {
                tvConfirm.setEnabled(false);
            }
        } else {
            if (StringUtils.isNotBlank(location)) {
                tvConfirm.setEnabled(true);
            } else {
                tvConfirm.setEnabled(false);
            }
        }
    }

    /**
     * 刷新列表选中状态
     */
    private void refreClickStatus(UsualContactListItem item) {
        List<UsualContactListItem> listItems = mPresenter.getUsualContactListItems();
        if (listItems != null) {
            for (int i = 0; i < listItems.size(); i++) {
                listItems.get(i).setCheck(false);
                if (item.getLinkman_id().equals(listItems.get(i).getLinkman_id())) {
                    listItems.get(i).setCheck(true);
                }
            }
        }

        selectContactsListAdapter.notifyDataSetChanged();
    }

    @OnClick({R.id.rl_location, R.id.rl_location_detailed, R.id.tv_add_contacts, R.id
            .ll_do_location, R.id.tv_confirm})
    public void onClick(View view) {
        super.onClick(view);
        int id = view.getId();

        if (id == R.id.rl_location) {
            new CitySelectPopupWindow(TAG).showCitySelectPopupWindow(this, tvLocation, false, false, true);
        } else if (id == R.id.rl_location_detailed) {
            if (TextUtils.isEmpty(tvLocation.getText())) {
                Toaster.showLongToast(R.string.please_select_address);
                return;
            }
            turnToActivityForResult(SelectAddressMapActivity.class, requestCode_toMap, mPresenter
                    .getCityAndLocationExtra());
        } else if (id == R.id.tv_add_contacts) {   // 添加联系人
            // 实名认证
            CheckUserStatusUtils.isRealNameAuthentication(this,
                    new CheckUserStatusUtils.OnJudgeResultListener() {
                        @Override
                        public void onSucceed() {
                            AddContactExtra extra = new AddContactExtra(true, AddContactExtra.LIST);
                            turnToActivity(ContactsInputActivity.class, extra);
                        }
                    });
        } else if (id == R.id.ll_do_location) {
            LocationUtils.location(mContext, this);
        } else if (id == R.id.tv_confirm) {
            CityAndLocationExtra cityAndLocationExtra = mPresenter.getCityAndLocationExtra();
            cityAndLocationExtra.setName(etName.getText().toString());
            cityAndLocationExtra.setPhone(etPhone.getText().toString());
            mPresenter.addLinkMan(etPhone.getText().toString(), etName.getText().toString());
            setResultAndFinish(Activity.RESULT_OK, cityAndLocationExtra);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode_toMap == requestCode) {
                CityAndLocationExtra cityAndLocationExtra = getIntentExtra(intent, CityAndLocationExtra.getExtraName());
                mPresenter.setCityAndLocationExtra(cityAndLocationExtra);
                String address = cityAndLocationExtra.getAddress();
                if (address != null) {
                    tvLocationDetailed.setText(address);
                }
                String backwards2Name = cityAndLocationExtra.getBackwards2Name();
                if (backwards2Name != null) {
                    tvLocation.setText(backwards2Name);
                }
            }
        }
    }

    /**
     * 定位回调
     */
    @Override
    public void onLocated(AMapLocation aMapLocation) {
        CityAndLocationExtra cityAndLocationExtra = DeliverGoodsDirtyDataProcess.aMapLocation2CityAndLocationExtra
                (aMapLocation);       // AMapLocation转换成CityAndLocationExtra
        if (cityAndLocationExtra != null) {
            String backwards2Name = cityAndLocationExtra.getBackwards2Name();
            tvLocation.setText(backwards2Name);
            tvLocationDetailed.setText(cityAndLocationExtra.getAddress());
        }
        mPresenter.setCityAndLocationExtra(cityAndLocationExtra);
    }

    @Override
    public void onLocationPermissionfaild() {
        Toaster.showLongToast(R.string.location_fail);
    }

    /**
     * 从城市选择器中获取数据
     */
    @Subscribe
    public void onEvent(AreaSelectedData areaSelectedData) {
        if (areaSelectedData.getTag().equals(TAG)) {
            mPresenter.selectAddress(areaSelectedData);     //  从城市选择器中获取数据
        }
    }

    public void showBackwards2Name(String backwards2Name) {
        if (!TextUtils.isEmpty(backwards2Name)) {
            tvLocation.setText(backwards2Name);
            tvLocationDetailed.setText("");
        }
    }

    public void refreListData(List<UsualContactListItem> usualContactListItems) {
        if (usualContactListItems != null && usualContactListItems.size() > 0) {
            emptyContacts.setVisibility(View.GONE);

            List<UsualContactListItem> tempList = new ArrayList<>();
            int maxSize = 3;    // 显示最大条数
            if (usualContactListItems.size() > maxSize) {
                selectContactsListAdapter.removeAllFooterView();
                selectContactsListAdapter.addFooterView(getFootView());
            }
            if (usualContactListItems.size() < maxSize) {
                maxSize = usualContactListItems.size();
                selectContactsListAdapter.removeAllFooterView();
            }
            for (int i = 0; i < maxSize; i++) {
                tempList.add(usualContactListItems.get(i));
            }
            selectContactsListAdapter.setNewData(tempList);
        }
    }

    /**
     * 获取脚布局
     */
    public View getFootView() {
        View view = getLayoutInflater().inflate(R.layout.layout_show_more_contacts, (ViewGroup)
                rvContacts.getParent(), false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 查看更多联系人
                turnToActivity(SelectContactsListActivity.class);
            }
        });
        return view;
    }

    /**
     * 选择的联系人填充至界面
     */
    public void setContact2Activity(UsualContactListItem item) {
        CityAndLocationExtra cityAndLocationExtra = DeliverGoodsDirtyDataProcess
                .usualContactListItem2CityAndLocationExtra(item);   // UsualContactListItem转换成CityAndLocationExtra
        CityItem cityItem = getCityItemByAdcode(item.getContacts_city_code());
        String fullName = item.getContacts_address();
        if (cityItem != null) {
            if (StringUtils.isNotBlank(cityItem.getCityFullName())) {
                fullName = cityItem.getCityFullName();

                CityItem parentItem = AreaDataDict.getCityItemByAdcode(cityItem.getParentId());
                if (parentItem != null) {
                    fullName = StringUtils.appendWithSpace(parentItem.getCityFullName(), fullName);
                }
            }
        }
        tvLocation.setText(fullName);
        tvLocationDetailed.setText(item.getContacts_address());
        etName.setText(item.getContacts_name());
        etPhone.setText(item.getContacts_mobile());

        if (cityAndLocationExtra != null) {
            cityAndLocationExtra.setBackwards2Name(fullName);
        }
        mPresenter.setCityAndLocationExtra(cityAndLocationExtra);

        refreClickStatus(item);
    }

    /**
     * 从选择常用联系人回来的数据
     */
    @Subscribe
    public void onEvent(UsualContactListItem item) {
        setContact2Activity(item);
    }
}
