package com.topjet.common.widget.viewpagetitle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;


public class DynamicLine extends View {
    private float lineHeight;
    private int shaderColorEnd;
    private int shaderColorStart;
    private float startX, stopX;
    private Paint paint;
    private RectF rectF = new RectF(startX, 0, stopX, 0);

    private float lineWidth; //下划线的宽度

    public DynamicLine(Context context, int shaderColorStart, int shaderColorEnd, float lineHeight, float lineWidth) {
        this(context, null);
        this.shaderColorStart = shaderColorStart;
        this.shaderColorEnd = shaderColorEnd;
        this.lineHeight = lineHeight;
        this.lineWidth = lineWidth;
        init();
    }

    public DynamicLine(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DynamicLine(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    public void setShaderColorEnd(int shaderColorEnd) {
        this.shaderColorEnd = shaderColorEnd;
    }

    public void setShaderColorStart(int shaderColorStart) {
        this.shaderColorStart = shaderColorStart;
    }

    public void setLineHeight(int lineHeight) {
        this.lineHeight = lineHeight;
    }

    public void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(5);
        paint.setShader(new LinearGradient(0, 100, Tool.getScreenWidth(getContext()), 100, shaderColorStart, shaderColorEnd, Shader.TileMode.MIRROR));
    }

    public float getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) lineHeight, MeasureSpec.getMode(heightMeasureSpec));
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        rectF.set(startX, 0, stopX, 10);
        canvas.drawRoundRect(rectF, 1, 1, paint);
    }


    /**
     * 更新下划线长度
     *
     * @param startX
     * @param stopX
     */
    public void updateView(float startX, float stopX) {
        float w = stopX - startX; // 将宽度设置为指定宽度
        float offset;
        if (w > lineWidth) {
            offset = (w - lineWidth) / 2;
            this.startX = startX + offset;
            this.stopX = stopX - offset;
        } else {
            offset = (lineWidth - w) / 2;
            this.startX = startX - offset;
            this.stopX = stopX + offset;
        }

        invalidate();
    }

}
