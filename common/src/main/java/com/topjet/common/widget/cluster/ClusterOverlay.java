package com.topjet.common.widget.cluster;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.animation.AlphaAnimation;
import com.amap.api.maps.model.animation.Animation;
import com.topjet.common.R;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.utils.Logger;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by yiyi.qi on 16/10/10.
 * 整体设计采用了两个线程,一个线程用于计算组织聚合数据,一个线程负责处理Marker相关操作
 */
public class ClusterOverlay implements
//        AMap.OnCameraChangeListener,
        AMap.OnMarkerClickListener {
    private AMap mAMap;
    private Context mContext;

    // 聚合距离
    private double mClusterDistance;
    // 聚合范围的大小（指点像素单位距离内的点会聚合到一个点显示）
    private int mClusterSize;
    // 当前缩放级别下，地图上1像素点对应的长度，单位米。
    private float mPXInMeters;
    // 是否结束
    private boolean mIsCanceled = false;

    // 需要进行点聚合的数据，（实现ClusterItem接口）
    private List<ClusterItem> mClusterItems;
    // 集合点集合
    private List<Cluster> mClusters;
    //
    private List<Marker> mAddMarkers = new ArrayList<Marker>();

    private ClusterClickListener mClusterClickListener;
    private ClusterRender mClusterRender;

    // 聚合显示元素图片缓存
    private LruCache<Integer, BitmapDescriptor> mLruCache;
    // 处理market添加，更新等操作线程
    private HandlerThread mMarkerHandlerThread = new HandlerThread("addMarker");
    // 处理聚合点算法线程
    private HandlerThread mSignClusterThread = new HandlerThread("calculateCluster");
    private Handler mMarkerhandler;
    private Handler mSignClusterHandler;

    // 添加时的渐变动画
    private AlphaAnimation mADDAnimation = new AlphaAnimation(0, 1);


    /**
     * 构造函数
     *
     * @param clusterSize 聚合范围的大小（指点像素单位距离内的点会聚合到一个点显示）
     */
    public ClusterOverlay(AMap amap, int clusterSize, Context context) {
        this(amap, null, clusterSize, context);
    }

    /**
     * 构造函数,批量添加聚合元素时,调用此构造函数
     *
     * @param clusterItems 聚合元素
     * @param clusterSize  聚合范围的大小（指点像素单位距离内的点会聚合到一个点显示）
     */
    public ClusterOverlay(AMap amap, List<ClusterItem> clusterItems, int clusterSize, Context context) {
        mContext = context;
        this.mAMap = amap;
        mClusterSize = clusterSize;
        // 获取当前缩放级别下，地图上1像素点对应的长度，单位米。
        mPXInMeters = mAMap.getScalePerPixel();
        mClusterDistance = mPXInMeters * mClusterSize;

        mClusters = new ArrayList<>();
        if (clusterItems != null) {
            mClusterItems = clusterItems;
        } else {
            mClusterItems = new ArrayList<>();
        }

//        amap.setOnCameraChangeListener(this);
        // 设置地图Marker点击事件，此处设置的就是聚合点的点击事件
        amap.setOnMarkerClickListener(this);

        initLruCache();
        initThreadHandler();
        assignClusters();
    }

    /**
     * 初始化聚合元素图片缓存
     */
    private void initLruCache() {
        // 默认最多会缓存80张图片作为聚合显示元素图片,根据自己显示需求和app使用内存情况,可以修改数量
        mLruCache = new LruCache<Integer, BitmapDescriptor>(10) {
            protected void entryRemoved(boolean evicted, Integer key, BitmapDescriptor oldValue, BitmapDescriptor
                    newValue) {
                oldValue.getBitmap().recycle();
            }
        };
    }

    /**
     * 初始化Handler
     */
    private void initThreadHandler() {
        mMarkerHandlerThread.start();
        mSignClusterThread.start();
        mMarkerhandler = new MarkerHandler(mMarkerHandlerThread.getLooper());
        mSignClusterHandler = new SignClusterHandler(mSignClusterThread.getLooper());
    }

    /**
     * 第一步： 对点进行聚合
     * 1.对象初始化时调用
     * 2.地图状态变化时调用（目前此种调用不存在，每次移动地图都会重新创建ClusterOverlay）
     */
    private void assignClusters() {
        Logger.i("oye", "开始添加assignClusters==========1");
        mIsCanceled = true;
        // 移除所有正在计算的线程
        mSignClusterHandler.removeMessages(SignClusterHandler.CALCULATE_CLUSTER);
        // 发送一条消息，通知计算线程开始计算聚合点
        mSignClusterHandler.sendEmptyMessage(SignClusterHandler.CALCULATE_CLUSTER);
    }

    /**
     * 处理聚合点算法线程
     */
    private class SignClusterHandler extends Handler {
        static final int CALCULATE_CLUSTER = 0;// 计算聚合点
        static final int CALCULATE_SINGLE_CLUSTER = 1;// 计算单个聚合点

        SignClusterHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message message) {
            switch (message.what) {
                case CALCULATE_CLUSTER:// 计算多个聚合点
                    Logger.i("oye", "计算多个聚合点==========2");
                    calculateClusters();
                    break;
                case CALCULATE_SINGLE_CLUSTER:// 计算单个集合点
                    ClusterItem item = (ClusterItem) message.obj;
                    mClusterItems.add(item);
                    Log.i("yiyi.qi", "计算单个集合点==========2");
                    calculateSingleCluster(item);
                    break;
            }
        }
    }

    /**
     * 第二步： 计算聚合点，然后发送Handler消息，调用addClusterToMap方法开始添加聚合点
     */
    private void calculateClusters() {
        Logger.i("oye", "开始计算聚合点calculateClusters==========3");
        mIsCanceled = false;
        mClusters.clear();
        // 获取当前地图显示的经纬度范围
        LatLngBounds visibleBounds = mAMap.getProjection().getVisibleRegion().latLngBounds;
        // 遍历聚合数据，创建新的聚合点或添加入已有的集合点
        for (ClusterItem clusterItem : mClusterItems) {
            if (mIsCanceled) {
                return;
            }
            LatLng latlng = clusterItem.getPosition();
            if (visibleBounds.contains(latlng)) {
                // 根据一个点获取是否可以依附的聚合点，有则返回那个聚合点持有者
                Cluster cluster = getCluster(latlng, mClusters);
                if (cluster != null) {
                    //  向聚合点内添加单个聚合数据
                    cluster.addClusterItem(clusterItem);
                } else {
                    cluster = new Cluster(latlng);
                    // 向聚合点内添加单个聚合数据
                    cluster.addClusterItem(clusterItem);
                    mClusters.add(cluster);
                }
            } else {
                Logger.i("oye", "聚合点不在显示范围内");
            }
        }
        //复制一份数据，规避同步
        List<Cluster> clusters = new ArrayList<>();
        clusters.addAll(mClusters);
        Message message = Message.obtain();
        message.what = MarkerHandler.ADD_CLUSTER_LIST;
        message.obj = clusters;
        if (mIsCanceled) {
            return;
        }
        mMarkerhandler.sendMessage(message);
    }

    /**
     * 根据一个点获取是否可以依附的聚合点，没有则返回null
     */
    private Cluster getCluster(LatLng latLng, List<Cluster> clusters) {
        for (Cluster cluster : clusters) {
            LatLng clusterCenterPoint = cluster.getCenterLatLng();
            double distance = AMapUtils.calculateLineDistance(latLng, clusterCenterPoint);
            if (distance < mClusterDistance) {
                return cluster;
            }
        }
        return null;
    }

    /**
     * 处理market添加，更新等操作线程
     */
    private class MarkerHandler extends Handler {

        static final int ADD_CLUSTER_LIST = 0;

        static final int ADD_SINGLE_CLUSTER = 1;

        static final int UPDATE_SINGLE_CLUSTER = 2;

        MarkerHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message message) {

            switch (message.what) {
                case ADD_CLUSTER_LIST:// 添加集合点集合
                    List<Cluster> clusters = (List<Cluster>) message.obj;
                    addClusterToMap(clusters);
                    break;
                case ADD_SINGLE_CLUSTER:// 添加单个聚合点
                    Cluster cluster = (Cluster) message.obj;
                    addSingleClusterToMap(cluster);
                    break;
                case UPDATE_SINGLE_CLUSTER:// 更新已加入地图聚合点的样式
                    Cluster updateCluster = (Cluster) message.obj;
                    updateCluster(updateCluster);
                    break;
            }
        }
    }

    /**
     * **************************************************
     * **************将聚合元素添加至地图上*****************
     * **************************************************
     *
     * @param clusters 通过计算得到的聚合集合数据，遍历集合进行聚合点覆盖物添加
     */
    private void addClusterToMap(List<Cluster> clusters) {
        ArrayList<Marker> removeMarkers = new ArrayList<>();
        removeMarkers.addAll(mAddMarkers);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
        MyAnimationListener myAnimationListener = new MyAnimationListener(removeMarkers);
        // 遍历以显示出来的集合点Marker对象，播放清除动画，动画结束移除Marker
        for (Marker marker : removeMarkers) {
            marker.setAnimation(alphaAnimation);
            marker.setAnimationListener(myAnimationListener);
            marker.startAnimation();
        }

        // 循环添加单个聚合点
        for (Cluster cluster : clusters) {
            addSingleClusterToMap(cluster);
        }
    }

    /**
     * 将单个聚合元素添加至地图显示
     */
    private void addSingleClusterToMap(Cluster cluster) {
        Marker marker;
        LatLng latlng = cluster.getCenterLatLng();
        MarkerOptions markerOptions = new MarkerOptions();

        Logger.d("oye", "cluster.getClusterCount()= " + cluster.getClusterCount());
        markerOptions
                .anchor(0.5f, 0.5f)//设置Marker覆盖物的锚点比例
                .icon(getBitmapDes(cluster.getClusterItems(), cluster.getClusterCount()))// 通过聚合点数量，得到聚合点样式
                .position(latlng);// 设置聚合点位置

        marker = mAMap.addMarker(markerOptions);
        // Marker持有形成该聚合点的集合数据
        marker.setObject(cluster);
        marker.setAnimation(mADDAnimation);
        marker.startAnimation();

        // 让聚合点对象持有自身添加后形成的Marker
        cluster.setMarker(marker);
        // 添加入集合点的Marker集合，用于下次聚合时删除这些已添加的点
        mAddMarkers.add(marker);
    }

    /**
     * 更新已加入地图聚合点的样式
     */
    private void updateCluster(Cluster cluster) {
        Marker marker = cluster.getMarker();
        marker.setIcon(getBitmapDes(cluster.getClusterItems(), cluster.getClusterCount()));
    }

    /**
     * 获取每个聚合点的绘制样式
     */
    private BitmapDescriptor getBitmapDes(List<ClusterItem> mClusterItems, int num) {
        BitmapDescriptor bitmapDescriptor = mLruCache.get(num);
        if (bitmapDescriptor == null) {
            View view = LayoutInflater.from(mContext).inflate(R.layout
                    .layout_bg_cluster, null);
            LinearLayout llRoot = (LinearLayout) view.findViewById(R.id.ll_root);
            TextView textView = (TextView) view.findViewById(R.id.tv_count);
            TextView tvUnit = (TextView) view.findViewById(R.id.tv_unit);

            if (mClusterRender != null && mClusterRender.getDrawAble(mClusterItems, num) != null) {
                llRoot.setBackgroundDrawable(mClusterRender.getDrawAble(mClusterItems, num));
            }

            if (num > 1) {
                if (num > 99) {
                    textView.setText("99+");
                } else {
                    textView.setText(num + "");
                }
            }

            if (!CMemoryData.isDriver()) {
                tvUnit.setText("辆");
            }


            bitmapDescriptor = BitmapDescriptorFactory.fromView(view);
            mLruCache.put(num, bitmapDescriptor);
        }
        return bitmapDescriptor;
    }

    /**
     * 在已有的聚合基础上，对添加的单个元素进行聚合
     */
    private void calculateSingleCluster(ClusterItem clusterItem) {
        LatLngBounds visibleBounds = mAMap.getProjection().getVisibleRegion().latLngBounds;
        LatLng latlng = clusterItem.getPosition();
        if (!visibleBounds.contains(latlng)) {
            return;
        }
        Cluster cluster = getCluster(latlng, mClusters);
        if (cluster != null) {
            cluster.addClusterItem(clusterItem);
            Message message = Message.obtain();
            message.what = MarkerHandler.UPDATE_SINGLE_CLUSTER;

            message.obj = cluster;
            mMarkerhandler.removeMessages(MarkerHandler.UPDATE_SINGLE_CLUSTER);
            mMarkerhandler.sendMessageDelayed(message, 5);
        } else {
            cluster = new Cluster(latlng);
            mClusters.add(cluster);
            cluster.addClusterItem(clusterItem);
            Message message = Message.obtain();
            message.what = MarkerHandler.ADD_SINGLE_CLUSTER;
            message.obj = cluster;
            mMarkerhandler.sendMessage(message);
        }
    }

    /**
     * 点击事件
     */
    @Override
    public boolean onMarkerClick(Marker marker) {
        if (mClusterClickListener == null) {
            return true;
        }
        Cluster cluster = (Cluster) marker.getObject();
        if (cluster != null) {
            mClusterClickListener.onClick(marker, cluster.getClusterItems());
            return true;
        }
        return false;
    }

    /**
     * marker渐变动画，动画结束后将Marker删除
     */
    private class MyAnimationListener implements Animation.AnimationListener {
        private List<Marker> mRemoveMarkers;

        MyAnimationListener(List<Marker> removeMarkers) {
            mRemoveMarkers = removeMarkers;
        }

        @Override
        public void onAnimationStart() {
        }

        @Override
        public void onAnimationEnd() {
            for (Marker marker : mRemoveMarkers) {
                marker.remove();
            }
            mRemoveMarkers.clear();
        }

    }

    /* ================================对外提供的API方法================================*/

    public void onDestroy() {
        mIsCanceled = true;
        mSignClusterHandler.removeCallbacksAndMessages(null);
        mMarkerhandler.removeCallbacksAndMessages(null);
        mSignClusterThread.quit();
        mMarkerHandlerThread.quit();
        for (Marker marker : mAddMarkers) {
            marker.remove();

        }
        mAddMarkers.clear();
        mLruCache.evictAll();
    }

    /**
     * 设置聚合点的点击事件
     */
    public void setOnClusterClickListener(ClusterClickListener clusterClickListener) {
        mClusterClickListener = clusterClickListener;
    }

    /**
     * 设置聚合元素的渲染样式，不设置则默认为气泡加数字形式进行渲染
     */
    public void setClusterRenderer(ClusterRender render) {
        mClusterRender = render;
    }


    /**
     * 添加一个聚合点
     */
    public void addClusterItem(ClusterItem item) {
        Message message = Message.obtain();
        message.what = SignClusterHandler.CALCULATE_SINGLE_CLUSTER;
        message.obj = item;
        mSignClusterHandler.sendMessage(message);
    }
    /* ================================对外提供的API方法================================*/

    /* 跟随地图状态变化进行点聚合
    @Override
    public void onCameraChange(CameraPosition arg0) {
    }

    @Override
    public void onCameraChangeFinish(CameraPosition arg0) {
        mPXInMeters = mAMap.getScalePerPixel();
        mClusterDistance = mPXInMeters * mClusterSize;
        assignClusters();
    }
    */
}