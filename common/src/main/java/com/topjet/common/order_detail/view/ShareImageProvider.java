package com.topjet.common.order_detail.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.topjet.common.R;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.order_detail.modle.response.ShareGoodsResponse;
import com.topjet.common.order_detail.view.adapter.ShareGoodsAdapter;
import com.topjet.common.utils.FileUtils;
import com.topjet.common.utils.PathHelper;
import com.topjet.common.utils.StringUtils;
import com.topjet.common.utils.TimeUtils;
import com.topjet.common.utils.Toaster;
import com.topjet.wallet.utils.BitmapUtils;

import butterknife.ButterKnife;

/**
 * creator: zhulunjun
 * time:    2017/10/26
 * describe: 分享需要的图片生成
 */

public class ShareImageProvider {


    TextView tvUserInfo;
    LinearLayout llTop;
    RecyclerView rvOrder;
    TextView tvTime;
    TextView tvAppName;
    View mView;

    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public ShareImageProvider(Context mContext) {
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
        initView();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        mView = mLayoutInflater.inflate(R.layout.layout_share_goods, null);
        tvUserInfo = ButterKnife.findById(mView, R.id.tv_user_info);
        llTop = ButterKnife.findById(mView, R.id.ll_top);
        rvOrder = ButterKnife.findById(mView, R.id.rv_order);
        tvTime = ButterKnife.findById(mView, R.id.tv_time);
        tvAppName = ButterKnife.findById(mView, R.id.tv_app_name);
        int color;
        if (CMemoryData.isDriver()) {
            color = mContext.getResources().getColor(R.color.v3_common_blue);
        } else {
            color = mContext.getResources().getColor(R.color.v3_common_green);
        }
        llTop.setBackgroundColor(color);
        tvAppName.setTextColor(color);
        rvOrder.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));

    }

    /**
     * 设置数据
     *
     * @param data
     */
    private void setImageData(ShareGoodsResponse data) {
        if (data == null) return;
        if (data.getShare_goods_lists() != null) {
            ShareGoodsAdapter adapter = new ShareGoodsAdapter();
            adapter.addData(data.getShare_goods_lists());
            rvOrder.setAdapter(adapter);
        }
        String userInfo = data.getOwner_name() + " " + data.getOwner_mobile();
        tvUserInfo.setText(userInfo);

        tvTime.setText(StringUtils.format(R.string.share_time, TimeUtils.showTimeDayHours(data.getShare_time())));


    }


    /**
     * 将View 转成bitmap
     *
     * @param v
     * @return
     */
    private Bitmap createViewBitmap(View v) {
        Bitmap bitmap = null;
        try {
            v.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
            v.buildDrawingCache(true);
            bitmap = v.getDrawingCache(true);
        } catch (Exception e) {
            Toaster.showLongToast("生成分享图片失败");
        }
        return bitmap;
    }


    /**
     * 返回生成的图片路径
     * @return
     */
    public String getShareImage(ShareGoodsResponse data) {
        setImageData(data);
        Bitmap bitmap = createViewBitmap(mView);
        String path = PathHelper.getShareImage() + "/ordersshare.jpg";
        boolean b = FileUtils.writeFile(path,
                BitmapUtils.Bitmap2InputStream(bitmap));// 以指定的路径重新保存
        if (b) {
            return path;
        } else {
            return "";
        }
    }

}
