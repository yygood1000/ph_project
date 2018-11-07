package com.topjet.crediblenumber.goods.view.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.common.modle.bean.GoodsListData;
import com.topjet.common.utils.ResourceUtils;
import com.topjet.common.widget.RecyclerViewWrapper.BaseRecyclerViewActivity;
import com.topjet.crediblenumber.R;
import com.topjet.crediblenumber.goods.modle.serverAPI.SubscribeRouteCommand;
import com.topjet.crediblenumber.goods.modle.serverAPI.SubscribeRouteCommandAPI;
import com.topjet.crediblenumber.goods.presenter.SubscribeRouteGoodsListPresenter;
import com.topjet.crediblenumber.goods.view.adapter.GoodsListAdapter;
import com.topjet.crediblenumber.goods.view.dialog.BidOrAlterDialog;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 订阅路线 货源 列表
 */
public class SubscribeRouteGoodsListActivity extends BaseRecyclerViewActivity<SubscribeRouteGoodsListPresenter,
        GoodsListData> implements SubscribeRouteGoodsListView<GoodsListData> {
    @BindView(R.id.tv_start_address)
    TextView tvStartAddress;
    @BindView(R.id.iv_center)
    ImageView ivCenter;
    @BindView(R.id.tv_end_address)
    TextView tvEndAddress;
    @BindView(R.id.tv_title_all_route)
    TextView tvTitleAllRoute;

    @Override
    protected int getLayoutResId() {
        noHasTitle();
        return R.layout.activity_subscribe_route_goods_list;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new SubscribeRouteGoodsListPresenter(this, mContext, new SubscribeRouteCommand
                (SubscribeRouteCommandAPI.class, this));
    }

    @Override
    public void initView() {
        super.initView();
        recyclerViewWrapper.getTvEmpty().setText(ResourceUtils.getString(R.string.goods_list_empty_warning));
        recyclerViewWrapper.getTvBtnEmpty().setText(ResourceUtils.getString(R.string.click_to_refresh));
    }

    @Override
    protected void initData() {
        // 获取货源列表数据
        refresh();
    }

    @Override
    public void setTitleAllRoute() {
        ivCenter.setVisibility(View.GONE);
        tvStartAddress.setVisibility(View.GONE);
        tvEndAddress.setVisibility(View.GONE);
        tvTitleAllRoute.setVisibility(View.VISIBLE);
    }

    /**
     * 设置标题
     */
    @Override
    public void setTitleText(String depart, String destination) {
        tvStartAddress.setText(depart);
        tvEndAddress.setText(destination);
    }

    @Override
    public void emptyClick() {
        refresh();
    }


    @OnClick(R.id.iv_title_bar_back)
    public void onViewClicked() {
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.updataSubscribeLineQueryTime();
    }

    @Override
    public BaseQuickAdapter getAdapter() {
        GoodsListAdapter mAdapter = new GoodsListAdapter((MvpActivity) mContext, true);
        mAdapter.setOnPayForDepositResultListener(new BidOrAlterDialog.OnPayForDepositResultListener() {
            @Override
            public void onSucceed() {
                refresh();
            }
        });
        return mAdapter;
    }

    @Override
    public void loadData() {
        mPresenter.loadData(page);
    }
}
