package com.easeim;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.easeim.domain.EmojiconExampleGroupData;
import com.easeim.receiver.CallReceiver;
import com.easeim.ui.activity.BlackActivity;
import com.easeim.ui.activity.ChatActivity;
import com.easeim.utils.PreferenceManager;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessage.ChatType;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.EaseUI.EaseEmojiconInfoProvider;
import com.hyphenate.easeui.EaseUI.EaseSettingsProvider;
import com.hyphenate.easeui.EaseUI.EaseUserProfileProvider;
import com.hyphenate.easeui.domain.EaseAvatarOptions;
import com.hyphenate.easeui.domain.EaseEmojicon;
import com.hyphenate.easeui.domain.EaseEmojiconGroupEntity;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.model.EaseAtMessageHelper;
import com.hyphenate.easeui.model.EaseNotifier;
import com.hyphenate.easeui.model.EaseNotifier.EaseNotificationInfoProvider;
import com.hyphenate.easeui.model.EaseOrderInfoBody;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.EMLog;
import com.topjet.common.base.CommonProvider;
import com.topjet.common.base.busManger.BusManager;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.config.CConstants;
import com.topjet.common.config.RespectiveData;
import com.topjet.common.db.bean.IMUserBean;
import com.topjet.common.im.event.IMMessageReceivedEvent;
import com.topjet.common.im.model.response.HistoryMessageResponse;
import com.topjet.common.im.presenter.IMPresenter;
import com.topjet.common.order_detail.modle.response.GoodsInfoResponse;
import com.topjet.common.order_detail.modle.response.OrderInfoResponse;
import com.topjet.common.utils.AppManager;
import com.topjet.common.utils.CheckUserStatusUtils;
import com.topjet.common.utils.ListUtils;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.SPUtils;
import com.topjet.common.utils.StringUtils;
import com.topjet.common.utils.Toaster;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import static com.hyphenate.easeui.EaseConstant.MESSAGE_ATTR_ICON_KEY;
import static com.hyphenate.easeui.EaseConstant.MESSAGE_ATTR_ICON_URL;
import static com.hyphenate.easeui.EaseConstant.MESSAGE_ATTR_IS_ANONYMOUS;
import static com.hyphenate.easeui.EaseConstant.MESSAGE_ATTR_SEX;
import static com.hyphenate.easeui.EaseConstant.MESSAGE_ATTR_TALK_ID;
import static com.hyphenate.easeui.EaseConstant.MESSAGE_ATTR_TALK_NAME;
import static com.hyphenate.easeui.EaseConstant.MESSAGE_ATTR_TALK_PHONE;
import static com.hyphenate.easeui.EaseConstant.MESSAGE_ATTR_USER_ID;


/**
 * 聊天控制帮助类
 * 监听
 * 数据同步等
 */
public class IMHelper {
    /**
     * data sync listener
     */
    public interface DataSyncListener {
        /**
         * sync complete
         *
         * @param success true：data sync successful，false: failed to sync data
         */
        void onSyncComplete(boolean success);
    }

    protected static final String TAG = "IMHelper";

    private EaseUI easeUI;

    /**
     * EMEventListener
     */
    protected EMMessageListener messageListener = null;

    private Map<String, EaseUser> contactList;


    private static IMHelper instance = null;

    private DateModel dateModel = null;

    /**
     * sync contacts status listener
     */
    private List<DataSyncListener> syncContactsListeners;
    /**
     * sync blacklist status listener
     */
    private List<DataSyncListener> syncBlackListListeners;

    private boolean isSyncingGroupsWithServer = false;
    private boolean isSyncingContactsWithServer = false;
    private boolean isSyncingBlackListWithServer = false;
    private boolean isGroupsSyncedWithServer = false;
    private boolean isContactsSyncedWithServer = false;
    private boolean isBlackListSyncedWithServer = false;

    private String username;

    private Context appContext;

    private CallReceiver callReceiver;


    private Context mContext;

    private IMHelper() {
    }

    public synchronized static IMHelper getInstance() {
        if (instance == null) {
            instance = new IMHelper();
        }
        return instance;
    }

