package com.easeim;

import android.text.TextUtils;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMLocationMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.chat.EMVoiceMessageBody;
import com.hyphenate.chat.adapter.message.EMAImageMessageBody;
import com.hyphenate.chat.adapter.message.EMAVoiceMessageBody;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.util.PathUtil;
import com.topjet.common.im.model.bean.HistoryMessageBean;
import com.topjet.common.im.model.bean.HistoryUserBean;
import com.topjet.common.im.model.response.HistoryMessageResponse;
import com.topjet.common.utils.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * creator: zhulunjun
 * time:    2017/12/7
 * describe: 历史消息处理
 * 1. 处理从服务器拿到的历史消息，保存的环信的数据库
 * 2. 获取从服务器拿到的用户信息，保存到环信
 */

public class HistoryMessageHelper {

	private static HistoryMessageHelper instance = null;

	public synchronized static HistoryMessageHelper getInstance() {
		if (instance == null) {
			instance = new HistoryMessageHelper();
		}
		return instance;
	}

    /**
     * 从服务器获取的聊天记录添加到本地
     * @return 给下拉刷新使用的数据
     */
    public List<EMMessage> saveHistoryMessages(HistoryMessageResponse response) throws JSONException {
        List<EMMessage> messages = new ArrayList<>();
		if(response == null) {
            return messages;
        }
        List<HistoryMessageBean> chatRecordList = response.getChat_record_list();
        List<HistoryUserBean> chatUserList = null;
        try {
            chatUserList = response.getChat_user_list();
        }catch (NullPointerException e) {
            e.printStackTrace();
        }

        //保存消息
        try {
            if (chatRecordList != null) {
				Logger.d("保存聊天历史记录 保存好友信息 "+chatRecordList.size());
                for (HistoryMessageBean message : chatRecordList) {
                    EMMessage emm = EMClient.getInstance().chatManager().getMessage(message.getMsg_id());
                    if (emm == null) {
						Logger.d("保存聊天历史记录  "+message.getMsg_pay_load());
                        emm = getEMMessage(message);
                        if (emm != null) {
                            messages.add(emm);
                            EMClient.getInstance().chatManager().saveMessage(emm);
							Logger.d("保存聊天历史记录 保存成功");
                        } else {
							Logger.d("保存聊天历史记录 kong ");
						}
                    }
                }
            }
        }catch (Exception e){
            Logger.d("保存聊天历史记录","Exception：："+e.toString());
        }

        //保存好友信息
        if(chatUserList != null) {
            for (HistoryUserBean users : chatUserList) {
                EaseUser euser = getHistoryUser(users);
                Logger.d("保存聊天历史记录 保存好友信息 "+euser.getUsername());
                IMHelper.getInstance().saveContact(euser);
            }
        }

		return messages;
    }

	/**
	 * 将服务器返回的消息对象转换成环信中的消息对象
	 * @param message 服务器返回的消息对象
	 * @return 转换后的消息对象
	 */
	private EMMessage getEMMessage(HistoryMessageBean message) throws JSONException {
		Logger.d("保存聊天历史记录 "+message.getMsg_pay_load());
		EMMessage emm;
		// 解析消息体，文本，语音，图片等等
		if(! TextUtils.isEmpty(message.getMsg_pay_load())) {
			JSONObject jsonObject = new JSONObject(message.getMsg_pay_load());
			//具体消息内容
			JSONArray bodiesArray = jsonObject.getJSONArray("bodies");
			Logger.d("保存聊天历史记录 bodiesArray " +bodiesArray.toString());
			if(bodiesArray != null && bodiesArray.length() > 0) {
				JSONObject bodiesObject = (JSONObject) bodiesArray.get(0);
				//消息类型
				String type = bodiesObject.getString("type");
                if(type.equals("cmd")){// 透传消息过滤
                    return null;
                }
				//消息体
				EMMessageBody emMessageBody = getEMMessageBody(bodiesObject, type,message);
				if (emMessageBody != null) {

                    emm = getEMMessage(type, message.getMsg_from());
                    setMassage(message, emm);

					emm.addBody(emMessageBody);
                    //扩展属性
                    JSONObject extObject = jsonObject.getJSONObject("ext");
                    if(extObject != null) {
                        emm = setHistoryMessageAttribute(extObject, emm);
                    }
                    return emm;
				}
			}
		}
		return null;
	}

