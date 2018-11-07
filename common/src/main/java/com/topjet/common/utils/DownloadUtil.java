package com.topjet.common.utils;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 文件下载工具类
 * Created by tsj028 on 2018/3/13 0013.
 */

public class DownloadUtil {
    public static final int DOWNLOAD_FAIL=0;
    public static final int DOWNLOAD_PROGRESS=1;
    public static final int DOWNLOAD_SUCCESS=2;
    public static final String DOWNLOAD_ARG_1 = "STRING1";
    public static final String DOWNLOAD_ARG_2 = "STRING2";
    private static DownloadUtil downloadUtil;
    private final OkHttpClient okHttpClient;
    public static DownloadUtil getInstance() {
        if (downloadUtil == null) {
            downloadUtil = new DownloadUtil();
        }
        return downloadUtil;
    }

    private DownloadUtil() {
        okHttpClient = new OkHttpClient();
    }

    /**
     *
     */
    public void download(final String arg1, final String arg2, final String url,final String saveDir,final OnDownloadListener listener){
        this.listener=listener;
        Request request=new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message=Message.obtain();
                message.what=DOWNLOAD_FAIL;
                mHandler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is=null;
                byte[] buf=new byte[2048];
                int len=0;
                FileOutputStream fos=null;
                //储存下载文件的目录
                String savePath=isExistDir(saveDir);
                try{
                    is=response.body().byteStream();
                    long total=response.body().contentLength();
                    File file=new File(savePath,getNameFromUrl(saveDir));
                    fos=new FileOutputStream(file);
                    long sum=0;
                    while((len = is.read(buf))!=-1){
                        fos.write(buf,0,len);
                        sum+=len;
                        int progress=(int)(sum*1.0f/total*100);
                        //下载中
                        Message message=Message.obtain();
                        message.what=DOWNLOAD_PROGRESS;
                        message.obj=progress;
                        mHandler.sendMessage(message);

                    }
                    fos.flush();
                    //下载完成
                    Message message=Message.obtain();
                    message.what=DOWNLOAD_SUCCESS;
                    message.obj=file.getAbsolutePath();
                    Bundle bundle = new Bundle();
                    bundle.putString(DOWNLOAD_ARG_1,arg1);
                    bundle.putString(DOWNLOAD_ARG_2,arg2);
                    message.setData(bundle);
                    mHandler.sendMessage(message);
                }catch (Exception e){
                    Logger.d("TTT","Exception:"+e);
                    Message message=Message.obtain();
                    message.what=DOWNLOAD_FAIL;
                    mHandler.sendMessage(message);
                }finally{
                    try{
                        if(is!=null)
                            is.close();
                    }catch (IOException e){

                    }
                    try {
                        if(fos!=null){
                            fos.close();
                        }
                    }catch (IOException e){

                    }
                }
            }
        });
    }

    private String getNameFromUrl(String url) {
        return url.substring(url.lastIndexOf("/")+1);
    }


    private String isExistDir(String saveDir) throws IOException {
        if (StringUtils.isNotBlank(saveDir)) {
            String tempDir = saveDir.substring(0, saveDir.lastIndexOf("/"));
            Logger.d("TTT","tempDir:"+tempDir);
            File downloadFile = new File(tempDir);
            if (!downloadFile.mkdirs()) {
                downloadFile.createNewFile();
            }
            String savePath = downloadFile.getAbsolutePath();
            return savePath;
        }else{
            return "";
        }
    }




    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case DOWNLOAD_PROGRESS:
                    listener.onDownloading((Integer) msg.obj);
                    break;
                case DOWNLOAD_FAIL:
                    listener.onDownloadFailed();
                    break;
                case DOWNLOAD_SUCCESS:
                    listener.onDownloadSuccess((String) msg.obj, msg.getData().getString(DOWNLOAD_ARG_1), msg.getData().getString(DOWNLOAD_ARG_2));
                    break;
            }
        }
    };


    OnDownloadListener listener;
    public interface OnDownloadListener{
        /**
         * 下载成功
         */
        void onDownloadSuccess(String path, String type, String version);
        /**
         * 下载进度
         * @param progress
         */
        void onDownloading(int progress);
        /**
         * 下载失败
         */
        void onDownloadFailed();
    }
}
