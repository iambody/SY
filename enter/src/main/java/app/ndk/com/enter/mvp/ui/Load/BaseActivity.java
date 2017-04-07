package app.ndk.com.enter.mvp.ui.Load;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * desc  B&C的区分启动activity的基类
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 17/4/7-16:59
 */
public abstract class BaseActivity extends AppCompatActivity {
    /**
     * 配置B&C
     **/
    protected abstract void configApp();

    /**
     * 初始化a
     **/
    protected abstract void initView(Bundle state);

    /**
     * 公用的上下文
     **/
    protected Activity baseActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        baseActivity = BaseActivity.this;
        initView(savedInstanceState);
        configApp();
    }
}
