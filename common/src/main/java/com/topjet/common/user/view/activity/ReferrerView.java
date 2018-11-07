package com.topjet.common.user.view.activity;

import com.topjet.common.base.view.activity.IView;
import com.topjet.common.user.modle.bean.ReferrerInfoBean;

/**
 * Created by yy on 2017/11/6.
 * 推荐人
 */

public interface ReferrerView extends IView {
    void showRefrererInfo(ReferrerInfoBean data);
}
