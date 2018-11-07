package com.topjet.common.im.model.params;

/**
 * creator: zhulunjun
 * time:    2017/12/7
 * describe: 获取更多聊天记录，下拉刷新的参数
 */

public class TalkIdParams {

    private String talk_id; //	是	List	聊天对象id	环信的Id
    private String time; //	是	List	消息时间	拉取历史消息的时间节点

    public TalkIdParams(String talk_id) {
        this.talk_id = talk_id;
    }

    public TalkIdParams(String talk_id, String time) {
        this.talk_id = talk_id;
        this.time = time;
    }

    public String getTalk_id() {
        return talk_id;
    }

    public void setTalk_id(String talk_id) {
        this.talk_id = talk_id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
