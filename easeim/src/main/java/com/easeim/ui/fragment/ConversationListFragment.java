package com.easeim.ui.fragment;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.easeim.IMHelper;
import com.easeim.presenter.ChatPresenter;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.topjet.common.base.busManger.BusManager;
import com.topjet.common.base.busManger.Subscribe;
import com.topjet.common.base.dialog.AutoDialogHelper;
import com.topjet.common.im.event.IMMessageReceivedEvent;
import com.topjet.common.utils.Logger;

/**
 * 历史聊天记录
 * 被common中的MsgCenterActivity反射调用
 */
public class ConversationListFragment extends EaseConversationListFragment {


    @Override
    protected void initView() {
        super.initView();
        this.titleBar.setVisibility(View.GONE);
        BusManager.getBus().register(this);
    }
    
    @Override
    protected void setUpView() {
        super.setUpView();
        // register context menu
        registerForContextMenu(conversationListView);
        //点击事件
        conversationListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    EMConversation conversation = conversationListView.getItem(position);
                    String username = conversation.conversationId();
                    IMHelper.getInstance().startSingleChat(getActivity(),username.substring(2,username.length()));
            }
        });
        conversationListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long id) {
                final EMConversation tobeDeleteCons = conversationListView.getItem(position);
                AutoDialogHelper.showContent(getActivity(), "确定删除此对话？", new AutoDialogHelper.OnConfirmListener() {
                    @Override
                    public void onClick() {
                        //删除和某个user会话，如果需要保留聊天记录，传false
                        try {
                            new ChatPresenter(getActivity()).clearHistoryMessage(tobeDeleteCons.conversationId(), false);
                            EMClient.getInstance().chatManager().deleteConversation(tobeDeleteCons.conversationId(), true);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        refresh();
                    }
                }).show();
                return true;
            }
        });

        super.setUpView();
    }


    @Override
    public void onResume() {
        super.onResume();
        Logger.d("TTT","===onResume=getHistoryMessage==");
//        imLogic = new IMLogic(getActivity());
//        imLogic.getHistoryMessage("V3_MsgCenterActivity");
    }

    @Override
    protected void onConnectionDisconnected() {
        super.onConnectionDisconnected();
//        if (NetUtils.hasNetwork(getActivity())){
//         errorText.setText(R.string.can_not_connect_chat_server_connection);
//        } else {
//          errorText.setText(R.string.the_current_network);
//        }
    }

    /**
     *接收到新聊天消息刷新列表
     */
    @Subscribe
    public void onEventMainThread(IMMessageReceivedEvent e) {
        conversationListView.post(new Runnable() {
            @Override
            public void run() {
                refresh();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BusManager.getBus().unregister(this);
    }

}
