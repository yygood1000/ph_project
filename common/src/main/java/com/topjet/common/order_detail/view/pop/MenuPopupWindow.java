package com.topjet.common.order_detail.view.pop;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.topjet.common.R;
import com.topjet.common.utils.ScreenUtils;

import butterknife.ButterKnife;

/**
 * creator: zhulunjun
 * time:    2017/9/7
 * describe: 订单详情的菜单操作
 */

public class MenuPopupWindow extends PopupWindow implements View.OnClickListener {

    private TextView tvMenuOne;
    private TextView tvMenuTwo;
    private LinearLayout llMenuTwo;
    private TextView tvMenuThree;
    private Context mContext;
    private Activity mActivity;
    private View mView;

    public MenuPopupWindow(Context context) {
        super(context);
        this.mContext = context;
        mActivity = (Activity) context;
        initPopupWindow();
    }

    public void initPopupWindow() {
        mView = View.inflate(mContext, R.layout.pop_order_menu, null);
        this.setContentView(mView);

        tvMenuOne = ButterKnife.findById(mView, R.id.tv_menu_one);
        tvMenuTwo = ButterKnife.findById(mView, R.id.tv_menu_two);
        tvMenuThree = ButterKnife.findById(mView, R.id.tv_menu_three);
        llMenuTwo = ButterKnife.findById(mView, R.id.ll_menu_two);

        tvMenuOne.setOnClickListener(this);
        tvMenuTwo.setOnClickListener(this);
        tvMenuThree.setOnClickListener(this);

        // 设置窗口属性
        this.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        this.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        this.setBackgroundDrawable(new BitmapDrawable());
        this.setFocusable(true);
        this.setTouchable(true);
        this.setOutsideTouchable(true);
        this.setClippingEnabled(false);


        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                //设置背景正常
                setActivityAlpha(1.0f);
            }
        });
    }

    private void setActivityAlpha(float alpha) {
        WindowManager.LayoutParams params = mActivity.getWindow().getAttributes();
        params.alpha = alpha;
        mActivity.getWindow().setAttributes(params);
    }

    /**
     * 显示
     */
    public void showPopupWindow(View view, String[] menuText) {
        // 传入的菜单信息不符合要求，则不显示
        if (setMenuText(menuText)) {
            return;
        }

        if (isShowing()) {
            dismiss();
        } else {
            // 设置背景变暗
            setActivityAlpha(0.7f);

            mView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            showAsDropDown(view, -(mView.getMeasuredWidth() - view.getMeasuredWidth()), ScreenUtils.dp2px(mContext, 10));
        }
    }

    /**
     * 设置菜单个数，显示文字
     * 个数只有2个，3个，两种情况，其他情况不显示
     * 2个菜单按钮时，隐藏第二个
     *
     * @param menuText 文本信息
     */
    private boolean setMenuText(String[] menuText) {
        if (menuText == null || menuText.length < 2 || menuText.length > 3) {
            return true;
        }
        tvMenuOne.setText(menuText[0]);
        if (menuText.length == 2) {
            llMenuTwo.setVisibility(View.GONE);
            tvMenuThree.setText(menuText[1]);
        } else {
            tvMenuTwo.setText(menuText[1]);
            tvMenuThree.setText(menuText[2]);
        }
        return false;
    }

    private OnPopMenuClickListener onPopMenuClickListener;

    public void setOnPopMenuClickListener(OnPopMenuClickListener onPopMenuClickListener) {
        this.onPopMenuClickListener = onPopMenuClickListener;
    }

    @Override
    public void onClick(View v) {
        if (onPopMenuClickListener != null) {
            onPopMenuClickListener.onMenuClick(v);
        }
        // 点击之后，默认关闭
        dismiss();
    }

    /**
     * 菜单点击
     */
    public interface OnPopMenuClickListener {
        void onMenuClick(View view);
    }
}
