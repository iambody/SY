package com.cn.hugo.android.scanner;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.base.webview.CwebNetConfig;
import com.cgbsoft.lib.base.webview.WebViewConstant;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.utils.constant.RxConstant;
import com.cgbsoft.lib.utils.rxjava.RxBus;
import com.cgbsoft.lib.utils.tools.DataUtils;
import com.cgbsoft.lib.utils.tools.Des3;
import com.cgbsoft.lib.utils.tools.NavigationUtils;
import com.chenenyu.router.annotation.Route;
import com.cn.hugo.android.scanner.camera.CameraManager;
import com.cn.hugo.android.scanner.common.BitmapUtils;
import com.cn.hugo.android.scanner.config.Config;
import com.cn.hugo.android.scanner.decode.BitmapDecoder;
import com.cn.hugo.android.scanner.decode.CaptureActivityHandler;
import com.cn.hugo.android.scanner.view.ViewfinderView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.Result;
import com.google.zxing.client.result.ResultParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * This activity opens the camera and does the actual scanning on a background
 * thread. It draws a viewfinder to help the user place the barcode correctly,
 * shows feedback as the image processing is happening, and then overlays the
 * results when a scan is successful.
 * <p/>
 * @author chenlong
 * 此Activity所做的事： 1.开启camera，在后台独立线程中完成扫描任务；
 * 2.绘制了一个扫描区（viewfinder）来帮助用户将条码置于其中以准确扫描； 3.扫描成功后会将扫描结果展示在界面上。
 */
@Route(RouteConfig.GOTO_TWO_CODE_ACTIVITY)
public final class CaptureActivity extends Activity implements
        SurfaceHolder.Callback, View.OnClickListener {

    private static final String TAG = CaptureActivity.class.getSimpleName();
    public static final int shuhui_result = 103;

    private static final int REQUEST_CODE = 100;

    private static final int PARSE_BARCODE_FAIL = 300;
    private static final int PARSE_BARCODE_SUC = 200;
    private static String nickname;
    private static String phoneNum;

    /**
     * 是否有预览
     */
    private boolean hasSurface;

    /**
     * 活动监控器。如果手机没有连接电源线，那么当相机开启后如果一直处于不被使用状态则该服务会将当前activity关闭。
     * 活动监控器全程监控扫描活跃状态，与CaptureActivity生命周期相同.每一次扫描过后都会重置该监控，即重新倒计时。
     */
    private InactivityTimer inactivityTimer;

    /**
     * 声音震动管理器。如果扫描成功后可以播放一段音频，也可以震动提醒，可以通过配置来决定扫描成功后的行为。
     */
    private BeepManager beepManager;

    /**
     * 闪光灯调节器。自动检测环境光线强弱并决定是否开启闪光灯
     */
    private AmbientLightManager ambientLightManager;

    private CameraManager cameraManager;
    /**
     * 扫描区域
     */
    private static ViewfinderView viewfinderView;

    private static CaptureActivityHandler handler;

    private static Result lastResult;

    private boolean isFlashlightOpen;

    /**
     * 【辅助解码的参数(用作MultiFormatReader的参数)】 编码类型，该参数告诉扫描器采用何种编码方式解码，即EAN-13，QR
     * Code等等 对应于DecodeHintType.POSSIBLE_FORMATS类型
     * 参考DecodeThread构造函数中如下代码：hints.put(DecodeHintType.POSSIBLE_FORMATS,
     * decodeFormats);
     */
    private Collection<BarcodeFormat> decodeFormats;

    /**
     * 【辅助解码的参数(用作MultiFormatReader的参数)】 该参数最终会传入MultiFormatReader，
     * 上面的decodeFormats和characterSet最终会先加入到decodeHints中 最终被设置到MultiFormatReader中
     * 参考DecodeHandler构造器中如下代码：multiFormatReader.setHints(hints);
     */
    private Map<DecodeHintType, ?> decodeHints;

    /**
     * 【辅助解码的参数(用作MultiFormatReader的参数)】 字符集，告诉扫描器该以何种字符集进行解码
     * 对应于DecodeHintType.CHARACTER_SET类型
     * 参考DecodeThread构造器如下代码：hints.put(DecodeHintType.CHARACTER_SET,
     * characterSet);
     */
    private String characterSet;

    private Result savedResultToShow;

    private IntentSource source;

    //是否来自相册
    private static int swipNum = 0;

    /**
     * 图片的路径
     */
    private String photoPath;

    private Handler mHandler = new MyHandler(this);
    private ImageView light;
    private ViewfinderView qr_codeswipe;

     class MyHandler extends Handler {

        private WeakReference<Activity> activityReference;

        public MyHandler(Activity activity) {
            activityReference = new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case PARSE_BARCODE_SUC: // 解析图片成功
//                    new MToast(activityReference.get(),
//                            "解析成功，结果为：" + msg.obj, Toast.LENGTH_SHORT).show();result
                    String result = msg.obj.toString();
 ;

                case PARSE_BARCODE_FAIL:// 解析图片失败
                    Toast.makeText(activityReference.get(), "解析图片失败", Toast.LENGTH_SHORT).show();
                    restartPreviewAfterDelay(0L);
                    break;

                default:
                    break;
            }

            super.handleMessage(msg);
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.capture);

        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
        beepManager = new BeepManager(this);
        ambientLightManager = new AmbientLightManager(this);
        nickname = getIntent().getStringExtra("nickname");
        phoneNum = getIntent().getStringExtra("phoneNum");

        qr_codeswipe = (ViewfinderView) findViewById(R.id.capture_viewfinder_view);

        // 监听图片识别按钮
        findViewById(R.id.capture_scan_photo).setOnClickListener(this);
        findViewById(R.id.qr_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qr_codeswipe.closeCamera();
                CaptureActivity.this.finish();
            }
        });

        light = (ImageView) findViewById(R.id.capture_flashlight);
        light.setOnClickListener(this);

