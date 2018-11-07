package com.topjet.shipper.deliver.modle.bean;

import com.topjet.common.resource.bean.OptionItem;

/**
 * Created by tsj-004 on 2017/9/1.
 * 单位和状态，状态用来设置背景色和文字颜色
 */

public class UnitAndState {
    private OptionItem item;
    private int state;      // 0 绿底 选中  1 绿底 未选中 2 白底 选中 3 白底 未选中
    private int id;

    public OptionItem getItem() {
        return item;
    }

    public void setItem(OptionItem item) {
        this.item = item;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
