package com.topjet.common.order_detail.view.activity;

import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.topjet.common.R;
import com.topjet.common.R2;
import com.topjet.common.base.CommonProvider;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.common.view.activity.ShowBigImageActivity;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.order_detail.modle.bean.FreightInfo;
import com.topjet.common.order_detail.modle.bean.OrderUserInfo;
import com.topjet.common.order_detail.modle.bean.OwnerInfo;
import com.topjet.common.order_detail.modle.extra.OrderExtra;
import com.topjet.common.order_detail.modle.params.ShareGoodsParams;
import com.topjet.common.order_detail.modle.response.GoodsInfoResponse;
import com.topjet.common.order_detail.modle.response.OrderInfoResponse;
import com.topjet.common.order_detail.modle.serverAPI.OrderDetailCommand;
import com.topjet.common.order_detail.modle.serverAPI.OrderDetailCommandApi;
import com.topjet.common.order_detail.modle.state.FreightState;
import com.topjet.common.order_detail.modle.state.GoodsState;
import com.topjet.common.order_detail.modle.state.OrderState;
import com.topjet.common.order_detail.modle.state.StateRightMenuProvider;
import com.topjet.common.order_detail.modle.state.StateTopMsgProvider;
import com.topjet.common.order_detail.presenter.OrderDetailBasePresenter;
import com.topjet.common.order_detail.presenter.SkipControllerProtocol;
import com.topjet.common.order_detail.presenter.SkipControllerRefund;
import com.topjet.common.order_detail.view.dialog.ShareDialog;
import com.topjet.common.order_detail.view.pop.MenuPopupWindow;
import com.topjet.common.utils.CallPhoneUtils;
import com.topjet.common.utils.CheckUserStatusUtils;
import com.topjet.common.utils.DelayUtils;
import com.topjet.common.utils.GlideUtils;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.ResourceUtils;
import com.topjet.common.utils.ShareHelper;
import com.topjet.common.utils.StringUtils;
import com.topjet.common.utils.TimeUtils;
import com.topjet.common.utils.Toaster;
import com.topjet.common.widget.MyScrollView;
import com.topjet.common.widget.MyTitleBar;
import com.topjet.common.widget.SwipeRefreshLayout.SwipeRefreshLayout;
import com.topjet.common.widget.viewpagetitle.ViewPagerTitle;

import butterknife.BindView;
import butterknife.OnClick;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * creator: zhulunjun
 * time:    2017/9/4
 * describe:订单详情，货源详情页面的基类
 * 包括货主版，司机版
 * 所以共有的显示都在这里
 */

