//package com.cn.hugo.android.scanner;
//
//import android.app.Activity;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.TextView;
//
//import com.cgbsoft.lib.utils.tools.SDCardUtil;
//import com.cn.hugo.android.scanner.view.ClipImageLayout;
//
//import java.io.File;
//
//import io.rong.eventbus.EventBus;
//
//
//public class ClipQrCodeActivity extends Activity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_clip_qr_code);
//        bindView();
//        show();
//    }
//
//    private ClipImageLayout clip;
//    private TextView cancel;
//    private TextView queding;
//    private String mSelectPath;
//
//    private void bindView() {
//        clip = (ClipImageLayout) findViewById(R.id.clip_image);
//        cancel = (TextView) findViewById(R.id.cancel);
//        queding = (TextView) findViewById(R.id.next);
//        mSelectPath = getIntent().getStringExtra("mSelectPath");
//    }
//
//    private void show() {
//
//        try {
//            int degree = SDCardUtil.getBitmapDegree(mSelectPath);
//
//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inSampleSize = 2;
//            Bitmap bm = SDCardUtil.rotateBitmapByDegree(BitmapFactory.decodeFile(mSelectPath, options), degree);
//
//            // Bitmap bitmap = SDCardUtil.loadFileFromSDCardToBitmap(mSelectPath);
////            Drawable drawable = new BitmapDrawable(bm);
//
//            // clip.setBitmap(bitmap);
////            clip.setDrawableData(drawable);
//            clip.setBitmapData(bm);
//
////            bm.recycle();
////            bm = null;
//
////            BitmapFactory.Options opts = new BitmapFactory.Options();
////            opts.inJustDecodeBounds = true;BitmapFactory.decodeFile(mSelectPath, opts);
////            opts.inSampleSize = computeSampleSize(opts, -1, 128*128);
////            opts.inJustDecodeBounds = false;
////            Bitmap bmp = null;
////            try {
////                bmp = BitmapFactory.decodeFile(mSelectPath, opts);
////                clip.setBitmapData(bmp);
////            } catch (OutOfMemoryError err) {
////
////            }finally {
////                if(null != bmp){
////                    bmp = null;
////                }
////            }
//
//            cancel.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    // TODO Auto-generated method stub
//                    ClipQrCodeActivity.this.finish();
//                }
//            });
//
//            queding.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    // TODO Auto-generated method stub
//                    Bitmap bitmap = clip.clip();
//
//                    String filePath = "simuyun" + File.pathSeparator + "Image";
//                    SDCardUtil.saveFileToSDCardCustomDir(bitmap, filePath, "qrcode.png");
//    //                EventBus.getDefault()
//    //                        .post(new EventBusQRcodeImg(SDCardUtil.getSDCardBaseDir()
//    //                                + File.pathSeparator + filePath + "qrcode.png"));
//                    EventBus.getDefault().post(new EventBusQRcodeImg(getIntent().getStringExtra("mSelectPath")));
//                    ClipQrCodeActivity.this.finish();
//
//                }
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        // TODO Auto-generated method stub
//        super.onDestroy();
//        EventBus.getDefault().unregister(this);
//    }
//
//    public static int computeSampleSize(BitmapFactory.Options options,
//                                        int minSideLength, int maxNumOfPixels) {
//        int initialSize = computeInitialSampleSize(options, minSideLength,
//                maxNumOfPixels);
//
//        int roundedSize;
//        if (initialSize <= 8) {
//            roundedSize = 1;
//            while (roundedSize < initialSize) {
//                roundedSize <<= 1;
//            }
//        } else {
//            roundedSize = (initialSize + 7) / 8 * 8;
//        }
//        return roundedSize;
//    }
//
//    public static int computeInitialSampleSize(BitmapFactory.Options options,
//                                               int minSideLength, int maxNumOfPixels) {
//        double w = options.outWidth;
//        double h = options.outHeight;
//        int lowerBound = (maxNumOfPixels == -1) ? 1 :
//                (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
//        int upperBound = (minSideLength == -1) ? 128 :
//                (int) Math.min(Math.floor(w / minSideLength),
//                        Math.floor(h / minSideLength));
//
//        if (upperBound < lowerBound) {
//            // return the larger one when there is no overlapping zone.
//            return lowerBound;
//        }
//
//        if ((maxNumOfPixels == -1) &&
//                (minSideLength == -1)) {
//            return 1;
//        } else if (minSideLength == -1) {
//            return lowerBound;
//        } else {
//            return upperBound;
//        }
//    }
//}
