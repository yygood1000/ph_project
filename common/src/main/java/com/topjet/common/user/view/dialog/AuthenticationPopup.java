package com.topjet.common.user.view.dialog;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextPaint;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.topjet.common.R;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.utils.KeyboardUtil;
import com.topjet.common.utils.PopupAlphaAnimatorUtil;
import com.topjet.common.utils.ResourceUtils;
import com.topjet.common.utils.ScreenUtils;

import butterknife.ButterKnife;

/**
 * 认证弹出的拍照popupwindow
 */
public class AuthenticationPopup {
    private Activity activity = null;
    private PopupWindow popupWindow = null;
    private View contentView = null;

    public AuthenticationPopup(Activity activity) {
        this.activity = activity;
        new KeyboardUtil(activity).hideSoftInputFromWindow(activity);       // 隐藏输入法
    }

    /**
     * 认证弹出的拍照popupwindow
     * 不想修改提示文字，第二个参数传null即可
     * <p>
     * mode 0 显示取消按钮，不显示相册，不显示右上角的叉叉
     * mode 1 不显示取消按钮，显示右上角的叉叉，显示相册
     */
    public AuthenticationPopup initPop(String title, String prompt, int exampleResId, final OnClickListener onClickListener, int mode) {
        popupWindow = new PopupWindow(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        LayoutInflater inflater = LayoutInflater.from(activity);
        contentView = inflater.inflate(R.layout.pop_photo_authentication, null);
        TextView tvTitle = ButterKnife.findById(contentView, R.id.tv_title);// 标题
        TextView tvPrompt = ButterKnife.findById(contentView, R.id.tv_prompt);// 提示
        ImageView ivExample = ButterKnife.findById(contentView, R.id.iv_example);// 示例
        TextView tvTakePhoto = ButterKnife.findById(contentView, R.id.tv_take_photo);// 拍照
        TextView tvAlbum = ButterKnife.findById(contentView, R.id.tv_album);// 相册
        TextView tvCancel = ButterKnife.findById(contentView, R.id.tv_cancel);// 取消
        ImageView ivCancel = ButterKnife.findById(contentView, R.id.iv_cancel);// 叉叉

        if (CMemoryData.isDriver()) {
            tvTakePhoto.setBackground(ResourceUtils.getDrawable(R.drawable.selector_btn_corner_blue));
            tvAlbum.setBackground(ResourceUtils.getDrawable(R.drawable.selector_btn_corner_blue));
            tvCancel.setTextColor(ResourceUtils.getColorStateList(R.color.selector_textcolor_empty_to_blue));
            tvCancel.setBackgroundDrawable(ResourceUtils.getDrawable(R.drawable.selector_btn_empty_to_blue));
        } else {
            tvTakePhoto.setBackground(ResourceUtils.getDrawable(R.drawable.selector_btn_corner_green));
            tvAlbum.setBackground(ResourceUtils.getDrawable(R.drawable.selector_btn_corner_green));
            tvCancel.setTextColor(ResourceUtils.getColorStateList(R.color.selector_textcolor_empty_to_green));
            tvCancel.setBackgroundDrawable(ResourceUtils.getDrawable(R.drawable.selector_btn_empty_to_green));
        }

        if (mode == 1) {
            ivCancel.setVisibility(View.VISIBLE);
            tvCancel.setVisibility(View.GONE);
            tvAlbum.setVisibility(View.VISIBLE);
        }

        tvTitle.setText(title);
        TextPaint tp = tvTitle.getPaint();
        tp.setFakeBoldText(true);
        if (prompt != null) {
            tvPrompt.setText(prompt);
        }
        ivExample.setImageResource(exampleResId);// 显示对应的示例图片

        tvCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                closePop();
            }
        });
        ivCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                closePop();
            }
        });
        tvTakePhoto.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                closePop();
                onClickListener.onClick(v);
            }
        });
        tvAlbum.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                closePop();
                onClickListener.onClick(v);
            }
        });
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

    /**
     * 认证弹出的拍照popupwindow
     */
    public AuthenticationPopup initPop(String title, int exampleResId, final OnClickListener onClickListener) {
        initPop(title, null, exampleResId, onClickListener, 0);
        return this;
    }

    /**
     * 认证弹出的拍照popupwindow
     */
    public AuthenticationPopup initPop(int title, int prompt, int exampleResId, final OnClickListener onClickListener) {
        initPop(ResourceUtils.getString(title), ResourceUtils.getString(prompt), exampleResId, onClickListener, 0);
        return this;
    }

    /**
     * 认证弹出的拍照popupwindow
     */
    public AuthenticationPopup initPop(int title, int exampleResId, final OnClickListener onClickListener) {
        initPop(ResourceUtils.getString(title), null, exampleResId, onClickListener, 0);
        return this;
    }

    /**
     * 修改头像弹出的popupwindow
     * 有相册，没有取消
     * 右上角一个叉叉
     */
    public AuthenticationPopup initPopWithAlbum(int title, int exampleResId, final OnClickListener onClickListener) {
        initPop(ResourceUtils.getString(title), null, exampleResId, onClickListener, 1);
        return this;
    }

    public void showPop(View parent) {
        PopupAlphaAnimatorUtil.setInAlphaAnim(activity);
        popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);

        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) contentView.getLayoutParams();
        layoutParams.setMargins(0, 0, 0, ScreenUtils.getNavigationBarHeight(activity));
    }

    /**
     * 关闭
     */
    public void closePop() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }

//    /**
//     * PopWindow 居于底部显示的方法 showAtLocation(parent,Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
//     */
//    public void showAtLocation(View parent) {
//        popupWindow.showAtLocation(parent, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
//        // 在展示pw的同时，让窗口的其余部分显示半透明的颜色
////        WindowManager.LayoutParams params = mActivity.getWindow().getAttributes();
////        params.alpha = 0.7f;
////        mActivity.getWindow().setAttributes(params);
//    }
}