//        EventBus.getDefault().register(this);

    }

    @Override
    protected void onResume() {
        super.onResume();

        // CameraManager must be initialized here, not in onCreate(). This is
        // necessary because we don't
        // want to open the camera driver and measure the screen size if we're
        // going to show the help on
        // first launch. That led to bugs where the scanning rectangle was the
        // wrong size and partially
        // off screen.

        // 相机初始化的动作需要开启相机并测量屏幕大小，这些操作
        // 不建议放到onCreate中，因为如果在onCreate中加上首次启动展示帮助信息的代码的 话，
        // 会导致扫描窗口的尺寸计算有误的bug
        cameraManager = new CameraManager(getApplication());

        viewfinderView = (ViewfinderView) findViewById(R.id.capture_viewfinder_view);
        viewfinderView.setCameraManager(cameraManager);

        handler = null;
        lastResult = null;

        // 摄像头预览功能必须借助SurfaceView，因此也需要在一开始对其进行初始化
        // 如果需要了解SurfaceView的原理
        // 参考:http://blog.csdn.net/luoshengyang/article/details/8661317
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.capture_preview_view); // 预览
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            // The activity was paused but not stopped, so the surface still
            // exists. Therefore
            // surfaceCreated() won't be called, so init the camera here.
            initCamera(surfaceHolder);

        } else {
            // 防止sdk8的设备初始化预览异常
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

            // Install the callback and wait for surfaceCreated() to init the
            // camera.
            surfaceHolder.addCallback(this);
        }

        // 加载声音配置，其实在BeemManager的构造器中也会调用该方法，即在onCreate的时候会调用一次
        beepManager.updatePrefs();

        // 启动闪光灯调节器
        ambientLightManager.start(cameraManager);

        // 恢复活动监控器
        inactivityTimer.onResume();

        source = IntentSource.NONE;
        decodeFormats = null;
        characterSet = null;
    }

    @Override
    protected void onPause() {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        inactivityTimer.onPause();
        ambientLightManager.stop();
        beepManager.close();

        // 关闭摄像头
        cameraManager.closeDriver();
        if (!hasSurface) {
            SurfaceView surfaceView = (SurfaceView) findViewById(R.id.capture_preview_view);
            SurfaceHolder surfaceHolder = surfaceView.getHolder();
            surfaceHolder.removeCallback(this);
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
//        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if ((source == IntentSource.NONE) && lastResult != null) { // 重新进行扫描
                    restartPreviewAfterDelay(0L);
                    return true;
                }
                break;
            case KeyEvent.KEYCODE_FOCUS:
            case KeyEvent.KEYCODE_CAMERA:
                // Handle these events so they don't launch the Camera app
                return true;

            case KeyEvent.KEYCODE_VOLUME_UP:
                cameraManager.zoomIn();
                return true;

            case KeyEvent.KEYCODE_VOLUME_DOWN:
                cameraManager.zoomOut();
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == shuhui_result) {
            if (resultCode == RESULT_OK) {
                //TODO twocode
//                List<String> mSelectPath = intent.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
//                if (mSelectPath != null && mSelectPath.size() > 0) {
//                    Intent intent1 = new Intent(this, ClipQrCodeActivity.class);
//                    intent1.putExtra("mSelectPath", mSelectPath.get(0));
//                    startActivity(intent1);
//                }
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (holder == null) {
            Log.e(TAG, "*** WARNING *** surfaceCreated() gave us a null surface!");
        }
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        /*hasSurface = false;*/
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    /**
     * A valid barcode has been found, so give an indication of success and show
     * the results.
     *
     * @param rawResult   The contents of the barcode.
     * @param scaleFactor amount by which thumbnail was scaled
     * @param barcode     A greyscale bitmap of the camera data which was decoded.
     */
    public void handleDecode(Result rawResult, Bitmap barcode, float scaleFactor) {
        // 重新计时
        inactivityTimer.onActivity();
        lastResult = rawResult;
        // 把图片画到扫描框
        viewfinderView.drawResultBitmap(barcode);
        beepManager.playBeepSoundAndVibrate();
//        new MToast(this).show("识别结果:" + ResultParser.parseResult(rawResult).toString(),0);
        String result = ResultParser.parseResult(rawResult).toString();
        parser(result);
    }

    public void parser(String result) {
        if (result.contains(Config.qr_codeUrl)) {
            try {
                String aa = result.substring(result.indexOf("?") + 1);
                String deresult = Des3.decode(result.substring(result.indexOf("?") + 1));
//                new MToast(this).show(deresult,0);
                JSONObject j = new JSONObject(deresult);
                String party_id = j.getString("party_id");
                String party_name = j.getString("party_name");
                String father_id = j.getString("uid");
                String deadline = j.getString("deadline");
                String father_name = j.getString("nickName");
//                adviserId='+advisorId+'&bindChannel=4
//              String.valueOf(CwebNetConfig.qrcoderesult+"advisorId=%s+&bindChannel=")
                if (deadline.equals(DataUtils.getDay1()) || (DataUtils.compareNow(deadline) != -1)) {
                    if (!AppManager.isInvestor(this)) {
                        //upload(party_id, party_name, father_id, father_name);
                    } else {

                        RxBus.get().post("twocode_look_observable", new QrCodeBean(party_id, party_name, father_id, deadline, father_name));
                    }
                } else {
                    Toast.makeText(this, "该二维码已过期", Toast.LENGTH_SHORT).show();
                    restartPreviewAfterDelay(0L);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (result.contains(CwebNetConfig.baseParentUrl)) {
            String isCallBack = getIntent().getStringExtra("isCallBack");
            if ((!TextUtils.isEmpty(isCallBack)) && isCallBack.equals("Y")) {
                RxBus.get().post(RxConstant.SWIPT_CODE_RESULT,result);

                this.finish();
            } else {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put(WebViewConstant.push_message_title, "投资-与未来对话");
                hashMap.put(WebViewConstant.push_message_url, result);
                NavigationUtils.startActivityByRouter(this, RouteConfig.GOTO_BASE_WEBVIEW, hashMap);
                this.finish();
            }
        } else {
            Toast.makeText(this, "不是有效的推荐人二维码", Toast.LENGTH_SHORT).show();
            restartPreviewAfterDelay(0L);
        }
        finish();
    }

//    public void parser(String result) {
//        if (result.contains(Config.qr_codeUrl)) {
//            try {
//                String aa = result.substring(result.indexOf("?") + 1);
//                String deresult = Des3.decode(result.substring(result.indexOf("?") + 1));
//                JSONObject j = new JSONObject(deresult);
//                String party_id = j.getString("party_id");
//                String party_name = j.getString("party_name");
//                String father_id = j.getString("uid");
//                String deadline = j.getString("deadline");
//                String father_name = j.getString("nickName");
//                if (deadline.equals(DataUtils.getDay()) || (DataUtils.compareNow(deadline) != -1)) {
//                    RxBus.get().post("twocode_look_observable", new QrCodeBean(party_id, party_name, father_id, deadline, father_name));
////                    if (!SPreference.isVisitorRole(getApplicationContext())) {
////                        upload(party_id, party_name, father_id, father_name);
////                    } else {
////                        // toJumpTouziren(result);
////                    }
//                } else {
//                    Toast.makeText(this, "该二维码已过期", Toast.LENGTH_SHORT).show();
//                    restartPreviewAfterDelay(0L);
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } else {
//            Toast.makeText(this, "不是有效的推荐人二维码", Toast.LENGTH_SHORT).show();
//            restartPreviewAfterDelay(0L);
//        }
//    }
//    private void toJumpTouziren(String result) {
//        try {
//            String deresult = Des3.decode(result.substring(result.indexOf("?") + 1));
//            JSONObject j = new JSONObject(deresult);
//            String father_id = j.getString("uid");
//            Intent intent = new Intent(this, InvestorCentifyActivity.class);
//            intent.putExtra(InvestorCentifyActivity.INVISTOR_PARAM, father_id);
//            startActivity(intent);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    private static void upload(String party_id, final String party_name, String father_id, final String father_name) {
//        JSONObject j = new JSONObject();
//        String name = nickname;
//        try {
//            j.put("advisersId", Mapp);
//            if ((!TextUtils.isEmpty(name)) && (!name.equals(""))) {
//                j.put("realName", name);
//            }
//            j.put("orgId", party_id);
//            if ((!TextUtils.isEmpty(phoneNum)) && (!phoneNum.equals(""))) {
//                j.put("phoneNum", phoneNum);
//            }
//            j.put("fatherId", father_id);
//            j.put("authenticationType", "1");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        new RenzhengLicaishiTask(BaseApplication.getContext()).start(j.toString(), new HttpResponseListener() {
//            @Override
//            public void onResponse(JSONObject response) {
////                new MyDogDialog(CaptureActivity.this, "", "成功认证到" + party_name + ",邀请人" + father_name, "", "确定") {
////                    @Override
////                    public void left() {
////                        restartPreviewAfterDelay(0L);
////                        EventBus.getDefault().post(new RefreshUserinfo());
////                    }
////
////                    @Override
////                    public void right() {
////                        restartPreviewAfterDelay(0L);
////                        EventBus.getDefault().post(new RefreshUserinfo());
////                    }
////                }.show();
//                new MToast(MApplication.mContext).show("成功认证到" + party_name + ",邀请人" + father_name, Toast.LENGTH_LONG);
//                restartPreviewAfterDelay(0L);
//                EventBus.getDefault().post(new RefreshUserinfo());
//            }

//            @Override
//            public void onErrorResponse(String error, int statueCode) {
//                try {
//                    JSONObject j = new JSONObject(error);
//                    String msg = j.getString("message");
//                    new MToast(MApplication.mContext).show(msg, 0);
//                    restartPreviewAfterDelay(0L);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }

    public static void restartPreviewAfterDelay(long delayMS) {
        if (handler != null) {
            handler.sendEmptyMessageDelayed(R.id.restart_preview, delayMS);
        }
        resetStatusView();
    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }

    private static void resetStatusView() {
        viewfinderView.setVisibility(View.VISIBLE);
        lastResult = null;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }

        if (cameraManager.isOpen()) {
            Log.w(TAG,
                    "initCamera() while already open -- late SurfaceView callback?");
            return;
        }
        try {
            cameraManager.openDriver(surfaceHolder);
            // Creating the handler starts the preview, which can also throw a
            // RuntimeException.
            if (handler == null) {
                handler = new CaptureActivityHandler(this, decodeFormats,
                        decodeHints, characterSet, cameraManager);
            }
            decodeOrStoreSavedBitmap(null, null);
        } catch (IOException ioe) {
            Log.w(TAG, ioe);
            displayFrameworkBugMessageAndExit();
        } catch (RuntimeException e) {
            // Barcode Scanner has seen crashes in the wild of this variety:
            // java.?lang.?RuntimeException: Fail to connect to camera service
            Log.w(TAG, "Unexpected error initializing camera", e);
            displayFrameworkBugMessageAndExit();
        }
    }

    /**
     * 向CaptureActivityHandler中发送消息，并展示扫描到的图像
     *
     * @param bitmap
     * @param result
     */
    private void decodeOrStoreSavedBitmap(Bitmap bitmap, Result result) {
        // Bitmap isn't used yet -- will be used soon
        if (handler == null) {
            savedResultToShow = result;
        } else {
            if (result != null) {
                savedResultToShow = result;
            }
            if (savedResultToShow != null) {
                Message message = Message.obtain(handler,
                        R.id.decode_succeeded, savedResultToShow);
                handler.sendMessage(message);
            }
            savedResultToShow = null;
        }
    }

    private void displayFrameworkBugMessageAndExit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.app_name));
        builder.setMessage(getString(R.string.msg_camera_framework_bug));
        builder.setPositiveButton(R.string.button_ok, new FinishListener(this));
        builder.setOnCancelListener(new FinishListener(this));
        builder.show();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.capture_scan_photo) {
                Intent innerIntent = new Intent(Intent.ACTION_GET_CONTENT); // "android.intent.action.GET_CONTENT"
                innerIntent.setType("image/*");
                Intent wrapperIntent = Intent.createChooser(innerIntent,
                        "选择二维码图片");
                this.startActivityForResult(wrapperIntent, REQUEST_CODE);

//            // TODO 图片选择
//                Intent ii = new Intent(this, MultiImageSelectorActivity.class);
//                // 是否显示拍摄图片
//                ii.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
//                // 最大可选择图片数量
//                ii.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 1);
//                ii.putExtra("clear", 1);
//                // 选择模式
//                ii.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_SINGLE);
//                // 设置是否可预览
//                ii.putExtra(MultiImageSelectorActivity.PREVIEW_MODE, 0);
//                startActivityForResult(ii, Contant.shuhui_result);
//                if (Utils.isVisteRole(this)) {
//                    DataStatistApiParam.lookToLookFromXiangCe();
//                }
        } else if (v.getId() == R.id.capture_flashlight) {
            if (isFlashlightOpen) {
                cameraManager.setTorch(false); // 关闭闪光灯
                isFlashlightOpen = false;
                light.setBackgroundResource(R.drawable.qrcode_scan_btn_flash_nor);
            } else {
                cameraManager.setTorch(true); // 打开闪光灯
                isFlashlightOpen = true;
                light.setBackgroundResource(R.drawable.qrcode_scan_btn_flash_down);
            }
        }
//        switch (v.getId()) {
//            case R.id.capture_scan_photo: // 图片识别
                // 打开手机中的相册
//                Intent innerIntent = new Intent(Intent.ACTION_GET_CONTENT); // "android.intent.action.GET_CONTENT"
//                innerIntent.setType("image/*");
//                Intent wrapperIntent = Intent.createChooser(innerIntent,
//                        "选择二维码图片");
//                this.startActivityForResult(wrapperIntent, REQUEST_CODE);

                // TODO 图片选择
//                Intent ii = new Intent(this, MultiImageSelectorActivity.class);
//                // 是否显示拍摄图片
//                ii.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
//                // 最大可选择图片数量
//                ii.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 1);
//                ii.putExtra("clear", 1);
//                // 选择模式
//                ii.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_SINGLE);
//                // 设置是否可预览
//                ii.putExtra(MultiImageSelectorActivity.PREVIEW_MODE, 0);
//                startActivityForResult(ii, Contant.shuhui_result);
//                if (Utils.isVisteRole(this)) {
//                    DataStatistApiParam.lookToLookFromXiangCe();
//                }
//                break;
//            break;
//            case R.id.capture_flashlight:
//                if (isFlashlightOpen) {
//                    cameraManager.setTorch(false); // 关闭闪光灯
//                    isFlashlightOpen = false;
//                    light.setBackgroundResource(R.drawable.qrcode_scan_btn_flash_nor);
//                } else {
//                    cameraManager.setTorch(true); // 打开闪光灯
//                    isFlashlightOpen = true;
//                    light.setBackgroundResource(R.drawable.qrcode_scan_btn_flash_down);
//                }
//                break;
//            default:
//                break;
//        }
    }

    public void onEventMainThread(EventBusQRcodeImg event) {
        final String fileDir = event.getFileDir();
        if (fileDir != null && fileDir.length() != 0) {
            ReadQrfromAlbum(fileDir);
        }
    }

    public void ReadQrfromAlbum(final String fileDir) {
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在扫描...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                boolean isSucess = false;
                while (true) {
                    i++;
                    Log.e("runrun","aaa"+i);
                    if (i < 10) {
                        if (parserQrImage(fileDir)) {
                            isSucess = true;
                            break;
                        }
                    }else{
                        break;
                    }
                }
                if (!isSucess) {
                    Message m = mHandler.obtainMessage();
                    m.what = PARSE_BARCODE_FAIL;
                    mHandler.sendMessage(m);
                }
                progressDialog.dismiss();
            }
        }).start();

    }

    private boolean parserQrImage(String fileDir) {
        String filepath = fileDir;//Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "simuyun" + File.pathSeparator + "Image" + File.separator + "qrcode.png";
        Bitmap img = BitmapUtils.getCompressedBitmap(filepath);
        BitmapDecoder decoder = new BitmapDecoder(
                CaptureActivity.this);
        Result result = decoder.getRawResult(img);

        if (result != null) {
            Message m = mHandler.obtainMessage();
            m.what = PARSE_BARCODE_SUC;
            m.obj = ResultParser.parseResult(result)
                    .toString();
            mHandler.sendMessage(m);
            return true;
        } else {
            return false;
        }
    }
}
