package com.topjet.common.utils;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.view.View;

import com.topjet.common.widget.clip.ClipImageViewActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by tsj-004 on 2017/8/11.
 * 图片相关操作
 */

public class ImageUtil {
    /**
     * bitmap保存到file文件
     */
    public static void saveImage2FileByBitmap(Context context, String fileName,
                                              Bitmap bitmap, int quality) {
        if (bitmap == null || fileName == null || context == null)
            return;

        try {
            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            compress(file, bitmap, quality);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 图片文件压缩到图片文件
     * 按质量压缩
     */
    public static void compressImageVolume2FileByFile(Context context, String fileName, int quality) {
        if (fileName == null || context == null)
            return;
        File file = new File(fileName);
        if (!file.exists()) {
            return;
        }
        Bitmap bitmap = BitmapFactory.decodeFile(fileName);
        compress(file, bitmap, quality);
    }

    /**
     * 图片文件压缩到图片文件
     * 按尺寸和质量压缩
     */
    public static void compressImageSize2FileByFile(Context context, String srcName, String newName, float ww, float
            hh, int quality) {
        if (srcName == null || context == null)
            return;
        File srcFile = new File(srcName);
        File newFile = new File(newName);
        if (!srcFile.exists()) {
            return;
        }

        if (hh <= 0) {
            hh = 800f;// 这里设置高度为800f
        }
        if (ww <= 0) {
            ww = 480f;// 这里设置宽度为480f
        }
        if (quality <= 0 || quality > 100) {
            quality = 88;
        }

        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcName, newOpts);// 此时返回bm为空
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        be--;
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcName, newOpts);

        compress(newFile, bitmap, quality);
    }

