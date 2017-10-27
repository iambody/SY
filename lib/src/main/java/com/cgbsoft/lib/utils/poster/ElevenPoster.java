package com.cgbsoft.lib.utils.poster;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ScrollView;

import java.io.FileOutputStream;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/10/26-09:17
 */
public class ElevenPoster {
    /**
     * 保存一个view的bitmap路径
     *
     * @param view
     * @return
     */
    public static Bitmap convertViewToBitmap(View view) {
        view.setDrawingCacheEnabled(true);
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        return bitmap;
    }

    public static String saveBitmapToSDCard(Bitmap bitmap, String imagename) {
        String path = "/sdcard/" + "sxy" + imagename + ".jpg";
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(path);
            if (fos != null) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
                fos.flush();
                fos.close();
            }

            return path;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 获取一个view的bitmap路径
     *
     * @param view
     * @param imagename
     * @return
     */
    public static String getViewPath(View view, String imagename) {
        return saveBitmapToSDCard(convertViewToBitmap(view), imagename);

    }

    /**
     * 获取一个view的bitmap路径
     *
     * @param view
     * @param imagename
     * @return
     */
    public static String getScrollViewPath(ScrollView view, String imagename) {
        return saveBitmapToSDCard(screenShot(view), imagename);
    }

    public static Bitmap screenShot(ScrollView scrollView) {
        if (null == scrollView) {
            throw new IllegalArgumentException("parameter can't be null.");
        }
        int height = 0;
        Bitmap bitmap = null;
        for (int i = 0, s = scrollView.getChildCount(); i < s; i++) {
            height += scrollView.getChildAt(i).getHeight();
            scrollView.getChildAt(i).setBackgroundResource(android.R.drawable.screen_background_light);
        }
        try {
            bitmap = Bitmap.createBitmap(scrollView.getWidth(), height, Bitmap.Config.RGB_565);
        } catch (Exception e) {
            e.getMessage();
        }
        final Canvas canvas = new Canvas(bitmap);
        scrollView.draw(canvas);
        return bitmap;
    }

    /**
     * 如果是需要xml资源（和界面资源原理不同）需要先计算下宽高否则DrawingCache是没有的
     *
     * @param context
     * @param myView
     */
    public static void resetViewSize(Activity context, View myView) {
        DisplayMetrics metric = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;     // 屏幕宽度（像素）
        int height = metric.heightPixels;   // 屏幕高度（像素）

        layoutView(myView, width, height);//去到指定view大小的函数
    }

    //然后View和其内部的子View都具有了实际大小，也就是完成了布局，相当与添加到了界面上。接着就可以创建位图并在上面绘制了：
    public static void layoutView(View v, int width, int height) {
        // 指定整个View的大小 参数是左上角 和右下角的坐标
        v.layout(0, 0, width, height);
        int measuredWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
        int measuredHeight = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.AT_MOST);
        /** 当然，measure完后，并不会实际改变View的尺寸，需要调用View.layout方法去进行布局。
         * 按示例调用layout函数后，View的大小将会变成你想要设置成的大小。
         */
        v.measure(measuredWidth, measuredHeight);
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
    }


    //base64转换成bitmap后在进行保存

    /**
     * base64转为bitmap
     */
    public static Bitmap base64ToBitmap(String base64Data) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    /**
     * 获取base64的路径
     */
    public static String base64ToPath(String base64, String name) {
        return saveBitmapToSDCard(base64ToBitmap(base64), name);
    }

}
