package com.topjet.common.db.bean;

import com.foamtrace.photopicker.ImageModel;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.topjet.common.db.DBManager;
import com.topjet.common.db.greendao.ImageMessageBeanDao;
import com.topjet.common.utils.FileUtils;
import com.topjet.common.utils.Logger;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.query.QueryBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * creator: zhulunjun
 * time:    2017/12/29
 * describe: 保存发生和接收到的图片消息，以供显示
 */
@Entity
public class ImageMessageBean {
    @Id(autoincrement = true)
    private Long id; // 主键

    private String messageId;
    private String imagePath;
    private String imageRemotePath;
    private long time;
    private String conversationId;
    @Generated(hash = 933475068)
    public ImageMessageBean(Long id, String messageId, String imagePath, String imageRemotePath, long time,
            String conversationId) {
        this.id = id;
        this.messageId = messageId;
        this.imagePath = imagePath;
        this.imageRemotePath = imageRemotePath;
        this.time = time;
        this.conversationId = conversationId;
    }
    @Generated(hash = 1488425678)
    public ImageMessageBean() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getMessageId() {
        return this.messageId;
    }
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
    public String getImagePath() {
        return this.imagePath;
    }
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
    public String getImageRemotePath() {
        return this.imageRemotePath;
    }
    public void setImageRemotePath(String imageRemotePath) {
        this.imageRemotePath = imageRemotePath;
    }
    public long getTime() {
        return this.time;
    }
    public void setTime(long time) {
        this.time = time;
    }
    public String getConversationId() {
        return this.conversationId;
    }
    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    @Override
    public String toString() {
        return "ImageMessageBean{" +
                "id=" + id +
                ", messageId='" + messageId + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", imageRemotePath='" + imageRemotePath + '\'' +
                ", time=" + time +
                ", conversationId='" + conversationId + '\'' +
                '}';
    }

    /**
     * 保存到数据库
     */
    public void saveImageMessageDB(String imagePath, String imageRemotePath, String messageId, String conversationId){
        ImageMessageBean imageMessageBean = new ImageMessageBean();
        imageMessageBean.setImagePath(imagePath);
        imageMessageBean.setMessageId(messageId);
        imageMessageBean.setImageRemotePath(imageRemotePath);
        imageMessageBean.setTime(System.currentTimeMillis());
        imageMessageBean.setConversationId(conversationId);
        if(query(messageId).size() == 0) {
            DBManager.getInstance().getDaoSession().getImageMessageBeanDao().save(imageMessageBean);
            Logger.d("保存图片对象 "+imageMessageBean.toString());
        }

    }

    /**
     * 根据消息体保存
     */
    public void saveImageMessageDB(EMMessage message){
        if(message.getType() == EMMessage.Type.IMAGE) {
            EMImageMessageBody imgBody = (EMImageMessageBody) message.getBody();
            String imagePath;
            if(!new File(imgBody.getLocalUrl()).exists()){
                imagePath = imgBody.getRemoteUrl();
            } else {
                imagePath = imgBody.getLocalUrl();
            }

            saveImageMessageDB(imagePath, imgBody.getRemoteUrl(), message.getMsgId(), message.conversationId());
        }
    }

    /**
     * 查询
     */
    private List<ImageMessageBean> query(String messageId){
        QueryBuilder qb = DBManager
                .getInstance()
                .getDaoSession()
                .getImageMessageBeanDao()
                .queryBuilder();

        return (List<ImageMessageBean>) qb.where(ImageMessageBeanDao.Properties.MessageId.eq(messageId)).list();
    }

    /**
     * 获取数据
     */
    public ArrayList<ImageModel> getImageModels(String conversationId){
        ArrayList<ImageModel> datas = new ArrayList<>();
        QueryBuilder qb = DBManager
                .getInstance()
                .getDaoSession()
                .getImageMessageBeanDao()
                .queryBuilder();
        List<ImageMessageBean> imageMessageBeans =qb.where(ImageMessageBeanDao.Properties.ConversationId.eq(conversationId)).list();

        for (ImageMessageBean bean : imageMessageBeans){
            ImageModel model = new ImageModel();
            model.setTime(bean.getTime());
            model.setPath(bean.getImagePath());
            model.setName(bean.getMessageId());
            datas.add(model);
        }
        Collections.sort(datas, new FileUtils.FileComparator());
        return datas;
    }

}
