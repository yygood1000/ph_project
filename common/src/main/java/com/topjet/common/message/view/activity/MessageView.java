package com.topjet.common.message.view.activity;

import com.topjet.common.base.view.activity.IListView;

/**
 *
 * Created by tsj028 on 2017/12/4 0004.
 */

public interface MessageView<D> extends IListView<D> {
    void setTitle(String title);
}
