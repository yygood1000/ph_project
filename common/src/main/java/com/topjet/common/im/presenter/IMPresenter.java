package com.topjet.common.im.presenter;

import android.content.Context;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMError;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMClient;
import com.topjet.common.base.CommonProvider;
import com.topjet.common.base.listener.ObserverOnResultListener;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.config.CConstants;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.config.CPersisData;
import com.topjet.common.db.DBManager;
import com.topjet.common.db.bean.SensitiveWordBean;
import com.topjet.common.im.model.ChatCommand;
import com.topjet.common.im.model.bean.HistoryUserBean;
import com.topjet.common.im.model.params.SendSensitiveCountParams;
import com.topjet.common.im.model.params.TalkIdParams;
import com.topjet.common.im.model.params.UserIdListParams;
import com.topjet.common.im.model.response.BlackListUserResponse;
import com.topjet.common.im.model.response.HistoryMessageResponse;
import com.topjet.common.im.model.response.SensitiveWordResponse;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.RxHelper;
import com.topjet.common.utils.SPUtils;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;

/**
 * creator: zhulunjun
 * time:    2017/11/28
 * describe: im的控制类
 */

public class IMPresenter {
    private Context mContext;
    private ChatCommand mApiCommand;

    private int reLoginNumber = 0; // 如果登录失败，重连3次

    public IMPresenter(Context mContext) {
        this.mContext = mContext;
        mApiCommand = new ChatCommand((MvpActivity) mContext);
    }


    /**
     * 统一的IM服务器登录
     */
    public void loginIM() {
        try {
            if (SPUtils.getBoolean(CConstants.IM_IS_LOGIN_ING, false)) { // 正在登陆中，则不再请求
                return;
            }
            String userName;
            // 用户id,用前缀来区分货主和司机
            if (CMemoryData.isDriver()) {
                userName = CConstants.IM_HEAR_DRIVER + CPersisData.getUserID();
            } else {
                userName = CConstants.IM_HEAR_SHIPPER + CPersisData.getUserID();
            }
            Logger.d("登录聊天服务器！" + userName);
            EMClient.getInstance().logout(true);//先退出，在登录，不然换账号登不进去了
            SPUtils.putBoolean(CConstants.IM_IS_LOGIN_ING, true);
            EMClient.getInstance().login(userName, CConstants.IM_USER_PASSWORD,
                    new EMCallBack() {//回调
                        @Override
                        public void onSuccess() {
                            SPUtils.putBoolean(CConstants.IM_IS_LOGIN_ING, false);
                            SPUtils.putBoolean(CConstants.IM_USER_EMPTY, false);
                            Logger.d("登录聊天服务器成功！");
                            // 存储数据
                            saveIMData();
                            reLoginNumber = 0;
                        }

                        @Override
                        public void onProgress(int progress, String status) {
                        }

                        @Override
                        public void onError(int code, String message) {
                            SPUtils.putBoolean(CConstants.IM_IS_LOGIN_ING, false);
                            Logger.d("登录聊天服务器失败！" +code+" "+ message);
                            if(message.equals("User dosn't exist") || code == EMError.USER_NOT_FOUND) {//User dosn't exist
                                SPUtils.putBoolean(CConstants.IM_USER_EMPTY, true);
                            }
                            //存储登录状态
                            SPUtils.putBoolean(CConstants.IM_IS_LOGIN_SUCCESS, false);
                        }
                    });
        } catch (Exception e) {
            Logger.e("聊天错误 " + e.getMessage());
        }
    }

