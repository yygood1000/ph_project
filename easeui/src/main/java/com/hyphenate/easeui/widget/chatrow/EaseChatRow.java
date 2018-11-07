package com.hyphenate.easeui.widget.chatrow;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessage.Direct;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.adapter.EaseMessageAdapter;
import com.hyphenate.easeui.domain.EaseAvatarOptions;
import com.hyphenate.easeui.model.styles.EaseMessageListItemStyle;
import com.hyphenate.easeui.utils.EaseUserUtils;
import com.hyphenate.easeui.widget.EaseChatMessageList;
import com.hyphenate.easeui.widget.EaseChatMessageList.MessageListItemClickListener;
import com.hyphenate.easeui.widget.EaseImageView;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.DateUtils;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.Toaster;

import java.util.Date;


public abstract class EaseChatRow extends LinearLayout {
	protected static final String TAG = EaseChatRow.class.getSimpleName();

	protected LayoutInflater inflater;
	protected Context context;
	protected BaseAdapter adapter;
	protected EMMessage message;
	protected int position;

	protected TextView timeStampView;
	protected ImageView userAvatarView;
	protected View bubbleLayout;
	protected TextView usernickView;
	protected ImageView imageBubbleTriangle;

	protected TextView percentageView;
	protected ProgressBar progressBar;
	protected ImageView statusView;
	protected MvpActivity activity;

	protected TextView ackedView;
	protected TextView deliveredView;
	protected LinearLayout resendView;//重新发送 是否显示
	protected TextView tvResend;

	protected EMCallBack messageSendCallback;
	protected EMCallBack messageReceiveCallback;

	protected MessageListItemClickListener itemClickListener;
	protected EaseMessageListItemStyle itemStyle;

	public EaseChatRow(Context context, EMMessage message, int position, BaseAdapter adapter) {
		super(context);
		this.context = context;
		this.activity = (MvpActivity) context;
		this.message = message;
		this.position = position;
		this.adapter = adapter;
		inflater = LayoutInflater.from(context);

		initView();
	}

	private void initView() {
		onInflateView();
		timeStampView = (TextView) findViewById(R.id.timestamp);
		userAvatarView = (ImageView) findViewById(R.id.iv_userhead);
		bubbleLayout = findViewById(R.id.bubble);
		usernickView = (TextView) findViewById(R.id.tv_userid);

		progressBar = (ProgressBar) findViewById(R.id.progress_bar);
		statusView = (ImageView) findViewById(R.id.msg_status);
		ackedView = (TextView) findViewById(R.id.tv_ack);
		deliveredView = (TextView) findViewById(R.id.tv_delivered);
		resendView = (LinearLayout) findViewById(R.id.ll_message_resend);
		tvResend = (TextView) findViewById(R.id.tv_resend);
		imageBubbleTriangle = (ImageView) findViewById(R.id.iv_bubble_triangle);

		onFindViewById();
	}

	/**
	 * 发送失败的状态判断
	 * 判断这条消息是否因为被加入黑名单而被拒绝了
	 */
	private void setMessageFailStatus() {
		if (message == null)
			return;
		if (tvResend == null) {
			tvResend = (TextView) findViewById(R.id.tv_resend);
		}
		try {
			String msg = message.getStringAttribute(EaseConstant.MESSAGE_ATTR_BY_ADD_BLACK_LIST);
			if (!TextUtils.isEmpty(msg) && tvResend != null) {
				tvResend.setText(msg);
			}
			Logger.d("消息状态 " + msg);
		} catch (HyphenateException e) {
			e.printStackTrace();
		}

	}

	/**
	 * set property according message and postion
	 *
	 * @param message
	 * @param position
	 */
	public void setUpView(EMMessage message, int position,
						  EaseChatMessageList.MessageListItemClickListener itemClickListener,
						  EaseMessageListItemStyle itemStyle) {
		this.message = message;
		this.position = position;
		this.itemClickListener = itemClickListener;
		this.itemStyle = itemStyle;


		setUpBaseView();
		onSetUpView();
		setClickListener();
		setMessageFailStatus();
	}

