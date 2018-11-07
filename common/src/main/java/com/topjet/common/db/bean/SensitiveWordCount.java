package com.topjet.common.db.bean;

/**
 * creator: zhulunjun
 * time:    2017/12/26
 * describe:
 */

public class SensitiveWordCount {
    private String word_id; //	否	string	敏感字ID
    private String word_count = "0"; //	否	string	敏感字发送统计

    public String getWord_id() {
        return word_id;
    }

    public void setWord_id(String word_id) {
        this.word_id = word_id;
    }

    public String getWord_count() {
        return word_count;
    }

    public void setWord_count(String word_count) {
        this.word_count = word_count;
    }
}
