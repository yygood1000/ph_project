package com.topjet.shipper.deliver.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.topjet.common.base.busManger.Subscribe;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.common.modle.event.AreaSelectedData;
import com.topjet.common.common.modle.event.TruckTypeLengthSelectedData;
import com.topjet.common.common.modle.extra.TabIndex;
import com.topjet.common.common.view.dialog.CitySelectPopupWindow;
import com.topjet.common.common.view.dialog.TimeWheelPop;
import com.topjet.common.common.view.dialog.TruckLengthAndTypePopupWindow;
import com.topjet.common.order_detail.modle.extra.CityAndLocationExtra;
import com.topjet.common.order_detail.modle.extra.ClickAgreeBtnEvent;
import com.topjet.common.order_detail.presenter.SkipControllerProtocol;
import com.topjet.common.utils.DoubleClickCheckUtil;
import com.topjet.common.utils.KeyboardUtil;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.NumberFormatUtils;
import com.topjet.common.utils.ResourceUtils;
import com.topjet.common.utils.StringUtils;
import com.topjet.common.utils.TimeUtils;
import com.topjet.common.utils.Toaster;
import com.topjet.common.widget.MyTitleBar;
import com.topjet.shipper.MainActivity;
import com.topjet.shipper.R;
import com.topjet.shipper.deliver.modle.bean.UnitAndState;
import com.topjet.shipper.deliver.modle.extra.OtherInfoExtra;
import com.topjet.shipper.deliver.modle.params.OwnerGoodsParams;
import com.topjet.shipper.deliver.presenter.DeliverGoodsPresenter;
import com.topjet.shipper.deliver.view.adapter.UnitAdapter;
import com.topjet.shipper.deliver.view.dialog.PayWayPopup;
import com.topjet.shipper.order.modle.response.AddGoodsReponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;


/**
 * 发货
 * 货主版
 * <p>
 * 点击提交按钮时，有几种情况，不跳是否托管运费TrusteeshipActivity
 * 分别是 1.发布新定向订单2.修改定向订单3.重新找车-给他下单4.发布回单付订单
 * 回单付使用的是  支付方式的文字判断的，代码  tvPayWay.getText().toString().contains("回单")
 * 我也不想用文字判断，但鬼晓得会不会更改支付方式列表的顺序，所以position判断行不通
 * <p>
 * 除此之外，都会跳转到托管运费界面TrusteeshipActivity
 * <p>
 * 进入界面可能会传入一些不会更改的信息，比如司机id，车辆id
 * 调用setPersistenceData即可，只可调用一次，多次调用不会覆盖
 */
