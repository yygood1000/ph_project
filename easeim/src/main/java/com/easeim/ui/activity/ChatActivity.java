package com.easeim.ui.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.easeim.Constant;
import com.easeim.IMHelper;
import com.easeim.R;
import com.easeim.presenter.ChatPresenter;
import com.easeim.ui.activity.view.ChatView;
import com.easeim.ui.fragment.ChatFragment;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.model.EaseOrderInfoBody;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.util.EasyUtils;
import com.topjet.common.base.busManger.Subscribe;
import com.topjet.common.base.dialog.AutoDialogHelper;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.config.CConstants;
import com.topjet.common.config.CMemoryData;

import com.topjet.common.im.ChatMenuDialog;
import com.topjet.common.im.event.MessageSendSuccessEvent;
import com.topjet.common.im.presenter.IMPresenter;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.SPUtils;
import com.topjet.common.utils.StringUtils;
import com.topjet.common.utils.Toaster;
import com.topjet.common.widget.MyTitleBar;


/**
 * chat activity，EaseChatFragment was used
 */
public class ChatActivity extends MvpActivity<ChatPresenter> implements ChatView {
    public static ChatActivity activityInstance;
    private TextView tvDepart;
    private TextView tvDestination;
    private TextView tvTruckInfo;
    private TextView tvSend;
    private TextView tvUnreadRemind;
    private LinearLayout llOrder;
    public TextView tvHaveBlackList;

    private EaseChatFragment chatFragment;
    private String toChatUsername;

    public static String ORDER_INFO = "order_info";//发送订单信息的标识
    private EaseOrderInfoBody mMessageOrderInfoExtra;//订单信息
    private ChatMenuDialog mIMMenuDialog;//菜单弹窗
    private boolean isHaveBlackList = false;


