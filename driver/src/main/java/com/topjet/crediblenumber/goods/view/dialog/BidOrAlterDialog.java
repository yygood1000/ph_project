package com.topjet.crediblenumber.goods.view.dialog;

import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.topjet.common.base.dialog.BaseDialog;
import com.topjet.common.base.listener.DebounceClickListener;
import com.topjet.common.base.listener.ObserverOnResultListener;
import com.topjet.common.base.net.request.CommonParams;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.common.presenter.SkipControllerWallet;
import com.topjet.common.utils.NumberFormatUtils;
import com.topjet.common.utils.ResourceUtils;
import com.topjet.common.utils.StringUtils;
import com.topjet.common.utils.Toaster;
import com.topjet.crediblenumber.R;
import com.topjet.crediblenumber.goods.modle.params.AlterQuotedPriceParams;
import com.topjet.crediblenumber.goods.modle.params.BidGoodsParams;
import com.topjet.crediblenumber.goods.modle.params.GetBidedPersonCountParams;
import com.topjet.crediblenumber.goods.modle.response.BidOrderAlterResponse;
import com.topjet.crediblenumber.goods.modle.response.GetBidedPersonCountResponse;
import com.topjet.crediblenumber.goods.modle.serverAPI.GoodsCommand;
import com.topjet.crediblenumber.goods.modle.serverAPI.GoodsCommandAPI;

/**
 * creator: zhulunjun
 * time:    2017/9/18
 * describe: 报价，修改报价
 */

public class BidOrAlterDialog extends BaseDialog {
    private MvpActivity mActivity;
    private String tag;

    private TextView tvTitle;
    private EditText etFreight;
    private EditText etDeposit;
    private TextView tvNumberOfPerson;
    // 请求用相关参数
    private String transport_fee;//报价金额
    private String deposit_fee;//	定金
    private String goods_id;//货源id
    private String goods_version;//	货源版本
    private String pre_goods_id;//报价单id
    private String pre_goods_version;//报价版本

    private boolean isAlter;// 是否是修改报价
    private OnPayForDepositResultListener onPayForDepositResultListener;// 支付信息费结果回调
    private String billNo;

    public BidOrAlterDialog(MvpActivity activity) {
        super(activity, R.layout.dialog_bidding);
        this.mActivity = activity;
    }

    @Override
    public void initView() {
        super.initView();
        setShowBottom();
    }

    public interface OnPayForDepositResultListener {
        void onSucceed();
    }

    private void showPopupWindow(boolean isAlter, String goodsId, String goodsVersion, String
            freight, String deposit, String preGoodsId, String preGoodsVersion) {
        // 获取控件
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        tvNumberOfPerson = (TextView) view.findViewById(R.id.tv_number_of_person);
        etFreight = (EditText) view.findViewById(R.id.et_freight);
        etDeposit = (EditText) view.findViewById(R.id.et_deposit);
        Button btnConfirm = (Button) view.findViewById(R.id.btn_confirm);
        ImageView ivClose = (ImageView) view.findViewById(R.id.iv_close);

        goods_id = goodsId;
        goods_version = goodsVersion;
        transport_fee = freight;
        deposit_fee = deposit.equals("0") ? "" : deposit;
        pre_goods_id = preGoodsId;
        pre_goods_version = preGoodsVersion;

        this.isAlter = isAlter;

        // 根据参数显示UI
        initUI(isAlter);
        getPersonCount();

        btnConfirm.setOnClickListener(clickListener);
        ivClose.setOnClickListener(clickListener);

        show();
    }

    /**
     * 根据参数显示UI
     */
    private void initUI(boolean isAlter) {
        // 修改报价
        if (isAlter) {
            tvTitle.setText(ResourceUtils.getString(R.string.alter_quoted_price));
            etFreight.setText(StringUtils.isBlank(transport_fee) ? "" : transport_fee);
            etDeposit.setText(StringUtils.isBlank(deposit_fee) ? "" : deposit_fee);
        }
    }

