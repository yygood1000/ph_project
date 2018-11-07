package com.topjet.crediblenumber.user.presenter;

import android.content.Context;

import com.topjet.common.base.listener.ObserverOnResultListener;
import com.topjet.common.base.presenter.BaseApiPresenter;
import com.topjet.common.common.modle.response.GetUserInfoAtHomeResponse;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.config.CPersisData;
import com.topjet.common.user.modle.params.RealNameAuthenticationParams;
import com.topjet.common.user.modle.response.RealNameAuthenticationResponse;
import com.topjet.common.user.modle.response.UserCenterRealNameAuthenticationResponse;
import com.topjet.common.user.modle.serverAPI.UserCertificationCommand;
import com.topjet.common.user.modle.serverAPI.UserCertificationCommandAPI;
import com.topjet.common.utils.CheckUserStatusUtils;
import com.topjet.common.utils.ImageUtil;
import com.topjet.crediblenumber.user.view.activity.RealNameAuthenticationView;

/**
 * Created by yy on 2017/7/29.
 * 实名认证
 */

public class RealNameAuthenticationPresenter extends BaseApiPresenter<RealNameAuthenticationView,
        UserCertificationCommand> {
    public int TYPE_AVATAR_PHOTO = 0;// 头像
    public int TYPE_IDCARD_PHOTO = 1;// 身份证

    public RealNameAuthenticationPresenter(RealNameAuthenticationView mView, Context mContext) {
        super(mView, mContext);
        mApiCommand = new UserCertificationCommand
                (UserCertificationCommandAPI.class, mActivity);
    }

    /**
     * 个人中心，实名认证，请求服务器接口
     */
    public void addOrEditAuth(String avatarPath, String idCardPath) {
        final RealNameAuthenticationParams realNameAuthenticationParams = new RealNameAuthenticationParams();
        String avatarData = ImageUtil.getBase64With480x800(avatarPath);// 转base64
        String idCardData = ImageUtil.getBase64With480x800(idCardPath);// 转base64
        realNameAuthenticationParams.setUse_status_icon_image(avatarData);
        realNameAuthenticationParams.setUse_status_idcard_image(idCardData);
        realNameAuthenticationParams.setVersion(CPersisData.getUserVersion());
        mApiCommand.userCentreAuth(realNameAuthenticationParams, new
                ObserverOnResultListener<RealNameAuthenticationResponse>() {
            @Override
            public void onResult(RealNameAuthenticationResponse response) {
                mView.submitSuccess();
                CMemoryData.setUserState(CheckUserStatusUtils.USER_STATUS_WAITAUCIT);
                CPersisData.setUserVersion(response.getVersion());
            }
        });
    }

    /**
     * 实名认证状态，请求服务器接口
     */
    public void queryAuthenStatus() {
        mApiCommand.queryRealNameAuthenStatus(new ObserverOnResultListener<UserCenterRealNameAuthenticationResponse>() {
            @Override
            public void onResult(UserCenterRealNameAuthenticationResponse response) {
                if (response != null) {
                    mView.showAvatarPhoto(response.getUse_status_icon_image_url(), response
                            .getUse_status_icon_image_key());
                    mView.showIdCardPhoto(response.getUse_status_idcard_image_url(), response
                            .getUse_status_idcard_image_key());
                    mView.setUseStatus(response.getUse_status(), response.getUse_status_remark());
                    CMemoryData.setUserState(response.getUse_status());
                    CPersisData.setUserVersion(response.getVersion());
                }
            }
        });
    }
}
