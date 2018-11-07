package com.topjet.common.widget.SwipeRefreshLayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.topjet.common.R;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.utils.ScreenUtils;
import com.topjet.common.utils.StringUtils;


/**
 * the default implementation class of the interface IRefreshStatus, and the class should always be rewritten
 * 1. 下拉刷新
 * 2. 上拉更多
 */
public class RefreshView extends View implements IRefreshStatus {

    private static final int MAX_ARC_DEGREE = 330;
    private static final int ANIMATION_DURATION = 300;
    private static final int DEFAULT_START_DEGREES = 285;
    private static final int DEFAULT_STROKE_WIDTH = 2;
    private static final float DEFAULT_SWIPE_DEGREES = 0.01f;

    public static final int REFRESH = 11; // 下拉刷新
    public static final int LOAD_MORE = 22; // 上拉更多

    private int refreshType = REFRESH;

    Context mContext;

    private final RectF mArcBounds = new RectF();
    private final Paint mPaint = new Paint();
    private final Paint mTextPaint = new Paint();

    private float mStartDegrees;
    private float mSwipeDegrees;

    private float pullDistance;
    private float pullProgress;

    private float mStrokeWidth;

    private boolean mHasTriggeredRefresh;

    private ValueAnimator mRotateAnimator;

    private String strPull = "下拉刷新";
    private String strIng = "正在刷新";
    private String strs = "松开刷新";
    private String strEnd = "刷新成功";
    private String emptyStr = "无数据";
    private String refreshText = strIng;

    public RefreshView(Context context, int type) {
        this(context, null);
        this.refreshType = type;
        this.mContext = context;
        isLoadEnd = false;
        refreshText = strIng = refreshType == REFRESH ? "正在刷新" : "正在加载";

        initData();
        initBitmap();
        initPaint();
    }

