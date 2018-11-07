package com.topjet.common.widget.clip;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * @author zhy
 *         http://blog.csdn.net/lmj623565791/article/details/39761281
 *         <p>
 *         四周半透明，中间白色框
 */
public class ClipImageBorderView extends View {
    /**
     * 水平方向与View的边距
     */
    private int mHorizontalPadding = 0;
    /**
     * 垂直方向与View的边距
     */
    private int mVerticalPadding = 0;
    /**
     * type s 正方形，左右边距0
     * type r 矩形  -》 宽高比 = 3 ：5
     */
    private String type = "s";

    /**
     * 边框的颜色，默认为白色
     */
    private int mBorderColor = Color.parseColor("#FFFFFF");
    /**
     * 边框的宽度 单位dp
     */
    private int mBorderWidth = 1;

    private Paint mPaint;

    public ClipImageBorderView(Context context) {
        this(context, null);
    }

    public ClipImageBorderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClipImageBorderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mBorderWidth = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, mBorderWidth, getResources()
                        .getDisplayMetrics());
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int viewWidth = getWidth();
        int viewHeight = getHeight();
        if (type.equalsIgnoreCase("s")) {
            int mWidth = viewWidth - 2 * mHorizontalPadding;
            mVerticalPadding = (viewHeight - mWidth) / 2;
            if (mVerticalPadding < 0) {
                mVerticalPadding = 0;
            }
        } else if (type.equalsIgnoreCase("r")) {
            int mWidth = viewWidth - 2 * mHorizontalPadding;
            int mHeight = (int) (mWidth / 3.0f * 5);
            mVerticalPadding = (viewHeight - mHeight) / 2;
            if (mVerticalPadding < 0) {
                mVerticalPadding = 0;
            }
        }
        mPaint.setColor(Color.parseColor("#aa000000"));
        mPaint.setStyle(Style.FILL);
        // 绘制左边1
        canvas.drawRect(0, 0, mHorizontalPadding, viewHeight, mPaint);
        // 绘制右边2
        canvas.drawRect(viewWidth - mHorizontalPadding, 0, viewWidth,
                viewHeight, mPaint);
        // 绘制上边3
        canvas.drawRect(mHorizontalPadding, 0, viewWidth - mHorizontalPadding,
                mVerticalPadding, mPaint);
        // 绘制下边4
        canvas.drawRect(mHorizontalPadding, viewHeight - mVerticalPadding,
                viewWidth - mHorizontalPadding, viewHeight, mPaint);
        // 绘制外边框
        mPaint.setColor(mBorderColor);
        mPaint.setStrokeWidth(mBorderWidth);
        mPaint.setStyle(Style.STROKE);
        canvas.drawRect(mHorizontalPadding, mVerticalPadding, viewWidth
                - mHorizontalPadding, viewHeight - mVerticalPadding, mPaint);
    }

    public void setHorizontalPadding(int horizontalPadding) {
        if (horizontalPadding < 0) {
            horizontalPadding = 0;
        }
        this.mHorizontalPadding = horizontalPadding;
    }

    public void setType(String type) {
        this.type = type;
    }
}
