package cn.sharesdk.wxapi;


import com.example.sharesdk.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CancleTitle extends LinearLayout  {

	private Context context;
	private ImageButton btn_cancle;
	private TextView titlv_title;
	private OnCancleButtonClickListener listener;

	public CancleTitle(Context context) {
		super(context);
		initView(context);
	}
	

	public CancleTitle(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}
	private void initView(Context context) {
		this.context = context;
		LayoutInflater.from(context).inflate(R.layout.custom_cancle_title, this, true);

		btn_cancle = (ImageButton) findViewById(R.id.btn_cancle);
		btn_cancle.setEnabled(true);
		
		registerListener();
	}

	private void registerListener() {
		btn_cancle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (listener != null) {
					listener.onClick();
				}
			}
		});
		
	}
	
	
	public interface OnCancleButtonClickListener {
		public void onClick();
	}
	
	//设置叉叉的点击事件的监听接口
	public void setOnCancleButtonClickListener(OnCancleButtonClickListener listener) {
		this.listener = listener;
	}
}
