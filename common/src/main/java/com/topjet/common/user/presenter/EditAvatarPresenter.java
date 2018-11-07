package com.topjet.common.user.presenter;

import android.app.Activity;
import android.content.Context;

import com.topjet.common.base.listener.ObserverOnResultListener;
import com.topjet.common.base.presenter.BaseApiPresenter;
import com.topjet.common.user.modle.params.EditAvatarParams;
import com.topjet.common.user.modle.response.GetAvatarInfoResponse;
import com.topjet.common.user.modle.serverAPI.UserCommand;
import com.topjet.common.user.modle.serverAPI.UserCommandAPI;
import com.topjet.common.user.view.activity.EditAvatarView;
import com.topjet.common.utils.ImageUtil;

/**
 * 修改头像
 * Created by tsj-004 on 2017/11/9.
 */

public class EditAvatarPresenter extends BaseApiPresenter<EditAvatarView, UserCommand> {

    public EditAvatarPresenter(EditAvatarView mView, Context mContext) {
        super(mView, mContext);
        mApiCommand = new UserCommand(UserCommandAPI.class, mActivity);
    }

    /**
     * 获取头像信息
     */
    public void getAvatarInfo() {
        mApiCommand.getAvatarInfo(new ObserverOnResultListener<GetAvatarInfoResponse>() {
            @Override
            public void onResult(GetAvatarInfoResponse response) {
                mView.showViewByParamas(response);
            }
        });
    }

    /**
     * 修改头像
     */
    public void editAvatarInfo(String path) {
        EditAvatarParams params = new EditAvatarParams();
        params.setUser_icon(ImageUtil.getBase64With480x800(path));
        mApiCommand.editAvatarInfo(params, new ObserverOnResultListener<Object>() {
            @Override
            public void onResult(Object object) {
                mActivity.setResultAndFinish(Activity.RESULT_OK);
            }
        });
    }
}