    /**
     * init helper
     *
     * @param context application context
     */
    public void init(Context context) {
        this.mContext = context;
        dateModel = new DateModel(context);
        EMOptions options = initChatOptions();


        //use default options if options is null
        if (EaseUI.getInstance().init(context, options)) {
            appContext = context;
            //debug mode, you'd better set it to false, if you want release your App officially.
            EMClient.getInstance().setDebugMode(true);
            //get easeui instance
            easeUI = EaseUI.getInstance();
            //to set user's profile and avatar
            setEaseUIProviders();
            //initialize preference manager
            PreferenceManager.init(context);
            getContactList();

            setGlobalListeners();
            initJumpProvider();
            initSaveHistoryMessageProvider();
            initUserInfoProvider();

            // 获取回话中的用户信息
            getUserInfoToConversation();
        }
    }

    /**
     * 保存自己的信息
     */
    private void initUserInfoProvider() {
        CommonProvider.getInstance().setUserInfoProvider(new CommonProvider.UserInfoProvider() {
            @Override
            public void saveUserInfo(IMUserBean userBean) {
                EaseUser user = new EaseUser(userBean.getUsername());
                saveContact(user.getEaseUser(userBean));
            }
        });
    }


    private EMOptions initChatOptions() {
        Log.d(TAG, "init HuanXin Options");

        EMOptions options = new EMOptions();

        // set if accept the invitation automatically
        options.setAcceptInvitationAlways(false);
        // set if you need read ack
        options.setRequireAck(true);
        // set if you need delivery ack
        options.setRequireDeliveryAck(false);

        //you need apply & set your own id if you want to use google cloud messaging.
        options.setGCMNumber("324169311137");
        //you need apply & set your own id if you want to use Mi push notification
        options.setMipushConfig("2882303761517426801", "5381742660801");
        //you need apply & set your own id if you want to use Huawei push notification
//        options.setHuaweiPushAppId("10492024");

        //set custom servers, commonly used in private deployment
        if (dateModel.isCustomServerEnable() && dateModel.getRestServer() != null && dateModel.getIMServer() != null) {
            options.setRestServer(dateModel.getRestServer());
            options.setIMServer(dateModel.getIMServer());
            if (dateModel.getIMServer().contains(":")) {
                options.setIMServer(dateModel.getIMServer().split(":")[0]);
                options.setImPort(Integer.valueOf(dateModel.getIMServer().split(":")[1]));
            }
        }

        if (dateModel.isCustomAppkeyEnabled() && dateModel.getCutomAppkey() != null && !dateModel.getCutomAppkey().isEmpty()) {
            options.setAppKey(dateModel.getCutomAppkey());
        }

        options.allowChatroomOwnerLeave(getModel().isChatroomOwnerLeaveAllowed());
        options.setDeleteMessagesAsExitGroup(getModel().isDeleteMessagesAsExitGroup());
        options.setAutoAcceptGroupInvitation(getModel().isAutoAcceptGroupInvitation());

        return options;
    }


