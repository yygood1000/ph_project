package com.topjet.common.adv;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.topjet.common.R;
import com.topjet.common.adv.modle.bean.AnnouncementBean;
import com.topjet.common.adv.modle.response.GetAnnouncementResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yy on 2017/11/15.
 * <p>
 * 对公告信息进行加工
 */

public class ProcessAnnouncementData {
    public interface OnAnnouncementClikcListener {
        void onClick(View v);
    }

    /**
     * 获取公告集合
     */
    public static List<View> getAnnouncementViews(Context mContext, GetAnnouncementResponse data,
                                                  final OnAnnouncementClikcListener clikcListener) {
        List<View> mViews = new ArrayList<>();
        for (AnnouncementBean item : data.getList()) {
            mViews.add(getTextView(mContext, item.getAnnouncement_id(), item.getContent(), clikcListener));
        }


        return mViews;
    }

    /**
     * 获取单个公告TextView
     *
     * @param id            需要进行跳转的公告Id
     * @param content       公告内容
     * @param clikcListener 公告点击事件
     */
    private static TextView getTextView(Context mContext, String id, String content, final OnAnnouncementClikcListener
            clikcListener) {
        TextView txtview = (TextView) View.inflate(mContext, R.layout.item_text_banner, null);
        txtview.setTag(id);
        txtview.setText(content);
        txtview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clikcListener != null) {
                    clikcListener.onClick(v);
                }
            }
        });
        return txtview;
    }

}
