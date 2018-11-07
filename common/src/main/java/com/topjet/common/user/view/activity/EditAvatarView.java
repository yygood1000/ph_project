package com.topjet.common.user.view.activity;

import com.topjet.common.base.view.activity.IView;
import com.topjet.common.user.modle.response.GetAvatarInfoResponse;

/**
 * 修改头像
 * Created by tsj-004 on 2017/11/9.
 */

public interface EditAvatarView extends IView {
    void showViewByParamas(GetAvatarInfoResponse response);
}
