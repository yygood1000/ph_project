package com.topjet.crediblenumber.user.presenter;

import android.content.Context;

import com.topjet.common.base.listener.ObserverOnResultListener;
import com.topjet.common.base.presenter.BaseApiPresenter;
import com.topjet.common.config.CPersisData;
import com.topjet.common.user.modle.params.TypeAuthDriverParams;
import com.topjet.common.user.modle.serverAPI.UserCertificationCommand;
import com.topjet.common.utils.ImageUtil;
import com.topjet.common.utils.StringUtils;
import com.topjet.crediblenumber.user.view.activity.IdentityAuthenticationView;

import java.io.File;

/**
 * Created by tsj-004 on 2017/10/26.
 * 身份认证
 */

public class IdentityAuthenticationPresenter extends BaseApiPresenter<IdentityAuthenticationView,
        UserCertificationCommand> {
    public int TYPE_DRIVING_LICENSE_PHOTO = 0;// 驾驶证
    public int TYPE_ENTERPRISE_LICENSE_PHOTO = 1;// 营运证

    public IdentityAuthenticationPresenter(IdentityAuthenticationView mView, Context mContext,
                                           UserCertificationCommand mApiCommand) {
        super(mView, mContext, mApiCommand);
    }

    /**
     * 身份认证，请求服务器接口
     */
    public void addOrEditAuth(String driverPath, String enterprisePath) {
        TypeAuthDriverParams typeAuthDriverParams = new TypeAuthDriverParams();
        String driverData = null;
        String enterpriseData = null;

        if (!StringUtils.isEmpty(driverPath) && new File(driverPath).exists()) {
            driverData = ImageUtil.getBase64With480x800(driverPath);// 转base64
        }
        if (!StringUtils.isEmpty(enterprisePath) && new File(enterprisePath).exists()) {
            enterpriseData = ImageUtil.getBase64With480x800(enterprisePath);// 转base64
        }

        typeAuthDriverParams.setDriver_license_img(driverData);
        typeAuthDriverParams.setDriver_operation_img(enterpriseData);
        typeAuthDriverParams.setVersion(CPersisData.getUserVersion());
        mApiCommand.typeAuthDriver(typeAuthDriverParams, new ObserverOnResultListener<Object>() {
            @Override
            public void onResult(Object o) {
                mView.submitSuccess();
            }
        });
    }

    /**
     * 获取身份认证状态，请求服务器接口
     */
    public void queryAuthenStatus() {
        mApiCommand.queryDriverIdentityAuthenStatus(new ObserverOnResultListener<TypeAuthDriverParams>() {
            @Override
            public void onResult(TypeAuthDriverParams params) {
                if (params != null) {
                    mView.setUseStatus(params.getUser_auth_remark(), params.getUser_auth_status(),
                            params.getDriver_license_img_url(), params.getDriver_license_img_key(),
                            params.getDriver_operation_img_url(), params.getDriver_operation_img_key());

                    CPersisData.setUserVersion(params.getVersion());
                }
            }
        });
    }
}