	private EMMessage getEMMessage(String type, String from){
        EMMessage emm;
        switch (type) {
            case TEXT:
                if(EMClient.getInstance().getCurrentUser().equals(from)) {
                    emm = EMMessage.createSendMessage(EMMessage.Type.TXT);
                } else {
                    emm = EMMessage.createReceiveMessage(EMMessage.Type.TXT);
                }
                break;
            case IMAGE:
                if(EMClient.getInstance().getCurrentUser().equals(from)) {
                    emm = EMMessage.createSendMessage(EMMessage.Type.IMAGE);
                } else {
                    emm = EMMessage.createReceiveMessage(EMMessage.Type.IMAGE);
                }
                break;
            case LOCATION:
                if(EMClient.getInstance().getCurrentUser().equals(from)) {
                    emm = EMMessage.createSendMessage(EMMessage.Type.LOCATION);
                } else {
                    emm = EMMessage.createReceiveMessage(EMMessage.Type.LOCATION);
                }
                break;
            case AUDIO:
                if(EMClient.getInstance().getCurrentUser().equals(from)) {
                    emm = EMMessage.createSendMessage(EMMessage.Type.VIDEO);
                } else {
                    emm = EMMessage.createReceiveMessage(EMMessage.Type.VIDEO);
                }
                break;
            case FILE:
                if(EMClient.getInstance().getCurrentUser().equals(from)) {
                    emm = EMMessage.createSendMessage(EMMessage.Type.FILE);
                } else {
                    emm = EMMessage.createReceiveMessage(EMMessage.Type.FILE);
                }
                break;
            default:
                emm = EMMessage.createSendMessage(EMMessage.Type.TXT);
                break;
        }
        return emm;

    }

	private void setMassage(HistoryMessageBean message, EMMessage emm){
        emm.setStatus(EMMessage.Status.SUCCESS);
         // 基本信息
        emm.setTo(message.getMsg_to());
        emm.setFrom(message.getMsg_from());
        emm.setMsgTime(Long.parseLong(message.getMsg_timestamp()));
        emm.setMsgId(message.getMsg_id());
        emm.setUnread(false);
    }

	/**
	 * 设置布林类型的消息属性
	 * @param extObject
	 * @param emm
	 * @param key
	 * @throws JSONException
	 */
	private void setBooleanAttribute(JSONObject extObject, EMMessage emm, String key) throws JSONException {
		if(extObject.has(key)){
            String temp = extObject.get(key)+"";
            if (temp.equals("1")){
                emm.setAttribute(key,true);
            }else if (temp.equals("0")){
                emm.setAttribute(key,false);
            }else{
                boolean value = extObject.getBoolean(key);//语音通话
                emm.setAttribute(key,value);
            }
		}
	}

	/**
	 * 设置字符串类型的消息属性
	 * @param extObject
	 * @param emm
	 * @param key
	 * @throws JSONException
	 */
	private void setStringAttribute(JSONObject extObject, EMMessage emm, String key) throws JSONException {
		if(extObject.has(key)){
			String value = extObject.getString(key);
			emm.setAttribute(key,value);
		}
	}


	/**
	 * 将服务器返回的自定义属性设置到本地消息对象中
	 * @param extObject 服务器返回的对象
	 * @param emm 本地要存储的消息对象
	 * @return 处理后的消息对象
	 */
	private EMMessage setHistoryMessageAttribute(JSONObject extObject, EMMessage emm) throws JSONException {
		//获取自定义属性
		//设置到消息对象中
		setBooleanAttribute(extObject,emm,EaseConstant.MESSAGE_ATTR_IS_VOICE_CALL);//语音通话
		setBooleanAttribute(extObject,emm,EaseConstant.MESSAGE_ATTR_IS_VIDEO_CALL);//视频通话
		setBooleanAttribute(extObject,emm,EaseConstant.MESSAGE_ATTR_IS_BIG_EXPRESSION);//大表情
		setBooleanAttribute(extObject,emm,EaseConstant.MESSAGE_ATTR_IS_ORDER_INFO);//订单详情类的消息
		setBooleanAttribute(extObject,emm,EaseConstant.MESSAGE_ATTR_IS_ORDER_INFO);//被加入黑名单了

		setStringAttribute(extObject,emm,EaseConstant.MESSAGE_ATTR_EXPRESSION_ID);//表情id
		setStringAttribute(extObject,emm,EaseConstant.MESSAGE_ATTR_AT_MSG);//@消息，本应用中不存在@消息
		setStringAttribute(extObject,emm,EaseConstant.MESSAGE_ATTR_VALUE_AT_MSG_ALL);//@所有人消息，本应用中不存在@消息
		setStringAttribute(extObject,emm,EaseConstant.MESSAGE_ATTR_DEPART);//出发地
		setStringAttribute(extObject,emm,EaseConstant.MESSAGE_ATTR_DESTINATION);//目的地
		setStringAttribute(extObject,emm,EaseConstant.MESSAGE_ATTR_TRUCK_INFO);//车辆信息
		setStringAttribute(extObject,emm,EaseConstant.MESSAGE_ATTR_GOODS_ID);//货源id
		setStringAttribute(extObject,emm,EaseConstant.MESSAGE_ATTR_GOODS_VERSION);//货源版本
        emm.setAttribute(EaseConstant.MESSAGE_ATTR_IS_FROM_HISTORY,true);


        return emm;

	}

