package com.topjet.common.order_detail.view.dialog;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.topjet.common.R;
import com.topjet.common.base.dialog.BaseDialog;
import com.topjet.common.widget.PwdEditText;

import butterknife.ButterKnife;

/**
 * creator: zhulunjun
 * time:    2017/11/1
 * describe: 输入支付密码弹窗
 */

public class PayPasswordDialog extends BaseDialog implements View.OnClickListener{

    private PwdEditText pePickUp;


    public PayPasswordDialog(Context context) {
        super(context, R.layout.dialog_pay_password);
    }


    @Override
    public void initView() {
        super.initView();
        setShowBottom();

        initCodeEdit();

        view.findViewById(R.id.ll_one).setOnClickListener(this);
        view.findViewById(R.id.ll_two).setOnClickListener(this);
        view.findViewById(R.id.ll_three).setOnClickListener(this);
        view.findViewById(R.id.ll_four).setOnClickListener(this);
        view.findViewById(R.id.ll_five).setOnClickListener(this);
        view.findViewById(R.id.ll_six).setOnClickListener(this);
        view.findViewById(R.id.ll_seven).setOnClickListener(this);
        view.findViewById(R.id.ll_eight).setOnClickListener(this);
        view.findViewById(R.id.ll_nine).setOnClickListener(this);
        view.findViewById(R.id.ll_zero).setOnClickListener(this);
        view.findViewById(R.id.ll_delete).setOnClickListener(this);
        view.findViewById(R.id.ll_point).setOnClickListener(this);
        view.findViewById(R.id.iv_back).setOnClickListener(this);

    }

    /**
     * 初始化显示控件
     */
    private void initCodeEdit() {
        pePickUp = ButterKnife.findById(view, R.id.pe_pick_up);
        pePickUp.initStyle(R.drawable.edit_num_bg, 6, 0.5f, R.color.text_color_999999, R.color.text_color_222222, 28);
        pePickUp.setShowPwd(true);
        pePickUp.setOnTextFinishListener(new PwdEditText.OnTextFinishListener() {
            @Override
            public void onFinish(String str) {
                // 输入完成，直接触发回调
                if (confirmListener != null && !TextUtils.isEmpty(str)) {
                    confirmListener.onConfirm(str);
                    dismiss();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {

        int i = v.getId();
        if (i == R.id.ll_one) {
            input("1");
        }else if(i == R.id.ll_two){
            input("2");
        }else if(i == R.id.ll_three){
            input("3");
        }else if(i == R.id.ll_four){
            input("4");
        }else if(i == R.id.ll_five){
            input("5");
        }else if(i == R.id.ll_six){
            input("6");
        }else if(i == R.id.ll_seven){
            input("7");
        }else if(i == R.id.ll_eight){
            input("8");
        }else if(i == R.id.ll_nine){
            input("9");
        }else if(i == R.id.ll_zero){
            input("0");
        }else if(i == R.id.ll_point){

        }else if(i == R.id.ll_delete){
            delete();
        } else if(i == R.id.iv_back){
            dismiss();
        }

    }


    /**
     * 获取光标
     * @param editText
     */
    private void setFocus(EditText editText){
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
    }

    /**
     * 输入
     * @param str
     */
    private void input(String str){
        Editable editable = pePickUp.getEditable();
        int index = pePickUp.getEditText().getSelectionStart();
        if(editable == null) return;
        editable.insert(index, str);//插入文本
    }

    /**
     * 删除
     */
    private void delete(){
        Editable editable  = pePickUp.getEditable();
        int index = pePickUp.getEditText().getSelectionStart();
        if(editable == null || index == 0) return;//没有可以删除的
        editable.delete(index - 1, index);//删除文本
    }

    /**
     * 检查是否输入过小数点了
     * @param str
     * @return
     */
    private boolean checkPoint(String str){
        if (TextUtils.isEmpty(str))
            return true;
        else
            return !str.contains(".");

    }


    public interface ConfirmListener{
        void onConfirm(String text);
    }

    private ConfirmListener confirmListener;

    public PayPasswordDialog setConfirmListener(ConfirmListener confirmListener) {
        this.confirmListener = confirmListener;
        return this;
    }
}
