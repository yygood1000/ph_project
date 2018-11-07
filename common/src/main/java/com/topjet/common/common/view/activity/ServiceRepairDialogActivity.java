package com.topjet.common.common.view.activity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.topjet.common.R;
import com.topjet.common.App;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.SystemUtils;

/**
 * 服务器维护对话框Activity
 * Created by tsj004 on 2017/8/9.
 */

public class ServiceRepairDialogActivity extends Activity {
    TextView tvContent;
    ImageView ivClose;
    Button btnSingleConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_repair_dialog);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setFinishOnTouchOutside(false);
        }

        tvContent = (TextView) findViewById(R.id.tv_content);
        tvContent.setMovementMethod(ScrollingMovementMethod.getInstance());
        btnSingleConfirm = (Button) findViewById(R.id.btn_single_confirm);
        btnSingleConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Logger.i("=======exit=======", "强制退出");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            SystemUtils.killProcess(App.getInstance());
                        }
                    }, 300);
                } catch (Exception e) {
                    Logger.i("=======exit=======", ""+e.getMessage());
                }
            }
        });
        ivClose = (ImageView) findViewById(R.id.iv_close);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();//关闭当前对话框
            }
        });
    }

    /**
     * 重写返回键事件
     */
    @Override
    public void onBackPressed() {

    }
}
