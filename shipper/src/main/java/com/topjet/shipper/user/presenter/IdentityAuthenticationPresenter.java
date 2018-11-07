package com.topjet.shipper.user.presenter;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.topjet.common.base.listener.ObserverOnResultListener;
import com.topjet.common.base.presenter.BaseApiPresenter;
import com.topjet.common.config.CPersisData;
import com.topjet.common.resource.bean.CityItem;
import com.topjet.common.resource.dict.AreaDataDict;
import com.topjet.common.user.modle.params.TypeAuthOwnerParams;
import com.topjet.common.user.modle.serverAPI.UserCertificationCommand;
import com.topjet.common.utils.ImageUtil;
import com.topjet.common.utils.LocationUtils;
import com.topjet.common.utils.StringUtils;
import com.topjet.shipper.user.view.activity.IdentityAuthenticationView;

import java.io.File;

/**
 * Created by tsj-004 on 2017/10/26.
 * 身份认证
 */

public class IdentityAuthenticationPresenter extends BaseApiPresenter<IdentityAuthenticationView,
        UserCertificationCommand> implements LocationUtils.OnLocationListener {
    private AMapLocation aMapLocation = null;

    public IdentityAuthenticationPresenter(IdentityAuthenticationView mView, Context mContext,
                                           UserCertificationCommand mApiCommand) {
        super(mView, mContext, mApiCommand);
    }

    /**
     * 请求定位
     */
    public void requestLocation() {
        LocationUtils.location(mContext, this);   // 定位
    }

    /**
     * 身份认证，货主版，请求服务器接口
     */
    public void typeAuthOwner(final String doorPath, String cardPath, String invoicePath, String licensePath) {
        String doorData = null;
        String cardData = null;
        String invoiceData = null;
        String licenseData = null;

        TypeAuthOwnerParams typeAuthOwnerParams = new TypeAuthOwnerParams();
        if (!StringUtils.isEmpty(doorPath) && new File(doorPath).exists()) {
            doorData = ImageUtil.getBase64With480x800(doorPath);// 转base64
            typeAuthOwnerParams.setShipper_title_img(doorData);
        }
        if (!StringUtils.isEmpty(cardPath) && new File(cardPath).exists()) {
            cardData = ImageUtil.getBase64With480x800(cardPath);// 转base64
            typeAuthOwnerParams.setShipper_card_img(cardData);
        }
        if (!StringUtils.isEmpty(invoicePath) && new File(invoicePath).exists()) {
            invoiceData = ImageUtil.getBase64With480x800(invoicePath);// 转base64
            typeAuthOwnerParams.setShipperi_certificate_img(invoiceData);
        }
        if (!StringUtils.isEmpty(licensePath) && new File(licensePath).exists()) {
            licenseData = ImageUtil.getBase64With480x800(licensePath);// 转base64
            typeAuthOwnerParams.setShipper_business_img(licenseData);
        }
        if (aMapLocation != null) {
            typeAuthOwnerParams.setDetailed_address(aMapLocation.getAddress());
            typeAuthOwnerParams.setLongitude(aMapLocation.getLongitude() + "");
            typeAuthOwnerParams.setLatitude(aMapLocation.getLatitude() + "");
            typeAuthOwnerParams.setRegistered_address_code(aMapLocation.getAdCode());
        }
        typeAuthOwnerParams.setVersion(CPersisData.getUserVersion());

        mApiCommand.typeauthowner(typeAuthOwnerParams, new ObserverOnResultListener<Object>() {
            @Override
            public void onResult(Object o) {
                mView.submitSuccess();
            }
        });

    }

    /**
     * 身份认证状态，请求服务器接口
     */
    public void queryAuthenStatus() {
        mApiCommand.queryOwnerIdentityAuthenStatus(new ObserverOnResultListener<TypeAuthOwnerParams>() {
            @Override
            public void onResult(TypeAuthOwnerParams response) {
                if (response != null) {
                    String addressId = response.getRegistered_address_id();
                    CityItem cityItem = AreaDataDict.getCityItemByAdcode(addressId);
                    if (cityItem != null) {
                        String fullName = cityItem.getCityFullName();
                        String parentId = cityItem.getParentId();
                        if (StringUtils.isNotBlank(parentId)) {
                            CityItem parentItem = AreaDataDict.getCityItemByAdcode(parentId);
                            String cityName = parentItem.getCityName();
                            if (StringUtils.isNotBlank(cityName)) {
                                fullName = StringUtils.appendWithSpace(cityName, fullName);
                                fullName = AreaDataDict.replaceCityAndProvinceString(fullName);
                            }
                        }

                        if (!StringUtils.isEmpty(fullName)) {
                            mView.showLocationSuccess(fullName);
                        }
                    }

                    mView.setUseStatus(response.getUser_auth_remark(), response.getUser_auth_status(), response
                                    .getShipper_title_img_url(), response.getShipper_title_img_key(),
                            response.getShipper_card_img_url(), response.getShipper_card_img_key(), response
                                    .getShipperi_certificate_img_url(), response.getShipperi_certificate_img_key(),
                            response.getShipper_business_img_url(), response.getShipper_business_img_key());

                    CPersisData.setUserVersion(response.getVersion());
                }
            }
        });
    }

    @Override
    public void onLocated(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            this.aMapLocation = aMapLocation;
            mView.showLocationSuccess(AreaDataDict.replaceCityAndProvinceString(StringUtils.appendWithSpace
                    (aMapLocation.getCity(), aMapLocation.getDistrict())));
        } else {
            mView.showLocationFail();
        }
    }

    @Override
    public void onLocationPermissionfaild() {
        mView.showLocationFail();
    }
}