    protected void setEaseUIProviders() {
        //set user avatar to circle shape
        EaseAvatarOptions avatarOptions = new EaseAvatarOptions();
        avatarOptions.setAvatarShape(1);
        easeUI.setAvatarOptions(avatarOptions);

        // set profile provider if you want easeUI to handle avatar and nickname
        easeUI.setUserProfileProvider(new EaseUserProfileProvider() {

            @Override
            public EaseUser getUser(String username) {
                return getUserInfo(username);
            }
        });

        //set options 
        easeUI.setSettingsProvider(new EaseSettingsProvider() {

            @Override
            public boolean isSpeakerOpened() {
                return dateModel.getSettingMsgSpeaker();
            }

            @Override
            public boolean isMsgVibrateAllowed(EMMessage message) {
                return dateModel.getSettingMsgVibrate();
            }

            @Override
            public boolean isMsgSoundAllowed(EMMessage message) {
                return dateModel.getSettingMsgSound();
            }

            @Override
            public boolean isMsgNotifyAllowed(EMMessage message) {
                if (message == null) {
                    return dateModel.getSettingMsgNotification();
                }
                if (!dateModel.getSettingMsgNotification()) {
                    return false;
                } else {
                    String chatUsename = null;
                    List<String> notNotifyIds = null;
                    // get user or group id which was blocked to show message notifications
                    if (message.getChatType() == ChatType.Chat) {
                        chatUsename = message.getFrom();
//                        notNotifyIds = dateModel.getDisabledIds();
                    }

                    return notNotifyIds == null || !notNotifyIds.contains(chatUsename);
                }
            }
        });
        //set emoji icon provider
        easeUI.setEmojiconInfoProvider(new EaseEmojiconInfoProvider() {

            @Override
            public EaseEmojicon getEmojiconInfo(String emojiconIdentityCode) {
                EaseEmojiconGroupEntity data = EmojiconExampleGroupData.getData();
                for (EaseEmojicon emojicon : data.getEmojiconList()) {
                    if (emojicon.getIdentityCode().equals(emojiconIdentityCode)) {
                        return emojicon;
                    }
                }
                return null;
            }

            @Override
            public Map<String, Object> getTextEmojiconMapping() {
                return null;
            }
        });

        //set notification options, will use default if you don't set it
        easeUI.getNotifier().setNotificationInfoProvider(new EaseNotificationInfoProvider() {

            @Override
            public String getTitle(EMMessage message) {
                //you can update title here
//                EaseUser user = getUserInfo(message.getFrom());
                EaseUser user = getUserInfoToMessage(message);
                return user != null ? user.getNick() : message.getFrom();
            }

            @Override
            public int getSmallIcon(EMMessage message) {
                //you can update icon here
                return R.drawable.ic_launcher;
            }

            @Override
            public String getDisplayedText(EMMessage message) {
                // be used on notification bar, different text according the message type.
                String ticker = EaseCommonUtils.getMessageDigest(message, appContext);
//                EaseUser user = getUserInfo(message.getFrom());
                EaseUser user = getUserInfoToMessage(message);
                if (user != null) {
                    if (EaseAtMessageHelper.get().isAtMeMsg(message)) {
                        return String.format(appContext.getString(R.string.at_your_in_group), user.getNick());
                    }
                    return user.getNick() + ": " + ticker;
                } else {
                    if (EaseAtMessageHelper.get().isAtMeMsg(message)) {
                        return String.format(appContext.getString(R.string.at_your_in_group), message.getFrom());
                    }
                    return message.getFrom() + ": " + ticker;
                }
            }

            @Override
            public String getLatestText(EMMessage message, int fromUsersNum, int messageNum) {
                // here you can customize the text.
                // return fromUsersNum + "contacts send " + messageNum + "messages to you";
                String ticker = EaseCommonUtils.getMessageDigest(message, appContext);
                String text;
                if (messageNum > 1) {
//                    EaseUser user = getUserInfo(message.getFrom());
                    EaseUser user = getUserInfoToMessage(message);
                    String name = user != null ? user.getNick() : message.getFrom();
                    text = "[" + messageNum + "条] " + name + "：" + ticker;
                } else {
                    text = ticker;
                }
                return text;
            }

            @Override
            public Intent getLaunchIntent(EMMessage message) {
                // you can set what activity you want display when user click the notification
                Intent intent;
                //检查是否可以聊天
                if (!checkChat(appContext, new EaseUser(message.getFrom()))) {
                    // 没有权限聊天就跳主页
                    intent = new Intent(appContext, RespectiveData.getMainActivityClass());
                } else {
                    intent = new Intent(appContext, ChatActivity.class);
                    intent.putExtra("chatType", Constant.CHATTYPE_SINGLE);
                    intent.putExtra(EaseConstant.EXTRA_USER_ID, message.getFrom());
                }

                // open calling activity if there is call
                return intent;
            }
        });

        // 保存下拉刷新传过来的历史消息
        easeUI.setHistorySaveProvider(new EaseUI.HistorySaveProvider() {
            @Override
            public List<EMMessage> saveHistoryMessage(HistoryMessageResponse response) {
                try {
                    return HistoryMessageHelper.getInstance().saveHistoryMessages(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return new ArrayList<>();
            }
        });
    }

    EMConnectionListener connectionListener;

    /**
     * set global listener
     */
    protected void setGlobalListeners() {
        syncContactsListeners = new ArrayList<DataSyncListener>();
        syncBlackListListeners = new ArrayList<DataSyncListener>();

        isGroupsSyncedWithServer = dateModel.isGroupsSynced();
        isContactsSyncedWithServer = dateModel.isContactSynced();
        isBlackListSyncedWithServer = dateModel.isBacklistSynced();

        // create the global connection listener
        connectionListener = new EMConnectionListener() {
            @Override
            public void onDisconnected(int error) {
                EMLog.d("global listener", "onDisconnect" + error);
                if (error == EMError.USER_REMOVED) {
                    onUserException(Constant.ACCOUNT_REMOVED);
                } else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                    EMClient.getInstance().logout(true);
                    onUserException(Constant.ACCOUNT_CONFLICT);
                } else if (error == EMError.SERVER_SERVICE_RESTRICTED) {
                    onUserException(Constant.ACCOUNT_FORBIDDEN);
                }
            }

            @Override
            public void onConnected() {
                // in case group and contact were already synced, we supposed to notify sdk we are ready to receive the events
                if (isGroupsSyncedWithServer && isContactsSyncedWithServer) {
                    EMLog.d(TAG, "group and contact already synced with servre");
                } else {
                    if (!isContactsSyncedWithServer) {
                        asyncFetchContactsFromServer(null);
                    }

                    if (!isBlackListSyncedWithServer) {
                        asyncFetchBlackListFromServer(null);
                    }
                }
            }
        };

        if (callReceiver == null) {
            callReceiver = new CallReceiver();
        }


        //register connection listener
        EMClient.getInstance().addConnectionListener(connectionListener);
        //register message event listener
        registerMessageListener();

    }

    /**
     * user met some exception: conflict, removed or forbidden
     * 聊天出现错误的处理
     */
    protected void onUserException(String exception) {
        EMLog.e(TAG, "onUserException: " + exception);
        // TODO: 2017/7/11 聊天出现错误，需要跳转主界面，或者其他处理
//        Intent intent = new Intent(appContext, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.putExtra(exception, true);
//        appContext.startActivity(intent);
    }

    public EaseUser getUserInfo(String username) {
        // To get instance of EaseUser, here we get it from the user list in memory
        // You'd better cache it if you get it from your server
        // TODO: 2017/7/11 获取个人信息，本地的，处理后设置
        EaseUser user = getContactList().get(username);

        if (user == null) {
            user = new EaseUser(username);
            getUserInfoToConversation(username);
            EaseCommonUtils.setUserInitialLetter(user);
        }

        Logger.d("用户信息 getUserInfo " + user.getUsername() + " " + user.getAvatar() + " " + user.getNick() + " " + user.getUserId());
        return user;
    }

    /**
     * 判断回话中用户信息是否完善
     * 从回话中获取用户信息
     */
    private void getUserInfoToConversation(){
        Map<String, EMConversation> conversationList = EMClient.getInstance().chatManager().getAllConversations();
        if(conversationList != null && conversationList.size() > 0){
            for (EMConversation conversation : conversationList.values()){
                getUserInfo(conversation.conversationId());
            }
        }

    }

    /**
     * 从回话中获取用户信息
     * @param conversationId 回话id
     */
    private void getUserInfoToConversation(String conversationId){
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(conversationId);
        if(conversation != null){
            List<EMMessage> messageList = conversation.getAllMessages();
            if(!ListUtils.isEmpty(messageList)){
                for (EMMessage message : messageList){
                    getUserInfoToMessage(message);
                }
            }
        }
    }

    /**
     * 从消息中获取用户信息
     * 保存到本地
     */
    private EaseUser getUserInfoToMessage(EMMessage message) {
        EaseUser user = getContactList().get(message.getFrom());
        if(user != null) {
            Logger.d("用户信息 接收到  " + message.getFrom() + " " + user.getAvatar() + " " + user.getNick());
        }
        //本地没有，才从消息中取,没有头像也需要重新更新,变成匿名了也保存
        if (user == null || StringUtils.isEmpty(user.getAvatar()) || StringUtils.isEmpty(user.getNick())) {

            user = new EaseUser(message.getFrom());
            user.setNickname(message.getStringAttribute(MESSAGE_ATTR_TALK_NAME, null));
            user.setUserPhone(message.getStringAttribute(MESSAGE_ATTR_TALK_PHONE, null));
            user.setIsAnonymous(message.getStringAttribute(MESSAGE_ATTR_IS_ANONYMOUS, "0"));
            user.setAvatar(message.getStringAttribute(MESSAGE_ATTR_ICON_URL, null));
            user.setAvatarKey(message.getStringAttribute(MESSAGE_ATTR_ICON_KEY, null));
            user.setSex(message.getStringAttribute(MESSAGE_ATTR_SEX, EaseUser.MAN));
            user.setUserId(message.getStringAttribute(MESSAGE_ATTR_USER_ID, null));
            user.setUserName(message.getStringAttribute(MESSAGE_ATTR_TALK_ID, null));

            Logger.d("用户信息 接收到的name " + user.getNickname());
            saveContact(user);
        }
        return user;
    }


    /**
     * Global listener
     * If this event already handled by an activity, you don't need handle it again
     * activityList.size() <= 0 means all activities already in background or not in Activity Stack
     */
    protected void registerMessageListener() {
        messageListener = new EMMessageListener() {
            private BroadcastReceiver broadCastReceiver = null;

            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                for (EMMessage message : messages) {
                    EMLog.d(TAG, "onMessageReceived id : " + message.getMsgId());
                    // in background, do not refresh UI, notify it in notification bar
                    // 如果chatActivity 不在前台，不在栈顶 则发送通知
                    Logger.d("onMessageReceived " + AppManager.getInstance().getTopActivity().getClass().toString());
                    if (!(AppManager.getInstance().getTopActivity().getClass().toString().equals("class com.easeim.ui.activity.ChatActivity"))) {
                        getNotifier().onNewMsg(message);
                    }
//                    if(!easeUI.hasForegroundActivies()){
//                        getNotifier().onNewMsg(message);
//                    }

                    getUserInfoToMessage(message);
                    BusManager.getBus().post(new IMMessageReceivedEvent());
                }
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> messages) {
                for (EMMessage message : messages) {

                    //TODO 透传消息 在这里处理
                    EMLog.d(TAG, "receive command message");
                    //get message body
                    EMCmdMessageBody cmdMsgBody = (EMCmdMessageBody) message.getBody();
                    final String action = cmdMsgBody.action();//获取自定义action

                    //获取扩展属性 此处省略
                    //maybe you need get extension of your message
                    //message.getStringAttribute("");
                    EMLog.d(TAG, String.format("Command：action:%s,message:%s", action, message.toString()));
                }
            }

            @Override
            public void onMessageRead(List<EMMessage> messages) {
            }

            @Override
            public void onMessageDelivered(List<EMMessage> message) {
            }

            @Override
            public void onMessageRecalled(List<EMMessage> list) {

            }

            @Override
            public void onMessageChanged(EMMessage message, Object change) {
                EMLog.d(TAG, "change:" + change);
            }
        };

        EMClient.getInstance().chatManager().addMessageListener(messageListener);
    }

