package com.topjet.common.jpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.topjet.common.config.CPersisData;
import com.topjet.common.utils.AppManager;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
	private static final String TAG = "MyReceiver-Test";

	@Override
	public void onReceive(Context context, Intent intent) {
		try {
			Bundle bundle = intent.getExtras();

			String messageType = intent.getStringExtra("MsgType");
			String msg = intent.getStringExtra("Msg");
			String businesstype = intent.getStringExtra("businesstype");

			Logger.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
			Logger.d(TAG, "[MyReceiver] onReceive - "+"messageType "+messageType+" "+msg+" "+businesstype);
			if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
				String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
				Logger.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
				CPersisData.setPushToken(regId);
				//send the Registration Id to your server...

			} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
				Logger.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
				// 这里处理json
				processCustomMessage(context, bundle);

			} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {


				Logger.d(TAG, "[MyReceiver] 接收到推送下来的通知");
				int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
				Logger.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

			} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
				Logger.d(TAG, "[MyReceiver] 用户点击打开了通知");
				//打开自定义的Activity
//				Intent i = new Intent(context, MainActivity.class);
//				i.putExtras(bundle);
//				//i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
//				context.startActivity(i);

			} else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
				Logger.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
				//在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

			} else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
				boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
				Logger.w(TAG, "[MyReceiver]" + intent.getAction() +" connected state change to "+connected);
			} else {
				Logger.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
			}
		} catch (Exception e){

		}

	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			}else if(key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)){
				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
			} else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
				if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
					Logger.i(TAG, "This message has no Extra data");
					continue;
				}

				try {
					JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
					Iterator<String> it =  json.keys();

					while (it.hasNext()) {
						String myKey = it.next();
						sb.append("\nkey:" + key + ", value: [" +
								myKey + " - " +json.optString(myKey) + "]");
					}
				} catch (JSONException e) {
					Logger.e(TAG, "Get message extra JSON error!");
				}

			} else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}

	/**
	 * 解析自定义消息中传递的参数
	 */
	private void processCustomMessage(Context context, Bundle bundle) {
		for (String key : bundle.keySet()) {
			// 获取json 数据
			if (key.equals(JPushInterface.EXTRA_MESSAGE)) {
				if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_MESSAGE))) {
					Logger.i(TAG, "This message has no Extra data");
					return;
				}
				String json = bundle.getString(JPushInterface.EXTRA_MESSAGE);
				JPushContentBean contentBean = new Gson().fromJson(json, JPushContentBean.class);
				if(contentBean != null) {
					JPushBean bean = new Gson().fromJson(contentBean.getData().getContent(), JPushBean.class);
					if (bean != null) {
						if (StringUtils.isEmpty(bean.getText())) {
							bean.setText(contentBean.getData().getAlert());
						}
						// 后台
						if (CPersisData.getIsBackground()) {
							// 显示通知
							Logger.d("JPushHelper 后台");
							JPushHelper.getInstance().getNotification(context, bean);
						} else {
							// 显示弹窗
							Logger.d("JPushHelper 前台 "+bean.toString());
							JPushHelper.getInstance().handleData(AppManager.getInstance().getTopActivity(), bean);
							// 跳转一个activity 透明的，在弹窗，避免获取悬浮窗权限
//							Intent i = new Intent(context, JPushActivity.class);
//							i.putExtra(JPushBean.getExtraName(), bean);
//							i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//							context.startActivity(i);
						}
					}
				}
			}
		}

	}
}
