package com.topjet.common.order_detail.view.dialog;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lvfq.pickerview.adapter.ArrayWheelAdapter;
import com.lvfq.pickerview.lib.WheelView;
import com.topjet.common.R;
import com.topjet.common.utils.KeyboardUtil;

import java.util.ArrayList;

/**
 * Created by yy on 2017/8/24.
 * <p>
 * 撤销订单
 */

public class CancelGoodsPopup {

    public static String REASON_1 = "地址需要改动";
    public static String REASON_2 = "用车时间要改";
    public static String REASON_3 = "不需要了";

    /**
     * 底部滚轮点击事件回调
     */
    public interface OnConfirmListener {
        void onClickConfirm(int position, String remark);// 确认按钮点击事件
    }

    /**
     * 弹出底部滚轮选择
     */
    public static void showPopWindow(Activity activity, final OnConfirmListener listener) {
        new KeyboardUtil(activity).hideSoftInputFromWindow(activity);       // 隐藏输入法
        final PopupWindow popupWindow = new PopupWindow();
        View view = LayoutInflater.from(activity).inflate(R.layout.pop_cancel_goods, null);
        final WheelView wv_option = (WheelView) view.findViewById(R.id.wv_option);
        TextView tvConfirm = (TextView) view.findViewById(R.id.tv_confirm);
        TextView tvCancel = (TextView) view.findViewById(R.id.tv_cancel);

        final ArrayList<String> dataLsit = new ArrayList<>();
        dataLsit.add(REASON_1);
        dataLsit.add(REASON_2);
        dataLsit.add(REASON_3);

        wv_option.setAdapter(new ArrayWheelAdapter<>(dataLsit));
        wv_option.setCyclic(false);
        wv_option.setTextSize(20);
        wv_option.setCurrentItem(2);

        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClickConfirm(wv_option.getCurrentItem(), dataLsit.get(wv_option.getCurrentItem()));
                popupWindow.dismiss();
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popupWindow.setContentView(view);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setClippingEnabled(false);     // 不可以超出屏幕
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.showAtLocation(((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0), Gravity
                .BOTTOM, 0, 0);

    }

}
