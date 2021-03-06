package app.ocrlib.com.identitycard;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cgbsoft.lib.utils.constant.Constant;
import com.cgbsoft.lib.utils.constant.RxConstant;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxBus;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.tools.CameraUtils;
import com.cgbsoft.lib.utils.tools.DimensionPixelUtil;
import com.cgbsoft.lib.utils.tools.DownloadUtils;
import com.cgbsoft.lib.utils.tools.PromptManager;
import com.cgbsoft.lib.widget.dialog.LoadingDialog;
import com.cgbsoft.privatefund.bean.living.IdentityCard;

import app.ocrlib.com.R;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * desc  ${ }
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/10/31-13:49
 */
public class IdentityCardActivity extends AppCompatActivity implements View.OnClickListener, ClipCamera.IAutoFocus {
    //自定义的横屏相机
    private ClipCamera clipCamera;
    //拍照按钮
    private Button btn_shoot;
    //提示文本
    private TextView identitycard_note;
    //扫描
//    private ImageView identitycard_scan_iv;

    //背影
    private IndentityCardShadow identitycard_shadow;
    //正面的头像
    private ImageView ocr_face_iv;
    public static final String CARD_FACE = "cardface";
    private int currentFace;
    //身份证正面
    public static final int FACE_FRONT = 0;
    //身份证反面
    public static final int FACE_BACK = 1;
    //相机对焦
//    private Camera.AutoFocusCallback mAutoFocusCallback;
    //加载
    private LoadingDialog mLoadingDialog;

    private boolean isCanClick = true;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (null != getIntent().getExtras() && getIntent().getExtras().containsKey(CARD_FACE)) {
            currentFace = getIntent().getIntExtra(CARD_FACE, -1);
        } else {
            PromptManager.ShowCustomToast(this, getResources().getString(R.string.put_parame));
            finish();
        }

