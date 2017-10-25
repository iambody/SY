package app.ocrlib.com.cardphoto;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.cgbsoft.lib.utils.constant.Constant;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSchedulersHelper;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.tools.DownloadUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import app.ocrlib.com.R;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/10/20-10:20
 */
public class TestCardCamera extends AppCompatActivity {
    Button getimag, getimag1, getimag3;
    ImageView ssss, ssss1;
    boolean second;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testcardcamera);
        getimag = (Button) findViewById(R.id.getimag);
        ssss = (ImageView) findViewById(R.id.ssss);
        getimag1 = (Button) findViewById(R.id.getimag1);
        ssss1 = (ImageView) findViewById(R.id.ssss1);
        getimag3 = (Button) findViewById(R.id.getimag3);

        getimag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CameraUtil.getInstance().startCamera(TestCardCamera.this, 100);
            }
        });
        getimag1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CameraUtil.getInstance().startCamera(TestCardCamera.this, 101);
            }
        });
        getimag3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send();

            }
        });
    }

    List<String> urlssss = new ArrayList<>();
    private List<String> NetUrl = new ArrayList<>();

    private void send() {
        Log.i("oooppllljjo", "开始");
        Observable.from(urlssss).map(new Func1<String, String>() {
            @Override
            public String call(String s) {

                String newTargetFile = com.cgbsoft.lib.utils.dm.Utils.helper.FileUtils.compressFileToUpload(s, false);

                String imageId = DownloadUtils.postObject(s, Constant.UPLOAD_USERICONNEWC_TYPE);
                com.cgbsoft.lib.utils.dm.Utils.helper.FileUtils.deleteFile(newTargetFile);
                return imageId;


            }
        }).compose(RxSchedulersHelper.getTransformer()).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                NetUrl.add(s);
                Log.i("oooppllljjo", s);

            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (requestCode == 100) {
            String img_path = data.getStringExtra(CameraConstants.IntentKeyFilePath);
//            updateImageView(img_path);
            Bitmap bitemapFromFile = CameraBitmapUtils.getBitemapFromFile(new File(img_path));
            ssss.setImageBitmap(bitemapFromFile);
            urlssss.add(img_path);
        }
        if (requestCode == 101) {
            String img_path1 = data.getStringExtra(CameraConstants.IntentKeyFilePath);
//            updateImageView(img_path);
            Bitmap bitemapFromFile = CameraBitmapUtils.getBitemapFromFile(new File(img_path1));
            ssss1.setImageBitmap(bitemapFromFile);
            urlssss.add(img_path1);
        }
    }

    private void updateImageView(String img_path) {
        Bitmap bitemapFromFile = CameraBitmapUtils.getBitemapFromFile(new File(img_path));
        ssss.setImageBitmap(bitemapFromFile);


    }

    /**
     * 开始进行ocr
     */
    private void cardOcr() {

        ApiClient.getOcrResult(1, "").subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {

            }

            @Override
            protected void onRxError(Throwable error) {

            }
        });
    }
}
