package app.ocrlib.com.facepicture;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.media.FaceDetector;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;

import com.cgbsoft.lib.utils.constant.Constant;
import com.cgbsoft.lib.utils.constant.RxConstant;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxBus;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.tools.DownloadUtils;
import com.cgbsoft.lib.utils.tools.PromptManager;
import com.cgbsoft.privatefund.bean.living.FaceInf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import app.ocrlib.com.R;
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
    Camera camera;

    SurfaceView surfaceview;

    SurfaceHolder surfaceholder;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facepicture);
        surfaceview = (SurfaceView) findViewById(R.id.facepicture_surfaceview);
        surfaceholder = surfaceview.getHolder();
        surfaceholder.addCallback(this);
    }

    /**
     * 初始化相机
     */
    private void initCamera() {
        //CameraID表示0或者1，表示是前置摄像头还是后置摄像头
        camera = Camera.open(1);
        //getPicImageResult();
        Camera.Parameters parameters = camera.getParameters();
        //强制竖屏
        camera.setDisplayOrientation(90);
        setPreviewSize(camera, parameters);

    }

    public void paizhao(View V) {
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
                        image.compressToJpeg(new Rect(0, 0, size.width, size.height), 80, stream);

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
        PromptManager.ShowCustomToast(FacePictureActivity.this, "成功截取");
        //开始上传bitmap并获取远程路径
        upLoadBitmap(nbmp2);


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
                        RxBus.get().post(RxConstant.COMPLIANCE_FACEUP, new FaceInf(data,facePath));
                        Log.i("PersonCompare", "上传成功了" + data);
//                        personCompare(data);
//                        FacePictureActivity.this.finish();

                    }
                });
    }

    /**
     * 检测人脸
     */
    private void findFace(final Bitmap bitmap, final int postion, final Handler handler) {
        if (null == bitmap) return;
        final int MAX_FACES = 1;
        //因为这是一个耗时的操作，所以放到另一个线程中运行
        new Thread(new Runnable() {
            @Override
            public void run() {
                FaceDetector.Face[] faces = new FaceDetector.Face[MAX_FACES];
                //格式必须为RGB_565才可以识别
                Bitmap bmp = bitmap.copy(Bitmap.Config.RGB_565, true);
                //返回识别的人脸数
                int faceCount = new FaceDetector(bmp.getWidth(), bmp.getHeight(), MAX_FACES).findFaces(bmp, faces);
                bmp.recycle();
                Message message = new Message();
                message.what = faceCount;
                message.arg1 = postion;
                message.obj = bitmap;
                handler.sendMessage(message);

            }


        }).start();


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
            // 如果sizeList只有一个我们也没有必要做什么了，因为就他一个别无选择
            if (sizeList.size() > 1) {
                Iterator<Camera.Size> itor = sizeList.iterator();
                while (itor.hasNext()) {
                    Camera.Size cur = itor.next();
                    //if(cur.width<1000)minSize=cur.width;
                    if (cur.width >= PreviewWidth
                            && cur.height >= PreviewHeight) {

                        PreviewWidth = cur.width;
                        PreviewHeight = cur.height;
                        break;
                    }
                }
            }
            parameters.setPreviewSize(PreviewWidth, PreviewHeight); //获得摄像区域的大小
//            parameters.setPreviewFrameRate(3);//每秒3帧  每秒从摄像头里面获得3个画面
            parameters.setJpegQuality(100);
            parameters.setPictureFormat(PixelFormat.JPEG);//设置照片输出的格式
            parameters.set("jpeg-quality", 100);//设置照片质量
            parameters.setPictureSize(PreviewWidth, PreviewHeight);//设置拍出来的屏幕大小
//            //
            //设置放大倍数
            parameters.setZoom(1);
            camera.setParameters(parameters);//把上面的设置 赋给摄像头
        } catch (Exception e) {
//            Log.e(TAG, e.toString());
        }

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
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        camera.stopPreview();
        //手动释放 一定得加！
        camera.release();
        camera=null;
    }

    /**
     * person对比
     *
     * @param remotpath
     */
    private void personCompare(final String remotpath) {
        ApiClient.getPersonCompare(remotpath).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                Log.i("PersonCompare", "对比成功了" + remotpath);
                PromptManager.ShowCustomToast(FacePictureActivity.this,"对比成功了！！！！" + remotpath);
            }

            @Override
            protected void onRxError(Throwable error) {
                Log.i("PersonCompare", "对比失败了" + remotpath);
                PromptManager.ShowCustomToast(FacePictureActivity.this,"对比失败了" );
            }
        });
    }
}
