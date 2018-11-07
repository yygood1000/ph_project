package com.topjet.common.common.view.dialog;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.foamtrace.photopicker.ImageModel;
import com.topjet.common.R;
import com.topjet.common.utils.CameraUtil;
import com.topjet.common.utils.PopupAlphaAnimatorUtil;
import com.topjet.common.utils.ScreenUtils;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * 拍照与相册选择弹窗
 * 该POP 只在发货录入备注图片是弹出
 */
public class AlbumAndCameraPopup {
    private Activity activity = null;
    private PopupWindow popupWindow = null;
    private View contentView = null;

    public AlbumAndCameraPopup(Activity activity) {
        this.activity = activity;
    }

    /**
     * 拍照、相册
     */
    public AlbumAndCameraPopup initPop(final int maxTotal, final ArrayList<ImageModel> selectedList) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        contentView = inflater.inflate(R.layout.pop_album_camera, null);
        popupWindow = new PopupWindow(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        TextView tvTakePhoto = ButterKnife.findById(contentView, R.id.tv_take_photo);// 拍照
        TextView tvAlbum = ButterKnife.findById(contentView, R.id.tv_album);// 相册
        TextView tvCancel = ButterKnife.findById(contentView, R.id.tv_cancel);// 取消

        final OnClickListener onClickListener2 = new OnClickListener() {
            @Override
            public void onClick(View v) {
                closePop();
                int id = v.getId();
                if (id == R.id.tv_take_photo) {
                    // 系统相机
                    CameraUtil.jumpToCameraGetPic(activity);
                } else if (id == R.id.tv_album) {
                    // 相册
                    CameraUtil.defaultTurnToAlbum(activity, maxTotal, selectedList);
                }
            }
        };
        tvCancel.setOnClickListener(onClickListener2);
        tvTakePhoto.setOnClickListener(onClickListener2);
        tvAlbum.setOnClickListener(onClickListener2);

        popupWindow.setContentView(contentView);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.setAnimationStyle(R.style.bottom_dialog_anim);
        popupWindow.setClippingEnabled(false);     // 可以超出屏幕，状态栏终于可以变半透明啦

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                PopupAlphaAnimatorUtil.setOutAlphaAnim(activity);
            }
        });

        return this;
    }

    public void showPop(View parent) {
        PopupAlphaAnimatorUtil.setInAlphaAnim(activity);
        popupWindow.showAtLocation(parent, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

        if (contentView != null) {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) contentView.getLayoutParams();
            layoutParams.setMargins(0, 0, 0, ScreenUtils.getNavigationBarHeight(activity));
        }
    }

    /**
     * 关闭
     */
    private void closePop() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }
}
