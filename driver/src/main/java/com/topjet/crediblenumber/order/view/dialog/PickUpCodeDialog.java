package com.topjet.crediblenumber.order.view.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.topjet.common.base.dialog.BaseDialog;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.utils.CallPhoneUtils;
import com.topjet.common.utils.KeyboardUtil;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.StringUtils;
import com.topjet.common.utils.Toaster;
import com.topjet.common.widget.PwdEditText;
import com.topjet.crediblenumber.R;

import butterknife.ButterKnife;

/**
 * creator: zhulunjun
 * time:    2017/9/18
 * describe: 提货码，签收码
 */

public class PickUpCodeDialog extends BaseDialog implements View.OnClickListener {
    ImageView ivClose;
    TextView tvBtnConfirm, tvTitle, tvMessage, tvName;
    ImageView ivCall;
    PwdEditText pePickUp;

    // 拨打电话信息
    private String mobile, name, text;
    private String pickCode;

    private int inputType = 2;
    // 自动输入
    private static final int INPUT_AUTO = 1;
    // 默认手动输入
    private static final int INPUT_DEFAULT = 2;

    private CallOwnerInfoBean mCallOwnerInfoBean;

    public PickUpCodeDialog(Context context, CallOwnerInfoBean callOwnerInfoBean) {
        super(context, R.layout.dialog_pick_up);
        this.mCallOwnerInfoBean = callOwnerInfoBean;
        Logger.d("签收码弹窗 " + callOwnerInfoBean.toString());
        initCallInfo();
    }

    private void initCallInfo() {
        text = mCallOwnerInfoBean.getCallText(mContext);
        mobile = mCallOwnerInfoBean.getMobile();
        name = mCallOwnerInfoBean.getName();
        tvName.setText(text);
    }


    @Override
    public void initView() {
        super.initView();
        ivClose = ButterKnife.findById(view, R.id.iv_close);
        pePickUp = ButterKnife.findById(view, R.id.pe_pick_up);
        tvBtnConfirm = ButterKnife.findById(view, R.id.tv_btn_confirm);
        tvTitle = ButterKnife.findById(view, R.id.tv_title);
        tvMessage = ButterKnife.findById(view, R.id.tv_message);
        ivCall = ButterKnife.findById(view, R.id.iv_call);
        tvName = ButterKnife.findById(view, R.id.tv_name);

        ivClose.setOnClickListener(this);
        tvBtnConfirm.setOnClickListener(this);
        ivCall.setOnClickListener(this);

        initCodeEdit();

    }

    /**
     * 初始化显示控件
     */
    private void initCodeEdit() {
        pePickUp.initStyle(R.drawable.edit_num_bg, 6, 0.5f, R.color.text_color_999999, R.color.text_color_222222, 28);
        pePickUp.setOnTextFinishListener(new PwdEditText.OnTextFinishListener() {
            @Override
            public void onFinish(String str) {
                pickCode = str; // 自动输入完，需要点击按钮
                if (inputType == INPUT_DEFAULT) { // 手动输入，输入完成，直接回调
                    if (pickUpCodeListener != null && !TextUtils.isEmpty(pickCode)) {
                        pickUpCodeListener.onPickUpConfirmClick(pickCode);
                        dismiss();
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                dismiss();
                break;
            case R.id.iv_call:
                call();
                break;
            case R.id.tv_btn_confirm:
                if (pickUpCodeListener != null && !TextUtils.isEmpty(pickCode)) {
                    pickUpCodeListener.onPickUpConfirmClick(pickCode);
                }
                dismiss();
                break;
        }
    }


    /**
     * 设置拨打电话的参数
     * 如果有发货人信息，就显示发货人信息
     * 如果有货主信息，就显示货主信息
     */
    private void call() {
        if (StringUtils.isEmpty(mobile)) {
            Toaster.showShortToast("无拨号信息");
            return;
        }

        new CallPhoneUtils().showCallDialogWithAdvNotUpload(
                (MvpActivity) mContext,
                ivCall,
                name,
                mobile,
                text,
                0
        );
    }


    private PickUpCodeListener pickUpCodeListener;

    public PickUpCodeDialog setPickUpCodeListener(PickUpCodeListener pickUpCodeListener) {
        this.pickUpCodeListener = pickUpCodeListener;
        return this;
    }

    public interface PickUpCodeListener {
        void onPickUpConfirmClick(String pickUpCode);
    }


    /**
     * 显示自动输入提货码的弹窗
     *
     * @param code
     */
    public void showAutoCode(String code) {
        if (!TextUtils.isEmpty(code)) {
            inputType = INPUT_AUTO;
            pickCode = code;
            pePickUp.setTexts(code);
            tvBtnConfirm.setVisibility(View.VISIBLE);
            show();
        }

    }

    /**
     * 默认显示，不自动输入提货码
     */
    public void showDefault() {
        inputType = INPUT_DEFAULT;
        tvBtnConfirm.setVisibility(View.GONE);
        pePickUp.setFocus();
        show();
        new KeyboardUtil(mContext).showSoftInput(pePickUp.getEditText(), false);
    }


    /**
     * 输入签收码
     */
    public void showSignCode() {
        inputType = INPUT_DEFAULT;
        tvTitle.setText("请输入签收码");
        tvMessage.setText(R.string.sign_up_message);
        tvBtnConfirm.setVisibility(View.GONE);
        pePickUp.setFocus();
        show();
        new KeyboardUtil(mContext).showSoftInput(pePickUp.getEditText(), false);
    }
}
