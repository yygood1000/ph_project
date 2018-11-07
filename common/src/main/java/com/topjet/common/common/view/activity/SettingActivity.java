package com.topjet.common.common.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.topjet.common.R;
import com.topjet.common.R2;
import com.topjet.common.base.CommonProvider;
import com.topjet.common.base.dialog.AutoDialogHelper;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.common.modle.response.AppUpgradeResponse;
import com.topjet.common.common.presenter.SettingPresenter;
import com.topjet.common.common.presenter.SystemPresenter;
import com.topjet.common.common.view.dialog.UpdateDialog;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.user.modle.serverAPI.UserCommand;
import com.topjet.common.user.modle.serverAPI.UserCommandAPI;
import com.topjet.common.user.view.activity.AboutActivity;
import com.topjet.common.user.view.activity.ChangePasswordActivity;
import com.topjet.common.user.view.activity.ReferrerActivity;
import com.topjet.common.utils.CacheUtils;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.StringUtils;
import com.topjet.common.utils.Toaster;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

/**
 * Created by yy on 2017/11/2.
 * <p>
 * 设置页面
 */

public class SettingActivity extends MvpActivity<SettingPresenter> implements SettingView {
    @BindView(R2.id.tv_referrer)
    TextView tvReferrer;
    @BindView(R2.id.tv_app_version)
    TextView tvAppVersion;
    @BindView(R2.id.tv_cache_size)
    TextView tvCacheSize;
    @BindView(R2.id.rl_referrer)
    RelativeLayout rlReferrer;
    @BindView(R2.id.tv_quit_account)
    TextView tvQuitAccount;

    String cachSize;
    @BindView(R2.id.rl_change_text_size)
    RelativeLayout rlChangeTextSize;

    @Override
    protected void initPresenter() {
        mPresenter = new SettingPresenter(this, mContext, new UserCommand(UserCommandAPI.class, this));
    }

    @Override
    protected void initTitle() {
        super.initTitle();
        getMyTitleBar().setTitleText("设置");
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView() {
        // 设置推荐人
        applyReferrer();
        // 检测缓存大小
        setCacheSize();
        // 设置当前版本
        resetBtnBackGround();
    }

    /**
     * 设置缓存大小
     */
    public void setCacheSize() {
        try {
            cachSize = CacheUtils.getInstance().getTotalCacheSize(this);
            tvCacheSize.setText(cachSize);
        } catch (Exception e) {
            e.printStackTrace();
            Logger.d("获取缓存失败 " + e.getMessage());
        }
    }

    /**
     * 设置退出APP按钮背景色
     */
    private void resetBtnBackGround() {
        if (CMemoryData.isDriver()) {
            tvQuitAccount.setBackgroundResource(R.drawable.selector_btn_square_blue);
        } else {
            rlChangeTextSize.setVisibility(View.GONE);
        }
    }

    /**
     * 检查推荐人是否是空的
     */
    public void applyReferrer() {
        if (StringUtils.isNotBlank(mPresenter.extra.getReferrerName())) {
            tvReferrer.setText(mPresenter.extra.getReferrerName());
        } else {
            rlReferrer.setVisibility(View.GONE);
        }
    }

    @OnClick({R2.id.rl_change_password, R2.id.rl_change_text_size, R2.id.rl_referrer, R2.id.rl_about_us, R2.id
            .rl_check_updata, R2.id.rl_clear_cache, R2.id.tv_quit_account})
    public void onViewClicked(View view) {
        int i = view.getId();
        if (i == R.id.rl_change_password) {
            // 修改密码
            turnToActivity(ChangePasswordActivity.class);
        } else if (i == R.id.rl_change_text_size) {
            // 修改字体大小
            CommonProvider.getInstance().getJumpDriverProvider().jumpChangeSize(this);
        } else if (i == R.id.rl_referrer) {
            // 推荐人
            turnToActivity(ReferrerActivity.class);
        } else if (i == R.id.rl_about_us) {
            // 关于我们
            turnToActivity(AboutActivity.class);
        } else if (i == R.id.rl_check_updata) {
            // 检查升级
            new SystemPresenter(this, this).checkAppUpgrade(true);
        } else if (i == R.id.rl_clear_cache) {
            //清除缓存
            if (cachSize.equals("0K")) {
                Toaster.showShortToast("已经很干净了!无须清除");
                return;
            }
            AutoDialogHelper.showContent(this, "缓存大小为" + cachSize + "，确定要清理缓存吗？",
                    new AutoDialogHelper.OnConfirmListener() {
                        @Override
                        public void onClick() {
                            clearCache();
                        }
                    }).show();
        } else if (i == R.id.tv_quit_account) {
            // 退出账号
            AutoDialogHelper.showContent(this, "确定退出登录吗？",
                    new AutoDialogHelper.OnConfirmListener() {
                        @Override
                        public void onClick() {
                            mPresenter.doLogout();
                        }
                    }).show();
        }
    }

    /**
     * 清除缓存
     */
    private void clearCache() {

        showLoadingDialog();
        CacheUtils.getInstance().clearCache(SettingActivity.this)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        cancelShowLoadingDialog();
                        if (aBoolean) {
                            Toaster.showShortToast("清除成功");
                            setCacheSize();
                        } else {
                            Toaster.showShortToast("清除失败");
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        cancelShowLoadingDialog();
                        Toaster.showShortToast("清除失败");
                    }
                });


    }

    @Override
    public void showUpdateDialog(AppUpgradeResponse result) {
        //有更新
        new UpdateDialog(this, result).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
