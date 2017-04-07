package app.ndk.com.enter.mvp.ui.Load;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;

import com.cgbsoft.lib.AppInfStore;

import app.ndk.com.enter.R;

/**
 * desc  B端的启动页 需要进行文件配置
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 17/4/7-17:04
 */
public class LoadAdviserActivity extends BaseActivity {
    @Override
    protected void configApp() {
        AppInfStore.Save_IsAdviser(baseActivity,true);

    }

    @Override
    protected void initView(Bundle state) {
        setContentView(R.layout.activity_adviser_loadadviser);
    }

}
