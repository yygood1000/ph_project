package com.hyphenate.easeui.widget.chatrow;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Message;
import android.support.v4.os.AsyncTaskCompat;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMFileMessageBody;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessage.ChatType;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.model.EaseImageCache;
import com.hyphenate.easeui.ui.EaseShowBigImageActivity;
import com.hyphenate.easeui.utils.EaseImageUtils;
import com.hyphenate.easeui.utils.EaseUserUtils;
import com.topjet.common.db.bean.ImageMessageBean;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.StringUtils;

import java.io.File;
import java.sql.Struct;
import java.util.List;

public class EaseChatRowImage extends EaseChatRowFile{

    protected ImageView imageView;
    private EMImageMessageBody imgBody;

    public EaseChatRowImage(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
    }

    @Override
    protected void onInflateView() {
        inflater.inflate(message.direct() == EMMessage.Direct.RECEIVE ? R.layout.ease_row_received_picture : R.layout.ease_row_sent_picture, this);
    }

    @Override
    protected void onFindViewById() {
        percentageView = (TextView) findViewById(R.id.percentage);
        imageView = (ImageView) findViewById(R.id.image);
    }

    
    @Override
    protected void onSetUpView() {
        imgBody = (EMImageMessageBody) message.getBody();

//        Logger.d("图片地址 "+imgBody.getLocalUrl()+" "+new File(imgBody.getLocalUrl()).exists());
        if(message.status() == EMMessage.Status.SUCCESS) {
            new ImageMessageBean().saveImageMessageDB(message);
        }
        // 历史消息中获取的图片
        boolean fromHistory = message.getBooleanAttribute(EaseConstant.MESSAGE_ATTR_IS_FROM_HISTORY,false);
        if(fromHistory) {
            if (!new File(imgBody.thumbnailLocalPath()).exists()) {
                EMClient.getInstance().chatManager().downloadThumbnail(message);
            }
        }
        // received messages
        if (message.direct() == EMMessage.Direct.RECEIVE) {
            if (imgBody.thumbnailDownloadStatus() == EMFileMessageBody.EMDownloadStatus.FAILED ||
                    imgBody.thumbnailDownloadStatus() == EMFileMessageBody.EMDownloadStatus.SUCCESSED) {

                progressBar.setVisibility(View.GONE);
                percentageView.setVisibility(View.GONE);
                imageView.setImageResource(R.drawable.ease_default_image);
                String thumbPath = imgBody.thumbnailLocalPath();
//
                if (!new File(thumbPath).exists()) {
                    // to make it compatible with thumbnail received in previous version
                    thumbPath = EaseImageUtils.getThumbnailImagePath(imgBody.getLocalUrl());
                }
                showImageView(thumbPath, imgBody.getLocalUrl(), message);
            } else {
                imageView.setImageResource(R.drawable.ease_default_image);
                //set the receive message callback
                setMessageReceiveCallback();
            }

            return;
        }
        
        String filePath = imgBody.getLocalUrl();
        String thumbPath = EaseImageUtils.getThumbnailImagePath(imgBody.getLocalUrl());
        showImageView(thumbPath, filePath, message);
        handleSendMessage();

    }

    /**
     * 下载图片，当本地图片不存在时，下载，为了查看图片，能查看到大图
     */
    private void downloadImage(String localPath){
        if(!new File(localPath).exists()) {

            EMClient.getInstance().chatManager().downloadAttachment(message);
            message.setMessageStatusCallback(new EMCallBack() {
                @Override
                public void onSuccess() {
                    Logger.d("下载图片成功");
                }

                @Override
                public void onError(int i, String s) {
                    Logger.d("下载图片失败"+s);
                }

                @Override
                public void onProgress(int i, String s) {
                    Logger.d("下载图片"+i);
                }
            });
        }
    }
    
    @Override
    protected void onUpdateView() {
        super.onUpdateView();
    }
    
    @Override
    protected void onBubbleClick() {
        if (imgBody.thumbnailDownloadStatus() == EMFileMessageBody.EMDownloadStatus.DOWNLOADING ||
                imgBody.thumbnailDownloadStatus() == EMFileMessageBody.EMDownloadStatus.PENDING) {
            //thumbnail image downloading
            return;
        } else if(imgBody.thumbnailDownloadStatus() == EMFileMessageBody.EMDownloadStatus.FAILED){
            progressBar.setVisibility(View.VISIBLE);
            percentageView.setVisibility(View.VISIBLE);
            // retry download with click event of user
            EMClient.getInstance().chatManager().downloadThumbnail(message);
        }


        Intent intent = new Intent(context, EaseShowBigImageActivity.class);
        File file = new File(imgBody.getLocalUrl());
        if (file.exists()) {
            Uri uri = Uri.fromFile(file);
            intent.putExtra("uri", uri);
        } else {
            // The local full size pic does not exist yet.
            // ShowBigImage needs to download it from the server
            // first
            String msgId = message.getMsgId();
            intent.putExtra("messageId", msgId);
            intent.putExtra("localUrl", imgBody.getLocalUrl());
        }
        if (message != null && message.direct() == EMMessage.Direct.RECEIVE && !message.isAcked()
                && message.getChatType() == ChatType.Chat) {
            try {
                EMClient.getInstance().chatManager().ackMessageRead(message.getFrom(), message.getMsgId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(!new File(imgBody.getLocalUrl()).exists()){
            // 下载大图
            downloadImage(imgBody.getLocalUrl());
        }
        itemClickListener.onImageMessageClick(message.getMsgId());
//        context.startActivity(intent);
    }

    /**
     * load image into image view
     * 
     */
    @SuppressLint("StaticFieldLeak")
    private void showImageView(final String thumbernailPath, final String localFullSizePath, final EMMessage message) {
        // first check if the thumbnail image already loaded into cache
        Bitmap bitmap = EaseImageCache.getInstance().get(thumbernailPath);
        if (bitmap != null) {
            // thumbnail image is already loaded, reuse the drawable
            imageView.setImageBitmap(bitmap);
        } else {
            AsyncTaskCompat.executeParallel( new AsyncTask<Object, Void, Bitmap>() {

                @Override
                protected Bitmap doInBackground(Object... args) {
                    File file = new File(thumbernailPath);
                    if (file.exists()) {
                        return EaseImageUtils.decodeScaleImage(thumbernailPath, 160, 160);
                    } else if (new File(imgBody.thumbnailLocalPath()).exists()) {
                        return EaseImageUtils.decodeScaleImage(imgBody.thumbnailLocalPath(), 160, 160);
                    }
                    else {
                        if (message.direct() == EMMessage.Direct.SEND) {
                            if (localFullSizePath != null && new File(localFullSizePath).exists()) {
                                return EaseImageUtils.decodeScaleImage(localFullSizePath, 160, 160);
                            } else {
                                return null;
                            }
                        } else {
                            return null;
                        }
                    }
                }

                protected void onPostExecute(Bitmap image) {
                    if (image != null) {
                        imageView.setImageBitmap(image);
                        EaseImageCache.getInstance().put(thumbernailPath, image);
                    }
                }
            });
        }
    }

}
