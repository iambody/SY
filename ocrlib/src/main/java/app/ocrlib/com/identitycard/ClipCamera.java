package app.ocrlib.com.identitycard;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;


public class ClipCamera extends SurfaceView implements SurfaceHolder.Callback, Camera.AutoFocusCallback {
    private static final String TAG = "ClipCamera:";
    private int mScreenWidth;
    private int mScreenHeight;
    private Context ctx;
    private SurfaceHolder holder;
    private Camera mCamera;
    private CameraResult cameraResult;

    public ClipCamera(Context context) {
        this(context, null);
    }

    public ClipCamera(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClipCamera(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ctx = context;
        initView();
    }


    private void initView() {
        getScreenMetrix(ctx);
        holder = getHolder();//获得surfaceHolder引用
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);//设置类型
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


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (mCamera == null) {
            mCamera = Camera.open();//开启相机
            setCameraParams(mCamera, mScreenWidth, mScreenHeight);
            try {
                mCamera.setPreviewDisplay(holder);//摄像头画面显示在Surface上
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mCamera.startPreview();

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        try {
            mCamera.stopPreview();//停止预览
            mCamera.release();//释放相机资源
            mCamera = null;
            holder = null;
        } catch (Exception e) {
        }
    }

    public void closeCamera() {
        if (mCamera == null) {
            return;
        }

        mCamera.release();
        mCamera = null;
    }

    @Override
    public void onAutoFocus(boolean success, Camera camera) {
        if (success) {
            Log.i(TAG, "onAutoFocus success=" + success);
        }
    }

    private void setCameraParams(Camera camera, int width, int height) {
        Log.i(TAG, "setCameraParams  width=" + width + "  height=" + height);
        Camera.Parameters parameters = mCamera.getParameters();
        // 获取摄像头支持的PictureSize列表
        List<Camera.Size> pictureSizeList = parameters.getSupportedPictureSizes();
        for (Camera.Size size : pictureSizeList) {
            Log.i(TAG, "pictureSizeList size.width=" + size.width + "  size.height=" + size.height);
        }
        /**从列表中选取合适的分辨率*/
        Camera.Size picSize = getProperSize(pictureSizeList, ((float) height / width));
        if (null == picSize) {
            Log.i(TAG, "null == picSize");
            picSize = parameters.getPictureSize();
        }
        Log.i(TAG, "picSize.width=" + picSize.width + "  picSize.height=" + picSize.height);
        // 根据选出的PictureSize重新设置SurfaceView大小
        float w = picSize.width;
        float h = picSize.height;
        parameters.setPictureSize(picSize.width, picSize.height);
        this.setLayoutParams(new RelativeLayout.LayoutParams((int) (height * (h / w)), height));

        // 获取摄像头支持的PreviewSize列表
        List<Camera.Size> previewSizeList = parameters.getSupportedPreviewSizes();

        for (Camera.Size size : previewSizeList) {
            Log.i(TAG, "previewSizeList size.width=" + size.width + "  size.height=" + size.height);
        }
        Camera.Size preSize = getProperSize(previewSizeList, ((float) height) / width);
//        Camera.Size preSize =getCloselyPreSize(width,height,previewSizeList);
        if (null != preSize) {
            Log.i(TAG, "preSize.width=" + preSize.width + "  preSize.height=" + preSize.height);
            parameters.setPreviewSize(preSize.width, preSize.height);

        }

        parameters.setJpegQuality(100); // 设置照片质量
        if (parameters.getSupportedFocusModes().contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);// 连续对焦模式
        }

        mCamera.cancelAutoFocus();//自动对焦。
        mCamera.setDisplayOrientation(90);// 设置PreviewDisplay的方向，效果就是将捕获的画面旋转多少度显示
        mCamera.setParameters(parameters);

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
                                            List<Camera.Size> preSizeList) {

        int ReqTmpWidth;
        int ReqTmpHeight;
        // 当屏幕为垂直的时候需要把宽高值进行调换，保证宽大于高
//        if (mIsPortrait) {
//            ReqTmpWidth = surfaceHeight;
//            ReqTmpHeight = surfaceWidth;
//        } else {
        ReqTmpWidth = surfaceWidth;
        ReqTmpHeight = surfaceHeight;
//        }
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

    /**
     * 从列表中选取合适的分辨率
     * 默认w:h = 4:3
     * <p>注意：这里的w对应屏幕的height
     * h对应屏幕的width<p/>
     */
    private Camera.Size getProperSize(List<Camera.Size> pictureSizeList, float screenRatio) {
        Log.i(TAG, "screenRatio=" + screenRatio);
        Camera.Size result = null;
        for (Camera.Size size : pictureSizeList) {
            float currentRatio = ((float) size.width) / size.height;
            if (currentRatio - screenRatio == 0) {
                result = size;
                break;
            }
        }

        if (null == result) {
            for (Camera.Size size : pictureSizeList) {
                float curRatio = ((float) size.width) / size.height;
                if (curRatio == 4f / 3) {// 默认w:h = 4:3
                    result = size;
                    break;
                }
            }
        }

        return result;
    }


    //创建jpeg图片回调数据对象
    private Camera.PictureCallback jpeg = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera Camera) {
            BufferedOutputStream bos = null;
            Bitmap bm = null;
            try {
                // 获得图片
                bm = BitmapFactory.decodeByteArray(data, 0, data.length);
                int pic_width = bm.getWidth();//1280
                int pic_height = bm.getHeight();//720
                int height, width, x_center, y_center;
                height = (int) (pic_height * 0.8);//屏幕宽的0.8,拍照取景框的宽为屏幕的0.8
                width = (int) (height * 1.6);
                x_center = pic_width / 2;
                y_center = pic_height / 2;


                Matrix matrix = new Matrix();
                matrix.postRotate(360, pic_width / 2, pic_height / 2);
                bm = Bitmap.createBitmap(bm, x_center - (width / 2),
                        y_center - (height / 2),
                        (int) (pic_height * 0.8 * 1.6),
                        (int) (pic_height * 0.8),
                        matrix, false);

                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    Log.i(TAG, "Environment.getExternalStorageDirectory()=" + Environment.getExternalStorageDirectory());
                    //   String filePath = "/sdcard/dyk"+System.currentTimeMillis()+".jpg";//照片保存路径
                    File file = new File(savePath);
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    bos = new BufferedOutputStream(new FileOutputStream(file));
                    bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);//将图片压缩到流中
                    if (null != cameraResult) {
                        cameraResult.picResult(savePath);
                    }
                } else {
                    File file = new File(savePath);
                    file.delete();
                    Toast.makeText(ctx, "没有检测到内存卡", Toast.LENGTH_SHORT).show();
                    cameraResult.picFailed();
                }
            } catch (Exception e) {
                File file = new File(savePath);
                file.delete();
                cameraResult.picFailed();
                e.printStackTrace();
            } finally {
                try {
                    bos.flush();//输出
                    bos.close();//关闭
                    bm.recycle();// 回收bitmap空间
                    mCamera.stopPreview();// 关闭预览
                    Uri uri = Uri.fromFile(new File(savePath));
                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    intent.setData(uri);
                    ctx.sendBroadcast(intent);
//                    ctx.startActivity(intent.setClass(ctx, IdentityCardTest.class));
                } catch (IOException e) {
                    e.printStackTrace();


                }
            }

        }
    };

    String savePath;

    public void takePicture(String savePath, CameraResult cameraResults) {
        this.cameraResult = cameraResults;
        this.savePath = savePath;
        //设置参数,并拍照
        setCameraParams(mCamera, mScreenWidth, mScreenHeight);
        // 当调用camera.takePiture方法后，camera关闭了预览，这时需要调用startPreview()来重新开启预览
        mCamera.takePicture(null, null, jpeg);
    }

    private IAutoFocus mIAutoFocus;

    /**
     * 聚焦的回调接口
     */
    public interface IAutoFocus {
        void autoFocus();
    }

    /**
     * 拍照成功的回调回调成功的
     **/
    public interface CameraResult {
        /**
         * 拍照成功
         **/
        void picResult(String ivPath);

        /**
         * 拍照失败
         **/
        void picFailed();
    }


    public void setIAutoFocus(IAutoFocus mIAutoFocus) {
        this.mIAutoFocus = mIAutoFocus;
    }

    public void setAutoFocus() {
        mCamera.autoFocus(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mIAutoFocus != null) {
            mIAutoFocus.autoFocus();
        }
        return super.onTouchEvent(event);
    }

}
