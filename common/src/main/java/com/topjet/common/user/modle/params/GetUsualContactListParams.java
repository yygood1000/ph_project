package com.topjet.common.user.modle.params;


import java.util.List;

/**
 * 常用联系人列表返回数据类
 * Created by tsj004 on 2017/8/24.
 */

public class GetUsualContactListParams {
    private List<UsualContactListItem> selectLinkmanListDTOs;

    public List<UsualContactListItem> getSelectLinkmanListDTOs() {
        return selectLinkmanListDTOs;
    }

}
