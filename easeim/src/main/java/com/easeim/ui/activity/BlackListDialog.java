package com.easeim.ui.activity;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.easeim.R;
import com.topjet.common.base.dialog.BaseDialog;
import com.topjet.common.config.CMemoryData;

import butterknife.ButterKnife;

/**
 * creator: zhulunjun
 * time:    2017/12/4
 * describe: 黑名单列表弹窗
 */

public class BlackListDialog extends BaseDialog {
    public BlackListDialog(Context context) {
        super(context, R.layout.dialog_black_list);
    }

    @Override
    public void initView() {
        super.initView();

        TextView tvBtnConfirm = ButterKnife.findById(view, R.id.tv_btn_confirm);
        if(CMemoryData.isDriver()){
            tvBtnConfirm.setBackgroundResource(R.drawable.selector_btn_blue_bottom_r8);
        } else {
            tvBtnConfirm.setBackgroundResource(R.drawable.selector_btn_green_bottom_r8);
        }
        tvBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
