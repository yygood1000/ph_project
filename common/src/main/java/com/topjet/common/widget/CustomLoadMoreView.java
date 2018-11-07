package com.topjet.common.widget;


import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.topjet.common.R;

/**
 * Created by yy  on 2016/10/15.
 * 自定义加载更多的底部
 */

public final class CustomLoadMoreView extends LoadMoreView {

    @Override
    public int getLayoutId() {
        return R.layout.layout_view_load_more;
    }

    @Override
    protected int getLoadingViewId() {
        return R.id.load_more_loading_view;
    }

    @Override
    protected int getLoadFailViewId() {
        return R.id.load_more_load_fail_view;
    }

    @Override
    protected int getLoadEndViewId() {
        return R.id.load_more_load_end_view;
    }
}