    @Override
    protected void initData() {
        super.initData();
        activityInstance = this;
        //use EaseChatFratFragment
        chatFragment = new ChatFragment();
        //pass parameters to chat fragment
        chatFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.container, chatFragment).commit();

    }

    @Override
    protected void onResume() {
        super.onResume();
        // 用户状态
        mPresenter.getUserStatus(toChatUsername);
        // 重连登录
        if(!SPUtils.getBoolean(CConstants.IM_IS_LOGIN_SUCCESS,false)){
            new IMPresenter(this).loginIM();
        }
    }

    @Override
    protected void initView() {
        tvDepart = (TextView) findViewById(R.id.tv_depart);
        tvDestination = (TextView) findViewById(R.id.tv_destination);
        tvTruckInfo = (TextView) findViewById(R.id.tv_truck_info);
        tvSend = (TextView) findViewById(R.id.tv_send);
        llOrder = (LinearLayout) findViewById(R.id.ll_order);
        tvHaveBlackList = (TextView) findViewById(R.id.tv_have_black_list);

        tvUnreadRemind = (TextView) findViewById(R.id.tv_unread_remind);
        setUnreadTextStyle();

        if (tvSend != null) {
            if (CMemoryData.isDriver()) {
                tvSend.setBackgroundResource(R.drawable.ease_send_order_driver_bg);
            } else {
                tvSend.setBackgroundResource(R.drawable.ease_send_order_shipper_bg);
            }
        }

        if (getOrderInfoExtra() == null) {
            //隐藏订单信息
            llOrder.setVisibility(View.GONE);
        } else {
            //显示订单信息
            llOrder.setVisibility(View.VISIBLE);
            setOrderInfo(mMessageOrderInfoExtra);
        }

        tvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llOrder.setVisibility(View.GONE);
                sendOrderInfo();
            }
        });

        tvUnreadRemind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvUnreadRemind.setVisibility(View.GONE);
                onActivityEvent.onClickUnreadRemind();
            }
        });

        estimateBlackList();

    }


    /**
     * 设置是否显示
     */
    public void setUnreadTextVisible(int visible) {
        if (tvUnreadRemind != null) {
            tvUnreadRemind.setVisibility(visible);
        }
    }

    public TextView getUnreadText() {
        if (tvUnreadRemind == null)
            tvUnreadRemind = (TextView) findViewById(R.id.tv_unread_remind);
        return tvUnreadRemind;
    }

    /**
     * 设置样式
     */
    private void setUnreadTextStyle() {
        Drawable drawable;
        if (CMemoryData.isDriver()) {
            tvUnreadRemind.setTextColor(getResources().getColor(R.color.remind_driver_color));
            tvUnreadRemind.setBackgroundResource(R.drawable.em_unread_remind_driver_bg);
            drawable = getResources().getDrawable(R.drawable.em_top_arrow_driver);

        } else {
            tvUnreadRemind.setTextColor(getResources().getColor(R.color.remind_shipper_color));
            tvUnreadRemind.setBackgroundResource(R.drawable.em_unread_remind_shipper_bg);
            drawable = getResources().getDrawable(R.drawable.em_top_arrow_shipper);
        }
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tvUnreadRemind.setCompoundDrawables(null, null, drawable, null);
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ChatPresenter(this,this);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.em_activity_chat;

    }


    /**
     * 判断是否已经将对方加入黑名单
     */
    private void estimateBlackList() {
        isHaveBlackList = EMClient.getInstance().contactManager().getBlackListUsernames().contains(toChatUsername);
        Logger.d("是否在黑名单中 " + isHaveBlackList);
        if (isHaveBlackList) {
            mIMMenuDialog.updateBlackText(true);
            tvHaveBlackList.setVisibility(View.VISIBLE);
        } else {
            tvHaveBlackList.setVisibility(View.GONE);
        }
    }

    /**
     * 消息发送成功的监听
     * 给在黑名单中的好友发送消息
     * 成功发送，隐藏提示
     *
     * @param event
     */
    @Subscribe
    public void onMessageSendSuccess(MessageSendSuccessEvent event){
        if(event != null && isHaveBlackList){
           mPresenter.outBlackList(toChatUsername);
        }
    }


    /**
     * 获取是否是带订单信息启动的聊天
     */
    private EaseOrderInfoBody getOrderInfoExtra() {
        mMessageOrderInfoExtra = getIntent().getParcelableExtra(ORDER_INFO);
        return mMessageOrderInfoExtra;
    }

    @Override
    protected void initTitle() {
        super.initTitle();
        //get user id or group id
        toChatUsername = getIntent().getExtras().getString(Constant.EXTRA_USER_ID);
        if (TextUtils.isEmpty(toChatUsername)) {
            return;
        }
        getRightMenuDialog();
        // 没有昵称，显示电话，没有电话，显示id
        EaseUser user = IMHelper.getInstance().getUserInfo(toChatUsername);
        String userNickname = user.getNick();
        if (TextUtils.isEmpty(userNickname.trim())) {
            userNickname = user.getUserPhone();
        }
        getMyTitleBar().setMode(MyTitleBar.Mode.BACK_TITLE_RIMG)
                .setTitleText(userNickname)
                .setRightImg(CMemoryData.isDriver()
                        ? R.drawable.iv_icon_menu_driver
                        : R.drawable.iv_icon_menu_shipper);

    }

    @Override
    protected void onClickRightImg() {
        super.onClickRightImg();
        mIMMenuDialog.show();
    }

    /**
     * 得到一个弹窗，清空聊天记录和拉入黑名单
     */
    private void getRightMenuDialog() {
        mIMMenuDialog = new ChatMenuDialog(this);
        mIMMenuDialog.setMenuClickListener(new ChatMenuDialog.MenuClickListener() {
            @Override
            public void clear() {
                clearHistoryMessage();
            }

            @Override
            public void addBlackList() {
                addBlackLists();
            }
        });
    }

    /**
     * 清空私信
     */
    private void clearHistoryMessage(){
        AutoDialogHelper.showContent(this,
                getResources().getString(R.string.Whether_to_empty_all_chats),
                new AutoDialogHelper.OnConfirmListener() {
                    @Override
                    public void onClick() {
                        //清空私信
                        mPresenter.clearHistoryMessage(toChatUsername, true);
                    }
                }).show();
    }

    /**
     * 加入黑名单
     */
    private void addBlackLists() {
        // 黑名单中只能加入15人
        int blackListSize = EMClient.getInstance().contactManager().getBlackListUsernames().size();
        if (blackListSize == CConstants.BLACK_LIST_MAX_SIZE) {
            Toaster.showShortToast(R.string.exceed_black_list_message);
            return;
        }
        if (isHaveBlackList) {
            getRemoveBlackDialog();
        } else {
            getAddBlackDialog();
        }
    }

    /**
     * 获取移除黑名单的弹窗
     */
    private void getRemoveBlackDialog() {
        AutoDialogHelper.showContent(this,
                getResources().getString(R.string.out_black_list_message),
                new AutoDialogHelper.OnConfirmListener() {
                    @Override
                    public void onClick() {
                        //移出黑名单
                        mPresenter.outBlackList(toChatUsername);
                    }
                }).show();
    }
    /**
     * 获取添加黑名单的弹窗
     */
    private void getAddBlackDialog() {
        AutoDialogHelper.showContent(this,
                getResources().getString(R.string.add_black_list_message),
                new AutoDialogHelper.OnConfirmListener() {
                    @Override
                    public void onClick() {
                        mPresenter.addBlack(toChatUsername);
                    }
                }).show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityInstance = null;

    }

    @Override
    protected void onNewIntent(Intent intent) {
        // make sure only one chat activity is opened
        String username = intent.getStringExtra("userId");
        if (toChatUsername.equals(username))
            super.onNewIntent(intent);
        else {
            finish();
            startActivity(intent);
        }

    }

    @Override
    public void onBackPressed() {
        chatFragment.onBackPressed();
        if (EasyUtils.isSingleActivity(this)) {
            finish();
        }
    }

    public String getToChatUsername() {
        return toChatUsername;
    }

    /**
     * 设置订单信息
     */
    private void setOrderInfo(EaseOrderInfoBody data) {
        Logger.d("data====================" + data.toString());
        // 出发地
        if (!StringUtils.isEmpty(data.getDepartCity())) {
            tvDepart.setText(data.getDepartCity());
        } else {
            tvDepart.setText("");
        }

        // 目的地
        if (!StringUtils.isEmpty(data.getDestinationCity())) {
            tvDestination.setText(data.getDestinationCity());
        } else {
            tvDestination.setText("");
        }

        // 车辆相关信息
        tvTruckInfo.setText(data.getTruckInfo());
    }

    /**
     * 发送订单信息
     */
    private void sendOrderInfo() {
        //创建一条扩展消息
        EMMessage message = EMMessage.createTxtSendMessage("[链接]", toChatUsername);
        message.setAttribute(EaseConstant.MESSAGE_ATTR_DEPART, mMessageOrderInfoExtra.getDepartCity());
        message.setAttribute(EaseConstant.MESSAGE_ATTR_DESTINATION, mMessageOrderInfoExtra.getDestinationCity());
        message.setAttribute(EaseConstant.MESSAGE_ATTR_TRUCK_INFO, mMessageOrderInfoExtra.getTruckInfo());
        message.setAttribute(EaseConstant.MESSAGE_ATTR_IS_ORDER_INFO, true);
        message.setAttribute(EaseConstant.MESSAGE_ATTR_GOODS_ID, mMessageOrderInfoExtra.getGoodsId());
        message.setAttribute(EaseConstant.MESSAGE_ATTR_GOODS_VERSION, mMessageOrderInfoExtra.getGoodsVersion());
        //发送消息
        onActivityEvent.sendMessageActivity(message);
        // 发送完毕要刷新列表,fragment回调刷新
        onActivityEvent.onSend();

    }

    /**
     * 移除黑名单成功
     */
    @Override
    public void removeBlackListSuccess() {
        tvHaveBlackList.setVisibility(View.GONE);
        isHaveBlackList = false;
        mIMMenuDialog.updateBlackText(false);
    }

    /**
     * 添加黑名单成功
     */
    @Override
    public void addBlackListSuccess() {
        tvHaveBlackList.setVisibility(View.VISIBLE);
        isHaveBlackList = true;
        mIMMenuDialog.updateBlackText(true);
    }

    @Override
    public void clearHistorySuccess() {
        onActivityEvent.onSend();
    }

    /**
     * 设置用户状态
     */
    @Override
    public void setUserStatus(String statusText) {
        getMyTitleBar()
                .showTitleRight()
                .setTitleRightText(statusText);
    }


    /**
     * 发送消息的监听
     * 供ChatActivity使用刷新消息列表
     */
    public interface OnChatActivityEvent {
        /**
         * 发送消息的回调
         */
        void onSend();

        /**
         * 发送消息
         */
        void sendMessageActivity(EMMessage message);

        /**
         * 点击未读提醒的回调
         */
        void onClickUnreadRemind();
    }

    private OnChatActivityEvent onActivityEvent;

    public void setChatActivityEvent(OnChatActivityEvent activityEvent) {
        this.onActivityEvent = activityEvent;
    }

}
