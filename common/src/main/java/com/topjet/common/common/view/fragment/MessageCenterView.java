package com.topjet.common.common.view.fragment;

import com.topjet.common.base.view.activity.IView;
import com.topjet.common.common.modle.response.MessageCenterCountResponse;

/**
 * Created by yy on 2017/8/14.
 * 消息中心  2017/12/07 zlj
 */

public interface MessageCenterView extends IView {
    void setUnReadCount(MessageCenterCountResponse response);
}
