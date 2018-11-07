package com.topjet.common.utils;

/**
 * Created by yue on 2015/8/13.
 */

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Surface;

import com.foamtrace.photopicker.ImageModel;
import com.foamtrace.photopicker.SelectModel;
import com.foamtrace.photopicker.intent.PhotoPickerIntent;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.topjet.common.R;
import com.topjet.common.common.view.activity.CameraActivity;
import com.topjet.common.config.CMemoryData;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

import static com.topjet.common.utils.CameraUtil.REQUEST_CODE.PHOTO_PICKER;

public class CameraUtil {
    //WHAT 0-10 预留值
    public interface WHAT {
        int SUCCESS = 0;
        int FAILURE = 1;
        int ERROR = 2;
    }

    public interface KEY {
        String IMG_PATH = "IMG_PATH";
        String VIDEO_PATH = "VIDEO_PATH";
        String PIC_WIDTH = "PIC_WIDTH";
        String PIC_HEIGHT = "PIC_HEIGHT";
    }

    public interface REQUEST_CODE {
        int SYSTEM_CAMERA = 100;
        int MY_CAMERA = 125;
        int CROP = 200;
        int SQUARE_CROP = 250;
        int RECTANGLE_CROP = 255;
        int PHOTO_PICKER = 300;
    }

    public interface RESULT_CODE {
        int RESULT_OK = -1;
        int RESULT_CANCELED = 0;
        int RESULT_ERROR = 1;
    }

    //降序
    private CameraDropSizeComparator dropSizeComparator = new CameraDropSizeComparator();
    //升序
    private CameraAscendSizeComparator ascendSizeComparator = new CameraAscendSizeComparator();
    private static CameraUtil myCamPara = null;

    private CameraUtil() {

    }

    public static CameraUtil getInstance() {
        if (myCamPara == null) {
            myCamPara = new CameraUtil();
            return myCamPara;
        } else {
            return myCamPara;
        }
    }

    public void camera(Activity activity) {
        Intent intent = new Intent(activity, CameraActivity.class);
        activity.startActivityForResult(intent, REQUEST_CODE.MY_CAMERA);
    }

    public int getRecorderRotation(int cameraId) {
        Camera.CameraInfo info =
                new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        return info.orientation;
    }

    /**
     * 获取所有支持的返回视频尺寸
     *
     * @param list
     * @param minHeight
     * @return
     */
    public Size getPropVideoSize(List<Size> list, int minHeight) {
        Collections.sort(list, ascendSizeComparator);

        int i = 0;
        for (Size s : list) {
            if ((s.height >= minHeight)) {
                break;
            }
            i++;
        }
        if (i == list.size()) {
            i = 0;//如果没找到，就选最小的size
        }
        return list.get(i);
    }

    /**
     * 保证预览方向正确
     *
     * @param activity
     * @param cameraId
     * @param camera
     */
    public void setCameraDisplayOrientation(Activity activity,
                                            int cameraId, Camera camera) {
        Camera.CameraInfo info =
                new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }


