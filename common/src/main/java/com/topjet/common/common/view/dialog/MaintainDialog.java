package com.topjet.common.common.view.dialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.topjet.common.R;
import com.topjet.common.base.dialog.BaseDialog;
import com.topjet.common.common.modle.bean.MaintainBean;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.utils.AppManager;
import com.topjet.common.utils.StringUtils;

import butterknife.ButterKnife;

/**
 * creator: zhulunjun
 * time:    2018/1/8
 * describe: 停机维护
 */

public class MaintainDialog extends BaseDialog {
    private TextView tvContent, tvTitle;
    private MaintainBean mMaintainBean;
    public MaintainDialog(Context context, MaintainBean maintainBean) {
        super(context, R.layout.dialog_maintain);
        this.mMaintainBean = maintainBean;
        setData();
    }

    @Override
    public void initView() {
        super.initView();
        setWindowAnimations(R.style.right_top_out_dialog_anim);
        // 外部不能取消
        setCanceledOnTouchOutside(false);
        // 返回按钮不能取消
        setCancelable(false);
        tvContent = ButterKnife.findById(view, R.id.tv_content);
        tvTitle = ButterKnife.findById(view, R.id.tv_title);
        ButterKnife.findById(view, R.id.tv_btn_single_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CMemoryData.setShowMaintainBeanDialog(false);
                AppManager.getInstance().AppExit(mContext);

            }
        });
    }

    private void setData(){
        if(mMaintainBean != null) {
            if(StringUtils.isNotBlank(mMaintainBean.getTitle()))
                tvTitle.setText(mMaintainBean.getTitle());
            if(StringUtils.isNotBlank(mMaintainBean.getText()))
                tvContent.setText(mMaintainBean.getText());
        }
    }

}
