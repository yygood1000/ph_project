package com.topjet.common.base.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.topjet.common.R;
import com.topjet.common.base.listener.DebounceClickListener;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.utils.LayoutUtils;
import com.topjet.common.utils.ResourceUtils;
import com.topjet.common.utils.ScreenUtils;

import butterknife.ButterKnife;

/**
 * Created by yy on 2017/8/9.
 * <p>
 * 公用Dialog，可以自定义布局
 */
public class AutoDialog extends Dialog {
    private LinearLayout llRoot, llBoth;
    private TextView tvTitle, tvContent;
    private TextView btnSingle, btnLeft, btnRight;

    private OnSingleConfirmListener mOnSingleConfirmListener;
    private OnBothConfirmListener mOnBothConfirmListener;

    private boolean isCustomDialog;// 是否是自定义布局
    private View view;
    private Window window = null;

    private Activity activity = null;

    /**
     */
    public AutoDialog(Context context) {
        super(context, R.style.AutoDialogTheme);
        initSelf(context, 0);
    }

    /**
     * @param resId 自定义布局的layout resId
     */
    public AutoDialog(Context context, int resId) {
        this(context);
        initSelf(context, resId);
    }


    private void initSelf(Context context, int resId) {
        if (context instanceof Activity) {
            this.activity = (Activity) context;
        }

        isCustomDialog = resId != 0;
        view = View.inflate(context, R.layout.auto_dialog, null);
        setContentView(view);

        llRoot = ButterKnife.findById(view, R.id.ll_root);
        tvTitle = ButterKnife.findById(view, R.id.tv_title);
        tvContent = ButterKnife.findById(view, R.id.tv_content);
        btnSingle = ButterKnife.findById(view, R.id.btn_single_confirm);
        llBoth = ButterKnife.findById(view, R.id.ll_both_confirm);
        btnLeft = ButterKnife.findById(view, R.id.btn_left_confirm);
        btnRight = ButterKnife.findById(view, R.id.btn_right_confirm);

        btnSingle.setOnClickListener(mOnClickListener);
        btnLeft.setOnClickListener(mOnClickListener);
        btnRight.setOnClickListener(mOnClickListener);

        initDialogWidth(context);// 设置Dialog宽度
        initUI(context, resId);// 设置UI

        window = getWindow();
        windowDeploy();//动画
    }

    /**
     * 初始化Dialog宽度
     */
    private void initDialogWidth(final Context context) {
        llRoot.post(new Runnable() {
            @Override
            public void run() {
                ViewGroup.LayoutParams rootParams = llRoot.getLayoutParams();
                rootParams.width = ScreenUtils.getScreenWidth(context) * 8 / 10;
                llRoot.setLayoutParams(rootParams);
            }
        });
    }

    /**
     * 初始化UI
     */
    private void initUI(Context context, int resId) {
        initNormal(context, resId);// 判断是否是自定义布局，如果是则显示llCustomize，并把自定义的布局设置进去
        initBtnStyle();// 设置按钮样式
    }

