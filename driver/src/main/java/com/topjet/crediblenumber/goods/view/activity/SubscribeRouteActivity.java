package com.topjet.crediblenumber.goods.view.activity;

import android.view.View;
import android.widget.Button;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.topjet.common.utils.ResourceUtils;
import com.topjet.common.widget.MyTitleBar;
import com.topjet.common.widget.RecyclerViewWrapper.BaseRecyclerViewActivity;
import com.topjet.crediblenumber.R;
import com.topjet.crediblenumber.goods.modle.bean.SubscribeRouteListItem;
import com.topjet.crediblenumber.goods.modle.extra.SubscribeRouteGoodsExtra;
import com.topjet.crediblenumber.goods.modle.serverAPI.SubscribeRouteCommand;
import com.topjet.crediblenumber.goods.modle.serverAPI.SubscribeRouteCommandAPI;
import com.topjet.crediblenumber.goods.presenter.SubscribeRoutePresenter;
import com.topjet.crediblenumber.goods.view.adapter.SubscribeRouteListAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 订阅路线
 */
public class SubscribeRouteActivity extends BaseRecyclerViewActivity<SubscribeRoutePresenter, SubscribeRouteListItem>
        implements SubscribeRouteView<SubscribeRouteListItem> {
    @BindView(R.id.btn_operate)
    Button btnOperate;

    // 标题栏
    private MyTitleBar myTitleBar;
    // 全部路线的实体类，进入编辑状态时取出，点击完成后再次加入到首位
    private SubscribeRouteListItem firstData;
    private SubscribeRouteListAdapter mAdapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_subscribe_route;
    }

    @Override
    protected void initTitle() {
        myTitleBar = getMyTitleBar().setMode(MyTitleBar.Mode.BACK_TITLE_RTEXT).setTitleText("订阅路线")
                .setRightText("编辑");
    }

    @Override
    protected void initPresenter() {
        mPresenter = new SubscribeRoutePresenter(this, mContext, new SubscribeRouteCommand(SubscribeRouteCommandAPI
                .class, this));
    }

    @Override
    protected void initView() {
        super.initView();

        recyclerViewWrapper.getTvEmpty().setText(ResourceUtils.getString(R.string.subscribe_route_empty_warning));
        recyclerViewWrapper.getTvBtnEmpty().setText(ResourceUtils.getString(R.string.add_subscribe_route));
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    @Override
    protected void onClickRightText() {
        // 编辑/完成
        if (!recyclerViewWrapper.isLoading) {
            if (mPresenter.isEditing) {
                // 处于编辑状态，将页面变为查看状态。
                mPresenter.isEditing = false;
                // 允许刷新
                recyclerViewWrapper.getSwipeRefreshLayout().setEnabled(true);
                myTitleBar.setRightText(getString(R.string.edit));
                btnOperate.setText(ResourceUtils.getString(R.string.add_subscribe_route));
                // 列表UI 根据编辑状态变更
                mAdapter.setEditing(mPresenter.isEditing);
                // 将全部路线实体类添加到第一项
                mAdapter.addData(0, firstData);
                resetChecked();
            } else {
                // 处于查看状态，将页面变为编辑状态
                mPresenter.isEditing = true;
                // 禁止刷新
                recyclerViewWrapper.getSwipeRefreshLayout().setEnabled(false);
                myTitleBar.setRightText(getString(R.string.complete));
                btnOperate.setText(ResourceUtils.getString(R.string.delete));
                // 列表UI 根据编辑状态变更
                mAdapter.setEditing(mPresenter.isEditing);
                // 进入编辑状态，取出全部路线实体类
                firstData = mAdapter.getItem(0);
                mAdapter.remove(0);
            }
        }
    }

    @Override
    public void emptyClick() {
        // 添加订阅路线
        turnToActivity(AddSubscribeRouteActivity.class);
    }

    @Override
    public void errorClick() {
        refresh();
    }

    @Override
    public boolean onEmpty() {
        myTitleBar = getMyTitleBar().setMode(MyTitleBar.Mode.BACK_TITLE);
        btnOperate.setVisibility(View.GONE);
        return super.onEmpty();
    }

    @Override
    public void loadSuccess(List<SubscribeRouteListItem> data) {
        super.loadSuccess(data);
        myTitleBar = getMyTitleBar().setMode(MyTitleBar.Mode.BACK_TITLE_RTEXT);
        btnOperate.setVisibility(View.VISIBLE);
    }

    /**
     * 按钮操作
     */
    @OnClick(R.id.btn_operate)
    public void onViewClicked() {
        if (mPresenter.isEditing) {
            // 删除订阅路线
            mPresenter.deleteRoute(mAdapter.getDeleteRouteIds());
        } else {
            // 添加订阅路线
            turnToActivity(AddSubscribeRouteActivity.class);
        }
    }

    @Override
    public BaseQuickAdapter getAdapter() {
        return mAdapter = new SubscribeRouteListAdapter(mContext, itemClickListener);
    }

    @Override
    public void loadData() {
        mPresenter.refreshData();
    }

    /**
     * 自定义列表项点击事件
     */
    public SubscribeRouteListAdapter.OnJumpToGoodsListListener itemClickListener = new SubscribeRouteListAdapter
            .OnJumpToGoodsListListener() {
        @Override
        public void jump(boolean isAllRoute, String departCityName, String destinationCityName, String
                subscribeLineId) {
            SubscribeRouteGoodsExtra extra = new SubscribeRouteGoodsExtra(isAllRoute, departCityName,
                    destinationCityName, subscribeLineId);
            // 跳转订阅路线货源列表页
            turnToActivity(SubscribeRouteGoodsListActivity.class, extra);
        }
    };

    /**
     * 退出编辑状态的时候把之前的选中状态全部变为false
     */
    public void resetChecked() {
        for (SubscribeRouteListItem item : getData()) {
            item.setSeleced(false);
        }
    }

    /**
     * 删除订阅路线成功，清空删除集合
     */
    public void clearDeleteRouteIdsList() {
        mAdapter.clearDeleteRouteIdsList();
    }
}
