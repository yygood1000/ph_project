package com.topjet.common.contact.view;

import com.topjet.common.base.view.activity.IView;
import com.topjet.common.contact.model.ContactBean;

import java.util.List;

/**
 * creator: zhulunjun
 * time:    2017/10/17
 * describe:
 */

public interface ContactView extends IView {

    void setContactData(List<ContactBean> data);
    void emptyData();
}