    private DebounceClickListener clickListener = new DebounceClickListener() {
        @Override
        public void performClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.iv_close:
                    dismiss();
                    break;
                case R.id.btn_confirm:
                    onClickConfirm();
                    break;
            }
        }
    };

    /**
     * 点击确认按钮的逻辑判断
     */
    private void onClickConfirm() {
        // 已经接单或修改成功，但是获取支付信息时失败了，点击确认直接获取支付信息然后跳转钱包
        if (StringUtils.isNotBlank(billNo)) {
            SkipControllerWallet.getPayForInfo(mActivity, SkipControllerWallet.PAY_FOR_DEPOSIT, billNo,
                    true, new SkipControllerWallet.OnPayForResultListener() {
                        @Override
                        public void onSucceed() {
                            dismiss();
                            onPayForDepositResultListener.onSucceed();
                        }
                    });
        } else if (applyFee()) {// 校验费用字段，之后进行报价/修改报价
            if (isAlter) {
                // 修改报价
                aleatQuotedPrice();
            } else {
                // 报价
                bidGoods();
            }

        }
    }

    /**
     * 校验费用字段
     */
    private boolean applyFee() {
        if (StringUtils.isNotBlank(etDeposit.getText().toString().trim()) &&
                Integer.parseInt(NumberFormatUtils.removeDecimalZero(etDeposit.getText().toString().trim())) < 50) {
            Toaster.showShortToast("定金不能少于50元");
            return false;
        }

        if (StringUtils.isNotBlank(etFreight.getText().toString()) &&
                Integer.parseInt(NumberFormatUtils.removeDecimalZero(etFreight.getText().toString())) <= 0) {
            Toaster.showShortToast("报价不能为0，请重新输入");
            etFreight.setText("");
            return false;
        }

        return checkData();
    }

    /**
     * 检查数据
     */
    private boolean checkData() {
        transport_fee = etFreight.getText().toString();
        if (StringUtils.isEmpty(transport_fee)) {
            Toaster.showShortToast("请输入报价");
            return false;
        }
        deposit_fee = etDeposit.getText().toString();
        deposit_fee = StringUtils.isEmpty(deposit_fee) ? "0" : deposit_fee;
        return true;
    }

    /**
     * 接单
     */
    private void bidGoods() {
        BidGoodsParams params = new BidGoodsParams(transport_fee, deposit_fee, goods_id, goods_version);
        new GoodsCommand(GoodsCommandAPI.class).bidGoods(params, new ObserverOnResultListener<BidOrderAlterResponse>() {
            @Override
            public void onResult(BidOrderAlterResponse response) {
                // 获取到定金托管的账单号，跳转钱包支付
                convert(response.getBill_no());
            }
        });
    }

    /**
     * 修改报价
     */
    private void aleatQuotedPrice() {
        CommonParams<AlterQuotedPriceParams> commonParams = new CommonParams<>(GoodsCommandAPI.ALEAT_QUOTED_PRICE);
        AlterQuotedPriceParams params = new AlterQuotedPriceParams(transport_fee, deposit_fee, pre_goods_id,
                pre_goods_version);

        commonParams.setParameter(params);

        new GoodsCommand(GoodsCommandAPI.class).aleatQuotedPrice(params, new
                ObserverOnResultListener<BidOrderAlterResponse>() {
                    @Override
                    public void onResult(BidOrderAlterResponse response) {
                        // 获取到定金托管的账单号，跳转钱包支付
                        convert(response.getBill_no());
                    }
                });
    }

    /**
     * 处理接单，修改报价的返回结果
     *
     * @param billNo 账单号
     */
    private void convert(String billNo) {
        dismiss();
        if (StringUtils.isNotBlank(billNo)) {
            this.billNo = billNo;
            SkipControllerWallet.getPayForInfo(mActivity, SkipControllerWallet.PAY_FOR_DEPOSIT, billNo, false,
                    new SkipControllerWallet.OnPayForResultListener() {
                        @Override
                        public void onSucceed() {
                            Toaster.showShortToast("接单成功");
                            onPayForDepositResultListener.onSucceed();
                        }
                    });
        } else {
            if (isAlter) {
                Toaster.showShortToast("修改成功");
            } else {
//                Toaster.showShortToast("接单成功");
                Toaster.showShortToast("报价成功");
            }
            // 没有带定金的的接单与修改报价，在处理最后结果的时候直接调用接口方法
            onPayForDepositResultListener.onSucceed();
        }
    }

    /**
     * 获取已报价人数
     */
    private void getPersonCount() {
        GetBidedPersonCountParams params = new GetBidedPersonCountParams(goods_id);
        new GoodsCommand(GoodsCommandAPI.class).getBidedPersonCount(params, new
                ObserverOnResultListener<GetBidedPersonCountResponse>() {
                    @Override
                    public void onResult(GetBidedPersonCountResponse getBidedPersonCountResponse) {
                        setNumberOfPerson(getBidedPersonCountResponse.getPregoods_valid_count());
                    }
                });
    }

    /**
     * 设置已报价人数
     */
    private void setNumberOfPerson(String count) {
        tvNumberOfPerson.setVisibility(View.VISIBLE);
        tvNumberOfPerson.setText(Html.fromHtml("<font color='#222222' size='13'>已有</font>"
                + "<font color='#FF5E5E' size='30'>" + count + "</font>"
                + "<font color='#222222' size='13'>人报价</font>"));
    }

    /*=========================对外提供的展示方法=========================*/

    /**
     * 显示报价弹窗
     */
    public void showBidView(String goodsId, String goodsVersion, OnPayForDepositResultListener
            listener) {
        this.onPayForDepositResultListener = listener;
        showPopupWindow(false, goodsId, goodsVersion, "", "", "", "");
    }

    /**
     * 显示修改报价弹窗
     *
     * @param goodsId         货源Id
     * @param freight         运费
     * @param deposit         定金
     * @param preGoodsId      报价Id
     * @param preGoodsVersion 报价版本号
     * @param listener        回掉（只有成功了才会回调），失败的处理在里面已经处理了
     */
    public void showAlterView(String goodsId,
                              String freight, String deposit,
                              String preGoodsId, String preGoodsVersion,
                              OnPayForDepositResultListener listener) {
        this.onPayForDepositResultListener = listener;
        showPopupWindow(true, goodsId, "", freight, deposit, preGoodsId, preGoodsVersion);
    }

    /*=========================对外提供的展示方法=========================*/
}
