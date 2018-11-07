package com.topjet.common.base.listener;

import android.view.View;

/**
 * 自定义Tab的点击事件,不覆盖原OnClickListener.
 * Created by yy
 */
public interface OnTabClickListener {
    void onClick(View v, int currentIndex);

    void onCenterClick(View v, boolean isChangeToSearch);
}
