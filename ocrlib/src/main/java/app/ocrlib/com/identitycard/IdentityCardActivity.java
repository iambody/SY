package app.ocrlib.com.identitycard;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cgbsoft.lib.utils.tools.PromptManager;

import app.ocrlib.com.R;

/**
 * desc  ${DESC}
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
    //正面的头像
    private ImageView ocr_face_iv;
    public static final String CARD_FACE = "cardface";
    private int currentFace;
    //身份证正面
    public static final int FACE_FRONT = 1;
    //身份证反面
    public static final int FACE_BACK = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (null != getIntent().getExtras() && getIntent().getExtras().containsKey(CARD_FACE)) {
            currentFace = getIntent().getIntExtra(CARD_FACE, -1);
        } else {
            PromptManager.ShowCustomToast(this, getResources().getString(R.string.put_parame));
            finish();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 22);
        } else {
            setContentView(R.layout.activity_identitycard);
            initView();
        }

    }

    private void initView() {
        clipCamera = (ClipCamera) findViewById(R.id.surface_view);
        btn_shoot = (Button) findViewById(R.id.btn_shoot);
        ocr_face_iv = (ImageView) findViewById(R.id.ocr_face_iv);
        identitycard_note = (TextView) findViewById(R.id.identitycard_note);
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
        RelativeLayout.LayoutParams iConParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//        int height = (int) (screenWidth * 0.8);//拍照的阴影框的高度为屏幕宽度的80%  0.8
//        int width = (int) (height * 1.6);//身份证宽高比例为1.6
        switch (type) {
            case FACE_FRONT:
                ocr_face_iv.setImageResource(R.drawable.ocr_face_blue);
                identitycard_note.setText(getResources().getString(R.string.put_identitycard_front));
                break;
            case FACE_BACK:
                ocr_face_iv.setImageResource(R.drawable.ocr_nation_blue);
                identitycard_note.setText(getResources().getString(R.string.put_identitycard_back));
                //身份证反面
                break;
        }

    }

    public void takePhoto() {
        String path = Environment.getExternalStorageDirectory().getPath() + "/test.jpg";
        clipCamera.takePicture(path, new ClipCamera.CameraResult() {
            @Override
            public void picResult(String ivPath) {
                
            }

            @Override
            public void picFailed() {

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
            for (int i = 0; i < permissions.length; i++) {
                String s = permissions[i];
                if (s.equals(Manifest.permission.CAMERA) && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    setContentView(R.layout.activity_identitycard);
                    initView();
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (R.id.btn_shoot == v.getId()) {//拍照
            takePhoto();
        }
    }


}
