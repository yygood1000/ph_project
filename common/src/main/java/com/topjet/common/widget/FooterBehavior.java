package com.topjet.common.widget;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import com.topjet.common.utils.Logger;

/**
 * Created by dandy on 2016/7/4.
 */
public class FooterBehavior extends CoordinatorLayout.Behavior<View> {

    private float targetY = -1;

    private static final String TAG = "FooterBehavior";

    public FooterBehavior(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /**
     * 当CoordinatorLayout的子View尝试发起嵌套滚动时调用
     *
     * @param coordinatorLayout            父布局CoordinatorLayout
     * @param child             使用此Behavior的AppBarLayout
     * @param directTargetChild CoordinatorLayout的子View，或者是包含嵌套滚动操作的目标View
     * @param target            发起嵌套滚动的目标View(即AppBarLayout下面的ScrollView或RecyclerView)
     * @param nestedScrollAxes  嵌套滚动的方向
     * @return 返回true表示接受滚动
     */
    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child,
                                       View directTargetChild, View target, int nestedScrollAxes) {
        Logger.i("oye", "onStartNestedScroll");
        if (targetY == -1) {
            targetY = target.getY();
        }
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target,
                                  int dx, int dy, int[] consumed) {
        Logger.i("oye", "onStartNestedScroll dy= " + dy);
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
        float scrooY = targetY - Math.abs(target.getY());
        float scaleY = scrooY / targetY;
        child.setTranslationY(child.getHeight() * scaleY);
    }
}