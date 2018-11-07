package org.yanzi.camera;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Camera.Size;
import android.os.AsyncTask;
import android.util.Log;
import android.view.SurfaceHolder;
import android.widget.Toast;

import org.yanzi.activity.CameraActivity;
import org.yanzi.util.CamParaUtil;
import org.yanzi.util.FileUtil;
import org.yanzi.util.ImageUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.List;
import java.util.logging.Logger;

@SuppressWarnings("deprecation")
public class CameraManager {
	private Camera mCamera;
	private Camera.Parameters mParams;
	private boolean isPreviewing = false;
	private float mPreviwRate = -1f;
	private boolean isFlashlight;
	private static CameraManager mCameraManager;
	private CameraActivity mActivity;
	private boolean isBack;
	private byte[] result;
	private boolean isOpenSucceed;
	int DST_RECT_WIDTH, DST_RECT_HEIGHT;// 生成图片的长宽，单位px。

	public interface CamOpenOverCallback {
		void cameraHasOpened(boolean isOpenSucceed);
	}

	public CameraManager() {
	}

	public static synchronized CameraManager getInstance() {
		if (mCameraManager == null) {
			mCameraManager = new CameraManager();
		}
		return mCameraManager;
	}

	public void setActivity(CameraActivity activity) {
		this.mActivity = activity;
	}

	/**
	 * 打开Camera
	 *
	 * @param callback 照相机开启完成回调
	 * @param cameraPosition 开启前置/后置相机
	 * @param isFlashlight 是否带有闪光灯功能
	 */
	public void doOpenCamera(CamOpenOverCallback callback, int cameraPosition, boolean isFlashlight) {
		this.isFlashlight = isFlashlight;
		if (mCamera != null) {
			mCamera.setPreviewCallback(null);
			mCamera.stopPreview();// 停掉原来摄像头的预览
			mCamera.release();
			mPreviwRate = -1f;
			mCamera = null;
			isPreviewing = false;
		}
		try {
			mCamera = Camera.open(cameraPosition);
			isOpenSucceed = true;
		} catch (Exception e) {
			isOpenSucceed = false;
		}
		isBack = cameraPosition == 0;
		callback.cameraHasOpened(isOpenSucceed);
	}

	/**
	 * 使用Surfaceview开启预览
	 *
	 * @param holder
	 * @param previewRate
	 */
	public void doStartPreview(SurfaceHolder holder, float previewRate) {
		// Log.i("oye", "doStartPreview...");
		if (isPreviewing) {
			mCamera.stopPreview();
			return;
		}
		if (mCamera != null) {
			try {
				mCamera.setPreviewDisplay(holder);
			} catch (IOException e) {
				e.printStackTrace();
			}
			initCamera(previewRate);
		}
	}

	/**
	 * 使用TextureView预览Camera
	 *
	 * @param surface
	 * @param previewRate
	 */
	public void doStartPreview(SurfaceTexture surface, float previewRate) {
		// Log.i("oye", "doStartPreview...");
		if (isPreviewing) {
			mCamera.stopPreview();
			return;
		}
		if (mCamera != null) {
			try {
				mCamera.setPreviewTexture(surface);
			} catch (IOException e) {
				e.printStackTrace();
			}
			initCamera(previewRate);
		}

	}

	/**
	 * 停止预览，释放Camera
	 */
	public void doStopCamera() {
		if (null != mCamera) {
			mCamera.setPreviewCallback(null);
			mCamera.stopPreview();
			isPreviewing = false;
			mPreviwRate = -1f;
			mCamera.release();
			mCamera = null;
		}
	}

	/**
	 * 拍照
	 */
	public void doTakePicture() {
		if (isPreviewing && (mCamera != null)) {
			mCamera.takePicture(mShutterCallback, null, mJpegPictureCallback);
		}
	}

	/**
	 * 拍照(方形图片)
	 */
	public void doTakePicture(int w, int h) {
		if (isPreviewing && (mCamera != null)) {
			// Log.i("oye", "矩形拍照尺寸:width = " + w + " h = " + h);
			DST_RECT_WIDTH = w;
			DST_RECT_HEIGHT = h;
			mCamera.takePicture(mShutterCallback, null, mRectJpegPictureCallback);
		}
	}

