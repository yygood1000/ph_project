package com.topjet.shipper.user.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.topjet.common.config.RespectiveData;
import com.topjet.common.user.modle.extra.GoToAuthenticationExtra;
import com.topjet.common.user.modle.serverAPI.UserCertificationCommand;
import com.topjet.common.user.modle.serverAPI.UserCertificationCommandAPI;
import com.topjet.common.utils.CameraUtil;
import com.topjet.common.utils.GlideUtils;
import com.topjet.common.utils.ImageUtil;
import com.topjet.common.utils.StringUtils;
import com.topjet.common.utils.Toaster;
import com.topjet.common.widget.MyTitleBar;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.user.view.dialog.AuthenticationPopup;
import com.topjet.shipper.R;
import com.topjet.shipper.user.presenter.IdentityAuthenticationPresenter;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 身份认证
 * 货主版
 */
public class IdentityAuthenticationActivity extends MvpActivity<IdentityAuthenticationPresenter> implements IdentityAuthenticationView {
    @BindView(R.id.rl_upload_door_photo)
    RelativeLayout rlUploadDoorPhoto;
    @BindView(R.id.rl_upload_business_card)
    RelativeLayout rlUploadBusinessCard;
    @BindView(R.id.rl_upload_invoice)
    RelativeLayout rlUploadInvoice;
    @BindView(R.id.rl_upload_business_license)
    RelativeLayout rlUploadBusinessLicense;
    @BindView(R.id.iv_show_door_photo)
    ImageView ivShowDoorPhoto;
    @BindView(R.id.iv_show_business_card)
    ImageView ivShowBusinessCard;
    @BindView(R.id.iv_show_invoice)
    ImageView ivShowInvoice;
    @BindView(R.id.iv_show_business_license)
    ImageView ivShowBusinessLicense;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;      //提交按钮

    @BindView(R.id.tv_door_photo_hint)
    TextView tvDoorPhotoHint;      //车头照提示
    @BindView(R.id.iv_door_photo_add)
    ImageView ivDoorPhotoAdd;      //车头照加号
    @BindView(R.id.tv_business_card_hint)
    TextView tvBusinessCardHint;      //名片提示
    @BindView(R.id.iv_business_card_add)
    ImageView ivBusinessCardAdd;      //名片加号
    @BindView(R.id.tv_invoice_hint)
    TextView tvInvoiceHint;      //机打发票提示
    @BindView(R.id.iv_invoice_add)
    ImageView ivInvoiceAdd;      //机打发票加号
    @BindView(R.id.tv_business_license_hint)
    TextView tvBusinessLicenseHint;      //营业执照提示
    @BindView(R.id.iv_business_license_add)
    ImageView ivBusinessLicenseAdd;      //营业执照加号


    @BindView(R.id.tv_status)
    TextView tvStatus;      // 认证进度
    @BindView(R.id.ll_progress)
    LinearLayout llProgress;      // 认证进度
    @BindView(R.id.vvvvvvv)
    View vvvvvvv;      // 认证进度下方间隔

    @BindView(R.id.ll_door_photo_authenticaioning)
    LinearLayout llDoorPhotoAuthenticaioning;      // 审核中，门头照
    @BindView(R.id.tv_door_photo_authenticaioning)
    TextView tvDoorPhotoAuthenticaioning;      // 审核中，门头照
    @BindView(R.id.rl_door_photo_fail)
    RelativeLayout rlDoorPhotoFail;      // 认证失败，重新上传，门头照

    @BindView(R.id.ll_business_card_authenticaioning)
    LinearLayout llBusinessCardAuthenticaioning;      // 审核中，名片
    @BindView(R.id.tv_business_card_authenticaioning)
    TextView tvBusinessCardAuthenticaioning;      // 审核中，名片
    @BindView(R.id.rl_business_card_fail)
    RelativeLayout rlBusinessCardFail;      // 认证失败，重新上传，名片

