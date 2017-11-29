package app.ocrlib.com.facepicture;

import android.Manifest;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.media.FaceDetector;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.cgbsoft.lib.utils.constant.Constant;
import com.cgbsoft.lib.utils.constant.RxConstant;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxBus;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.tools.CameraUtils;
import com.cgbsoft.lib.utils.tools.DownloadUtils;
import com.cgbsoft.lib.utils.tools.PromptManager;
import com.cgbsoft.lib.utils.tools.RxCountDown;
import com.cgbsoft.privatefund.bean.living.FaceInf;
import com.cgbsoft.privatefund.bean.living.LivingResultData;
import com.cgbsoft.privatefund.bean.living.PersonCompare;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import app.ocrlib.com.R;
import app.ocrlib.com.utils.AnimUtils;
import app.ocrlib.com.utils.BitmapUtils;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * desc    人脸拍照
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/10/24-16:28
 */
public class FacePictureActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    private Camera camera;
    private SurfaceView surfaceview;
    private SurfaceHolder surfaceholder;
    //是否需要进行person对比
    private boolean isNeedPersonCompare = false;
    //需要进行person比较的key
    public static String TAG_NEED_PERSON = "needPersonCompare";
    private boolean isCanclick = true;
    public static final String PAGE_TAG = "pagtag";
    public static String currentPageTag;
    private ImageView facepiceture_detection_iv;
    private ImageView facepiceture_eye_detection_iv;
    private int mScreenWidth;
    private int mScreenHeight;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (null != getIntent().getExtras() && getIntent().getExtras().containsKey(PAGE_TAG)) {
            currentPageTag = getIntent().getStringExtra(PAGE_TAG);
        } else {
            PromptManager.ShowCustomToast(this, getResources().getString(R.string.put_parame));
            finish();
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (CameraUtils.getCameraPermission(this)) {
                setContentView(R.layout.activity_facepicture);
                initview();
            } else {
                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 22);
            }


        } else {
            setContentView(R.layout.activity_facepicture);
            initview();
        }

    }

    private void initview() {
        isNeedPersonCompare = getIntent().getBooleanExtra(TAG_NEED_PERSON, false);
        surfaceview = (SurfaceView) findViewById(R.id.facepicture_surfaceview);
        facepiceture_detection_iv = (ImageView) findViewById(R.id.facepiceture_detection_iv);
        facepiceture_eye_detection_iv = (ImageView) findViewById(R.id.facepiceture_eye_detection_iv);
        surfaceholder = surfaceview.getHolder();
        surfaceholder.addCallback(this);
        getScreenMetrix(this);
    }

    /**
     * 初始化相机
     */
    private void initCamera() {
        //CameraID表示0或者1，表示是前置摄像头还是后置摄像头
        try {
            camera = Camera.open(1);
        } catch (Exception e) {
//            camera = Camera.open();
            Log.i("kskkssa", e.getMessage());
        }
        //getPicImageResult();
        Camera.Parameters parameters = camera.getParameters();
        //强制竖屏
        camera.setDisplayOrientation(90);
        setPreviewSize(camera, parameters);

    }

    public void paizhao(View V) {
        if (!isCanclick) return;
        isCanclick = false;
        getPicImageResult();
    }

    /**
     * 监听获取的图片
     */
    private void getPicImageResult() {
        camera.setOneShotPreviewCallback(new Camera.PreviewCallback() {
            @Override
            public void onPreviewFrame(byte[] data, Camera camera) {
                //生成 bitmap
                Camera.Size size = camera.getParameters().getPreviewSize();
                try {
                    YuvImage image = new YuvImage(data, ImageFormat.NV21, size.width, size.height, null);
                    if (image != null) {
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        image.compressToJpeg(new Rect(0, 0, size.width, size.height), 70, stream);

                        Bitmap bmp = BitmapFactory.decodeByteArray(stream.toByteArray(), 0, stream.size());

                        //**********************
                        //因为图片会放生旋转，因此要对图片进行旋转到和手机在一个方向上
                        rotateMyBitmap(bmp);
                        //**********************************

                        stream.close();
                    }
                } catch (Exception ex) {
                    Log.e("Sys", "Error:" + ex.getMessage());
                }
                Log.i("camera444444", "previewFrame调用");
            }
        });

    }

    /**
     * 旋转bitmap
     *
     * @param bmp
     */
    private void rotateMyBitmap(Bitmap bmp) {
        //*****旋转一下
        Matrix matrix = new Matrix();
        matrix.postRotate(-90);
        //Bitmap bitmap = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), Bitmap.Config.ARGB_8888);
        Bitmap nbmp2 = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
        //开始上传bitmap并获取远程路径
        findFace(nbmp2);
    }

    String facePath = null;

    //开始上传bitmap并且进行处理
    private void upLoadBitmap(Bitmap nbmp2) {
        facePath = BitmapUtils.saveBitmap(nbmp2, "face");
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                //异步操作相关代码
                String imageId = DownloadUtils.postSecretObject(facePath, Constant.UPLOAD_COMPLIANCE_FACE);
                subscriber.onNext(imageId);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String data) {
                        // 主线程操作
                        Log.i("PersonCompare", "上传成功了" + data);
                        if (isNeedPersonCompare) {
                            personCompare(data);
                        } else {
                            Log.i("PersonCompare", "没进行对比直接退出并发通知" + data);

                            RxBus.get().post(RxConstant.COMPLIANCE_FACEUP, new FaceInf(data, facePath, currentPageTag));
                            FacePictureActivity.this.finish();
                        }

                    }
                });
    }

    /**
     * 检测人脸
     */
    private void findFace(final Bitmap bitmap) {
        if (null == bitmap) return;
        final int MAX_FACES = 1;
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                //因为这是一个耗时的操作，所以放到另一个线程中运行
                FaceDetector.Face[] faces = new FaceDetector.Face[MAX_FACES];
                //格式必须为RGB_565才可以识别
                Bitmap bmp = bitmap.copy(Bitmap.Config.RGB_565, true);
                //返回识别的人脸数
                int faceCount = new FaceDetector(bmp.getWidth(), bmp.getHeight(), MAX_FACES).findFaces(bmp, faces);
                bmp.recycle();
                subscriber.onNext(faceCount);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer bitmapNumber) {
                        if (bitmapNumber >= 1) {
                            AnimUtils.startEyeDetection(FacePictureActivity.this, facepiceture_eye_detection_iv, facepiceture_detection_iv);
                            // todo 开始调接口
                            //开始上传bitmap并获取远程路径
                            upLoadBitmap(bitmap);

                        } else {
                            AnimUtils.playNotece(FacePictureActivity.this);
                            PromptManager.ShowCustomToast(FacePictureActivity.this, "请人脸对准相框");
                            getPicImageResult();
                        }
                    }
                });


    }

    /**
     * 设置preview和picturesize的尺寸 大多数手机因为设置的不支持底层 会造成崩溃
     */
    public void setPreviewSize(Camera camera, Camera.Parameters parameters) {
        try {
            int PreviewWidth = 100;
            int PreviewHeight = 100;
            WindowManager wm = (WindowManager) getSystemService(FacePictureActivity.this.WINDOW_SERVICE);//获取窗口的管理器
            Display display = wm.getDefaultDisplay();//获得窗口里面的屏幕
            // 选择合适的预览尺寸
            List<Camera.Size> sizeList = parameters.getSupportedPreviewSizes();
            Camera.Size preSize = getCloselyPreSize(mScreenWidth, mScreenHeight, sizeList,true);
            if (null != preSize) {
                parameters.setPreviewSize(preSize.width, preSize.height); //获得摄像区域的大小
            }
            // 如果sizeList只有一个我们也没有必要做什么了，因为就他一个别无选择
//            if (sizeList.size() > 1) {
//                Iterator<Camera.Size> itor = sizeList.iterator();
//                while (itor.hasNext()) {
//                    Camera.Size cur = itor.next();
//                    //if(cur.width<1000)minSize=cur.width;
//                    if (cur.width >= PreviewWidth
//                            && cur.height >= PreviewHeight) {
//
//                        PreviewWidth = cur.width;
//                        PreviewHeight = cur.height;
//                        break;
//                    }
//                }
//            }

            //parameters.setPreviewFrameRate(3);//每秒3帧  每秒从摄像头里面获得3个画面
            parameters.setJpegQuality(100);
            parameters.setPictureFormat(PixelFormat.JPEG);//设置照片输出的格式
            parameters.set("jpeg-quality", 100);//设置照片质量
            parameters.setPictureSize(PreviewWidth, PreviewHeight);//设置拍出来的屏幕大小
            //设置放大倍数
            parameters.setZoom(1);
            //把上面的设置 赋给摄像头
            camera.setParameters(parameters);
        } catch (Exception e) {
        }

    }


    /**
     * 通过对比得到与宽高比最接近的尺寸（如果有相同尺寸，优先选择）
     *
     * @param surfaceWidth  需要被进行对比的原宽
     * @param surfaceHeight 需要被进行对比的原高
     * @param preSizeList   需要对比的预览尺寸列表
     * @return 得到与原宽高比例最接近的尺寸
     */
    protected Camera.Size getCloselyPreSize(int surfaceWidth, int surfaceHeight,
                                            List<Camera.Size> preSizeList, boolean mIsPortrait) {

        int ReqTmpWidth;
        int ReqTmpHeight;
        // 当屏幕为垂直的时候需要把宽高值进行调换，保证宽大于高
        if (mIsPortrait) {
            ReqTmpWidth = surfaceHeight;
            ReqTmpHeight = surfaceWidth;
        } else {
            ReqTmpWidth = surfaceWidth;
            ReqTmpHeight = surfaceHeight;
        }
        //先查找preview中是否存在与surfaceview相同宽高的尺寸
        for (Camera.Size size : preSizeList) {
            if ((size.width == ReqTmpWidth) && (size.height == ReqTmpHeight)) {
                return size;
            }
        }

        // 得到与传入的宽高比最接近的size
        float reqRatio = ((float) ReqTmpWidth) / ReqTmpHeight;
        float curRatio, deltaRatio;
        float deltaRatioMin = Float.MAX_VALUE;
        Camera.Size retSize = null;
        for (Camera.Size size : preSizeList) {
            curRatio = ((float) size.width) / size.height;
            deltaRatio = Math.abs(reqRatio - curRatio);
            if (deltaRatio < deltaRatioMin) {
                deltaRatioMin = deltaRatio;
                retSize = size;
            }
        }

        return retSize;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //开始初始化camera
        initCamera();
        try {
            camera.setPreviewDisplay(surfaceholder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        camera.startPreview();
        AnimUtils.playNotece(FacePictureActivity.this);
        //等待三秒在进行取帧
        RxCountDown.countdown(2).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.i("daojishi", "时间" + integer);
                if (0 == integer) {
                    getPicImageResult();
                }
            }
        });

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        camera.stopPreview();
        //手动释放 一定得加！
        camera.release();
        camera = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AnimUtils.stopFaceDetection();
        if (null != camera) {
            try {
                camera.release();
                camera = null;
            } catch (Exception e) {
            }
        }
    }

    /**
     * person对比
     *
     * @param remotpath
     */
    private void personCompare(final String remotpath) {
        Log.i("PersonCompare", "开始调用person对比" + remotpath);
        ApiClient.getPersonCompare(remotpath).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                try {
                    JSONObject obj = new JSONObject(s);
                    String result = obj.getString("result");
                    LivingResultData recognitionCode = new Gson().fromJson(result, LivingResultData.class);
                    if ("0".equals(recognitionCode.getRecognitionCode())) {//成功
                        RxBus.get().post(RxConstant.COMPLIANCE_PERSON_COMPARE, new PersonCompare(0, currentPageTag));
                        Log.i("PersonCompare", "对比成功了开始发射信息" + remotpath);
                    } else {//失败
                        RxBus.get().post(RxConstant.COMPLIANCE_PERSON_COMPARE, new PersonCompare(1, currentPageTag));
                        Log.i("PersonCompare", "对比失败了开始发射信息" + remotpath);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                FacePictureActivity.this.finish();
            }

            @Override
            protected void onRxError(Throwable error) {
                isCanclick = true;
                Log.i("PersonCompare", "对比失败了" + remotpath);
                RxBus.get().post(RxConstant.COMPLIANCE_PERSON_COMPARE, new PersonCompare(1, currentPageTag));
                FacePictureActivity.this.finish();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            RxBus.get().post(RxConstant.COMPIANCE_FACE_BACK, 1);
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 22) {
            if (CameraUtils.getCameraPermission(FacePictureActivity.this)) {
                setContentView(R.layout.activity_facepicture);
                initview();
            } else {
                FacePictureActivity.this.finish();
                PromptManager.ShowCustomToast(FacePictureActivity.this, "请去系统设置开启权限");
            }

        }
    }

    /**
     * 获取屏幕的宽高
     *
     * @param context
     */
    private void getScreenMetrix(Context context) {
        WindowManager WM = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        WM.getDefaultDisplay().getMetrics(outMetrics);
        mScreenWidth = outMetrics.widthPixels;
        mScreenHeight = outMetrics.heightPixels;
    }
}
