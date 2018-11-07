package com.topjet.common.user.modle.event;

import com.topjet.common.base.model.BaseEvent;
import com.topjet.common.user.modle.response.IdResponse;

/**
 * 添加联系人
 * Created by tsj004 on 2017/8/28.
 */

public class AddLinkManEvent extends BaseEvent {

    private IdResponse response;

    public AddLinkManEvent(){

    }

    public void setResponse(IdResponse response){
        this.response = response;
    }

    public IdResponse getResponse(){
        return response;
    }
}
