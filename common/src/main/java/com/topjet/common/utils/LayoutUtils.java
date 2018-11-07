package com.topjet.common.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TabLayout;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.reflect.Field;

public class LayoutUtils {
    public static final int MATCH = ViewGroup.LayoutParams.MATCH_PARENT;
    public static final int WRAP = ViewGroup.LayoutParams.WRAP_CONTENT;

    /**
     * Create a ViewGroup params,and set to the target view.
     */
    public static void setViewGroupParams(View view, int width, int height) {
        if (view != null) {
            ViewGroup.LayoutParams params = view.getLayoutParams();
            if (params == null) {
                params = new ViewGroup.LayoutParams(width, height);
            } else {
                params.width = width;
                params.height = height;
            }

            view.setLayoutParams(params);
        }
    }

    /**
     * Create a LinearLayout params,and set to the target view.
     */
    public static void setLinearParams(View view, int width, int height) {
        if (view != null) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();

            if (params == null) {
                params = new LinearLayout.LayoutParams(width, height);
            } else {
                params.width = width;
                params.height = height;
            }
            view.setLayoutParams(params);
        }
    }

    /**
     * Create a LinearLayout params,and set to the target view.
     */
    public static void setRelativeParams(View view, int width, int height) {
        if (view != null) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();

            if (params == null) {
                params = new RelativeLayout.LayoutParams(width, height);
            } else {
                params.width = width;
                params.height = height;
            }
            view.setLayoutParams(params);
        }
    }

    /**
     * Create a FrameLayout params,and set to the target view.
     */
    public static void setFrameParams(View view, int width, int height) {
        if (view != null) {
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();

            if (params == null) {
                params = new FrameLayout.LayoutParams(width, height);
            } else {
                params.width = width;
                params.height = height;
            }
            view.setLayoutParams(params);
        }
    }

    /**
     * Create a LinearLayout params with margins,and set to the target view.
     */
    public static void setLinearParams(View view, int width, int height, int top, int left, int right, int bottom) {
        if (view != null) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
            if (params == null) {
                params = new LinearLayout.LayoutParams(width, height);
            } else {
                params.width = width;
                params.height = height;
            }

            params.topMargin = top;
            params.leftMargin = left;
            params.rightMargin = right;
            params.bottomMargin = bottom;

            view.setLayoutParams(params);
        }
    }

    public static void setLayoutParams(View view, ViewGroup.LayoutParams params) {
        if (view != null) {
            view.setLayoutParams(params);
        }
    }

    public static void setLinearGravity(View view, int gravity) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
        if (layoutParams != null) {
            layoutParams.gravity = gravity;
            view.setLayoutParams(layoutParams);
        }
    }

    public static void setFrameGravity(View view, int gravity) {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view.getLayoutParams();
        if (layoutParams != null) {
            layoutParams.gravity = gravity;
            view.setLayoutParams(layoutParams);
        }
    }

    /**
     * 设置文本内容
     */
    public static void setText(TextView view, CharSequence text) {
        if (view != null) {
            view.setText(text);
        }
    }

    /**
     * 设置资源文件文本内容
     */
    public static void setText(TextView view, int resId) {
        setText(view, ResourceUtils.getString(resId));
    }

    /**
     * 设置文本颜色
     */
    public static void setTextColorByResource(TextView view, int resId) {
        int color = ResourceUtils.getColor(resId);
        setTextColor(view, color);
    }

    /**
     * 设置文本颜色
     */
    public static void setTextColor(TextView view, int color) {
        if (view != null) {
            view.setTextColor(color);
        }
    }

    /**
     * 不会出现空指针的设置文本方法
     */
    public static void setNonEmptyText(TextView view, CharSequence text) {
        if (view != null) {
            if (StringUtils.isEmpty(text)) {
                text = "";
            }
            view.setText(text);
        }
    }

    /**
     * @param view
     * @param resId
     */
    public static void setImageResource(ImageView view, int resId) {
        if (view != null) {
            view.setImageResource(resId);
        }
    }

    /**
     * @param view
     * @param resId
     */
    public static void setBackgroundResource(View view, int resId) {
        if (view != null) {
            int paddingLeft = view.getPaddingLeft();
            int paddingTop = view.getPaddingTop();
            int paddingRight = view.getPaddingRight();
            int paddingBottom = view.getPaddingBottom();

            view.setBackgroundResource(resId);
            view.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
        }
    }

    /**
     * @param view
     * @param resId
     */
    public static void setBackgroundColor(View view, int resId) {
        if (view != null) {
            int paddingLeft = view.getPaddingLeft();
            int paddingTop = view.getPaddingTop();
            int paddingRight = view.getPaddingRight();
            int paddingBottom = view.getPaddingBottom();

            view.setBackgroundColor(resId);
            view.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
        }
    }

    /**
     * @param view
     * @param enabled
     */
    public static void setEnabled(View view, boolean enabled) {
        if (view != null) {
            view.setEnabled(enabled);
        }
    }

    /**
     * 代码设置DrawableLeft
     */
    public static void setDrawableLeft(TextView view, int drawableResId) {
        if (view != null) {
            Drawable drawableLeft = ResourceUtils.getDrawable(drawableResId);
            drawableLeft.setBounds(0, 0, drawableLeft.getMinimumWidth(), drawableLeft.getMinimumHeight());
            view.setCompoundDrawables(drawableLeft, null, null, null);
        }
    }

    /**
     * 代码设置DrawableRight
     */
    public static void setDrawableRight(TextView view, int drawableResId) {
        Drawable drawable = ResourceUtils.getDrawable(drawableResId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());//对图片进行压缩
        view.setCompoundDrawables(null, null, drawable, null);
    }

    /**
     * 设置view 的权重
     */
    public static void setLinearWeight(View view, float weight) {
        if (view != null) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
            if (params == null) {
                return;
            }
            params.weight = weight;
            view.setLayoutParams(params);
        }
    }

    /**
     * 判断TextView 显示内容是否为空
     */
    public static boolean isTextViewEmpty(TextView view) {
        if (view != null) {
            return TextUtils.isEmpty(view.getText().toString().trim());
        }
        return true;
    }

    /**
     * 获取TextView显示的文本内容
     */
    public static String getTextViewText(TextView view) {
        if (view != null) {
            return view.getText().toString().trim();
        }
        return null;
    }

    /**
     * 在TextView的文字后面继续添加文字
     */
    public static void addToTextView(TextView view, String text) {
        if (view == null || text == null) {
            return;
        }
        String newText = getTextViewText(view) + text;
        view.setText(newText);
    }

    /**
     * @param absListView
     * @return
     */
    public static boolean isLastItemVisible(AbsListView absListView) {
        int totalCount = absListView.getCount();
        return totalCount != 0 && absListView.getLastVisiblePosition() >= (totalCount - 1);
    }

    /**
     * @param absListView
     * @return
     */
    public static boolean isLast3ItemVisible(AbsListView absListView) {
        int totalCount = absListView.getCount();
        if (totalCount != 0) {
            if (totalCount >= 3) {
                return absListView.getLastVisiblePosition() >= (totalCount - 3);
            } else {
                return absListView.getLastVisiblePosition() >= (totalCount - 1);
            }
        } else {
            return false;
        }
    }

    /**
     * 图片紧跟着文字显示
     *
     * @param context
     * @param tv
     * @param textResId
     * @param imageResId
     */
    public static void imageFollowText(Context context, TextView tv, int textResId, int imageResId) {
        String text = ResourceUtils.getString(textResId);
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        ImageSpan imageSpan = getImageSpanFollowText(context, imageResId);
        builder.setSpan(imageSpan, builder.length() - 1, builder.length(), 0);
        tv.setText(builder);
    }

    /**
     * @param context
     * @param imageResId
     * @return
     */
    public static ImageSpan getImageSpanFollowText(Context context, int imageResId) {
        Drawable d = ResourceUtils.getDrawable(imageResId);
        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
        return new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
    }

    /**
     * 半角转全角,返回全角字符串
     *
     * @param content
     * @return
     */
    public static String setTextContent(String content) {
        char[] c = content.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }


    /**
     * @param tv
     */
    public static void restrictOneLineText(final TextView tv) {
        tv.post(new Runnable() {
            @Override
            public void run() {
                int lineCount = tv.getLineCount();
                if (tv.getEllipsize() == TextUtils.TruncateAt.END) {
                    setLinearWeight(tv, 1f);
                    tv.setMaxLines(1);
                    return;
                }

                if (lineCount > 1) {
                    setLinearWeight(tv, 1f);
                    tv.setMaxLines(1);
                    tv.setEllipsize(TextUtils.TruncateAt.END);
                } else {
                    setLinearWeight(tv, 0f);
                }
            }
        });
    }

    /**
     * @param tv
     * @param text
     */
    public static void restrictOneLineText(final TextView tv, String text) {
        tv.setText(text);
        restrictOneLineText(tv);
    }


    /**
     * 设置列表项背景变化
     *
     * @param viewGroup：最上层布局
     */
    public static void setAllItem(Context context, ViewGroup viewGroup, int colorId) {
        if (viewGroup == null) {
            return;
        }

        int count = viewGroup.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = viewGroup.getChildAt(i);
            if (view instanceof ViewGroup) {
                //递归了。若是布局控件（LinearLayout或RelativeLayout）,继续查询子View
                setAllItem(context, (ViewGroup) view, colorId);
            } else if (view instanceof TextView) {
                //判断是否为文本
                TextView newDtv = (TextView) view;
                newDtv.setTextColor(context.getResources().getColor(colorId));
            }

        }
    }

    /**
     * 设置系统TabLayout 下划线的长度
     *
     * @param tabs     tabLayout
     * @param leftDip  限制左边的间距
     * @param rightDip 限制右边的间距
     */
    public static void setIndicator(TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem()
                .getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem()
                .getDisplayMetrics());

        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams
                    .MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }
    }
}
