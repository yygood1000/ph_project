package com.topjet.common.common.modle.params;

/**
 * Created by yy on 2017/9/15.
 * 滑动按钮组 请求类
 */

public class GetScrollBtnsParams {
    private String version;

    public GetScrollBtnsParams(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "GetScrollBtnsParams{" +
                "version='" + version + '\'' +
                '}';
    }
}
