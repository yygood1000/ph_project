package com.topjet.common.common.modle.params;

/**
 * Created by tsj028 on 2017/12/1 0001.
 */

public class GetTabLayoutBtnsParams {
    private String version;

    public GetTabLayoutBtnsParams(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "GetTabLayoutBtnsParams{" +
                "version='" + version + '\'' +
                '}';
    }
}