public class DeliverGoodsActivity extends MvpActivity<DeliverGoodsPresenter> implements DeliverGoodsView,
        PayWayPopup.OnWheelViewClick,
        BaseQuickAdapter.OnItemClickListener {

    /* ==========发货地址相关控件========== */
    @BindView(R.id.rl_deliver)
    RelativeLayout rlDeliver;
    // 发货地址
    @BindView(R.id.tv_deliver_address)
    TextView tvDeliverAddress;
    // 发货人名
    @BindView(R.id.tv_deliver_name)
    TextView tvDeliverName;
    // 发货人电话
    @BindView(R.id.tv_deliver_phone)
    TextView tvDeliverPhone;
    @BindView(R.id.iv_start)
    ImageView ivStart;
    @BindView(R.id.tv_start)
    TextView tvStart;
    @BindView(R.id.v_start)
    View vStart;
    @BindView(R.id.tv_to_deliver)
    TextView tvToDeliver;
    /* ==========发货地址相关控件========== */
    /* ==========收货地址相关控件========== */
    @BindView(R.id.rl_receipt)
    RelativeLayout rlReceipt;
    // 收货地址
    @BindView(R.id.tv_receipt_address)
    TextView tvReceiptAddress;
    // 收货人名
    @BindView(R.id.tv_receipt_name)
    TextView tvReceiptName;
    // 收货人电话
    @BindView(R.id.tv_receipt_phone)
    TextView tvReceiptPhone;
    @BindView(R.id.iv_end_point)
    ImageView ivEndPoint;
    @BindView(R.id.tv_end_point)
    TextView tvEndPoint;
    @BindView(R.id.v_end)
    View vEnd;
    @BindView(R.id.tv_to_receipt)
    TextView tvToReceipt;
    /* ==========收货地址相关控件========== */
    /* ==========货物数量相关控件========== */
    @BindView(R.id.ll_goods_number)
    LinearLayout llGoodsNumber;
    // 固定数量
    @BindView(R.id.v_fixed_select)
    View vFixedSelect;
    // 范围数量
    @BindView(R.id.v_range_select)
    View vRangeSelect;
    // 货物数量-固定
    @BindView(R.id.et_goods_quantity)
    EditText etGoodsQuantity;
    // 货物数量-范围
    @BindView(R.id.ll_range)
    LinearLayout llRange;
    // 最小值
    @BindView(R.id.et_min_amount_trend)
    EditText etMinAmountTrend;
    // 最大值
    @BindView(R.id.et_max_amount_trend)
    EditText etMaxAmountTrend;
    // 单位，点击后下方弹出gridview
    @BindView(R.id.tv_unit)
    TextView tvUnit;
    // 单位选择列表
    @BindView(R.id.gv_unit)
    RecyclerView gvUnit;
    // 固定值标签
    @BindView(R.id.tv_fixed_select)
    TextView tvFixedSelect;
    // 范围标签
    @BindView(R.id.tv_range_select)
    TextView tvRangeSelect;
    @BindView(R.id.v_fen)
    View vFen;
    @BindView(R.id.v_gang)
    View vGang;
    @BindView(R.id.iv_unit)
    ImageView ivUnit;
    /* ==========货物数量相关控件========== */
    /* ==========车型车长相关控件========== */
    // 车型车长布局
    @BindView(R.id.rl_type_length)
    RelativeLayout rlTypeLength;
    @BindView(R.id.tv_type_length_title)
    TextView tvTypeLengthTitle;
    @BindView(R.id.tv_type_length)
    TextView tvTypeLength;
    @BindView(R.id.iv_type_length)
    ImageView ivTypeLength;
    /* ==========车型车长相关控件========== */
    /* ==========费用相关控件========== */
    // 支付方式
    @BindView(R.id.rl_pay_way)
    RelativeLayout rlPayWay;
    @BindView(R.id.tv_pay_way)
    TextView tvPayWay;
    // 提货付
    @BindView(R.id.rl_ahead)
    RelativeLayout rlAhead;
    @BindView(R.id.et_ahead)
    EditText etAhead;
    @BindView(R.id.cb_ahead)
    CheckBox cbAhead;
    // 到货付
    @BindView(R.id.rl_delivery)
    RelativeLayout rlDelivery;
    @BindView(R.id.et_delivery)
    EditText etDelivery;
    @BindView(R.id.cb_delivery)
    CheckBox cbDelivery;
    // 回单付
    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.et_back)
    EditText etBack;
    // 运费总计
    @BindView(R.id.rl_sum)
    RelativeLayout rlSum;
    @BindView(R.id.tv_sum)
    TextView tvSum;
    /* ==========费用相关控件========== */
    // 提货时间
    @BindView(R.id.tv_take_time)
    TextView tvTakeTime;
    // 距离
    @BindView(R.id.tv_distance)
    TextView tvDistance;
    // 其他信息
    @BindView(R.id.tv_remarks)
    TextView tvRemarks;

    /**
     * 发货地址
     */
    public static final int TYPE_DEPART_ADDRESS = 0;
    /**
     * 收货地址
     */
    public static final int TYPE_DESTINATION_ADDRESS = 1;
    /**
     * 固定单位
     */
    public static final int FLAG_UNITS_EXACT = 1;
    /**
     * 范围单位
     */
    public static final int FLAG_UNITS_RANGE = 2;
    /**
     * 页面请求值（其他信息）
     */
    private final static int REQUEST_CODE_OTHER = 100;
    /**
     * 页面请求值（发货信息补充）
     */
    private final static int REQUEST_CODE_TO_DEPART_ADDRESS = 200;
    /**
     * 页面请求值（收货信息补充）
     */
    private final static int REQUEST_CODE_TO_DESTINATION_ADDRESS = 400;
    /**
     * 页面请求值（常发货源）
     */
    private final static int REQUEST_CODE_OFTEN_GOODS = 500;

    // 当前正在处理的地址类型标记 0 发货地址 1 收货地址
    private int curAddressType = TYPE_DEPART_ADDRESS;
    // 单位类型标记 1 固定单位 2 范围单位
    public static int mCurrentUnit = FLAG_UNITS_EXACT;
    // 单位列表是否可见
    private boolean canSeeGvUnit = false;
    // 单位背景布局是否为白色
    private boolean unitBackGroundIsWhite = false;
    // 选中的单位序号
    private int selectedUnitId = 0;
    //支付方式index
    private int selectedPayWayIndex = 0;
    // 提货时间
    private String loadDate = new Date().getTime() + "";
    // 提货时间 类型   0：具体时间 1：今定今装 2：今定明装 3：随到随走
    private String loadDateType = "3";
    /**
     * 该值用于判断发布货源成功是否跳转协议页面查看协议
     * 0 定向货源  1 回单付新货源
     */
    private int curNetMode = 0;

    // 单位列表适配器
    private UnitAdapter unitAdapter;
    // 提货时间选择弹窗
    private TimeWheelPop timeWheelPop = null;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_deliver_goods;
    }

    @Override
    protected void initTitle() {
        getMyTitleBar().setMode(MyTitleBar.Mode.CANCEL_TITLE_RTEXT).setTitleText(R.string.deliver_goods).setRightText(R
                .string.habit);
    }

    @Override
    protected void initPresenter() {
        mPresenter = new DeliverGoodsPresenter(this, mContext);
    }

    @Override
    public void initView() {
        // 初始化单位适配器
        unitAdapter = new UnitAdapter();
        gvUnit.setLayoutManager(new GridLayoutManager(mContext, 7));
        gvUnit.setAdapter(unitAdapter);
        unitAdapter.setOnItemClickListener(this);
        mPresenter.setUnitData();

        // 设置车型车长的文字变化监听
        setTypeLengthTextChangeListener(tvTypeLength);

        // 设置地址的文字变化监听
        setAddressTextChangeListener(tvReceiptAddress);

        // 设置地址的文字变化监听
        setAddressTextChangeListener(tvDeliverAddress);

        // 设置发货单位的文字变化监听
        setUnitTextChangeListener(etMinAmountTrend);
        setUnitTextChangeListener(etMaxAmountTrend);
        setUnitTextChangeListener(etGoodsQuantity);

        // 设置费用的文字变化监听
        setFeeTextChangeListener(etAhead);
        setFeeTextChangeListener(etDelivery);
        setFeeTextChangeListener(etBack);

        // 初始化支付方式数据
        mPresenter.initPayWayList();

        // 初始化提货时间
        initTimeWheelPop();
    }

    @Override
    protected void initData() {
        // 处理跳转信息
        OwnerGoodsParams params = getIntentExtra(OwnerGoodsParams.getExtraName());
        if (params != null) {
            int inType = params.getInType();
            switch (inType) {
                case OwnerGoodsParams.IN_TYPE_ADD_ASSIGNED:
                    // 发布定向订单
                    getMyTitleBar()
                            .setMode(MyTitleBar.Mode.BACK_TITLE_RTEXT)
                            .setTitleText("给他下单")
                            .setRightText(R.string.habit);
                    // 隐藏支付方式
                    hidePayWay();
                    break;
                case OwnerGoodsParams.IN_TYPE_COPY:
                    // 复制订单，不需要右上角的常发货源。左上角变成返回键
                    getMyTitleBar()
                            .setMode(MyTitleBar.Mode.BACK_TITLE)
                            .setTitleText(R.string.deliver_goods);
                    break;
                case OwnerGoodsParams.IN_TYPE_EDIT:
                    // 修改定向/非定向订单，不需要右上角的常发货源。左上角变成返回键
                    getMyTitleBar()
                            .setMode(MyTitleBar.Mode.BACK_TITLE)
                            .setTitleText(R.string.update_order);
                    break;
                case OwnerGoodsParams.IN_TYPE_EDIT_ASSIGNED:
                    // 修改定向/非定向订单，不需要右上角的常发货源。左上角变成返回键
                    getMyTitleBar()
                            .setMode(MyTitleBar.Mode.BACK_TITLE)
                            .setTitleText(R.string.update_order);
                    // 隐藏支付方式
                    hidePayWay();
                    break;
                case OwnerGoodsParams.IN_TYPE_ADD_ASSIGNED_REFIND_TRUCK:
                    // 重新找车-给他下单，不需要右上角的常发货源。左上角变成返回键
                    getMyTitleBar()
                            .setMode(MyTitleBar.Mode.BACK_TITLE)
                            .setTitleText("给他下单");
                    hidePayWay();
                    break;
            }

            // 设置初始化数据
            mPresenter.setPersistenceData(params);
            // 请求服务器，根据id获取订单数据
            mPresenter.getParamsFromServerById(params);
        }
    }

    /**
     * 发布的是定向订单时,隐藏支付方式，显示回单付、到货付等
     * 隐藏 自动刷新视图
     */
    private void hidePayWay() {
        // 定向货源，需要显示提货付等信息
        rlAhead.setVisibility(View.VISIBLE);
        rlDelivery.setVisibility(View.VISIBLE);
        rlBack.setVisibility(View.VISIBLE);
        rlSum.setVisibility(View.VISIBLE);

        rlPayWay.setVisibility(View.GONE);
        mPresenter.getOtherInfoExtra().setAssigned(true);
    }

    /**
     * 根据参数刷新单位模块界面
     */
    @Override
    public void refreViewsByParams(String quantity_type, String quantity_min, String quantity_max, String unit) {
        if (quantity_type.equals(DeliverGoodsActivity.FLAG_UNITS_EXACT + "")) {
            mCurrentUnit = DeliverGoodsActivity.FLAG_UNITS_EXACT;
            vFixedSelect.performClick();
            etGoodsQuantity.setText(quantity_max);
        } else if (quantity_type.equals(DeliverGoodsActivity.FLAG_UNITS_RANGE + "")) {
            mCurrentUnit = DeliverGoodsActivity.FLAG_UNITS_RANGE;
            vRangeSelect.performClick();
            llRange.setVisibility(View.VISIBLE);
            etMinAmountTrend.setText(quantity_min);
            etMaxAmountTrend.setText(quantity_max);
        }
        tvUnit.setText(unit);
    }

    /**
     * 设置其他货物信息
     */
    public void setOtherInfo(OtherInfoExtra otherInfoExtra) {
        String temp = "";
        String type = otherInfoExtra.getType();
        String pack = otherInfoExtra.getPack();
        String load = otherInfoExtra.getLoad();
        if (StringUtils.isEmpty(type)) {
            type = "";
        }
        if (StringUtils.isEmpty(pack)) {
            pack = "";
        }
        if (StringUtils.isEmpty(load)) {
            load = "";
        }
        temp = StringUtils.appendWithSpace(type, pack, load);
        tvRemarks.setText(temp);
    }

    /**
     * 常发货源
     */
    @Override
    protected void onClickRightText() {
        turnToActivityForResult(OftenGoodsListActivity.class, REQUEST_CODE_OFTEN_GOODS);
    }

    /**
     * 清空数量输入框
     */
    public void clearetAmountText() {
        etMinAmountTrend.setText("");
        etMaxAmountTrend.setText("");
    }

    /**
     * 发货定向货源成功，需要跳转协议页面查看协议
     */
    @Subscribe
    public void onEvent(AddGoodsReponse addGoodsReponse) {
        if (addGoodsReponse.getTag().equalsIgnoreCase(TAG)) {
            if (curNetMode == 0) {
                SkipControllerProtocol.skipDeliverGoods(this, addGoodsReponse.getGoods_id(), TAG);
                Toaster.showLongToast(getResources().getString(R.string.order_success));
            }
            finish();
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == Activity.RESULT_OK) {
            if (REQUEST_CODE_OTHER == requestCode) {     // 从其他信息界面回来
                OtherInfoExtra otherInfoExtra = getIntentExtra(intent, OtherInfoExtra.getExtraName());
                mPresenter.setOtherInfoExtra(otherInfoExtra);

                setOtherInfo(otherInfoExtra);
            } else if (REQUEST_CODE_TO_DEPART_ADDRESS == requestCode) {     // 从发货地址界面回来
                CityAndLocationExtra deliverExtra = getIntentExtra(intent, CityAndLocationExtra.getExtraName());
                String phone = deliverExtra.getPhone();
                String name = deliverExtra.getName();
                mPresenter.setDeliverExtra(deliverExtra);
                tvDeliverAddress.setText((deliverExtra.getBackwards2Name() == null ? "" : deliverExtra.getBackwards2Name())+" "+(deliverExtra.getAddress()==null ? "" : deliverExtra.getAddress()));
                if (phone == null) {
                    phone = "";
                }
                tvDeliverPhone.setText(phone);
                if (name == null) {
                    name = "";
                }
                tvDeliverName.setText(name);
                // 将发货布局的背景色改成白色
                changeDeliverLayoutColor();
            } else if (REQUEST_CODE_TO_DESTINATION_ADDRESS == requestCode) {// 从收货地址界面回来
                CityAndLocationExtra receiptExtra = getIntentExtra(intent, CityAndLocationExtra.getExtraName());
                String address = receiptExtra.getBackwards2Name();
                String phone = receiptExtra.getPhone();
                String name = receiptExtra.getName();
                mPresenter.setReceiptExtra(receiptExtra);
                tvReceiptAddress.setText(receiptExtra.getBackwards2Name()+" "+(receiptExtra.getAddress()==null?"":receiptExtra.getAddress()));  //收货地址
                if (phone == null) {
                    phone = "";
                }
                tvReceiptPhone.setText(phone);
                if (name == null) {
                    name = "";
                }
                tvReceiptName.setText(name);
                // 将收货布局的背景色改成白色
                changeReceiptLayoutColor();
            } else if (REQUEST_CODE_OFTEN_GOODS == requestCode) {// 常发货源界面回来
                OwnerGoodsParams params = getIntentExtra(intent, OwnerGoodsParams.getExtraName());
                // 请求服务器，根据goodsId获取订单数据
                mPresenter.getParamsFromServerById(params);
            }
        }
    }

    /**
     * 设置显示地址和手机号、姓名等等
     * needSetValue是否将extra填充到p里的extra中
     */
    @Override
    public void showAddress(int curAddressType, CityAndLocationExtra extra, boolean needSetValue) {
        if (curAddressType == TYPE_DEPART_ADDRESS) {
            if (needSetValue) {
                mPresenter.setDeliverExtra(extra);
            }
            tvDeliverAddress.setText((extra.getBackwards2Name() == null ? "" : extra.getBackwards2Name())+
                    " "+(extra.getAddress()==null ? "" : extra.getAddress()));  // 发货人地址
            String name = extra.getName();
            tvDeliverName.setText(name);  // 发货人姓名
            String phone = extra.getPhone();
            tvDeliverPhone.setText(phone);  // 发货人手机号
            changeDeliverLayoutColor();     // 将发货布局的背景色改成白色
        } else {
            if (needSetValue) {
                mPresenter.setReceiptExtra(extra);
            }
            tvReceiptAddress.setText((extra.getBackwards2Name() == null ? "" : extra.getBackwards2Name())+
                    " "+(extra.getAddress()==null?"":extra.getAddress()));  // 收货地址
            String name = extra.getName();
            tvReceiptName.setText(name);  // 收货人姓名
            String phone = extra.getPhone();
            tvReceiptPhone.setText(phone);  // 收货人手机号
            changeReceiptLayoutColor(); // 将收货布局的背景色改成白色
        }
    }

    /**
     * 显示车型车长
     */
    public void setTypeAndLengthText(String txt) {
        tvTypeLength.setText(txt);
    }

    /**
     * 支付方式滚轮选择回调（点击确定时回调）
     */
    @Override
    public void onWheelViewSelected(View view, int postion, String tag) {
        if (tag.equals("pay")) {
            setPayWayText(postion); // 设置支付方式文字 等操作
        }
    }

    /**
     * 设置支付方式文字 等操作
     */
    public void setPayWayText(int postion) {
        tvPayWay.setText(mPresenter.getPayList().get(postion));
        selectedPayWayIndex = postion;
    }

    /**
     * 显示路程距离
     */
    public void showDistance(String distanceStr) {
        tvDistance.setVisibility(View.VISIBLE);
        tvDistance.setText(distanceStr);
    }

    /**
     * 点击tvUnit
     */
    public void clickTvUnit(String txt) {
        tvUnit.setText(txt);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvUnit.performClick();
            }
        });
    }

    /**
     * 提货时间弹窗初始化
     */
    public void initTimeWheelPop() {
        loadDate = "1482933600000";
        loadDateType = "3";//提货时间 类型   0：具体时间（默认） 1：今定今装 2：今定明装 3：随到随走
        timeWheelPop = new TimeWheelPop(this, tvDeliverAddress, new TimeWheelPop.OnCompleteListener() {
            @Override
            public void OnCompleteListener(String mon, String morning, String hour) {
                if (!NumberFormatUtils.hasDigit(mon)) {
                    loadDate = mon;
                    if (loadDate.equals("今定今装")) {
                        loadDateType = "1";
                    } else if (loadDate.equals("今定明装")) {
                        loadDateType = "2";
                    } else if (loadDate.equals("随到随走")) {
                        loadDateType = "3";
                    }
                    tvTakeTime.setText(mon);
                } else {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");// 小写的mm表示的是分钟
                    String dstr = mon + " " + hour;
                    java.util.Date date;
                    try {
                        date = sdf.parse(dstr);
                        Logger.i("TTT", "date.getTime():" + date.getTime() + "");
                        loadDate = date.getTime() + "";
                        loadDateType = "0";
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    tvTakeTime.setText(TimeUtils.getFormatDate(Long.parseLong(loadDate), "MM-dd HH时"));
                }
            }
        }, loadDate, loadDateType);
        System.gc();
    }

    /**
     * 设置提货时间文本等操作
     */
    public void setTakeTimeText(String loadDateType, String loadDate) {
        this.loadDateType = loadDateType;
        this.loadDate = loadDate;
        tvTakeTime.setText(TimeUtils.getFormatDate(Long.parseLong(loadDate), "MM-dd HH时"));
    }

    /**
     * 设置发货单位的文字变化监听
     */
    public void setUnitTextChangeListener(final EditText editText) {
        RxTextView.textChanges(editText).compose(this.<CharSequence>bindToLifecycle())
                .subscribe(new Consumer<CharSequence>() {
                    @Override
                    public void accept(CharSequence charSequence) throws Exception {
                        String str = charSequence.toString();
                        Logger.d("TTT","setUnitTextChangeListener-----"+str.trim());
                        if (!TextUtils.isEmpty(str.trim())) {
                            if (editText.getId() == R.id.et_goods_quantity) {
                                String tempStr = NumberFormatUtils.decimalRestrict(str, 3, 2);      // 限定为前3后2的小数
                                if (!TextUtils.isEmpty(tempStr.trim()) && !tempStr.equals(str)) {
                                    editText.setText(tempStr);
                                    editText.setSelection(tempStr.length());
                                }
                                quantityNotEmptySetBg();
                            } else {
                                minOrMaxUnitNotEmptySetBg();
                            }
                        } else {
                            if (editText.getId() == R.id.et_goods_quantity) {
                                String tempStr = NumberFormatUtils.decimalRestrict(str, 3, 2);  // 限定为前3后2的小数
                                if (!TextUtils.isEmpty(tempStr.trim()) && !tempStr.equals(str)) {
                                    editText.setText(tempStr);
                                    editText.setSelection(tempStr.length());
                                }
                                quantityEmptySetBg();
                            } else {
                                if (TextUtils.isEmpty(etMinAmountTrend.getText().toString()) && TextUtils.isEmpty
                                        (etMaxAmountTrend.getText().toString())) {
                                    minOrMaxUnitEmptySetBg();
                                }
                            }
                        }
                    }
                });
    }

    /**
     * 设置费用的文字变化监听
     */
    public void setFeeTextChangeListener(final EditText editText) {
        RxTextView.textChanges(editText).compose(this.<CharSequence>bindToLifecycle())
                .subscribe(new Consumer<CharSequence>() {
                    @Override
                    public void accept(CharSequence charSequence) throws Exception {
                        String str = charSequence.toString();
                        if (!TextUtils.isEmpty(str.trim())) {
                            String tempStr = NumberFormatUtils.decimalRestrict(str, 6, 2);      // 限定为前6后2的小数
                            if (!TextUtils.isEmpty(tempStr.trim()) && !tempStr.equals(str)) {
                                editText.setText(tempStr);
                                editText.setSelection(tempStr.length());
                            }

                            float ahead = 0;        // 提货
                            float delivery = 0;     // 到货
                            float back = 0;         // 回单
                            String aheadStr = etAhead.getText().toString();
                            String deliveryStr = etDelivery.getText().toString();
                            String backStr = etBack.getText().toString();

                            if (!StringUtils.isEmpty(aheadStr)) {
                                ahead = Float.parseFloat(aheadStr);
                            }
                            if (!StringUtils.isEmpty(deliveryStr)) {
                                delivery = Float.parseFloat(deliveryStr);
                            }
                            if (!StringUtils.isEmpty(backStr)) {
                                back = Float.parseFloat(backStr);
                            }

                            tvSum.setText(ahead + delivery + back + "");
                        }
                    }
                });
    }

    /**
     * 设置地址的文字变化监听
     */
    public void setAddressTextChangeListener(final TextView textView) {
        RxTextView.textChanges(textView).compose(this.<CharSequence>bindToLifecycle())
                .subscribe(new Consumer<CharSequence>() {
                    @Override
                    public void accept(CharSequence charSequence) throws Exception {
                        String str = charSequence.toString();
                        if (!TextUtils.isEmpty(str.trim())) {
                            if (textView.getId() == R.id.tv_deliver_address) {       // 发货地址
                                changeDeliverLayoutColor();
                            } else if (textView.getId() == R.id.tv_receipt_address) { // 收货地址
                                changeReceiptLayoutColor();
                            }
                        }
                    }
                });
    }

    /**
     * 设置车型车长的文字变化监听
     */
    public void setTypeLengthTextChangeListener(final TextView textView) {
        RxTextView.textChanges(textView).compose(this.<CharSequence>bindToLifecycle())
                .subscribe(new Consumer<CharSequence>() {
                    @Override
                    public void accept(CharSequence charSequence) throws Exception {
                        String str = charSequence.toString();
                        if (!TextUtils.isEmpty(str.trim())) {
                            changeTypeAndLengthLayoutColor();
                        }
                    }
                });
    }

    /**
     * 将车型车长布局的背景色改成白色
     */
    public void changeTypeAndLengthLayoutColor() {
        rlTypeLength.setBackground(this.getResources().getDrawable(R.drawable.shape_bg_deliver_goods_04));
        tvTypeLength.setTextColor(ResourceUtils.getColor(R.color.text_color_222222));
        tvTypeLengthTitle.setTextColor(ResourceUtils.getColor(R.color.text_color_969696));
        ivTypeLength.setImageResource(R.drawable.icon_right_arrow_gray);
    }

    /**
     * 将收货布局的背景色改成白色
     */
    public void changeReceiptLayoutColor() {
        rlReceipt.setBackground(this.getResources().getDrawable(R.drawable.shape_bg_deliver_goods_04));
        ivEndPoint.setImageResource(R.drawable.icon_end_point);
        tvEndPoint.setTextColor(ResourceUtils.getColor(R.color.text_color_969696));
        tvReceiptAddress.setTextColor(ResourceUtils.getColor(R.color.text_color_222222));
        vEnd.setBackgroundColor(ResourceUtils.getColor(R.color.color_dddddd));
        tvToReceipt.setTextColor(ResourceUtils.getColor(R.color.text_color_969696));
        tvReceiptName.setTextColor(ResourceUtils.getColor(R.color.text_color_969696));
        tvReceiptPhone.setTextColor(ResourceUtils.getColor(R.color.text_color_969696));
    }

    /**
     * 将发货布局的背景色改成白色
     */
    public void changeDeliverLayoutColor() {
        rlDeliver.setBackground(this.getResources().getDrawable(R.drawable.shape_bg_deliver_goods_04));
        ivStart.setImageResource(R.drawable.icon_start_point);
        tvStart.setTextColor(ResourceUtils.getColor(R.color.text_color_969696));
        tvDeliverAddress.setTextColor(ResourceUtils.getColor(R.color.text_color_222222));
        vStart.setBackgroundColor(ResourceUtils.getColor(R.color.color_dddddd));
        tvToDeliver.setTextColor(ResourceUtils.getColor(R.color.text_color_969696));
        tvDeliverName.setTextColor(ResourceUtils.getColor(R.color.text_color_969696));
        tvDeliverPhone.setTextColor(ResourceUtils.getColor(R.color.text_color_969696));
    }

    /**
     * 最大最小单位不为空，背景设置
     */
    public void minOrMaxUnitNotEmptySetBg() {
        unitBackGroundIsWhite = true;
        llGoodsNumber.setBackground(this.getResources().getDrawable(R.drawable.shape_bg_deliver_goods_04));
        // 白底，范围选中
        tvUnit.setTextColor(ResourceUtils.getColor(R.color.text_color_666666));

        tvFixedSelect.setTextColor(ResourceUtils.getColor(R.color.text_color_969696));
        tvRangeSelect.setTextColor(ResourceUtils.getColor(R.color.text_color_16CA4E));
        vFixedSelect.setBackgroundColor(ResourceUtils.getColor(R.color.transparent_100));
        vRangeSelect.setBackgroundColor(ResourceUtils.getColor(R.color.color_16ca4e));
        vFen.setBackgroundColor(ResourceUtils.getColor(R.color.color_dddddd));

        vGang.setBackgroundColor(ResourceUtils.getColor(R.color.color_dddddd));
        etMinAmountTrend.setBackground(this.getResources().getDrawable(R.drawable.shape_bg_unit_selected));
        etMaxAmountTrend.setBackground(this.getResources().getDrawable(R.drawable.shape_bg_unit_selected));

        setUnitImg();
        setUnitItemColor();
    }


    /**
     * 货物数量不为空为空背景色设置
     */
    public void quantityNotEmptySetBg() {
        unitBackGroundIsWhite = true;
        llGoodsNumber.setBackground(this.getResources().getDrawable(R.drawable.shape_bg_deliver_goods_04));
        // 白底，固定选中
        tvUnit.setTextColor(ResourceUtils.getColor(R.color.text_color_666666));
        tvFixedSelect.setTextColor(ResourceUtils.getColor(R.color.text_color_16CA4E));
        tvRangeSelect.setTextColor(ResourceUtils.getColor(R.color.text_color_969696));
        vFixedSelect.setBackgroundColor(ResourceUtils.getColor(R.color.color_16ca4e));
        vRangeSelect.setBackgroundColor(ResourceUtils.getColor(R.color.transparent_100));
        vFen.setBackgroundColor(ResourceUtils.getColor(R.color.color_dddddd));

        setUnitImg();
        setUnitItemColor();
    }

    /**
     * 货物数量为空背景色设置
     */
    public void quantityEmptySetBg() {
        unitBackGroundIsWhite = false;
        llGoodsNumber.setBackground(this.getResources().getDrawable(R.drawable.shape_bg_deliver_goods_01));
        // 绿底，固定选中
        tvUnit.setTextColor(ResourceUtils.getColor(R.color.white));
        tvFixedSelect.setTextColor(ResourceUtils.getColor(R.color.white));
        tvRangeSelect.setTextColor(ResourceUtils.getColor(R.color.white));
        vFixedSelect.setBackgroundColor(ResourceUtils.getColor(R.color.white));
        vRangeSelect.setBackgroundColor(ResourceUtils.getColor(R.color.transparent_100));
        vFen.setBackgroundColor(ResourceUtils.getColor(R.color.white));

        vGang.setBackgroundColor(ResourceUtils.getColor(R.color.white));
        etMinAmountTrend.setBackground(this.getResources().getDrawable(R.drawable.shape_bg_unit_not_select));
        etMaxAmountTrend.setBackground(this.getResources().getDrawable(R.drawable.shape_bg_unit_not_select));

        setUnitImg();
        setUnitItemColor();
    }

    /**
     * 最大最小值为空时背景设置
     */
    public void minOrMaxUnitEmptySetBg() {
        unitBackGroundIsWhite = false;
        llGoodsNumber.setBackground(this.getResources().getDrawable(R.drawable.shape_bg_deliver_goods_01));
        // 绿底，范围选中
        tvUnit.setTextColor(ResourceUtils.getColor(R.color.white));
        tvFixedSelect.setTextColor(ResourceUtils.getColor(R.color.white));
        tvRangeSelect.setTextColor(ResourceUtils.getColor(R.color.white));
        vFixedSelect.setBackgroundColor(ResourceUtils.getColor(R.color.transparent_100));
        vRangeSelect.setBackgroundColor(ResourceUtils.getColor(R.color.white));
        vFen.setBackgroundColor(ResourceUtils.getColor(R.color.white));

        vGang.setBackgroundColor(ResourceUtils.getColor(R.color.white));
        etMinAmountTrend.setBackground(this.getResources().getDrawable(R.drawable.shape_bg_unit_not_select));
        etMaxAmountTrend.setBackground(this.getResources().getDrawable(R.drawable.shape_bg_unit_not_select));

        setUnitImg();
        setUnitItemColor();
    }

    /**
     * 设置R.id.iv_unit的图片
     */
    private void setUnitImg() {
        if (canSeeGvUnit) {
            if (unitBackGroundIsWhite) {
                ivUnit.setImageResource(R.drawable.icon_top_arrow_gray);
            } else {
                ivUnit.setImageResource(R.drawable.icon_top_arrow_white);
            }
        } else {
            if (unitBackGroundIsWhite) {
                ivUnit.setImageResource(R.drawable.icon_bottom_arrow_gray);
            } else {
                ivUnit.setImageResource(R.drawable.icon_bottom_arrow_white);
            }
        }
    }

    /**
     * 单位列表列表项点击选中事件
     */
    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        selectedUnitId = position;
        setUnitItemColor();
        clickTvUnit(mPresenter.getUnitList().get(selectedUnitId).getItem().getName());
    }

    /**
     * 改变RecyclerView->gvUnit的状态值，用以改变背景色
     * 并刷新gvUnit
     */
    private void setUnitItemColor() {
        List<UnitAndState> unitList = mPresenter.getUnitList();
        for (int i = 0; i < unitList.size(); i++) {
            if (unitBackGroundIsWhite) {
                if (i == selectedUnitId) {
                    unitList.get(i).setState(2);
                } else {
                    unitList.get(i).setState(3);
                }
            } else {
                if (i == selectedUnitId) {
                    unitList.get(i).setState(0);
                } else {
                    unitList.get(i).setState(1);
                }
            }
        }
        unitAdapter.setNewData(unitList);
    }

    /**
     * 设置R.id.iv_unit的图片和GvUnit的显隐
     */
    public void setUnitImgAndGV() {
        canSeeGvUnit = !canSeeGvUnit;
        if (canSeeGvUnit) {
            gvUnit.setVisibility(View.VISIBLE);
        } else {
            gvUnit.setVisibility(View.GONE);
        }
        setUnitImg();
    }

    /**
     * 从城市选择器中获取数据
     */
    @Subscribe
    public void onEvent(AreaSelectedData areaSelectedData) {
        if (areaSelectedData.getTag().equals(TAG)) {
            // 从城市选择器中获取数据
            mPresenter.selectAddress(curAddressType, areaSelectedData);
        }
    }

    /**
     * 从车型车长选择器中获取数据
     */
    @Subscribe
    public void onEvent(TruckTypeLengthSelectedData tld) {
        if (tld.getTag().equals(TAG)) {
            // 从车型车长选择器中获取数据
            mPresenter.selectTypeAndLength(tld);
        }
    }

    /**
     * 获取选中的单位序号
     */
    @Override
    public int getSelectedUnitId() {
        return selectedUnitId;
    }

    /**
     * 获取支付方式序号
     */
    @Override
    public int getSelectedPayWayIndex() {
        return selectedPayWayIndex;
    }

    /**
     * 页面点击事件
     */
    @OnClick({R.id.rl_deliver, R.id.rl_receipt, R.id.ll_fixed_select, R.id.ll_range_select, R.id.rl_pay_way, R.id
            .rl_take_time, R.id.rl_remarks, R.id.ll_to_deliver, R.id.ll_to_receipt, R.id.tv_unit, R.id.iv_unit, R.id
            .rl_type_length, R.id.ll_submit})
    public void clickViews(View v) {
        int id = v.getId();

        //防止重复点击
        if (DoubleClickCheckUtil.check(this.getClass().getName(), id)) {
            return;
        }

        switch (id) {
            case R.id.rl_deliver:
                // 发货信息，弹出城市选择器
                curAddressType = TYPE_DEPART_ADDRESS;
                new CitySelectPopupWindow(TAG).showCitySelectPopupWindow(this, tvDeliverAddress, false, false, true);
                break;
            case R.id.rl_receipt:
                // 收货信息，弹出城市选择器
                curAddressType = TYPE_DESTINATION_ADDRESS;
                new CitySelectPopupWindow(TAG).showCitySelectPopupWindow(this, tvDeliverAddress, false, false, true);
                break;
            case R.id.ll_to_deliver:
                // 跳转发货信息补充界面
                new KeyboardUtil(this).hideSoftInputFromWindow(this);
                turnToActivityForResult(AddressInformationActivity.class, REQUEST_CODE_TO_DEPART_ADDRESS, mPresenter
                        .getDeliverExtra());
                break;
            case R.id.ll_to_receipt:
                // 跳转收货信息补充界面
                new KeyboardUtil(this).hideSoftInputFromWindow(this);
                turnToActivityForResult(AddressInformationActivity.class, REQUEST_CODE_TO_DESTINATION_ADDRESS,
                        mPresenter
                                .getReceiptExtra());
                break;
            case R.id.ll_fixed_select:
                // 选择固定单位标签
                mCurrentUnit = FLAG_UNITS_EXACT;
                etGoodsQuantity.setVisibility(View.VISIBLE);
                llRange.setVisibility(View.GONE);

                if (StringUtils.isEmpty(etGoodsQuantity.getText().toString())) {
                    // 货物数量为空背景色设置
                    quantityEmptySetBg();
                } else {
                    quantityNotEmptySetBg();
                }
                break;
            case R.id.ll_range_select:
                // 选择范围单位标签
                mCurrentUnit = FLAG_UNITS_RANGE;
                etGoodsQuantity.setVisibility(View.INVISIBLE);
                llRange.setVisibility(View.VISIBLE);

                if (StringUtils.isEmpty(etMinAmountTrend.getText().toString())
                        && StringUtils.isEmpty(etMaxAmountTrend.getText().toString())) {
                    minOrMaxUnitEmptySetBg();
                } else {
                    minOrMaxUnitNotEmptySetBg();
                }
                break;
            case R.id.tv_unit:
            case R.id.iv_unit:
                setUnitImgAndGV(); // 点击单位，显隐单位gridview

                break;
            case R.id.rl_type_length:   // 车型车长
                new TruckLengthAndTypePopupWindow(TAG, 2, 5, true, true)
                        .showPopupWindow(this,
                                tvTypeLength,
                                true,
                                TruckLengthAndTypePopupWindow.ENTIRETY_SELECTED_INDEX_1,
                                true,
                                mPresenter.getTruckTypeLengthSelectedData());
                break;
            case R.id.rl_pay_way:  // 支付方式
                PayWayPopup.alertBottomWheelOption(this, mPresenter.getPayList(), this, selectedPayWayIndex, "pay");
                break;
            case R.id.rl_take_time:  // 提货时间
                timeWheelPop.showPop();
                break;
            case R.id.rl_remarks:  // 其他信息
                new KeyboardUtil(this).hideSoftInputFromWindow(this);
                turnToActivityForResult(OtherInformationActivity.class, REQUEST_CODE_OTHER, mPresenter
                        .getOtherInfoExtra());
                break;
            case R.id.ll_submit: // 提交
                new KeyboardUtil(this).hideSoftInputFromWindow(this);   // 隐藏输入法
                // 整理请求参数
                mPresenter.disposeData(loadDateType,// 提货时间 类型
                        loadDate,
                        mCurrentUnit,
                        etGoodsQuantity.getText().toString(),
                        etMaxAmountTrend.getText().toString(),
                        etMinAmountTrend.getText().toString(),
                        etAhead.getText().toString(),
                        etDelivery.getText().toString(),
                        etBack.getText().toString(),
                        tvSum.getText().toString(),
                        cbAhead.isChecked(),
                        cbDelivery.isChecked());
                break;
        }
    }

    /**
     * 点击提交时，是否有字段未填写
     * 判断是否跳转是否托管运费页面
     *
     * @param success  是否成功
     * @param errorMsg 错误信息
     */
    public void onClickSumbmitError(boolean success, String errorMsg, OwnerGoodsParams params) {
        if (success) {
            if (params.getInType() == OwnerGoodsParams.IN_TYPE_ADD_ASSIGNED) {
                curNetMode = 0;
                // 是否托管运费参数设置
                setFreightFeeManaged(params);
                // 发布定向货源接口调用
                mPresenter.addAssignedGoods(params, TAG);
            } else if (params.getInType() == OwnerGoodsParams.IN_TYPE_EDIT_ASSIGNED// 修改定向订单
                    || params.getInType() == OwnerGoodsParams.IN_TYPE_ADD_ASSIGNED_REFIND_TRUCK) {// 定向订单-重新找车
                curNetMode = 0;
                // 是否托管运费参数设置
                setFreightFeeManaged(params);
                // 修改定向订单接口调用
                mPresenter.updateGoodsAssigned(params, TAG);
            } else if (tvPayWay.getText().toString().contains("回单")) {
                curNetMode = 1;
                // 提付费是否托	0:不托管 1:托管
                params.setFreight_fee_managed("0");
                // 发布货源接口调用
                mPresenter.addGoodsWithBackOrder(params, TAG);
            } else {
                turnToActivity(TrusteeshipActivity.class, params);
            }
        } else {
            Toaster.showLongToast(errorMsg);
        }
    }

    /**
     * 不跳转是否托管运费页面时，是否托管运费参数设置
     * 0:不托管 1:托管
     */
    private void setFreightFeeManaged(OwnerGoodsParams params) {
        if ((StringUtils.isNotBlank(etAhead.getText().toString())
                && etAhead.getText().toString().equals("0")
                && cbAhead.isChecked())
                || (StringUtils.isNotBlank(etDelivery.getText().toString())
                && etDelivery.getText().toString().equals("0")
                && cbDelivery.isChecked())) {
            params.setFreight_fee_managed("1");
        } else {
            params.setFreight_fee_managed("0");
        }
    }

    /**
     * 协议页面点击同意
     * 跳转首页我的订单
     */
    @Subscribe
    public void onEvent(ClickAgreeBtnEvent event) {
        if (event.getTag().equalsIgnoreCase(TAG)) {
            // 跳转我的订单
            turnToActivity(MainActivity.class, new TabIndex(TabIndex.MY_ORDER_PAGE, TabIndex.MY_ORDER_PAGE));
            //  添加下单成功提示
            Toaster.showShortToast(getResources().getString(R.string.order_success));
        }
    }

    /**
     * 添加发货
     */
    public static void turnToDeliverGoodsForAdd(MvpActivity activity) {
        OwnerGoodsParams params = turnParamsMaking(OwnerGoodsParams.IN_TYPE_ADD, null, null, null, null, null);
        activity.turnToActivity(DeliverGoodsActivity.class, params);
    }

    /**
     * 复制订单
     *
     * @param goodsId 货源id
     */
    public static void turnToDeliverGoodsForCopy(MvpActivity activity, String goodsId) {
        OwnerGoodsParams params = turnParamsMaking(OwnerGoodsParams.IN_TYPE_COPY, goodsId, null, null, null, null);
        activity.turnToActivity(DeliverGoodsActivity.class, params);
    }

    /**
     * 修改订单
     *
     * @param goodsId 货源id
     */
    public static void turnToDeliverGoodsForEdit(MvpActivity activity, String goodsId) {
        OwnerGoodsParams params = turnParamsMaking(OwnerGoodsParams.IN_TYPE_EDIT, goodsId, null, null, null, null);
        activity.turnToActivity(DeliverGoodsActivity.class, params);
    }

    /**
     * 添加定向发货
     */
    public static void turnToDeliverGoodsForAddAssigned(MvpActivity activity, String driver_id, String
            driver_truck_id, List<String> truck_type_ids, List<String> truck_length_ids) {
        OwnerGoodsParams params = turnParamsMaking(OwnerGoodsParams.IN_TYPE_ADD_ASSIGNED, null, driver_id,
                driver_truck_id, truck_type_ids, truck_length_ids);
        activity.turnToActivity(DeliverGoodsActivity.class, params);
    }

    /**
     * 定向订单-重新找车
     * 由于该种情况goodsId需要一直传递 （我的订单 -》 车辆列表 -》 发货页面），所以在CMemoryData中存取
     */
    public static void turnToDeliverGoodsForAddAssignedIncidentalGoodsId(MvpActivity activity, String driver_id, String
            driver_truck_id, List<String> truck_type_ids, List<String> truck_length_ids) {
        OwnerGoodsParams params = turnParamsMaking(OwnerGoodsParams.IN_TYPE_ADD_ASSIGNED_REFIND_TRUCK, null,
                driver_id, driver_truck_id, truck_type_ids, truck_length_ids);
        activity.turnToActivity(DeliverGoodsActivity.class, params);
    }

    /**
     * 修改定向发货，附带订单id
     * goodsId从CMemoryData中取
     */
    public static void turnToDeliverGoodsForEditAssigned(MvpActivity activity) {
        OwnerGoodsParams params = turnParamsMaking(OwnerGoodsParams.IN_TYPE_EDIT_ASSIGNED, null, null, null, null,
                null);
        activity.turnToActivity(DeliverGoodsActivity.class, params);
    }

    /**
     * 跳转发货页，自己带入type
     */
    public static void turnToDeliverGoodsWithType(MvpActivity activity, int inType, String goodsId, String driver_id,
                                                  String driver_truck_id, List<String> truck_type_ids, List<String>
                                                          truck_length_ids) {
        OwnerGoodsParams params = turnParamsMaking(inType, goodsId, driver_id, driver_truck_id, truck_type_ids,
                truck_length_ids);
        activity.turnToActivity(DeliverGoodsActivity.class, params);
    }

    /**
     * 跳转参数整理
     */
    private static OwnerGoodsParams turnParamsMaking(int inType, String goodsId, String driver_id, String
            driver_truck_id, List<String> truck_type_ids, List<String> truck_length_ids) {
        OwnerGoodsParams params = new OwnerGoodsParams();
        params.setInType(inType);
        params.setGoods_id(goodsId);
        params.setDriver_id(driver_id);
        params.setDriver_truck_id(driver_truck_id);
        params.setTruck_type_ids(truck_type_ids);
        params.setTruck_length_ids(truck_length_ids);
        return params;
    }
}