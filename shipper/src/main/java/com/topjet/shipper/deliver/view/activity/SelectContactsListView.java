package com.topjet.shipper.deliver.view.activity;

import com.topjet.common.base.view.activity.IView;
import com.topjet.shipper.deliver.view.adapter.SelectContactsListAdapter;

/**
 * 选择联系人
 * Created by tsj004 on 2017/8/25.
 */

public interface SelectContactsListView extends IView {

    void setAdapter(SelectContactsListAdapter adapter);

    void onRefreshFinish(SelectContactsListAdapter adapter);

    void setRecyclerViewVisibility(int visibility);

    void setllNoDataVisibility(int visibility);

    void refreshDataFinish();
}
