package com.topjet.shipper.order.view.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.topjet.common.base.dialog.AutoDialogHelper;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.order_detail.modle.bean.ShowOfferList;
import com.topjet.common.order_detail.modle.params.GetShowOfferListParams;
import com.topjet.common.order_detail.modle.serverAPI.OrderDetailCommand;
import com.topjet.common.order_detail.modle.serverAPI.OrderDetailCommandApi;
import com.topjet.common.user.modle.extra.PhoneExtra;
import com.topjet.common.utils.CallPhoneUtils;
import com.topjet.common.widget.MyTitleBar;
import com.topjet.common.widget.RecyclerViewWrapper.BaseRecyclerViewActivity;
import com.topjet.shipper.R;
import com.topjet.shipper.familiar_car.view.activity.TruckInfoActivity;
import com.topjet.shipper.order.modle.extra.ShowOfferExtra;
import com.topjet.shipper.order.presenter.ShowOfferPresenter;
import com.topjet.shipper.order.view.adapter.ShowOfferAdapter;
import com.topjet.shipper.user.view.activity.UserInfoActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * creator: zhulunjun
 * time:    2017/9/12
 * describe: 货主查看报价
 */

public class ShowOfferActivity extends BaseRecyclerViewActivity<ShowOfferPresenter, ShowOfferList> implements
        ShowOfferView<ShowOfferList> {
    @BindView(R.id.tv_filter_time)
    TextView tvFilterTime;
    @BindView(R.id.iv_filter_time)
    ImageView ivFilterTime;
    @BindView(R.id.ll_filter_time)
    LinearLayout llFilterTime;
    @BindView(R.id.tv_filter_offer)
    TextView tvFilterOffer;
    @BindView(R.id.iv_filter_offer)
    ImageView ivFilterOffer;
    @BindView(R.id.ll_filter_offer)
    LinearLayout llFilterOffer;
    @BindView(R.id.tv_filter_evaluate)
    TextView tvFilterEvaluate;
    @BindView(R.id.iv_filter_evaluate)
    ImageView ivFilterEvaluate;
    @BindView(R.id.ll_filter_evaluate)
    LinearLayout llFilterEvaluate;

    // 1: 正序 2:倒序 默认倒序
    private String filterTime = "2", filterOffer = "2", filterEvaluate = "2";
    private static final String POSITIVE = "1"; // 正序
    private static final String REVERSE = "2"; // 倒序
    private int type = TIME; // 当前排序
    private static final int TIME = 1, OFFER= 2, EVALUATE = 3;


    private ShowOfferAdapter mAdapter;

    @Override
    protected void initPresenter() {
        mPresenter = new ShowOfferPresenter(this, this, new OrderDetailCommand(OrderDetailCommandApi.class, this));
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_show_offer;
    }

    @Override
    protected void initTitle() {
        super.initTitle();
        getMyTitleBar().setMode(MyTitleBar.Mode.BACK_TITLE_RTEXT)
                .setTitleText("报价列表")
                .setRightText("撤销订单");
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        mPresenter.onCreate();
    }

    @Override
    protected void initView() {
        super.initView();
        setEmptyText("暂无司机报价");
    }


    @Override
    public BaseQuickAdapter getAdapter() {
        mAdapter = new ShowOfferAdapter();
        mAdapter.setShowOfferClick(onShowOfferClickListener);
        return mAdapter;
    }

    @Override
    protected void onClickRightText() {
        super.onClickRightText();
        mPresenter.cancelGoods();
    }

    /**
     * 得到请求参数
     */
    public GetShowOfferListParams getOfferListParams() {
        GetShowOfferListParams params = new GetShowOfferListParams();
        params.setOrder_comment_level(type == EVALUATE ? filterEvaluate : "");
        params.setOrder_fee(type == OFFER ? filterOffer : "");
        params.setOrder_time(type == TIME ? filterTime : "");
        return params;
    }

    @Override
    public void loadData() {
        mPresenter.showOfferList(page, getOfferListParams());
    }

    /**
     * 清空确认成交实体对象
     */
    @Override
    public void clearConfirmOfferItem() {
        mAdapter.clearConfirmOffer();
    }

    @Override
    public void errorClick() {
        super.errorClick();
        refresh();
    }

    @Override
    public void emptyClick() {
        super.emptyClick();
        refresh();
    }

    /**
     * 时间筛选
     */
    @OnClick(R.id.ll_filter_time)
    public void timeClick() {
        downOffer(false);
        downEvaluate(false);
        type = TIME;
        if (filterTime.equals(REVERSE)) {
            upTime();
        } else {
            downTime(true);
        }
        refresh();
    }


    /**
     * 报价筛选
     */
    @OnClick(R.id.ll_filter_offer)
    public void offerClick() {
        downTime(false);
        downEvaluate(false);
        type = OFFER;
        if (filterOffer.equals(REVERSE)) {
           upOffer();
        } else {
           downOffer(true);
        }
        refresh();
    }

    /**
     * 好评度筛选
     */
    @OnClick(R.id.ll_filter_evaluate)
    public void evaluateClick() {
        downOffer(false);
        downTime(false);
        type = EVALUATE;
        if (filterEvaluate.equals(REVERSE)) {
            upEvaluate();
        } else {
            downEvaluate(true);
        }
        refresh();
    }

    public void downEvaluate(boolean isCheck){
        if(isCheck){
            ivFilterEvaluate.setImageResource(R.drawable.iv_icon_arrow_down_green);
            tvFilterEvaluate.setTextColor(getResources().getColor(R.color.color_16ca4e));
        } else {
            ivFilterEvaluate.setImageResource(R.drawable.iv_icon_arrow_black_down);
            tvFilterEvaluate.setTextColor(getResources().getColor(R.color.text_color_666666));
        }
        filterEvaluate = REVERSE;
    }
    public void downOffer(boolean isCheck){
        if(isCheck) {
            ivFilterOffer.setImageResource(R.drawable.iv_icon_arrow_down_green);
            tvFilterOffer.setTextColor(getResources().getColor(R.color.color_16ca4e));
        } else {
            ivFilterOffer.setImageResource(R.drawable.iv_icon_arrow_black_down);
            tvFilterOffer.setTextColor(getResources().getColor(R.color.text_color_666666));
        }
        filterOffer = REVERSE;
    }
    public void downTime(boolean isCheck){
        if(isCheck) {
            ivFilterTime.setImageResource(R.drawable.iv_icon_arrow_down_green);
            tvFilterTime.setTextColor(getResources().getColor(R.color.color_16ca4e));
        } else {
            ivFilterTime.setImageResource(R.drawable.iv_icon_arrow_black_down);
            tvFilterTime.setTextColor(getResources().getColor(R.color.text_color_666666));
        }
        filterTime = REVERSE;
    }
    public void upEvaluate(){
        ivFilterEvaluate.setImageResource(R.drawable.iv_icon_arrow_green);
        tvFilterEvaluate.setTextColor(getResources().getColor(R.color.color_16ca4e));
        filterEvaluate = POSITIVE;
    }
    public void upOffer(){
        ivFilterOffer.setImageResource(R.drawable.iv_icon_arrow_green);
        tvFilterOffer.setTextColor(getResources().getColor(R.color.color_16ca4e));
        filterOffer = POSITIVE;
    }
    public void upTime(){
        ivFilterTime.setImageResource(R.drawable.iv_icon_arrow_green);
        tvFilterTime.setTextColor(getResources().getColor(R.color.color_16ca4e));
        filterTime = POSITIVE;
    }

    private ShowOfferAdapter.OnShowOfferClickListener onShowOfferClickListener = new ShowOfferAdapter
            .OnShowOfferClickListener() {

        @Override
        public void contactClick(View v, ShowOfferList item) {
            // 去车辆详情
            TruckInfoActivity.truckInfo(ShowOfferActivity.this, item.getDriver_truck_id());
        }

        @Override
        public void callClick(View v, ShowOfferList item) {
            // 拨打电话
            new CallPhoneUtils().showCallDialogNotUpload((MvpActivity) mContext, item.getDriver_name(), item
                    .getDriver_mobile());
        }

        @Override
        public void confirmClick(View v, final ShowOfferList item) {
            // 确认成交
            // 这里需要判断是否已经成交过司机了，如果有已经确认过的，需要弹窗
            if (mAdapter.getConfirmedOffer() != null) {
                AutoDialogHelper.showContent(ShowOfferActivity.this,
                        "您的订单已指派过司机，运费报价" + mAdapter.getConfirmedOffer().getTransport_fee() +
                                "元；\n" +
                                "确定更换为当前司机？运费报价" + item.getTransport_fee() + "元", new AutoDialogHelper
                                .OnConfirmListener() {
                            @Override
                            public void onClick() {
                                mPresenter.confirmClick(item);
                            }
                        }).show();
            } else {
                mPresenter.confirmClick(item);
            }
        }

        @Override
        public void cancelClick(View v, ShowOfferList item) {
            // 取消交易
            mPresenter.cancelClick(v, item);
        }

        @Override
        public void avatarClick(View v, ShowOfferList item) {
            // 司机主页 诚信查询结果
            turnToActivity(UserInfoActivity.class, new PhoneExtra(item.getDriver_mobile()));
        }
    };

    /**
     * 跳转到当前页面
     *
     * @param activity 页面
     * @param extra    参数
     */
    public static void toShowOffer(MvpActivity activity, ShowOfferExtra extra) {
        activity.turnToActivity(ShowOfferActivity.class, extra);
    }
}
