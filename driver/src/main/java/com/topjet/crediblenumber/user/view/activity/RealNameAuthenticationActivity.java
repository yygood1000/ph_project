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
import com.topjet.common.user.view.dialog.AuthenticationPopup;
import com.topjet.common.utils.CameraUtil;
import com.topjet.common.utils.GlideUtils;
import com.topjet.common.utils.ImageUtil;
import com.topjet.common.utils.StringUtils;
import com.topjet.common.utils.Toaster;
import com.topjet.common.widget.MyTitleBar;
import com.topjet.crediblenumber.R;
import com.topjet.crediblenumber.user.presenter.RealNameAuthenticationPresenter;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 实名认证
 * 司机版
 */
public class RealNameAuthenticationActivity extends MvpActivity<RealNameAuthenticationPresenter> implements
        RealNameAuthenticationView {
    @BindView(R.id.rl_upload_avatar)
    RelativeLayout rlUploadAvatar;
    @BindView(R.id.rl_upload_idcard)
    RelativeLayout rlUploadIdcard;
    @BindView(R.id.iv_show_avatar)
    ImageView ivShowAvatar;
    @BindView(R.id.iv_show_idcard)
    ImageView ivShowIDCard;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;      //提交按钮

    @BindView(R.id.tv_status)
    TextView tvStatus;      // 认证进度
    @BindView(R.id.ll_progress)
    LinearLayout llProgress;      // 认证进度
    @BindView(R.id.v_0000)
    View v_0000;      // 认证进度下方间隔
    @BindView(R.id.ll_avatar_authenticaioning)
    LinearLayout llAvatarAuthenticaioning;      // 审核中，头像
    @BindView(R.id.ll_idcard_authenticaioning)
    LinearLayout llIdcardAuthenticaioning;      // 审核中，身份证
    @BindView(R.id.tv_avatar_authenticaioning)
    TextView tvAvatarAuthenticaioning;      // 审核中，头像
    @BindView(R.id.rl_avatar_fail)
    RelativeLayout rlAvatarFail;      // 认证失败，重新上传，头像

    @BindView(R.id.iv_avatar_add)
    ImageView ivAvatarAdd;      // 头像加号
    @BindView(R.id.tv_avatar_hint)
    TextView tvAvatarHint;      // 头像提示
    @BindView(R.id.iv_idcard_add)
    ImageView ivIdcardAdd;      // 身份证加号
    @BindView(R.id.tv_idcard_hint)
    TextView tvIdcardHint;      // 身份证提示

    @BindView(R.id.tv_idcard_authenticaioning)
    TextView tvIdcardAuthenticaioning;      // 审核中，身份证
    @BindView(R.id.rl_idcard_fail)
    RelativeLayout rlIdcardFail;      // 认证失败，重新上传，身份证

    private int curPhotoType = 0;       //0 头像 1身份证
    private String avatarPath = null;   //头像图片地址
    private String idCardPath = null;   //身份证图片地址

    private boolean isShowMode = false; // 是否是显示模式，显示模式有返回按钮，没有“跳过”

    private GoToAuthenticationExtra goToAuthenticationExtra = new GoToAuthenticationExtra();    // 界面间传值

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_authentication_real_name;
    }

    @Override
    protected void initTitle() {
        getMyTitleBar().setMode(MyTitleBar.Mode.TITLE_RTEXT).setTitleText(R.string.real_name_authentication)
                .setRightText(R.string.skip);
    }

    @Override
    protected void initPresenter() {
        mPresenter = new RealNameAuthenticationPresenter(this, this);
    }

    @Override
    public void initView() {


        GoToAuthenticationExtra extra = getIntentExtra(GoToAuthenticationExtra.getExtraName());
        if (extra != null) {
            goToAuthenticationExtra = extra;
        }

        ivShowAvatar.post(new Runnable() {
            @Override
            public void run() {
                if (goToAuthenticationExtra != null && goToAuthenticationExtra.getInType() == GoToAuthenticationExtra
                        .IN_TYPE_SHOW) {
                    getMyTitleBar().setMode(MyTitleBar.Mode.BACK_TITLE).setTitleText(R.string.real_name_authentication);
                    isShowMode = true;

                    mPresenter.queryAuthenStatus(); // 请求网络，获取审核状态和图片地址
                    llProgress.setVisibility(View.GONE);
                    v_0000.setVisibility(View.GONE);
                }
            }
        });

    }

    @Override
    protected void onClickRightText() { // 跳过
        turnToActivityWithFinish(IdentityAuthenticationActivity.class);
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
    @OnClick({R.id.rl_upload_avatar, R.id.rl_upload_idcard, R.id.tv_submit, R.id.rl_avatar_fail, R.id.rl_idcard_fail})
    public void clickViews(View v) {
        int id = v.getId();
        if (id == R.id.rl_upload_avatar || id == R.id.rl_avatar_fail) {   // 头像
            curPhotoType = mPresenter.TYPE_AVATAR_PHOTO;
            new AuthenticationPopup(this).initPop(R.string.avatar, R.drawable.approve_head_eg, this).showPop
                    (rlUploadAvatar);
        } else if (id == R.id.rl_upload_idcard || id == R.id.rl_idcard_fail) {   // 身份证
            curPhotoType = mPresenter.TYPE_IDCARD_PHOTO;
            new AuthenticationPopup(this).initPop(R.string.id_card, R.drawable.approve_id_eg, this).showPop
                    (rlUploadIdcard);
        } else if (id == R.id.tv_submit) {
            mPresenter.addOrEditAuth(avatarPath, idCardPath);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int id = v.getId();
        if (id == R.id.tv_take_photo) {
            if (curPhotoType == mPresenter.TYPE_AVATAR_PHOTO) {
                // 前置摄像头
                CameraUtil.jumpToFrontCameraGetPic(this);
            } else if (curPhotoType == mPresenter.TYPE_IDCARD_PHOTO) {
                // 自定义相机，身份证照片
                CameraUtil.jumpToCameraGetIdCardPic(this);
            }
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (CameraUtil.REQUEST_CODE.MY_CAMERA == requestCode) { // 自定义相机
                if (data != null) {
                    if (curPhotoType == 0) {
                        avatarPath = data.getStringExtra(CameraUtil.KEY.IMG_PATH);
                        ImageUtil.toCropForSquare(this, new File(avatarPath));
                    } else {
                        idCardPath = data.getStringExtra(CameraUtil.KEY.IMG_PATH);
                        showIdCardPhoto(idCardPath, null);
                    }
                }
                if (avatarPath != null && idCardPath != null) {
                    tvSubmit.setEnabled(true);
                }
            } else if (requestCode == CameraUtil.REQUEST_CODE.SQUARE_CROP) { // 裁剪后
                if (curPhotoType == 0) {
                    avatarPath = data.getStringExtra("filePath");
                    showAvatarPhoto(avatarPath, null);
                }

                if (avatarPath != null && idCardPath != null) {
                    tvSubmit.setEnabled(true);
                }
            }
        }
    }

    @Override
    public void submitSuccess() {
        if (isShowMode) {
            Toaster.showLongToast(R.string.update_authentication_success);
            setResultAndFinish(RESULT_OK);
        } else {
            turnToActivity(IdentityAuthenticationActivity.class);
        }
    }

    /**
     * 显示头像
     */
    @Override
    public void showAvatarPhoto(String url, String key) {
        if (StringUtils.isEmpty(key)) {
            GlideUtils.loaderImageRoundWithSize(mContext, url, ivShowAvatar, 5, ivShowAvatar.getWidth(), ivShowAvatar
                    .getHeight());
        } else {
            GlideUtils.loaderImageRoundWithSize(mContext, url, key, ivShowAvatar, 5, ivShowAvatar.getWidth(),
                    ivShowAvatar.getHeight());
        }
    }

    /**
     * 显示身份证
     */
    @Override
    public void showIdCardPhoto(String url, String key) {
        if (StringUtils.isEmpty(key)) {
            GlideUtils.loaderImageRoundWithSize(mContext, url, ivShowIDCard, 5, ivShowIDCard.getWidth(), ivShowIDCard
                    .getHeight());
        } else {
            GlideUtils.loaderImageRoundWithSize(mContext, url, key, ivShowIDCard, 5, ivShowIDCard.getWidth(),
                    ivShowIDCard.getHeight());
        }
    }

    /**
     * 根据服务器参数，对界面设置
     */
    @Override
    public void setUseStatus(String useStatus, String useStatusRemark) {
        if (StringUtils.isEmpty(useStatus)) {
            return;
        }

        /**
         * 0、未认证 1、待审核 2、审核通过 3、资料修改待审核 4、认证失败
         */
        boolean canClickPhoto = false;

        switch (useStatus) {
            case "0":
                canClickPhoto = true;
                break;

            case "1":
            case "3":
                hideAddAndHint();

                llAvatarAuthenticaioning.setVisibility(View.VISIBLE);
                llIdcardAuthenticaioning.setVisibility(View.VISIBLE);
                canClickPhoto = false;
                tvSubmit.setVisibility(View.GONE);
                break;

            case "2":
                hideAddAndHint();

                llAvatarAuthenticaioning.setVisibility(View.VISIBLE);
                llIdcardAuthenticaioning.setVisibility(View.VISIBLE);
                tvAvatarAuthenticaioning.setText(R.string.authentication_success);
                tvIdcardAuthenticaioning.setText(R.string.authentication_success);
                canClickPhoto = false;
                tvSubmit.setVisibility(View.GONE);
                break;

            case "4":
                hideAddAndHint();
                canClickPhoto = true;
                rlAvatarFail.setVisibility(View.VISIBLE);
                rlIdcardFail.setVisibility(View.VISIBLE);
                break;
        }

        tvStatus.setText(useStatusRemark);

        rlAvatarFail.setEnabled(canClickPhoto);
        rlUploadAvatar.setEnabled(canClickPhoto);
        rlUploadIdcard.setEnabled(canClickPhoto);
        rlIdcardFail.setEnabled(canClickPhoto);
    }

    /**
     * 隐藏加号和提示
     */
    private void hideAddAndHint() {
        ivAvatarAdd.setVisibility(View.INVISIBLE);
        tvAvatarHint.setVisibility(View.INVISIBLE);
        ivIdcardAdd.setVisibility(View.INVISIBLE);
        tvIdcardHint.setVisibility(View.INVISIBLE);
    }

    public static void turnToRealNameActivity(MvpActivity mActivtiy, int type) {
        mActivtiy.turnToActivity(RealNameAuthenticationActivity.class, new GoToAuthenticationExtra(type));
    }
}
