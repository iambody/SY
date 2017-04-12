package app.ndk.com.enter.mvp.ui.Load;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;

import com.cgbsoft.lib.AppInfStore;
import com.cgbsoft.lib.utils.tools.UiSkipUtils;

import app.ndk.com.enter.R;
import app.ndk.com.enter.mvp.ui.start.WelcomeActivity;

/**
 * desc  B端的启动页 需要进行文件配置
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 17/4/7-17:04
 */
public class LoadAdviserActivity extends BaseActivity {
    @Override
    protected void configApp() {
        baseActivity.finish();
        AppInfStore.Save_IsAdviser(baseActivity, true);
        UiSkipUtils.toNextActivity(baseActivity, WelcomeActivity.class);

    }

    @Override
    protected void initView(Bundle state) {
//        setContentView(R.layout.activity_adviser_loadadviser);

    }

}