	private void setUpBaseView() {
		//区分司机和货主版气泡显示三角
		if (imageBubbleTriangle != null) {
			if (CMemoryData.isDriver()) {
				imageBubbleTriangle.setImageResource(R.drawable.ic_message_v_left_driver);
			} else {
				imageBubbleTriangle.setImageResource(R.drawable.ic_message_v_left_shipper);
			}
		}
		if (resendView == null) {
			resendView = (LinearLayout) findViewById(R.id.ll_message_resend);
		}
		// set nickname, avatar and background of bubble
//        TextView timestamp = (TextView) findViewById(R.id.timestamp);
		if (timeStampView != null) {
			if (position == 0) {
				timeStampView.setText(DateUtils.getTimestampString(new Date(message.getMsgTime())));
				timeStampView.setVisibility(View.VISIBLE);
			} else {
				// show time stamp if interval with last message is > 30 seconds
				EMMessage prevMessage = (EMMessage) adapter.getItem(position - 1);
				if (prevMessage != null && isShowTime(message.getMsgTime(), prevMessage.getMsgTime())) {
					timeStampView.setText(DateUtils.getTimestampString(new Date(message.getMsgTime())));
					timeStampView.setVisibility(View.VISIBLE);
				} else {
					timeStampView.setVisibility(View.GONE);
				}
			}
		}
		//set nickname and avatar
		if (message.direct() == Direct.SEND) {
			EaseUserUtils.setUserAvatar(context, EMClient.getInstance().getCurrentUser(), userAvatarView);
		} else {
			EaseUserUtils.setUserAvatar(context, message.getFrom(), userAvatarView);
			EaseUserUtils.setUserNick(message.getFrom(), usernickView);
		}
		//是否送达,不做处理
		if (deliveredView != null) {
//            if (message.isDelivered()) {
//                deliveredView.setVisibility(View.VISIBLE);
//            } else {
//                deliveredView.setVisibility(View.INVISIBLE);
//            }
		}
		//是否已读，不做处理
		if (ackedView != null) {
//            if (message.isAcked()) {
//                if (deliveredView != null) {
//                    deliveredView.setVisibility(View.INVISIBLE);
//                }
//                ackedView.setVisibility(View.VISIBLE);
//            } else {
//                ackedView.setVisibility(View.INVISIBLE);
//            }
		}

		if (itemStyle != null) {
			if (userAvatarView != null) {
				if (itemStyle.isShowAvatar()) {
					userAvatarView.setVisibility(View.VISIBLE);
					EaseAvatarOptions avatarOptions = EaseUI.getInstance().getAvatarOptions();
					if (avatarOptions != null && userAvatarView instanceof EaseImageView) {
						EaseImageView avatarView = ((EaseImageView) userAvatarView);
						if (avatarOptions.getAvatarShape() != 0)
							avatarView.setShapeType(avatarOptions.getAvatarShape());
						if (avatarOptions.getAvatarBorderWidth() != 0)
							avatarView.setBorderWidth(avatarOptions.getAvatarBorderWidth());
						if (avatarOptions.getAvatarBorderColor() != 0)
							avatarView.setBorderColor(avatarOptions.getAvatarBorderColor());
						if (avatarOptions.getAvatarRadius() != 0)
							avatarView.setRadius(avatarOptions.getAvatarRadius());
					}
				} else {
					userAvatarView.setVisibility(View.GONE);
				}
			}
			if (usernickView != null) {
				if (itemStyle.isShowUserNick())
					usernickView.setVisibility(View.VISIBLE);
				else
					usernickView.setVisibility(View.GONE);
			}
			if (bubbleLayout != null) {
				if (message.direct() == Direct.SEND) {
					if (itemStyle.getMyBubbleBg() != -1) {
						bubbleLayout.setBackgroundResource(((EaseMessageAdapter) adapter).getMyBubbleBg());
					}
				} else if (message.direct() == Direct.RECEIVE) {
					if (itemStyle.getOtherBubbleBg() != -1) {
						bubbleLayout.setBackgroundResource(((EaseMessageAdapter) adapter).getOtherBubbleBg());
					}
					try {
						//大表情没有背景
						if (message.getBooleanAttribute(EaseConstant.MESSAGE_ATTR_IS_BIG_EXPRESSION, false)) {
							bubbleLayout.setBackgroundColor(Color.parseColor("#00000000"));
						}
					}catch (Exception e){
						e.printStackTrace();
					}
				}
			}
		}

	}


	/**
	 * 两条消息间隔时间大于5分钟 则显示时间
	 */
	private boolean isShowTime(long curTime, long lastTime){
		long time = curTime - lastTime;
		Logger.d("间隔时间 "+time);
        return time > 5 * 60 * 1000;
    }

