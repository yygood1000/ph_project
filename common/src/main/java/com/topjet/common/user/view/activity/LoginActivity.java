package com.topjet.common.user.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.topjet.common.R;
import com.topjet.common.R2;
import com.topjet.common.base.dialog.AutoDialog;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.common.modle.response.AppUpgradeResponse;
import com.topjet.common.common.presenter.SystemPresenter;
import com.topjet.common.common.view.dialog.UpdateDialog;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.config.CPersisData;
import com.topjet.common.user.modle.bean.UserAccountsBeBanned;
import com.topjet.common.user.modle.extra.PhoneExtra;
import com.topjet.common.user.modle.serverAPI.UserCommand;
import com.topjet.common.user.modle.serverAPI.UserCommandAPI;
import com.topjet.common.user.presenter.LoginPresenter;
import com.topjet.common.user.presenter.RegisterPresenter;
import com.topjet.common.utils.ApplyUtils;
import com.topjet.common.utils.CallPhoneUtils;
import com.topjet.common.utils.EditTextGangedUtils;
import com.topjet.common.utils.ResourceUtils;
import com.topjet.common.utils.StringUtils;
import com.topjet.common.utils.TimeUtils;
import com.topjet.common.utils.Toaster;

import butterknife.BindView;
import io.reactivex.functions.Consumer;

/**
 * 登录界面
 */
public class LoginActivity extends MvpActivity<LoginPresenter> implements LoginView, View.OnClickListener {
    // 左上角，当前的登录方式
    @BindView(R2.id.tv_cur_login_way)
    TextView tvCurLoginWay;
    // 右上角切换登录方式
    @BindView(R2.id.tv_other_way)
    TextView tvOtherWay;

    @BindView(R2.id.iv_other_way)
    ImageView ivOtherWay;
    @BindView(R2.id.et_phonenumber)
    EditText etPhonenumber;
    @BindView(R2.id.iv_delete_phonenumber)
    ImageView ivDeletePhonenumber;
    @BindView(R2.id.tv_send_code)
    TextView tvSendCode;
    @BindView(R2.id.et_password)
    EditText etPassword;
    @BindView(R2.id.iv_see_password)
    ImageView ivSeePassword;
    @BindView(R2.id.rl_password)
    RelativeLayout rlPassword;
    @BindView(R2.id.et_check_code)
    EditText etCheckCode;
    @BindView(R2.id.tv_login)
    TextView tvLogin;
    @BindView(R2.id.tv_forget_password)
    TextView tvForgetPassword;
    @BindView(R2.id.tv_register)
    TextView tvRegister;
    @BindView(R2.id.rl_forget_password)
    RelativeLayout rlForgetPassword;
    @BindView(R2.id.tv_register2)
    TextView tvRegister2;
    @BindView(R2.id.tv_voice_code)
    TextView tvVoiceCode;
    @BindView(R2.id.rl_send_voice)
    RelativeLayout rlSendVoice;