    public void showLoading(boolean goMain) {
        if (goMain) {
            final MvpActivity activity = (MvpActivity) mContext;
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    activity.showLoadingDialog();
                }
            });
        }
    }

    public void closeLoading(boolean goMain) {
        if (goMain) {
            final MvpActivity activity = (MvpActivity) mContext;
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    activity.cancelShowLoadingDialog();
                }
            });

        }
    }


    /**
     * 登陆成功之后需要保存的数据
     */
    private void saveIMData() {
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {
                //加载本地回话列表
                EMClient.getInstance().chatManager().loadAllConversations();
                // 加载黑名单
                getBlackList();
                SPUtils.putBoolean(CConstants.IM_IS_LOGIN_SUCCESS, true);
                e.onNext(new Object());
                e.onComplete();
            }
        })
                .compose(RxHelper.rxSchedulerHelper())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {

                    }
                });

    }

    /**
     * 获取黑名单,从服务器
     */
    private void getBlackList() {
        EMClient.getInstance().contactManager().aysncGetBlackListFromServer(new EMValueCallBack<List<String>>() {
            @Override
            public void onSuccess(List<String> strings) {
                Logger.d("TTT", "aysncGetBlackListFromServer==onSuccess==" + strings.size());
                saveBlackList(strings);
            }

            @Override
            public void onError(int i, String s) {
                Logger.d("TTT", "aysncGetBlackListFromServer==onError===" + s);
            }
        });
    }

    /**
     * 保存黑名单
     */
    private void saveBlackList(List<String> strings) {
        getBlackListUsers(strings);

        EMClient.getInstance().contactManager().asyncSaveBlackList(strings, new EMCallBack() {
            @Override
            public void onSuccess() {
                Logger.d("TTT", "asyncSaveBlackList==onSuccess==");
            }

            @Override
            public void onError(int i, String s) {
                Logger.d("TTT", "asyncSaveBlackList==onError==" + s);
            }

            @Override
            public void onProgress(int i, String s) {
                Logger.d("TTT", "asyncSaveBlackList==onProgress==" + i);
            }
        });
    }

    /**
     * 查询黑名单中用户的头像昵称
     */
    public void getBlackListUsers(List<String> strings) {
        if (strings != null && strings.size() > 0) {
            mApiCommand.getChatBlackUser(new UserIdListParams(strings), new ObserverOnResultListener<BlackListUserResponse>() {
                @Override
                public void onResult(final BlackListUserResponse blackListUserResponse) {
                    if (blackListUserResponse != null && blackListUserResponse.getChat_user_list() != null) {
                        RxHelper.runIOThread(new RxHelper.OnIOThreadListener() {
                            @Override
                            public Object onIOThread() {
                                for (HistoryUserBean userBean : blackListUserResponse.getChat_user_list()) {
                                    CommonProvider.getInstance().getUserInfoProvider().saveUserInfo(userBean.getUserBean(userBean));
                                }
                                return new Object();
                            }

                            @Override
                            public void onMainThread(Object o) {

                            }
                        });
                    }
                }
            });
        }
    }

    /**
     * 获取敏感字列表
     */
    public void getSensitiveWord() {
        mApiCommand.getSensitiveWord(new ObserverOnResultListener<SensitiveWordResponse>() {
            @Override
            public void onResult(SensitiveWordResponse sensitiveWordResponse) {
                // 保存到本地数据库
                if (sensitiveWordResponse != null) {
                    final List<SensitiveWordBean> sensitiveWordBeans = sensitiveWordResponse.getSensitive_word_list();
                    if (sensitiveWordBeans != null) {
                        RxHelper.runIOThread(new RxHelper.OnIOThreadListener() {
                            @Override
                            public Object onIOThread() {
                                for (SensitiveWordBean bean : sensitiveWordBeans) {
                                    DBManager.getInstance()
                                            .getDaoSession()
                                            .getSensitiveWordBeanDao()
                                            .save(bean);
                                }
                                return new Object();
                            }

                            @Override
                            public void onMainThread(Object o) {

                            }
                        });


                    }
                }
            }
        });
    }

    /**
     * 更新关键字状态
     * 距离上传更新间隔大于一天，就在此上传记录
     */
    public void updateSensitiveWord() {
        // 关键字统计总数，如果总数为0则不上传
        long amount = CPersisData.getSensitiveWordAmount();
        // 上一次提交关键字统计的时间
//        long updateTime = CPersisData.getSensitiveWordSubmitTime();
        long updateTime = 1501572135;
        // 当前时间，从服务器接口中获取的
        long currentTime = CPersisData.getServersDate();
        long difference = currentTime - updateTime;
//        if (difference > (1000 * 60 * 60 * 24)) { //大于一天就上传
        CPersisData.setSensitiveWordSubmitTime(System.currentTimeMillis());

        if (amount == 0) {
            //获取
            getSensitiveWord();
        } else {
            //提交
            mApiCommand.sendSensitiveWordCount(new SendSensitiveCountParams(),
                    new ObserverOnResultListener<Object>() {
                        @Override
                        public void onResult(Object o) {
                            Logger.d("关键字上传成功");
                            // 清空
                            clearSensitiveDB();
                            // 设置总数为0
                            CPersisData.setSensitiveWordAmount(0);
                            //获取
                            getSensitiveWord();
                        }
                    });
        }

//        } else {
//            getSensitiveWord();
//        }
    }

    /**
     * 将关键字归零
     */
    private void clearSensitiveDB() {
        List<SensitiveWordBean> sensitiveWordBeans = DBManager
                .getInstance()
                .getDaoSession()
                .getSensitiveWordBeanDao()
                .loadAll();
        for (SensitiveWordBean bean : sensitiveWordBeans) {
            bean.setWord_count(0);
            DBManager
                    .getInstance()
                    .getDaoSession()
                    .getSensitiveWordBeanDao()
                    .save(bean);
        }
    }


    /**
     * 获取聊天记录
     */
    public void getHistoryMessage() {
        mApiCommand.getHistoryMessage(new ObserverOnResultListener<HistoryMessageResponse>() {
            @Override
            public void onResult(final HistoryMessageResponse historyMessageResponse) {
                Observable.create(new ObservableOnSubscribe<Object>() {
                    @Override
                    public void subscribe(ObservableEmitter<Object> e) throws Exception {
                        CommonProvider.getInstance().getHistoryMessageProvider().saveHistoryMessage(historyMessageResponse);
                    }
                })
                        .compose(RxHelper.rxSchedulerHelper())
                        .subscribe(new Consumer<Object>() {
                            @Override
                            public void accept(Object o) throws Exception {

                            }
                        });
            }
        });
    }

    /**
     * 获取更多历史聊天记录 ， 下拉刷新时调用
     */
    public void getMoreHistoryMessage(String talkId, String time, ObserverOnResultListener<HistoryMessageResponse> observerOnResultListener) {
        mApiCommand.getMoreHistoryMessage(new TalkIdParams(talkId, time), observerOnResultListener);
    }


}