    /**
     * if ever logged in
     *
     * @return
     */
    public boolean isLoggedIn() {
        return EMClient.getInstance().isLoggedInBefore();
    }

    /**
     * logout
     *
     * @param unbindDeviceToken whether you need unbind your device token
     * @param callback          callback
     */
    public void logout(boolean unbindDeviceToken, final EMCallBack callback) {
        Log.d(TAG, "logout: " + unbindDeviceToken);
        EMClient.getInstance().logout(unbindDeviceToken, new EMCallBack() {

            @Override
            public void onSuccess() {
                Log.d(TAG, "logout: onSuccess");
                reset();
                if (callback != null) {
                    callback.onSuccess();
                }

            }

            @Override
            public void onProgress(int progress, String status) {
                if (callback != null) {
                    callback.onProgress(progress, status);
                }
            }

            @Override
            public void onError(int code, String error) {
                Log.d(TAG, "logout: onSuccess");
                reset();
                if (callback != null) {
                    callback.onError(code, error);
                }
            }
        });
    }

    /**
     * get instance of EaseNotifier
     *
     * @return
     */
    public EaseNotifier getNotifier() {
        return easeUI.getNotifier();
    }

    public DateModel getModel() {
        return (DateModel) dateModel;
    }

    /**
     * update contact list
     *
     * @param aContactList
     */
    public void setContactList(Map<String, EaseUser> aContactList) {
        if (aContactList == null) {
            if (contactList != null) {
                contactList.clear();
            }
            return;
        }

        contactList = aContactList;
    }

