package com.topjet.shipper.user.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.topjet.common.base.listener.ObserverOnNextListener;
import com.topjet.common.base.listener.ObserverOnResultListener;
import com.topjet.common.base.presenter.BaseApiPresenter;
import com.topjet.common.base.progress.BackgroundObserver;
import com.topjet.common.config.CPersisData;
import com.topjet.common.user.modle.params.RealNameAuthenticationParams;
import com.topjet.common.base.net.request.CommonParams;
import com.topjet.common.user.modle.response.RealNameAuthenticationResponse;
import com.topjet.common.user.modle.response.UserCenterRealNameAuthenticationResponse;
import com.topjet.common.user.modle.serverAPI.UserCertificationCommand;
import com.topjet.common.user.modle.serverAPI.UserCertificationCommandAPI;
import com.topjet.common.utils.FileUtils;
import com.topjet.common.utils.GlideUtils;
import com.topjet.common.utils.ImageUtil;
import com.topjet.common.utils.SPUtils;
import com.topjet.common.utils.Toaster;
import com.topjet.common.base.presenter.BasePresenter;
import com.topjet.shipper.user.view.activity.RealNameAuthenticationView;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by yy on 2017/7/29.
 * 实名认证
 */

public class RealNameAuthenticationPresenter extends BaseApiPresenter<RealNameAuthenticationView, UserCertificationCommand> {
    public int TYPE_AVATAR_PHOTO = 0;// 头像
    public int TYPE_IDCARD_PHOTO = 1;// 身份证

    public RealNameAuthenticationPresenter(RealNameAuthenticationView mView, Context mContext, UserCertificationCommand mApiCommand) {
        super(mView, mContext, mApiCommand);
    }

    /**
     * 实名认证，请求服务器接口
     */
    public void auth(String avatarPath, String idCardPath) {
        final RealNameAuthenticationParams realNameAuthenticationParams = new RealNameAuthenticationParams();
        String avatarData = ImageUtil.getBase64With480x800(avatarPath);// 转base64
        String idCardData = ImageUtil.getBase64With480x800(idCardPath);// 转base64

        realNameAuthenticationParams.setUse_status_icon_image(avatarData);
        realNameAuthenticationParams.setUse_status_idcard_image(idCardData);
        realNameAuthenticationParams.setVersion(CPersisData.getUserVersion());
        mApiCommand.userCentreAuth(realNameAuthenticationParams, new ObserverOnResultListener<RealNameAuthenticationResponse>() {
            @Override
            public void onResult(RealNameAuthenticationResponse response) {
                mView.submitSuccess();
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
                    mView.setUseStatus(response.getUse_status(), response.getUse_status_remark());

                    mView.showAvatarPhoto(response.getUse_status_icon_image_url(), response.getUse_status_icon_image_key());
                    mView.showIdCardPhoto(response.getUse_status_idcard_image_url(), response.getUse_status_idcard_image_key());

                    CPersisData.setUserVersion(response.getVersion());
                }
            }
        });
    }
}