	public Point doGetPrictureSize() {
		Size s = mCamera.getParameters().getPictureSize();
		return new Point(s.width, s.height);
	}

	private void initCamera(float previewRate) {
		if (mCamera != null) {

			mParams = mCamera.getParameters();
			mParams.setPictureFormat(PixelFormat.JPEG);// 设置拍照后存储的图片格式
			// CamParaUtil.getInstance().printSupportPictureSize(mParams);
			// CamParaUtil.getInstance().printSupportPreviewSize(mParams);
			// 设置PreviewSize和PictureSize
			Size pictureSize = CamParaUtil.getInstance().getPropPictureSize(mParams.getSupportedPictureSizes(),
					previewRate, 0);
			mParams.setPictureSize(pictureSize.width, pictureSize.height);
			Size previewSize = CamParaUtil.getInstance().getPropPreviewSize(mParams.getSupportedPreviewSizes(),
					previewRate, 0);
			mParams.setPreviewSize(previewSize.width, previewSize.height);

			mCamera.setDisplayOrientation(90);

			// 是否开启闪光灯
			if (isFlashlight) {
				mParams.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
			} else {
				mParams.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
			}

			// CamParaUtil.getInstance().printSupportFocusMode(mParams);
			List<String> focusModes = mParams.getSupportedFocusModes();
			if (focusModes.contains("continuous-video")) {
				// 自动对焦
				mParams.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
			}

			mCamera.setParameters(mParams);
			mCamera.startPreview();// 开启预览

			isPreviewing = true;
			mPreviwRate = previewRate;

			mParams = mCamera.getParameters(); // 重新get一次
			// Log.i(TAG, "最终设置:PreviewSize--With = " +
			// mParams.getPreviewSize().width + "Height = "
			// + mParams.getPreviewSize().height);
			// Log.i(TAG, "最终设置:PictureSize--With = " +
			// mParams.getPictureSize().width + "Height = "
			// + mParams.getPictureSize().height);
		}
	}

	public void setFlashlight(boolean isOpenFlashlight) {
		mParams = mCamera.getParameters();
		if (isFlashlight) {
			mParams.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
		} else {
			mParams.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
		}
		mCamera.setParameters(mParams);
	}

	/**
	 * 为了实现拍照的快门声音及拍照保存照片需要下面三个回调变量
	 */

	/*
	 * 快门按下的回调，在这里我们可以设置类似播放“咔嚓”声之类的操作。默认的就是咔嚓。
	 */
	ShutterCallback mShutterCallback = new ShutterCallback() {
		public void onShutter() {
			// Log.i("oye", "myShutterCallback:onShutter...");
		}
	};

	/*
	 * 拍摄的未压缩原数据的回调,可以为null
	 */
	PictureCallback mRawCallback = new PictureCallback() {

		public void onPictureTaken(byte[] data, Camera camera) {
			// Log.i("oye", "myRawCallback:onPictureTaken...");
		}
	};

