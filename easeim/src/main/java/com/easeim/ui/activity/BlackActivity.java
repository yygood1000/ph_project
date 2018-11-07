package com.easeim.ui.activity;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easeim.R;
import com.easeim.presenter.BlackListPresenter;
import com.easeim.ui.BlackListAdapter;
import com.easeim.ui.activity.view.BlackListView;
import com.topjet.common.base.dialog.AutoDialogHelper;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.utils.ListUtils;
import com.topjet.common.widget.MyTitleBar;
import com.topjet.common.widget.RecyclerViewWrapper.BaseRecyclerViewActivity;

import java.util.List;

/**
 * creator: zhulunjun
 * time:    2017/12/4
 * describe: 黑名单页面
 */

public class BlackActivity extends BaseRecyclerViewActivity<BlackListPresenter, String> implements BlackListView<String> {


    @Override
    protected void initPresenter() {
        mPresenter = new BlackListPresenter(this,this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.em_activity_black;
    }

    @Override
    protected void initTitle() {
        super.initTitle();
        getMyTitleBar().setMode(MyTitleBar.Mode.BACK_TITLE_RIMG)
                .setTitleText("黑名单")
                .setRightImg(CMemoryData.isDriver() ?
                        R.drawable.icon_black_list_right :
                        R.drawable.icon_black_list_right_green);
    }

    @Override
    protected void initView() {
        super.initView();

        recyclerViewWrapper.getIvEmpty().setImageResource(R.drawable.icon_black_list_empty);
        recyclerViewWrapper.getTvBtnEmpty().setVisibility(View.GONE);
        recyclerViewWrapper.getTvEmpty().setVisibility(View.GONE);
        recyclerViewWrapper.getTvEmptyMini().setText(getResources().getText(R.string.black_list_empty_text));
        setNoPageMode();
        setShowEmpty(true);
    }

    @Override
    protected void onClickRightImg() {
        super.onClickRightImg();
       new BlackListDialog(this).show();
    }

    @Override
    public BaseQuickAdapter getAdapter() {
        BlackListAdapter mBlackListAdapter = new BlackListAdapter();
        mBlackListAdapter.setBlackOutClick(new BlackListAdapter.BlackOutClick() {
            @Override
            public void outClick(final String item) {
                AutoDialogHelper.showContent(BlackActivity.this,
                        getResources().getString(R.string.out_black_list_dialog),
                        new AutoDialogHelper.OnConfirmListener() {
                    @Override
                    public void onClick() {
                        showLoadingDialog();
                        mPresenter.outBlackList(item);
                    }
                }).show();

            }
        });
        return mBlackListAdapter;
    }

    @Override
    protected void initData() {
        super.initData();
        loadData();
    }

    @Override
    public void loadData() {
        mPresenter.getBlackList();
    }

    @Override
    public void loadSuccess(List<String> data) {
        super.loadSuccess(data);
        // 空就不显示右上角按钮
        if(ListUtils.isEmpty(data)){
            getMyTitleBar().setMode(MyTitleBar.Mode.BACK_TITLE);
        }else {
            getMyTitleBar().setMode(MyTitleBar.Mode.BACK_TITLE_RIMG)
                    .setRightImg(R.drawable.icon_black_list_right);

        }
    }

    @Override
    public void removeBlackListSuccess() {
        loadData();
        cancelShowLoadingDialog();
    }

}
