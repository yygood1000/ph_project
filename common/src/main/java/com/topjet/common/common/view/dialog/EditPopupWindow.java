package com.topjet.common.common.view.dialog;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.topjet.common.R;
import com.topjet.common.utils.StringUtils;

/**
 * Created by tsj-004 on 2017/8/26.
 * 只有一个输入框的popupwindow
 */

public class EditPopupWindow extends PopupWindow {
    private boolean clickEnter = false;

    /**
     * 显示
     */
    public void showPopupWindow(final Activity activity, View view, String defaultStr, final OnEditPopupDismissListener editPopupDismissListener) {
        View parent = View.inflate(activity, R.layout.pop_edit, null);
        this.setContentView(parent);

        // 获取控件
        final EditText et = (EditText) parent.findViewById(R.id.et);
        if (StringUtils.isBlank(defaultStr)) {
            defaultStr = "";
        }
        et.setText(defaultStr);
        et.setSelection(defaultStr.length());

        // 设置窗口属性
        this.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        this.setHeight(WindowManager.LayoutParams.MATCH_PARENT);
        this.setBackgroundDrawable(new BitmapDrawable());
        this.setFocusable(true);
        this.setTouchable(true);
        this.setOutsideTouchable(true);
//        this.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);   // 让pop覆盖在输入法上面
//        this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE); // 让pop自适应输入状态，收缩布局，以免editText被挡
        this.setClippingEnabled(false);     // 可以超出屏幕，状态栏终于可以变半透明啦

        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    clickEnter = true;
                    InputMethodManager imm = (InputMethodManager) activity.getSystemService(Service.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
                    dismiss();
                }
                return true;
            }
        });

        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                if (clickEnter) {
                    clickEnter = false;
                    editPopupDismissListener.editPopDismiss(et.getText().toString().replaceAll(" ", ""));
                }
            }
        });

        this.showAtLocation(view, Gravity.CENTER, 0, 0);
        popupInputMethodWindow(activity, et);

        activity.getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        Rect r = new Rect();
                        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
                        int screenHeight = activity.getWindow().getDecorView().getRootView().getHeight();
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) et.getLayoutParams();
                        layoutParams.setMargins(0, 0, 0, screenHeight - r.bottom);
                        et.setLayoutParams(layoutParams);
                    }
                });
    }

    /**
     * 弹出输入法
     */
    private void popupInputMethodWindow(final Context context, EditText et) {
        et.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Service.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
            }
        }, 0);
    }

    public interface OnEditPopupDismissListener {
        void editPopDismiss(String result);
    }
}
