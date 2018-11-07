package com.topjet.crediblenumber.goods.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.topjet.common.base.busManger.BusManager;
import com.topjet.common.base.busManger.Subscribe;
import com.topjet.common.common.modle.bean.GoodsListData;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.utils.StringUtils;
import com.topjet.crediblenumber.goods.modle.event.ChangeListenOrderStatusEvent;
import com.topjet.crediblenumber.goods.modle.event.RequestNetOpenOrCloseListenOrderEvent;
import com.topjet.crediblenumber.goods.presenter.ListenOrderController;

/**
 * Created by tsj-004 on 2017/9/12.
 * <p>
 * 听单后台服务
 */

public class ListenOrderService extends IntentService implements InitListener, SynthesizerListener {
    private boolean canRun = true;      // 线程是否可运行
    private SpeechSynthesizer mTts = null;
    private String mEngineType = SpeechConstant.TYPE_CLOUD;// 引擎类型
    private String voicer = "xiaoyan";// 默认发音人
    private boolean isInitSuccess = true;        // 引擎是否初始化完成
    private boolean isPauseBroadcast = false;   // 是否暂停播报
    private String startTime = System.currentTimeMillis() + "";
    private boolean isCloseSelf = false;        // 是否需要关闭自己

    @Override
    public void onCreate() {
        super.onCreate();
        mTts = SpeechSynthesizer.createSynthesizer(this, this);

        canRun = true;

        BusManager.getBus().register(this);
        requestServer(false, "1");
    }

    /**
     * 请求服务器，修改开关状态
     */
    private void requestServer(boolean isPause, String status) {
        isPauseBroadcast = isPause;
        CMemoryData.setIsOrderOpen(!isPause);      // 0:关闭听单 1:开启听单
        new ListenOrderController().setCheckBoxStatus(status); // 请求修改开关状态服务器接口
    }

    public ListenOrderService() {
        super("ListenOrderService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        while (canRun) {
            new ListenOrderController().getListenList(startTime);       // 请求服务器获取数据

            try {
                Thread.sleep(10 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        canRun = false;
        mTts.stopSpeaking();
        BusManager.getBus().unregister(this);
    }

    /**
     * 获取到听单列表数据
     */
    @Subscribe
    public void onEvent(GoodsListData listenOrderItem) {
        if (isInitSuccess) {
            String strTextToSpeech = listenOrderItem.getListen_single_content();
            if (!isPauseBroadcast) {
                if (CMemoryData.isOrderOpen() && StringUtils.isNotBlank(strTextToSpeech)) {
                    mTts.startSpeaking(strTextToSpeech, this);
                }
            }
            if (StringUtils.isNotBlank(listenOrderItem.getUpdate_time())) {
                startTime = listenOrderItem.getUpdate_time();
            }
        }
    }

    /**
     * 修改听单播报状态
     */
    @Subscribe
    public void onEvent(ChangeListenOrderStatusEvent statusEvent) {
        String str = statusEvent.getType();
        /**
         * 0:关闭听单 1:开启听单
         */
        if (str.equals(ChangeListenOrderStatusEvent.CLOSE_LISTEN_ORDER)) { // 关闭播报
            mTts.stopSpeaking();
            isCloseSelf = true;
            requestServer(true, "0");
        }
    }

    /**
     * 修改听单播报状态
     */
    @Subscribe
    public void onEvent(RequestNetOpenOrCloseListenOrderEvent event) {
        if (isCloseSelf) {
            isCloseSelf = false;
            this.stopSelf();
        }
    }

    @Override
    public void onInit(int code) {
        if (code == ErrorCode.SUCCESS) {
            setParam();
            isInitSuccess = true;
        } else {
            isInitSuccess = false;
        }
    }

    @Override
    public void onSpeakBegin() {

    }

    @Override
    public void onBufferProgress(int i, int i1, int i2, String s) {

    }

    @Override
    public void onSpeakPaused() {

    }

    @Override
    public void onSpeakResumed() {

    }

    @Override
    public void onSpeakProgress(int i, int i1, int i2) {

    }

    @Override
    public void onCompleted(SpeechError speechError) {

    }

    @Override
    public void onEvent(int i, int i1, int i2, Bundle bundle) {

    }

    /**
     * 参数设置
     *
     * @return
     */
    private void setParam() {
        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);
        // 根据合成引擎设置相应参数
        if (mEngineType.equals(SpeechConstant.TYPE_CLOUD)) {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
            // 设置在线合成发音人
            mTts.setParameter(SpeechConstant.VOICE_NAME, voicer);
            //设置合成语速
            mTts.setParameter(SpeechConstant.SPEED, "50");
            //设置合成音调
            mTts.setParameter(SpeechConstant.PITCH, "50");
            //设置合成音量
            mTts.setParameter(SpeechConstant.VOLUME, "50");
        } else {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
            // 设置本地合成发音人 voicer为空，默认通过语记界面指定发音人。
            mTts.setParameter(SpeechConstant.VOICE_NAME, "");
            /**
             * TODO 本地合成不设置语速、音调、音量，默认使用语记设置
             * 开发者如需自定义参数，请参考在线合成参数设置
             */
        }
        //设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE, "3");
        // 设置播放合成音频打断音乐播放，默认为true
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/tts.wav");
    }
}