    public Bitmap setTakePicktrueOrientation(int id, Bitmap bitmap) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(id, info);
        bitmap = rotaingImageView(id, info.orientation, bitmap);
        return bitmap;
    }

    /**
     * 把相机拍照返回照片转正
     *
     * @param angle 旋转角度
     * @return bitmap 图片
     */
    public Bitmap rotaingImageView(int id, int angle, Bitmap bitmap) {
        //旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        //加入翻转 把相机拍照返回照片转正
        if (id == 1) {
            matrix.postScale(-1, 1);
        }
        // 创建新的图片
        return Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    /**
     * 获取所有支持的预览尺寸
     *
     * @param list
     * @param minWidth
     * @return
     */
    public Size getPropPreviewSize(List<Size> list, int minWidth) {
        Collections.sort(list, ascendSizeComparator);

        int i = 0;
        for (Size s : list) {
            if ((s.width >= minWidth)) {
                break;
            }
            i++;
        }
        if (i == list.size()) {
            i = 0;//如果没找到，就选最小的size
        }
        return list.get(i);
    }

    /**
     * 获取所有支持的返回图片尺寸
     *
     * @param list
     * @param minWidth
     * @return
     */
    public Size getPropPictureSize(List<Size> list, int minWidth) {
        Collections.sort(list, ascendSizeComparator);

        int i = 0;
        for (Size s : list) {
            if ((s.width >= minWidth)) {
                break;
            }
            i++;
        }
        if (i == list.size()) {
            i = 0;//如果没找到，就选最小的size
        }
        return list.get(i);
    }

    /**
     * 获取所有支持的返回视频尺寸
     *
     * @param list
     * @param minHeight
     * @return
     */
    public Size getPropSizeForHeight(List<Size> list, int minHeight) {
        Collections.sort(list, new CameraAscendSizeComparatorForHeight());

        int i = 0;
        for (Size s : list) {
            if ((s.height >= minHeight)) {
                break;
            }
            i++;
        }
        if (i == list.size()) {
            i = 0;//如果没找到，就选最小的size
        }
        return list.get(i);
    }

    /**
     * 获取所有支持的返回视频尺寸
     *
     * @param list
     * @param minWidth
     * @return
     */
    public Size getPropSizeForWidth(List<Size> list, int minWidth) {
        Collections.sort(list, new CameraAscendSizeComparatorForHeight());

        int i = 0;
        for (Size s : list) {
            if ((s.width >= minWidth)) {
                break;
            }
            i++;
        }
        if (i == list.size()) {
            i = 0;//如果没找到，就选最小的size
        }
        return list.get(i);
    }

    //升序 按照高度
    public class CameraAscendSizeComparatorForHeight implements Comparator<Size> {
        public int compare(Size lhs, Size rhs) {
            if (lhs.height == rhs.height) {
                return 0;
            } else if (lhs.height > rhs.height) {
                return 1;
            } else {
                return -1;
            }
        }
    }

    public boolean equalRate(Size s, float rate) {
        float r = (float) (s.width) / (float) (s.height);
        return Math.abs(r - rate) <= 0.03;
    }

    //降序
    public class CameraDropSizeComparator implements Comparator<Size> {
        public int compare(Size lhs, Size rhs) {
            if (lhs.width == rhs.width) {
                return 0;
            } else if (lhs.width < rhs.width) {
                return 1;
            } else {
                return -1;
            }
        }

    }

    //升序
    public class CameraAscendSizeComparator implements Comparator<Size> {
        public int compare(Size lhs, Size rhs) {
            if (lhs.width == rhs.width) {
                return 0;
            } else if (lhs.width > rhs.width) {
                return 1;
            } else {
                return -1;
            }
        }

    }

    /**
     * 打印支持的previewSizes
     *
     * @param params
     */
    public void printSupportPreviewSize(Camera.Parameters params) {
        List<Size> previewSizes = params.getSupportedPreviewSizes();
        for (int i = 0; i < previewSizes.size(); i++) {
            Size size = previewSizes.get(i);
        }

    }

    /**
     * 打印支持的pictureSizes
     *
     * @param params
     */
    public void printSupportPictureSize(Camera.Parameters params) {
        List<Size> pictureSizes = params.getSupportedPictureSizes();
        for (int i = 0; i < pictureSizes.size(); i++) {
            Size size = pictureSizes.get(i);
        }
    }

    /**
     * 打印支持的聚焦模式
     *
     * @param params
     */
    public void printSupportFocusMode(Camera.Parameters params) {
        List<String> focusModes = params.getSupportedFocusModes();
        for (String mode : focusModes) {
        }
    }

    /**
     * 打开闪关灯
     *
     * @param mCamera
     */
    public void turnLightOn(Camera mCamera) {
        if (mCamera == null) {
            return;
        }
        Camera.Parameters parameters = mCamera.getParameters();
        if (parameters == null) {
            return;
        }
        List<String> flashModes = parameters.getSupportedFlashModes();
        // Check if camera flash exists
        if (flashModes == null) {
            // Use the screen as a flashlight (next best thing)
            return;
        }
        String flashMode = parameters.getFlashMode();
        if (!Camera.Parameters.FLASH_MODE_ON.equals(flashMode)) {
            // Turn on the flash
            if (flashModes.contains(Camera.Parameters.FLASH_MODE_TORCH)) {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                mCamera.setParameters(parameters);
            } else {
            }
        }
    }


    /**
     * 自动模式闪光灯
     *
     * @param mCamera
     */
    public void turnLightAuto(Camera mCamera) {
        if (mCamera == null) {
            return;
        }
        Camera.Parameters parameters = mCamera.getParameters();
        if (parameters == null) {
            return;
        }
        List<String> flashModes = parameters.getSupportedFlashModes();
        // Check if camera flash exists
        if (flashModes == null) {
            // Use the screen as a flashlight (next best thing)
            return;
        }
        String flashMode = parameters.getFlashMode();
        if (!Camera.Parameters.FLASH_MODE_AUTO.equals(flashMode)) {
            // Turn on the flash
            if (flashModes.contains(Camera.Parameters.FLASH_MODE_TORCH)) {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                mCamera.setParameters(parameters);
            } else {
            }
        }
    }


    /**
     * 关闭闪光灯
     *
     * @param mCamera
     */
    public void turnLightOff(Camera mCamera) {
        if (mCamera == null) {
            return;
        }
        Camera.Parameters parameters = mCamera.getParameters();
        if (parameters == null) {
            return;
        }
        List<String> flashModes = parameters.getSupportedFlashModes();
        String flashMode = parameters.getFlashMode();
        // Check if camera flash exists
        if (flashModes == null) {
            return;
        }
        if (!Camera.Parameters.FLASH_MODE_OFF.equals(flashMode)) {
            // Turn off the flash
            if (flashModes.contains(Camera.Parameters.FLASH_MODE_TORCH)) {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                mCamera.setParameters(parameters);
            } else {
            }
        }
    }

    public static final String PHOTO_ROOT_PATH = PathHelper.camera() + "/pic_";
    public static String currentPicPath;

    /**
     * 跳转至系统相机进行拍摄
     */
    public static void jumpToCameraGetPic(Activity activity) {
        permissionCheck(activity, 0);   // 0 后置摄像头
    }

    /**
     * 跳转至前置相机进行拍摄
     */
    public static void jumpToFrontCameraGetPic(Activity activity) {
        Intent intent = new Intent(activity, CameraActivity.class);
        intent.putExtra(CameraActivity.TYPE, CameraActivity.TYPE_AVATAR);
        activity.startActivityForResult(intent, CameraUtil.REQUEST_CODE.MY_CAMERA);
    }

    /**
     * 跳转至相机拍摄身份证照片
     */
    public static void jumpToCameraGetIdCardPic(Activity activity) {
        Intent intent = new Intent(activity, CameraActivity.class);
        intent.putExtra(CameraActivity.TYPE, CameraActivity.TYPE_IDCARD);
        activity.startActivityForResult(intent, CameraUtil.REQUEST_CODE.MY_CAMERA);
    }

    /**
     * 去系统拍照
     */
    private static void getPhotoFromSystem(Activity activity, int type) {
        File dir = new File(PathHelper.camera());
        if (!dir.exists())
            dir.mkdirs();

        createCurrentPicPath();
        Uri fileUri = Uri.fromFile(new File(currentPicPath));
        Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        it.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        if (type == 1) { // 前置相机
            it.putExtra("android.intent.extras.CAMERA_FACING", Camera.CameraInfo.CAMERA_FACING_FRONT);
            it.putExtra("camerasensortype", Camera.CameraInfo.CAMERA_FACING_FRONT); // 调用前置摄像头
            it.putExtra("autofocus", false);
        }

        activity.startActivityForResult(it, REQUEST_CODE.SYSTEM_CAMERA);
    }

    /**
     * 创建这个文件名
     */
    public static String createCurrentPicPath() {
        currentPicPath = PHOTO_ROOT_PATH + System.currentTimeMillis() + ".jpg";
        File f = new File(PHOTO_ROOT_PATH);
        if (!f.exists()) {
            f.mkdirs();
        }
        f = new File(currentPicPath);
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return currentPicPath;
    }

    /**
     * 拍照权限检查
     */
    private static void permissionCheck(final Activity activity, final int type) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_UNMOUNTED)) {
            Toaster.showLongToast(R.string.no_has_sdcard);
            return;
        }

        RxPermissions rxPermissions = new RxPermissions(activity);
        rxPermissions.setLogging(true);
        rxPermissions
                .requestEach(// 写入需要获取的运行时权限
                        Manifest.permission.CAMERA)
                .subscribe(new Consumer<Permission>() {// 观察者
                               @Override
                               public void accept(Permission permission) {// 请求权限的返回结果
                                   if (permission.name.equals(Manifest.permission.CAMERA)) {
                                       if (permission.granted) {
                                           getPhotoFromSystem(activity, type);
                                       } else {
                                           Toaster.showLongToast(R.string.no_has_permission);
                                       }
                                   }
                               }
                           },
                        new Consumer<Throwable>() {// 获取权限发生异常，必须得写，华为系统当用户拒绝后，第二次请求会直接报error
                            @Override
                            public void accept(Throwable t) {
                                t.printStackTrace();
                            }
                        },
                        new Action() {// 结束
                            @Override
                            public void run() {
                            }
                        }
                );
    }

    /**
     * 打开相册选择页面
     * 需要照相
     * 不是IM页面跳转
     */
    private static void toAlbum(final Activity mActivity, int maxTotal, boolean isShowCamera, boolean isIm,
                                ArrayList<ImageModel> mSelectImagesBeanList) {
        // 打开相册
        final PhotoPickerIntent intent = new PhotoPickerIntent(mActivity);
        intent.setSelectModel(SelectModel.MULTI);
        intent.setShowCarema(isShowCamera); // 是否显示拍照
        intent.setMaxTotal(maxTotal); // 最多选择照片数量，默认为4
        intent.setIsIM(isIm);// 设置是否是聊天跳转过去的

        intent.setSelectedPaths(mSelectImagesBeanList); // 已选中的照片地址， 用于回显选中状态
        if (CMemoryData.isDriver()) {
            intent.setIsDriver(true); // 已选中的照片地址， 用于回显选中状态
        } else {
            intent.setIsDriver(false); // 已选中的照片地址， 用于回显选中状态
        }

        RxPermissions rxPermissions = new RxPermissions(mActivity);
        rxPermissions.setLogging(true);
        rxPermissions
                .requestEach(// 写入需要获取的运行时权限
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Permission>() {// 观察者
                               @Override
                               public void accept(Permission permission) {// 请求权限的返回结果
                                   if (permission.name.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                       if (permission.granted) {
                                           mActivity.startActivityForResult(intent, PHOTO_PICKER);
                                       } else {
                                           Toaster.showLongToast(R.string.no_has_sd_permission);
                                       }
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable t) {
                                // 获取权限发生异常，必须得写，华为系统当用户拒绝后，第二次请求会直接报error
                            }
                        },
                        new Action() {
                            @Override
                            public void run() {
                                // 结束
                            }
                        }
                );
    }

    /**
     * 普通跳转相册选择
     */
    public static void defaultTurnToAlbum(final Activity mActivity, final int maxTotal, final ArrayList<ImageModel>
            mSelectImagesBeanList) {
        //  选择相册的时候可能需要拍照，所以拍照权限也需要
        RxPermissions rxPermissions = new RxPermissions(mActivity);
        rxPermissions.setLogging(true);
        rxPermissions
                .requestEach(// 写入需要获取的运行时权限
                        Manifest.permission.CAMERA)
                .subscribe(new Consumer<Permission>() {// 观察者
                               @Override
                               public void accept(Permission permission) {// 请求权限的返回结果
                                   if (permission.name.equals(Manifest.permission.CAMERA)) {
                                       if (permission.granted) {
                                           toAlbum(mActivity, maxTotal, true, false, mSelectImagesBeanList);
                                       } else {
                                           Toaster.showLongToast(R.string.no_has_permission);
                                       }
                                   }
                               }
                           },
                        new Consumer<Throwable>() { // 获取权限发生异常，必须得写，华为系统当用户拒绝后，第二次请求会直接报error
                            @Override
                            public void accept(Throwable t) {
                                t.printStackTrace();
                            }
                        },
                        new Action() {// 结束
                            @Override
                            public void run() {
                            }
                        }
                );

    }
}
