package com.topjet.common.common.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.topjet.common.R;
import com.topjet.common.config.CMemoryData;

/**
 * Created by tsj-004 on 2017/8/3.
 * 网络请求进度框
 */

public class NetRequestDialog extends Dialog {
    private ImageView iv_progress_net_request = null;
    private Animation rotate = null;    //动画

    public NetRequestDialog(final Context context) {
        super(context, R.style.NetRequestDialog);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View conentView = inflater.inflate(R.layout.dialog_netrequest, null);
        // 设置SelectPicPopupWindow的View
        this.setContentView(conentView);

        // 取消弹出
//        Window window = this.getWindow();
//        WindowManager.LayoutParams lp = window.getAttributes();
//        lp.dimAmount = 0f;
//        window.setAttributes(lp);
//        window.setBackgroundDrawable(null);
        // mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        // 设置SelectPicPopupWindow弹出窗体动画效果
//        this.setAnimationStyle(R.style.AnimationPreview);
        iv_progress_net_request = (ImageView) conentView
                .findViewById(R.id.iv_progress_net_request);

        if (CMemoryData.isDriver()) {
            iv_progress_net_request.setImageResource(R.drawable.iv_loading_progress_driver);
        }

        rotate = AnimationUtils.loadAnimation(context, R.anim.refresh_header_rotate);
//        this.setCancelable(false);
        setCanceledOnTouchOutside(false);
        setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                ((Activity) context).finish();
            }
        });
    }

    /**
     * 开始动画
     */
    public void startAnimation() {
        iv_progress_net_request.startAnimation(rotate);
    }

    /**
     * 结束动画
     */
    public void stopAnimation() {
        iv_progress_net_request.clearAnimation();
    }

    public void show() {
        if (!this.isShowing()) {
            super.show();
            startAnimation();
        }
    }

    public void dismiss() {
        if (this.isShowing()) {
            stopAnimation();
            super.dismiss();
        }
    }
}