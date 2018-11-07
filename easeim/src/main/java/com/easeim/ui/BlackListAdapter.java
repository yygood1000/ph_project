package com.easeim.ui;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easeim.R;
import com.hyphenate.easeui.utils.EaseUserUtils;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.utils.StringUtils;

import java.util.List;

/**
 * creator: zhulunjun
 * time:    2017/12/4
 * describe: 黑名单适配器
 */

public class BlackListAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public BlackListAdapter() {
        super(R.layout.ease_row_blacklist);
    }

    @Override
    protected void convert(BaseViewHolder helper, final String item) {
        EaseUserUtils.setUserNick(item, (TextView) helper.getView(R.id.tv_name));
        EaseUserUtils.setUserAvatar(mContext, item, (ImageView) helper.getView(R.id.iv_avatar));
        //判断客户端版本显示不同按钮效果
        TextView btnOut = helper.getView(R.id.tv_btn_out);
        if (CMemoryData.isDriver()) {
            btnOut.setBackgroundResource(R.drawable.em_driver_blacklist_remove_bt_bg);
        } else {
            btnOut.setBackgroundResource(R.drawable.em_shipper_blacklist_remove_bt_bg);
        }
        btnOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(blackOutClick != null){
                    blackOutClick.outClick(item);
                }
            }
        });

    }

    private BlackOutClick blackOutClick;

    public void setBlackOutClick(BlackOutClick blackOutClick) {
        this.blackOutClick = blackOutClick;
    }

    public interface BlackOutClick {
        void outClick(String item);
    }
}
