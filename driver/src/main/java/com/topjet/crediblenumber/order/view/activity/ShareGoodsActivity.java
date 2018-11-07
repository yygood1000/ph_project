package com.topjet.crediblenumber.order.view.activity;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.topjet.common.order_detail.view.activity.ShareGoodsBaseActivity;
import com.topjet.crediblenumber.R;
import com.topjet.crediblenumber.order.view.adapter.MyOrderListAdapter;
import com.topjet.crediblenumber.order.view.adapter.ShareGoodsListAdapter;

import java.util.List;

/**
 * creator: zhulunjun
 * time:    2017/10/24
 * describe: 分享多个货源的列表
 * 从个人中心进入
 */

public class ShareGoodsActivity extends ShareGoodsBaseActivity {
    private ShareGoodsListAdapter mAdapter;


    @Override
    public BaseQuickAdapter getAdapter() {
        mAdapter = new ShareGoodsListAdapter();
        return mAdapter;
    }

    @Override
    protected void initView() {
        super.initView();
        tvBtnShare.setBackgroundResource(R.drawable.selector_btn_square_blue);
        mAdapter.setOnCheckListener(new ShareGoodsListAdapter.OnCheckListener() {
            @Override
            public void checkNum(int num) {
                tvBtnShare.setEnabled(num > 0);
            }
        });
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        // 刷新清空選中
        mAdapter.getCheckDate().clear();
        tvBtnShare.setFocusable(false);
    }

    @Override
    public List<String> getCheckIds() {
        return mAdapter.getCheckIds();
    }


}
