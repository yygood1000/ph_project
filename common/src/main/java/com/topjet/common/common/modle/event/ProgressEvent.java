package com.topjet.common.common.modle.event;

import com.topjet.common.base.model.BaseEvent;

/**
 * 下载进度
 * Created by tsj004 on 2017/8/3.
 */

public class ProgressEvent extends BaseEvent {

    private int progress;
    public ProgressEvent(boolean success, int progress){
        super.success=success;
        this.progress = progress;
    }

    public int getProgress(){
        return progress;
    }

}
