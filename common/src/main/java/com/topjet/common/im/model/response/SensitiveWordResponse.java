package com.topjet.common.im.model.response;

import com.topjet.common.db.bean.SensitiveWordBean;

import java.util.List;

/**
 * creator: zhulunjun
 * time:    2017/12/7
 * describe: 聊天关键字，列表
 */

public class SensitiveWordResponse {

    private List<SensitiveWordBean> sensitive_word_list;	// 是	List	敏感字列表

    public List<SensitiveWordBean> getSensitive_word_list() {
        return sensitive_word_list;
    }

    public void setSensitive_word_list(List<SensitiveWordBean> sensitive_word_list) {
        this.sensitive_word_list = sensitive_word_list;
    }
}
