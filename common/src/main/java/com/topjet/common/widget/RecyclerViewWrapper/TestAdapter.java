package com.topjet.common.widget.RecyclerViewWrapper;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.topjet.common.R;

/**
 * creator: zhulunjun
 * time:    2017/9/26
 * describe:
 */

public class TestAdapter extends BaseQuickAdapter<TestBean, BaseViewHolder> {
    public TestAdapter() {
        super(R.layout.listitem_test);
    }

    @Override
    protected void convert(BaseViewHolder helper, TestBean item) {
        helper.setText(R.id.tv_str, item.name);
    }
}
