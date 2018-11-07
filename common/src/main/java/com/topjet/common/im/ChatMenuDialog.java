package com.topjet.common.im;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.hyphenate.util.DensityUtil;
import com.topjet.common.R;
import com.topjet.common.base.dialog.BaseDialog;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.utils.ResourceUtils;

/**
 * creator: zhulunjun
 * time:    2017/11/30
 * describe: 聊天界面右上角点击弹窗
 * 1. 清空聊天记录
 * 2. 拉入黑名单
 */

public class ChatMenuDialog extends BaseDialog implements View.OnClickListener{
    private TextView tvCancel;
    private TextView tvBlack;
    private TextView tvBlackMsg;

    public ChatMenuDialog(Context context) {
        super(context, R.layout.dialog_chat_menu);
    }

    @Override
    public void initView() {
        super.initView();
        setShowBottom();
        tvBlack = (TextView) view.findViewById(R.id.tv_black);
        tvBlackMsg = (TextView) view.findViewById(R.id.tv_black_msg);
        view.findViewById(R.id.ll_add_black_list).setOnClickListener(this);
        view.findViewById(R.id.tv_clear_message).setOnClickListener(this);
        view.findViewById(R.id.tv_cancel).setOnClickListener(this);

        tvCancel = (TextView) view.findViewById(R.id.tv_cancel);
        if (!CMemoryData.isDriver()) {
            tvCancel.setTextColor(ResourceUtils.getColor(R.color.v3_common_green));
        }
    }

    /**
     * 加入黑名单之后，更新显示
     * @param isBlack
     */
    public void updateBlackText(boolean isBlack){
        int padding;
        if(isBlack) {
            tvBlack.setText(R.string.out_black_list);
            tvBlackMsg.setVisibility(View.GONE);
            padding = DensityUtil.dip2px(mContext,8);
            tvBlack.setTextColor(Color.parseColor("#FF5E5E"));
        } else {
            tvBlack.setText(R.string.add_black_list);
            tvBlackMsg.setVisibility(View.VISIBLE);
            padding = DensityUtil.dip2px(mContext,0);
            tvBlack.setTextColor(Color.parseColor("#FF5E5E"));
        }
        tvBlack.setPadding(padding,padding,padding,padding);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ll_add_black_list) {
            if(menuClickListener != null)
                menuClickListener.addBlackList();
        } else if (v.getId() == R.id.tv_clear_message) {
            if(menuClickListener != null)
                menuClickListener.clear();
        } else if (v.getId() == R.id.tv_cancel) {

        }
        dismiss();
    }

    /**
     * 弹窗点击监听
     */
    public interface MenuClickListener {
        void clear();
        void addBlackList();
    }

    private MenuClickListener menuClickListener;

    public void setMenuClickListener(MenuClickListener menuClickListener) {
        this.menuClickListener = menuClickListener;
    }
}
