package com.topjet.common.user.view.activity;

import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.topjet.common.R;
import com.topjet.common.R2;
import com.topjet.common.resource.bean.OptionItem;
import com.topjet.common.common.view.dialog.SimpleOptionsSelectPopup;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.user.modle.bean.ReferrerInfoBean;
import com.topjet.common.user.modle.serverAPI.UserCommand;
import com.topjet.common.user.modle.serverAPI.UserCommandAPI;
import com.topjet.common.user.presenter.RecommendLogPresenter;
import com.topjet.common.user.view.adapter.RecommendLogAdapter;
import com.topjet.common.utils.LayoutUtils;
import com.topjet.common.utils.ResourceUtils;
import com.topjet.common.widget.RecyclerViewWrapper.BaseRecyclerViewActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by yy on 2017/11/6.
 * 推荐记录页面
 */

public class RecommendLogActivity extends BaseRecyclerViewActivity<RecommendLogPresenter, ReferrerInfoBean>
        implements RecommendLogView<ReferrerInfoBean> {
    @BindView(R2.id.cb_time_range)
    CheckBox cbTimeRange;
    @BindView(R2.id.cb_status)
    CheckBox cbStatus;
    @BindView(R2.id.rl_options)
    RelativeLayout rlOptions;

    private String timeRange = "0";//	查询时间范围	是	String	0全部 1 今天 2近一周 3近一个月 4 近三个月
    private String status = "0";//审核状态	是	String	0全部 1审核成功 2审核中 3审核失败 4无补贴
    private SimpleOptionsSelectPopup pop;// 下拉筛选弹窗;
    private ArrayList<OptionItem> timeOptions;
    private ArrayList<OptionItem> statusOptions;

    @Override
    public BaseQuickAdapter getAdapter() {
        return new RecommendLogAdapter();
    }

    @Override
    public void loadData() {
        mPresenter.getRecommendList(timeRange, status, page);
    }

    @Override
    protected void initPresenter() {
        mPresenter = new RecommendLogPresenter(this, mContext, new UserCommand(UserCommandAPI.class, this));
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_recommend_log;
    }

    @Override
    protected void initView() {
        super.initView();
        recyclerViewWrapper.getTvBtnEmpty().setVisibility(View.GONE);
        recyclerViewWrapper.getTvEmpty().setText(ResourceUtils.getString(R.string.recommend_list_empty_warning));

        if (!CMemoryData.isDriver()) {
            LayoutUtils.setDrawableRight(cbTimeRange, R.drawable.selector_cb_options_shipper);
            LayoutUtils.setDrawableRight(cbStatus, R.drawable.selector_cb_options_shipper);
            cbTimeRange.setTextColor(ResourceUtils.getColorStateList(R.color.selector_cb_text_color_22_to_16ca4e));
            cbStatus.setTextColor(ResourceUtils.getColorStateList(R.color.selector_cb_text_color_22_to_16ca4e));
        }
        // 初始化下拉筛选弹窗
        initPopWindow();
    }

    /**
     * 初始化下拉筛选弹窗
     */
    private void initPopWindow() {
        pop = new SimpleOptionsSelectPopup();
    }

    @Override
    protected void initData() {
        refresh();
        initSelectOptions();
    }

    private void initSelectOptions() {
        timeOptions = new ArrayList<>();
        timeOptions.add(new OptionItem("0", "全部时间"));
        timeOptions.add(new OptionItem("1", "今天"));
        timeOptions.add(new OptionItem("2", "近一周"));
        timeOptions.add(new OptionItem("3", "近一个月"));
        timeOptions.add(new OptionItem("4", "近三个月"));

        statusOptions = new ArrayList<>();
        statusOptions.add(new OptionItem("0", "全部状态"));
        statusOptions.add(new OptionItem("1", "审核成功"));
        statusOptions.add(new OptionItem("2", "审核中"));
        statusOptions.add(new OptionItem("3", "审核失败"));
        statusOptions.add(new OptionItem("4", "无补贴"));
    }

    @Override
    public void errorClick() {
        refresh();
    }

    @OnClick({R2.id.ll_time_range, R2.id.ll_status})
    public void onViewClicked(View view) {
        final int i = view.getId();
        if (i == R.id.ll_time_range) {
            // 时间筛选框
            cbTimeRange.setChecked(true);
            pop.showPopupWindow(this, rlOptions, timeOptions, new SimpleOptionsSelectPopup.SimpleOptionsPopListener() {
                @Override
                public void onItemClick(OptionItem item) {
                    timeRange = item.getCode();
                    cbTimeRange.setText(item.getName());
                    refresh();
                }

                @Override
                public void onPopdismissListener() {
                    cbTimeRange.setChecked(false);
                }
            });
        } else if (i == R.id.ll_status) {
            // 状态筛选框
            cbStatus.setChecked(true);
            pop.showPopupWindow(this, rlOptions, statusOptions, new SimpleOptionsSelectPopup.SimpleOptionsPopListener
                    () {
                @Override
                public void onItemClick(OptionItem item) {
                    status = item.getCode();
                    cbStatus.setText(item.getName());
                    refresh();
                }

                @Override
                public void onPopdismissListener() {
                    cbStatus.setChecked(false);
                }
            });
        }
    }
}
