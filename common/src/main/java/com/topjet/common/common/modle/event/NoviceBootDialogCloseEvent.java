package com.topjet.common.common.modle.event;

import com.topjet.common.base.model.BaseEvent;

/**
 * Created by yy on 2017/11/27.
 * 新手引导页关闭事件
 */

public class NoviceBootDialogCloseEvent extends BaseEvent {
    public NoviceBootDialogCloseEvent() {
        super.setSuccess(true);
    }
}
