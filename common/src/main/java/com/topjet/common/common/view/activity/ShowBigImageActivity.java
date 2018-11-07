package com.topjet.common.common.view.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.topjet.common.R;
import com.topjet.common.utils.AppManager;
import com.topjet.common.utils.GlideUtils;
import com.topjet.common.utils.Logger;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * creator: zhulunjun
 * time:    2017/9/6
 * describe: 查看大图
 */

public class ShowBigImageActivity extends AppCompatActivity {

    public static final String IMAGE_KEY = "image_key";
    public static final String IMAGE_URL = "image_url";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_big_image);

        final PhotoView imageView = (PhotoView) findViewById(R.id.pv_image);
        String key = getIntent().getStringExtra(IMAGE_KEY);
        String url = getIntent().getStringExtra(IMAGE_URL);
        imageView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
                public void onPhotoTap(View view, float x, float y) {
                    //直接调用finish没有共享视图动画
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        finishAfterTransition();
                    }else{
                        finish();
                    }
                }
        });
        GlideUtils.loaderImage(
                url,
                key,
                R.drawable.shape_avatar_loading,
                R.drawable.shape_avatar_loading,
                imageView,
                new GlideDrawableImageViewTarget(imageView) {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                        super.onResourceReady(resource, animation);
                        // 这里指定了被共享的视图元素
                        ViewCompat.setTransitionName(imageView, "image");
                    }
                });


    }

    /**
     * 查看大图
     *
     * @param activity
     * @param transitionView
     * @param key
     * @param url
     */
    public static void showImage(AppCompatActivity activity, View transitionView, String key, String url) {
        Intent intent = new Intent(activity, ShowBigImageActivity.class);
        intent.putExtra(IMAGE_KEY, key);
        intent.putExtra(IMAGE_URL, url);
        // 这里指定了共享的视图元素
        ActivityOptionsCompat options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(activity, transitionView, "image");
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }

}
