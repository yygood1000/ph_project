package org.yanzi.activity;

import android.app.Activity;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.yanzi.camera.CameraManager;
import org.yanzi.camera.CameraManager.CamOpenOverCallback;
import org.yanzi.camera.DebounceClickListener;
import org.yanzi.camera.preview.CameraSurfaceView;
import org.yanzi.playcamera.R;
import org.yanzi.ui.MaskView;
import org.yanzi.util.DisplayUtil;

public class CameraActivity extends Activity implements CamOpenOverCallback {
    /**
     * 拍摄默认图片，该种情况配合截取正方形图片使用
     */
    public static int TAKE_DEFAULT_PHOTO = 1;
    /**
     * 拍摄头像
     */
    public static int TAKE_HEAD_PHOTO = 2;
    /**
     * 拍摄身份证正面
     */
    public static int TAKE_ID_CARD_PHOTO = 3;
    /**
     * 拍摄身份证背面
     */
    public static int TAKE_ID_CARD_BACK_PHOTO = 4;

    CameraActivity mActivity;
    CameraManager cManager;
    CameraSurfaceView surfaceView;
    MaskView maskView = null;
    ImageView ivRemark;
    float previewRate = -1f;
    int DST_CENTER_RECT_WIDTH; // 屏幕的总宽单位是dip
    int DST_CENTER_RECT_HEIGHT;// 屏幕的总长单位是dip
    Point rectPictureSize = null;

    private ImageButton btnChange;
    private ImageButton shutterBtn;
    private ImageButton btnFlashlight;
    private int cameraPosition;// 0代表前置摄像头，1代表后置摄像头
    private boolean isFlashlight;
    private boolean hasCamera = true;
    int camera_id = 0;

    private int facing;// 前 1 后0
    private int isClipImage;//是否截取框中内容成像 0否 1是  默认1
    private int type = 1;// 拍摄什么类型图图片。类型有1234，在上面列举。默认是1


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        mActivity = this;

        facing = getIntent().getIntExtra("facing", 1);
        isClipImage = getIntent().getIntExtra("isClipImage", 1);
        type = getIntent().getIntExtra("type", 1);


