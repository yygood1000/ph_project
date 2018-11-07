package com.topjet.common.message.view.activity;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.topjet.common.R;
import com.topjet.common.message.modle.bean.MessageListInfo;
import com.topjet.common.message.modle.params.ReadFlagParams;
import com.topjet.common.message.modle.serverAPI.MessageCommand;
import com.topjet.common.message.modle.serverAPI.MessageCommandApi;
import com.topjet.common.message.presenter.MessagePresenter;
import com.topjet.common.message.view.adapter.SystemMessageAdapter;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.ResourceUtils;
import com.topjet.common.utils.StringUtils;
import com.topjet.common.widget.MyTitleBar;
import com.topjet.common.widget.RecyclerViewWrapper.BaseRecyclerViewActivity;

/**
 * 消息列表页面
 * Created by tsj028 on 2017/12/4 0004.
 */

public class MessageListActivity extends BaseRecyclerViewActivity<MessagePresenter,MessageListInfo> implements MessageView<MessageListInfo>{

    @Override
    protected void initPresenter() {
        mPresenter = new MessagePresenter(this, this, new MessageCommand(MessageCommandApi.class, this));
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_system_message;
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected void initTitle() {
        super.initTitle();
        getMyTitleBar()
                .setMode(MyTitleBar.Mode.BACK_TITLE)
                .setTitleText(ResourceUtils.getString(R.string.system_message));
    }
    /**
     * 设置标题
     */
    @Override
    public void setTitle(String title) {
        getMyTitleBar().setTitleText(title);
    }

    @Override
    protected void initView() {
        super.initView();
        setEmptyText(ResourceUtils.getString(R.string.message_empty));
        recyclerViewWrapper.getTvBtnEmpty().setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    @Override
    public BaseQuickAdapter getAdapter() {
        SystemMessageAdapter messageAdapter = new SystemMessageAdapter();
        messageAdapter.setmOnClickLister(new SystemMessageAdapter.OnClickLister() {
            @Override
            public void onClick(View view, MessageListInfo item) {
                if (item.getMessage_type().equals(MessageListInfo.MESSAGE_TYPE_ORDER) && StringUtils.isNotBlank(item.getRelated_id())){
                    mPresenter.jumpOrderDetail(item.getRelated_id());
                    mPresenter.setReadFlag(mPresenter.message_extra.getType(), ReadFlagParams.SETTING_TYPE_0, new String[]{item.getMessage_id()});
                }else if (item.getMessage_type().equals(MessageListInfo.MESSAGE_TYPE_WALLET)){
                    mPresenter.jumpWallet();
                    mPresenter.setReadFlag(mPresenter.message_extra.getType(), ReadFlagParams.SETTING_TYPE_0, new String[]{item.getMessage_id()});
                }
            }
        });
        return messageAdapter;
    }

    @Override
    public void loadData() {
        if (null != mPresenter.message_extra && StringUtils.isNotBlank(mPresenter.message_extra.getType())) {
            try {
                mPresenter.getMessageListData(Integer.valueOf(mPresenter.message_extra.getType()), page);
            }catch (Exception e){
                Logger.d("oye",e.toString());
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


}