    /**
     * 登录方式，外部传入
     */
    private final static String LOGIN_WAY = "LOGIN_WAY";
    private final static String EXTRA_PHONE = "PHONE";
    private final static String LOGIN_WAY_PASSWORD = "LOGIN_WAY_PASSWORD";  // 密码登录
    private final static String LOGIN_WAY_CODE = "LOGIN_WAY_CODE";  // 验证码登录
    public final static String ACCOUNTS_BE_BANNED_INFO = "USER_ACCOUNTS_BE_BANNED";   // 用户被冻结信息

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new LoginPresenter(this, this, new UserCommand(UserCommandAPI.class, this));
    }

    String aes  = "uNaDkQHyrGE7MEV7Nc9+NibpiSyAhYxwpzo8j/Y7gacxrnvF9viOHIa2yf4FMUs0mA7v9/3NaVCsC7Bglg8+e/JvZelhR8qC0SMyMLFaYoH+2zCNRs2ScdzHJLkl8mkouxKNc9y4gICusgks450rD8W8AWJ8wzvEkx+TNaM3A38nFEAGI3/etBaM/NIb6eDOF8FI7uJRca9V1oZKo3grqbK5TqqZlAvIyFNMGKILxjTcWDBbdUP6TLVccjapUKAcpRqor8+cKPF1qGzMsT2Eomy8bc+Bwd3OASPtELhL2h7KIIKNe/HUvvnYkNGwftzl5NtU1O+FvgYWFd0ZvoehB2/nUGVXi26/pn1PdS5REchS5CoQ7agaFWscXtXq1VWsB9i/CtsbQXkNUFWGL3kJuIhImwRBdWeC8ShGHN8Yaz8jI6SG/Ivw8KvLoJy0Ode5rEN8J8v19hmWrOzb0ao8Zgfn/F+41FOjtVu9pZ6uG9S7B3RSdBj1Qy4nEaxWbArIpqRIYuM6C2fNGH17IMkzTW+nA/ZDuFDxbosv46MjfttvyGDAh3z77/nc9XhWb3DzUd7qBWsquOwKW+ar55FFu3HFauZCMOdhHCchPDp7HTtjQvscApwPta5mZVpxQA+6zGLKiqOZKgRJVRN9v+TNbj7QW46fN9MlCN3/tJlnkq8a3KyxpVj10X+gstcwSOd0I7YKR1bmr0aw+32Otvt4bCwXUV5LUjJ1FW7K8absZYm6CivICswSkhK2qJ+UWFq3asbd/JASqJIppAJvW+dkbtWwGoxHFo2sHi0DfJTIJXvKiYgj/WfWMKYsh5AdLDak/D0h5fGHG5M2fzJ1PuAaFg==";

    @Override
    protected void initView() {
        if (CMemoryData.isDriver()) {
            setStatusBarViewDrawableResource(R.drawable.shape_bg_gradient_driver);
        } else {
            setStatusBarViewDrawableResource(R.drawable.shape_bg_gradient_shipper);
        }
        getMyTitleBar().hideToolBar();

        tvOtherWay.setOnClickListener(this);
        ivOtherWay.setOnClickListener(this);
        ivDeletePhonenumber.setOnClickListener(this);
        ivSeePassword.setOnClickListener(this);
        tvLogin.setOnClickListener(this);
        tvForgetPassword.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
        tvRegister2.setOnClickListener(this);
        tvSendCode.setOnClickListener(this);
        tvVoiceCode.setOnClickListener(this);

        //设置按钮、文本框默认颜色
        setDefaultColor();
        //切换登录模式，默认调用一次，切换到密码登录模
        mPresenter.changeLoginWay();

        // 手机号输入长度大于0 且在 密码登录模式下 就显示清除按钮
        setPhoneLengthWithDelete(etPhonenumber, ivDeletePhonenumber);
        EditTextGangedUtils.setEtLengthWithSendEnable(this, etPhonenumber, tvSendCode);
        EditTextGangedUtils.setEtLengthWithSendEnable(this, etPhonenumber, tvVoiceCode);

        // 自动填写上次登录过的账号
        etPhonenumber.setText(CPersisData.getUserMobile());
        // 关闭听单、定位、悬浮窗等服务
        mPresenter.stopServices();


//        Logger.d("解密  "+ AESEncodeUtil.decrypt(aes));
    }


    @Override
    protected void initData() {
        // 检查版本
        new SystemPresenter(this, mContext).checkAppUpgrade();

        // 对传入参数进行逻辑处理
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                String phone = bundle.getString(EXTRA_PHONE);
                if (StringUtils.isNotBlank(phone)) {
                    etPhonenumber.setText(phone);
                }

                String extraLoginWay = bundle.getString(LOGIN_WAY);
                if (StringUtils.isNotBlank(extraLoginWay)) {
                    if (extraLoginWay.equalsIgnoreCase(LOGIN_WAY_PASSWORD)) {
                        mPresenter.setCurLoginModeToPass();
                    } else if (extraLoginWay.equalsIgnoreCase(LOGIN_WAY_CODE)) {
                        mPresenter.setCurLoginModeToCode();
                    }
                }

                UserAccountsBeBanned accountsBeBannedInfo = (UserAccountsBeBanned) bundle.getSerializable
                        (ACCOUNTS_BE_BANNED_INFO);
                if (accountsBeBannedInfo != null) {
                    String time = TimeUtils.showTimeDaySecond(accountsBeBannedInfo.getCongeal_time());
                    showUserBeBanned(time, accountsBeBannedInfo.getCongeal_remark());
                }
            }
        }
    }

    /**
     * 手机号输入长度大于0 且在 密码登录模式下 就显示清除按钮
     */
    public void setPhoneLengthWithDelete(EditText etPhonenumber, final ImageView ivDeletePhonenumber) {
        // 单个输入框变化监听
        RxTextView.textChanges(etPhonenumber)
                .compose(this.<CharSequence>bindToLifecycle())
                .subscribe(new Consumer<CharSequence>() {
                    @Override
                    public void accept(CharSequence charSequence) throws Exception {
                        RxView.visibility(ivDeletePhonenumber).accept(charSequence.length() > 0
                                && mPresenter.curLoginMode % 2 == 0);
                    }
                });
    }

    /**
     * 页面的点击事件
     */
    public void onClick(View v) {
        super.onClick(v);

        int id = v.getId();
        if (id == R.id.tv_login) {
            // 去登录
            mPresenter.doLogin(etPhonenumber, etPassword, etCheckCode);
        } else if (id == R.id.tv_other_way || id == R.id.iv_other_way) {
            // 切换登录模式
            mPresenter.changeLoginWay();
        } else if (id == R.id.iv_delete_phonenumber) {
            // 清空手机号
            etPhonenumber.setText("");
        } else if (id == R.id.iv_see_password) {
            // 切换密码是否可见
            EditTextGangedUtils.setEtInputTypeWithImge(etPassword, ivSeePassword);
        } else if (id == R.id.tv_register || id == R.id.tv_register2) {
            // 注册
            Bundle bundle = new Bundle();
            bundle.putString("phoneNumber", etPhonenumber.getText().toString());
            turnToActivity(RegisterActivity.class, bundle);
        } else if (id == R.id.tv_send_code) {
            // 发送验证码
            if (tvSendCode.getText().toString().equals(ResourceUtils.getString(R.string.get_verification_code))) {
                String phoneNumber = etPhonenumber.getText().toString();
                if (ApplyUtils.validateMobile(phoneNumber)) {
                    CMemoryData.setUserMobile(phoneNumber);
                    CPersisData.setUserMobile(phoneNumber);
                    mPresenter.startCountDown(tvSendCode, tvVoiceCode, RegisterPresenter.GET_VERIFY_CODE);
                } else {
                    Toaster.showLongToast(R.string.please_input_correct_phone_number);
                }
            }
        } else if (id == R.id.tv_voice_code) {
            // 发送语音验证码
            showVoiceVerifyCodeDialog();
        } else if (id == R.id.tv_forget_password) {
            // 忘记密码
            goToForgotPasswordActivity();
        }
    }

    /**
     * 显示语音验证码弹窗
     */
    public void showVoiceVerifyCodeDialog() {
        final AutoDialog dialog = new AutoDialog(mContext);
        dialog.setTitle(ResourceUtils.getString(R.string.is_get_voice_verify_code));
        dialog.setOnBothConfirmListener(new AutoDialog.OnBothConfirmListener() {
            @Override
            public void onLeftClick() {
                dialog.toggleShow();
            }

            @Override
            public void onRightClick() {
                mPresenter.startCountDown(tvSendCode, tvVoiceCode, RegisterPresenter.GET_VOICE_VERIFY_CODE);
                dialog.toggleShow();
            }
        });
        dialog.toggleShow();
    }

    /**
     * 设置各种按钮和字体的默认颜色
     * 货主绿色
     * 司机蓝色
     */
    public void setDefaultColor() {
        if (CMemoryData.isDriver()) {
            etPhonenumber.setBackgroundResource(R.drawable.selector_login_edittext_bg_blue);
            etPassword.setBackgroundResource(R.drawable.selector_login_edittext_bg_blue);
            etCheckCode.setBackgroundResource(R.drawable.selector_login_edittext_bg_blue);
            ivSeePassword.setImageResource(R.drawable.signin_icon_closeeye_nor_blue);
            tvLogin.setBackgroundResource(R.drawable.selector_btn_corner_blue);
            tvSendCode.setBackgroundResource(R.drawable.selector_btn_corner_blue);
            tvVoiceCode.setTextColor(mContext.getResources().getColor(R.color.v3_common_blue));
        } else {
            etPhonenumber.setBackgroundResource(R.drawable.selector_login_edittext_bg_green);
            etPassword.setBackgroundResource(R.drawable.selector_login_edittext_bg_green);
            etCheckCode.setBackgroundResource(R.drawable.selector_login_edittext_bg_green);
            ivSeePassword.setImageResource(R.drawable.signin_icon_closeeye_nor_green);
            tvLogin.setBackgroundResource(R.drawable.selector_btn_corner_green);
            tvSendCode.setBackgroundResource(R.drawable.selector_btn_corner_green);
            tvVoiceCode.setTextColor(mContext.getResources().getColor(R.color.color_17B745));
        }
    }

    /**
     * 切换登录模式
     */
    public void changeLoginWay(int curLoginMode) {
        if (curLoginMode % 2 == 0) {  // 密码登录模式
            tvCurLoginWay.setText(R.string.password_login);
            tvOtherWay.setText(R.string.verification_code_login);
            rlPassword.setVisibility(View.VISIBLE);
            etCheckCode.setVisibility(View.GONE);
            tvForgetPassword.setVisibility(View.VISIBLE);
            rlSendVoice.setVisibility(View.GONE);
            rlForgetPassword.setVisibility(View.VISIBLE);
            tvSendCode.setVisibility(View.GONE);
        } else {        // 验证码登录模式
            tvCurLoginWay.setText(R.string.verification_code_login);
            tvOtherWay.setText(R.string.password_login);
            rlPassword.setVisibility(View.GONE);
            etCheckCode.setVisibility(View.VISIBLE);
            tvForgetPassword.setVisibility(View.GONE);
            rlSendVoice.setVisibility(View.VISIBLE);
            rlForgetPassword.setVisibility(View.GONE);
            tvSendCode.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 自动发送验证码
     */
    @Override
    public void autoSendCode() {
        onClick(tvSendCode);
    }

    /**
     * 转到找回密码页面
     */
    private void goToForgotPasswordActivity() {
        Bundle bundle = new Bundle();
        bundle.putString("phoneNumber", etPhonenumber.getText().toString());
        turnToActivity(ForgotPasswordActivity.class, bundle);
    }

    /**
     * 没有注册，提示注册
     */
    public void showUserNotRegisterDialog(String title) {
        final AutoDialog dialog = new AutoDialog(mContext);
        dialog.setTitle(title);
        dialog.setLeftText(R.string.cancel);
        dialog.setRightText(R.string.register_now);
        dialog.setOnBothConfirmListener(new AutoDialog.OnBothConfirmListener() {
            @Override
            public void onLeftClick() {
                dialog.toggleShow();
            }

            @Override
            public void onRightClick() {
                turnToActivity(RegisterActivity.class, new PhoneExtra(etPhonenumber.getText().toString()));
                dialog.toggleShow();
            }
        });
        dialog.toggleShow();
    }

    /**
     * 展示用户被禁止登录弹窗
     */
    public void showUserBeBanned(String time, String reason) {
        final AutoDialog dialog = new AutoDialog(mContext, R.layout.pop_user_be_banned);
        ((TextView) dialog.findViewById(R.id.tv_banned_time)).setText("冻结时间：" + time);
        ((TextView) dialog.findViewById(R.id.tv_banned_reason)).setText("冻结原因：" + reason);
        ((TextView) dialog.findViewById(R.id.tv_confirm)).getPaint().setFakeBoldText(true);  // 联系客服，字体加粗
        dialog.findViewById(R.id.tv_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.toggleShow();
                new CallPhoneUtils().callCustomerService(LoginActivity.this);
            }
        });
        dialog.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.toggleShow();
            }
        });
        dialog.toggleShow();
    }

    /**
     * 显示升级弹窗
     */
    @Override
    public void showUpdateDialog(AppUpgradeResponse result) {
        if (!CMemoryData.isIsShowedUpdataDialog()) {
            CMemoryData.setIsShowedUpdataDialog();
            new UpdateDialog(mContext, result).show();
        }
    }

    /**
     * 直接跳转到密码登录
     * bundle中传入phone参数
     */
    public static void turnToPassLogin(MvpActivity mvpActivity, String phone) {
        Bundle bundle = new Bundle();
        bundle.putString(LOGIN_WAY, LOGIN_WAY_PASSWORD);
        bundle.putString(EXTRA_PHONE, phone);
        mvpActivity.turnToActivity(LoginActivity.class, bundle);
    }

    /**
     * 直接跳转到验证码登录
     * bundle中传入phone参数
     */
    public static void turnToCodeLogin(MvpActivity mvpActivity, String phone) {
        Bundle bundle = new Bundle();
        bundle.putString(LOGIN_WAY, LOGIN_WAY_CODE);
        bundle.putString(EXTRA_PHONE, phone);
        mvpActivity.turnToActivity(LoginActivity.class, bundle);
    }
}