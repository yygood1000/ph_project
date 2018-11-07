package com.topjet.crediblenumber.goods.view.activity;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.topjet.common.base.view.activity.IListView;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.widget.RecyclerViewWrapper.BaseRecyclerViewActivity;
import com.topjet.crediblenumber.R;
import com.topjet.crediblenumber.goods.modle.bean.EconomicListData;
import com.topjet.crediblenumber.goods.modle.params.GetEconomicParams;
import com.topjet.crediblenumber.goods.modle.serverAPI.GoodsCommand;
import com.topjet.crediblenumber.goods.modle.serverAPI.GoodsCommandAPI;
import com.topjet.crediblenumber.goods.presenter.EconomicPresenter;
import com.topjet.crediblenumber.goods.view.adapter.EconomicListAdapter;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 货运经纪人 页面
 */
public class EconomicListActivity extends BaseRecyclerViewActivity<EconomicPresenter, EconomicListData>
        implements IListView<EconomicListData> {
    @BindView(R.id.tv_title)
    TextView tvTitle;

    @Override
    protected void initPresenter() {
        mPresenter = new EconomicPresenter(this, mContext, new GoodsCommand(GoodsCommandAPI.class, this));
    }

    @Override
    protected int getLayoutResId() {
        noHasTitle();
        return R.layout.activity_around_goods_map_list;
    }

    @Override
    public void initView() {
        super.initView();
        tvTitle.setText("共为您找到以下经纪人");
    }

    @Override
    protected void initData() {
        refresh();
    }

    @OnClick(R.id.iv_close)
    public void onViewClicked() {
        finish();
        overridePendingTransition(com.topjet.common.R.anim.anim_no, com.topjet.common.R.anim.slide_out_bottom);
    }

    @Override
    public BaseQuickAdapter getAdapter() {
        return new EconomicListAdapter((MvpActivity) mContext);
    }

    @Override
    public void loadData() {
        mPresenter.getEconomicList(page);
    }

    /**
     * 外部跳转页面方法
     */
    public static void turnToEconomicActivity(MvpActivity mActivity, GetEconomicParams params) {
        mActivity.overridePendingTransition(R.anim.slide_in_bottom, com.topjet.common.R.anim.anim_no);
        mActivity.turnToActivity(EconomicListActivity.class, params);
    }
}
