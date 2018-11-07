package com.topjet.common.common.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.topjet.common.R;
import com.topjet.common.R2;
import com.topjet.common.base.CommonProvider;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.base.view.fragment.BaseMvpFragment;
import com.topjet.common.common.modle.response.MessageCenterCountResponse;
import com.topjet.common.common.presenter.MessageCenterPresenter;
import com.topjet.common.config.CConstants;
import com.topjet.common.config.CPersisData;
import com.topjet.common.im.presenter.IMPresenter;
import com.topjet.common.message.modle.extra.MessageListExtra;
import com.topjet.common.message.view.activity.MessageListActivity;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.PermissionsUtils;
import com.topjet.common.utils.SPUtils;
import com.topjet.common.utils.Toaster;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by yy on 2017/8/14.
 * 首页 消息中心 2017/11/30 朱伦军
 */

public class MessageCenterFragment extends BaseMvpFragment<MessageCenterPresenter> implements MessageCenterView {


    @BindView(R2.id.rl_permission)
    RelativeLayout rlPermission;
    @BindView(R2.id.tv_message_num)
    TextView tvMessageNum;
    @BindView(R2.id.tv_message_order_num)
    TextView tvMessageOrderNum;
    @BindView(R2.id.tv_message_wallet_num)
    TextView tvMessageWalletNum;


    @Override
    protected void initPresenter() {
        mPresenter = new MessageCenterPresenter(this, mContext, this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_message_center;
    }

    @Override
    protected void initView(View v) {
        addIMConversation();
    }

    @Override
    protected void initData() {
        checkNoticationPermission();
        mPresenter.getHistoryMessage();
        if (!SPUtils.getBoolean(CConstants.IM_IS_LOGIN_SUCCESS, false)) {
            new IMPresenter(getActivity()).loginIM();
        }
    }

    /**
     * 判断是否有通知权限
     */
    private void checkNoticationPermission() {
        if (!PermissionsUtils.isNotificationEnabled(getActivity())) {
            //没有打开权限
            Logger.d("没有打开通知权限");
            rlPermission.setVisibility(View.VISIBLE);
        } else {
            Logger.d("打开通知权限");
            rlPermission.setVisibility(View.GONE);
        }
    }

    /**
     * 权限打开
     */
    @OnClick(R2.id.bt_open_permission)
    public void onOpenPermisionClick() {
        PermissionsUtils.goToSyetemSetting(getActivity());
    }

    /**
     * 关闭权限提示
     */
    @OnClick(R2.id.iv_close)
    public void onCloseClick() {
        rlPermission.setVisibility(View.GONE);
    }

    /**
     * 黑名单页面
     */
    @OnClick(R2.id.tv_black_list)
    public void onBlackListClick() {
        CommonProvider.getInstance().getJumpChatPageProvider().jumpBlackList((MvpActivity) getActivity());
    }

    /**
     * 系统消息
     */
    @OnClick(R2.id.rl_system)
    public void onSystemClick() {
        MessageListExtra messageListExtra = new MessageListExtra(MessageListExtra.MESSAGE_LIST_SYSTEM);
        turnToActivity(MessageListActivity.class, messageListExtra);
    }

    /**
     * 订单消息
     */
    @OnClick(R2.id.rl_order)
    public void onOrderClick() {
        MessageListExtra messageListExtra = new MessageListExtra(MessageListExtra.MESSAGE_LIST_ORDER);
        turnToActivity(MessageListActivity.class, messageListExtra);
    }

    /**
     * 钱包消息
     */
    @OnClick(R2.id.rl_wallet)
    public void onWalletClick() {
        MessageListExtra messageListExtra = new MessageListExtra(MessageListExtra.MESSAGE_LIST_WALLET);
        turnToActivity(MessageListActivity.class, messageListExtra);
    }


    /**
     * IM 聊天记录
     */
    private void addIMConversation() {
        try {
            Fragment fragment = (Fragment) Class.forName("com.easeim.ui.fragment.ConversationListFragment").newInstance();
            getChildFragmentManager().beginTransaction()
                    .add(R.id.frame_chat, fragment)
                    .commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 设置未读数
     */
    @Override
    public void setUnReadCount(MessageCenterCountResponse response) {
        if (response != null) {
            setTextCount(tvMessageNum, response.getSys_sum());
            setTextCount(tvMessageOrderNum, response.getOrder_sum());
            setTextCount(tvMessageWalletNum, response.getWalle_sum());
        }
    }

    /**
     * 设置文本
     */
    public void setTextCount(TextView text, int count) {
        if (count > 0) {
            text.setVisibility(View.VISIBLE);
            text.setText(count > 99 ? "99+" : count + "");
        } else {
            text.setVisibility(View.GONE);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if(!isHidden()){
            mPresenter.getMessageCenterCount();
        }
    }
}
