package com.topjet.shipper.deliver.view.activity;

import android.app.Activity;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.topjet.common.base.view.activity.IListView;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.ResourceUtils;
import com.topjet.common.widget.MyTitleBar;
import com.topjet.common.widget.RecyclerViewWrapper.BaseRecyclerViewActivity;
import com.topjet.shipper.R;
import com.topjet.shipper.deliver.modle.bean.OftenGoodsListItem;
import com.topjet.shipper.deliver.modle.params.OwnerGoodsParams;
import com.topjet.shipper.deliver.modle.serverAPI.DeliverCommand;
import com.topjet.shipper.deliver.modle.serverAPI.DeliverCommandAPI;
import com.topjet.shipper.deliver.presenter.OftenGoodsListPresenter;
import com.topjet.shipper.deliver.view.adapter.OftenGoodsListAdapter;

/**
 * 常发货源列表
 * Created by tsj004 on 2017/8/29.
 */

public class OftenGoodsListActivity extends BaseRecyclerViewActivity<OftenGoodsListPresenter, OftenGoodsListItem>
        implements IListView<OftenGoodsListItem> {
    @Override
    public void initTitle() {
        getMyTitleBar().setMode(MyTitleBar.Mode.BACK_TITLE).setTitleText(R.string.oftengoods_title);
    }

    @Override
    protected void initPresenter() {
        mPresenter = new OftenGoodsListPresenter(this, this, new DeliverCommand(DeliverCommandAPI.class, this));
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_often_goods_list;
    }

    @Override
    protected void initView() {
        super.initView();
        recyclerViewWrapper.getTvEmpty().setText(ResourceUtils.getString(R.string.oftengoods_nodata));
    }

    @Override
    protected void initData() {
        super.initData();
        refresh();
    }

    public void setResultAndFinish1(OwnerGoodsParams params) {
        Logger.i("oye", "000 " + params.toString());
        setResultAndFinish(Activity.RESULT_OK, params);
    }

    @Override
    public BaseQuickAdapter getAdapter() {
        OftenGoodsListAdapter adapter = new OftenGoodsListAdapter(mContext);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int id = view.getId();
                if (id == R.id.rl_goodsinfo) {
                    //选择货源
                    if (adapter != null) {
                        if (adapter.getItem(position) != null) {
                            OftenGoodsListItem item = (OftenGoodsListItem) adapter.getItem(position);
                            if (item.getIsSelect()) {
                                ((OftenGoodsListItem) adapter.getItem(position)).setIsSelect("0");
                            } else {
                                ((OftenGoodsListItem) adapter.getItem(position)).setIsSelect("1");
                            }
                            adapter.notifyDataSetChanged();//更新列表

                            // 跳转回发货页面
                            OwnerGoodsParams params = new OwnerGoodsParams();
                            params.setInType(OwnerGoodsParams.IN_TYPE_COPY);
                            params.setGoods_id(item.getGoods_id());
                            setResultAndFinish1(params);
                        }
                    }
                }
            }
        });
        return adapter;
    }

    @Override
    public void loadData() {
        mPresenter.oftenGoodsList(page);
    }
}
