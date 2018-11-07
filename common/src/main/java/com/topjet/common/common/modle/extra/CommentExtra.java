package com.topjet.common.common.modle.extra;

import com.topjet.common.base.model.BaseExtra;

/**
 * creator: yy
 * time:    2017/10/31
 * describe: 评价跳转参数
 */

public class CommentExtra extends BaseExtra {
    public static int TYPE_SUBMIT_COMMENT = 0;// 提交评论
    public static int TYPE_CHECK_BACK_COMMENT = 1;// 查看回评
    public static int TYPE_CHECK_COMMENT_LIST_SELF = 2;// 查看自身评论列表
    public static int TYPE_CHECK_COMMENT_LIST_OTHERS = 3;// 查看别人评论列表


    private int type;

    private String orderId;
    private String orderVersion;
    private String commentedUserId;
    private String commentedUserName;
    private String user_id;// 被查看人的用户id


    /**
     * 查看回评
     */
    public CommentExtra(String orderId, String orderVersion) {
        type = TYPE_CHECK_BACK_COMMENT;
        this.orderId = orderId;
        this.orderVersion = orderVersion;
    }

    /**
     * 跳转查看他人评论列表
     */
    public CommentExtra(String user_id) {
        type = TYPE_CHECK_COMMENT_LIST_OTHERS;
        this.user_id = user_id;
    }

    /**
     * 跳转查看自身评论列表
     */
    public CommentExtra() {
        type = TYPE_CHECK_COMMENT_LIST_SELF;
    }

    /**
     * 跳转提交评论
     */

    public CommentExtra(String orderId, String orderVersion, String commentedUserId, String commentedUserName) {
        type = TYPE_SUBMIT_COMMENT;
        this.orderId = orderId;
        this.orderVersion = orderVersion;
        this.commentedUserId = commentedUserId;
        this.commentedUserName = commentedUserName;
    }

    public int getType() {
        return type;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getOrderVersion() {
        return orderVersion;
    }

    public String getCommentedUserId() {
        return commentedUserId;
    }

    public String getCommentedUserName() {
        return commentedUserName;
    }

    public String getUser_id() {
        return user_id;
    }
}
