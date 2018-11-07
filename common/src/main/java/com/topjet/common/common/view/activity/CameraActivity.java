package com.topjet.common.common.view.activity;

import android.Manifest;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.topjet.common.R;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.utils.CameraUtil;
import com.topjet.common.utils.ImageUtil;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.ScreenUtils;
import com.topjet.common.utils.Toaster;
import com.topjet.common.widget.RotateTextView;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.io.File;
import java.io.IOException;

import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * 自定义相机
 */
public class CameraActivity extends RxAppCompatActivity implements SurfaceHolder.Callback, View.OnClickListener {
    private Camera mCamera;
    private ImageView ivTop;
    private SurfaceView surfaceView;
    private RelativeLayout rl;
    private SurfaceHolder mHolder;
    private int mCameraId = 0;
    private Context context;

    //屏幕宽高
    private int screenWidth;
    private int screenHeight;
    private LinearLayout home_custom_top_relative;
    private ImageView camera_delay_time;
    private View homeCustom_cover_top_view;
    private View homeCustom_cover_bottom_view;
    private View home_camera_cover_top_view;
    private View home_camera_cover_bottom_view;
    private ImageView flash_light;
    private TextView camera_delay_time_text;
    private ImageView camera_square;
    private int index;
    //底部高度 主要是计算切换正方形时的动画高度
    private int menuPopviewHeight;
    //动画高度
    private int animHeight;
    //闪光灯模式 0:关闭 1: 开启 2: 自动
    private int light_num = 0;
    //延迟时间
    private int delay_time;
    private int delay_time_temp;
    private boolean isview = false;
    private boolean is_camera_delay;
    private ImageView camera_frontback;
    private ImageView camera_close;
    private RelativeLayout homecamera_bottom_relative;
    private ImageView img_camera;
    private int picHeight;
    private RxPermissions rxPermissions;

    public final static String TYPE = "TYPE_TYPE";        //type
    public final static String TYPE_IDCARD = "id_card";        //身份证正面照
    public final static String TYPE_AVATAR = "avatar";        // 头像

    private RotateTextView rtv_please_move = null; //请把身份证正面移入框内
    private RotateTextView rtv_cancel = null; // 取消
    private TextView tv_cancel = null;      // 取消
    private ImageView camera_frontback2 = null; // 切换前后摄像头