public abstract class OrderDetailBaseActivity extends MvpActivity<OrderDetailBasePresenter> implements
        OrderDetailBaseView {
    /**
     * 货源详情UI
     */
    @Nullable
    @BindView(R2.id.pager_title)
    public ViewPagerTitle pagerTitle;
    @Nullable
    @BindView(R2.id.view_pager)
    public ViewPager viewPager;

    /**
     * 共有UI
     */
    @Nullable
    @BindView(R2.id.tv_order_msg)
    public TextView tvOrderMsg;
    @Nullable
    @BindView(R2.id.tv_order_quote_num)
    public TextView tvOrderQuoteNum;
    @BindView(R2.id.tv_refresh_order)
    @Nullable
    public TextView tvRefreshOrder;
    @Nullable
    @BindView(R2.id.tv_order_id)
    public TextView tvOrderId;
    @Nullable
    @BindView(R2.id.tv_order_time)
    public TextView tvOrderTime;
    @Nullable
    @BindView(R2.id.ll_order_id)
    public LinearLayout llOrderId;
    @Nullable
    @BindView(R2.id.tv_start_city_name)
    public TextView tvStartCityName;
    @Nullable
    @BindView(R2.id.tv_start_city_address)
    public TextView tvStartCityAddress;
    @Nullable
    @BindView(R2.id.tv_name_phone)
    public TextView tvNamePhone;
    @Nullable
    @BindView(R2.id.tv_name_phone_end)
    public TextView tvNamePhoneEnd;
    @Nullable
    @BindView(R2.id.iv_gif)
    public ImageView ivGif;
    @Nullable
    @BindView(R2.id.tv_end_city_name)
    public TextView tvEndCityName;
    @Nullable
    @BindView(R2.id.tv_end_city_address)
    public TextView tvEndCityAddress;
    @Nullable
    @BindView(R2.id.iv_local_point)
    public ImageView ivLocalPoint;
    @Nullable
    @BindView(R2.id.tv_distance)
    public TextView tvDistance;
    @Nullable
    @BindView(R2.id.rl_map_distance)
    public RelativeLayout rlMapDistance;
    @Nullable
    @BindView(R2.id.tv_goods_info)
    public TextView tvGoodsInfo;
    @Nullable
    @BindView(R2.id.ll_goods_info)
    public LinearLayout llGoodsInfo;
    @Nullable
    @BindView(R2.id.tv_remark_info)
    public TextView tvRemarkInfo;
    @Nullable
    @BindView(R2.id.iv_remark_info)
    public ImageView ivRemarkInfo;
    @Nullable
    @BindView(R2.id.ll_remark_info)
    public LinearLayout llRemarkInfo;
    @Nullable
    @BindView(R2.id.iv_user_avatar)
    public ImageView ivUserAvatar;
    @Nullable
    @BindView(R2.id.rb_score)
    public RatingBar rbScore;
    @Nullable
    @BindView(R2.id.tv_user_name)
    public TextView tvUserName;
    @Nullable
    @BindView(R2.id.tv_order_num)
    public TextView tvOrderNum;
    @Nullable
    @BindView(R2.id.tv_mobile)
    public TextView tvMobile;
    @Nullable
    @BindView(R2.id.iv_message)
    public ImageView ivMessage;
    @Nullable
    @BindView(R2.id.iv_call)
    public ImageView ivCall;
    @Nullable
    @BindView(R2.id.ll_driver_info)
    public LinearLayout llDriverInfo;
    @Nullable
    @BindView(R2.id.tv_btn_one)
    public TextView tvBtnOne;
    @Nullable
    @BindView(R2.id.tv_btn_two)
    public TextView tvBtnTwo;
    @Nullable
    @BindView(R2.id.ll_btn_two)
    public LinearLayout llBtnTwo;
    @Nullable
    @BindView(R2.id.tv_btn_two_right)
    public TextView tvBtnTwoRight;

    /**
     * 订单详情UI
     */
    @Nullable
    @BindView(R2.id.iv_step1)
    ImageView ivStep1;
    @Nullable
    @BindView(R2.id.tv_step1)
    TextView tvStep1;
    @Nullable
    @BindView(R2.id.iv_step2)
    ImageView ivStep2;
    @Nullable
    @BindView(R2.id.tv_step2)
    TextView tvStep2;
    @Nullable
    @BindView(R2.id.iv_step3)
    ImageView ivStep3;
    @Nullable
    @BindView(R2.id.tv_step3)
    TextView tvStep3;
    @Nullable
    @BindView(R2.id.iv_step4)
    ImageView ivStep4;
    @Nullable
    @BindView(R2.id.tv_step4)
    TextView tvStep4;
    @Nullable
    @BindView(R2.id.ll_order_press)
    LinearLayout llOrderPress;

    //金额相关
    @Nullable
    @BindView(R2.id.tv_freight)
    TextView tvFreight;
    @Nullable
    @BindView(R2.id.tv_trusteeship_freight)
    public TextView tvTrusteeshipFreight;
    @Nullable
    @BindView(R2.id.tv_trusteeship_deposit)
    public TextView tvTrusteeshipDeposit;
    @Nullable
    @BindView(R2.id.tv_unfold_money)
    TextView tvUnfoldMoney;
    @Nullable
    @BindView(R2.id.iv_unfold_money)
    ImageView ivUnfoldMoney;
    @Nullable
    @BindView(R2.id.ll_unfold_money)
    LinearLayout llUnfoldMoney;
    @Nullable
    @BindView(R2.id.tv_payment_put)
    TextView tvPaymentPut;
    @Nullable
    @BindView(R2.id.tv_state_put)
    TextView tvStatePut;
    @Nullable
    @BindView(R2.id.tv_payment_arrive)
    TextView tvPaymentArrive;
    @Nullable
    @BindView(R2.id.tv_state_arrive)
    TextView tvStateArrive;
    @Nullable
    @BindView(R2.id.tv_payment_return)
    TextView tvPaymentReturn;
    @Nullable
    @BindView(R2.id.tv_state_return)
    TextView tvStateReturn;
    @Nullable
    @BindView(R2.id.ll_payment_return)
    LinearLayout llPaymentReturn;
    @BindView(R2.id.ll_payment_arrive)
    LinearLayout llPaymentArrive;
    @BindView(R2.id.ll_payment_put)
    LinearLayout llPaymentPut;
    @Nullable
    @BindView(R2.id.tv_deposit)
    TextView tvDeposit;
    @Nullable
    @BindView(R2.id.tv_state_deposit)
    public TextView tvStateDeposit;
    @Nullable
    @BindView(R2.id.tv_show_deposit)
    public TextView tvShowDeposit;
    @Nullable
    @BindView(R2.id.tv_arrived_account)
    public TextView tvArrivedAccount;

    // 收起 展开
    @Nullable
    @BindView(R2.id.tv_unfold)
    TextView tvUnfold;
    @Nullable
    @BindView(R2.id.iv_unfold)
    ImageView ivUnfold;
    @Nullable
    @BindView(R2.id.ll_unfold_content)
    LinearLayout llUnfoldContent;
    @Nullable
    @BindView(R2.id.ll_hidden_money)
    LinearLayout llHiddenMoney;

    @Nullable
    @BindView(R2.id.sv_content)
    public MyScrollView svContent;
    @Nullable
    @BindView(R2.id.ll_deposit)
    LinearLayout llDeposit;
    @Nullable
    @BindView(R2.id.ll_quote)
    public LinearLayout llQuote;
    @Nullable
    @BindView(R2.id.ll_waite_pay_deposit)
    public LinearLayout llWaitPayDeposit;
    @Nullable
    @BindView(R2.id.tv_quote)
    public TextView tvQuote;
    @Nullable
    @BindView(R2.id.tv_waite_pay_deposit)
    public TextView tvWaitPayDeposit;
    @Nullable
    @BindView(R2.id.ll_content)
    public LinearLayout llContent;

    @Nullable
    @BindView(R2.id.srf_scroll)
    public SwipeRefreshLayout mSwipeRefreshLayout;

    @Nullable
    @BindView(R2.id.ll_view_already)
    public LinearLayout llViewAlready;
    @Nullable
    @BindView(R2.id.ll_float)
    public LinearLayout llFloat;
    @Nullable
    @BindView(R2.id.ll_page_title)
    public LinearLayout llPageTitle;

    public int mDataSource = 0;
    private boolean isUnfold = false;// 是否展开 货源信息
    private boolean isUnfoldMoney = false;// 是否展开 运费信息

    public String[] popMenuTexts; // 显示popupWindow时的文本显示
    private MenuPopupWindow mMenuPopupWindow; // 菜单弹框

    public GoodsInfoResponse mGoodsInfo; // 货源详情
    public OrderInfoResponse mOrderInfo; // 订单详情

    @Override
    protected void initPresenter() {
        mPresenter = new OrderDetailBasePresenter(this, mContext, new OrderDetailCommand(OrderDetailCommandApi.class,
                this));
    }


    @Override
    protected int getLayoutResId() {
        // 外部获取，数据来源是订单详情，还是货源详情
        OrderExtra extra = getIntentExtra(OrderExtra.getExtraName());
        mDataSource = extra.getDataSource();
        if (mDataSource == OrderExtra.GOODS_DETAIL) {
            // 由于去除了Style中WindowBackground值，并且货源详情初始为内容布局不可见。会造成进入页面内容布局位置为黑色。
            // 设置窗口跟布局背景颜色为common_background。
            getWindow().getDecorView().setBackgroundColor(ResourceUtils.getColor(R.color.common_background));
            return R.layout.activity_common_goods_detail;
        } else {
            return R.layout.activity_common_order_detail;
        }
    }

    /**
     * 区分货源详情，订单详情
     */
    @Override
    protected void initTitle() {
        if (mDataSource == OrderExtra.GOODS_DETAIL) {
            getMyTitleBar().setMode(MyTitleBar.Mode.BACK_TITLE)
                    .setTitleText(R.string.goods_detail);
        } else {
            getMyTitleBar().setMode(MyTitleBar.Mode.BACK_TITLE)
                    .setTitleText(R.string.order_detail);
        }
    }


    /**
     * 初始化菜单弹窗
     */
    private void initPopMenu() {
        mMenuPopupWindow = new MenuPopupWindow(this);
        mMenuPopupWindow.setOnPopMenuClickListener(new MenuPopupWindow.OnPopMenuClickListener() {
            @Override
            public void onMenuClick(View view) {
                TextView textView = (TextView) view;
                onRightMenuClick(textView.getText().toString().trim());
            }
        });
    }

    /**
     * 设置顶部菜单
     * 只有1,2,3 三种情况
     * 0 为隐藏右边
     */
    public void setTitleMenu(String[] titleText) {
        if (titleText == null || titleText.length == 0 || titleText.length > 3) {
            setMenu();
            return;
        }
        if (titleText.length == 1) {
            setMenuText(titleText[0]);
        } else {
            setMenuImage();
            // 复制数组
            popMenuTexts = new String[titleText.length];
            System.arraycopy(titleText, 0, popMenuTexts, 0, titleText.length);
        }
    }

    /**
     * 设置右边菜单为多个菜单
     */
    private void setMenuImage() {
        getMyTitleBar()
                .setMode(MyTitleBar.Mode.BACK_TITLE_RIMG)
                .setRightImg(CMemoryData.isDriver() ? R.drawable.iv_icon_menu_driver : R.drawable
                        .iv_icon_menu_shipper);
    }

    /**
     * 设置右边按钮为单个菜单
     */
    private void setMenuText(String text) {
        getMyTitleBar()
                .setMode(MyTitleBar.Mode.BACK_TITLE_RTEXT)
                .setRightText(text);
    }

    /**
     * 色或者右边无文本，无图标
     */
    private void setMenu() {
        getMyTitleBar()
                .setMode(MyTitleBar.Mode.BACK_TITLE);
    }

    /**
     * 货源详情共有显示
     */
    @Override
    public void showGoodsInfo(GoodsInfoResponse data) {
        if (data == null) {
            return;
        }
        llContent.setVisibility(View.VISIBLE);
        mGoodsInfo = data;
        // 如果货源变成订单，就关闭页面
        if (data.getGoods_status() == GoodsState.GOODS_STATUS_CREATE_ORDER) {
            finishPage();
        }

        // 顶部提示
        String pushNum = getString(R.string.push_num);
        setTopMessage(String.format(pushNum, data.getGoods_push_num()));
        // 订单号
        setOrderId(data.getGoods_no(), data.getUpdate_time());
        // 发货人信息
        setSenderInfo(data.getSender_info());
        // 收货人信息
        setReceiverInfo(data.getReceiver_info());
        // 货源信息
        setGoodsInfo(data.getOther_remark(), data.getRemark(), data.getRemark_img_key(), data.getRemark_img_url());

        // 设置右边菜单按钮显示
        setRightMenu(data.getGoods_status());
        // 底部按钮显示
        setBottomBtnText(data.getGoods_status()); // 货源状态
        setGoodsInfo(data);//子类中显示
    }

    /**
     * 订单详情共有显示
     */
    private int oldOrderState = -1; // 记录退款前的订单状态，记录提货前状态，在司机同意退款后关闭页面
    @Override
    public void showOrderInfo(OrderInfoResponse data) {
        if (data == null) {
            return;
        }
        llContent.setVisibility(View.VISIBLE);
        mOrderInfo = data;

        // 如果订单退款成功后，关闭页面，进入货源详情
        // 只有司机版 提货前退款的订单看不到。提货后可以看到，在已签收，显示已退款
        if (CMemoryData.isDriver() &&
                data.getOrder_status() == OrderState.ORDER_STATUS_REFUND &&
                oldOrderState > 0 &&
                oldOrderState < OrderState.ORDER_DELIVERING ) {
            Toaster.showLongToast("很可惜，该订单已成交/撤销");
            finishPage();
        }
        oldOrderState = mOrderInfo.getOrder_status();

        tvBtnTwoRight.setVisibility(View.GONE);
        // 隐藏刷新按钮
        tvRefreshOrder.setVisibility(View.GONE);
        // 顶部提示
        setTopMessage(StateTopMsgProvider.getInstance().getTopMessage(data.getOrder_status(), data));
        // 订单号
        setOrderId(data.getOrder_no(), data.getUpdate_time());
        // 发货人信息
        setSenderInfo(data.getSender_info());
        // 收货人信息
        setReceiverInfo(data.getReceiver_info());
        // 货源信息
        setGoodsInfo(data.getOther_remark(), data.getRemark(), data.getRemark_img_key(), data.getRemark_img_url());
        // 订单状态
        setStateBar(data.getOrder_status());
        // 运费信息
        setFreightInfo(data.getFreight(), data.getAgency_fee(), data.getAgency_status());
        // 底部按钮显示
        setBottomBtnText(data.getOrder_status());
        // 设置右边菜单按钮显示
        setRightMenu(data.getOrder_status());

        setOrderInfo(data);//子类中显示
    }


    /**
     * 抽象方法，在子类中使用
     * 设置特有的视图显示
     * 货源和订单详情的显示
     */
    public abstract void setGoodsInfo(GoodsInfoResponse data);

    public abstract void setOrderInfo(OrderInfoResponse data);

    /**
     * 设置底部按钮文字显示
     * 在子类中设置不同版本的显示
     */
    public abstract void setBottomBtnText(int orderState);

    /**
     * 底部按钮点击事件
     * 在子类中的不同操作
     */
    public abstract void onOneBtnClick(String btnText);

    public abstract void onTwoBtnClick(String btnText);

    /**
     * 设置右边菜单显示
     */
    public abstract void setRightMenu(int orderState);

    /**
     * 地图点击
     */
    public abstract void mapClick();

    /**
     * 右边按钮的点击事件处理
     * topMenuTexts = {"撤销订单", "分享货源", "查看合同", "我要退款", "我要投诉", "退款详情"};
     */
    private void onRightMenuClick(String btnText) {
        if (TextUtils.isEmpty(btnText)) {
            return;
        }
        String[] topMenuTexts = StateRightMenuProvider.getInstance().topMenuTexts;
        if (btnText.equals(topMenuTexts[0])) { // 撤销订单
            if (mDataSource == OrderExtra.GOODS_DETAIL) {
                mPresenter.cancelGoods(mGoodsInfo.getGoods_id(), mGoodsInfo.getGoods_version());
            } else {
                mPresenter.cancelGoods(mOrderInfo.getGoods_id(), mOrderInfo.getGoods_version());
            }
        } else if (btnText.equals(topMenuTexts[1])) { // 分享货源
            mPresenter.shareGoods(getGoodsId());
        } else if (btnText.equals(topMenuTexts[2])) { // 查看合同
            String goodsId = "";
            if (mGoodsInfo != null) {
                goodsId = mGoodsInfo.getGoods_id();
            } else if (mOrderInfo != null) {
                goodsId = mOrderInfo.getGoods_id();
            }
            if (StringUtils.isNotBlank(goodsId)) {
                SkipControllerProtocol.skipCheckProtocol(this, goodsId);
            }
        } else if (btnText.equals(topMenuTexts[3]) | btnText.equals(topMenuTexts[5])) { // 3:我要退款，进我要退款页面时refundInfo
            // 这个参数是空的 // 5:退款详情
            if(mDataSource == OrderExtra.ORDER_DETAIL) {
                SkipControllerRefund.skipToRefund(this,
                        mOrderInfo.getOrder_id(), mOrderInfo.getOrder_status() + "", mOrderInfo.getOrder_version(),
                        CMemoryData.getUserId(),
                        mOrderInfo.getFreight(),
                        mOrderInfo.getRefund_info());
            }else {
                SkipControllerRefund.skipToRefund(this,
                        mGoodsInfo.getOrder_info().getOrder_id(),
                        mGoodsInfo.getOrder_info().getOrder_status() + "",
                        mGoodsInfo.getOrder_info().getOrder_version(),
                        CMemoryData.getUserId(),
                        mGoodsInfo.getFreight(),
                        mGoodsInfo.getRefund_info());
            }
        } else if (btnText.equals(topMenuTexts[4])) { // 我要投诉
            ComplainActivity.turnToComplainActiivty(this, mOrderInfo.getOrder_id(), mOrderInfo.getOrder_version());
        }
    }

    /**
     * 顶部提示
     */
    public void setTopMessage(String message) {
        // 顶部提示
        tvOrderMsg.setText(message);
    }

    /**
     * 设置订单号，更新时间
     */
    private void setOrderId(String orderNo, String time) {
        String idStr = getString(R.string.order_id);
        Logger.d("订单号 " + orderNo);
        tvOrderId.setText(idStr + orderNo);
        tvOrderTime.setText(TimeUtils.showUpdateTime(time));
    }

    /**
     * 货源详情，备注信息
     */
    private void setGoodsInfo(String goods, String remark, String key, String url) {
        if (StringUtils.isEmpty(remark) && (TextUtils.isEmpty(key) || TextUtils.isEmpty(url))) {
            llRemarkInfo.setVisibility(View.GONE);
        } else {
            llRemarkInfo.setVisibility(View.VISIBLE);
        }

        // 货源详情
        tvGoodsInfo.setText(goods);
        // 备注信息
        if (StringUtils.isEmpty(remark)) {
            tvRemarkInfo.setVisibility(View.GONE);
        } else {
            tvRemarkInfo.setVisibility(View.VISIBLE);
            tvRemarkInfo.setText(remark);
        }
        // 备注图片
        if (TextUtils.isEmpty(key) || TextUtils.isEmpty(url)) {
            ivRemarkInfo.setVisibility(View.GONE);
        } else {
            ivRemarkInfo.setVisibility(View.VISIBLE);
            GlideUtils.loaderImageRound(this,
                    url,
                    key,
                    R.drawable.shape_avatar_loading,
                    R.drawable.shape_avatar_loading,
                    ivRemarkInfo,
                    4);
        }
    }

    /**
     * 设置发货人信息
     */
    private void setSenderInfo(OrderUserInfo sender) {
        if (sender != null) {
            tvStartCityName.setText(sender.getCity());
            tvStartCityAddress.setText(sender.getDetailed_address());
            tvNamePhone.setText(sender.getName() + " " + sender.getMobile());
        }
    }

    /**
     * 设置收货人信息
     */
    public void setReceiverInfo(OrderUserInfo receiver) {
        if (receiver != null) {
            tvEndCityName.setText(receiver.getCity());
            tvEndCityAddress.setText(receiver.getDetailed_address());
            tvNamePhoneEnd.setText(receiver.getName() + " " + receiver.getMobile());
        }
    }

    /**
     * 用户信息，司机或者货主
     */
    public void setUserInfo(String name, String key, String url, String orderNum, String otherInfo, float scoreLevel) {
        tvUserName.setText(name);
        String orderNumStr = getString(R.string.order_num_str);
        tvOrderNum.setText(String.format(orderNumStr, orderNum));
        if(StringUtils.isNotBlank(url) && StringUtils.isNotBlank(key)) {
            // 用户头像
            GlideUtils.loaderImageRound(this,
                    url,
                    key,
                    R.drawable.shape_avatar_loading,
                    R.drawable.shape_avatar_loading,
                    ivUserAvatar,
                    4);
        }

        tvMobile.setText(otherInfo);
        rbScore.setRating(scoreLevel);
    }

    /**
     * 设置展开，收起
     */
    public void setUnfold() {
        if (isUnfold) { //展开状态
            ivUnfold.setImageResource(R.drawable.iv_icon_arrow_up);
            tvUnfold.setText(R.string.pack_up);
            llUnfoldContent.setVisibility(View.VISIBLE);
            svContent.fullScroll(ScrollView.FOCUS_DOWN);//滚动到底部
        } else { // 收起状态
            ivUnfold.setImageResource(R.drawable.iv_icon_arrow_bottom);
            tvUnfold.setText(R.string.unfold);
            llUnfoldContent.setVisibility(View.GONE);
        }
    }

    /**
     * 设置展开，收起 运费信息
     */
    public void setUnfoldMoney() {
        if (isUnfoldMoney) { //展开状态
            ivUnfoldMoney.setImageResource(R.drawable.iv_icon_arrow_up);
            tvUnfoldMoney.setText(R.string.pack_up_detail);
            llHiddenMoney.setVisibility(View.VISIBLE);
        } else { // 收起状态
            ivUnfoldMoney.setImageResource(R.drawable.iv_icon_arrow_bottom);
            tvUnfoldMoney.setText(R.string.unfold_detail);
            llHiddenMoney.setVisibility(View.GONE);
        }
    }


    /**
     * 设置状态进度显示
     */
    private void setStateBar(int orderState) {
        setStepTextColorNormal(tvStep1);
        setStepTextColorNormal(tvStep2);
        setStepTextColorNormal(tvStep3);
        setStepTextColorNormal(tvStep4);
        switch (orderState) {
            case OrderState.ORDER_CANCEL: // 取消
                break;
            case OrderState.ORDER_CONFIRM: // 待承运
                ivStep1.setImageResource(R.drawable.iv_order_state_left_gray);
                ivStep2.setImageResource(R.drawable.iv_order_state_middle_gray);
                ivStep3.setImageResource(R.drawable.iv_order_state_middle_gray);
                ivStep4.setImageResource(R.drawable.iv_order_state_right_gray);
                break;
            case OrderState.ORDER_UNPAID_DEPOSIT: // 待付定金 非订单状态，报价中
                break;
            case OrderState.ORDER_UNPAID_FREIGHT: // 待付运费
                ivStep1.setImageResource(R.drawable.iv_order_state_left_check);
                ivStep2.setImageResource(R.drawable.iv_order_state_middle_gray);
                ivStep3.setImageResource(R.drawable.iv_order_state_middle_gray);
                ivStep4.setImageResource(R.drawable.iv_order_state_right_gray);
                setStepTextColorCheck(tvStep1);
                break;
            case OrderState.ORDER_PICK_UP_GOODS: // 待提货
                ivStep1.setImageResource(R.drawable.iv_order_state_left_yellow);
                ivStep2.setImageResource(R.drawable.iv_order_state_middle_check);
                ivStep3.setImageResource(R.drawable.iv_order_state_middle_gray);
                ivStep4.setImageResource(R.drawable.iv_order_state_right_gray);
                setStepTextColorCheck(tvStep2);
                break;
            case OrderState.ORDER_DELIVERING: // 配送中，承运中
                ivStep1.setImageResource(R.drawable.iv_order_state_left_yellow);
                ivStep2.setImageResource(R.drawable.iv_order_state_middle_yellow);
                ivStep3.setImageResource(R.drawable.iv_order_state_middle_check);
                ivStep4.setImageResource(R.drawable.iv_order_state_right_gray);
                setStepTextColorCheck(tvStep3);
                break;
            case OrderState.ORDER_STATUS_IS_THE_CARRIER: // 已承运/待评价
            case OrderState.ORDER_STATUS_DRIVERUNEVALUATE:
            case OrderState.ORDER_STATUS_OWNERUNEVALUATE:
            case OrderState.ORDER_STATUS_EVALUATE:
                ivStep1.setImageResource(R.drawable.iv_order_state_left_yellow);
                ivStep2.setImageResource(R.drawable.iv_order_state_middle_yellow);
                ivStep3.setImageResource(R.drawable.iv_order_state_middle_yellow);
                ivStep4.setImageResource(R.drawable.iv_order_state_right_check);
                setStepTextColorCheck(tvStep4);
                break;
            default:
                break;
        }

    }

    /**
     * 订单状态默认字体颜色
     */
    private void setStepTextColorNormal(TextView tv) {
        tv.setTextColor(getResources().getColor(R.color.text_color_666666));
    }

    /**
     * 订单状态选中字体颜色
     */
    private void setStepTextColorCheck(TextView tv) {
        tv.setTextColor(getResources().getColor(R.color.text_color_FFB000));
    }

    /**
     * 设置运费信息
     */
    private void setFreightInfo(FreightInfo freightInfo, double agencyFee, int agencyState) {
        // 0.不托管 1.未托管 2.未支付 3.已支付
        // 2.未支付 表示 钱已经付给平台 不是0就是已托管
        if (freightInfo.getFreight_fee_status() > FreightState.TRUSTEESHIP_NO) {
            tvTrusteeshipFreight.setVisibility(View.VISIBLE);
        } else {
            tvTrusteeshipFreight.setVisibility(View.GONE);
        }
        // 运费
        setFreightMoney(tvFreight, freightInfo.getFreight_fee());
        setFreightMoney(tvPaymentPut, freightInfo.getAhead_fee());
        setFreightMoney(tvPaymentArrive, freightInfo.getDelivery_fee());
        setFreightMoney(tvPaymentReturn, freightInfo.getBack_fee());
        if (agencyFee == 0) {
            llDeposit.setVisibility(View.GONE);
        } else {
            setFreightMoney(tvDeposit, agencyFee);
        }
        // 状态
        setFreightState(llPaymentPut, tvStatePut, freightInfo.getAhead_fee_status(), freightInfo.getAhead_fee());
        setFreightState(llPaymentArrive, tvStateArrive, freightInfo.getDelivery_fee_status(), freightInfo
                .getDelivery_fee());

        setFreightState(llPaymentReturn, tvStateReturn, freightInfo.getBack_fee_status(), freightInfo.getBack_fee());
        setFreightState(llDeposit, tvStateDeposit, agencyState, agencyFee);

    }

    /**
     * 设置运费
     */
    private void setFreightMoney(TextView tv, double money) {
        tv.setText("￥" + money);
    }

    /**
     * 设置运费金额状态, 金额要大于0才显示状态
     * FREIGHTFEE_STATUS_NOTHOSTING	0	0.不托管
     * FREIGHTFEE_STATUS_YETHOSTING	1	1.未托管
     * FREIGHTFEE_STATUS_NON_PAYMENT	2	2.未支付
     * FREIGHTFEE_STATUS_HAVA_TO_PEY	3	3.已支付
     */
    private void setFreightState(LinearLayout layout, TextView tv, int state, double money) {
        int res = StateTopMsgProvider.getInstance().getFreightState(state);
        Logger.d("运费 " + state + " " + money);
        // 金额大于0 则显示
        if (layout != null) {
            if (money > 0) {
                layout.setVisibility(View.VISIBLE);
            } else {
                layout.setVisibility(View.GONE);
            }
        }
        // 状态大于0 则显示状态
        // 回单付不现实状态
        if (res != -1 && tv.getId() != tvStateReturn.getId()) {
            tv.setVisibility(View.VISIBLE);
            tv.setText(res);
        } else {
            tv.setVisibility(View.GONE);
        }

    }


    /**
     * 刷新订单
     */
    @OnClick(R2.id.tv_refresh_order)
    public void refreshClick() {
        if (StringUtils.isNotBlank(mGoodsInfo.getGoods_version())) {
            mPresenter.refreshGoods(mGoodsInfo.getGoods_id(), mGoodsInfo.getGoods_version());
        }
    }

    /**
     * 左边按钮点击事件
     */
    @OnClick(R2.id.tv_btn_one)
    public void btnOneClick(View view) {
        TextView textView = (TextView) view;
        onOneBtnClick(textView.getText().toString().trim());
    }

    /**
     * 第二个按钮点击事件
     * 当只有一个按钮时，时这个按钮
     */
    @OnClick(R2.id.ll_btn_two)
    public void btnTwoClick(View view) {
        onTwoBtnClick(tvBtnTwo.getText().toString().trim());
    }

    /**
     * 展开收起点击事件
     */
    @OnClick(R2.id.ll_unfold)
    public void unfoldClick() {
        isUnfold = !isUnfold;
        setUnfold();
    }

    /**
     * 距离点击事件
     */
    @OnClick(R2.id.rl_map_distance)
    public void distanceClick() {
        mapClick();
    }

    /**
     * 展开收起运费信息点击事件
     */
    @OnClick(R2.id.ll_unfold_money)
    public void unfoldMoneyClick() {
        isUnfoldMoney = !isUnfoldMoney;
        setUnfoldMoney();
    }

    /**
     * 点击查看大图 备注图片
     */
    @OnClick(R2.id.iv_remark_info)
    public void remarkImageClick() {
        String key, url;
        if (mDataSource == OrderExtra.ORDER_DETAIL) {
            key = mOrderInfo.getRemark_img_key();
            url = mOrderInfo.getRemark_img_url();
        } else {
            key = mGoodsInfo.getRemark_img_key();
            url = mGoodsInfo.getRemark_img_url();
        }
        if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(url)) {
            ShowBigImageActivity.showImage(this, ivRemarkInfo, key, url);
        }

    }

    /**
     * 拨打电话点击
     */
    @OnClick(R2.id.iv_call)
    public void callClick(final View view) {
        CheckUserStatusUtils.isRealNameAuthentication(this, new CheckUserStatusUtils.OnJudgeResultListener() {
            @Override
            public void onSucceed() {
                String name, mobile;
                int flag;
                if (CMemoryData.isDriver()) { // 司机版
                    flag = 0;
                    OwnerInfo ownerInfo;
                    if (mDataSource == OrderExtra.GOODS_DETAIL) {
                        ownerInfo = mGoodsInfo.getOwner_info();
                    } else {
                        ownerInfo = mOrderInfo.getOwner_info();
                    }
                    if (ownerInfo != null) {
                        name = ownerInfo.getOwner_name();
                        mobile = ownerInfo.getOwner_mobile();
                        if(mDataSource == OrderExtra.GOODS_DETAIL) {
                            // 拨打电话 上传通话记录
                            new CallPhoneUtils()
                                    .showCallDialogWithAdvUpload(OrderDetailBaseActivity.this,
                                            view, name, mobile, flag,
                                            mGoodsInfo.getGoods_id(),
                                            mGoodsInfo.getGoods_status()+""
                                            , new CallPhoneUtils.CallPhoneSucceedListener() {
                                                @Override
                                                public void isSucceed(boolean isSucceed) {

                                                }
                                            });
                            return;
                        }
                    } else {
                        Toaster.showLongToast("货主信息为空");
                        return;
                    }
                } else { // 货主版 货源不能打电话
                    if (mOrderInfo.getDriver_info() != null) {
                        flag = 2;
                        name = mOrderInfo.getDriver_info().getDriver_name();
                        mobile = mOrderInfo.getDriver_info().getDriver_mobile();
                    } else {
                        Toaster.showLongToast("司机信息为空");
                        return;
                    }
                }
                // 拨打电话
                new CallPhoneUtils().showCallDialogWithAdvNotUpload(OrderDetailBaseActivity.this, view, name,  mobile, flag);
            }
        });
    }

    /**
     * 聊天点击
     */
    @OnClick(R2.id.iv_message)
    public void messageClick() {
        if (mDataSource == OrderExtra.ORDER_DETAIL) {
            CommonProvider.getInstance().getJumpChatPageProvider().jumpChatPage(this, mPresenter.getImUserBean(),
                    mOrderInfo);
        } else {
            if (CMemoryData.isDriver()) {
                // 司机进入货源详情后，此时货主撤销订单|与他人成交，订单详情未刷新，点击聊天图标按钮。
                mPresenter.checkChat(mGoodsInfo.getGoods_id(), mGoodsInfo.getGoods_version());
            } else {
                if (mGoodsInfo.getGoods_status() == GoodsState.GOODS_STATUS_NEW) {
                    CommonProvider.getInstance().getJumpChatPageProvider().jumpChatPage(this, mPresenter
                            .getImUserBean(), mGoodsInfo);
                } else {
                    CommonProvider.getInstance().getJumpChatPageProvider().jumpChatPage(this, mPresenter
                            .getImUserBean());
                }
            }
        }
    }



    /**
     * 头像点击，进入个人信息页
     */
    @OnClick(R2.id.iv_user_avatar)
    public void avatarClick() {
        if (CMemoryData.isDriver()) {
            String mobile;
            if (mDataSource == OrderExtra.ORDER_DETAIL) {
                mobile = mOrderInfo.getOwner_info().getOwner_mobile();
                CommonProvider.getInstance().getJumpDriverProvider().jumpUserInfo(this, mobile);
            } else {
                if (mGoodsInfo.getOwner_info().getIs_anonymous()) {
                    Toaster.showLongToast("对方设置了匿名，无法查看主页");
                } else {
                    mobile = mGoodsInfo.getOwner_info().getOwner_mobile();
                    CommonProvider.getInstance().getJumpDriverProvider().jumpUserInfo(this, mobile);
                }
            }
        } else {
            if (mOrderInfo != null && mOrderInfo.getDriver_info() != null &&
                    StringUtils.isNotBlank(mOrderInfo.getDriver_info().getDriver_mobile())) {
                CommonProvider.getInstance().getJumpShipperProvider().jumpUserInfo(this,
                        mOrderInfo.getDriver_info().getDriver_mobile());
            }
        }
    }


    /**
     * 右边按钮，点击 ，菜单
     */
    @Override
    protected void onClickRightImg(View view) {
        super.onClickRightImg(view);
        if (mMenuPopupWindow == null) {
            initPopMenu();
        }
        mMenuPopupWindow.showPopupWindow(view, popMenuTexts);
    }

    /**
     * 右边文字，点击
     */
    @Override
    protected void onClickRightText(View view) {
        super.onClickRightText(view);
        TextView textView = (TextView) view;
        onRightMenuClick(textView.getText().toString().trim());
    }

    /**
     * 得到分享的货源id
     */
    public String getGoodsId() {
        if (mDataSource == OrderExtra.GOODS_DETAIL) {
            return mGoodsInfo.getGoods_id();
        } else {
            return mOrderInfo.getGoods_id();
        }
    }

    /**
     * 分享图片
     */
    @Override
    public void shareImage(final String path) {
        new ShareDialog(this)
                .setShareClickListener(new ShareDialog.ShareClickListener() {
                    @Override
                    public void weChatClick() {
                        ShareHelper.shareImage(OrderDetailBaseActivity.this, Wechat.NAME, path, shareComplete);
                    }

                    @Override
                    public void circleClick() {
                        ShareHelper.shareImage(OrderDetailBaseActivity.this, WechatMoments.NAME, path, shareComplete);
                    }
                }).show();
    }

    // 分享的回调
    ShareHelper.OnShareComplete shareComplete = new ShareHelper.OnShareComplete() {
        @Override
        public void shareComple(String s) {
            mPresenter.recordShareGoods(getGoodsId(), ShareGoodsParams.SUCCESS);
        }

        @Override
        public void shareError() {
            mPresenter.recordShareGoods(getGoodsId(), ShareGoodsParams.FAIL);
        }
    };

    @Override
    public void showGif() {
        if(ivGif != null) {
            Logger.d("gif显示");
            ivGif.setVisibility(View.VISIBLE);
            Glide.with(this).load(R.drawable.money_down).asGif()
                    .listener(new RequestListener<Integer, GifDrawable>() {
                        @Override
                        public boolean onException(Exception e, Integer model, Target<GifDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GifDrawable resource, Integer model, Target<GifDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            hiddenGifView();
                            return false;
                        }
                    }).into(ivGif);
        }else {
            Toaster.showLongToast("gif空的");
        }

    }

    /**
     * 隐藏gif
     */
    private void hiddenGifView(){
        DelayUtils.delay(1000 * 3, new DelayUtils.OnListener() {
                @Override
                public void onListener() {
                    ivGif.setVisibility(View.GONE);
                }
            });
    }
}
