package com.topjet.crediblenumber.goods.view.widget;


import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.topjet.common.base.busManger.BusManager;
import com.topjet.common.base.busManger.Subscribe;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.utils.ScreenUtils;
import com.topjet.crediblenumber.R;
import com.topjet.crediblenumber.goods.modle.event.ChangeListenOrderStatusEvent;

/**
 * Created by tsj-004 on 2016年12月2日17:59:01
 * version 2.2
 * 听单悬浮控件
 * <p>
 * 仅对FloatView微调
 * 1、修复拖动也会弹出菜单的问题，解决方案：OnTouchListener的同时不设置OnClickListener即可
 * 2、把多余的控件去掉
 */
public class FloatView extends RelativeLayout implements View.OnTouchListener {

    private Context mContext;

    private static ImageView iv_laba = null; // 喇叭
    public boolean isClickedView = false;        //是否点击了喇叭按钮，是则不可点击，否则可点击
    private static boolean isTouchedView = false;        //是否touch了喇叭按钮，是则处于激活状态，否则处于休眠状态
    private MyHandler myHandler = new MyHandler();

    private static FloatViewListener mFloatListener; // 接口

    public void setFloatViewListener(FloatViewListener mFloatListener) {
        FloatView.mFloatListener = mFloatListener;
    }

    public interface FloatViewListener {
        void move(View view, int x, int y);

        void click(View view);
    }

    public FloatView(Context context) {
        this(context, null);
    }

    public FloatView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;

        BusManager.getBus().register(this);

        View view = LayoutInflater.from(mContext).inflate(R.layout.ppw_listen_order, this);

        iv_laba = (ImageView) view.findViewById(R.id.iv_laba);

        // onTouch > onClick > onLongClick (事件先后顺序)
//		iv_laba.setOnClickListener(this);
        iv_laba.setOnTouchListener(this);

        if (CMemoryData.isOrderOpen()) {
            iv_laba.setImageResource(R.drawable.icon_listen_order_on_nor);           //睡眠状态_已开启
        } else {
            iv_laba.setImageResource(R.drawable.icon_listen_order_off_nor);          //睡眠状态_已关闭
        }

    }

    //	/**
//	 * 为当前点击的Item设置 变大/变小 和 透明度降低的动画
//	 *
//	 * @param duration
//	 *            延迟 时间 毫秒 float f 放大 、 缩小 倍数
//	 * @return
//	 */
//	private Animation scaleItemAnim(int duration, float f) {
//		AnimationSet animationSet = new AnimationSet(true);
//
//		ScaleAnimation scaleAnim = new ScaleAnimation(1.0f, f, 1.0f, f, Animation.RELATIVE_TO_SELF, 0.5f,
//				Animation.RELATIVE_TO_SELF, 0.5f);
//		// 透明度 变化
//		AlphaAnimation alphaAnim = new AlphaAnimation(1f, 0.8f);
//
//		animationSet.addAnimation(scaleAnim);
//		animationSet.addAnimation(alphaAnim);
//
//		animationSet.setDuration(duration);
//		animationSet.setFillAfter(true);
//		return animationSet;
//
//	}
//
//	// 旋转动画
//
//	private void rotateCButton(View v, float start, float end, int duration) {
//
//		RotateAnimation anim = new RotateAnimation(start, end, Animation.RELATIVE_TO_SELF, 0.5f,
//				Animation.RELATIVE_TO_SELF, 0.5f);
//		anim.setDuration(duration);
//		anim.setFillAfter(true);
//		v.startAnimation(anim);
//	}
//
//	/**
//	 * 隐藏开、关、设置
//	 */
//	private void hideMenu() {
//		tv_open.setVisibility(View.INVISIBLE);
//		tv_set.setVisibility(View.INVISIBLE);
//		tv_close.setVisibility(View.INVISIBLE);
//	}
//
    public void onClick(View v) {
        if (!isClickedView) {
            isClickedView = true;
            mFloatListener.click(v);
        }
    }

