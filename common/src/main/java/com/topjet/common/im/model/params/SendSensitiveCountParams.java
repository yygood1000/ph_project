package com.topjet.common.im.model.params;

import android.content.Context;

import com.google.gson.Gson;
import com.topjet.common.db.DBManager;
import com.topjet.common.db.bean.SensitiveWordBean;
import com.topjet.common.db.bean.SensitiveWordCount;
import com.topjet.common.utils.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * creator: zhulunjun
 * time:    2017/12/7
 * describe: 发送敏感字统计 请求参数
 */

public class SendSensitiveCountParams {

    private List<SensitiveWordCount> sensitive_count_list; // 敏感字次数统计集合

    public SendSensitiveCountParams() {
        // 获取统计关键字, 从数据库
        List<SensitiveWordBean> sensitiveWordBeans = DBManager
                .getInstance()
                .getDaoSession()
                .getSensitiveWordBeanDao()
                .loadAll();
        sensitive_count_list = new ArrayList<>();
        for (SensitiveWordBean bean : sensitiveWordBeans){
            if(bean.getWord_count() > 0) {
                SensitiveWordCount countBean = new SensitiveWordCount();
                countBean.setWord_count(bean.getWord_count()+"");
                countBean.setWord_id(bean.getWord_id());
                sensitive_count_list.add(countBean);
            }
        }

        Logger.d("发送敏感字统计 "+sensitive_count_list);
    }
}
