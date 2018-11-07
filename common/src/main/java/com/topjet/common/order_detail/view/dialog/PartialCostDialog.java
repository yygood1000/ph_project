package com.topjet.common.order_detail.view.dialog;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.topjet.common.R;
import com.topjet.common.base.dialog.BaseDialog;
import com.topjet.common.order_detail.modle.bean.FreightInfo;
import com.topjet.common.order_detail.modle.params.UpdatePayTypeParams;
import com.topjet.common.utils.NumberFormatUtils;
import com.topjet.common.utils.StringUtils;
import com.topjet.wallet.utils.Toaster;

import butterknife.ButterKnife;

/**
 * creator: zhulunjun
 * time:    2017/9/13
 * describe: 1.修改支付方式
 * 2.提付部分运费支付方式
 */

public class PartialCostDialog extends BaseDialog {
    private EditText etPickUp;
    private CheckBox cbPickUpCost;
    private EditText etAround;
    private CheckBox cbAroundCost;
    private TextView tvBack;
    private TextView tvBtnCancel;
    private TextView tvBtnConfirm;
    private TextView tvFreight;
    private TextView tvTitle;
    private TextView tv1, tv2, tvAroundCost;

    private double pickUpCost = 0, aroundCost = 0, backCost = 0; // 提付费，到付费，回单付
    private FreightInfo freightInfo;
    private double freight = 0;
    private int pickUpCheck = 1, aroundCheck = 1;
    private OnConfirmClick mListener; // 点击提交的回调

    private boolean isOnlyShow = false; // 是否不让修改托管，支付运费弹窗时，不让修改， 这个时候提付和到付不能都为0

    public PartialCostDialog(Context context) {
        super(context, R.layout.dialog_part_cost);
    }

    @Override
    public void initView() {
        super.initView();
        etPickUp = ButterKnife.findById(view, R.id.et_pick_up_cost);
        cbPickUpCost = ButterKnife.findById(view, R.id.cb_pick_up_cost);
        etAround = ButterKnife.findById(view, R.id.et_around);
        cbAroundCost = ButterKnife.findById(view, R.id.cb_around_cost);
        tvBack = ButterKnife.findById(view, R.id.tv_back);
        tvBtnCancel = ButterKnife.findById(view, R.id.tv_btn_cancel);
        tvBtnConfirm = ButterKnife.findById(view, R.id.tv_btn_confirm);
        tvFreight = ButterKnife.findById(view, R.id.tv_freight);
        tvTitle = ButterKnife.findById(view, R.id.tv_title);

        tv1 = ButterKnife.findById(view, R.id.tv_1);
        tv1 = ButterKnife.findById(view, R.id.tv_2);
        tvAroundCost = ButterKnife.findById(view, R.id.tv_around_cost_t);

        setListener();

    }

    /**
     * 报价列表的 提付部分运费支付方式 司机
     *
     * @param freight
     */
    public void showSetCost(FreightInfo freight, boolean isOnlyShow, OnConfirmClick listener) {
        tvTitle.setText("提付部分运费支付方式");
        setFreight(freight);
        mListener = listener;
        if (isOnlyShow) {
            this.isOnlyShow = isOnlyShow;
            setOnlyShow();
        }
        this.show();
    }

    /**
     * 可修改的弹窗
     */
    public void showSetCost(FreightInfo freight, OnConfirmClick listener) {
        showSetCost(freight, false, listener);
    }

    /**
     * 详情页的 修改支付方式 货主
     *
     * @param freight
     */
    public void showUpdatePayment(FreightInfo freight, OnConfirmClick listener) {
        tvTitle.setText("修改支付方式");
        setFreight(freight);
        mListener = listener;
        this.show();
    }

    /**
     * 不能取消托管勾选
     */
    public void setOnlyShow() {
        cbAroundCost.setChecked(true);
        cbPickUpCost.setChecked(true);
        cbPickUpCost.setEnabled(false);
        cbAroundCost.setEnabled(false);
//        cbAroundCost.setVisibility(View.GONE);
//        cbPickUpCost.setVisibility(View.GONE);
//        tvAroundCost.setVisibility(View.GONE);
//        tv1.setTextColor(mContext.getResources().getColor(R.color.text_color_969696));
//        tv2.setTextColor(mContext.getResources().getColor(R.color.text_color_969696));
//        etPickUp.setTextColor(mContext.getResources().getColor(R.color.text_color_969696));
//        etAround.setTextColor(mContext.getResources().getColor(R.color.text_color_969696));
//        etPickUp.setEnabled(false);
//        etAround.setEnabled(false);
    }


    /**
     * 设置运费
     */
    public PartialCostDialog setFreight(FreightInfo freightInfo) {
        this.freight = freightInfo.getFreight_fee();
        if (freight > 0) {
            tvFreight.setText(StringUtils.format(R.string.total_freight, freight + ""));
            etPickUp.setText(""+freightInfo.getAhead_fee());
            etAround.setText(""+freightInfo.getDelivery_fee());
            tvBack.setText(""+freightInfo.getBack_fee());
            if(freightInfo.getAhead_fee() == 0 && freightInfo.getDelivery_fee() == 0 && freightInfo.getBack_fee() == 0){
                setCost();
            } else {
                pickUpCost = freightInfo.getAhead_fee();
                aroundCost = freightInfo.getDelivery_fee();
                backCost = freightInfo.getBack_fee();
            }
            etPickUp.addTextChangedListener(pickUpTextWatcher);
            etAround.addTextChangedListener(aroundTextWatcher);
        }

        return this;
    }


