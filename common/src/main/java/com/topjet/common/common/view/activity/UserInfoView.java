package com.topjet.common.common.view.activity;

import com.topjet.common.base.view.activity.IListView;
import com.topjet.common.common.modle.bean.UserInfo;
import com.topjet.common.common.modle.response.UserInfoResponse;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.utils.StringUtils;

/**
 * creator: zhulunjun
 * time:    2017/11/13
 * describe: 诚信查询
 */

public interface UserInfoView<D> extends IListView<D> {

    void setUserInfo(UserInfo userInfo);

    /**
     * 设置长跑城市
     */
    void setBusinessLine(String lines);


}
