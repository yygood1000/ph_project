package com.topjet.common.jpush;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.topjet.common.config.CPersisData;
import com.topjet.common.utils.Logger;

/**
 * creator: zhulunjun
 * time:    2017/11/23
 * describe: activity 生命周期的回调实现
 */

public class ActivityLifecycleListener implements Application.ActivityLifecycleCallbacks {

    private int refCount = 0;

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {
        refCount++;
        CPersisData.setIsBackground(false);
        Logger.d("ActivityLifecycleListener Started refCount = "+refCount);
    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
        refCount--;
        Logger.d("ActivityLifecycleListener Stopped refCount = "+refCount);
        if(refCount == 0){
            // 处于后台
            CPersisData.setIsBackground(true);
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