    /**
     * save single contact
     */
    public void saveContact(EaseUser user) {
        if (null == contactList) {
            contactList = dateModel.getContactList();
            return;
        }
        if (StringUtils.isNotBlank(user.getUsername())) {
            contactList.put(user.getUsername(), user);//CMemoryData.isDriver()?"20"+user.getUsername():"10"+user.getUsername()
            dateModel.saveContact(user);
        }
    }

    /**
     * get contact list
     *
     * @return
     */
    public Map<String, EaseUser> getContactList() {
        if (isLoggedIn() && contactList == null) {
            contactList = dateModel.getContactList();
        }

        // return a empty non-null object to avoid app crash
        if (contactList == null) {
            return new Hashtable<String, EaseUser>();
        }


        return contactList;
    }


    /**
     * update user list to cache and database
     *
     * @param contactInfoList
     */
    public void updateContactList(List<EaseUser> contactInfoList) {
        for (EaseUser u : contactInfoList) {
            contactList.put(u.getUsername(), u);
        }
        ArrayList<EaseUser> mList = new ArrayList<EaseUser>();
        mList.addAll(contactList.values());
        dateModel.saveContactList(mList);
    }


    public void asyncFetchContactsFromServer(final EMValueCallBack<List<String>> callback) {
        if (isSyncingContactsWithServer) {
            return;
        }

        isSyncingContactsWithServer = true;

        new Thread() {
            @Override
            public void run() {
                List<String> usernames = null;
                try {
                    usernames = EMClient.getInstance().contactManager().getAllContactsFromServer();
                    // in case that logout already before server returns, we should return immediately
                    if (!isLoggedIn()) {
                        isContactsSyncedWithServer = false;
                        isSyncingContactsWithServer = false;
                        notifyContactsSyncListener(false);
                        return;
                    }

                    Map<String, EaseUser> userlist = new HashMap<String, EaseUser>();
                    for (String username : usernames) {
                        EaseUser user = new EaseUser(username);
                        EaseCommonUtils.setUserInitialLetter(user);
                        userlist.put(username, user);
                    }
                    // save the contact list to cache
                    getContactList().clear();
                    getContactList().putAll(userlist);
                    // save the contact list to database
//                   UserDao dao = new UserDao(appContext);
                    List<EaseUser> users = new ArrayList<EaseUser>(userlist.values());
//                   dao.saveContactList(users);

                    dateModel.setContactSynced(true);
                    EMLog.d(TAG, "set contact syn status to true");

                    isContactsSyncedWithServer = true;
                    isSyncingContactsWithServer = false;

                    //notify sync success
                    notifyContactsSyncListener(true);


                    if (callback != null) {
                        callback.onSuccess(usernames);
                    }
                } catch (HyphenateException e) {
                    dateModel.setContactSynced(false);
                    isContactsSyncedWithServer = false;
                    isSyncingContactsWithServer = false;
                    notifyContactsSyncListener(false);
                    e.printStackTrace();
                    if (callback != null) {
                        callback.onError(e.getErrorCode(), e.toString());
                    }
                }

            }
        }.start();
    }