    /**
     * 判断是否是自定义布局，如果是则显示llCustomize，并把自定义的布局设置进去
     */
    private void initNormal(Context context, int resId) {
        if (isCustomDialog) {
            llRoot.removeAllViews();
            View customizeView = View.inflate(context, resId, null);
            llRoot.addView(customizeView);
        } else {
            llRoot.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 设置按钮样式
     */
    private void initBtnStyle() {
        if (CMemoryData.isDriver()) {
            btnSingle.setBackgroundResource(R.drawable.selector_dialog_singlebtn_bg_driver);
            btnLeft.setBackgroundResource(R.drawable.selector_dialog_leftbtn_bg_driver);
            btnRight.setBackgroundResource(R.drawable.selector_dialog_rightbtn_bg_driver);
        } else {
            btnSingle.setBackgroundResource(R.drawable.selector_dialog_singlebtn_bg_shipper);
            btnLeft.setBackgroundResource(R.drawable.selector_dialog_leftbtn_bg_shipper);
            btnRight.setBackgroundResource(R.drawable.selector_dialog_rightbtn_bg_shipper);
        }
    }


    private DebounceClickListener mOnClickListener = new DebounceClickListener() {
        @Override
        public void performClick(View v) {
            int viewId = v.getId();
            if (viewId == R.id.btn_single_confirm) {
                mOnSingleConfirmListener.onClick();
            } else if (viewId == R.id.btn_left_confirm) {
                mOnBothConfirmListener.onLeftClick();
            } else if (viewId == R.id.btn_right_confirm) {
                mOnBothConfirmListener.onRightClick();
            } else if (viewId == R.id.iv_close) {
                close();
            }
        }
    };

    /**
     * 设置单个按钮时的点击事件
     */
    public void setOnSingleConfirmListener(OnSingleConfirmListener onSingleConfirmListener) {
        mOnSingleConfirmListener = onSingleConfirmListener;
        changeConfirmVisibility(true);
    }

    /**
     * 设置两个按钮时的点击事件
     */
    public void setOnBothConfirmListener(OnBothConfirmListener onBothConfirmListener) {
        mOnBothConfirmListener = onBothConfirmListener;
        changeConfirmVisibility(false);
    }

    /**
     * 设置单按钮显示或者双按钮显示
     */
    private void changeConfirmVisibility(boolean isSingleConfirm) {
        if (isSingleConfirm) {
            btnSingle.setVisibility(View.VISIBLE);
            llBoth.setVisibility(View.GONE);
        } else {
            btnSingle.setVisibility(View.GONE);
            llBoth.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 展示/隐藏Dialog
     */
    public void toggleShow() {
        if (activity != null) {
            if (!activity.isFinishing()) {
                if (isShowing()) {
                    dismiss();
                } else {
                    show();
                }
            }
            return;
        }
        if (isShowing()) {
            dismiss();
        } else {
            show();
        }
    }

    /**
     * 关闭Dialog
     */
    public void close() {
        if (isShowing()) {
            dismiss();
        }
    }

    /**
     * 进出动画
     */
    public void windowDeploy() {
        window.setWindowAnimations(R.style.center_dialog_anim);
    }

    /**
     * 隐藏Title
     */
    public void hideTitle(TextView textView, CharSequence text) {
        if (text == null || text.equals("")) {
            textView.setVisibility(View.GONE);
        } else {
            textView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 自定义布局时会用到，通过view获取子控件
     */
    public View getView() {
        return view;
    }

    @Override
    public void setTitle(int resId) {
        LayoutUtils.setText(tvTitle, resId);
        hideTitle(tvTitle, ResourceUtils.getString(resId));
    }

    @Override
    public void setTitle(CharSequence text) {
        LayoutUtils.setText(tvTitle, text);
        hideTitle(tvTitle, text);
    }

    /**
     * 标题加粗
     */
    public void setTitleBold() {
        tvTitle.getPaint().setFakeBoldText(true);
    }

    /**
     * 设置标题下方padding
     */
    public void setTitleBottomPadding(int padding) {
        tvTitle.setPadding(tvTitle.getPaddingLeft(), tvTitle.getPaddingTop(), tvTitle.getPaddingRight(), padding);
    }

    public void setContent(int resId) {
        tvContent.setVisibility(View.VISIBLE);
        LayoutUtils.setText(tvContent, resId);
    }

    public void setContent(CharSequence text) {
        tvContent.setVisibility(View.VISIBLE);
        LayoutUtils.setText(tvContent, text);
    }

    public void setContentColor(int color) {
        tvContent.setVisibility(View.VISIBLE);
        LayoutUtils.setTextColor(tvContent, color);
    }

    public void setSingleText(CharSequence text) {
        LayoutUtils.setText(btnSingle, text);
    }

    public void setSingleTextColor(int color) {
        LayoutUtils.setTextColor(btnSingle, color);
    }

    public void setSingleText(int resId) {
        LayoutUtils.setText(btnSingle, resId);
    }

    public void setRightText(CharSequence text) {
        LayoutUtils.setText(btnRight, text);
    }

    public void setRightTextColor(int color) {
        LayoutUtils.setTextColor(btnRight, color);
    }

    public void setRightText(int resId) {
        LayoutUtils.setText(btnRight, resId);
    }

    public void setLeftText(CharSequence text) {
        LayoutUtils.setText(btnLeft, text);
    }

    public void setLeftText(int resId) {
        LayoutUtils.setText(btnLeft, resId);
    }

    public void setLeftTextColor(int color) {
        LayoutUtils.setTextColor(btnLeft, color);
    }

    public void setLeftBtnBackgroundColor(int color) {
        LayoutUtils.setBackgroundColor(btnLeft, color);
    }

    public void setRightBtnBackgroundColor(int color) {
        LayoutUtils.setBackgroundColor(btnRight, color);
    }

    public void setLeftBackgroundResource(int leftResId) {
        btnLeft.setBackgroundResource(leftResId);
    }

    public void setRightBackgroundResource(int rightResId) {
        btnRight.setBackgroundResource(rightResId);
    }

    public void setConfirmBackgroundResource(int singleResId) {
        btnSingle.setBackgroundResource(singleResId);
    }

    public interface OnSingleConfirmListener {
        void onClick();
    }

    public interface OnBothConfirmListener {
        void onLeftClick();

        void onRightClick();

    }

}
