package com.topjet.crediblenumber.user.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.user.modle.extra.GoToAuthenticationExtra;
import com.topjet.common.user.modle.serverAPI.UserCertificationCommand;
import com.topjet.common.user.modle.serverAPI.UserCertificationCommandAPI;
import com.topjet.common.user.view.dialog.AuthenticationPopup;
import com.topjet.common.utils.CameraUtil;
import com.topjet.common.utils.GlideUtils;
import com.topjet.common.utils.ImageUtil;
import com.topjet.common.utils.StringUtils;
import com.topjet.common.utils.Toaster;
import com.topjet.common.widget.MyTitleBar;
import com.topjet.crediblenumber.R;
import com.topjet.crediblenumber.user.presenter.IdentityAuthenticationPresenter;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 身份认证
 * 司机版
 */
public class IdentityAuthenticationActivity extends MvpActivity<IdentityAuthenticationPresenter> implements
        IdentityAuthenticationView {
    @BindView(R.id.rl_upload_driving_license)
    RelativeLayout rlUploadDrivingLicense;
    @BindView(R.id.rl_upload_enterprise_license)
    RelativeLayout rlUploadEnterpriseLicense;
    @BindView(R.id.iv_show_driving_license)
    ImageView ivShowDrivingLicense;
    @BindView(R.id.iv_show_enterprise_license)
    ImageView ivShowEnterpriseLicense;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;      //提交按钮

    @BindView(R.id.tv_driver_license_hint) //驾驶证提示
            TextView tvDriverLicenseHint;
    @BindView(R.id.tv_driver_license_hint_max) //驾驶证提示
            TextView tvDriverLicenseHintMax;
    @BindView(R.id.iv_driver_license_add) //驾驶证加号
            ImageView ivDriverLicenseAdd;

    @BindView(R.id.tv_enterprise_license_hint) //营运证提示
            TextView tvEnterpriseLicenseHint;
    @BindView(R.id.tv_enterprise_license_hint_max) //营运证提示
            TextView tvEnterpriseLicenseHintMax;
    @BindView(R.id.iv_enterprise_license_add) //营运证加号
            ImageView ivEnterpriseLicenseAdd;

    @BindView(R.id.tv_status)
    TextView tvStatus;      // 认证进度
    @BindView(R.id.ll_progress)
    LinearLayout llProgress;      // 认证进度
    @BindView(R.id.vvvvvv)
    View vvvvvv;      // 认证进度下方间隔
    @BindView(R.id.ll_driver_license_authenticaioning)
    LinearLayout llDriverLicenseAuthenticaioning;      // 审核中，驾驶证
    @BindView(R.id.ll_enterprise_license_authenticaioning)
    LinearLayout llEnterpriseLicenseAuthenticaioning;      // 审核中，营运证
    @BindView(R.id.tv_driver_license_authenticaioning)
    TextView tvDriverLicenseAuthenticaioning;      // 审核中，驾驶证
    @BindView(R.id.rl_driver_license_fail)
    RelativeLayout rlDriverLicenseFail;      // 认证失败，重新上传，驾驶证

    @BindView(R.id.tv_enterprise_license_authenticaioning)
    TextView tvEnterpriseLicenseAuthenticaioning;      // 审核中，营运证
    @BindView(R.id.rl_enterprise_license_fail)
    RelativeLayout rlEnterpriseLicenseFail;      // 认证失败，重新上传，营运证

    private int curPhotoType = 0;       //0 驾驶证 1 营运证
    private String driverPath = null;   // 驾照图片地址
    private String enterprisePath = null;   //营运证图片地址

    private boolean isShowMode = false; // 是否是显示模式，显示模式有返回按钮，没有“跳过”

    private GoToAuthenticationExtra goToAuthenticationExtra = new GoToAuthenticationExtra();    // 界面间传值

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_authentication_identity;
    }

    @Override
    protected void initTitle() {
        getMyTitleBar().setMode(MyTitleBar.Mode.TITLE_RTEXT).setTitleText(R.string.identity_authentication)
                .setRightText(R.string.skip);
    }

    @Override
    protected void initPresenter() {
        mPresenter = new IdentityAuthenticationPresenter(this, this, new UserCertificationCommand
                (UserCertificationCommandAPI.class, this));
    }

    @Override
    public void initView() {
        GoToAuthenticationExtra extra = getIntentExtra(GoToAuthenticationExtra.getExtraName());
        if (extra != null) {
            goToAuthenticationExtra = extra;
        }

        ivShowDrivingLicense.post(new Runnable() {
            @Override
            public void run() {
                if (goToAuthenticationExtra != null && goToAuthenticationExtra.getInType() == GoToAuthenticationExtra
                        .IN_TYPE_SHOW) {
                    getMyTitleBar().setMode(MyTitleBar.Mode.BACK_TITLE).setTitleText(R.string.identity_authentication);
                    isShowMode = true;

                    mPresenter.queryAuthenStatus(); // 请求网络，获取审核状态和图片地址
                    llProgress.setVisibility(View.GONE);
                    vvvvvv.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    protected void onClickRightText() { // 跳过
        turnToActivityWithFinish(CarAuthenticationActivity.class);
    }

    @Override
    public void onBackPressed() {
        if (isShowMode) {
            super.onBackPressed();
        }
    }

    /**
     * 点击头像
     */
    @OnClick({R.id.rl_upload_driving_license, R.id.rl_upload_enterprise_license, R.id.tv_submit, R.id
            .rl_driver_license_fail, R.id.rl_enterprise_license_fail})
    public void clickViews(View v) {
        int id = v.getId();
        if (id == R.id.rl_upload_driving_license || id == R.id.rl_driver_license_fail) {   // 驾驶证
            curPhotoType = mPresenter.TYPE_DRIVING_LICENSE_PHOTO;
            new AuthenticationPopup(this).initPop(R.string.driving_license, R.drawable.approve_driving_licence_eg,
                    this).showPop(rlUploadDrivingLicense);
        } else if (id == R.id.rl_upload_enterprise_license || id == R.id.rl_enterprise_license_fail) {   // 营运证
            curPhotoType = mPresenter.TYPE_ENTERPRISE_LICENSE_PHOTO;
            new AuthenticationPopup(this).initPop(R.string.enterprise_license, R.drawable.approve_trading_card_eg,
                    this).showPop(rlUploadEnterpriseLicense);
        } else if (id == R.id.tv_submit) {
            mPresenter.addOrEditAuth(driverPath, enterprisePath);    // 请求服务器接口
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int id = v.getId();
        if (id == R.id.tv_take_photo) {
            if (curPhotoType == 0) {
                CameraUtil.jumpToCameraGetPic(this);// 驾驶证
            } else if (curPhotoType == 1) {  // 自定义相机
                CameraUtil.jumpToCameraGetPic(this);// 营运证
            }
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case CameraUtil.REQUEST_CODE.SYSTEM_CAMERA:  // 系统拍照
                    if (curPhotoType == mPresenter.TYPE_DRIVING_LICENSE_PHOTO) {
                        driverPath = CameraUtil.currentPicPath;
                        ImageUtil.toCropForSquare(this, new File(driverPath));
                    } else if (curPhotoType == mPresenter.TYPE_ENTERPRISE_LICENSE_PHOTO) {
                        enterprisePath = CameraUtil.currentPicPath;
                        ImageUtil.toCropForSquare(this, new File(enterprisePath));
                    }
                    break;
                case CameraUtil.REQUEST_CODE.SQUARE_CROP:  // 裁剪后
                    if (curPhotoType == mPresenter.TYPE_DRIVING_LICENSE_PHOTO) {
                        driverPath = data.getStringExtra("filePath");
                        showDrivingLiscensePhoto(driverPath, null); // 显示驾驶证
                    } else {
                        enterprisePath = data.getStringExtra("filePath");
                        showEnterpriseLicensePhoto(enterprisePath, null); // 显示营运证
                    }

                    if (driverPath != null || enterprisePath != null) {
                        tvSubmit.setEnabled(true);
                    }
                    break;
            }
        }
    }

    @Override
    public void submitSuccess() {
        if (isShowMode) {
            Toaster.showLongToast(R.string.update_authentication_success);
            setResultAndFinish(RESULT_OK);
        } else {
            turnToActivity(CarAuthenticationActivity.class);
        }
    }

    @Override
    public void showDrivingLiscensePhoto(String url, String key) {
        if (StringUtils.isEmpty(key)) {
            GlideUtils.loaderImageRoundWithSize(mContext, url, ivShowDrivingLicense, 5, ivShowDrivingLicense.getWidth
                    (), ivShowDrivingLicense.getHeight());
        } else {
            GlideUtils.loaderImageRoundWithSize(mContext, url, key, ivShowDrivingLicense, 5, ivShowDrivingLicense
                    .getWidth(), ivShowDrivingLicense.getHeight());
        }
    }

    @Override
    public void showEnterpriseLicensePhoto(String url, String key) {
        if (StringUtils.isEmpty(key)) {
            GlideUtils.loaderImageRoundWithSize(mContext, url, ivShowEnterpriseLicense, 5, ivShowEnterpriseLicense
                    .getWidth(), ivShowEnterpriseLicense.getHeight());
        } else {
            GlideUtils.loaderImageRoundWithSize(mContext, url, key, ivShowEnterpriseLicense, 5,
                    ivShowEnterpriseLicense.getWidth(), ivShowEnterpriseLicense.getHeight());
        }
    }

    @Override
    public void setUseStatus(String use_status_remark, String user_auth_status, String driver_license_img_url, String
            driver_license_img_key, String driver_operation_img_url, String driver_operation_img_key) {
        if (StringUtils.isEmpty(user_auth_status)) {
            return;
        }

        /**
         * 0、未认证 1、认证中 2、认证失败 3、认证通过
         */
        boolean canClickPhoto = false;
        boolean driverLicenseIsEmpty = StringUtils.isEmpty(driver_license_img_url);       // 驾驶证是否为空
        boolean driverOperationIsEmpty = StringUtils.isEmpty(driver_operation_img_url);     // 营运证是否为空

        showDrivingLiscensePhoto(driver_license_img_url, driver_license_img_key);
        showEnterpriseLicensePhoto(driver_operation_img_url, driver_operation_img_key);
        switch (user_auth_status) {
            case "0":
                canClickPhoto = true;
                break;

            case "1":
                hideAddAndHint(!driverLicenseIsEmpty, !driverOperationIsEmpty);

                if (driverLicenseIsEmpty) {
                    ivDriverLicenseAdd.setImageResource(R.drawable.icon_approve_ban);
                } else {
                    llDriverLicenseAuthenticaioning.setVisibility(View.VISIBLE);
                }

                if (driverOperationIsEmpty) {
                    ivEnterpriseLicenseAdd.setImageResource(R.drawable.icon_approve_ban);
                } else {
                    llEnterpriseLicenseAuthenticaioning.setVisibility(View.VISIBLE);
                }
                canClickPhoto = false;
                tvSubmit.setVisibility(View.GONE);
                break;

            case "2":
                hideAddAndHint(!driverLicenseIsEmpty, !driverOperationIsEmpty);
                canClickPhoto = true;

                if (driverLicenseIsEmpty) {
                    rlDriverLicenseFail.setVisibility(View.GONE);
                } else {
                    rlDriverLicenseFail.setVisibility(View.VISIBLE);
                }

                if (driverOperationIsEmpty) {
                    rlEnterpriseLicenseFail.setVisibility(View.GONE);
                } else {
                    rlEnterpriseLicenseFail.setVisibility(View.VISIBLE);
                }
                break;

            case "3":
                hideAddAndHint(!driverLicenseIsEmpty, !driverOperationIsEmpty);

                if (driverLicenseIsEmpty) {
                    ivDriverLicenseAdd.setImageResource(R.drawable.icon_approve_ban);
                } else {
                    llDriverLicenseAuthenticaioning.setVisibility(View.VISIBLE);
                    tvDriverLicenseAuthenticaioning.setText(R.string.authentication_success);
                }

                if (driverOperationIsEmpty) {
                    ivEnterpriseLicenseAdd.setImageResource(R.drawable.icon_approve_ban);
                } else {
                    llEnterpriseLicenseAuthenticaioning.setVisibility(View.VISIBLE);
                    tvEnterpriseLicenseAuthenticaioning.setText(R.string.authentication_success);
                }
                canClickPhoto = false;
                tvSubmit.setVisibility(View.GONE);
                break;
        }

        tvStatus.setText(use_status_remark);
        rlDriverLicenseFail.setEnabled(canClickPhoto);
        rlUploadDrivingLicense.setEnabled(canClickPhoto);
        rlUploadEnterpriseLicense.setEnabled(canClickPhoto);
        rlEnterpriseLicenseFail.setEnabled(canClickPhoto);
    }

    /**
     * 隐藏加号和提示
     */
    private void hideAddAndHint(boolean driverLicenseIsHide, boolean driverOperationIsHide) {
        if (driverLicenseIsHide) {
            tvDriverLicenseHint.setVisibility(View.INVISIBLE);
            tvDriverLicenseHintMax.setVisibility(View.INVISIBLE);
            ivDriverLicenseAdd.setVisibility(View.INVISIBLE);
        }

        if (driverOperationIsHide) {
            tvEnterpriseLicenseHint.setVisibility(View.INVISIBLE);
            tvEnterpriseLicenseHintMax.setVisibility(View.INVISIBLE);
            ivEnterpriseLicenseAdd.setVisibility(View.INVISIBLE);
        }
    }
}
