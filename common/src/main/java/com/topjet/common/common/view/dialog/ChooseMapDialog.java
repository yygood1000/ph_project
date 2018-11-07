package com.topjet.common.common.view.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.topjet.common.R;
import com.topjet.common.base.dialog.BaseDialog;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.Toaster;

import java.io.File;
import java.net.URISyntaxException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * creator: zhulunjun
 * time:    2017/11/6
 * describe: 选择地图去导航
 * 百度或者高德
 */

public class ChooseMapDialog extends BaseDialog implements View.OnClickListener {

    String addressName;
    String endLa, endLon;

    public ChooseMapDialog(Context context, String addressName, String endLa, String endLon) {
        super(context, R.layout.dialog_choose_map);
        this.addressName =addressName;
        this.endLa = endLa;
        this.endLon = endLon;
    }

    @Override
    public void initView() {
        super.initView();
        setShowBottom();

        ButterKnife.findById(view, R.id.tv_baidu_map).setOnClickListener(this);
        ButterKnife.findById(view, R.id.tv_gaode_map).setOnClickListener(this);
        ButterKnife.findById(view, R.id.tv_cancel).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_baidu_map) {
            setUpBaiduAPPByMine(endLa,endLon, addressName);
        } else if (i == R.id.tv_gaode_map) {
            setUpGaodeAppByMine(endLa, endLon, addressName);
        } else if (i == R.id.tv_cancel) {

        }
        dismiss();
    }

    /**
     * 我的位置到终点通过百度地图
     */
    void setUpBaiduAPPByMine(String la, String lo,String addressName) {
        try {
            //("baidumap://map/direction?region=beijing&origin=39.98871,116.43234&destination=name:西直门&mode=driving")

            Intent intent = Intent.getIntent("intent://map/direction?" +
                    "origin=我的位置&" +
                    "destination=" + addressName + "&" +
                    "mode=driving&" +
                    "src=yourCompanyName|yourAppName#Intent;" +
                    "scheme=bdapp;" +
                    "package=com.baidu.BaiduMap;end");
//            Intent intent = new Intent();
//            intent.setData(Uri.parse("baidumap://map/direction?" +
//                    "origin=我的位置&" +
//                    "destination=latlng:"+
//                    endLa+","+
//                    endLon+"&" +
//                    "mode=driving"));
//            intent.setPackage("com.baidu.BaiduMap");
            if (isInstallByRead("com.baidu.BaiduMap")) {
                mContext.startActivity(intent);
                Logger.d("百度地图客户端已经安装");
            } else {
                Logger.d("没有安装百度地图客户端");
                webByMine(la,lo,addressName);

            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.d("百度地图 "+e.getMessage());
            webByMine(la,lo,addressName);
        }
    }

    /**
     * 我的位置BY高德
     */
    void setUpGaodeAppByMine(String la, String lo, String addressName) {
        try {
            Intent intent = Intent.getIntent("androidamap://route?" +
                    "sourceApplication=softname&" +
                    "sname=我的位置&" +
                    "dlat=" + la + "&" +
                    "dlon=" + lo + "&" +
                    "dname=" + addressName + "&" +
                    "dev=0&m=0&t=0"); // t = 0（驾车）= 1（公交）= 2（步行）= 3（骑行）= 4（火车）= 5（长途客车）
            if (isInstallByRead("com.autonavi.minimap")) {
                mContext.startActivity(intent);
                Logger.d("高德地图客户端已经安装");
            } else {
                webByMine(la,lo,addressName);
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
            webByMine(la,lo,addressName);
        }
    }

    /**
     * 百度地图定位
     */
    private void webByMine(String la, String lo, String addressName){
        String url = "http://api.map.baidu.com/direction?origin=latlng:"+la+","+lo+"|name:我的位置&destination="+addressName+"+&mode=driving&region=西安&output=html&src=yourCompanyName|yourAppName";

//        String url = String.format("http://api.map.baidu.com/direction?origin=latlng:%s,%s|name:%s&destination=%s&mode=driving&region=&output=html&src=yourCompanyName|yourAppName",
//                lo,la,"我的位置",addressName);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        mContext.startActivity(intent);
    }

    /**
     * 判断是否安装目标应用
     *
     * @param packageName 目标应用安装后的包名
     * @return 是否已安装目标应用
     */
    private boolean isInstallByRead(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }
}
