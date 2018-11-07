package com.topjet.common.widget.clip;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

import com.topjet.common.R;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.utils.FileUtils;
import com.topjet.common.utils.PathHelper;
import com.topjet.common.utils.StringUtils;
import com.topjet.common.utils.TimeUtils;
import com.topjet.common.utils.Toaster;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Date;

public class ClipImageViewActivity extends MvpActivity {
    private ClipImageLayout mClipImageLayout;
    private TextView tvTakePhoto;
    private TextView tvUsePhoto;
    private String filePicPath;

    @Override
    protected void initPresenter() {

    }

    @Override
    public int getLayoutResId() {
        noHasTitle();
        return R.layout.activity_clip_image;
    }

    @Override
    protected void initView() {
        Intent intent = this.getIntent();
        String url = intent.getStringExtra("url");
        String type = "s";
        String tempType = intent.getStringExtra("type");
        if (!StringUtils.isEmpty(tempType)) {
            type = tempType;
        }
        int padding = intent.getIntExtra("padding", 0);

        mClipImageLayout = (ClipImageLayout) findViewById(R.id.id_clipImageLayout);
        tvTakePhoto = (TextView) findViewById(R.id.tv_take_photo);
        tvUsePhoto = (TextView) findViewById(R.id.tv_use_photo);
        tvTakePhoto.setOnClickListener(clickListener);
        tvUsePhoto.setOnClickListener(clickListener);

        mClipImageLayout.setImageView(url, type, padding);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.tv_take_photo) {
                setResultAndFinish(RESULT_CANCELED);
            } else if (id == R.id.tv_use_photo) {
                final Bitmap bitmap = mClipImageLayout.clip();
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        if (bitmap != null) {
                            try {
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                InputStream is = new ByteArrayInputStream(baos.toByteArray());
                                filePicPath = PathHelper.externalPictures() + "/" + TimeUtils.getFormatDate(new Date(System.currentTimeMillis()), "yyyyMMddHHmmss");
                                boolean b = FileUtils.writeFile(filePicPath, is);// 以指定的路径保存
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toaster.showShortToast("处理图片发生异常！[" + e.getMessage() + "]");
                            }
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        bitmap.recycle();
                        Intent intent = new Intent();
                        intent.putExtra("filePath", filePicPath);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }.execute();
            }
        }
    };
}