        mLoadingDialog = LoadingDialog.getLoadingDialog(this, " ", false, false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (CameraUtils.getCameraPermission(this)) {
                setContentView(R.layout.activity_identitycard);
                initView();
            } else {
                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 22);
            }
        } else {
            setContentView(R.layout.activity_identitycard);
            initView();
        }

    }

    private void initView() {
//        PromptManager.ShowCustomToast(IdentityCardActivity.this,"请对齐脸部轮廓，点击拍照按钮");
        clipCamera = (ClipCamera) findViewById(R.id.surface_view);
        btn_shoot = (Button) findViewById(R.id.btn_shoot);
        ocr_face_iv = (ImageView) findViewById(R.id.ocr_face_iv);
        identitycard_note = (TextView) findViewById(R.id.identitycard_note);
//        identitycard_scan_iv = (ImageView) findViewById(R.id.identitycard_scan_iv);
        identitycard_shadow = (IndentityCardShadow) findViewById(R.id.identitycard_shadow);
        clipCamera.setIAutoFocus(this);
        btn_shoot.setOnClickListener(this);
        setIconPostion(currentFace);

    }

    /**
     * @param type 1标识身份证头像的一面 2标识身份证国徽的一面
     */
    private void setIconPostion(int type) {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        RelativeLayout.LayoutParams iConParams = new RelativeLayout.LayoutParams(DimensionPixelUtil.dp2px(this, 180), DimensionPixelUtil.dp2px(this, 140));
        int height = (int) (screenWidth * 0.8);//拍照的阴影框的高度为屏幕宽度的80%  0.8
        int width = (int) (height * 1.6);//身份证宽高比例为1.6
        switch (type) {
            case FACE_FRONT:
                iConParams.setMargins((height / 2) - DimensionPixelUtil.dp2px(this, 40), width - DimensionPixelUtil.dp2px(this, 100), 0, 0);
                ocr_face_iv.setLayoutParams(iConParams);
                ocr_face_iv.setImageResource(R.drawable.ocr_face_blue);
                identitycard_note.setText(getResources().getString(R.string.put_identitycard_front));
                break;
            case FACE_BACK:
                iConParams = new RelativeLayout.LayoutParams(DimensionPixelUtil.dp2px(this, 110), DimensionPixelUtil.dp2px(this, 110));

//                iConParams.setMargins((height * 3 / 5) + DimensionPixelUtil.dp2px(this, 16), screenHeight - width - DimensionPixelUtil.dp2px(this, 70), 0, 0);
                iConParams.setMargins((height * 3 / 5) + DimensionPixelUtil.dp2px(this, 16), screenHeight - width - (screenHeight - width) * 13 / 40, 0, 0);

                ocr_face_iv.setLayoutParams(iConParams);
                ocr_face_iv.setImageResource(R.drawable.ocr_nation_blue);
                identitycard_note.setText(getResources().getString(R.string.put_identitycard_back));
                identitycard_note.setVisibility(View.GONE);
                //身份证反面
                break;
        }

        //设置扫描的parms
////        iConParams.setMargins((height / 2) - DimensionPixelUtil.dp2px(this, 40), width - DimensionPixelUtil.dp2px(this, 100), 0, 0);
//        RelativeLayout.LayoutParams sCanConParams = new RelativeLayout.LayoutParams(height, RelativeLayout.LayoutParams.WRAP_CONTENT);
//
//        identitycard_scan_iv.setLayoutParams(sCanConParams);
//        AnimUtils.IdentityCard((screenHeight - width) / 2 - DimensionPixelUtil.dip2px(this, 20), width + DimensionPixelUtil.dip2px(this, 20), identitycard_scan_iv);

    }

    public void takePhoto() {
        if (!isCanClick) return;
        isCanClick = false;
        String path = Environment.getExternalStorageDirectory().getPath() + "/" + System.currentTimeMillis() + "identity.jpg";
        clipCamera.takePicture(path, new ClipCamera.CameraResult() {
            @Override
            public void picResult(String ivPath) {
                Log.i("OCR回调", "活体回调结果成功" + ivPath);

                analyzeCard(ivPath);
            }

            @Override
            public void picFailed() {
                Log.i("OCR回调", "活体回调结果失败");
            }
        });
    }

    /**
     * 开始解析card 先上传图片到网络获取
     *
     * @param ivPath
     */
    private void analyzeCard(final String ivPath) {
        mLoadingDialog.setLoading("");
        mLoadingDialog.show();

        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                //异步操作相关代码
                String imageId = DownloadUtils.postSecretObject(ivPath, Constant.UPLOAD_COMPLIANCE_OCR);
                subscriber.onNext(imageId);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(final String data) {
                        // 主线程操作获取了远程的url
                        Log.i("OCR回调", "远程地址" + data);
                        ApiClient.getOcrResult(data, currentFace + 1).subscribe(new RxSubscriber<IdentityCard>() {
                            @Override
                            protected void onEvent(IdentityCard identityCard) {
                                isCanClick = true;
                                if (null != mLoadingDialog) {
                                    mLoadingDialog.dismiss();
                                }
                                if (null != identityCard && "0".equals(identityCard.getAnalysisType())) {
                                    PromptManager.ShowCustomToast(IdentityCardActivity.this, "拍摄照片非身份证或者拍摄范围有误，请重新拍摄");
                                }
                                identityCard.setLocalPath(ivPath);
                                identityCard.setRemotPath(data);
                                RxBus.get().post(currentFace == FACE_FRONT ? RxConstant.COMPLIANCE_CARD_FRONT : RxConstant.COMPLIANCE_CARD_BACK, identityCard);
                                IdentityCardActivity.this.finish();
                                Log.i("OCR回调", "信息成功" + identityCard.toString());

                            }

                            @Override
                            protected void onRxError(Throwable error) {
                                isCanClick = true;
                                if (null != mLoadingDialog) {
                                    mLoadingDialog.dismiss();

                                }
                                PromptManager.ShowCustomToast(IdentityCardActivity.this, "拍摄照片非身份证或者拍摄范围有误，请重新拍摄");
                                IdentityCard identityCard = new IdentityCard();
                                identityCard.setAnalysisType("0");
                                identityCard.setLocalPath(ivPath);
                                identityCard.setRemotPath(data);
                                identityCard.setAnalysisMsg(error.getMessage());
                                RxBus.get().post(currentFace == FACE_FRONT ? RxConstant.COMPLIANCE_CARD_FRONT : RxConstant.COMPLIANCE_CARD_BACK, identityCard);
                                IdentityCardActivity.this.finish();
                                Log.i("OCR回调", "信息失败" + error.getMessage());
                            }
                        });
                    }
                });
    }

    @Override
    public void autoFocus() {
        clipCamera.setAutoFocus();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 22) {
            if (CameraUtils.getCameraPermission(IdentityCardActivity.this)) {
                setContentView(R.layout.activity_identitycard);
                initView();
            } else {
                IdentityCardActivity.this.finish();
                PromptManager.ShowCustomToast(IdentityCardActivity.this, "请去系统设置开启权限");

            }
//            for (int i = 0; i < permissions.length; i++) {
//                String s = permissions[i];
//                if (s.equals(Manifest.permission.CAMERA) && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
//                    setContentView(R.layout.activity_identitycard);
//                    initView();
//                }
//            }


        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mLoadingDialog) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }

        try {
            clipCamera.closeCamera();
        } catch (Exception e) {
        }
    }

    @Override
    public void onClick(View v) {
        if (R.id.btn_shoot == v.getId()) {//拍照
            takePhoto();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (null != mLoadingDialog) {
                mLoadingDialog.dismiss();
                mLoadingDialog = null;
            }
            try {
                clipCamera.closeCamera();
            } catch (Exception e) {

            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
