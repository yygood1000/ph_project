package com.topjet.common.adv.modle.response;

import com.topjet.common.adv.modle.bean.MarqueeBean;
import com.topjet.common.utils.ListUtils;
import com.topjet.common.utils.Logger;

import java.util.ArrayList;

/**
 * Created by yy on 2017/11/13.
 * 跑马灯广告返回
 */

public class GetMarqueeResponse {
    private ArrayList<MarqueeBean> list;
    private String SPACE = "                                ";

    public ArrayList<MarqueeBean> getList() {
        return list;
    }

    public String getMarqueeText() {
        if (ListUtils.isEmpty(list)) {
            return "";
        } else {


            StringBuilder builder = new StringBuilder();
            for (MarqueeBean adv : list) {
                builder.append(SPACE);
                builder.append(SPACE);
                builder.append(adv.getContent());
            }
            Logger.d("跑马灯 "+builder.toString());
            return builder.toString();
        }
    }
}
