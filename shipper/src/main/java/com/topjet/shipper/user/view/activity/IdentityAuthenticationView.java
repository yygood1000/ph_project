package com.topjet.shipper.user.view.activity;

import com.topjet.common.base.view.activity.IView;

/**
 * Created by yy on 2017/7/29.
 * 身份认证
 */

public interface IdentityAuthenticationView extends IView {
    void submitSuccess();//提交成功

    void showDoorPhoto(String url, String key);

    void showBusinessCardPhoto(String url, String key);

    void showInvoicePhoto(String url, String key);

    void showLocationSuccess(String text);      // 定位成功
    void showLocationFail();      // 定位失败

    void showBusinessLicensePhoto(String url, String key);

    void setUseStatus(String use_status_remark, String use_status, String shipper_title_img_url,
                      String shipper_title_img_key, String shipper_card_img_url,
                      String shipper_card_img_key, String shipperi_certificate_img_url,
                      String shipperi_certificate_img_key, String shipper_business_img_url,
                      String shipper_business_img_key);  // 根据状态设置界面按钮是否可点击
}
