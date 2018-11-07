package com.topjet.common.jpush;

/**
 * creator: zhulunjun
 * time:    2017/11/27
 * describe:
 */

public class JPushContentBean {


    /**
     * id : 1711241643268639652
     * type : 1
     * data : {"alert":"从上海市市徐汇区运往天津市市区的订单（订单号：CJ171117000003），司机李清正已关闭退款，请继续完成交易。","title":"","content":"{\"title\":\"从上海市市徐汇区运往天津市市区的订单（订单号：CJ171117000003），司机李清正已关闭退款，请继续完成交易。\",\"button\":[{\"text\":\"我知道了\",\"action\":\"\"}],\"action\":\"\"}"}
     * pushtime : 1511513006024
     */

    private String id;
    private String type;
    private DataBean data;
    private String pushtime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getPushtime() {
        return pushtime;
    }

    public void setPushtime(String pushtime) {
        this.pushtime = pushtime;
    }

    public static class DataBean {
        /**
         * alert : 从上海市市徐汇区运往天津市市区的订单（订单号：CJ171117000003），司机李清正已关闭退款，请继续完成交易。
         * title :
         * content : {"title":"从上海市市徐汇区运往天津市市区的订单（订单号：CJ171117000003），司机李清正已关闭退款，请继续完成交易。","button":[{"text":"我知道了","action":""}],"action":""}
         */

        private String alert;
        private String title;
        private String content;

        public String getAlert() {
            return alert;
        }

        public void setAlert(String alert) {
            this.alert = alert;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
