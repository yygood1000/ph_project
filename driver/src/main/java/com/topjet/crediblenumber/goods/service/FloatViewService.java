package com.topjet.crediblenumber.goods.service;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;

import com.topjet.common.App;
import com.topjet.common.base.busManger.BusManager;
import com.topjet.common.base.dialog.AutoDialog;
import com.topjet.common.common.modle.extra.TabIndex;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.ScreenUtils;
import com.topjet.crediblenumber.MainActivity;
import com.topjet.crediblenumber.R;
import com.topjet.crediblenumber.goods.modle.event.ChangeListenOrderStatusEvent;
import com.topjet.crediblenumber.goods.view.widget.FloatView;

/**
 * Created by hedong on 2016/4/26. 2.0 悬浮窗 service
 */
public class FloatViewService extends Service implements FloatView.FloatViewListener {
    private static WindowManager windowManager = null;
    private static WindowManager.LayoutParams layoutParams = null;
    private FloatView mFloatView = null;
    //	private GuideLayout guideLayout = null;
    private Context mContext;
//    private V3_Listen_OrderLogic v3_Listen_OrderLogic;

    private View contentView = null;
    private boolean isAddContentView = false;
    private RelativeLayout r2 = null;
    private WindowManager.LayoutParams wmParams;
    private View tempView = null;                //我在写什么？鬼知道！

    private int curX = 0;
    private int curY = 0;
    private int moveMaxX = 0;
    private int moveMaxY = 0;
    private static final int ANIMATIONTIME = 200;       // 动画展开和收缩的时间

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 1:
                    showAnimator();
                    break;

                case 2:
                    hideAnimator();
                    break;

                default:
                    break;
            }
        }

    };

    /**
     * 被系统杀掉后不再重新启动
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();

        windowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        createFloatView();
        createContentView();
    }

    // 创建 悬浮窗
    public void createFloatView() {
        mFloatView = new FloatView(this);
        mFloatView.setFloatViewListener(this);
        getlayp();

        windowManager.addView(mFloatView, layoutParams);
    }

    //创建开启、关闭、设置控件
    private void createContentView() {
        contentView = LayoutInflater.from(mContext).inflate(R.layout.ppw_listen_order_stretch, null);
        r2 = (RelativeLayout) contentView.findViewById(R.id.r2);
        contentView.findViewById(R.id.r0).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    tempView = null;
                    mHandler.sendEmptyMessage(2);
                }
                return false;
            }
        });
        contentView.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tempView = view;
                click(view);
            }
        });

        contentView.findViewById(R.id.iv_open).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tempView = view;
                mHandler.sendEmptyMessage(2);
            }
        });

        contentView.findViewById(R.id.iv_set).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tempView = view;
                mHandler.sendEmptyMessage(2);
            }
        });
    }

    /**
     * 暂时保留，等待和产品确认
     */
    private void showListenOrderOffDialog() {
        final AutoDialog dialog = new AutoDialog(this);
        dialog.setTitle("");
        dialog.setContent("为保证您能及时收到新货源，建议您打开听单");
        dialog.setLeftText(R.string.clolse_listen_order);
        dialog.setRightText(R.string.cancel);
        dialog.setOnBothConfirmListener(new AutoDialog.OnBothConfirmListener() {
            @Override
            public void onRightClick() {
                dialog.toggleShow();
            }

            @Override
            public void onLeftClick() {

                dialog.toggleShow();
                // 关闭听单
                tempView = null;
                mHandler.sendEmptyMessage(2);
                CMemoryData.setIsOrderOpen(false);
                BusManager.getBus().post(new ChangeListenOrderStatusEvent(ChangeListenOrderStatusEvent.CLOSE_LISTEN_ORDER));
                BusManager.getBus().post(new ChangeListenOrderStatusEvent(ChangeListenOrderStatusEvent.CHANGE_CHECKBOX_STATUS));  // 修改按钮状态
                BusManager.getBus().post(new ChangeListenOrderStatusEvent(ChangeListenOrderStatusEvent.CHANGE_FLOATVIEW_STATUS));  // 修改FloatView图标状态
            }
        });
        if (!CMemoryData.isDriver()) {
            dialog.setRightTextColor(App.getInstance().getResources().getColor(com.topjet.common.R.color.shippercolors));
        }
        dialog.getWindow().setType((WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));
        dialog.toggleShow();
    }

    /**
     * @category 实例化WindowManager 初次模拟位置时候使用
     */
    private void getWindowManagerLayoutParams() {
        wmParams = new WindowManager.LayoutParams();
        wmParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        wmParams.format = PixelFormat.TRANSPARENT;
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        wmParams.gravity = Gravity.CENTER;
        wmParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        wmParams.height = WindowManager.LayoutParams.MATCH_PARENT;
    }

    private void getlayp() {
        layoutParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);

