package com.topjet.common.common.view.activity;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.topjet.common.R;
import com.topjet.common.utils.CameraUtil;

/**
 * 显示图片，这个类只是暂时的
 * 正式项目中删掉
 */
public class ShowPicActivity extends Activity {

    private ImageView img;
    private int picWidth;
    private int picHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_pic);

        picWidth = getIntent().getIntExtra(CameraUtil.KEY.PIC_WIDTH, 0);
        picHeight = getIntent().getIntExtra(CameraUtil.KEY.PIC_HEIGHT, 0);
        img = (ImageView)findViewById(R.id.img);
        img.setImageURI(Uri.parse(getIntent().getStringExtra(CameraUtil.KEY.IMG_PATH)));
        img.setLayoutParams(new RelativeLayout.LayoutParams(picWidth, picHeight));
    }
}
