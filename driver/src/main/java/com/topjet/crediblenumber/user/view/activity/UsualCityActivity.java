package com.topjet.crediblenumber.user.view.activity;

import android.app.Activity;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.topjet.common.base.busManger.Subscribe;
import com.topjet.common.resource.bean.AreaInfo;
import com.topjet.common.common.modle.bean.UsualCityBean;
import com.topjet.common.common.modle.event.AreaSelectedData;
import com.topjet.common.common.view.dialog.CitySelectPopupWindow;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.ResourceUtils;
import com.topjet.common.utils.StringUtils;
import com.topjet.common.utils.Toaster;
import com.topjet.common.widget.MyTitleBar;
import com.topjet.common.widget.RecyclerViewWrapper.BaseRecyclerViewActivity;
import com.topjet.crediblenumber.R;
import com.topjet.crediblenumber.user.modle.serverAPI.UsualCityCommand;
import com.topjet.crediblenumber.user.modle.serverAPI.UsualCityCommandAPI;
import com.topjet.crediblenumber.user.presenter.UsualCityPresenter;
import com.topjet.crediblenumber.user.view.adapter.UsualCityAdapter;

import java.util.List;

import butterknife.OnClick;

/**
 * Created by yy on 2017/10/18.
 * <p>
 * 常跑城市
 */

public class UsualCityActivity extends BaseRecyclerViewActivity<UsualCityPresenter, UsualCityBean> implements
        UsualCityView<UsualCityBean> {
    // 添加新的常跑城市
    private int ADD_NEW_USUAL_CITY = 1;
    // 修改已有的常跑城市
    private int ALTER_USUAL_CITY = 2;
    // 城市选择框标记 ADD_NEW_USUAL_CITY：添加新的常跑城市 ALTER_USUAL_CITY：修改已有的常跑城市
    private int flag;

    private UsualCityBean editingItem;// 正在被修改的常跑城市项

    @Override
    protected void initPresenter() {
        mPresenter = new UsualCityPresenter(this, mContext, new UsualCityCommand(UsualCityCommandAPI.class, this));
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_usual_city;
    }

    @Override
    protected void initTitle() {
        getMyTitleBar().setMode(MyTitleBar.Mode.BACK_TITLE_RTEXT).setTitleText(ResourceUtils.getString(R.string
                .add_usual_city)).setRightText(ResourceUtils
                .getString(R.string.add));
    }

    @Override
    protected void initView() {
        super.initView();
        setNoPageMode();
    }

    @Override
    protected void initData() {
        refresh();
    }

    @Override
    public void loadData() {
        mPresenter.getUsualCityList();
    }

    @Override
    public BaseQuickAdapter getAdapter() {
        final UsualCityAdapter mAdapter = new UsualCityAdapter();
        mAdapter.setOnClickListener(new UsualCityAdapter.OnClickListener() {
            @Override
            public void onClickDelete(UsualCityBean item, int position) {
                // 删除常跑城市
                getData().remove(position);
                if (StringUtils.isNotBlank(item.getBussinessLineId())) {
                    getData().remove(item);
                    // 往删除集合中插入数据
                    mPresenter.updataDeleteList(item.getBussinessLineId());
                } else {
                    // 删除新增集合中的路线
                    mPresenter.removeAddListItem(item.getBusinessLineCitycode());
                }
                notifyDataSetChanged();
            }

            @Override
            public void onClickItem(UsualCityBean item, int position) {
                flag = ALTER_USUAL_CITY;
                editingItem = item;
                new CitySelectPopupWindow(TAG).showCitySelectPopupWindow((Activity) mContext, getMyTitleBar(), false,
                        true, true);
            }
        });
        return mAdapter;
    }


    @OnClick(R.id.tv_submit)
    public void onViewClicked() {
        // 提交按钮
        mPresenter.updataUsualCity();
    }

    /**
     * 添加常跑城市按钮
     */
    @Override
    protected void onClickRightText() {
        if (getData().size() >= 8) {
            showToast("最多允许添加八个城市！");
        } else {
            flag = ADD_NEW_USUAL_CITY;
            new CitySelectPopupWindow(TAG).showCitySelectPopupWindow(this, getMyTitleBar(), false, true, true);
        }

    }

    /**
     * 添加/修改 常跑城市，返回事件
     */
    @Subscribe
    public void onEvent(AreaSelectedData data) {
        if (data.getTag().equals(TAG)) {
            AreaInfo areaInfo = data.getAreaInfo();

            /**
             * 先在getData()中寻找一遍，存不存在，存在则不做任何操作
             */
            if (getData() != null) {
                for (int i = 0; i < getData().size(); i++) {
                    if (areaInfo.getLastCityId().equals(getData().get(i).getBusinessLineCitycode())) {
                        Toaster.showLongToast("该城市已存在");
                        return;
                    }
                }
            }

            if (flag == ALTER_USUAL_CITY) {
                // 判断是否修改的是新增的数据
                if (StringUtils.isNotBlank(editingItem.getBussinessLineId())) {
                    Logger.i("oye", "修改了已经添加成功的城市");
                    // 往修改集合中插入数据
                    mPresenter.setUpdataList(areaInfo.getLastCityId(), editingItem.getBussinessLineId());
                } else {
                    Logger.i("oye", "修改了新增的城市");
                    // 修改新增集合中的数据
                    mPresenter.updataAddList(editingItem.getBusinessLineCitycode(), areaInfo.getLastCityId());
                }
                // 更新本地的数据
                editingItem.setBusiness_line_city_id(areaInfo.getLastCityId());
                editingItem.setBusiness_line_city(areaInfo.getFullCityName());
            } else if (flag == ADD_NEW_USUAL_CITY) {
                Logger.i("oye", "新增了城市");
                // 往新增集合中插入数据
                mPresenter.updataAddList("", areaInfo.getLastCityId());
                // 更新本地的数据
                getData().add(new UsualCityBean(areaInfo.getLastCityId(), areaInfo.getFullCityName()));
            }

            //TODO 修改或添加常跑省市
            notifyDataSetChanged();
        }
    }

    @Override
    public void loadSuccess(List<UsualCityBean> data) {
        super.loadSuccess(data);
        if (data == null || data.size() == 0) {
            getMyTitleBar().setMode(MyTitleBar.Mode.BACK_TITLE).setTitleText(ResourceUtils.getString(R.string
                    .add_usual_city));
        }
    }
}
