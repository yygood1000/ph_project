package com.topjet.common.user.modle.extra;

import com.topjet.common.base.model.BaseExtra;

/**
 * Created by tsj-004 on 2017/10/25.
 * 进入实名认证界面传递的数据
 */

public class GoToAuthenticationExtra extends BaseExtra{
    public final static int IN_TYPE_ADD = 0;        // 进入类型，还没认证过，添加认证。跳多界面
    public final static int IN_TYPE_SHOW = 1;        // 进入类型，添加过认证，显示认证。跳单界面
    private int inType = IN_TYPE_ADD;              // 进入类型

    public GoToAuthenticationExtra() {
    }

    public GoToAuthenticationExtra(int type) {
        this.inType = type;
    }

    public int getInType() {
        return inType;
    }

    public void setInType(int inType) {
        this.inType = inType;
    }
}