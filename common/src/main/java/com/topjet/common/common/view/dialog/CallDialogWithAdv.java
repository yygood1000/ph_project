package com.topjet.common.common.view.dialog;

import android.app.Activity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.topjet.common.R;
import com.topjet.common.base.dialog.BaseDialog;
import com.topjet.common.utils.StringUtils;
import com.topjet.common.widget.CornersWebView;
import com.topjet.common.widget.MyWebView;

/**
 * 拨打电话弹窗
 * 有广告
 */
public class CallDialogWithAdv extends BaseDialog implements View.OnClickListener {
    private Activity activity = null;
    //    private PopupWindow popupWindow = null;
//    private View contentView = null;
    private String phoneNumber = "";
    private boolean needStatistics = false;     // 是否上传通话时长到服务器
    private onClickCallListener onClickCall = null;

    public CallDialogWithAdv(Activity activity, onClickCallListener onClickCall) {
        super(activity, R.layout.pop_call);
        this.activity = activity;
        this.onClickCall = onClickCall;

        Window window = this.getWindow();
        WindowManager.LayoutParams p = window.getAttributes(); // 获取对话框当前的参数值
        p.height = WindowManager.LayoutParams.WRAP_CONTENT; // 改变的是dialog框在屏幕中的位置而不是大小
        p.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(p);
    }

    /**
     * @param needStatistics 是否上传通话时长到服务器
     */
    public CallDialogWithAdv initPop(String phoneNumber, boolean needStatistics, String name, String note, String bottomText, String url) {
//        popupWindow = new PopupWindow(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
//        LayoutInflater inflater = LayoutInflater.from(activity);
//        contentView = inflater.inflate(R.layout.pop_call, null);

        ImageView iv_close = (ImageView) view.findViewById(R.id.iv_close);
        final CornersWebView web_view = (CornersWebView) view.findViewById(R.id.web_view);
        TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
        TextView tv_phone = (TextView) view.findViewById(R.id.tv_phone);
        TextView tv_note = (TextView) view.findViewById(R.id.tv_note);
        TextView tv_call = (TextView) view.findViewById(R.id.tv_call);

        this.phoneNumber = phoneNumber;
        this.needStatistics = needStatistics;
        // 姓名只显示4个字符
        if (StringUtils.isNotBlank(name) && name.length() > 4) {
            name = name.substring(0, 4);
            name = name + "...";
        }
        web_view.loadUrl(url);

        tv_name.setText(name);
        tv_phone.setText(phoneNumber);
        tv_note.setText(note);
        if (StringUtils.isNotBlank(bottomText)) {
            tv_call.setText(bottomText);
        }

        iv_close.setOnClickListener(this);
        tv_call.setOnClickListener(this);
        web_view.setOnLoadErrorListener(new MyWebView.OnLoadErrorListener() {
            @Override
            public void onError() {
                web_view.setVisibility(View.GONE);
            }
        });

//        popupWindow.setContentView(contentView);
//        popupWindow.setBackgroundDrawable(new BitmapDrawable());
//        popupWindow.setOutsideTouchable(true);
//        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//        popupWindow.setAnimationStyle(R.style.center_dialog_anim);
//        popupWindow.setClippingEnabled(false);     // 可以超出屏幕，状态栏终于可以变半透明啦
//
//        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                PopupAlphaAnimatorUtil.setOutAlphaAnim(activity);
//            }
//        });


        return this;
    }

    public void showPop(View parent) {
        show();
//        PopupAlphaAnimatorUtil.setInAlphaAnim(activity);
//        popupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
    }

    /**
     * 关闭
     */
    public void closePop() {
        dismiss();
//        if (popupWindow != null && popupWindow.isShowing()) {
//            popupWindow.dismiss();
//        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_close) {
            dismiss();
        } else if (id == R.id.tv_call) {
            dismiss();
            onClickCall.onClick(activity, phoneNumber, needStatistics);
        }
    }

    public interface onClickCallListener {
        void onClick(Activity activity, String phone, boolean needStatistics);
    }
}
