package com.common.tools;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;

import java.io.File;
import java.io.IOException;

/**
 * 图片工具类
 */
public class BitmapHelper {

    /**
     * 压缩、旋转 图片文件
     *
     * @param imagePath 文件路径
     * @param width
     * @param height
     * @return
     */
    public static Bitmap decodeScaleImage(String imagePath, int width, int height) {
        BitmapFactory.Options bitmapOptions = getBitmapOptions(imagePath);
        int scale = calculateInSampleSize(bitmapOptions, width, height);
        bitmapOptions.inSampleSize = scale;
        bitmapOptions.inJustDecodeBounds = false;
        Bitmap scaleBitmap = BitmapFactory.decodeFile(imagePath, bitmapOptions);
        int degree = readPictureDegree(imagePath);
        Bitmap rotateBitmap = null;
        if (scaleBitmap != null && degree != 0) {
            rotateBitmap = rotateBitmap(scaleBitmap, degree);
            scaleBitmap.recycle();
            scaleBitmap = null;
            return rotateBitmap;
        } else {
            return scaleBitmap;
        }
    }

    /**
     * 读取图片文件旋转角度
     *
     * @param path 图片的路径
     * @return
     */
    public static int readPictureDegree(String path) {
        short degress = 0;
        try {
            ExifInterface var2 = new ExifInterface(path);
            int var3 = var2.getAttributeInt("Orientation", 1);
            switch (var3) {
                case 3:
                    degress = 180;
                case 4:
                case 5:
                case 7:
                default:
                    break;
                case 6:
                    degress = 90;
                    break;
                case 8:
                    degress = 270;
            }
        } catch (IOException var4) {
            var4.printStackTrace();
        }
        return degress;
    }

    /**
     * 计算图片的缩放值
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio > widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }


    /**
     * 旋转bitmap
     *
     * @param bitmap
     * @param degress
     * @return
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, int degress) {
        if (bitmap != null) {
            Matrix m = new Matrix();
            m.postRotate((float) degress);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
            return bitmap;
        }
        return bitmap;
    }

    /**
     * 获取bitmap属性
     *
     * @param imagePath
     * @return
     */
    public static BitmapFactory.Options getBitmapOptions(String imagePath) {
        BitmapFactory.Options var1 = new BitmapFactory.Options();
        var1.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, var1);
        return var1;
    }

    /**
     * 保存图片 通知系统扫描
     *
     * @param activity
     * @param newImageFile
     * @param isInsertToGallary 是否插入系统相册
     * @param isScanFile        是否扫描原来图片
     * @return
     */
    public static String notifysaveImage(Activity activity, File newImageFile, boolean isInsertToGallary, boolean isScanFile) {
        String uri = "";
        if (activity == null || newImageFile == null || newImageFile.length() == 0) {
            return uri;
        }
        try {
            if (isInsertToGallary) {
                uri = MediaStore.Images.Media.insertImage(activity.getContentResolver(), newImageFile.getAbsolutePath(), newImageFile.getName(), "");
            }
            if (isScanFile) {
                activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + newImageFile.getAbsolutePath())));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return uri;
    }

    /**
     * 通知系统相册
     *
     * @param activity
     * @param newImageFile
     */
    public static void notifysaveImage(Activity activity, File newImageFile) {
        notifysaveImage(activity, newImageFile, false, true);
    }

    public static Bitmap getViewBitmap(View view, int width, int height) {
        view.measure(View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    public static void recycleBitmap(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
        System.gc();
    }
}