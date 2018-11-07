package com.topjet.shipper.deliver.presenter;

import android.content.Context;

import com.topjet.common.base.controller.AreaDataMachiningController;
import com.topjet.common.base.listener.ObserverOnNextListener;
import com.topjet.common.base.listener.ObserverOnResultListener;
import com.topjet.common.base.presenter.BasePresenter;
import com.topjet.common.common.modle.event.AreaSelectedData;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.order_detail.modle.extra.CityAndLocationExtra;
import com.topjet.common.user.modle.params.GetUsualContactListParams;
import com.topjet.common.user.modle.params.PageNumParams;
import com.topjet.common.user.modle.params.UsualContactListItem;
import com.topjet.common.user.modle.response.IdResponse;
import com.topjet.common.utils.StringUtils;
import com.topjet.shipper.deliver.view.activity.AddressInformationView;
import com.topjet.shipper.user.model.bean.ContactsInputParams;
import com.topjet.shipper.user.model.serviceAPI.ContactCommand;
import com.topjet.shipper.user.model.serviceAPI.ContactCommandAPI;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tsj-004 on 2017/8/28.
 * 发货--发货信息页/发货--收货信息页
 */

public class AddressInformationPresenter extends BasePresenter<AddressInformationView> {
    private List<UsualContactListItem> usualContactListItems = new ArrayList<UsualContactListItem>();    // 联系人列表list

    private CityAndLocationExtra cityAndLocationExtra = new CityAndLocationExtra();

    public AddressInformationPresenter(AddressInformationView mView, Context mContext) {
        super(mView, mContext);
    }

    public CityAndLocationExtra getCityAndLocationExtra() {
        if (cityAndLocationExtra == null) {
            cityAndLocationExtra = new CityAndLocationExtra();
        }
        return cityAndLocationExtra;
    }

    public void setCityAndLocationExtra(CityAndLocationExtra cityAndLocationExtra) {
        if (cityAndLocationExtra == null) {
            cityAndLocationExtra = new CityAndLocationExtra();
        }
        this.cityAndLocationExtra = cityAndLocationExtra;
    }

    /**
     * 从城市选择器中获取数据
     */
    public void selectAddress(AreaSelectedData areaSelectedData) {
        cityAndLocationExtra = AreaDataMachiningController.machining(areaSelectedData);  // 处理城市选择器数据工具类
        mView.showBackwards2Name(cityAndLocationExtra.getBackwards2Name());
    }

    /**
     * 请求网络接口，返回常用联系人
     */
    public void requestNetBackContact() {
        new ContactCommand(ContactCommandAPI.class, mActivity)
                .selectLinkmanList(new PageNumParams("1"), new ObserverOnResultListener<GetUsualContactListParams>() {
                    @Override
                    public void onResult(GetUsualContactListParams getUsualContactListParams) {
                        usualContactListItems.clear();
                        usualContactListItems.addAll(getUsualContactListParams.getSelectLinkmanListDTOs());
                        mView.refreListData(usualContactListItems);
                    }
                });
    }

    public List<UsualContactListItem> getUsualContactListItems() {
        return usualContactListItems;
    }

    /**
     * 添加常用联系人
     */
    public void addLinkMan(String phone, String name) {
        // 不能添加自己
        if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(name) || cityAndLocationExtra == null) {
            return;
        }
        if (CMemoryData.getUserMobile().equals(phone)) {
            // 这里不提示
//            Toaster.showShortToast(mContext.getString(R.string.self_contact));
            return;
        }
        ContactsInputParams params = new ContactsInputParams();
        params.setContacts_mobile(phone);
        params.setContacts_name(name);
        params.setContacts_address(cityAndLocationExtra.getAddress());
        params.setContacts_city_code(cityAndLocationExtra.getCityCode());
        params.setLatitude(cityAndLocationExtra.getLatitude());
        params.setLongitude(cityAndLocationExtra.getLongitude());
        new ContactCommand(ContactCommandAPI.class, mActivity)
                .insertLinkman(params, new ObserverOnNextListener<IdResponse>() {
                    @Override
                    public void onNext(IdResponse idResponse) {

                    }

                    @Override
                    public void onError(String errorCode, String msg) {

                    }
                });
    }

}