    public void notifyContactsSyncListener(boolean success) {
        for (DataSyncListener listener : syncContactsListeners) {
            listener.onSyncComplete(success);
        }
    }

    public void asyncFetchBlackListFromServer(final EMValueCallBack<List<String>> callback) {

        if (isSyncingBlackListWithServer) {
            return;
        }

        isSyncingBlackListWithServer = true;

        new Thread() {
            @Override
            public void run() {
                try {
                    List<String> usernames = EMClient.getInstance().contactManager().getBlackListFromServer();

                    // in case that logout already before server returns, we should return immediately
                    if (!isLoggedIn()) {
                        isBlackListSyncedWithServer = false;
                        isSyncingBlackListWithServer = false;
                        notifyBlackListSyncListener(false);
                        return;
                    }

                    dateModel.setBlacklistSynced(true);

                    isBlackListSyncedWithServer = true;
                    isSyncingBlackListWithServer = false;

                    notifyBlackListSyncListener(true);
                    if (callback != null) {
                        callback.onSuccess(usernames);
                    }
                } catch (HyphenateException e) {
                    dateModel.setBlacklistSynced(false);

                    isBlackListSyncedWithServer = false;
                    isSyncingBlackListWithServer = true;
                    e.printStackTrace();

                    if (callback != null) {
                        callback.onError(e.getErrorCode(), e.toString());
                    }
                }

            }
        }.start();
    }