//
//	private void toggleMenu(View v) {
//
//		int id = v.getId();
//		Log.d("toggleMenu", "id==============" + id);
//		switch (id) {
//		case R.id.iv_laba:
//			if (!isOpen) {
//				hideMenu();
//				isOpen = true;
//			} else {
////				rotateCButton(v, 0f, 720f, 300);
//				// Toast.makeText(mContext, "laba: " + id,
//				// Toast.LENGTH_SHORT).show();
//				tv_close.setVisibility(View.VISIBLE);
//				tv_open.setVisibility(View.VISIBLE);
//				tv_set.setVisibility(View.VISIBLE);
//				isOpen = false;
//			}
//			break;
//		}
//	}

    int lastX, lastY;
    int lastX0, lastY0;

    /*
     * return false说明你还没消费onTouch事件 return true说明你已经消费完了onTouch事件,onTouch 事件结束
     **/
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (isClickedView) {
            return false;
        } else {
            isTouchedView = true;
            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    lastX = (int) event.getRawX();
                    lastY = (int) event.getRawY();
                    lastX0 = (int) event.getRawX();
                    lastY0 = (int) event.getRawY();

                    if (CMemoryData.isOrderOpen()) {
                        iv_laba.setImageResource(R.drawable.icon_listen_order_on_high);           // 激活状态_已开启
                    } else {
                        iv_laba.setImageResource(R.drawable.icon_listen_order_off_high);          // 激活状态_已关闭
                    }
                    myHandler.removeCallbacksAndMessages(null);        // 清空

                    Log.d("TTT", "onTouch ACTION_DOWN:lastX = " + lastX + "lastY = " + lastY);
                    break;
                case MotionEvent.ACTION_MOVE:
                    int dx = (int) event.getRawX() - lastX;
                    int dy = (int) event.getRawY() - lastY;
                    Log.d("TTT", "onTouch ACTION_MOVE:dx = " + dx + "dy = " + dy);
                    mFloatListener.move(this, dx, dy);
                    lastX = (int) event.getRawX();
                    lastY = (int) event.getRawY();
                    Log.d("TTT", "onTouch ACTION_MOVE:lastX = " + lastX + "lastY = " + lastY);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    int dx1 = Math.abs((int) event.getRawX() - lastX0);
                    int dy1 = Math.abs((int) event.getRawY() - lastY0);
                    if (dx1 <= 10 && dy1 <= 10) {
                        this.onClick(v);
                    }
                    myHandler.sendEmptyMessageDelayed(500, 1000);        // 1秒后靠边
                    myHandler.sendEmptyMessageDelayed(5000, 5000);        // 5秒后变为非激活
                    break;
            }
            // 此处返回true 该控件的 onClick 事件将不能响应
            return true;
        }
    }

    public class MyHandler extends Handler {
        public void handleMessage(Message msg) {
            isTouchedView = false;
            if (msg.what == 500) {
                // 移动到屏幕边缘
                int[] location = new int[2];
                FloatView.this.getLocationOnScreen(location);
                int screenWidth = ScreenUtils.getWindowsWidth();
                int viewWidth = FloatView.this.getWidth();

                if (mFloatListener != null) {
                    if (!ViewCompat.isAttachedToWindow(FloatView.this)) // 判断是否已经绑定到窗口
                        return;
                    if (location[0] + viewWidth / 2 < screenWidth / 2) {
                        mFloatListener.move(FloatView.this, -location[0], 0);
                    } else {
                        mFloatListener.move(FloatView.this, screenWidth - viewWidth - location[0], 0);
                    }
                    if (CMemoryData.isOrderOpen()) {
                        iv_laba.setImageResource(R.drawable.icon_listen_order_on_high);           // 激活状态_已开启
                    } else {
                        iv_laba.setImageResource(R.drawable.icon_listen_order_off_high);          // 激活状态_已关闭
                    }
                }
            } else if (msg.what == 5000) {
                if (CMemoryData.isOrderOpen()) {
                    iv_laba.setImageResource(R.drawable.icon_listen_order_on_nor);           // 睡眠状态_已开启
                } else {
                    iv_laba.setImageResource(R.drawable.icon_listen_order_off_nor);          // 睡眠状态_已关闭
                }
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        BusManager.getBus().unregister(this);
    }

    @Subscribe
    public void onEvent(ChangeListenOrderStatusEvent event) {
        if (event.getType().equals(ChangeListenOrderStatusEvent.CHANGE_FLOATVIEW_STATUS)) {
            myHandler.sendEmptyMessage(500);        // 靠边
            myHandler.sendEmptyMessageDelayed(5000, 5000);  // 变灰
        }
    }

//	/**
//	 *
//	 */
//	public void onEventMainThread(V3_refreshListenorderOnOffServiceEvent e) {
//		if (e != null && e.isSuccess()) {
//			if(isTouchedView)
//			{
//				iv_laba.setImageResource(R.drawable.icon_voice_open_act);
//			}
//			else
//			{
//				iv_laba.setImageResource(R.drawable.icon_voice_open_sle);
//			}
//
//		} else if (e != null && !e.isSuccess()) {
//			if(isTouchedView)
//			{
//				iv_laba.setImageResource(R.drawable.icon_voice_close_act);
//			}
//			else
//			{
//				iv_laba.setImageResource(R.drawable.icon_voice_close_sle);
//			}
//		}
//	}

    /**
     * 清楚所有消息
     */
    public void destroyHandler() {
        if (myHandler != null) {
            myHandler.removeCallbacksAndMessages(null);
        }
    }
}
