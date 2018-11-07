package com.topjet.common.common.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.topjet.common.R;
import com.topjet.common.base.busManger.BusManager;
import com.topjet.common.common.modle.event.NoviceBootDialogCloseEvent;
import com.topjet.common.config.CMemoryData;

/**
 * 新手引导
 * Created by tsj004 on 2017/8/17.
 */

public class NoviceBootDialogActivity extends Activity {

    private ImageView ivShipper1;
    private ImageView ivShipper2;
    private ImageView ivShipper3;

    private ImageView ivDriver1;
    private ImageView ivDriver2;
    private ImageView ivDriver3;

    private LinearLayout llShipper;
    private LinearLayout llDriver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novice_boot_dialog);

        ivShipper1 = (ImageView) findViewById(R.id.iv_shipper_novice_boot_1);
        ivShipper2 = (ImageView) findViewById(R.id.iv_shipper_novice_boot_2);
        ivShipper3 = (ImageView) findViewById(R.id.iv_shipper_novice_boot_3);

        ivDriver1 = (ImageView) findViewById(R.id.iv_driver_novice_boot_1);
        ivDriver2 = (ImageView) findViewById(R.id.iv_driver_novice_boot_2);
        ivDriver3 = (ImageView) findViewById(R.id.iv_driver_novice_boot_3);

        llShipper = (LinearLayout) findViewById(R.id.ll_shipper);
        llDriver = (LinearLayout) findViewById(R.id.ll_driver);

        ivShipper1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivShipper1.setVisibility(View.GONE);
                ivShipper2.setVisibility(View.VISIBLE);
                ivShipper3.setVisibility(View.GONE);
            }
        });

        ivShipper2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivShipper1.setVisibility(View.GONE);
                ivShipper2.setVisibility(View.GONE);
                ivShipper3.setVisibility(View.VISIBLE);
            }
        });

        ivShipper3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();//关闭当前对话框
                BusManager.getBus().post(new NoviceBootDialogCloseEvent());
            }
        });

        ivDriver1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivDriver1.setVisibility(View.GONE);
                ivDriver2.setVisibility(View.VISIBLE);
                ivDriver3.setVisibility(View.GONE);
            }
        });

        ivDriver2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivDriver1.setVisibility(View.GONE);
                ivDriver2.setVisibility(View.GONE);
                ivDriver3.setVisibility(View.VISIBLE);
            }
        });

        ivDriver3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();//关闭当前对话框
                BusManager.getBus().post(new NoviceBootDialogCloseEvent());
            }
        });

        showImg();
    }


    /**
     * 显示接货版的还是发货版的
     */
    private void showImg() {
        if (CMemoryData.isDriver()) {
            llShipper.setVisibility(View.GONE);
            llDriver.setVisibility(View.VISIBLE);
        } else {
            llShipper.setVisibility(View.VISIBLE);
            llDriver.setVisibility(View.GONE);
        }
    }
}
