package com.topjet.shipper.deliver.view.activity;

import com.topjet.common.user.modle.params.UsualContactListItem;
import com.topjet.common.base.view.activity.IView;

import java.util.List;

/**
 * Created by tsj-004 on 2017/8/22.
 * 发货--其他信息
 */

public interface AddressInformationView extends IView {
    void showBackwards2Name(String backwards2Name);

    void refreListData(List<UsualContactListItem> usualContactListItems);      // 刷新联系人数据

    void setContact2Activity(UsualContactListItem item);     // 选择的联系人填充至界面
}