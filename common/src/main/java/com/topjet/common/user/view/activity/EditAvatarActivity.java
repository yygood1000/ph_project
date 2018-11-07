package com.topjet.common.user.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.foamtrace.photopicker.ImageModel;
import com.foamtrace.photopicker.PhotoPickerActivity;
import com.topjet.common.R;
import com.topjet.common.R2;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.user.modle.response.GetAvatarInfoResponse;
import com.topjet.common.user.presenter.EditAvatarPresenter;
import com.topjet.common.user.view.dialog.AuthenticationPopup;
import com.topjet.common.utils.CameraUtil;
import com.topjet.common.utils.GlideUtils;
import com.topjet.common.utils.ImageUtil;
import com.topjet.common.widget.MyTitleBar;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 修改头像
 * Created by tsj-004 on 2017/11/9.
 */

public class EditAvatarActivity extends MvpActivity<EditAvatarPresenter> implements EditAvatarView {
    @BindView(R2.id.iv_show_avatar)
    ImageView ivShowAvatar;
    @BindView(R2.id.tv_show_status)
    TextView tvShowStatus;
    @BindView(R2.id.tv_show_hint)
    TextView tvShowHint;
    @BindView(R2.id.tv_confirm)
    TextView tvConfirm;
    @BindView(R2.id.rl_avatar)
    RelativeLayout rlAvatar;

    private ArrayList<ImageModel> selectedImgList = new ArrayList<>();      // 选中的图片集合
    private String avatarPath = null;

    @Override
    public void initTitle() {
        getMyTitleBar()
                .setMode(MyTitleBar.Mode.BACK_TITLE)
                .setTitleText(R.string.edit_avatar);
    }

    @Override
    protected void initPresenter() {
        mPresenter = new EditAvatarPresenter(this, this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_edit_avatar;
    }

    @Override
    protected void initView() {
        resetBtnBackGround();
        mPresenter.getAvatarInfo(); // 请求网络，获取头像信息
    }

    /**
     * 设置按钮背景色
     */
    private void resetBtnBackGround() {
        if (CMemoryData.isDriver()) {
            tvConfirm.setBackgroundResource(R.drawable.selector_btn_square_blue);
        } else {
            tvConfirm.setBackgroundResource(R.drawable.selector_btn_square_green);
        }
    }

    @OnClick({R2.id.rl_avatar, R2.id.tv_confirm})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.tv_confirm) {
            mPresenter.editAvatarInfo(avatarPath);
        } else if (id == R.id.rl_avatar) {
            new AuthenticationPopup(this).initPopWithAlbum(R.string.avatar, R.drawable.approve_head_eg, this).showPop(ivShowAvatar);
        }
    }

    /**
     * popupwindow控件点击事件
     */
    @Override
    public void onClick(View v) {
        super.onClick(v);
        int id = v.getId();
        if (id == R.id.tv_take_photo) {
            CameraUtil.jumpToFrontCameraGetPic(this);// 相机
        } else if (id == R.id.tv_album) {
            CameraUtil.defaultTurnToAlbum(this, 1, selectedImgList);// 相册
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CameraUtil.REQUEST_CODE.MY_CAMERA) {
                if (data != null) {
                    avatarPath = data.getStringExtra(CameraUtil.KEY.IMG_PATH);
                    ImageUtil.toCropForSquare(this, new File(avatarPath));   // 裁剪
                }
            } else if (requestCode == CameraUtil.REQUEST_CODE.PHOTO_PICKER) {
                selectedImgList = (ArrayList<ImageModel>) data.getSerializableExtra(PhotoPickerActivity.EXTRA_RESULT);
                avatarPath = selectedImgList.get(0).getPath();

                ImageUtil.toCropForSquare(this, new File(avatarPath));   // 裁剪
            } else if (CameraUtil.REQUEST_CODE.SQUARE_CROP == requestCode) { // 裁剪后
                if (avatarPath != null) {
                    avatarPath = data.getStringExtra("filePath");
                    showAvatar(avatarPath, null);
                    tvConfirm.setEnabled(true);
                }
            }
        }
    }

    @Override
    public void showViewByParamas(GetAvatarInfoResponse response) {
        if (response != null) {
            showAvatar(response.getIcon_image_url(), response.getIcon_image_key());

            /**
             * 0、未认证 1、认证中 2、认证失败 3、认证通过
             */
            if (response.getIs_solve() != null) {
                if (response.getIs_solve().equals("1")) {
                    tvShowStatus.setVisibility(View.VISIBLE);
                }
            }

            /**
             * 0:不能修改 1:可以修改
             */
            if (response.getIs_update() != null) {
                if (response.getIs_update().equals("0")) {
                    tvConfirm.setVisibility(View.GONE);
                    rlAvatar.setEnabled(false);
                } else {
                    tvConfirm.setVisibility(View.VISIBLE);
                    rlAvatar.setEnabled(true);
                }
            }
        }
    }

    /**
     * 显示头像
     */
    public void showAvatar(String url, String key) {
        if (key == null) {
            GlideUtils.loaderImageRoundWithSize(this, url,
                    ivShowAvatar, 4, ivShowAvatar.getWidth(), ivShowAvatar.getHeight());
        } else {
            GlideUtils.loaderImageRoundWithSize(this, url, key,
                    R.drawable.icon_edit_avatar_default_photo, R.drawable.icon_edit_avatar_default_photo, ivShowAvatar, 4, ivShowAvatar.getWidth(), ivShowAvatar.getHeight());
        }
    }
}
