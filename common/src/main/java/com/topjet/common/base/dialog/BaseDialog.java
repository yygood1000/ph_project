package com.topjet.common.base.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.topjet.common.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * creator: zhulunjun
 * time:    2017/9/13
 * describe: dialog基类
 */

public abstract class BaseDialog extends Dialog {
    public Context mContext;
    public View view;

    public BaseDialog(Context context, int resId) {
        super(context, R.style.AutoDialogTheme);
        mContext = context;
        view = View.inflate(mContext, resId, null);
        initView();
    }

    /**
     * 初始化ui
     */
    public void initView() {
        setContentView(view);
    }

    /**
     * 设置底部显示
     */
    public void setShowBottom(){
        // 拿到Dialog的Window, 修改Window的属性
        Window window = getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        // 获取Window的LayoutParams
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
        attributes.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        // 一定要重新设置, 才能生效
        window.setAttributes(attributes);
        window.setWindowAnimations(R.style.bottom_dialog_anim);
    }

    /**
     * 设置动画
     * @param resId 动画ID
     */
    public void setWindowAnimations(int resId){
        // 拿到Dialog的Window, 修改Window的属性
        Window window = getWindow();
        window.setWindowAnimations(resId);
    }
}
