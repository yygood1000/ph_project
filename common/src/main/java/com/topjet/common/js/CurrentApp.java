package com.topjet.common.js;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;

import com.topjet.common.App;
//import com.topjet.common.base.logic.MainLogic;
import com.topjet.common.base.dialog.AutoDialog;
import com.topjet.common.base.dialog.AutoDialog.OnBothConfirmListener;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.SystemUtils;

/**
 * App对象类
 *
 * @author huangb
 */
public class CurrentApp {

    private Context context;
    public boolean b = false;
    public static String version;
    public static String updateVersion;

    public CurrentApp(Context context) {
        this.context = context;
    }

    /**
     * 获得系统JS脚本公共方法
     *
     * @return
     */
    public String getCommonJS() {
        String strScript = "";
        strScript += "function alert(strMsg){ return currentApp.alert(strMsg); }";//提示对话框公共方法
        strScript += "function confirm(strMsg){ return currentApp.confirm(strMsg); }";//确认对话公共方法
        strScript += "function simpleTextDownload(strURL){ return currentApp.simpleTextDownload(strURL); }";//公共方法
        strScript += "function getSystem(){ return currentApp.getSystem(); }";//获取所属哪个系统公共方法

        return strScript;
    }

    /**
     * 对话框
     *
     * @param strMsg：信息提示
     */
    public void alert(String strMsg) {
        final Looper lp = Looper.getMainLooper();
        try {
            //弹出提示框
            final AutoDialog dialog = new AutoDialog(context);
            dialog.setTitle("温馨提示");
            dialog.setContent(strMsg);
            dialog.setSingleText("确定");
            dialog.setOnSingleConfirmListener(new AutoDialog.OnSingleConfirmListener() {
                @Override
                public void onClick() {
                    dialog.dismiss();
                    lp.quit();
                }
            });
            dialog.toggleShow();
            dialog.setCancelable(false);

            Looper.loop();//进入死循环，为了等用户点击“确定”或“取消”按扭。这样实现不马上执行下面代码

        } catch (Exception e) {

        }
    }

    /**
     * 确认对话框
     *
     * @param strMsg：信息提示
     * @return：用户的操作结果。确定为true,取消为false。
     */
    public boolean confirm(String strMsg) {
        final Looper lp = Looper.getMainLooper();
        try {
            /////显示进度对话框///开始////
            final AutoDialog dialog = new AutoDialog(context);
            dialog.setTitle("温馨提示");
            dialog.setContent(strMsg);
            dialog.setLeftText("取消");
            dialog.setRightText("确定");
            dialog.setOnBothConfirmListener(new OnBothConfirmListener() {

                /**
                 * java.lang.IllegalStateException:
                 * Main thread not allowed to quit.
                 * at android.os.MessageQueue.quit(MessageQueue.java:239)
                 * at android.os.Looper.quit(Looper.java:291)
                 * at com.topjet.common.js.CurrentApp$2.onLeftClick(CurrentApp.java:97)
                 *
                 * 主线程不允许退出
                 */
                @Override
                public void onLeftClick() {
                    //取消
                    b = false;
                    dialog.dismiss();
                    lp.quit();
                }

                @Override
                public void onRightClick() {
                    //确定
                    b = true;
                    dialog.dismiss();
                    lp.quit();
                }

            });
            dialog.toggleShow();
            dialog.setCancelable(false);
            /////显示进度对话框///结束////

            Looper.loop();//进入死循环，为了等用户点击“确定”或“取消”按扭。这样实现不马上执行下面代码

        } catch (Exception e) {

        }

        return b;
    }

    /**
     * 从给定的URL地址，下载一个UTF8编码格式的纯文本、脚本、XML文件等
     *
     * @param strURL
     * @return
     */
    public String simpleTextDownload(String strURL) {
        String retStr = "";
        try {
//			retStr = HttpUtil.getRequest(strURL, "UTF-8");
        } catch (Exception e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
            retStr = "" + e.getMessage();
        }
        return retStr;
    }

    /**
     * 登出
     */
    public void logout() {
        //new MainLogic(context).doLogOut(LogOutEvent.JS);
    }

    /**
     * 退出系统
     */
    public void exit() {
        try {
            Logger.i("=======exit=======", "强制退出");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    SystemUtils.killProcess(App.getInstance());
                }
            }, 300);
        } catch (Exception e) {
            Logger.i("=======exit=======", "" + e.getMessage());
        }
    }

    /**
     * 用于跳转到App的下载页面。
     */
    public void openDownloadPage() {
        try {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://d.566560.com/appdown.html"));//设置一个URI地址
            context.startActivity(intent);
        } catch (Exception e) {
            Logger.i("=======openDownloadPage=======", "" + e.getMessage());
        }
    }

    /**
     * 获取所属是哪个系统。1为Android，2为IOS
     *
     * @return
     */
    public int getSystem() {
        return 1;
    }
}
