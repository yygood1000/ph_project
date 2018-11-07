package com.topjet.common.user.modle.event;

import com.topjet.common.base.model.BaseEvent;
import com.topjet.common.user.modle.params.UsualContactListItem;

/**
 * 获取常用联系人信息
 * Created by tsj004 on 2017/8/28.
 */

public class GetLinkManEvent extends BaseEvent {

    private UsualContactListItem response;

    public GetLinkManEvent(){

    }

    public void setResponse(UsualContactListItem response){
        this.response = response;
    }

    public UsualContactListItem getResponse(){
        return response;
    }
}
