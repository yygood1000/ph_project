package com.topjet.common.db.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * creator: zhulunjun
 * time:    2017/12/7
 * describe: 聊天敏感字统计数据表
 */
@Entity
public class SensitiveWordBean {
    @Id(autoincrement = true)
    private Long id; // 主键

    private String word_id; //	否	string	敏感字ID
    private String word_name; // 敏感字
    private int word_count = 0; //	否	string	敏感字发送统计
    @Generated(hash = 3973606)
    public SensitiveWordBean(Long id, String word_id, String word_name,
            int word_count) {
        this.id = id;
        this.word_id = word_id;
        this.word_name = word_name;
        this.word_count = word_count;
    }
    @Generated(hash = 511340391)
    public SensitiveWordBean() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getWord_id() {
        return this.word_id;
    }
    public void setWord_id(String word_id) {
        this.word_id = word_id;
    }
    public String getWord_name() {
        return this.word_name;
    }
    public void setWord_name(String word_name) {
        this.word_name = word_name;
    }
    public int getWord_count() {
        return this.word_count;
    }
    public void setWord_count(int word_count) {
        this.word_count = word_count;
    }

}
