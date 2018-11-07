package com.topjet.crediblenumber.order.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.topjet.common.base.view.activity.IListView;
import com.topjet.common.order_detail.modle.bean.MyOfferList;
import com.topjet.common.order_detail.modle.serverAPI.OrderDetailCommand;
import com.topjet.common.order_detail.modle.serverAPI.OrderDetailCommandApi;
import com.topjet.common.utils.ListUtils;
import com.topjet.common.widget.MyTitleBar;
import com.topjet.common.widget.RecyclerViewWrapper.BaseRecyclerViewActivity;
import com.topjet.crediblenumber.R;
import com.topjet.crediblenumber.order.presenter.MyOfferPresenter;
import com.topjet.crediblenumber.order.view.adapter.MyOfferAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * creator: zhulunjun
 * time:    2017/9/12
 * describe: 司机我的报价列表
 */

public class MyOfferActivity extends BaseRecyclerViewActivity<MyOfferPresenter, MyOfferList> implements
        IListView<MyOfferList> {


    @BindView(R.id.cb_all)
    CheckBox cbAll;

    MyOfferAdapter mAdapter;
    @BindView(R.id.ll_check_all)
    LinearLayout llCheckAll;
    @BindView(R.id.tv_btn_cancel)
    TextView tvBtnCancel;

    @Override
    protected void initPresenter() {
        mPresenter = new MyOfferPresenter(this, this, new OrderDetailCommand(OrderDetailCommandApi.class, this));
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_my_offer;
    }

    @Override
    protected void initTitle() {
        super.initTitle();
        getMyTitleBar().setMode(MyTitleBar.Mode.BACK_TITLE)
                .setTitleText("我的报价");
    }


    @Override
    public BaseQuickAdapter getAdapter() {
        mAdapter = new MyOfferAdapter();
        return mAdapter;
    }

    @Override
    public void loadData() {
        mPresenter.showOfferList(page);
    }


    @Override
    public void loadSuccess(List<MyOfferList> data) {
        super.loadSuccess(data);
        // 加载成功，并且有数据
        if ((!llCheckAll.isShown()) && !ListUtils.isEmpty(data)) {
            llCheckAll.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void initView() {
        super.initView();
        setEmptyText("没有报价");
        showLoadingDialog();

        mAdapter.setMyOfferClickListener(new MyOfferAdapter.MyOfferClickListener() {
            @Override
            public void contentClick(View v, MyOfferList item) {
                OrderDetailActivity.toGoodsDetail(MyOfferActivity.this, item.getGoods_id());
            }

            @Override
            public void checkClick(boolean check, MyOfferList item) {
                mPresenter.checkClick(check, item);
                if (mPresenter.cancelOfferList.size() > 0) {
                    tvBtnCancel.setEnabled(true);
                } else {
                    tvBtnCancel.setEnabled(false);
                }
            }

            @Override
            public void payDepositClick(View v, MyOfferList item) {
                mPresenter.payDepositClick(v, item);
            }
        });
        cbAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mPresenter.checkAll(recyclerViewWrapper.getAdapter().getData());
                    tvBtnCancel.setEnabled(true);
                } else {
                    mPresenter.checkCancelAll(recyclerViewWrapper.getAdapter().getData());
                    tvBtnCancel.setEnabled(false);
                }
                recyclerViewWrapper.notifyDataSetChanged();
            }
        });

    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter.getLocationData();
    }

    private boolean isFirstInto = true;

    @Override
    protected void onResume() {
        super.onResume();
        if (!isFirstInto) {
            refresh();
        } else {
            isFirstInto = false;
        }
    }

    @Override
    public void emptyClick() {
        super.emptyClick();
        refresh();
    }

    @Override
    public void errorClick() {
        super.errorClick();
        refresh();
    }

    @Override
    public boolean onEmpty() {
        llCheckAll.setVisibility(View.GONE);
        return super.onEmpty();
    }

    /**
     * 撤销订单点击事件
     */
    @OnClick(R.id.tv_btn_cancel)
    public void cancelClick() {
        mPresenter.cancelOffer();
    }
}
