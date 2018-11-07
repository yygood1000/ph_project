package com.hyphenate.easeui.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.PowerManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMError;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.model.EaseVoiceRecorder;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.easeui.widget.chatrow.EaseChatRowVoicePlayClickListener;
import com.hyphenate.util.EMLog;
import com.topjet.common.utils.Toaster;

/**
 * Voice recorder view
 *
 */
public class EaseVoiceRecorderView extends RelativeLayout {
    protected Context context;
    protected LayoutInflater inflater;
    protected Drawable[] micImages;
    protected EaseVoiceRecorder voiceRecorder;

    protected PowerManager.WakeLock wakeLock;
    protected ImageView micImage,ivRecording;
    protected TextView recordingHint;
    protected TextView tvPressToSpeak;
    protected RelativeLayout rlRecord;
    private boolean recordingTimeOut = false;//录音超时

    protected Handler micImageHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            if(msg.what == -1){//60秒超时
                recordingTimeOut = true;
                tvPressToSpeak.setText(R.string.button_pushtotalk);
                int length = stopRecoding();
                if (mEaseVoiceRecorderCallback != null) {
                    mEaseVoiceRecorderCallback.onVoiceRecordComplete(getVoiceFilePath(), length);
                }
                Toaster.showLongToast("录音超时，已自动发送");
            }else {
                // change image
                micImage.setImageDrawable(micImages[msg.what]);
                EMLog.e("voice111", "22 " + (msg.what));
            }
        }
    };

    public EaseVoiceRecorderView(Context context) {
        super(context);
        init(context);
    }

    public EaseVoiceRecorderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public EaseVoiceRecorderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.ease_widget_voice_recorder, this);

        micImage = (ImageView) findViewById(R.id.mic_image);
        recordingHint = (TextView) findViewById(R.id.recording_hint);

        voiceRecorder = new EaseVoiceRecorder(micImageHandler);
        rlRecord = (RelativeLayout) findViewById(R.id.rl_record);
        ivRecording = (ImageView) findViewById(R.id.iv_recording);

        // animation resources, used for recording
        micImages = new Drawable[] {
                getResources().getDrawable(R.drawable.mes_icon_voice_line1),
                getResources().getDrawable(R.drawable.mes_icon_voice_line2),
                getResources().getDrawable(R.drawable.mes_icon_voice_line3),
                getResources().getDrawable(R.drawable.mes_icon_voice_line4),
                getResources().getDrawable(R.drawable.mes_icon_voice_line5),
//                getResources().getDrawable(R.drawable.ease_record_animate_06),
//                getResources().getDrawable(R.drawable.ease_record_animate_07),
//                getResources().getDrawable(R.drawable.ease_record_animate_08),
//                getResources().getDrawable(R.drawable.ease_record_animate_09),
//                getResources().getDrawable(R.drawable.ease_record_animate_10),
//                getResources().getDrawable(R.drawable.ease_record_animate_11),
//                getResources().getDrawable(R.drawable.ease_record_animate_12),
//                getResources().getDrawable(R.drawable.ease_record_animate_13),
//                getResources().getDrawable(R.drawable.ease_record_animate_14),
        };

        wakeLock = ((PowerManager) context.getSystemService(Context.POWER_SERVICE)).newWakeLock(
                PowerManager.SCREEN_DIM_WAKE_LOCK, "demo");
    }

    private EaseVoiceRecorderCallback mEaseVoiceRecorderCallback;
    /**
     * on speak button touched
     * 
     * @param v
     * @param event
     */
    public boolean onPressToSpeakBtnTouch(View v, MotionEvent event, EaseVoiceRecorderCallback recorderCallback) {
        mEaseVoiceRecorderCallback = recorderCallback;
       tvPressToSpeak = (TextView) v.findViewById(R.id.tv_press_to_speak);
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            try {
                if (EaseChatRowVoicePlayClickListener.isPlaying)
                    EaseChatRowVoicePlayClickListener.currentPlayListener.stopPlayVoice();
                v.setPressed(true);
                startRecording();
                tvPressToSpeak.setText(R.string.button_pulltostop);
            } catch (Exception e) {
                v.setPressed(false);
            }
            return true;
        case MotionEvent.ACTION_MOVE:
            if(recordingTimeOut)
                return true;
            if (event.getY() < 0) {
                showReleaseToCancelHint();
            } else {
                showMoveUpToCancelHint();
            }
            return true;
        case MotionEvent.ACTION_UP:
            tvPressToSpeak.setText(R.string.button_pushtotalk);
            v.setPressed(false);
            if(recordingTimeOut)
                return true;
            if (event.getY() < 0) {
                // discard the recorded audio.
                discardRecording();
            } else {
                // stop recording and send voice file
                try {
                    int length = stopRecoding();
                    if (length > 0) {
                        if (recorderCallback != null) {
                            recorderCallback.onVoiceRecordComplete(getVoiceFilePath(), length);
                        }
                    } else if (length == EMError.FILE_INVALID) {
                        Toast.makeText(context, R.string.Recording_without_permission, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, R.string.The_recording_time_is_too_short, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, R.string.send_failure_please, Toast.LENGTH_SHORT).show();
                }
            }
            return true;
        default:
            discardRecording();
            return false;
        }
    }

    public interface EaseVoiceRecorderCallback {
        /**
         * on voice record complete
         * 
         * @param voiceFilePath
         *            录音完毕后的文件路径
         * @param voiceTimeLength
         *            录音时长
         */
        void onVoiceRecordComplete(String voiceFilePath, int voiceTimeLength);
    }

    public void startRecording() {
        if (!EaseCommonUtils.isSdcardExist()) {
            Toast.makeText(context, R.string.Send_voice_need_sdcard_support, Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            wakeLock.acquire();
            this.setVisibility(View.VISIBLE);
            recordingHint.setText(context.getString(R.string.move_up_to_cancel));
//            rlRecord.setBackgroundResource(R.drawable.ease_recording_hint_bg);
            showMoveUpToCancelHint();
            voiceRecorder.startRecording(context);
        } catch (Exception e) {
            e.printStackTrace();
            if (wakeLock.isHeld())
                wakeLock.release();
            if (voiceRecorder != null)
                voiceRecorder.discardRecording();
            this.setVisibility(View.INVISIBLE);
            Toast.makeText(context, R.string.recoding_fail, Toast.LENGTH_SHORT).show();
        }
    }

    public void showReleaseToCancelHint() {
        recordingHint.setText(context.getString(R.string.release_to_cancel));
        rlRecord.setBackgroundResource(R.drawable.ease_recording_hint_red_bg);
        ivRecording.setImageResource(R.drawable.im_icon_voice_high);
        micImage.setVisibility(GONE);

    }

    public void showMoveUpToCancelHint() {
        recordingHint.setText(context.getString(R.string.move_up_to_cancel));
        rlRecord.setBackgroundResource(R.drawable.ease_recording_hint_bg);
        ivRecording.setImageResource(R.drawable.mes_icon_voice_def);
        micImage.setVisibility(VISIBLE);
    }

    public void discardRecording() {
        if (wakeLock.isHeld())
            wakeLock.release();
        try {
            // stop recording
            if (voiceRecorder.isRecording()) {
                voiceRecorder.discardRecording();
                this.setVisibility(View.INVISIBLE);
            }
        } catch (Exception e) {
        }
    }

    public int stopRecoding() {
        this.setVisibility(View.INVISIBLE);
        if (wakeLock.isHeld())
            wakeLock.release();
        return voiceRecorder.stopRecoding();
    }

    public String getVoiceFilePath() {
        return voiceRecorder.getVoiceFilePath();
    }

    public String getVoiceFileName() {
        return voiceRecorder.getVoiceFileName();
    }

    public boolean isRecording() {
        return voiceRecorder.isRecording();
    }

}