    public RefreshView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initData();
        initBitmap();
        initPaint();
    }

    private void initData() {
        float density = getResources().getDisplayMetrics().density;
        mStrokeWidth = density * DEFAULT_STROKE_WIDTH;

        mStartDegrees = DEFAULT_START_DEGREES;
        mSwipeDegrees = DEFAULT_SWIPE_DEGREES;
    }

    private void initPaint() {
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(mStrokeWidth);

        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(0xff969696);
        mTextPaint.setTextSize(ScreenUtils.dp2px(mContext, 14));
        mTextPaint.setStrokeWidth(mStrokeWidth);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

    }

    private void startAnimator() {
        mRotateAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
        mRotateAnimator.setInterpolator(new LinearInterpolator());
        mRotateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float rotateProgress = (float) animation.getAnimatedValue();
                rotation = rotateProgress * 360;
                setStartDegrees(DEFAULT_START_DEGREES + rotateProgress * 360);
            }
        });
        mRotateAnimator.setRepeatMode(ValueAnimator.RESTART);
        mRotateAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mRotateAnimator.setDuration(ANIMATION_DURATION);
        mRotateAnimator.start();
    }

    private void resetAnimator() {
        if (mRotateAnimator != null) {
            mRotateAnimator.cancel();
            mRotateAnimator.removeAllUpdateListeners();
            mRotateAnimator = null;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawArc(canvas);
        drawText(canvas);

    }

    @Override
    protected void onDetachedFromWindow() {
        resetAnimator();
        super.onDetachedFromWindow();
    }

    /**
     * 画图片
     */
    private void drawArc(Canvas canvas) {
        if (isLoadEnd) {
            if (isEmpty) {
                setRefreshText(emptyStr);
            } else {
                setRefreshText(getContext().getResources().getString(R.string.load_end));
            }
//            cleanCanvas(canvas, mPaint);
        } else {
            drawRotateBitmap(canvas, mPaint, bitmap, rotation, getCenterX() - bitmapW, 5);
        }

    }


    /**
     * 画文本
     */
    private void drawText(Canvas canvas) {
        Rect bounds = new Rect();
        setBackgroundColor(Color.TRANSPARENT);
        mTextPaint.getTextBounds(refreshText, 0, refreshText.length(), bounds);
        int marginLeft = isLoadEnd ? 0 : ScreenUtils.dp2px(mContext, 35);
        if (isEmpty) {
            canvas.drawText(refreshText, getCenterX(), getCenterY(), mTextPaint);
        } else {
            canvas.drawText(refreshText, getCenterX() - bounds.width() / 2 + bitmapW + marginLeft, getCenterY(),
                    mTextPaint);
        }
    }

    public void setRefreshText(String refreshText) {
        this.refreshText = refreshText;
    }

    private boolean isLoadEnd = false;

    public void showLoadEnd() {
        isLoadEnd = true;
        postInvalidate();
    }

    private boolean isEmpty;

    public void showEmpty(String emptyStr) {
        if (StringUtils.isNotBlank(emptyStr)) {
            this.emptyStr = emptyStr;
        }
        isEmpty = true;
        isLoadEnd = true;
        postInvalidate();
    }

    public boolean isLoadEnd() {
        return isLoadEnd;
    }

    public void setLoadEnd(boolean loadEnd) {
        isLoadEnd = loadEnd;
    }

    /**
     * 清空画布
     *
     * @param canvas
     * @param paint
     */
    public void cleanCanvas(Canvas canvas, Paint paint) {
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        canvas.drawPaint(paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
        canvas.drawColor(getResources().getColor(R.color.common_background));
        invalidate();
    }

    private int getCenterX() {
        return getMeasuredWidth() / 2;
    }

    private int getCenterY() {
        Paint.FontMetricsInt fontMetrics = mTextPaint.getFontMetricsInt();
        return (getMeasuredHeight() - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top - 5;
    }

    /**
     * 绘制自旋转位图
     *
     * @param canvas
     * @param paint
     * @param bitmap   位图对象
     * @param rotation 旋转度数
     * @param posX     在canvas的位置坐标
     * @param posY
     */
    private void drawRotateBitmap(Canvas canvas, Paint paint, Bitmap bitmap,
                                  float rotation, float posX, float posY) {
        Matrix matrix = new Matrix();
        int offsetX = bitmap.getWidth() / 2;
        int offsetY = bitmap.getHeight() / 2;
        matrix.postTranslate(-offsetX, -offsetY);
        matrix.postRotate(rotation);
        matrix.postTranslate(posX + offsetX, posY + offsetY);
        canvas.drawBitmap(bitmap, matrix, paint);
    }

    Bitmap bitmap;
    int bitmapW, bitmapH;
    private float rotation;

    private void initBitmap() {
        // 图片的宽和高

        bitmap = ((BitmapDrawable) getContext().getResources().getDrawable(
                CMemoryData.isDriver() ? R.drawable.iv_refresh_progress_driver : R.drawable
                        .iv_refresh_progress_shipper))
                .getBitmap();
        bitmapW = bitmap.getWidth();
        bitmapH = bitmap.getHeight();


    }


    private void setStartDegrees(float startDegrees) {
        mStartDegrees = startDegrees;
        postInvalidate();
    }

    public void setSwipeDegrees(float swipeDegrees) {
        this.mSwipeDegrees = swipeDegrees;
        postInvalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        float radius = Math.min(w, h) / 2.0f;
        float centerX = w / 2.0f;
        float centerY = h / 2.0f;

        mArcBounds.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
        mArcBounds.inset(mStrokeWidth / 2.0f, mStrokeWidth / 2.0f);

    }

    @Override
    public void reset() {
        resetAnimator();

        mHasTriggeredRefresh = false;
        mStartDegrees = DEFAULT_START_DEGREES;
        mSwipeDegrees = DEFAULT_SWIPE_DEGREES;
        setSwipeDegrees(mSwipeDegrees);
    }

    public void startLoading() {
        isLoadEnd = false;
        isEmpty = false;
        ValueAnimator anim = ValueAnimator.ofFloat(0.0f, 1.0f);
        anim.setInterpolator(new LinearInterpolator());
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float rotateProgress = (float) animation.getAnimatedValue();
                setSwipeDegrees(rotateProgress * MAX_ARC_DEGREE);
            }
        });
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                setVisibility(VISIBLE);
                reset();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                refreshing();
            }
        });
        anim.setDuration(ANIMATION_DURATION);
        anim.start();
    }

    public void stopLoading() {

        resetAnimator();
        ValueAnimator anim = ValueAnimator.ofFloat(1.0f, 0.1f);
        anim.setInterpolator(new LinearInterpolator());
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float rotateProgress = (float) animation.getAnimatedValue();
                setSwipeDegrees(rotateProgress * MAX_ARC_DEGREE);
            }
        });
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                reset();
//                setVisibility(INVISIBLE);
            }
        });
        anim.setDuration(ANIMATION_DURATION);
        anim.start();
    }

    public void success() {
        refreshText = strEnd; // 刷新完成
        postInvalidate();
    }

    @Override
    public void refreshing() {
        mHasTriggeredRefresh = true;
        mSwipeDegrees = MAX_ARC_DEGREE;

        refreshText = strIng; // 正在刷新
        startAnimator();
        postInvalidate();
    }

    @Override
    public void pullToRefresh() {
        refreshText = strs;// 松开刷新
        postInvalidate();
    }

    @Override
    public void releaseToRefresh() {
        refreshText = strPull;// 下拉刷新
        postInvalidate();
    }

    @Override
    public void pullProgress(float pullDistance, float pullProgress) {
        this.pullDistance = pullDistance;
        this.pullProgress = pullProgress;
        if (pullDistance <= 0f) {
            mSwipeDegrees = DEFAULT_SWIPE_DEGREES;
        }
        if (!mHasTriggeredRefresh) {
            float swipeProgress = Math.min(1.0f, pullProgress);
            setSwipeDegrees(swipeProgress * MAX_ARC_DEGREE);
        }
    }

    public float getPullDistance() {
        return pullDistance;
    }

    public void setPullDistance(float pullDistance) {
        this.pullDistance = pullDistance;
    }

    public float getPullProgress() {
        return pullProgress;
    }

    public void setPullProgress(float pullProgress) {
        this.pullProgress = pullProgress;
    }
}
