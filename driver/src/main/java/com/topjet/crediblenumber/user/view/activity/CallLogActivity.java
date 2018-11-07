package com.topjet.crediblenumber.user.view.activity;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.topjet.common.base.dialog.AutoDialog;
import com.topjet.common.utils.ResourceUtils;
import com.topjet.common.widget.MyTitleBar;
import com.topjet.common.widget.RecyclerViewWrapper.BaseRecyclerViewActivity;
import com.topjet.crediblenumber.R;
import com.topjet.crediblenumber.user.modle.bean.CallLogData;
import com.topjet.crediblenumber.user.presenter.CallLogPresenter;
import com.topjet.crediblenumber.user.view.adapter.CallLogAdapter;

/**
 * Created by yy on 2017/10/16.
 * <p>
 * 通话记录
 */

public class CallLogActivity extends BaseRecyclerViewActivity<CallLogPresenter, CallLogData> implements
        CallLogView<CallLogData> {

    @Override
    protected void initPresenter() {
        mPresenter = new CallLogPresenter(this, mContext);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_call_log;
    }

    @Override
    protected void initTitle() {
        super.initTitle();
        getMyTitleBar().setMode(MyTitleBar.Mode.BACK_TITLE_RTEXT).setTitleText(ResourceUtils.getString(R.string
                .call_log)).setRightText(ResourceUtils.getString(R.string.clear_all));
    }

    @Override
    protected void initView() {
        super.initView();
        recyclerViewWrapper.getTvEmpty().setText("暂无订单通话记录");
        recyclerViewWrapper.getTvEmptyMini().setText("您联系过的订单信息，将会显示在这里");
        recyclerViewWrapper.getTvBtnEmpty().setVisibility(View.GONE);
    }

    @Override
    protected void initData() {
        super.initData();
        refresh();
    }

    @Override
    public void errorClick() {
        refresh();
    }

    @Override
    public void emptyClick() {
        refresh();
    }

    @Override
    protected void onClickRightText() {
        final AutoDialog dialog = new AutoDialog(mContext);
        dialog.setTitle("确定要清空吗？");
        dialog.setLeftText(com.topjet.common.R.string.cancel);
        dialog.setRightText(com.topjet.common.R.string.confirm);
        dialog.setOnBothConfirmListener(new AutoDialog.OnBothConfirmListener() {
            @Override
            public void onLeftClick() {
                dialog.toggleShow();
            }

            @Override
            public void onRightClick() {
                // 清空
                mPresenter.clearCallPhoneInfo();
                dialog.toggleShow();
            }
        });
        dialog.toggleShow();
    }

    @Override
    public BaseQuickAdapter getAdapter() {
        CallLogAdapter mAdapter = new CallLogAdapter(this);
        /**
         * 订单失效，刷新列表
         */
        mAdapter.setOrderIsUnEnableListener(new CallLogAdapter.OrderIsUnEnableListener() {
            @Override
            public void unable() {
                refresh();
            }
        });
        return mAdapter;
    }

    @Override
    public void loadData() {
        mPresenter.getCallPhoneInfo(page);
    }
}