    /**
     * 默认平方运费给提付费和到付费，回单付为0
     * 如果无法平方，提付费多1
     */
    private void setCost() {
        if (freight % 2 != 0) {
            //奇数
            pickUpCost = (freight - 1) / 2 + 1;
            aroundCost = freight - pickUpCost;
            etPickUp.setText("" + pickUpCost);
            etAround.setText("" + aroundCost);
            tvBack.setText("" + backCost);
        } else {
            //偶数
            pickUpCost = freight / 2;
            aroundCost = freight - pickUpCost;
            etPickUp.setText("" + pickUpCost);
            etAround.setText("" + aroundCost);
            tvBack.setText("" + backCost);
        }


    }


    /**
     * 设置监听
     */
    private void setListener() {


        // 1是托管， 0是不托管
        cbAroundCost.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    aroundCheck = 1;
                } else {
                    aroundCheck = 0;
                }
            }
        });

        cbPickUpCost.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    pickUpCheck = 1;
                } else {
                    pickUpCheck = 0;
                }
            }
        });

        tvBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        tvBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkCost()) {
                    if(pickUpCost == 0){ // 费用为0则不托管
                        cbPickUpCost.setChecked(false);
                    }
                    if(aroundCost == 0){ // 费用为0则不托管
                        cbAroundCost.setChecked(false);
                    }
                    UpdatePayTypeParams payTypeParams = new UpdatePayTypeParams();
                    payTypeParams.setAhead_fee(pickUpCost + "");
                    payTypeParams.setAhead_fee_is_managed(pickUpCheck + "");
                    payTypeParams.setDelivery_fee(aroundCost + "");
                    payTypeParams.setDelivery_fee_is_managed(aroundCheck + "");
                    payTypeParams.setBack_fee(backCost + "");
                    mListener.confirmClick(payTypeParams);
                    dismiss();
                }
            }
        });
    }

    /**
     * 支付运费进入，不让提付到付都为0
     */
    private boolean checkCost() {
        if(pickUpCost == 0){ // 费用为0则不托管
            cbPickUpCost.setChecked(false);
        }
        if(aroundCost == 0){ // 费用为0则不托管
            cbAroundCost.setChecked(false);
        }
        if (isOnlyShow && pickUpCost == 0 && aroundCost == 0) {
            Toaster.showLongToast("提付费和到付费不可同时为0");
            return false;
        }
        return true;
    }

    //提付费输入操作
    private TextWatcher pickUpTextWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!StringUtils.isBlank(s.toString())) {
                pickUpCost = StringUtils.getDoubleToString(s.toString());//得到当前提付费
            } else {
                pickUpCost = 0;
            }
            if(aroundCost > 0 && backCost > 0) { // 回单付和到付都大于0
                if(pickUpCost >= aroundCost){
                    aroundCost = 0;
                    backCost = freight - pickUpCost;
                    tvBack.setText("" + backCost);
                } else {
                    aroundCost = aroundCost - pickUpCost;
                }
                etAround.setText(""+aroundCost);
            }else if(backCost >  0){ // 回单付大于0
                if(pickUpCost >= backCost){
                    backCost = 0;
                }else {
                    backCost = freight - pickUpCost;
                }
                tvBack.setText(""+backCost);
            } else {// 回单付等于0
                if (pickUpCost <= freight) {
                    aroundCost = freight - pickUpCost;//计算到付费
                    etAround.setText("" + aroundCost);
                    backCost = 0;
                    tvBack.setText("" + backCost);
                } else {
                    pickUpCost = freight;
                    etPickUp.setText("" + pickUpCost);
                    aroundCost = freight - pickUpCost;//计算到付费
                    etAround.setText("" + aroundCost);
                    backCost = 0;
                    tvBack.setText("" + backCost);
                }
            }
            checkCost();
        }
    };

    //到付费输入操作
    private TextWatcher aroundTextWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!StringUtils.isBlank(s.toString())) {
                aroundCost = StringUtils.getDoubleToString(s.toString());//得到当前到付费
            } else {
                aroundCost = 0;
            }

            double tempdF = freight - pickUpCost;
            if (aroundCost <= tempdF) {
                backCost = freight - pickUpCost - aroundCost;//计算回单付
                tvBack.setText("" + backCost);
            } else {
                aroundCost = tempdF;
                etAround.setText("" + aroundCost);
                backCost = freight - pickUpCost - aroundCost;//计算回单付
                tvBack.setText("" + backCost);
            }
            checkCost();

        }
    };


    public interface OnConfirmClick {
        void confirmClick(UpdatePayTypeParams payTypeParams);
    }
}
