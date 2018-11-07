package com.foamtrace.photopicker;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by tsj005 on 2017/3/31.
 */
public class ToasterManager {

    public TextView title;
    public Toast mToast;// 如果定义成静态的变量，会造成内存泄露。

    public ToasterManager(Context context) {
        mToast = new Toast(context.getApplicationContext());

        LayoutInflater inflate = (LayoutInflater)
                context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflate.inflate(R.layout.toast, null);
        title = (TextView) layout.findViewById(R.id.message_textview);


        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.setView(layout);

    }

    /**
     * @param message Toast的信息
     */
    public void showToast(String message) {
        if (null != mToast) {
            title.setText(message);
            mToast.show();
        }
    }

    public void destroy() {
        mToast = null;
    }
}
