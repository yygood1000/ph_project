package com.topjet.common.message.view.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.topjet.common.R;
import com.topjet.common.message.modle.bean.MessageListInfo;
import com.topjet.common.utils.TimeUtils;

/**
 * 系統消息适配器
 * Created by tsj028 on 2017/12/4 0004.
 */

public class SystemMessageAdapter extends BaseQuickAdapter<MessageListInfo, BaseViewHolder> {

    private OnClickLister mOnClickLister;

    public OnClickLister getmOnClickLister() {
        return mOnClickLister;
    }

    public void setmOnClickLister(OnClickLister mOnClickLister) {
        this.mOnClickLister = mOnClickLister;
    }

    public SystemMessageAdapter() {
        super(R.layout.listitem_system_message);
    }

    @Override
    protected void convert(BaseViewHolder helper, final MessageListInfo item) {
        helper.setText(R.id.tv_time, TimeUtils.showTimeMessage(item.getCreate_time()));
        helper.setText(R.id.tv_title,  item.getTitle());
        helper.setText(R.id.tv_content,  item.getContent());
        if (item.getRead_flag().equals("1")){
            //已读消息
            helper.getView(R.id.tv_tag).setVisibility(View.INVISIBLE);
        }else{
            //未读消息
            helper.getView(R.id.tv_tag).setVisibility(View.VISIBLE);
        }
        helper.getView(R.id.ll_top).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnClickLister){
                    mOnClickLister.onClick(v, item);
                }
            }
        });
    }

    public interface OnClickLister{
        void onClick(View view, MessageListInfo item);
    }
}