	/**
	 * 常规拍照
	 */
	// 对jpeg图像数据的回调,最重要的一个回调
	PictureCallback mJpegPictureCallback = new PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {
			// Log.i("oye", "myJpegCallback:onPictureTaken...");
			try {
				Bitmap b = null;
				if (null != data) {
//				b = BitmapFactory.decodeByteArray(data, 0, data.length);// data是字节数据，将其解析成位图
					b = byteToBitmap(data);
					mCamera.stopPreview();
					isPreviewing = false;
				}
				// 保存图片到sdcard
				if (null != b) {
					// 设置FOCUS_MODE_CONTINUOUS_VIDEO)之后，myParam.set("rotation",
					// 90)失效。
					// 图片竟然不能旋转了，故这里要旋转下

					final Bitmap rotaBitmap;
					if (isBack) {
						rotaBitmap = ImageUtil.getRotateBitmap(b, 90.0f);
					} else {
						rotaBitmap = ImageUtil.getRotateBitmap(b, -90.0f);
					}

					new AsyncTask<String, String, String>() {
						@Override
						protected String doInBackground(String... params) {
							return FileUtil.saveBitmap(rotaBitmap);
						}

						@Override
						protected void onPostExecute(String str) {
							doStopCamera();
							Intent intent = new Intent();
							intent.putExtra("resultPath", str);
							mActivity.setResult(Activity.RESULT_OK, intent);
							mActivity.finish();
						}
					}.execute();

				}
//			// 再次进入预览
//			mCamera.startPreview();
//			isPreviewing = true;
			}catch (Exception e){
				Toast.makeText(mActivity,"请优化释放手机内存后重试",Toast.LENGTH_SHORT).show();
				mActivity.finish();
			}
		}
	};

	/**
	 * 拍摄指定区域的Rect
	 */
	// 对jpeg图像数据的回调,最重要的一个回调
	PictureCallback mRectJpegPictureCallback = new PictureCallback() {

		public void onPictureTaken(byte[] data, Camera camera) {
			// Log.i("oye", "myJpegCallback:onPictureTaken...");
			try {
				Bitmap b = null;
				if (null != data) {
//				b = BitmapFactory.decodeByteArray(data, 0, data.length);// data是字节数据，将其解析成位图
					b = byteToBitmap(data);
					mCamera.stopPreview();
					isPreviewing = false;
				}
				// 保存图片到sdcard
				if (null != b) {
					Bitmap rotaBitmap;
					// 设置FOCUS_MODE_CONTINUOUS_VIDEO)之后，myParam.set("rotation",
					// 90)失效。
					// 图片竟然不能旋转了，故这里要旋转下
					if (isBack) {
						rotaBitmap = ImageUtil.getRotateBitmap(b, 90.0f);
					} else {
						rotaBitmap = ImageUtil.getRotateBitmap(b, -90.0f);
					}
					int x = rotaBitmap.getWidth() / 2 - DST_RECT_WIDTH / 2;
					int y = rotaBitmap.getHeight() / 2 - DST_RECT_HEIGHT / 2;
					// ���з���ͼƬ�Ľ�ȡ
					Bitmap rectBitmap = Bitmap.createBitmap(rotaBitmap, x, y, DST_RECT_WIDTH, DST_RECT_HEIGHT);

					// 生成的BitMap压缩成为300*300的图片
					rectBitmap = resizeImage(rectBitmap, 300, 300);

					// Log.i("oye", "rectBitmap.getWidth() = " +
					// rectBitmap.getWidth() + " rectBitmap.getHeight() = "
					// + rectBitmap.getHeight());

					ByteArrayOutputStream output = new ByteArrayOutputStream();// 初始化一个流对象
					rectBitmap.compress(CompressFormat.PNG, 100, output);// 把bitmap100%高质量压缩到output对象里
					result = output.toByteArray();// 转换成功了
					// Log.i("oye", result.toString() + "++++++++Manager");
					if (result != null) {
						doStopCamera();
						Intent intent = new Intent();
						intent.putExtra("result", output.toByteArray());
						mActivity.setResult(Activity.RESULT_OK, intent);
						mActivity.finish();
						// Log.i("oye", "finish");
						try {
							output.close();
							output = null;
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}catch (Exception e){
				Toast.makeText(mActivity,"请优化释放手机内存后重试",Toast.LENGTH_SHORT).show();
				mActivity.finish();
			}
		}
	};

	// 使用Bitmap加Matrix来缩放
	public static Bitmap resizeImage(Bitmap bitmap, int w, int h) {
		Bitmap BitmapOrg = bitmap;
		int width = BitmapOrg.getWidth();
		int height = BitmapOrg.getHeight();
		int newWidth = w;
		int newHeight = h;

		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;

		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		// if you want to rotate the Bitmap
		// matrix.postRotate(45);
		Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width, height, matrix, true);
		return resizedBitmap;
	}

	/**
	 * byte[]转为bitmap （解决oom）
	 * @param imgByte
	 * @return
	 */
	public static Bitmap byteToBitmap(byte[] imgByte) {
		InputStream input = null;
		Bitmap bitmap = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 3;
		Log.e("TTT","压缩inSampleSize："+options.inSampleSize);
		input = new ByteArrayInputStream(imgByte);
		SoftReference softRef = new SoftReference(BitmapFactory.decodeStream(
				input, null, options));
		bitmap = (Bitmap) softRef.get();
		if (imgByte != null) {
			imgByte = null;
		}

		try {
			if (input != null) {
				input.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bitmap;
	}

}