    @BindView(R.id.ll_invoice_authenticaioning)
    LinearLayout llInvoiceAuthenticaioning;      // 审核中，发票
    @BindView(R.id.tv_invoice_authenticaioning)
    TextView tvInvoiceAuthenticaioning;      // 审核中，发票
    @BindView(R.id.rl_invoice_fail)
    RelativeLayout rlInvoiceFail;      // 认证失败，重新上传，发票

    @BindView(R.id.ll_business_license_authenticaioning)
    LinearLayout llBusinessLicenseAuthenticaioning;      // 审核中，营业证
    @BindView(R.id.tv_business_license_authenticaioning)
    TextView tvBusinessLicenseAuthenticaioning;      // 审核中，营业证
    @BindView(R.id.rl_business_license_fail)
    RelativeLayout rlBusinessLicenseFail;      // 认证失败，重新上传，营业证

    private int curPhotoType = 0;       //0 门头照 1 名片 2 发票 3 营业执照
    private String doorPath = null;   // 门头照图片地址
    private String cardPath = null;   //名片图片地址
    private String invoicePath = null;   // 发票图片地址
    private String licensePath = null;   //营业证图片地址

    private boolean isShowMode = false; // 是否是显示模式，显示模式有返回按钮，没有“跳过”
    private GoToAuthenticationExtra goToAuthenticationExtra = new GoToAuthenticationExtra();    // 界面间传值

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_authentication_identity;
    }

    @Override
    protected void initTitle() {
        getMyTitleBar().setMode(MyTitleBar.Mode.TITLE_RTEXT).setTitleText(R.string.identity_authentication).setRightText(R.string.skip);
    }

    @Override
    protected void initPresenter() {
        mPresenter = new IdentityAuthenticationPresenter(this, mContext, new UserCertificationCommand(UserCertificationCommandAPI.class, this));
    }

    @Override
    public void initView() {
        GoToAuthenticationExtra extra = getIntentExtra(GoToAuthenticationExtra.getExtraName());
        if (extra != null) {
            goToAuthenticationExtra = extra;
        }
        ivShowDoorPhoto.post(new Runnable() {
            @Override
            public void run() {
                if (goToAuthenticationExtra != null && goToAuthenticationExtra.getInType() == GoToAuthenticationExtra.IN_TYPE_SHOW) {
                    getMyTitleBar().setMode(MyTitleBar.Mode.BACK_TITLE).setTitleText(R.string.identity_authentication);
                    tvAddress.setText("注册地");
                    isShowMode = true;

                    mPresenter.queryAuthenStatus(); // 请求网络，获取审核状态和图片地址
                    llProgress.setVisibility(View.GONE);
                    vvvvvvv.setVisibility(View.GONE);
                } else {
                    mPresenter.requestLocation();   //  请求定位
                }
            }
        });
    }

    @Override
    protected void onClickRightText() { // 跳过
        turnToActivityWithFinish(RespectiveData.getMainActivityClass());
    }

    @Override
    public void onBackPressed() {
        if (isShowMode) {
            super.onBackPressed();
        }
    }

    /**
     * 点击头像和注册地
     */
    @OnClick({R.id.rl_upload_door_photo, R.id.rl_upload_business_card, R.id.rl_upload_invoice, R.id.rl_upload_business_license,
            R.id.tv_submit, R.id.tv_address, R.id.rl_door_photo_fail, R.id.rl_business_card_fail, R.id.rl_invoice_fail, R.id.rl_business_license_fail})
    public void clickViews(View v) {
        int id = v.getId();
        if (id == R.id.rl_upload_door_photo || id == R.id.rl_door_photo_fail) {   // 门头照
            curPhotoType = 0;
            new AuthenticationPopup(this).initPop(R.string.door_photo, R.drawable.approve_door_head_eg, this).showPop(rlUploadDoorPhoto);
            mPresenter.requestLocation();// 定位
        } else if (id == R.id.rl_upload_business_card || id == R.id.rl_business_card_fail) {   // 名片
            curPhotoType = 1;
            new AuthenticationPopup(this).initPop(R.string.business_card, R.drawable.approve_card_eg, this).showPop(rlUploadBusinessCard);
            mPresenter.requestLocation();// 定位
        } else if (id == R.id.rl_upload_invoice || id == R.id.rl_invoice_fail) {   // 发票
            curPhotoType = 2;
            new AuthenticationPopup(this).initPop(R.string.invoice, R.drawable.approve_invoice_eg, this).showPop(rlUploadInvoice);
            mPresenter.requestLocation();// 定位
        } else if (id == R.id.rl_upload_business_license || id == R.id.rl_business_license_fail) {   // 营业执照
            curPhotoType = 3;
            new AuthenticationPopup(this).initPop(R.string.business_license, R.drawable.approve_business_license_eg, this).showPop(rlUploadBusinessLicense);
            mPresenter.requestLocation();// 定位
        } else if (id == R.id.tv_submit) {
            mPresenter.typeAuthOwner(doorPath, cardPath, invoicePath, licensePath); // 请求服务器接口
        } else if (id == R.id.tv_address) {
            mPresenter.requestLocation();// 定位
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int id = v.getId();
        if (id == R.id.tv_take_photo) {
            CameraUtil.jumpToCameraGetPic(this); // 去系统拍照
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (CameraUtil.REQUEST_CODE.SYSTEM_CAMERA == requestCode) { // 系统拍照
                if (curPhotoType == 0) {
                    doorPath = CameraUtil.currentPicPath;
                    ImageUtil.toCropForSquare(this, new File(doorPath));
                } else if (curPhotoType == 1) {
                    cardPath = CameraUtil.currentPicPath;
                    ImageUtil.toCropForSquare(this, new File(cardPath));
                } else if (curPhotoType == 2) {
                    invoicePath = CameraUtil.currentPicPath;
                    ImageUtil.toCropForSquare(this, new File(invoicePath));
                } else if (curPhotoType == 3) {
                    licensePath = CameraUtil.currentPicPath;
                    ImageUtil.toCropForSquare(this, new File(licensePath));
                }
            } else if (CameraUtil.REQUEST_CODE.SQUARE_CROP == requestCode) { // 裁剪后
                if (curPhotoType == 0) {
                    doorPath = data.getStringExtra("filePath");
                    showDoorPhoto(doorPath, null); // 显示门头照
                } else if (curPhotoType == 1) {
                    cardPath = data.getStringExtra("filePath");
                    showBusinessCardPhoto(cardPath, null); // 显示名片
                } else if (curPhotoType == 2) {
                    invoicePath = data.getStringExtra("filePath");
                    showInvoicePhoto(invoicePath, null); // 显示发票
                } else if (curPhotoType == 3) {
                    licensePath = data.getStringExtra("filePath");
                    showBusinessLicensePhoto(licensePath, null); // 显示执照
                }

                tvSubmit.setEnabled(true);
            }
        }
    }

    @Override
    public void submitSuccess() {
        if (isShowMode) {
            Toaster.showLongToast(R.string.update_authentication_success);
            finish();
        } else {
            turnToActivityWithFinish(RespectiveData.getMainActivityClass());
        }
    }

    @Override
    public void showDoorPhoto(String url, String key) {
        if (StringUtils.isEmpty(key)) {
            GlideUtils.loaderImageRoundWithSize(mContext, url, ivShowDoorPhoto, 5, ivShowDoorPhoto.getWidth(), ivShowDoorPhoto.getHeight());
        } else {
            GlideUtils.loaderImageRoundWithSize(mContext, url, key, ivShowDoorPhoto, 5, ivShowDoorPhoto.getWidth(), ivShowDoorPhoto.getHeight());
        }
    }

    @Override
    public void showBusinessCardPhoto(String url, String key) {
        if (StringUtils.isEmpty(key)) {
            GlideUtils.loaderImageRoundWithSize(mContext, url, ivShowBusinessCard, 5, ivShowBusinessCard.getWidth(), ivShowBusinessCard.getHeight());
        } else {
            GlideUtils.loaderImageRoundWithSize(mContext, url, key, ivShowBusinessCard, 5, ivShowBusinessCard.getWidth(), ivShowBusinessCard.getHeight());
        }
    }

    @Override
    public void showInvoicePhoto(String url, String key) {
        if (StringUtils.isEmpty(key)) {
            GlideUtils.loaderImageRoundWithSize(mContext, url, ivShowInvoice, 5, ivShowInvoice.getWidth(), ivShowInvoice.getHeight());
        } else {
            GlideUtils.loaderImageRoundWithSize(mContext, url, key, ivShowInvoice, 5, ivShowInvoice.getWidth(), ivShowInvoice.getHeight());
        }
    }

    @Override
    public void showLocationSuccess(String text) {
        tvAddress.setText(text);
    }

    @Override
    public void showLocationFail() {
        tvAddress.setText(R.string.click_do_location);
        Toaster.showLongToast(R.string.location_error);
    }

    @Override
    public void showBusinessLicensePhoto(String url, String key) {
        if (StringUtils.isEmpty(key)) {
            GlideUtils.loaderImageRoundWithSize(mContext, url, ivShowBusinessLicense, 5, ivShowBusinessLicense.getWidth(), ivShowBusinessLicense.getHeight());
        } else {
            GlideUtils.loaderImageRoundWithSize(mContext, url, key, ivShowBusinessLicense, 5, ivShowBusinessLicense.getWidth(), ivShowBusinessLicense.getHeight());
        }
    }

    @Override
    public void setUseStatus(String use_status_remark, String use_status, String shipper_title_img_url,
                             String shipper_title_img_key, String shipper_card_img_url,
                             String shipper_card_img_key, String shipperi_certificate_img_url,
                             String shipperi_certificate_img_key, String shipper_business_img_url,
                             String shipper_business_img_key) {
        if (StringUtils.isEmpty(use_status) || "0".equals(use_status)) {
            mPresenter.requestLocation();
            return;
        }

        /**
         * 0、未认证 1、认证中 2、认证失败 3、认证通过
         */
        boolean canClickPhoto = false;
        boolean titleIsEmpty = StringUtils.isEmpty(shipper_title_img_url);       // 门头照是否为空
        boolean cardIsEmpty = StringUtils.isEmpty(shipper_card_img_url);     // 名片是否为空
        boolean certificateIsEmpty = StringUtils.isEmpty(shipperi_certificate_img_url);       // 机打发票是否为空
        boolean businessIsEmpty = StringUtils.isEmpty(shipper_business_img_url);     // 营业执照是否为空

        showDoorPhoto(shipper_title_img_url, shipper_title_img_key);
        showBusinessCardPhoto(shipper_card_img_url, shipper_card_img_key);
        showInvoicePhoto(shipperi_certificate_img_url, shipperi_certificate_img_key);
        showBusinessLicensePhoto(shipper_business_img_url, shipper_business_img_key);

        switch (use_status) {
            case "0":
                canClickPhoto = true;
                break;

            case "1":
                if (titleIsEmpty) {
                    ivDoorPhotoAdd.setImageResource(R.drawable.icon_approve_ban);
                } else {
                    llDoorPhotoAuthenticaioning.setVisibility(View.VISIBLE);
                }

                if (cardIsEmpty) {
                    ivBusinessCardAdd.setImageResource(R.drawable.icon_approve_ban);
                } else {
                    llBusinessCardAuthenticaioning.setVisibility(View.VISIBLE);
                }

                if (certificateIsEmpty) {
                    ivInvoiceAdd.setImageResource(R.drawable.icon_approve_ban);
                } else {
                    llInvoiceAuthenticaioning.setVisibility(View.VISIBLE);
                }

                if (businessIsEmpty) {
                    ivBusinessLicenseAdd.setImageResource(R.drawable.icon_approve_ban);
                } else {
                    llBusinessLicenseAuthenticaioning.setVisibility(View.VISIBLE);
                }
                hideAddAndHint(!titleIsEmpty, !cardIsEmpty, !certificateIsEmpty, !businessIsEmpty);

                canClickPhoto = false;
                tvSubmit.setVisibility(View.GONE);
                break;

            case "2":
                canClickPhoto = true;
                hideAddAndHint(!titleIsEmpty, !cardIsEmpty, !certificateIsEmpty, !businessIsEmpty);

                if (titleIsEmpty) {
                    rlDoorPhotoFail.setVisibility(View.GONE);
                } else {
                    rlDoorPhotoFail.setVisibility(View.VISIBLE);
                }

                if (cardIsEmpty) {
                    rlBusinessCardFail.setVisibility(View.GONE);
                } else {
                    rlBusinessCardFail.setVisibility(View.VISIBLE);
                }

                if (certificateIsEmpty) {
                    rlInvoiceFail.setVisibility(View.GONE);
                } else {
                    rlInvoiceFail.setVisibility(View.VISIBLE);
                }

                if (businessIsEmpty) {
                    rlBusinessLicenseFail.setVisibility(View.GONE);
                } else {
                    rlBusinessLicenseFail.setVisibility(View.VISIBLE);
                }
                break;

            case "3":
                hideAddAndHint(!titleIsEmpty, !cardIsEmpty, !certificateIsEmpty, !businessIsEmpty);

                if (titleIsEmpty) {
                    ivDoorPhotoAdd.setImageResource(R.drawable.icon_approve_ban);
                } else {
                    llDoorPhotoAuthenticaioning.setVisibility(View.VISIBLE);
                    tvDoorPhotoAuthenticaioning.setText(R.string.authentication_success);
                }

                if (cardIsEmpty) {
                    ivBusinessCardAdd.setImageResource(R.drawable.icon_approve_ban);
                } else {
                    llBusinessCardAuthenticaioning.setVisibility(View.VISIBLE);
                    tvBusinessCardAuthenticaioning.setText(R.string.authentication_success);
                }

                if (certificateIsEmpty) {
                    ivInvoiceAdd.setImageResource(R.drawable.icon_approve_ban);
                } else {
                    llInvoiceAuthenticaioning.setVisibility(View.VISIBLE);
                    tvInvoiceAuthenticaioning.setText(R.string.authentication_success);
                }

                if (businessIsEmpty) {
                    ivBusinessLicenseAdd.setImageResource(R.drawable.icon_approve_ban);
                } else {
                    llBusinessLicenseAuthenticaioning.setVisibility(View.VISIBLE);
                    tvBusinessLicenseAuthenticaioning.setText(R.string.authentication_success);
                }
                canClickPhoto = false;
                tvSubmit.setVisibility(View.GONE);
                break;
        }

        tvStatus.setText(use_status_remark);
        tvAddress.setEnabled(canClickPhoto);
        rlDoorPhotoFail.setEnabled(canClickPhoto);
        rlBusinessCardFail.setEnabled(canClickPhoto);
        rlInvoiceFail.setEnabled(canClickPhoto);
        rlBusinessLicenseFail.setEnabled(canClickPhoto);
        rlUploadDoorPhoto.setEnabled(canClickPhoto);
        rlUploadBusinessCard.setEnabled(canClickPhoto);
        rlUploadInvoice.setEnabled(canClickPhoto);
        rlUploadBusinessLicense.setEnabled(canClickPhoto);
    }

    /**
     * 隐藏加号和提示
     */
    private void hideAddAndHint(boolean titleIsHide, boolean cardIsHide, boolean certificateIsHide, boolean businessIsHide) {
        if (titleIsHide) {
            tvDoorPhotoHint.setVisibility(View.INVISIBLE);
            ivDoorPhotoAdd.setVisibility(View.INVISIBLE);
        }
        if (cardIsHide) {
            tvBusinessCardHint.setVisibility(View.INVISIBLE);
            ivBusinessCardAdd.setVisibility(View.INVISIBLE);
        }
        if (certificateIsHide) {
            tvInvoiceHint.setVisibility(View.INVISIBLE);
            ivInvoiceAdd.setVisibility(View.INVISIBLE);
        }
        if (businessIsHide) {
            tvBusinessLicenseHint.setVisibility(View.INVISIBLE);
            ivBusinessLicenseAdd.setVisibility(View.INVISIBLE);
        }
    }
}
