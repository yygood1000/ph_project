package com.topjet.common.utils;

import android.content.Context;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.topjet.common.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * author: zhulunjun
 * time:   2017/6/29
 * about:  基于原生的Toast封装的Toast
 * 1.可自定义动画
 * 2.可自定义显示时间
 *
 * 2017/07/13 修复7.1.1（25）以后不显示的问题
 */

public class MyToast {

    public static final int LENGTH_ALWAYS = 0;//小于等于0则一直显示
    public static final int LENGTH_SHORT = 2; //显示 2 秒
    public static final int LENGTH_LONG = 4;  //显示 4 秒

    private Toast mToast;
    private Context mContext;
    private int mDuration = LENGTH_SHORT;
    private CharSequence showText;
    private int animations = -1;
    private boolean isShow = false;

    //反射对象
    private Object mTN;
    private Method show;
    private Method hide;

    private TextView mMessageTextView;//显示的文本

    private boolean isHaveArguments = false; //show()是否带参数

    //隐藏Toast
    private Runnable hideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };


    //消息管理
    final Handler mHandler = new Handler();

    public MyToast(Context mContext) {
        this.mContext = mContext;
        if (mToast == null) {
            mToast = new Toast(mContext);
        }
    }

    /**
     * 反射调用原生 Toast 的显示方法
     * 并设置隐藏的时间
     */
    public void show() {
        if (isShow) return;
        initToastView();
        initTN();

        try {

            if(isHaveArguments){
                Logger.d("弹窗Toaset 55");
                mToast.setDuration(Toast.LENGTH_LONG);
                mToast.show();
                Logger.d("弹窗Toaset 88");
                return;
            }else {
                Logger.d("弹窗Toaset 44");
                show.invoke(mTN);
            }
        } catch (Exception e) {
            Logger.d("弹窗Toaset 66"+e.getMessage());
            e.printStackTrace();
            //默认显示
            mToast.setDuration(Toast.LENGTH_LONG);
            mToast.show();
        }
        Logger.d("弹窗Toaset 77"+show);
        isShow = true;
        //判断duration，如果大于#LENGTH_ALWAYS 则设置消失时间
        if (mDuration > LENGTH_ALWAYS) {
            mHandler.postDelayed(hideRunnable, mDuration * 1000);
        }
    }

    /**
     * 反射调用 原生Toast 的隐藏
     */
    public void hide() {
        if (!isShow) return;
        try {
            hide.invoke(mTN);
        } catch (Exception e) {
            e.printStackTrace();
        }
        isShow = false;
    }

    /**
     * 初始化Toast的显示视图
     */
    public void initToastView() {

        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_supertoast, null);
        mMessageTextView = (TextView) view.findViewById(R.id.tv_message);
        if(showText !=null)
            mMessageTextView.setText(showText);
        mToast.setView(view);
    }

    /**
     * 1. 通过反射拿到原生 Toast 的显示和隐藏方法对象
     * 2. 并设置动画
     */
    private void initTN() {
        try {
            Field tnField = mToast.getClass().getDeclaredField("mTN");
            tnField.setAccessible(true);
            mTN = tnField.get(mToast);
            /**
             * API 25（7.1.1）以后，Toast的源码被修改了
             * 25以前 show()
             * 25以后 show(IBinder windowToken)
             *
             * 需要传入windowToken对象
             */
            try{
                Logger.d("弹窗Toaset 11");
                show = mTN.getClass().getMethod("show");
            }catch (NoSuchMethodException e) {
                Logger.d("弹窗Toaset 22"+e.getMessage());
                isHaveArguments = true;
                show = mTN.getClass().getMethod("show", IBinder.class);
                Logger.d("弹窗Toaset 33"+show);
            }
            try {
                hide = mTN.getClass().getMethod("hide");
            } catch (Exception e){
                isHaveArguments = true;
                Logger.d("弹窗Toaset 55"+e.getMessage());
            }
            /** 设置动画*/
            if (animations != -1) {
                Field tnParamsField = mTN.getClass().getDeclaredField("mParams");
                tnParamsField.setAccessible(true);
                WindowManager.LayoutParams params = (WindowManager.LayoutParams) tnParamsField.get(mTN);
                params.windowAnimations = animations;
            }

            /**调用tn.show()之前一定要先设置mNextView*/
            Field tnNextViewField = mTN.getClass().getDeclaredField("mNextView");
            tnNextViewField.setAccessible(true);
            tnNextViewField.set(mTN, mToast.getView());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     * 设置动画
     *
     * @param animations
     */
    public void setAnimations(int animations) {
        this.animations = animations;
    }

    /**
     * 设置显示文字
     * @param text
     */
    public void setText(CharSequence text){
        this.showText = text;
    }

    public CharSequence getText(){
        return showText;
    }

    /**
     * 设置显示位置
     * @param gravity
     * @param xOffset
     * @param yOffset
     */
    public void setGravity(int gravity, int xOffset, int yOffset) {
        if (mToast != null) {
            mToast.setGravity(gravity, xOffset, yOffset);
        }
    }

    /**
     * 设置显示时长
     * 单位：秒
     * @param duration
     */
    public void setDuration(int duration){
        this.mDuration = duration;
    }

    public void setBackground(int background){
        this.mMessageTextView.setBackgroundResource(background);
    }

}
