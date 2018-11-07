package com.topjet.common.order_detail.view.dialog;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.topjet.common.R;
import com.topjet.common.base.dialog.BaseDialog;

import butterknife.ButterKnife;

/**
 * creator: zhulunjun
 * time:    2017/10/26
 * describe: 分享的弹窗
 */

public class ShareDialog extends BaseDialog {
    public ShareDialog(Context context) {
        super(context, R.layout.dialog_share);
    }

    private ImageView ivWeChat, ivCircle;
    private TextView tvCancel;
    @Override
    public void initView() {
        super.initView();
        setShowBottom();
        ivWeChat = ButterKnife.findById(view, R.id.iv_wechat);
        ivCircle = ButterKnife.findById(view, R.id.iv_wechat_circle);
        tvCancel = ButterKnife.findById(view, R.id.tv_cancel);

        ivWeChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(shareClickListener != null){
                   shareClickListener.weChatClick();
                   dismiss();
               }
            }
        });
        ivCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(shareClickListener!=null){
                    shareClickListener.circleClick();
                    dismiss();
                }
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private ShareClickListener shareClickListener;

    public ShareDialog setShareClickListener(ShareClickListener shareClickListener) {
        this.shareClickListener = shareClickListener;
        return this;
    }

    public interface ShareClickListener{
        void weChatClick();
        void circleClick();
    }
}
