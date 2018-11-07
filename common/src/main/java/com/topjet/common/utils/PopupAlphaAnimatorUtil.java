package com.topjet.common.utils;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.view.Window;
import android.view.WindowManager;

import com.topjet.common.R;

/**
 * Created by tsj-004 on 2017/9/3.
 * popupwindow渐变动画类
 */

public class PopupAlphaAnimatorUtil {
    private static float MIN_ALPHA = 0.4f;
    /**
     * 背景透明度渐变动画
     * tsj-004 2017年8月10日 22:23:49
     */
    public static void setAlphaAnim(Activity activity, float startValue, float toValue)
    {
        final Window activityWindow = activity.getWindow();
        final WindowManager.LayoutParams lp = activityWindow.getAttributes();
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(startValue, toValue);
        int duration = activity.getResources().getInteger(R.integer.activity_animation_time);
        valueAnimator.setDuration(duration);
        valueAnimator.setRepeatCount(0);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                lp.alpha = (float)animation.getAnimatedValue();
                activityWindow.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                activityWindow.setAttributes(lp);
            }
        });
        valueAnimator.start();
    }

    /**
     * 进入动画--透明色变成MIN_ALPHA
     */
    public static void setInAlphaAnim(Activity activity)
    {
        setAlphaAnim(activity, 1.0f, MIN_ALPHA);
    }

    /**
     * 退出动画--透明色MIN_ALPHA变成1.0
     */
    public static void setOutAlphaAnim(Activity activity)
    {
        setAlphaAnim(activity, MIN_ALPHA, 1.0f);
    }
}
