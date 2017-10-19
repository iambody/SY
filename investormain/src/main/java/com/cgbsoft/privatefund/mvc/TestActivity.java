package com.cgbsoft.privatefund.mvc;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.cgbsoft.privatefund.R;

import java.io.File;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/10/18-11:31
 */
public class TestActivity extends Activity {
    ImageView kk;
    String filepathpath = "/data/user/0/com.cgbsoft.privatefund/files/ocr/1.jpg";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testactivity);
        kk = (ImageView) findViewById(R.id.kk);
        File file = new File(filepathpath);
        if (file.exists()) {
            Bitmap bm = BitmapFactory.decodeFile(filepathpath);
            //将图片显示到ImageView中
            kk.setImageBitmap(bm);
        }
    }
}
