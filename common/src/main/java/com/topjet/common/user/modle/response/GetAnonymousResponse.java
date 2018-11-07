package com.topjet.common.user.modle.response;

import com.topjet.common.utils.StringUtils;

/**
 * Created by yy on 2017/10/24.
 * <p>
 * 获取匿名设置
 */

public class GetAnonymousResponse {
    private String is_anonymous;

    public boolean getIs_anonymous() {
        return StringUtils.isNotBlank(is_anonymous) && is_anonymous.equals("1");
    }

    @Override
    public String toString() {
        return "GetAnonymousResponse{" +
                "is_anonymous='" + is_anonymous + '\'' +
                '}';
    }
}

