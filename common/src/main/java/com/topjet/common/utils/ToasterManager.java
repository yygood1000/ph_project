package com.topjet.common.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.topjet.common.R;

/**
 * Created by yy on 2017/3/31.
 * Toast
 */
public class ToasterManager {

    public TextView title;
    public Toast mToast;// 如果定义成静态的变量，会造成内存泄露。

    public ToasterManager(Context context) {
        mToast = new Toast(context.getApplicationContext());

        View layout = LayoutInflater.from(context).inflate(R.layout.layout_supertoast, null);
        title = (TextView) layout.findViewById(R.id.tv_message);

        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.setView(layout);
    }

    /**
     * @param message Toast的信息
     */
    public void showToast(CharSequence message, int duration) {
        if (null != mToast) {
            mToast.setDuration(duration);
            title.setText(message);
            mToast.show();
        }
    }

    public void destroy() {
        mToast = null;
    }
}
