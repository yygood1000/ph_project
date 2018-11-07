package com.hyphenate.easeui.model.styles;

/**
 * Created by wei on 2016/11/29.
 */

public class EaseMessageListItemStyle {
    private boolean showUserNick;
    private boolean showAvatar;
    private int myBubbleBg;
    private int otherBubbleBg;

    public EaseMessageListItemStyle(Builder builder){
        showUserNick = builder.showUserNick;
        showAvatar = builder.showAvatar;
        myBubbleBg = builder.myBubbleBg;
        otherBubbleBg = builder.otherBubbleBg;
    }

    public boolean isShowUserNick() {
        return showUserNick;
    }

    public void setShowUserNick(boolean showUserNick) {
        this.showUserNick = showUserNick;
    }

    public boolean isShowAvatar() {
        return showAvatar;
    }

    public void setShowAvatar(boolean showAvatar) {
        this.showAvatar = showAvatar;
    }

    public int getMyBubbleBg() {
        return myBubbleBg;
    }

    public void setMyBubbleBg(int myBubbleBg) {
        this.myBubbleBg = myBubbleBg;
    }

    public int getOtherBubbleBg() {
        return otherBubbleBg;
    }

    public void setOtherBubbleBg(int otherBubbleBg) {
        this.otherBubbleBg = otherBubbleBg;
    }


    public static final class Builder{
        private boolean showUserNick;
        private boolean showAvatar;
        private int myBubbleBg;
        private int otherBubbleBg;

        public Builder showUserNick(boolean showUserNick){
            this.showUserNick = showUserNick;
            return  this;
        }

        public Builder showAvatar(boolean showAvatar){
            this.showAvatar = showAvatar;
            return  this;
        }

        public Builder myBubbleBg(int myBubbleBg){
            this.myBubbleBg = myBubbleBg;
            return  this;
        }

        public Builder otherBuddleBg(int otherBuddleBg){
            this.otherBubbleBg = otherBuddleBg;
            return  this;
        }


        public EaseMessageListItemStyle build(){
            return new EaseMessageListItemStyle(this);
        }
    }

}