    public void notifyBlackListSyncListener(boolean success) {
        for (DataSyncListener listener : syncBlackListListeners) {
            listener.onSyncComplete(success);
        }
    }

    public boolean isSyncingGroupsWithServer() {
        return isSyncingGroupsWithServer;
    }

    public boolean isSyncingContactsWithServer() {
        return isSyncingContactsWithServer;
    }

    public boolean isSyncingBlackListWithServer() {
        return isSyncingBlackListWithServer;
    }

    public boolean isGroupsSyncedWithServer() {
        return isGroupsSyncedWithServer;
    }

    public boolean isContactsSyncedWithServer() {
        return isContactsSyncedWithServer;
    }

    public boolean isBlackListSyncedWithServer() {
        return isBlackListSyncedWithServer;
    }

    synchronized void reset() {
        isSyncingGroupsWithServer = false;
        isSyncingContactsWithServer = false;
        isSyncingBlackListWithServer = false;

        dateModel.setGroupsSynced(false);
        dateModel.setContactSynced(false);
        dateModel.setBlacklistSynced(false);

        isGroupsSyncedWithServer = false;
        isContactsSyncedWithServer = false;
        isBlackListSyncedWithServer = false;

        setContactList(null);
    }

    public void pushActivity(Activity activity) {
        easeUI.pushActivity(activity);
    }

    public void popActivity(Activity activity) {
        easeUI.popActivity(activity);
    }


    /**
     * 统一的聊天入口
     *
     * @param context   从哪个页面跳转的
     * @param user      聊天的用户信息
     * @param orderInfo 需要带入的订单信息，可以传空
     */
    public void startSingleChat(final Context context, final EaseUser user, final EaseOrderInfoBody orderInfo) {
        // 1. 实名认证通过才能聊天
        CheckUserStatusUtils.isRealNameAuthentication((MvpActivity) context,
                new CheckUserStatusUtils.OnJudgeResultListener() {
                    @Override
                    public void onSucceed() {
                        try {
                            // 检查是否可以聊天
                            if (!checkChat(context, user)) {
                                return;
                            }
                            Intent intent = new Intent(context, ChatActivity.class);
                            Logger.d("启动聊天", user.getUsername());
                            intent.putExtra(Constant.EXTRA_USER_ID, user.getUsername());
                            if (orderInfo != null) {
                                //订单信息
                                intent.putExtra(ChatActivity.ORDER_INFO, orderInfo);
                            }
                            context.startActivity(intent);
                        } catch (Exception e) {
                            Logger.d("聊天错误 " + e.getMessage());
                        }
                    }
                });

    }

