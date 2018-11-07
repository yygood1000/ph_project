package com.topjet.common.im.model.params;

import java.util.List;

/**
 * creator: zhulunjun
 * time:    2017/12/11
 * describe: 用户id集合
 */

public class UserIdListParams {

    private String user_list;    // 是	String	用逗号拼接多个userId	560id

    public UserIdListParams(List<String> userIds) {
        setUser_list(userIds);
    }

    public String getUser_list() {
        return user_list;
    }

    public void setUser_list(List<String> userIds) {
        if(userIds == null || userIds.size() == 0){
            this.user_list = "";
            return;
        }
        StringBuilder stringBuffer = new StringBuilder();
        for (String id : userIds) {
            stringBuffer.append(id);
            stringBuffer.append(",");
        }
        this.user_list = stringBuffer.substring(0, stringBuffer.length() - 1);
    }
}
