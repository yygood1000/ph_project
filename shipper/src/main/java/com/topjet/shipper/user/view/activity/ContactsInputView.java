package com.topjet.shipper.user.view.activity;

import com.topjet.common.base.view.activity.IView;
import com.topjet.common.order_detail.modle.extra.CityAndLocationExtra;

/**
 * 联系人添加或编辑
 * Created by tsj004 on 2017/8/28.
 */

public interface ContactsInputView extends IView {
    void setEditTextPhone(CharSequence text);
    void setEditTextLinkman(CharSequence text);
    void setTextViewCity(CharSequence text);
    void setTextViewAddress(CharSequence text);

    void setTitleText(boolean isAdd);

    void setCityAndLocationExtra(CityAndLocationExtra cityData);
}