        DST_CENTER_RECT_WIDTH = DisplayUtil.px2dip(mActivity, DisplayUtil.getScreenWidthMetrics(mActivity));
        DST_CENTER_RECT_HEIGHT = DisplayUtil.px2dip(mActivity, DisplayUtil.getScreenWidthMetrics(mActivity));

    }

    @Override
    protected void onResume() {
        super.onResume();
        cManager = CameraManager.getInstance();
        cManager.setActivity(this);
        if (facing == 1) {
            // 判断是否有前置摄像头
            if (hasFrontFacingCamera()) {
                cameraPosition = CameraInfo.CAMERA_FACING_FRONT;
                isFlashlight = false;
            } else {
                hasCamera = false;
            }
        } else {
            // 判断是否有后置摄像头
            if (hasBackFacingCamera()) {
                cameraPosition = CameraInfo.CAMERA_FACING_BACK;
                isFlashlight = false;
            } else {
                hasCamera = false;
            }
        }

        initUI();
        initViewParams();
        openCamera(cameraPosition);
    }

    private void initUI() {
        TextView tvClose = (TextView) findViewById(R.id.tv_close);
        surfaceView = (CameraSurfaceView) findViewById(R.id.camera_surfaceview);
        shutterBtn = (ImageButton) findViewById(R.id.btn_shutter);
        ivRemark = (ImageView) findViewById(R.id.iv_remark);
        maskView = (MaskView) findViewById(R.id.view_mask);
        btnChange = (ImageButton) findViewById(R.id.btn_change);
        btnFlashlight = (ImageButton) findViewById(R.id.btn_flashlight);
        if (!hasCamera) {
            btnChange.setVisibility(View.GONE);
        }

        // 根据传入值设置覆盖物
        switch (type) {
            case 1:
                ivRemark.setVisibility(View.GONE);
                maskView.setVisibility(View.VISIBLE);
                break;
            case 2:
                ivRemark.setVisibility(View.VISIBLE);
                ivRemark.setBackgroundResource(R.drawable.iv_marker_head);
                maskView.setVisibility(View.GONE);
                break;
            case 3:
                ivRemark.setVisibility(View.VISIBLE);
                ivRemark.setBackgroundResource(R.drawable.iv_marker_id_card);
                maskView.setVisibility(View.GONE);
                break;
            case 4:
                ivRemark.setVisibility(View.VISIBLE);
                ivRemark.setBackgroundResource(R.drawable.iv_marker_id_card_back);
                maskView.setVisibility(View.GONE);
                break;
            default:
                break;
        }

        shutterBtn.setOnClickListener(mClickListener);
        btnChange.setOnClickListener(mClickListener);
        btnFlashlight.setOnClickListener(mClickListener);
        tvClose.setOnClickListener(mClickListener);
    }

    private void initViewParams() {
        LayoutParams params = surfaceView.getLayoutParams();
        Point p = DisplayUtil.getScreenMetrics(this);
        params.width = p.x;
        params.height = p.y;
        surfaceView.setLayoutParams(params);

        previewRate = DisplayUtil.getScreenRate(this); // 默认全屏的比例预览

        // 手动设置拍照ImageButton的大小为120dip×120dip,原图片大小是64×64
        LayoutParams p2 = shutterBtn.getLayoutParams();
        p2.width = DisplayUtil.dip2px(this, 70);
        p2.height = DisplayUtil.dip2px(this, 70);
        shutterBtn.setLayoutParams(p2);

    }

    public void openCamera(int cameraId) {
        // 在子线程中打开照相机
        Thread openThread = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                cManager.doOpenCamera(CameraActivity.this, cameraPosition, isFlashlight);
            }
        };
        openThread.start();
    }

    @Override
    public void cameraHasOpened(boolean isOpenSucceed) {
        if (!isOpenSucceed) {
            Looper.prepare();
            Toast.makeText(mActivity, "摄像头权限未开启!", Toast.LENGTH_LONG).show();
            finish();
            Looper.loop();
            return;
        }
        // 获取SurfaceView 管理类
        SurfaceHolder holder = surfaceView.getSurfaceHolder();

        // 开始预览照相机所获取的信息
        cManager.doStartPreview(holder, previewRate);


        /**
         * 绘制预览界面中间矩形框
         */
        if (maskView != null) {
            Rect screenCenterRect = createCenterScreenRect(DisplayUtil.getScreenWidthMetrics(mActivity),
                    DisplayUtil.getScreenWidthMetrics(mActivity));
            maskView.setCenterRect(screenCenterRect);
        }
    }

    DebounceClickListener mClickListener = new DebounceClickListener() {

        @Override
        public void performClick(View v) {
            if (v.getId() == R.id.btn_shutter) {
                if (rectPictureSize == null) {
                    rectPictureSize = createCenterPictureRect(DisplayUtil.getScreenWidthMetrics(mActivity),
                            DisplayUtil.getScreenWidthMetrics(mActivity));
                }

                if (isClipImage == 0) {
                    cManager.doTakePicture();// 正常拍照
                } else {
                    cManager.doTakePicture(rectPictureSize.x, rectPictureSize.y);// 裁剪框中的图片
                }

            } else if (v.getId() == R.id.btn_change) {
                // 如果当前是后置，设置为前置，否则为后置
                if (cameraPosition == CameraInfo.CAMERA_FACING_BACK) {
                    cameraPosition = CameraInfo.CAMERA_FACING_FRONT;
                    isFlashlight = false;
                    btnFlashlight.setVisibility(View.GONE);
                } else {
                    cameraPosition = CameraInfo.CAMERA_FACING_BACK;
                    btnFlashlight.setVisibility(View.VISIBLE);
                }
                int mNumberOfCameras = Camera.getNumberOfCameras();
                CameraInfo cameraInfo = new CameraInfo();
                for (int i = 0; i < mNumberOfCameras; i++) {
                    Camera.getCameraInfo(i, cameraInfo);
                    if (cameraInfo.facing == cameraPosition) {
                        camera_id = i;
                    }
                }
                openCamera(camera_id);
            } else if (v.getId() == R.id.btn_flashlight) {
                isFlashlight = !isFlashlight;
                if (isFlashlight) {
                    btnFlashlight.setBackgroundResource(R.drawable.flashlight_open);
                } else {
                    btnFlashlight.setBackgroundResource(R.drawable.flashlight_close);
                }
                openCamera(camera_id);
            } else {
                cManager.doStopCamera();
                finish();
            }
        }
    };

    /**
     * 生成拍照后图片的中间矩形的宽度和高度
     *
     * @param w 屏幕上的矩形宽度，单位px
     * @param h 屏幕上的矩形高度，单位px
     */
    private Point createCenterPictureRect(int w, int h) {

        int wScreen = DisplayUtil.getScreenMetrics(this).x;
        int hScreen = DisplayUtil.getScreenMetrics(this).y;
        int wSavePicture = cManager.doGetPrictureSize().y; // 因为图片旋转了，所以此处宽高换位
        int hSavePicture = cManager.doGetPrictureSize().x; // 因为图片旋转了，所以此处宽高换位
        float wRate = (float) (wSavePicture) / (float) (wScreen);
        float hRate = (float) (hSavePicture) / (float) (hScreen);
        float rate = (wRate <= hRate) ? wRate : hRate;// 默认全屏的比例预览

        int wRectPicture = (int) (w * wRate);
        int hRectPicture = (int) (h * hRate);
        return new Point(wRectPicture, hRectPicture);

    }

    /**
     * 生成屏幕中间的矩形尺寸对象
     *
     * @param w 目标矩形的宽度,单位px
     * @param h 目标矩形的高度,单位px
     */
    private Rect createCenterScreenRect(int w, int h) {
        int x1 = DisplayUtil.getScreenMetrics(this).x / 2 - w / 2;
        int y1 = DisplayUtil.getScreenMetrics(this).y / 2 - h / 2;
        int x2 = x1 + w;
        int y2 = y1 + h;
        return new Rect(x1, y1, x2, y2);
    }

    // 后置
    public static boolean hasBackFacingCamera() {
        final int CAMERA_FACING_BACK = 0;
        return checkCameraFacing(CAMERA_FACING_BACK);
    }

    // 前置
    public static boolean hasFrontFacingCamera() {
        final int CAMERA_FACING_BACK = 1;
        return checkCameraFacing(CAMERA_FACING_BACK);
    }

    /**
     * 检查手机是否有前置摄像头(该方法只能在版本大于9的安卓系统时有效)
     */
    @SuppressWarnings("deprecation")
    private static boolean checkCameraFacing(final int facing) {
        if (getSdkVersion() < Build.VERSION_CODES.GINGERBREAD) {
            return false;
        }
        final int cameraCount = Camera.getNumberOfCameras();
        CameraInfo info = new CameraInfo();
        for (int i = 0; i < cameraCount; i++) {
            Camera.getCameraInfo(i, info);
            if (facing == info.facing) {
                return true;
            }
        }
        return false;
    }

    // 判断sdk版本是否大于等于9.
    public static int getSdkVersion() {
        return android.os.Build.VERSION.SDK_INT;
    }

    @Override
    protected void onStop() {
        super.onStop();
        cManager.doStopCamera();
    }

}
