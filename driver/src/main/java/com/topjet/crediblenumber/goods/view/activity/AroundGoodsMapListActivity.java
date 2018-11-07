package com.topjet.crediblenumber.goods.view.activity;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.topjet.common.common.modle.bean.GoodsListData;
import com.topjet.common.utils.Logger;
import com.topjet.common.widget.RecyclerViewWrapper.BaseRecyclerViewActivity;
import com.topjet.crediblenumber.R;
import com.topjet.crediblenumber.goods.modle.serverAPI.GoodsCommand;
import com.topjet.crediblenumber.goods.modle.serverAPI.GoodsCommandAPI;
import com.topjet.crediblenumber.goods.presenter.AroundGoodsMapListPresenter;
import com.topjet.crediblenumber.goods.view.adapter.GoodsListAdapter;
import com.topjet.crediblenumber.goods.view.dialog.BidOrAlterDialog;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 附近货源 地图列表 页面
 */
public class AroundGoodsMapListActivity extends BaseRecyclerViewActivity<AroundGoodsMapListPresenter, GoodsListData>
        implements AroundGoodsMapListView<GoodsListData> {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    private boolean isLoadEnd;

    @Override
    protected void initPresenter() {
        mPresenter = new AroundGoodsMapListPresenter(this, mContext, new GoodsCommand(GoodsCommandAPI.class, this));
    }

    @Override
    protected int getLayoutResId() {
        noHasTitle();
        return R.layout.activity_around_goods_map_list;
    }

    @Override
    public void initView() {
        super.initView();
    }

    @Override
    protected void initData() {
        if (!mPresenter.isUseLocalListData) {
            refresh();
        } else {
            loadSuccess(mPresenter.dataList);
        }
    }

    @Override
    public void loadData() {
        Logger.i("oye", "loadData");
        if (mPresenter.isUseLocalListData) {
            Logger.i("oye", "isUseLocalListData = true");
            mPresenter.refreshData(isLoadEnd);
            // 将加载已经结束标记变为true,在 refreshData 方法中，new 一个空的集合显示加载结束
            isLoadEnd = true;
        } else {
            Logger.i("oye", "isUseLocalListData = false");
            mPresenter.getListData(page);
        }
    }

    @Override
    public BaseQuickAdapter getAdapter() {
        final GoodsListAdapter mAdapter = new GoodsListAdapter(this, true);
        mAdapter.setOnPayForDepositResultListener(new BidOrAlterDialog.OnPayForDepositResultListener() {
            @Override
            public void onSucceed() {
                // 使用本地的列表数据，报价成功，删除该项
                if (mPresenter.isUseLocalListData) {
                    for (GoodsListData d : mData) {
                        if (d.getGoods_id().equals(mAdapter.biddingGoodsId)) {
                            mData.remove(d);
                            mAdapter.notifyDataSetChanged();
                            return;
                        }
                    }
                } else {
                    recyclerViewWrapper.showRefreshing();
                }
            }
        });
        return mAdapter;
    }

    @Override
    public void showTitle(String str) {
        tvTitle.setText(str);
    }


    @OnClick({R.id.iv_close, R.id.rl_root})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
            case R.id.rl_root:
                finish();
                overridePendingTransition(com.topjet.common.R.anim.anim_no, com.topjet.common.R.anim.slide_out_bottom);
                break;
        }
    }
}
