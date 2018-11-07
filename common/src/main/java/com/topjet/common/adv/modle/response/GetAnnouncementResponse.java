package com.topjet.common.adv.modle.response;

import com.topjet.common.adv.modle.bean.AnnouncementBean;

import java.util.ArrayList;

/**
 * Created by yy on 2017/11/14.
 * <p>
 * 首页公告返回体
 */

public class GetAnnouncementResponse {

    private ArrayList<AnnouncementBean> list;

    public ArrayList<AnnouncementBean> getList() {
        return list;
    }
}
