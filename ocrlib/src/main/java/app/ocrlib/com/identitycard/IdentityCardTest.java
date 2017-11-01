package app.ocrlib.com.identitycard;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import app.ocrlib.com.R;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/10/31-14:51
 */
public class IdentityCardTest extends AppCompatActivity {
    ImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.identity_test);
        inview();
    }

    public void inview( ) {
        String path = Environment.getExternalStorageDirectory().getPath() + "/tesst.jpg";
        imageView = (ImageView) findViewById(R.id.iid);
        imageView.setImageBitmap(BitmapFactory.decodeFile(path));
    }


}
