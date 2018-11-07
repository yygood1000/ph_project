package com.easeim.ui.fragment;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.easeim.Constant;
import com.easeim.R;
import com.easeim.domain.EmojiconExampleGroupData;
import com.easeim.ui.activity.ChatActivity;
import com.easeim.ui.activity.ContextMenuActivity;
import com.easeim.widget.ChatRowVoiceCall;
import com.foamtrace.photopicker.ImageModel;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.ui.EaseChatFragment.EaseChatFragmentHelper;
import com.hyphenate.easeui.utils.EaseUserUtils;
import com.hyphenate.easeui.widget.chatrow.EaseChatRow;
import com.hyphenate.easeui.widget.chatrow.EaseCustomChatRowProvider;
import com.hyphenate.easeui.widget.emojicon.EaseEmojiconMenu;
import com.hyphenate.util.PathUtil;
import com.topjet.common.base.CommonProvider;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.config.CConstants;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.config.CPersisData;
import com.topjet.common.db.DBManager;
import com.topjet.common.db.bean.SensitiveWordBean;
import com.topjet.common.im.model.response.SensitiveWordResponse;
import com.topjet.common.utils.CallPhoneUtils;
import com.topjet.common.utils.FileUtils;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.SPUtils;
import com.topjet.common.utils.Toaster;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ChatFragment extends EaseChatFragment implements EaseChatFragmentHelper,ChatActivity.OnChatActivityEvent {

	// constant start from 11 to avoid conflict with constant in base class
    private static final int ITEM_VIDEO = 11;
    private static final int ITEM_FILE = 12;
    private static final int ITEM_VOICE_CALL = 13;
    private static final int ITEM_VIDEO_CALL = 14;

    private static final int REQUEST_CODE_SELECT_VIDEO = 11;
    private static final int REQUEST_CODE_SELECT_FILE = 12;
    private static final int REQUEST_CODE_GROUP_DETAIL = 13;
    private static final int REQUEST_CODE_CONTEXT_MENU = 14;
    private static final int REQUEST_CODE_SELECT_AT_USER = 15;

    

    private static final int MESSAGE_TYPE_SENT_VOICE_CALL = 1;
    private static final int MESSAGE_TYPE_RECV_VOICE_CALL = 2;
    private static final int MESSAGE_TYPE_SENT_VIDEO_CALL = 3;
    private static final int MESSAGE_TYPE_RECV_VIDEO_CALL = 4;


    /**
     * if it is chatBot 
     */
    private boolean isRobot;
    private ChatActivity chatActivity;
    private EaseUser currentUser;//当前用户
    private List<SensitiveWordBean> mSenstiveWordList;//关键字列表

    private int totalUnreadMessageCount;//总的未读消息的条数
    private int currentUnreadMessageCount;//实时的未读消息的条数
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void setUpView() {
        setChatFragmentHelper(this);
        super.setUpView();
        chatActivity = (ChatActivity) getActivity();
        chatActivity.setChatActivityEvent(this);//activity 发送消息之后要在这个界面刷新UI
        //隐藏IM自带的title
        titleBar.setVisibility(View.GONE);

        //司机版和货主版的气泡显示颜色不一样，在这里判断
        if(CMemoryData.isDriver()){
            messageList.setOtherBuddleBg(R.drawable.ease_chatfrom_driver_bg);
        }else{
            messageList.setOtherBuddleBg(R.drawable.ease_chatfrom_shipper_bg);
        }
        currentUser = EaseUserUtils.getUserInfo(toChatUsername);
        //获得关键字列表
        mSenstiveWordList = DBManager.getInstance().getDaoSession().getSensitiveWordBeanDao().loadAll();
        //  给关键字列表排序
        Collections.sort(mSenstiveWordList, new SenstiveWordComparator());//通过重写Comparator的实现类

        ((EaseEmojiconMenu)inputMenu.getEmojiconMenu()).addEmojiconGroup(EmojiconExampleGroupData.getData());
        // 群聊
        if(chatType == EaseConstant.CHATTYPE_GROUP){

        }

        //未读消息数
        totalUnreadMessageCount = conversation.getUnreadMsgCount();
		if(totalUnreadMessageCount > 5) {//消息太少，进入就读完了
			chatActivity.setUnreadTextVisible(View.VISIBLE);
			String str = totalUnreadMessageCount > 99 ? "99+" : totalUnreadMessageCount+"";
            chatActivity.getUnreadText().setText(str+"条消息未读");
			messageList.getListView().setOnScrollListener(new AbsListView.OnScrollListener() {
				@Override
				public void onScrollStateChanged(AbsListView view, int scrollState) {

				}
				@Override
				public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    if(totalItemCount > 0) {
                        //计算实时未读数
                        currentUnreadMessageCount = totalUnreadMessageCount - (totalItemCount - firstVisibleItem);
                        //总的未读
                        if(currentUnreadMessageCount == 0){
                            chatActivity.setUnreadTextVisible(View.GONE);
                        }
                    }

                    Logger.d("未读消息数 "+conversation.getUnreadMsgCount()+" firstVisibleItem " +
                            ""+firstVisibleItem+" totalItemCount "+totalItemCount+" visibleItemCount " +
                            ""+visibleItemCount+" readeMessageCount "+currentUnreadMessageCount);
				}
			});
		} else {
			chatActivity.setUnreadTextVisible(View.GONE);
		}
		// 不主动设置已读
//        conversation.markAllMessagesAsRead();

    }


    /**
     * 根据长度排序， 关键字
     */
    public static class SenstiveWordComparator implements Comparator<SensitiveWordBean> {
        public int compare(SensitiveWordBean sensitiveWordBean, SensitiveWordBean sensitiveWordBean2) {
            Integer o1 = sensitiveWordBean.getWord_name().length();
            Integer o2 = sensitiveWordBean2.getWord_name().length();
            return o1.compareTo(o2);
        }
    }

    
    @Override
    protected void registerExtendMenuItem() {
        //use the menu in base class
        super.registerExtendMenuItem();
        //extend menu items
        //设置聊天底部菜单

        //视频
//        inputMenu.registerExtendMenuItem(R.string.attach_video, R.drawable.em_chat_video_selector, ITEM_VIDEO, extendMenuItemClickListener);
        //文件
//        inputMenu.registerExtendMenuItem(R.string.attach_file, R.drawable.em_chat_file_selector, ITEM_FILE, extendMenuItemClickListener);
        if(chatType == Constant.CHATTYPE_SINGLE){
            //语音通话
            inputMenu.registerExtendMenuItem(R.string.attach_voice_call, R.drawable.em_chat_voice_call_selector, ITEM_VOICE_CALL, extendMenuItemClickListener);
            //视频通话
//           inputMenu.registerExtendMenuItem(R.string.attach_video_call, R.drawable.em_chat_video_call_selector, ITEM_VIDEO_CALL, extendMenuItemClickListener);
        }

    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.d("图片选择父类"+requestCode+" "+resultCode);
        if (requestCode == REQUEST_CODE_CONTEXT_MENU) {
            switch (resultCode) {
            case ContextMenuActivity.RESULT_CODE_COPY: // copy
                clipboard.setPrimaryClip(ClipData.newPlainText(null,
                        ((EMTextMessageBody) contextMenuMessage.getBody()).getMessage()));
                break;
            case ContextMenuActivity.RESULT_CODE_DELETE: // delete
                conversation.removeMessage(contextMenuMessage.getMsgId());
                messageList.refresh();
                break;

//            case ContextMenuActivity.RESULT_CODE_FORWARD: // forward 消息转发
//                Intent intent = new Intent(getActivity(), ForwardMessageActivity.class);
//                intent.putExtra("forward_msg_id", contextMenuMessage.getMsgId());
//                startActivity(intent);
                
//                break;

            default:
                break;
            }
        }
        if(resultCode == Activity.RESULT_OK){
            switch (requestCode) {
            case REQUEST_CODE_SELECT_VIDEO: //send the video
                if (data != null) {
                    int duration = data.getIntExtra("dur", 0);
                    String videoPath = data.getStringExtra("path");
                    File file = new File(PathUtil.getInstance().getImagePath(), "thvideo" + System.currentTimeMillis());
                    try {
                        FileOutputStream fos = new FileOutputStream(file);
                        Bitmap ThumbBitmap = ThumbnailUtils.createVideoThumbnail(videoPath, 3);
                        ThumbBitmap.compress(CompressFormat.JPEG, 100, fos);
                        fos.close();
                        sendVideoMessage(videoPath, file.getAbsolutePath(), duration);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case REQUEST_CODE_SELECT_FILE: //send the file
                if (data != null) {
                    Uri uri = data.getData();
                    if (uri != null) {
                        sendFileByUri(uri);
                    }
                }
                break;
            case REQUEST_CODE_SELECT_AT_USER:
                if(data != null){
                    String username = data.getStringExtra("username");
                    inputAtUsername(username, false);
                }
                break;

            default:
                break;
            }
        }
        
    }

    @Override
    public String onFiltrateTextMessage(String text) {
        return filtrateSensitiveWord(text);
    }

    /**
     * 在这里过滤关键字
     * @param message
     * @return
     */
    private String filtrateSensitiveWord(String message){
        if(mSenstiveWordList == null)
            return message;
        long amount = CPersisData.getSensitiveWordAmount();
        for (SensitiveWordBean sensitive:mSenstiveWordList){
            if(message.contains(sensitive.getWord_name())){//如果包含关键字
                //统计出现次数
                sensitive.setWord_count(sensitive.getWord_count()+1);
                // 保存到数据库
                DBManager.getInstance().getDaoSession().getSensitiveWordBeanDao().update(sensitive);
                amount++;//总数+1
                //关键字替换成星号
                message = message.replace(sensitive.getWord_name(),getAsterisks(sensitive.getWord_name().length()));
                Logger.d("过滤关键字 "+message+" "+sensitive.getWord_name()+" "+sensitive.getWord_name());
            }
        }
        CPersisData.setSensitiveWordAmount(amount);
        return message;
    }

    /**
     * 根据传入的数字，返回星号
     * @param number 星号个数
     * @return
     */
    private String getAsterisks(int number){
        StringBuffer value =new StringBuffer();
        for (int i = 0; i < number; i++){
            value.append("*");
        }
        return value.toString();
    }

    @Override
    public void onSetMessageAttributes(EMMessage message) {
        if(isRobot){
            //set message extension
            message.setAttribute("em_robot_message", isRobot);
        }
    }
    
    @Override
    public EaseCustomChatRowProvider onSetCustomChatRowProvider() {
        return new CustomChatRowProvider();
    }
  

    @Override
    public void onEnterToChatDetails() {
//        if (chatType == Constant.CHATTYPE_GROUP) {//群组信息
//            EMGroup group = EMClient.getInstance().groupManager().getGroup(toChatUsername);
//            if (group == null) {
//                Toast.makeText(getActivity(), R.string.gorup_not_found, Toast.LENGTH_SHORT).show();
//                return;
//            }
//            startActivityForResult(
//                    (new Intent(getActivity(), GroupDetailsActivity.class).putExtra("groupId", toChatUsername)),
//                    REQUEST_CODE_GROUP_DETAIL);
//        }else if(chatType == Constant.CHATTYPE_CHATROOM){//聊天室信息
//        	startActivityForResult(new Intent(getActivity(), ChatRoomDetailsActivity.class).putExtra("roomId", toChatUsername), REQUEST_CODE_GROUP_DETAIL);
//        }
    }

    @Override
    public void onAvatarClick(String username) {
        //handling when user click avatar
        // 点击头像，去个人主页,诚信查询页
        EaseUser user = EaseUserUtils.getUserInfo(username);
        if (username.equals(EMClient.getInstance().getCurrentUser())){
            Logger.d("TTT","点击了自己的头像");
        }else {
            if (CMemoryData.isDriver()) {
                CommonProvider.getInstance().getJumpDriverProvider().jumpUserInfo((MvpActivity) getActivity(), user.getUserPhone());
            } else {
                CommonProvider.getInstance().getJumpShipperProvider().jumpUserInfo((MvpActivity) getActivity(), user.getUserPhone());
            }
        }
    }
    
    @Override
    public void onAvatarLongClick(String username) {
        inputAtUsername(username);
    }
    
    
    @Override
    public boolean onMessageBubbleClick(EMMessage message) {

        return false;
    }
    @Override
    public void onCmdMessageReceived(List<EMMessage> messages) {
        //red packet code : 处理红包回执透传消息
        for (EMMessage message : messages) {
            EMCmdMessageBody cmdMsgBody = (EMCmdMessageBody) message.getBody();
            String action = cmdMsgBody.action();//获取自定义action
//            if (action.equals(RPConstant.REFRESH_GROUP_RED_PACKET_ACTION)){
//                RedPacketUtil.receiveRedPacketAckMessage(message);
//                messageList.refresh();
//            }
        }
        //end of red packet code
        super.onCmdMessageReceived(messages);
    }

    @Override
    public void onMessageBubbleLongClick(EMMessage message) {
    	// no message forward when in chat room
        startActivityForResult((new Intent(getActivity(), ContextMenuActivity.class)).putExtra("message",message)
                .putExtra("ischatroom", chatType == EaseConstant.CHATTYPE_CHATROOM),
                REQUEST_CODE_CONTEXT_MENU);
    }

    @Override
    public boolean onExtendMenuItemClick(int itemId, View view) {
        switch (itemId) {
        case ITEM_VIDEO://发送视频
//            Intent intent = new Intent(getActivity(), ImageGridActivity.class);
//            startActivityForResult(intent, REQUEST_CODE_SELECT_VIDEO);
            break;
        case ITEM_FILE: //file
            selectFileFromLocal();
            break;
        case ITEM_VOICE_CALL://语音通话,调用本地电话
//            startVoiceCall();
//            CommonUtils.showCallPhoneDialog(getActivity(),currentUser.getUserPhone(),ChatFragment.class.getName());
            new CallPhoneUtils().showCallDialogNotUpload((MvpActivity) getActivity(), "",currentUser.getUserPhone());
            break;
        case ITEM_VIDEO_CALL://视频通话
            startVideoCall();
            break;

        default:
            break;
        }
        //keep exist extend menu
        return false;
    }
    
    /**
     * select file
     */
    protected void selectFileFromLocal() {
        Intent intent = null;
        if (Build.VERSION.SDK_INT < 19) { //api 19 and later, we can't use this way, demo just select from images
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);

        } else {
            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent, REQUEST_CODE_SELECT_FILE);
    }
    
    /**
     * make a voice call
     */
    protected void startVoiceCall() {
        if (!EMClient.getInstance().isConnected()) {
            Toast.makeText(getActivity(), R.string.not_connect_to_server, Toast.LENGTH_SHORT).show();
        } else {
//            startActivity(new Intent(getActivity(), VoiceCallActivity.class).putExtra("username", toChatUsername)
//                    .putExtra("isComingCall", false));
            // voiceCallBtn.setEnabled(false);
            inputMenu.hideExtendMenuContainer();
        }
    }
    
    /**
     * make a video call
     * 视频通话
     */
    protected void startVideoCall() {
//        if (!EMClient.getInstance().isConnected())
//            Toast.makeText(getActivity(), R.string.not_connect_to_server, Toast.LENGTH_SHORT).show();
//        else {
//            startActivity(new Intent(getActivity(), VideoCallActivity.class).putExtra("username", toChatUsername)
//                    .putExtra("isComingCall", false));
//            // videoCallBtn.setEnabled(false);
//            inputMenu.hideExtendMenuContainer();
//        }
    }

    @Override
    public void onSend() {
        refreshLast();
    }

    @Override
    public void sendMessageActivity(EMMessage message) {
        sendMessage(message);
    }

    @Override
    public void onClickUnreadRemind() {
        //跳转到指定消息位置
        Logger.d("未读消息数 "+messageList.getListView().getFirstVisiblePosition() +" "+currentUnreadMessageCount);
        int first = messageList.getListView().getFirstVisiblePosition();
        int selectionIndex = first - currentUnreadMessageCount;
        if(selectionIndex > 0){
            messageList.getListView().setSelection(selectionIndex);
        }else {
            messageList.getListView().setSelection(0);
        }
    }

    /**
     * chat row provider 
     *
     */
    private final class CustomChatRowProvider implements EaseCustomChatRowProvider {
        @Override
        public int getCustomChatRowTypeCount() {
            //here the number is the message type in EMMessage::Type
        	//which is used to count the number of different chat row
            return 10;
        }

        @Override
        public int getCustomChatRowType(EMMessage message) {
            if(message.getType() == EMMessage.Type.TXT){
                //voice call
                if (message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VOICE_CALL, false)){
                    return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VOICE_CALL : MESSAGE_TYPE_SENT_VOICE_CALL;
                }else if (message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VIDEO_CALL, false)){
                    //video call
                    return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VIDEO_CALL : MESSAGE_TYPE_SENT_VIDEO_CALL;
                }

            }
            return 0;
        }

        @Override
        public EaseChatRow getCustomChatRow(EMMessage message, int position, BaseAdapter adapter) {
            if(message.getType() == EMMessage.Type.TXT){
                // voice call or video call
                if (message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VOICE_CALL, false) ||
                    message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VIDEO_CALL, false)){
                    return new ChatRowVoiceCall(getActivity(), message, position, adapter);
                }
            }
            return null;
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
