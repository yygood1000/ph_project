package com.topjet.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Picture;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebView;

import com.topjet.common.R;
import com.topjet.common.utils.ResourceUtils;

/**
 * 圆角webview
 */
public class CornersWebView extends MyWebView implements MyWebView.OnLoadFinishListener, WebView.PictureListener {
    private float top_left = 0;
    private float top_right = 0;
    private float bottom_left = 0;
    private float bottom_right = 0;
    private int vWidth;
    private int vHeight;
    private int x;
    private int y;
    private Paint paint2;

    private float[] radiusArray = {0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f};

    public CornersWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CornersWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        paint2 = new Paint();
        paint2.setColor(Color.WHITE);
        paint2.setAntiAlias(true);
        paint2.setXfermode(null);

        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CornersWebView); // 读取xml styleable，attrs是xml属性的集合

        top_left = a.getDimension(R.styleable.CornersWebView_top_left, 0);
        top_right = a.getDimension(R.styleable.CornersWebView_top_right, 0);
        bottom_left = a.getDimension(R.styleable.CornersWebView_bottom_left, 0);
        bottom_right = a.getDimension(R.styleable.CornersWebView_bottom_right, 0);
        a.recycle();

        setRadius(top_left, top_right, bottom_right, bottom_left);
        this.setBackgroundColor(ResourceUtils.getColor(R.color.transparent_100));
        this.setOnLoadFinishListener(this);
        this.setPictureListener(this);
        this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    /**
     * 设置四个角的圆角半径
     */
    public void setRadius(float leftTop, float rightTop, float rightBottom, float leftBottom) {
        radiusArray[0] = leftTop;
        radiusArray[1] = leftTop;
        radiusArray[2] = rightTop;
        radiusArray[3] = rightTop;
        radiusArray[4] = rightBottom;
        radiusArray[5] = rightBottom;
        radiusArray[6] = leftBottom;
        radiusArray[7] = leftBottom;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        vWidth = getMeasuredWidth();

        this.getLayoutParams().height = (int)(vWidth / 82.0f * 75);
        vHeight = getMeasuredHeight();
    }

    @Override
    public void onDraw(Canvas canvas) {
        x = this.getScrollX();
        y = this.getScrollY();
        vWidth = getMeasuredWidth();
        vHeight = getMeasuredHeight();

        Path path = new Path();
        path.addRoundRect(new RectF(0, y, x + vWidth, y + vHeight), radiusArray, Path.Direction.CW);        // 使用半角的方式，性能比较好
        canvas.clipPath(path);
        canvas.drawPath(path, paint2);
        super.onDraw(canvas);
    }

    @Override
    public void onLoadFinish() {
        postInvalidate();
    }

    @Override
    public void onNewPicture(WebView view, Picture picture) {
        postInvalidate();
    }
}