	/**
	 * set callback for sending message
	 * 重新发送才会调用的回调
	 */
	protected void setMessageSendCallback() {
		if (messageSendCallback == null) {
			messageSendCallback = new EMCallBack() {

				@Override
				public void onSuccess() {
					updateView();
				}

				@Override
				public void onProgress(final int progress, String status) {
					activity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							if (percentageView != null)
								percentageView.setText(progress + "%");

						}
					});
				}

				@Override
				public void onError(int code, String error) {
					Logger.e("发送消息失败code " + code);
					updateView(code, error);
				}
			};
		}
		message.setMessageStatusCallback(messageSendCallback);
	}

	/**
	 * set callback for receiving message
	 * 重新发送才会调用的回调
	 */
	protected void setMessageReceiveCallback() {
		if (messageReceiveCallback == null) {
			messageReceiveCallback = new EMCallBack() {

				@Override
				public void onSuccess() {
					updateView();
				}

				@Override
				public void onProgress(final int progress, String status) {
					activity.runOnUiThread(new Runnable() {
						public void run() {
							if (percentageView != null) {
								percentageView.setText(progress + "%");
							}
						}
					});
				}

				@Override
				public void onError(int code, String error) {
					Logger.e("接收消息失败code " + code);
					updateView();
				}
			};
		}
		message.setMessageStatusCallback(messageReceiveCallback);
	}


	private void setClickListener() {
		if (bubbleLayout != null) {
			bubbleLayout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (itemClickListener != null) {
						if (!itemClickListener.onBubbleClick(message)) {
							// if listener return false, we call default handling
							onBubbleClick();
						}
					}
				}
			});

			bubbleLayout.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {
					if (itemClickListener != null) {
						itemClickListener.onBubbleLongClick(message);
					}
					return true;
				}
			});
		}

		if (statusView != null) {
			statusView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (itemClickListener != null) {
						itemClickListener.onResendClick(message);
					}
				}
			});
		}
		if (tvResend != null) {
			tvResend.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (itemClickListener != null) {
						itemClickListener.onResendClick(message);
					}
				}
			});
		}

		if (userAvatarView != null) {
			userAvatarView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (itemClickListener != null) {
						if (message.direct() == Direct.SEND) {
							itemClickListener.onUserAvatarClick(EMClient.getInstance().getCurrentUser());
						} else {
							itemClickListener.onUserAvatarClick(message.getFrom());
						}
					}
				}
			});
			userAvatarView.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {
					if (itemClickListener != null) {
						if (message.direct() == Direct.SEND) {
							itemClickListener.onUserAvatarLongClick(EMClient.getInstance().getCurrentUser());
						} else {
							itemClickListener.onUserAvatarLongClick(message.getFrom());
						}
						return true;
					}
					return false;
				}
			});
		}
	}


	protected void updateView() {
		activity.runOnUiThread(new Runnable() {
			public void run() {
				if (message.status() == EMMessage.Status.FAIL) {
					Toast.makeText(activity, activity.getString(R.string.send_fail) + activity.getString(R.string.connect_failuer_toast), Toast.LENGTH_SHORT).show();
				}

				onUpdateView();
			}
		});
	}

	protected void updateView(final int errorCode, final String desc) {

		Logger.d("发送消息失败code " + errorCode + " " + desc);
		activity.runOnUiThread(new Runnable() {
			public void run() {
				if (errorCode == EMError.MESSAGE_INCLUDE_ILLEGAL_CONTENT) {
					Toaster.showShortToast(activity.getString(R.string.send_fail) + activity.getString(R.string.error_send_invalid_content));
				} else if (errorCode == EMError.GROUP_NOT_JOINED) {
					Toaster.showShortToast(activity.getString(R.string.send_fail) + activity.getString(R.string.error_send_not_in_the_group));
				} else if (errorCode == EMError.USER_PERMISSION_DENIED) {//被拉入黑名单了
					// 这里显示 发送失败，对方拒绝接收您的消息
					tvResend.setText(activity.getString(R.string.by_other_black_list_msg));
				} else if(errorCode == EMError.FILE_DOWNLOAD_FAILED) {
					// 下载文件失败

				} else {
					Toaster.showShortToast(activity.getString(R.string.send_fail) + activity.getString(R.string.connect_failuer_toast));
				}
				onUpdateView();
			}
		});
	}

	protected abstract void onInflateView();

	/**
	 * find view by id
	 */
	protected abstract void onFindViewById();

	/**
	 * refresh list view when message status change
	 */
	protected abstract void onUpdateView();

	/**
	 * setup view
	 */
	protected abstract void onSetUpView();

	/**
	 * on bubble clicked
	 */
	protected abstract void onBubbleClick();

}
