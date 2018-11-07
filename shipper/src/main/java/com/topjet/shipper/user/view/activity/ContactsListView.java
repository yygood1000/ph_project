package com.topjet.shipper.user.view.activity;

import android.os.Bundle;

import com.topjet.common.base.view.activity.IListView;
import com.topjet.common.base.view.activity.IView;
import com.topjet.common.user.modle.params.UsualContactListItem;
import com.topjet.shipper.user.view.adapter.ContactsListAdapter;

/**
 * 常用联系人列表
 * Created by tsj004 on 2017/8/22.
 */

public interface ContactsListView<D> extends IListView<D> {

    void deleteItem(UsualContactListItem item, int position);
}