	/**
	 * 根据json对象获取消息内容
	 * 对视频和文件消息暂时不做处理
	 * 因为没有发送这两个消息的方式
	 * @param bodiesObject json对象
	 * @param type 消息类型
	 * @return
	 */
	private static final String TEXT = "txt";
	private static final String IMAGE = "img";
	private static final String LOCATION = "loc";
	private static final String AUDIO = "audio";
	private static final String VIDEO = "video";
	private static final String FILE = "file";
	private EMMessageBody getEMMessageBody(JSONObject bodiesObject, String type, HistoryMessageBean message) throws JSONException {

		EMMessageBody messageBody = null;
		if(!TextUtils.isEmpty(type)) {
			switch (type) {
				case TEXT:
					// 1. 文本类型
					String txt = bodiesObject.getString("msg");
					messageBody = new EMTextMessageBody(txt);
					break;
				case IMAGE:
					// 2. 图片类型
					String displayName = bodiesObject.getString("filename");
					String remotePath = bodiesObject.getString("url");
					String secret = bodiesObject.getString("secret");
					JSONObject sizeObject = bodiesObject.getJSONObject("size");
					int width = sizeObject.getInt("width");
					int height = sizeObject.getInt("height");
					EMAImageMessageBody emaImageMessageBody = new EMAImageMessageBody("","");
					emaImageMessageBody.width = width;
					emaImageMessageBody.height = height;

					EMImageMessageBody imageMessageBody = new EMImageMessageBody(emaImageMessageBody);
					imageMessageBody.setRemoteUrl(remotePath);
					imageMessageBody.setSecret(secret);
					imageMessageBody.setThumbnailSecret(secret);
					imageMessageBody.setThumbnailUrl(remotePath);
					imageMessageBody.setFileName(displayName);
                    // 按照IM文件制定的方式拼接文件路径
                    String finalThumbPath = getHistoryHeadPath()+"/files/"+message.getMsg_to()+"/"+message.getMsg_from()+"/thumb_"+getHistoryLastPath(remotePath);
                    String finalPath = getHistoryHeadPath()+"/files/"+message.getMsg_to()+"/"+message.getMsg_from()+"/"+getHistoryLastPath(remotePath);
                    imageMessageBody.setThumbnailLocalPath(finalThumbPath);
                    imageMessageBody.setLocalUrl(finalPath);

					messageBody = imageMessageBody;

					break;
				case LOCATION:
					// 3. 地图类型
					String address = bodiesObject.getString("addr");
					double latitude = bodiesObject.getDouble("lat");
					double longitude = bodiesObject.getDouble("lng");
					messageBody = new EMLocationMessageBody(address,latitude,longitude);
					break;
				case AUDIO:
					// 4. 语音类型
					String fileName = bodiesObject.getString("filename");
					int duration = bodiesObject.getInt("length");
					String voiceSecret = bodiesObject.getString("secret");
					String remoteVoicePath = bodiesObject.getString("url");

					EMAVoiceMessageBody emaVoiceMessageBody = new EMAVoiceMessageBody("",4);
					emaVoiceMessageBody.setDuration(duration);

					EMVoiceMessageBody voiceMessageBody = new EMVoiceMessageBody(emaVoiceMessageBody);
					voiceMessageBody.setFileName(fileName);
					voiceMessageBody.setRemoteUrl(remoteVoicePath);
					voiceMessageBody.setSecret(voiceSecret);

                    // 按照IM文件制定的方式拼接文件路径
                    String voiceFinalPath = getHistoryHeadPath()+"/files/"+message.getMsg_to()+"/"+message.getMsg_from()+"/"+getHistoryLastPath(remoteVoicePath)+".amr";
                    voiceMessageBody.setLocalUrl(voiceFinalPath);

					messageBody = voiceMessageBody;
					break;
				case VIDEO:
					// 5. 视频类型 暂时不处理，不存在发送这种类型的消息

					break;
				case FILE:
					// 6. 文件类型 暂时不处理，不存在发送这种类型的消息
					break;
			}

		}
		return messageBody;
	}

	/**
	 * 将服务器返回的用户信息转换成环信中的用户信息
	 * @param users 服务器返回的
	 * @return 本地环信的
	 */
	public EaseUser getHistoryUser(HistoryUserBean users){
        EaseUser euser = new EaseUser(users.getTalk_id());

        euser.setUserPhone(users.getTalk_phone());
        euser.setUserId(users.getTalk_id());
        euser.setAvatar(users.getImg_url());
        euser.setAvatarKey(users.getImg_key());
        euser.setNickname(users.getTalk_name());
        euser.setNick(users.getTalk_name());
        euser.setIsAnonymous(users.getIs_anonymous());
        return euser;
	}

    /**
     * 获取IM文件路径的开始头路径
     * IM的文件使用了特定的拼接方式
     * 只有使用这种凭借方式才能正常下载文件
     * 否则会下载失败
     * @return 头路径
     */
	private String getHistoryHeadPath(){
        String filePath = PathUtil.getInstance().getFilePath().getParent();
        return filePath.substring(0,(filePath.lastIndexOf("/")));
    }

    /**
     * 获取历史消息中IM文件的文件名
     * @param remoteUrl 文件路径
     * @return 文件名
     */
    private String getHistoryLastPath(String remoteUrl){
        return remoteUrl.substring(remoteUrl.lastIndexOf("/") + 1, remoteUrl.length());
    }
}
