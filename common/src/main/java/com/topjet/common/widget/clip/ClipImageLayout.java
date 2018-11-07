package com.topjet.common.widget.clip;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.RelativeLayout;

import com.topjet.common.utils.ImageUtil;

/**
 * http://blog.csdn.net/lmj623565791/article/details/39761281
 *
 * @author zhy
 */
public class ClipImageLayout extends RelativeLayout {

    private ClipZoomImageView mZoomImageView;
    private ClipImageBorderView mClipImageView;

    public ClipImageLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        mZoomImageView = new ClipZoomImageView(context);
        mClipImageView = new ClipImageBorderView(context);

        android.view.ViewGroup.LayoutParams lp = new LayoutParams(
                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.MATCH_PARENT);

        this.addView(mZoomImageView, lp);
        this.addView(mClipImageView, lp);
    }

    /**
     * 裁切图片
     *
     * @return
     */
    public Bitmap clip() {
        return mZoomImageView.clip();
    }


    /**
     * 设置裁剪图片
     *
     * type s 正方形，左右边距0
     * type r 矩形  -》 宽高比 = 3 ：5
     */
    public void setImageView(String url, String type, int mHorizontalPadding) {
        // 计算padding的px
        mHorizontalPadding = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, mHorizontalPadding, getResources()
                        .getDisplayMetrics());

        mZoomImageView.setType(type);
        mZoomImageView.setHorizontalPadding(mHorizontalPadding);
        mClipImageView.setType(type);
        mClipImageView.setHorizontalPadding(mHorizontalPadding);

        // 设置图片
        Bitmap bitmap = ImageUtil.getBitmap(url, 480, 800);
        Drawable drawable = new BitmapDrawable(bitmap);
        mZoomImageView.setImageDrawable(drawable);
//        mZoomImageView.setImageBitmap(bitmap);
    }

}
