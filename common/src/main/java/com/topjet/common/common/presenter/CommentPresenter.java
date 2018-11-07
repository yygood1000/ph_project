package com.topjet.common.common.presenter;

import android.content.Context;

import com.topjet.common.base.presenter.BasePresenter;
import com.topjet.common.common.modle.extra.CommentExtra;
import com.topjet.common.common.view.activity.CommentView;
import com.topjet.common.config.Config;

/**
 * Created by yy on 2017/10/12.
 * 积分商城相关页面
 */

public class CommentPresenter extends BasePresenter<CommentView> {
    public CommentExtra extra;

    public CommentPresenter(CommentView mView, Context mContext) {
        super(mView, mContext);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        extra = (CommentExtra) mActivity.getIntentExtra(CommentExtra.getExtraName());
    }

    public String getUrl() {
        if (extra.getType() == CommentExtra.TYPE_SUBMIT_COMMENT) {
            return getSubmitCommentUrl();
        } else if (extra.getType() == CommentExtra.TYPE_CHECK_BACK_COMMENT) {
            return getCheckBackCommentUrl();
        } else if (extra.getType() == CommentExtra.TYPE_CHECK_COMMENT_LIST_SELF) {
            return getCommentListSelfUrl();
        } else if (extra.getType() == CommentExtra.TYPE_CHECK_COMMENT_LIST_OTHERS) {
            return getCommentListOthersUrl();
        }
        return "";
    }

    /**
     * 获取提交评论页面Url
     */
    private String getSubmitCommentUrl() {
        return Config.getCommentUrl() +
                "?order_id=" + extra.getOrderId() +
                "&version=" + extra.getOrderVersion() +
                "&commented_user=" + extra.getCommentedUserId() +
                "&commented_name=" + extra.getCommentedUserName();
    }

    /**
     * 查看回评页面Url
     */
    private String getCheckBackCommentUrl() {
        return Config.getCheckCommentUrl() +
                "?order_id=" + extra.getOrderId() +
                "&version=" + extra.getOrderVersion();
    }

    /**
     * 查看自身页面Url
     */
    private String getCommentListSelfUrl() {
        return Config.getCommentListUrl();
    }

    /**
     * 查看他人页面Url
     */
    private String getCommentListOthersUrl() {
        return Config.getCommentListUrl() +
                "?user_id=" + extra.getUser_id();
    }
}