    String type = null;     // 外部传参

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_camera);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        context = this;

        Intent intent = getIntent();
        if (intent != null) {
            type = intent.getStringExtra(TYPE);
        }

        rtv_please_move = (RotateTextView) findViewById(R.id.rtv_please_move);
        rtv_please_move.setDegrees(90);

        rtv_cancel = (RotateTextView) findViewById(R.id.rtv_cancel);
        rtv_cancel.setDegrees(90);
        rtv_cancel.setOnClickListener(this);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(this);

        camera_frontback2 = (ImageView) findViewById(R.id.camera_frontback2);
        camera_frontback2.setOnClickListener(this);

        rxPermissions = new RxPermissions(this);
        rxPermissions.setLogging(true);
        rxPermissions
                .requestEach(// 写入需要获取的运行时权限
                        Manifest.permission.CAMERA)
                .subscribe(new Consumer<Permission>() {// 观察者
                               @Override
                               public void accept(Permission permission) {// 请求权限的返回结果
                                   if (permission.name.equals(Manifest.permission.CAMERA)) {
                                       // 当CAMERA权限获取成功时，permission.granted=true
                                       if (permission.granted) {
                                           showGetPermissionResult(permission);
                                       } else {
                                           finish();
                                       }
                                   }
                               }
                           }
                        ,
                        new Consumer<Throwable>() {// 获取权限发生异常，必须得写，华为系统当用户拒绝后，第二次请求会直接报error
                            @Override
                            public void accept(Throwable t) {
                                finish();
                            }
                        },
                        new Action() {// 结束
                            @Override
                            public void run() {
                            }
                        }
                );

        setStatusBarUpperAPI21();
    }

    // 5.0版本以上
    private void setStatusBarUpperAPI21() {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            //设置透明状态栏,这样才能让 ContentView 向上
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            ViewGroup mContentView = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
            View mChildView = mContentView.getChildAt(0);
            if (mChildView != null) {
                //注意不是设置 ContentView 的 FitsSystemWindows, 而是设置 ContentView 的第一个子 View . 使其不为系统 View 预留空间.
                ViewCompat.setFitsSystemWindows(mChildView, false);
            }
        }
    }

    /**
     * 根据获取权限结果 做相应操作
     */
    private void showGetPermissionResult(Permission permission) {
        if (permission.granted) {// 用户同意
            initMyView();
            initMyData();
        } else if (permission.shouldShowRequestPermissionRationale) {//
            // 用户拒绝，并且勾选了不在提示，MIUI系统默认勾选。在这里可以做自定义弹窗提示用户去设置里开启权限
            // Denied permission without ask never again
            Toaster.showLongToast(R.string.no_has_permission);
        } else {// 用户决绝 ，但是没有勾选不在提示。
            // Denied permission with ask never again
            // Need to go to the settings
            Toaster.showLongToast(R.string.no_has_permission);
        }
    }

    private void initMyView() {
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        rl = (RelativeLayout) findViewById(R.id.rl);
        ivTop = (ImageView) findViewById(R.id.iv_top);
        mHolder = surfaceView.getHolder();
        mHolder.addCallback(this);
        img_camera = (ImageView) findViewById(R.id.img_camera);
        img_camera.setOnClickListener(this);

        //关闭相机界面按钮
        camera_close = (ImageView) findViewById(R.id.camera_close);
        camera_close.setOnClickListener(this);

        //top 的view
        home_custom_top_relative = (LinearLayout) findViewById(R.id.home_custom_top_relative);
        home_custom_top_relative.setAlpha(0.5f);

        //前后摄像头切换
        camera_frontback = (ImageView) findViewById(R.id.camera_frontback);
        camera_frontback.setOnClickListener(this);

        //延迟拍照时间
        camera_delay_time = (ImageView) findViewById(R.id.camera_delay_time);
        camera_delay_time.setOnClickListener(this);

        //正方形切换
        camera_square = (ImageView) findViewById(R.id.camera_square);
        camera_square.setOnClickListener(this);

        //切换正方形时候的动画
        homeCustom_cover_top_view = findViewById(R.id.homeCustom_cover_top_view);
        homeCustom_cover_bottom_view = findViewById(R.id.homeCustom_cover_bottom_view);

        homeCustom_cover_top_view.setAlpha(0.5f);
        homeCustom_cover_bottom_view.setAlpha(0.5f);

        //拍照时动画
        home_camera_cover_top_view = findViewById(R.id.home_camera_cover_top_view);
        home_camera_cover_bottom_view = findViewById(R.id.home_camera_cover_bottom_view);
        home_camera_cover_top_view.setAlpha(1);
        home_camera_cover_bottom_view.setAlpha(1);

        // 闪光灯
        flash_light = (ImageView) findViewById(R.id.flash_light);
        flash_light.setOnClickListener(this);

        camera_delay_time_text = (TextView) findViewById(R.id.camera_delay_time_text);

        homecamera_bottom_relative = (RelativeLayout) findViewById(R.id.homecamera_bottom_relative);
    }

    private void initMyData() {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;

        menuPopviewHeight = screenHeight - screenWidth * 4 / 3;
        animHeight = (screenHeight - screenWidth - menuPopviewHeight - ScreenUtils.dp2px(context, 44)) / 2;

        //这里相机取景框我这是为宽高比3:4 所以限制底部控件的高度是剩余部分
        RelativeLayout.LayoutParams bottomParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, menuPopviewHeight);
        bottomParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        homecamera_bottom_relative.setLayoutParams(bottomParam);

        if (type != null && type.equalsIgnoreCase(TYPE_AVATAR)) {
            mCameraId = 1;
            rtv_cancel.setVisibility(View.GONE);
            tv_cancel.setVisibility(View.VISIBLE);
            camera_frontback2.setVisibility(View.VISIBLE);
            rtv_please_move.setVisibility(View.GONE);
        } else if (type != null && type.equalsIgnoreCase(TYPE_IDCARD)) {
            rtv_cancel.setVisibility(View.VISIBLE);
            tv_cancel.setVisibility(View.GONE);
            camera_frontback2.setVisibility(View.GONE);
            rtv_please_move.setVisibility(View.VISIBLE);
        }
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            int what = msg.what;
            switch (what) {
                case CameraUtil.WHAT.SUCCESS:
                    if (delay_time > 0) {
                        camera_delay_time_text.setText("" + delay_time);
                    }

                    try {
                        if (delay_time == 0) {
                            captrue();
                            is_camera_delay = false;
                            camera_delay_time_text.setVisibility(View.GONE);
                        }
                    } catch (Exception e) {
                        return;
                    }

                    break;

                case CameraUtil.WHAT.ERROR:
                    is_camera_delay = false;
                    break;

            }
        }
    };


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.img_camera) {
            if (isview) {
                if (delay_time == 0) {
                    switch (light_num) {
                        case 0:
                            //关闭
                            CameraUtil.getInstance().turnLightOff(mCamera);
                            break;
                        case 1:
                            CameraUtil.getInstance().turnLightOn(mCamera);
                            break;
                        case 2:
                            //自动
                            CameraUtil.getInstance().turnLightAuto(mCamera);
                            break;
                    }
                    captrue();
                } else {
                    camera_delay_time_text.setVisibility(View.VISIBLE);
                    camera_delay_time_text.setText(String.valueOf(delay_time));
                    is_camera_delay = true;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (delay_time > 0) {
                                //按秒数倒计时
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    mHandler.sendEmptyMessage(CameraUtil.WHAT.ERROR);
                                    return;
                                }
                                delay_time--;
                                mHandler.sendEmptyMessage(CameraUtil.WHAT.SUCCESS);
                            }
                        }
                    }).start();
                }
                isview = false;
            }

        } else if (i == R.id.rtv_cancel || i == R.id.tv_cancel) {
            onBackPressed();
        } else if (i == R.id.camera_square) {
            if (index == 0) {
                camera_square_0();
            } else if (index == 1) {
                camera_square_1();
            }


            //前后置摄像头拍照
        } else if (i == R.id.camera_frontback || i == R.id.camera_frontback2) {
            switchCamera();


            //退出相机界面 释放资源
        } else if (i == R.id.camera_close) {
            if (is_camera_delay) {
                Toast.makeText(CameraActivity.this, "正在拍照请稍后...", Toast.LENGTH_SHORT).show();
                return;
            }
            finish();


            //闪光灯
        } else if (i == R.id.flash_light) {
            if (mCameraId == 1) {
                //前置
                Toaster.showLongToast("请切换为后置摄像头开启闪光灯");
                return;
            }
            Camera.Parameters parameters = mCamera.getParameters();
            switch (light_num) {
                case 0:
                    //打开
                    light_num = 1;
                    flash_light.setImageResource(R.drawable.btn_camera_flash_on);
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);//开启
                    mCamera.setParameters(parameters);
                    break;
                case 1:
                    //自动
                    light_num = 2;
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
                    mCamera.setParameters(parameters);
                    flash_light.setImageResource(R.drawable.btn_camera_flash_auto);
                    break;
                case 2:
                    //关闭
                    light_num = 0;
                    //关闭
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    mCamera.setParameters(parameters);
                    flash_light.setImageResource(R.drawable.btn_camera_flash_off);
                    break;
            }


            //延迟拍照时间
        } else if (i == R.id.camera_delay_time) {
            switch (delay_time) {
                case 0:
                    delay_time = 3;
                    delay_time_temp = delay_time;
                    camera_delay_time.setImageResource(R.drawable.btn_camera_timing_3);
                    break;

                case 3:
                    delay_time = 5;
                    delay_time_temp = delay_time;
                    camera_delay_time.setImageResource(R.drawable.btn_camera_timing_5);
                    break;

                case 5:
                    delay_time = 10;
                    delay_time_temp = delay_time;
                    camera_delay_time.setImageResource(R.drawable.btn_camera_timing_10);
                    break;

                case 10:
                    delay_time = 0;
                    delay_time_temp = delay_time;
                    camera_delay_time.setImageResource(R.drawable.btn_camera_timing_0);
                    break;

            }
        }
    }

    public void switchCamera() {
        releaseCamera();
        mCameraId = (mCameraId + 1) % Camera.getNumberOfCameras();
        mCamera = getCamera(mCameraId);
        if (mHolder != null) {
            startPreview(mCamera, mHolder);
        }
    }

    /**
     * 正方形拍摄
     */
    public void camera_square_0() {
        camera_square.setImageResource(R.drawable.btn_camera_size1_n);

        //属性动画
        ValueAnimator anim = ValueAnimator.ofInt(0, animHeight);
        anim.setDuration(300);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int currentValue = Integer.parseInt(animation.getAnimatedValue().toString());
                RelativeLayout.LayoutParams Params = new RelativeLayout.LayoutParams(screenWidth, currentValue);
                Params.setMargins(0, ScreenUtils.dp2px(context, 44), 0, 0);
                homeCustom_cover_top_view.setLayoutParams(Params);

                RelativeLayout.LayoutParams bottomParams = new RelativeLayout.LayoutParams(screenWidth, currentValue);
                bottomParams.setMargins(0, screenHeight - menuPopviewHeight - currentValue, 0, 0);
                homeCustom_cover_bottom_view.setLayoutParams(bottomParams);
            }

        });
        anim.start();

        homeCustom_cover_top_view.bringToFront();
        home_custom_top_relative.bringToFront();
        homeCustom_cover_bottom_view.bringToFront();
        index++;
    }

    /**
     * 长方形方形拍摄
     */
    public void camera_square_1() {
        camera_square.setImageResource(R.drawable.btn_camera_size2_n);

        ValueAnimator anim = ValueAnimator.ofInt(animHeight, 0);
        anim.setDuration(300);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int currentValue = Integer.parseInt(animation.getAnimatedValue().toString());
                RelativeLayout.LayoutParams Params = new RelativeLayout.LayoutParams(screenWidth, currentValue);
                Params.setMargins(0, ScreenUtils.dp2px(context, 44), 0, 0);
                homeCustom_cover_top_view.setLayoutParams(Params);

                RelativeLayout.LayoutParams bottomParams = new RelativeLayout.LayoutParams(screenWidth, currentValue);
                bottomParams.setMargins(0, screenHeight - menuPopviewHeight - currentValue, 0, 0);
                homeCustom_cover_bottom_view.setLayoutParams(bottomParams);
            }
        });
        anim.start();
        index = 0;
    }


    @Override
    public void onResume() {
        super.onResume();
        if (mCamera == null) {
            mCamera = getCamera(mCameraId);
            if (mHolder != null) {
                startPreview(mCamera, mHolder);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        releaseCamera();
    }

    /**
     * 获取Camera实例
     *
     * @return
     */
    private Camera getCamera(int id) {
        Camera camera = null;
        try {
            camera = Camera.open(id);
        } catch (Exception e) {

        }
        return camera;
    }

    /**
     * 预览相机
     */
    private void startPreview(Camera camera, SurfaceHolder holder) {
        try {
            boolean fini = setupCamera(camera);
            if (fini) {
                finish();
                return;
            }
            camera.setPreviewDisplay(holder);
            //亲测的一个方法 基本覆盖所有手机 将预览矫正
            CameraUtil.getInstance().setCameraDisplayOrientation(this, mCameraId, camera);
//            camera.setDisplayOrientation(90);
            camera.startPreview();
            isview = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void captrue() {
        mCamera.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                isview = false;
                //将data 转换为位图 或者你也可以直接保存为文件使用 FileOutputStream
                //这里我相信大部分都有其他用处把 比如加个水印 后续再讲解
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                Bitmap saveBitmap = CameraUtil.getInstance().setTakePicktrueOrientation(mCameraId, bitmap);

                saveBitmap = Bitmap.createScaledBitmap(saveBitmap, screenWidth, picHeight, true);

                if (index == 1) {
                    // 正方形 animHeight(动画高度)
                    saveBitmap = Bitmap.createBitmap(saveBitmap, 0, animHeight + ScreenUtils.dp2px(context, 44), screenWidth, screenWidth);
                } else {
                    boolean isIdCard = false;

                    // 长方形（身份证）
                    if (type != null && type.equalsIgnoreCase(TYPE_IDCARD)) {
                        isIdCard = true;
                    }

                    if (isIdCard) {
                        // 身份证
                        saveBitmap = Bitmap.createBitmap(saveBitmap, ivTop.getLeft(), ivTop.getTop(),
                                ivTop.getRight() - ivTop.getLeft(), ivTop.getBottom() - ivTop.getTop());

                        saveBitmap = ImageUtil.reviewPicRotate(saveBitmap, 270);
                    } else {
                        // 长方形 animHeight(动画高度)
                        saveBitmap = Bitmap.createBitmap(saveBitmap, 0, 0, screenWidth, screenWidth * 4 / 3);
                    }
                }

                String img_path = getExternalFilesDir(Environment.DIRECTORY_DCIM).getPath() +
                        File.separator + System.currentTimeMillis() + ".jpeg";

                ImageUtil.saveImage2FileByBitmap(context, img_path, saveBitmap, 88);

                if (!bitmap.isRecycled()) {
                    bitmap.recycle();
                }

                if (!saveBitmap.isRecycled()) {
                    saveBitmap.recycle();
                }

                Intent intent = new Intent();
                intent.putExtra(CameraUtil.KEY.IMG_PATH, img_path);
                intent.putExtra(CameraUtil.KEY.PIC_WIDTH, screenWidth);
                intent.putExtra(CameraUtil.KEY.PIC_HEIGHT, picHeight);
                setResult(CameraUtil.RESULT_CODE.RESULT_OK, intent);
                finish();

                //这里打印宽高 就能看到 CameraUtil.getInstance().getPropPictureSize(parameters.getSupportedPictureSizes(), 200);
                // 这设置的最小宽度影响返回图片的大小 所以这里一般这是1000左右把我觉得
//                Log.d("bitmapWidth==", bitmap.getWidth() + "");
//                Log.d("bitmapHeight==", bitmap.getHeight() + "");
            }
        });
    }

    /**
     * 设置
     */
    private boolean setupCamera(Camera camera) {
        if (camera == null) {
            return true;
        }
        try {
            Camera.Parameters parameters = camera.getParameters();
            if (parameters == null) {
                return true;
            }

            if (parameters.getSupportedFocusModes().contains(
                    Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            }

            // 这里第三个参数为最小尺寸 getPropPreviewSize方法会对从最小尺寸开始升序排列 取出所有支持尺寸的最小尺寸
            Camera.Size previewSize = CameraUtil.getInstance().getPropSizeForWidth(parameters.getSupportedPreviewSizes(), ScreenUtils.getScreenWidth(context));
            parameters.setPreviewSize(previewSize.width, previewSize.height);

            Camera.Size pictrueSize = CameraUtil.getInstance().getPropSizeForHeight(parameters.getSupportedPictureSizes(), 800);
            parameters.setPictureSize(pictrueSize.width, pictrueSize.height);

            camera.setParameters(parameters);

            /**
             * 设置surfaceView的尺寸 因为camera默认是横屏，所以取得支持尺寸也都是横屏的尺寸
             * 我们在startPreview方法里面把它矫正了过来，但是这里我们设置设置surfaceView的尺寸的时候要注意 previewSize.height<previewSize.width
             * previewSize.width才是surfaceView的高度
             * 一般相机都是屏幕的宽度 这里设置为屏幕宽度 高度自适应 你也可以设置自己想要的大小
             */

            picHeight = (screenWidth * pictrueSize.width) / pictrueSize.height;
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(screenWidth, (screenWidth * pictrueSize.width) / pictrueSize.height);
            //这里当然可以设置拍照位置 比如居中 我这里就置顶了
//        params.gravity = Gravity.CENTER;
            rl.setLayoutParams(params);
            surfaceView.setLayoutParams(params);

            if (type != null && type.equalsIgnoreCase(TYPE_IDCARD)) {
                if (CMemoryData.isDriver()) {
                    ivTop.setImageResource(R.drawable.camera_image_people_blue_nor);
                } else {
                    ivTop.setImageResource(R.drawable.camera_image_people_green_nor);
                }
            }

            return false;
        } catch (Exception e) {
            Logger.d("oye","setupCamera:"+e.toString());
            return true;
        }
    }

    /**
     * 释放相机资源
     */
    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
//        startPreview(mCamera, holder);
    }

    @Override
    public void surfaceChanged(final SurfaceHolder holder, int format, int width, int height) {
//        mCamera.stopPreview();
        startPreview(mCamera, holder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        releaseCamera();
    }

}