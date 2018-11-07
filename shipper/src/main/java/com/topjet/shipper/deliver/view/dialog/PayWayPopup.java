package com.topjet.shipper.deliver.view.dialog;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lvfq.pickerview.adapter.ArrayWheelAdapter;
import com.lvfq.pickerview.lib.WheelView;
import com.topjet.common.R;
import com.topjet.common.utils.KeyboardUtil;
import com.topjet.common.utils.ScreenUtils;

import java.util.List;

/**
 * Created by tsj-004 on 2017/8/24.
 *
 * 支付方式  滚轮选择
 */

public class PayWayPopup {
    /**
     * 底部滚轮点击事件回调
     */
    public interface OnWheelViewClick {
        void onWheelViewSelected(View view, int postion, String tag); // 支付方式滚轮选择回调（点击确定时回调）
    }

    /**
     * 弹出底部滚轮选择
     */
    public static void alertBottomWheelOption(Activity activity, List<?> list, final OnWheelViewClick click,
        final int defaultIndex, final String tag) {
        new KeyboardUtil(activity).hideSoftInputFromWindow(activity);       // 隐藏输入法

        final PopupWindow popupWindow = new PopupWindow();
        View view = LayoutInflater.from(activity).inflate(R.layout.layout_bottom_wheel_option, null);
        TextView tv_confirm = (TextView) view.findViewById(R.id.btnSubmit);
        final WheelView wv_option = (WheelView) view.findViewById(R.id.wv_option);
        wv_option.setAdapter(new ArrayWheelAdapter(list));
        wv_option.setCyclic(false);
        wv_option.setTextSize(20);
        wv_option.setCurrentItem(defaultIndex);
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                click.onWheelViewSelected(view, wv_option.getCurrentItem(), tag);
            }
        });

        view.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int top = view.findViewById(R.id.ll_container).getTop();
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    int y = (int) motionEvent.getY();
                    if (y < top) {
                        popupWindow.dismiss();
                    }
                }
                return true;
            }
        });
        popupWindow.setContentView(view);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setClippingEnabled(false);     // 可以超出屏幕，状态栏终于可以变半透明啦
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.showAtLocation(((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0), Gravity
                .BOTTOM, 0, 0);

        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view.getLayoutParams();
        layoutParams.setMargins(0, 0, 0, ScreenUtils.getNavigationBarHeight(activity));
    }

    /**
     * 弹出底部滚轮选择
     */
    public static void alertBottomWheelOption(Activity activity, List<?> list, final OnWheelViewClick click) {
        alertBottomWheelOption(activity, list, click, 1, "");
    }
}
