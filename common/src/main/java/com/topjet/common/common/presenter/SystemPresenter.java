package com.topjet.common.common.presenter;

import android.content.Context;

import com.topjet.common.R;
import com.topjet.common.base.listener.ObserverOnResultListener;
import com.topjet.common.base.presenter.BaseApiPresenter;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.common.modle.response.AppUpgradeResponse;
import com.topjet.common.common.modle.serverAPI.SystemCommand;
import com.topjet.common.common.view.activity.SystemView;
import com.topjet.common.utils.Toaster;

/**
 * creator: zhulunjun
 * time:    2017/11/8
 * describe: 系统服务 逻辑抽离
 * 当一个接口多次使用时，可以抽离一个P和V,将调用逻辑抽离
 */

public class SystemPresenter extends BaseApiPresenter<SystemView, SystemCommand> {
    public SystemPresenter(SystemView mView, Context mContext) {
        super(mView, mContext, new SystemCommand((MvpActivity) mContext));
    }

    /**
     * 检测APP是否需要升级
     * @param isToast 无须升级是是否需要提示
     */

    public void checkAppUpgrade(final boolean isToast) {
        new SystemCommand(mActivity)
                .appUpgrade(new ObserverOnResultListener<AppUpgradeResponse>() {
                    @Override
                    public void onResult(AppUpgradeResponse result) {
                        if (result != null) {
                            if (result.getIsUpdate().equals("1")) {
                                //有更新
                                mView.showUpdateDialog(result);
                            } else if(isToast){
                                Toaster.showLongToast(mContext.getString(R.string.not_new_version));
                            }
                        }
                    }
                });
    }

    /**
     * 默认不需要提示
     */
    public void checkAppUpgrade() {
        checkAppUpgrade(false);
    }
}