    /**
     * 按质量压缩
     */
    private static void compress(File file, Bitmap bitmap, int quality) {
        try {
            FileOutputStream fos = new FileOutputStream(file);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream);
            byte[] bytes = stream.toByteArray();
            fos.write(bytes);
            fos.close();
            bitmap.recycle();
            bitmap = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将文件生成位图
     */
    public static BitmapDrawable getImageDrawableFromFile(String path) {
        try {
            //转换成byte 后 再格式化成位图
            byte[] data = getImageBytes(path);
            if (data != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);// 生成位图
                return new BitmapDrawable(bitmap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] getImageBytes(String path) {
        try {
            File file = new File(path);
            if (!file.exists()) {
                return null;
            }

            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] bt = new byte[1024];

            //得到文件的输入流
            InputStream in = new FileInputStream(file);

            //将文件读出到输出流中
            int readLength = in.read(bt);
            while (readLength != -1) {
                outStream.write(bt, 0, readLength);
                readLength = in.read(bt);
            }

            //转换成byte 后 再格式化成位图
            return outStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据控件的宽截取圆角图片
     */
    public static Bitmap createRoundConerImage(String pathName, View view, int mRadius) {
        return createRoundConerImage(pathName, view.getWidth(), view.getHeight(), mRadius);
    }

    /**
     * 根据宽度截取圆角图片
     * source的高度大于宽度
     * source的高度大于mHeight
     */
    public static Bitmap createRoundConerImage(String pathName, int mWidth, int mHeight, int mRadius) {
        Bitmap bitmap;
        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inJustDecodeBounds = true;
        options.inSampleSize = options.outWidth / mWidth;
        options.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(pathName, options);

        int tempX = bitmap.getWidth() / 2 - mWidth / 2;
        int tempY = bitmap.getHeight() / 2 - mHeight / 2;
        int tempWdith = mWidth;
        int tempHeight = mHeight;
        if (tempX < 0) {
            tempX = 0;
        }
        if (tempY < 0) {
            tempY = 0;
        }
        if (mWidth > bitmap.getWidth()) {
            tempWdith = bitmap.getWidth();
        }
        if (mHeight > bitmap.getHeight()) {
            tempHeight = bitmap.getHeight();
        }

        bitmap = Bitmap.createBitmap(bitmap, tempX, tempY, tempWdith, tempHeight);
        Paint paint = new Paint();
        paint.setAntiAlias(true);

        Bitmap target = Bitmap.createBitmap(bitmap.getWidth(), mHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(target);
        RectF rect = new RectF(0, 0, mWidth, mHeight);
        canvas.drawRoundRect(rect, mRadius, mRadius, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return target;
    }

//    /**
//     * 请求裁剪[系统原生裁剪]
//     */
//    public static void toCrop(Activity ac, File tempFile) {
//        Intent intent = new Intent("com.android.camera.action.CROP");
//        Uri imageFileUri = null;
//        if (Build.VERSION.SDK_INT >= 24) {
//            imageFileUri = getImageContentUri(ac, tempFile);
//            //设置权限
//            ac.grantUriPermission("com.android.camera", imageFileUri,
//                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        } else {
//            imageFileUri = Uri.fromFile(tempFile);                            //获取文件的Uri
//        }
//        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//        intent.setDataAndType(imageFileUri, "image/*");
//        intent.putExtra("crop", "true");    // crop为true是设置在开启的intent中设置显示的view可以剪裁
//        intent.putExtra("aspectX", 480);    // aspectX aspectY 是宽高的比例
//        intent.putExtra("aspectY", 800);
//        intent.putExtra("outputX", 960);    // outputX,outputY 是剪裁图片的宽高
//        intent.putExtra("outputY", 1600);
//        intent.putExtra("scale", true);//保持比例
//        Uri imageFileUri2 = Uri.fromFile(tempFile);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageFileUri2);
//        intent.putExtra("return-data", false);
//        intent.putExtra("outputFormat", "JPEG");
//        intent.putExtra("noFaceDetection", true);
//        ac.startActivityForResult(intent, CameraUtil.REQUEST_CODE.CROP);
//    }

    /**
     * 请求裁剪[正方形]
     */
    public static void toCropForSquare(Activity ac, File tempFile) {
        Intent intent = new Intent(ac, ClipImageViewActivity.class);
        intent.putExtra("url", tempFile.getAbsolutePath());
        intent.putExtra("type", "s");
        intent.putExtra("padding", 0);
        ac.startActivityForResult(intent, CameraUtil.REQUEST_CODE.SQUARE_CROP);
    }

    /**
     * 请求裁剪[矩形]
     */
    public static void toCropForRectangle(Activity ac, File tempFile) {
        Intent intent = new Intent(ac, ClipImageViewActivity.class);
        intent.putExtra("url", tempFile.getAbsolutePath());
        intent.putExtra("type", "r");
        intent.putExtra("padding", 0);
        ac.startActivityForResult(intent, CameraUtil.REQUEST_CODE.RECTANGLE_CROP);
    }

    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    /* ============================尺寸压缩，质量压缩，转Base64系列操作代码 start============================*/

    /**
     * 480*800尺寸时获取方式
     */
    public static String getBase64With480x800(String truck_icon_img) {
        return getBase64String(truck_icon_img, 480, 800, 300);

//        return getBase64String(truck_icon_img, 30, 30, 10);
    }

    /**
     * 把bitmap转换成Base64字符串
     *
     * @param filePath:图片路径
     * @param reqWidth：设置图片的宽度
     * @param reqHeight：设置图片的高度
     * @param bitmapMaxSize：压缩图片最终大小峰值。比如300K
     * @return 返回Base64字符串
     */
    @Nullable
    public static String getBase64String(String filePath, int reqWidth, int reqHeight, int bitmapMaxSize) {
        File f = new File(filePath);
        boolean h = f.exists();
        Logger.i("TTT", "bitmapToBase64String===filePath:" + filePath + h);
        // 先尺寸压缩
        Bitmap bm = getBitmap(filePath, reqWidth, reqHeight);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (bm != null) {
            int options = 100;
            if (getBitmapSize(bm) / 1024 > bitmapMaxSize) {
                options = 70;
            }
            try {
                bm.compress(Bitmap.CompressFormat.JPEG, options, baos);//先做一次质量压缩
                while (baos.toByteArray().length / 1024 > bitmapMaxSize) { // 循环判断如果压缩后图片是否大于指定大小,大于继续压缩
                    baos.reset();// 重置baos即清空baos
                    if (options < 0) {
                        options = 10;
                        bm.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
                        break;
                    }
                    options -= 10;// 每次都减少10
                    bm.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
                }
                byte[] b = baos.toByteArray();
                bm.recycle();
                String temp = Base64.encodeToString(b, Base64.NO_WRAP);

                temp = temp.replaceAll("\\r\\n", "");
                temp = temp.replaceAll("\\r", "");
                temp = temp.replaceAll("\\n", "");
                temp = temp.replaceAll("\r\n", "");
                temp = temp.replaceAll("\r", "");
                temp = temp.replaceAll("\n", "");
                temp = temp.replaceAll("\\+", "%2B");        // 替换加号
                return temp;
            } catch (Exception e) {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * 根据路径获得图片并尺寸压缩，返回bitmap
     *
     * @param filePath:图片路径
     * @param reqWidth：设置图片的宽度
     * @param reqHeight：设置图片的高度
     * @return 返回bitmap
     */
    public static Bitmap getBitmap(String filePath, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;//不读进内存
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        BitmapFactory.decodeFile(filePath, options);

        // 计算图片的缩放值
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;// 读进内存

        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * 根据路径获得图片，返回bitmap
     * 图片宽高为原来的一半
     *
     * @param filePath:图片路径
     * @return 返回bitmap
     */
    public static Bitmap getBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;// 不读进内存
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        BitmapFactory.decodeFile(filePath, options);

        options.inSampleSize = 2;

        options.inJustDecodeBounds = false;// 读进内存

        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * 从Assets读取bitmap
     */
    public static Bitmap getImageFromAssetsFile(Context context, String fileName) {
        Bitmap image = null;
        AssetManager am = context.getAssets();
        try {
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;
    }

    /**
     * 获取图片文件的信息，是否旋转了90度，如果是则反转
     *
     * @param bitmap 需要旋转的图片
     */
    public static Bitmap reviewPicRotate(Bitmap bitmap, int degree) {
        if (degree != 0) {
            Matrix m = new Matrix();
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            m.setRotate(degree); // 旋转angle度
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, m, true);// 从新生成图片
        }
        return bitmap;
    }

    /**
     * 计算图片的缩放值
     */
    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        if (reqWidth == 0) {
            reqWidth = 1;
        }
        if (reqHeight == 0) {
            reqHeight = 1;
        }
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        // 如果图片的宽或者高  大于指定的 宽或者高，则计算缩放比例。
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    /**
     * 得到bitmap的大小
     */
    private static int getBitmapSize(Bitmap bitmap) {
        if (bitmap == null) {
            return 0;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { // API 19
            return bitmap.getAllocationByteCount();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {// API 12
            return bitmap.getByteCount();
        }
        // 在低版本中用一行的字节x高度
        return bitmap.getRowBytes() * bitmap.getHeight(); // earlier version
    }

     /* ============================尺寸压缩，质量压缩，转Base64系列操作代码 end============================*/

}
