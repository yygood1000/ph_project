package com.topjet.common.resource.params;

/**
 * Created by yy on 2017/12/8.
 * <p>
 * 请求资源文件更新请求体
 */

public class UpdataResourceParams {
    private String inner_version;

    public UpdataResourceParams(String inner_version) {
        this.inner_version = inner_version;
    }
}
