package com.topjet.shipper.deliver.modle.rxbus_modle;

import com.topjet.common.base.model.BaseEvent;
import com.topjet.shipper.deliver.modle.bean.OftenGoodsListItem;

import java.util.List;

/**
 * 常发货源列表
 * Created by tsj004 on 2017/8/29.
 */

public class OftenGoodsEvent extends BaseEvent {

    private List<OftenGoodsListItem> list;
    public OftenGoodsEvent(){

    }

    public List<OftenGoodsListItem> getList(){
        return list;
    }

    public void setList(List<OftenGoodsListItem> list){
        this.list = list;
    }

}
