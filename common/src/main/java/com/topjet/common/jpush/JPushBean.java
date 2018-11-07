package com.topjet.common.jpush;

import com.topjet.common.base.model.BaseExtra;

import java.util.List;

/**
 * creator: zhulunjun
 * time:    2017/11/23
 * describe: 极光推送返回的json 数据
 */

public class JPushBean extends BaseExtra{


    /**
     * title : 这是标题
     * text : 这是内容
     * button : [{"text":"我是第一个按钮文字","action":"shipperapp://"},{"text":"我是第二个按钮文字","action":"shipperapp://"}]
     * action : shipperapp://
     */

    private String title;
    private String text;
    private String action;
    private List<ButtonBean> button;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public List<ButtonBean> getButton() {
        return button;
    }

    public void setButton(List<ButtonBean> button) {
        this.button = button;
    }

    public static class ButtonBean extends BaseExtra{
        @Override
        public String toString() {
            return "ButtonBean{" +
                    "text='" + text + '\'' +
                    ", action='" + action + '\'' +
                    '}';
        }

        /**
         * text : 我是第一个按钮文字
         * action : shipperapp://
         */


        private String text;
        private String action;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }
    }

    @Override
    public String toString() {
        return "JPushBean{" +
                "title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", action='" + action + '\'' +
                ", button=" + button.toString() +
                '}';
    }
}