//		// window type
//		layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;

        layoutParams.format = PixelFormat.RGBA_8888; // 设置图片格式，效果为背景透明

        // 设置Window flag
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

        // layoutParams.gravity = Gravity.BOTTOM | Gravity.RIGHT;
        layoutParams.gravity = Gravity.NO_GRAVITY;

        layoutParams.x = ScreenUtils.px2dp(mContext, ScreenUtils.getScreenWidth(mContext));
        layoutParams.y = ScreenUtils.px2dp(mContext, ScreenUtils.getScreenHeight(mContext)) / 2;

        // 设置悬浮窗的长宽
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void move(View view, int x, int y) {
        layoutParams.gravity = Gravity.NO_GRAVITY;
        Logger.i("TTT", "layoutParams.x:" + layoutParams.x + "layoutParams.y:" + layoutParams.y);
        layoutParams.x = layoutParams.x + x;
        layoutParams.y = layoutParams.y + y;
        windowManager.updateViewLayout(mFloatView, layoutParams);
    }

    public void click(View v) {
        if (v == null) {
            mFloatView.setVisibility(View.VISIBLE);
            mFloatView.isClickedView = false;
            if (isAddContentView) {
                windowManager.removeView(contentView);
                isAddContentView = false;
            }
        } else {
            if (v.getId() == R.id.iv_laba) {
                int i[] = new int[2];
                mFloatView.getLocationOnScreen(i);
                curX = (int) i[0];
                curY = (int) i[1];

                getWindowManagerLayoutParams();
                if (!isAddContentView) {
                    windowManager.addView(contentView, wmParams);
                    isAddContentView = true;
                    mHandler.sendEmptyMessage(1);
                }
                mFloatView.setVisibility(View.GONE);
            } else if (v.getId() == R.id.iv_close) {   // 关闭听单
                showListenOrderOffDialog();
            } else if (v.getId() == R.id.iv_set) {  // 设置路线
                mFloatView.setVisibility(View.VISIBLE);
                mFloatView.isClickedView = false;
                if (isAddContentView) {
                    windowManager.removeView(contentView);
                    isAddContentView = false;
                }

                Intent intent1 = new Intent(mContext, MainActivity.class);
//                intent1.putExtra(MainActivity.INTYPE, MainActivity.INTYPE_TINGDAN);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent1.putExtra(TabIndex.getExtraName(), new TabIndex(TabIndex.LISTEN_ORDER_PAGE));
                mContext.startActivity(intent1);
            } else if (v.getId() == R.id.iv_open) {     // 开启听单
                mFloatView.setVisibility(View.VISIBLE);
                mFloatView.isClickedView = false;
                if (isAddContentView) {
                    windowManager.removeView(contentView);
                    isAddContentView = false;
                }
                CMemoryData.setIsOrderOpen(true);
//                BusManager.getBus().post(new ChangeListenOrderStatusEvent(ChangeListenOrderStatusEvent.OPEN_LISTEN_ORDER));    // 开启听单（声音和网络）
                mContext.startService(new Intent(mContext, ListenOrderService.class));  // 开启听单服务（包含网络请求和播报）
                BusManager.getBus().post(new ChangeListenOrderStatusEvent(ChangeListenOrderStatusEvent.CHANGE_CHECKBOX_STATUS));  // 修改按钮状态
                BusManager.getBus().post(new ChangeListenOrderStatusEvent(ChangeListenOrderStatusEvent.CHANGE_FLOATVIEW_STATUS));  // 修改floatview图标状态

//                EventBus.getDefault().post(new V3_refreshListenorderOnOffEvent(true));
//                EventBus.getDefault().post(new V3_refreshListenorderOnOffServiceEvent(true));
//                if (v3_Listen_OrderLogic != null) {
//                    v3_Listen_OrderLogic.sendUpdateListenOrderStatus220("1");
//                }
            }
        }
    }

    public void showAnimator() {
        r2.setVisibility(View.GONE);
        contentView.setVisibility(View.VISIBLE);
        final RelativeLayout i = (RelativeLayout) contentView.findViewById(R.id.r1);

        moveMaxX = ScreenUtils.getScreenWidth(mContext) / 2;
        moveMaxY = ScreenUtils.getScreenHeight(mContext) / 2;

        Point point1 = new Point(moveMaxX, moveMaxY);
        Point point2 = new Point(curX, curY);
        final ValueAnimator moveAnimator = ValueAnimator.ofObject(new PointEvaluator(), point1, point2);
        moveAnimator.setDuration(ANIMATIONTIME);
        moveAnimator.setInterpolator(new DecelerateInterpolator());

        moveAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Point currentPoint = (Point) animation.getAnimatedValue();

                i.setTranslationX(curX - moveMaxX - currentPoint.getX());
                i.setTranslationY(curY - moveMaxY - currentPoint.getY());

            }
        });

        ValueAnimator va = ValueAnimator.ofFloat(0.3f, 1.0f);
        va.setDuration(ANIMATIONTIME);
        va.setInterpolator(new DecelerateInterpolator());
        va.setRepeatCount(0);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                i.setAlpha(value);
                i.setScaleX(value);
                i.setScaleY(value);
            }
        });

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(va).with(moveAnimator);
        animatorSet.setDuration(ANIMATIONTIME);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                r2.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.start();
    }

    public void hideAnimator() {
        int ii[] = new int[2];
        mFloatView.getLocationOnScreen(ii);
        curX = (int) ii[0];
        curY = (int) ii[1];

        r2.setVisibility(View.GONE);
        contentView.setVisibility(View.VISIBLE);
        final RelativeLayout i = (RelativeLayout) contentView.findViewById(R.id.r1);

        moveMaxX = ScreenUtils.getScreenWidth(mContext) / 2;
        moveMaxY = ScreenUtils.getScreenHeight(mContext) / 2;

        Point point1 = new Point(moveMaxX, moveMaxY);
        Point point2 = new Point(curX, curY);
        final ValueAnimator moveAnimator = ValueAnimator.ofObject(new PointEvaluator(), point1, point2);
        moveAnimator.setDuration(ANIMATIONTIME);
        moveAnimator.setInterpolator(new DecelerateInterpolator());

        moveAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Point currentPoint = (Point) animation.getAnimatedValue();

                i.setTranslationX(currentPoint.getX());
                i.setTranslationY(currentPoint.getY());
            }
        });

        ValueAnimator va = ValueAnimator.ofFloat(1.0f, 0.3f);
        va.setDuration(ANIMATIONTIME);
        va.setInterpolator(new DecelerateInterpolator());
        va.setRepeatCount(0);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                i.setAlpha(value);
                i.setScaleX(value);
                i.setScaleY(value);
            }
        });

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(va).with(moveAnimator);
        animatorSet.setDuration(ANIMATIONTIME);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (tempView != null) {
                    if (tempView.getId() != R.id.iv_close) {
                        click(tempView);
                    }
                } else {
                    click(tempView);
                }

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mHandler.removeCallbacksAndMessages(null);

        if (mFloatView != null) {
            mFloatView.destroyHandler();
            windowManager.removeView(mFloatView);
        }
        if (contentView != null) {
            if (isAddContentView) {
                windowManager.removeView(contentView);
                isAddContentView = false;
            }
        }
    }

    private class PointEvaluator implements TypeEvaluator {
        @Override
        public Object evaluate(float fraction, Object startValue, Object endValue) {
            Point startPoint = (Point) startValue;
            Point endPoint = (Point) endValue;
            float x = fraction * (endPoint.getX() - startPoint.getX());
            float y = fraction * (endPoint.getY() - startPoint.getY());
            return new Point((int) x, (int) y);
        }
    }

    private class Point {
        private int x;
        private int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }
}