    /**
     * * 1. 实名认证通过才能聊天
     * 2. 登录失败的情况，提示并且再次登录
     * 3. 用户id为空的情况
     * 4. 保存用户信息到本地数据库
     * 5. 不能和自己聊天
     */
    public boolean checkChat(Context context, EaseUser user) {

        boolean isLoginSuccess = SPUtils.getBoolean(CConstants.IM_IS_LOGIN_SUCCESS, false);
        boolean isUserEmpty = SPUtils.getBoolean(CConstants.IM_USER_EMPTY, false);
        // 用户不存在的情况，由于公司账户是体验账户，一天只能注册100人，所以可能存在用户不存在的情况
        if(isUserEmpty){
            Toaster.showLongToast(context.getString(R.string.im_error_user_empty_msg));
            new IMPresenter(context).loginIM();
            return false;
        }
        // 2. 登录失败的情况，提示并且再次登录
        if (!isLoginSuccess) {
            Toaster.showLongToast(context.getString(R.string.im_error_msg));
            new IMPresenter(context).loginIM();
            return false;
        }
        // 3. 用户id为空的情况
        if (user == null || TextUtils.isEmpty(user.getUsername())) {
            Toaster.showLongToast("用户id为空，无法聊天");
            return false;
        }
        // 4. 保存用户信息到本地数据库
        // 只传用户ID的情况不更新本地数据
        Logger.d("用户信息 checkChat " + user.getAvatar() + " " + user.getNick());
        if ((StringUtils.isNotBlank(user.getAvatar()) && !user.getAvatar().equals("null")) ||
                (StringUtils.isNotBlank(user.getNick()) && !user.getNick().equals(user.getUsername()))) {
            Logger.d("用户信息 checkChat " + user.getUsername());
            saveContact(user);
        }
        // 5. 不能和自己聊天

        if (user.getUsername().equals(EMClient.getInstance().getCurrentUser())) {
            Toaster.showLongToast(R.string.Cant_chat_with_yourself);
            return false;
        }
        return true;
    }

    /**
     * 不带订单信息的聊天
     *
     * @param context
     * @param user
     */
    public void startSingleChat(Context context, EaseUser user) {
        startSingleChat(context, user, null);
    }

    /**
     * 只传userId的聊天
     *
     * @param context
     * @param username
     */
    public void startSingleChat(Context context, String username) {
        startSingleChat(context, new EaseUser(username), null);
    }

    /**
     * 初始化跳转回调
     */
    private void initJumpProvider() {
        CommonProvider.getInstance().setJumpChatPageProvider(new CommonProvider.JumpChatPageProvider() {
            @Override
            public void jumpChatPage(Context context, IMUserBean userBean, OrderInfoResponse orderInfo) {
                EaseUser user = new EaseUser(userBean.getUsername());
                EaseOrderInfoBody body = new EaseOrderInfoBody();
                startSingleChat(context, user.getEaseUser(userBean), body.getOrderBody(orderInfo));
            }

            @Override
            public void jumpChatPage(Context context, IMUserBean userBean, GoodsInfoResponse orderInfo) {
                EaseUser user = new EaseUser(userBean.getUsername());
                EaseOrderInfoBody body = new EaseOrderInfoBody();
                startSingleChat(context, user.getEaseUser(userBean), body.getOrderBody(orderInfo));
            }

            @Override
            public void jumpChatPage(Context context, IMUserBean userBean) {
                EaseUser user = new EaseUser(userBean.getUsername());
                startSingleChat(context, user.getEaseUser(userBean));
            }

            @Override
            public void jumpChatPage(Context context, String username) {
                startSingleChat(context, username);
            }

            @Override
            public void jumpBlackList(MvpActivity activity) {
                activity.turnToActivity(BlackActivity.class);
            }


        });
    }

    /**
     * 保存历史聊天记录
     */
    private void initSaveHistoryMessageProvider() {
        CommonProvider.getInstance().setHistoryMessageProvider(new CommonProvider.HistoryMessageProvider() {
            @Override
            public void saveHistoryMessage(HistoryMessageResponse response) {
                try {
                    HistoryMessageHelper.getInstance().saveHistoryMessages(response);
                    // 刷新列表
                    BusManager.getBus().post(new IMMessageReceivedEvent());
                } catch (JSONException e) {
                    Logger.d("保存聊天历史记录 失败" + e);
                    e.printStackTrace();
                }
            }
        });
    }

}
