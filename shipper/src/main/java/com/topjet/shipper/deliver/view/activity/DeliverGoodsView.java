package com.topjet.shipper.deliver.view.activity;

import com.topjet.common.base.view.activity.IView;
import com.topjet.common.order_detail.modle.extra.CityAndLocationExtra;
import com.topjet.shipper.deliver.modle.extra.OtherInfoExtra;
import com.topjet.shipper.deliver.modle.params.OwnerGoodsParams;

/**
 * Created by tsj-004 on 2017/8/22.
 * 发货
 */

public interface DeliverGoodsView extends IView {
    void showAddress(int curAddressType, CityAndLocationExtra extra, boolean needSetValue);        //
    // 显示地址。needSetValue是否将extra填充到p里的extra中
    void showDistance(String distance);                             // 显示距离

    void clickTvUnit(String text);      // 点击tvUnit

    void setTypeAndLengthText(String txt);   // 显示车型车长

    void setPayWayText(int position);        // 设置支付方式文字 等操作

    void setTakeTimeText(String loadDateType, String loadDate);   // 设置提货时间文本等操作

    void refreViewsByParams(String quantity_type, String quantity_min, String quantity_max, String unit);      //
    // 根据id请求到货源详情后刷新界面

    void setOtherInfo(OtherInfoExtra otherInfoExtra);

    int getSelectedUnitId();             // 获取单位选中的id

    int getSelectedPayWayIndex();        // 获取支付方式选中的id

    void onClickSumbmitError(boolean success, String errorMsg, OwnerGoodsParams params);     // 点击提交时，是否有字段未填写

    void clearetAmountText();   // 清空数量输入框
}