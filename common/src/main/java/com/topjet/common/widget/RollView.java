package com.topjet.common.widget;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ViewFlipper;

import com.topjet.common.R;
import com.topjet.common.utils.ListUtils;
import com.topjet.common.utils.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by blue on 2015/7/19.
 */
public class RollView extends ViewFlipper {
    // 每次滚动的时间间隔
    private int mDelayed;
    // 判断滚动是否结束
    private boolean isStart;
    // 判断 mHandler 是否开始
    private boolean isStart_mHandler;
    // 需要滚动的View集合
    private List<View> mViews;
    // 处理View滚动刷新
    private Handler mHandler;

    public RollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RollView(Context context) {
        super(context);
        init();
    }

    private void init() {
        mViews = new ArrayList<View>();
        mHandler = new Handler();
        setEnterAndExitAnimation();
    }

    /**
     * 设置淡入淡出动画
     */
    public void setEnterAndExitAnimation() {
        setInAnimation(getContext(), R.anim.slide_in_bottom);
        setOutAnimation(getContext(), R.anim.slide_out_top);
    }

    public RollView setAdapter(List<View> views) {
        if (views.size() > 0) {
            mViews = views;
        }

        removeAllViews();
        for (View v : mViews) {
            addView(v);
        }

        return this;
    }

    public RollView setDelayed(int delayed) {
        mDelayed = delayed;
        return this;
    }

    public void start() {
        isStart = true;
        if (!isStart_mHandler) {
            isStart_mHandler = true;
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (isStart) {
                        if (!ListUtils.isEmpty(mViews) && mViews.size() > 1) {
                            Logger.d("公告显示 isStart_mHandler ");
                            showNext();
                        }
                        mHandler.postDelayed(this, mDelayed);
                    }
                }
            }, mDelayed);
        }
    }

    public void stop() {
        isStart = false;
        // 执行stop时需要清空消息队列，否则多次执行start，Handler中会存在多个循环Runnable
        mHandler.removeCallbacksAndMessages(null);
        isStart_mHandler = false;
    }

    public void destroy() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
    }
}
