package com.topjet.common.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by tsj-004 on 2017/9/8.
 */

public class KeyboardUtil {
    protected InputMethodManager mInputMethodManager = null;
    public static final int SOFT_INPUT_SHOWING_DELAY = 350;

    public KeyboardUtil(Context context)
    {
        mInputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    /**
     * @param view
     */
    public void showSoftInput(View view) {
        mInputMethodManager.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    /**
     * 如果不能传入activity,则使用使用 toggleSoftInput()
     *
     * @param activity
     */
    public void hideSoftInputFromWindow(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            mInputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 打开输入法还是建议使用 showSoftInput(View view)
     */
    public void toggleSoftInput() {
        mInputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * @param view
     */
    public void showSoftInput(final View view, boolean showImmediately) {
        if (showImmediately) {
            showSoftInput(view);
            return;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showSoftInput(view);
            }
        }, SOFT_INPUT_SHOWING_DELAY);
    }

    /**
     * 如果不能传入activity,则使用使用 toggleSoftInput()
     *
     * @param activity
     */
    public void hideSoftInputFromWindow(final Activity activity, boolean showImmediately) {
        if (showImmediately) {
            hideSoftInputFromWindow(activity);
            return;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                hideSoftInputFromWindow(activity);
            }
        }, SOFT_INPUT_SHOWING_DELAY);
    }

    /**
     * 打开输入法还是建议使用 showSoftInput(View view)
     */
    public void toggleSoftInput(boolean showImmediately) {
        if (showImmediately) {
            toggleSoftInput();
            return;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                toggleSoftInput();
            }
        }, SOFT_INPUT_SHOWING_DELAY);
    }

    /**
     * @return
     */
    protected boolean isSoftInputActive() {
        return mInputMethodManager.isActive();
    }
}
