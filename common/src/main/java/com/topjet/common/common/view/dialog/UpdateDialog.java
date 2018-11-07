package com.topjet.common.common.view.dialog;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.topjet.common.R;
import com.topjet.common.base.busManger.BusManager;
import com.topjet.common.base.busManger.Subscribe;
import com.topjet.common.base.controller.BackGroundController;
import com.topjet.common.base.dialog.BaseDialog;
import com.topjet.common.common.modle.event.ProgressEvent;
import com.topjet.common.common.modle.response.AppUpgradeResponse;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.controller.DownloadChangeObserver;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.ScreenUtils;
import com.topjet.common.utils.StringUtils;
import com.topjet.common.utils.Toaster;

import butterknife.ButterKnife;

/**
 * creator: zhulunjun
 * time:    2017/11/2
 * describe: 版本更新的dialog
 */

public class UpdateDialog extends BaseDialog implements View.OnClickListener {

    private TextView tvContent, tvProgress;
    private ImageView ivClose, ivLoadingImg;
    /**
     * 服务器数据
     */
    private AppUpgradeResponse result;
    private LinearLayout llDownload, llUpdate;
    private DownloadChangeObserver downloadChangeObserver;
    private ProgressBar progressBar;
    private TextView btnBackground;

    private static final int FINISH = 100;

    ViewGroup.MarginLayoutParams marginImg;
    LinearLayout.LayoutParams layoutParamsImg;

    /**
     * 百分之一进度汽车图片所前进的距离
     */
    private int progressValue = 0;
    /**
     * 初始百分百，图片长度所占的百分比
     */
    private int initialValue = 0;
    private int imgTop;
    private int imgLeft = 0;

    public UpdateDialog(Context context, AppUpgradeResponse result) {
        super(context, R.layout.dialog_update);
        this.result = result;
        BusManager.getBus().register(this);
        setView();
        setData();
    }

    @Override
    public void setOnDismissListener(@Nullable OnDismissListener listener) {
        super.setOnDismissListener(listener);
        BusManager.getBus().unregister(this);
    }

    public void setView() {
        setWindowAnimations(R.style.right_top_out_dialog_anim);
        // 外部不能取消
        setCanceledOnTouchOutside(false);

        ivClose = ButterKnife.findById(view, R.id.iv_close);
        tvContent = ButterKnife.findById(view, R.id.tv_content);
        llDownload = ButterKnife.findById(view, R.id.ll_download);
        llUpdate = ButterKnife.findById(view, R.id.ll_update);
        progressBar = ButterKnife.findById(view, R.id.progressBar);
        ivLoadingImg = ButterKnife.findById(view, R.id.iv_loading_img);
        tvProgress = ButterKnife.findById(view, R.id.tv_progress);
        btnBackground = ButterKnife.findById(view, R.id.tv_btn_background);

        ivClose.setOnClickListener(this);
        btnBackground.setOnClickListener(this);
        ButterKnife.findById(view, R.id.tv_btn_single_confirm).setOnClickListener(this);

        //根据司机/货主版，设置颜色样式
        showStyle();
    }


    /**
     * 设置数据
     */
    public void setData() {
        if (result != null) {
            // 是否强制更新
            if (result.getIsForced().equals(AppUpgradeResponse.FORCED)) {
                // 返回按钮不能取消
                setCancelable(false);
                // 不显示后台更新 不显示关闭按钮
                btnBackground.setVisibility(View.GONE);
                ivClose.setVisibility(View.GONE);
            }
            tvContent.setText(result.getDescription());
        }

        marginImg = new ViewGroup.MarginLayoutParams(ivLoadingImg.getLayoutParams());
        imgTop = ScreenUtils.dp2px(mContext, 45);
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.iv_close) {
            // 关闭
            dismiss();
        } else if (i == R.id.tv_btn_single_confirm) {
            // 下载
            download();
        } else if (i == R.id.tv_btn_background) {
            // 后台更新 关闭弹窗
            dismiss();
        }
    }


    /**
     * 获取图片前进的长度
     */
    private void getProgressValue() {
        ViewTreeObserver vto = llDownload.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (progressBar != null && ivLoadingImg != null) {
                    int progressW = progressBar.getMeasuredWidth();
                    int imgW = ivLoadingImg.getMeasuredWidth();

                    progressValue = (progressW - imgW) / 100;
                    // 进度达到图片宽度之后，图片才开始动
                    initialValue = imgW / progressValue - 10;
                    progressValue = (progressW - imgW) / (100 - initialValue);

                    Logger.d("宽度", progressW + " " + imgW + " " + progressValue + " " + imgTop);
                }
            }
        });
    }

    /**
     * 下载
     */
    private void download() {
        setWindowAnimations(R.style.top_out_dialog_anim);
        if (StringUtils.isBlank(result.getDownloadAddress())) {
            Toaster.showShortToast("下载地址为空，请联系客服！");
            return;
        }
        llUpdate.setVisibility(View.GONE);
        llDownload.setVisibility(View.VISIBLE);

        getProgressValue();

        //开始下载
        downloadChangeObserver = new DownloadChangeObserver(mContext, null);
        new BackGroundController(mContext).downloadApk(result.getDownloadAddress(), downloadChangeObserver);


    }

    /**
     * 根据不同版本显示不同样式
     */
    private void showStyle() {
        if (CMemoryData.isDriver()) {
            progressBar.setProgressDrawable(ContextCompat.getDrawable(mContext, R.drawable.progressbar_blue));
            btnBackground.setBackgroundResource(R.drawable.shape_bg_btn_border_blue);
            btnBackground.setTextColor(mContext.getResources().getColor(R.color.v3_common_blue));
        } else {
            progressBar.setProgressDrawable(ContextCompat.getDrawable(mContext, R.drawable.progressbar));
            btnBackground.setBackgroundResource(R.drawable.shape_bg_btn_border_green);
            btnBackground.setTextColor(mContext.getResources().getColor(R.color.v3_common_green));
        }
    }

    /**
     * 接收下载进度信息
     */
    @Subscribe
    public void onEvent(ProgressEvent progressEvent) {
        int progress = progressEvent.getProgress();
        int oldProgress = progressBar.getProgress();
        progressBar.setProgress(progress);
        tvProgress.setText("已完成：" + progress + "%");
        if (progress >= initialValue) {
            //每增加一个百分比，将对应margin增加
            int intImgLocation = (progress - oldProgress) * progressValue;
            setImageViewLocation(intImgLocation);
        }
        if (progress == FINISH) {
            dismiss();//下载完成，关闭当前对话框
        }
    }

    /**
     * 设置图片的位置
     *
     * @param left:增加的位置。px
     */
    private void setImageViewLocation(int left) {
        imgLeft += left;
        marginImg.setMargins(imgLeft, imgTop, 0, 0);
        layoutParamsImg = new LinearLayout.LayoutParams(marginImg);
        ivLoadingImg.setLayoutParams(layoutParamsImg);
    }
}
