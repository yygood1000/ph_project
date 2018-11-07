package com.hyphenate.easeui.widget.chatrow;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMLocationMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessage.ChatType;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.ui.EaseViewLocationActivity;
import com.hyphenate.easeui.utils.EaseUserUtils;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.LatLng;

public class EaseChatRowLocation extends EaseChatRow{

    private TextView locationView,tv_name;
    private EMLocationMessageBody locBody;
    private String [] addressAndName;

	public EaseChatRowLocation(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
    }

    @Override
    protected void onInflateView() {
        inflater.inflate(message.direct() == EMMessage.Direct.RECEIVE ?
                R.layout.ease_row_received_location : R.layout.ease_row_sent_location, this);
    }

    @Override
    protected void onFindViewById() {
    	locationView = (TextView) findViewById(R.id.tv_location);
        tv_name = (TextView) findViewById(R.id.tv_name);
    }


    @Override
    protected void onSetUpView() {
	    // 地图显示白色背景
        bubbleLayout.setBackgroundResource(R.drawable.ease_chatto_bg);
		locBody = (EMLocationMessageBody) message.getBody();
		String addressStr = locBody.getAddress();
		addressAndName = addressStr.split("&");
		if(addressAndName.length > 1) {
            tv_name.setText(addressAndName[0]);
            locationView.setText(addressAndName[1]); //&
        }

		// handle sending message
		if (message.direct() == EMMessage.Direct.SEND) {
		    setMessageSendCallback();
            switch (message.status()) {
				case CREATE:
					progressBar.setVisibility(View.GONE);
					statusView.setVisibility(View.VISIBLE);
					resendView.setVisibility(VISIBLE);
					break;
				case SUCCESS:
					progressBar.setVisibility(View.GONE);
					statusView.setVisibility(View.GONE);
					resendView.setVisibility(GONE);
					break;
				case FAIL:
					progressBar.setVisibility(View.GONE);
					statusView.setVisibility(View.VISIBLE);
					resendView.setVisibility(VISIBLE);
					break;
				case INPROGRESS:
					progressBar.setVisibility(View.VISIBLE);
					statusView.setVisibility(View.GONE);
					resendView.setVisibility(GONE);
					break;
            default:
               break;
            }
        }else{
		    // 接收到的地图信息显示白色三角
            imageBubbleTriangle.setImageResource(R.drawable.ic_message_v_left_white);
            if(!message.isAcked() && message.getChatType() == ChatType.Chat){
                try {
                    EMClient.getInstance().chatManager().ackMessageRead(message.getFrom(), message.getMsgId());
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    @Override
    protected void onUpdateView() {
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onBubbleClick() {
        Intent intent = new Intent(context, EaseViewLocationActivity.class);
        if(null == EaseUserUtils.getUserInfo(message.getUserName()).getNick() || EaseUserUtils.getUserInfo(message.getUserName()).getNick().equals("")){
            intent.putExtra("username", message.getUserName());
        }else{
            intent.putExtra("username", EaseUserUtils.getUserInfo(message.getUserName()).getNick());
        }
        intent.putExtra("latitude", locBody.getLatitude());
        intent.putExtra("longitude", locBody.getLongitude());

        intent.putExtra("address", addressAndName[1] + addressAndName[0]);

        intent.putExtra("addressname", addressAndName[0]);
        activity.startActivity(intent);
    }
    
    /*
	 * listener for map clicked
	 */
	protected class MapClickListener implements OnClickListener {

		LatLng location;
		String address;

		public MapClickListener(LatLng loc, String address) {
			location = loc;
			this.address = address;

		}

		@Override
		public void onClick(View v) {
		   
		}
	}

}
