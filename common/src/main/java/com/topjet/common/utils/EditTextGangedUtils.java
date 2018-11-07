package com.topjet.common.utils;

import android.text.InputType;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.topjet.common.R;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.config.CMemoryData;

import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;

/**
 * Created by yy on 2017/10/31.
 * <p>
 * EditText 联动工具类
 */

public class EditTextGangedUtils {

    /**
     * 输入框 与 删除按钮 联动（有字符，显示删除按钮）
     *
     * @param et       输入框
     * @param ivDelete 删除按钮
     */
    public static void setEtLengthWithDelete(MvpActivity mActivity, EditText et, final ImageView ivDelete) {
        RxTextView.textChanges(et)
                .compose(mActivity.<CharSequence>bindToLifecycle())
                .subscribe(new Consumer<CharSequence>() {
                    @Override
                    public void accept(CharSequence charSequence) throws Exception {
                        RxView.visibility(ivDelete).accept(charSequence.length() > 0);
                    }
                });
    }

    /**
     * 输入框 与 发送按钮 联动（长度等于11，且显示的是获取验证码才可点击）
     *
     * @param et         输入框
     * @param tvSendCode 发送验证码
     */
    public static void setEtLengthWithSendEnable(MvpActivity mActivity, EditText et, final TextView tvSendCode) {
        RxTextView.textChanges(et)
                .compose(mActivity.<CharSequence>bindToLifecycle())
                .subscribe(new Consumer<CharSequence>() {
                    @Override
                    public void accept(CharSequence charSequence) throws Exception {
                        RxView.enabled(tvSendCode).accept((charSequence.length() == 11
                                && (tvSendCode.getText().equals(ResourceUtils.getString(R.string
                                .get_verification_code))) || tvSendCode.getText().equals(ResourceUtils.getString(R
                                .string.voice_verification_code))));
                    }
                });
    }

    /**
     * 输入框密文明文显示切换监听
     */
    public static void setEtInputTypeWithImge(EditText et, ImageView iv) {
        if (et.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {  // 目前为 可见
            if (CMemoryData.isDriver()) {
                iv.setImageResource(R.drawable.signin_icon_closeeye_nor_blue);
            } else {
                iv.setImageResource(R.drawable.signin_icon_closeeye_nor_green);
            }
            et.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        } else {
            if (CMemoryData.isDriver()) {
                iv.setImageResource(R.drawable.signin_icon_openeye_high);
            } else {
                iv.setImageResource(R.drawable.signin_icon_openeye_high_green);
            }
            et.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }
        et.setSelection(et.getText().length());     //设置光标位置
    }


    public static void setEnableWithDoubleEt(MvpActivity mActivity, EditText etMobile, EditText checkCode, final
    TextView tv) {
        // 多个文本框变化监听
        Observable<CharSequence> ObservableName = RxTextView.textChanges(etMobile);
        Observable<CharSequence> ObservablePassword = RxTextView.textChanges(checkCode);
        Observable.combineLatest(ObservableName, ObservablePassword, new BiFunction<CharSequence, CharSequence,
                Boolean>() {
            @Override
            public Boolean apply(CharSequence phoneNum, CharSequence password) throws
                    Exception {
                return phoneNum.length() == 11 && password.length() >= 6;
            }
        }).compose(mActivity.<Boolean>bindToLifecycle())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        RxView.enabled(tv).accept(aBoolean);
                    }
                });
    }

    public static void setEnableWithDoubleEtPassword(MvpActivity mActivity, EditText etPassword1, EditText
            etPassword2, final TextView tv) {
        // 多个文本框变化监听
        Observable<CharSequence> ObservableName = RxTextView.textChanges(etPassword1);
        Observable<CharSequence> ObservablePassword = RxTextView.textChanges(etPassword2);
        Observable.combineLatest(ObservableName, ObservablePassword, new BiFunction<CharSequence, CharSequence,
                Boolean>() {
            @Override
            public Boolean apply(CharSequence password1, CharSequence password2) throws
                    Exception {
                return password1.length() >= 6 && password2.length() >= 6;
            }
        }).compose(mActivity.<Boolean>bindToLifecycle())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        RxView.enabled(tv).accept(aBoolean);
                    }
                });
    }
}
