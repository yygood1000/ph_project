package com.topjet.common.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.topjet.common.R;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.utils.ResourceUtils;

import butterknife.ButterKnife;

/**
 * Created by tsj-004 on 2017/7/28.
 */

public class MyTitleBar extends LinearLayout {
    private ImageView mBack = null;     //返回按钮
    private ImageView mCancel; // 叉叉关闭
    private TextView mTitle = null;     //标题
    private FrameLayout mFlRight = null;//右边部分的frameLayout
    private TextView mTvRight = null;   //右边的文字
    private TextView mTvRightOfTitle = null; // title紧靠右边的文本
    private ImageView mIvRight = null;  //右边的图片
    private Toolbar mToolBar = null;  //TitleBar 整体控件
    private View mViewStatusBarFill;// 状态栏填充布局
    private View mViewLine;// 标题栏底部分割线
    private Context mContext;

    private TranslateAnimation animation;
    private Mode mMode;
    private View parent;

    public MyTitleBar(Context context) {
        super(context);
        init(context);
    }

    public MyTitleBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        parent = View.inflate(context, R.layout.layout_mytitlebar, this);
        mViewStatusBarFill = ButterKnife.findById(parent, R.id.view_statusbar_fill);
        mToolBar = ButterKnife.findById(parent, R.id.toolbar);
        mBack = ButterKnife.findById(parent, R.id.iv_title_bar_back);
        mCancel = ButterKnife.findById(parent, R.id.iv_title_bar_cancel);
        mTitle = ButterKnife.findById(parent, R.id.tv_title_bar);
        mFlRight = ButterKnife.findById(parent, R.id.fl_title_bar_right);
        mTvRight = ButterKnife.findById(parent, R.id.tv_title_bar_right);
        mIvRight = ButterKnife.findById(parent, R.id.iv_title_bar_right);
        mViewLine = ButterKnife.findById(parent, R.id.view_line);
        mTvRightOfTitle = ButterKnife.findById(parent, R.id.tv_title_bar_of_right);

        if (mMode == null) {
            setMode(Mode.getDefault());
        }

        if (!CMemoryData.isDriver()) {
            mViewStatusBarFill.setBackgroundResource(R.drawable.shape_bg_gradient_shipper);
        }
    }


    public MyTitleBar showTitleBar() {
        parent.setVisibility(View.VISIBLE);
        return this;
    }

    /**
     * 显示紧靠title 右边的文本
     */
    public MyTitleBar showTitleRight() {
        mTvRightOfTitle.setVisibility(VISIBLE);
        return this;
    }

    /**
     * 设置紧靠title 右边的文本
     */
    public MyTitleBar setTitleRightText(String text) {
        mTvRightOfTitle.setText(text);
        return this;
    }

    public MyTitleBar hideTitleBar() {
        parent.setVisibility(View.GONE);
        return this;
    }

    public MyTitleBar hideBack() {
        mBack.setVisibility(View.GONE);
        return this;
    }

    public ImageView getmBack() {
        return mBack;
    }

    public ImageView getCancel() {
        return mCancel;
    }

    /**
     * 获取标题控件
     */
    public TextView getmTitle() {
        return mTitle;
    }

    /**
     * 设置标题文字
     */
    public MyTitleBar setTitleText(String ti) {
        mTitle.setText(ti);
        return this;
    }

    /**
     * 设置标题文字
     */
    public MyTitleBar setTitleText(int textId) {
        mTitle.setText(ResourceUtils.getString(textId));
        return this;
    }

    /**
     * 设置标题颜色
     */
    public MyTitleBar setTitleColor(int color) {
        mTitle.setTextColor(ResourceUtils.getColor(color));
        return this;
    }

    /**
     * 设置返回键图标
     */
    public MyTitleBar setBackImg(int id) {
        mBack.setImageResource(id);
        return this;
    }

    public MyTitleBar setCancleImg(int id) {
        mCancel.setImageResource(id);
        return this;
    }

    /**
     * 设置右边图片图标
     */
    public MyTitleBar setRightImg(int id) {
        mIvRight.setImageResource(id);
        return this;
    }

    /**
     * 设置右边文字内容
     */
    public MyTitleBar setRightText(String st) {
        mTvRight.setText(st);
        return this;
    }

    /**
     * 设置右边文字内容
     */
    public MyTitleBar setRightText(int textId) {
        mTvRight.setText(ResourceUtils.getString(textId));
        return this;
    }

    /**
     * 设置右边文字颜色
     */
    public MyTitleBar setRightTextColor(int color) {
        mTvRight.setTextColor(ResourceUtils.getColor(color));
        return this;
    }

    public FrameLayout getmFlRight() {
        return mFlRight;
    }

    public TextView getmTvRight() {
        return mTvRight;
    }

    public ImageView getmIvRight() {
        return mIvRight;
    }

    public MyTitleBar hideLine() {
        mViewLine.setVisibility(View.GONE);
        return this;
    }

    public Toolbar getmToolBar() {
        return mToolBar;
    }

    /**
     * 隐藏标题栏下半部分，显示状态栏
     */
    public void hideToolBar() {
        if (mToolBar != null) {
            mToolBar.setVisibility(GONE);
        }
    }

    public static final int BACK = 0;
    public static final int TITLE = 1;
    public static final int RTEXT = 2;
    public static final int RIMG = 3;
    public static final int CANCEL = 4;

    public static int[] primes = {2, 3, 5, 7, 9};// 让乘积不相等

    public enum Mode {
        TITLE_ONLY(primes[TITLE]),

        BACK_ONLY(primes[BACK]),

        BACK_TITLE(primes[BACK] * primes[TITLE]),

        BACK_TITLE_RTEXT(primes[BACK] * primes[TITLE] * primes[RTEXT]),

        BACK_TITLE_RIMG(primes[BACK] * primes[TITLE] * primes[RIMG]),

        TITLE_RTEXT(primes[RTEXT] * primes[TITLE]),

        TITLE_RIMG(primes[RIMG] * primes[TITLE]),

        RTEXT_ONLY(primes[RTEXT]),

        RIMG_ONLY(primes[RIMG]),

        CANCEL_ONLY(primes[CANCEL]),

        CANCEL_TITLE(primes[CANCEL] * primes[TITLE]),

        CANCEL_TITLE_RTEXT(primes[CANCEL] * primes[TITLE] * primes[RTEXT]),

        CANCEL_TITLE_RIMG(primes[CANCEL] * primes[TITLE] * primes[RIMG]);


        private int mIntValue;

        Mode(int i) {
            mIntValue = i;
        }

        public int getIntValue() {
            return mIntValue;
        }

        public static Mode getDefault() {
            return BACK_TITLE;
        }
    }

    private void setVisibility(View view, int primeNo) {
        view.setVisibility(mMode.getIntValue() % primeNo == 0 ? View.VISIBLE : View.GONE);
    }

    public MyTitleBar setMode(Mode mode) {
        mMode = mode;
        setVisibility(mBack, primes[BACK]);
        setVisibility(mTitle, primes[TITLE]);
        setVisibility(mTvRight, primes[RTEXT]);
        setVisibility(mIvRight, primes[RIMG]);
        setVisibility(mCancel, primes[CANCEL]);
        return this;
    }

    public Mode getmMode() {
        return mMode;
    }
}
