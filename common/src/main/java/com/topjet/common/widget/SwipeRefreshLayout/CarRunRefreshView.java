package com.topjet.common.widget.SwipeRefreshLayout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.topjet.common.utils.ImageUtil;

/**
 * 卡车奔跑动画
 * 图片在common/src/main/assest
 * <p>
 * 1. 下拉刷新
 * 2. 上拉更多
 */
public class CarRunRefreshView extends RefreshView {
    private Context context = null;
    private int curIndex = 0;

    public CarRunRefreshView(Context context, int type) {
        super(context, type);

        this.context = context;

        initBitmap();
        initPaint();
    }

    private int getCenterX() {
        return getMeasuredWidth() / 2;
    }

    private int getCenterY() {
        return getMeasuredHeight() / 2;
    }

    Bitmap bitmap;
    int bitmapW, bitmapH;
    private float rotation;

    private void initBitmap() {
        String curIndexStr = curIndex + "";
        if (curIndex > 29) {
            curIndex = 0;
        }
        if (curIndex < 10) {
            curIndexStr = "0" + curIndex;
        }
        bitmap = ImageUtil.getImageFromAssetsFile(context, "icon_person_center_refre_" + curIndexStr + ".png");
        curIndex++;
        bitmapW = bitmap.getWidth();
        bitmapH = bitmap.getHeight();
    }

    Paint mPaint = new Paint();

    private void initPaint() {
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.WHITE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        initBitmap();

        int offsetX = bitmapW / 2;
        int offsetY = bitmapH / 2;
        canvas.drawBitmap(bitmap, getCenterX() - offsetX, getCenterY() - offsetY, mPaint);
    }
}