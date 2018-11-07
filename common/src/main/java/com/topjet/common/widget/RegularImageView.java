package com.topjet.common.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by yy on 2017/11/14.
 * <p>
 * 定时活动
 */

public class RegularImageView extends android.support.v7.widget.AppCompatImageView implements View.OnTouchListener {
    private static final int FLAG_SHOW_WHOLE = 0;
    private static final int FLAG_SHOW_SHRINK = 1;
    private MyHandler myHandler = new MyHandler();

    public boolean isShowWhole;// 是否是显示完整图标
    private int width;// 控件的宽
    private OnClickListener onClickListener;

    public void setOnCustomClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public RegularImageView(Context context) {
        super(context);
        setOnTouchListener(this);
    }

    public RegularImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOnTouchListener(this);
    }

    public RegularImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnTouchListener(this);
    }

    /**
     * onMeasure是measure方法引起的回调,而measure方法是父VIew在测量子View会调用子的View的measure方法
     * 所以widthMeasureSpec(宽度测量规则)和heightMeasureSpec(高度测量规则)是父VIew在调用子View的measure方法时计算好的
     * MeasureSpec： 测量规则类，由size和mode2个因素组成:
     * size: 就是指定的大小值
     * mode: MeasureSpec.AT_MOST : 对应的是warp_content;
     * MeasureSpec.EXACTLY : 对应的是具体的dp值，match_parent
     * MeasureSpec.UNSPECIFIED: 未定义的，一般用adapter的view的测量中
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //1.从widthMeasureSpec中反向获取父VIew计算好的size
        width = MeasureSpec.getSize(widthMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * return false说明你还没消费onTouch事件 return true说明你已经消费完了onTouch事件,onTouch 事件结束
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:// 按下
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                if (isShowWhole) {
                    // 处于显示全部状态，回调点击接口
                    if (onClickListener != null) {
                        onClickListener.onClick(this);
                    }
                } else {
                    // 处于显示缩小状态，执行显示全部动画
                    myHandler.sendEmptyMessage(FLAG_SHOW_WHOLE);
                }
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        // 此处返回true 该控件的 onClick 事件将不能响应
        return true;
    }

    public class MyHandler extends Handler {
        public void handleMessage(Message msg) {
            int[] location = new int[2];
            switch (msg.what) {
                case FLAG_SHOW_SHRINK:
                    // 向右移动半个控件宽度，显示缩小
                    isShowWhole = false;
                    setTranslationX(0);
                    getLocationOnScreen(location);
                    break;
                case FLAG_SHOW_WHOLE:
                    // 向左移动半个控件宽度,显示全部
                    isShowWhole = true;
                    setTranslationX(-(width / 2));
                    // 给自身发送缩小MSG，5秒后进行缩小
                    myHandler.sendEmptyMessageDelayed(FLAG_SHOW_SHRINK, 5000);
                    break;
            }
        }
    }

    public interface OnClickListener {
        void onClick(View view);
    }
}
