package app.ndk.com.enter.test;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.cgbsoft.lib.base.webview.BaseWebview;
import com.cgbsoft.lib.base.webview.CwebNetConfig;

import app.ndk.com.enter.R;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/5/4-18:43
 */
public class TestWebview extends Activity {
    private BaseWebview BaseWebview;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testweb);
        BaseWebview= (com.cgbsoft.lib.base.webview.BaseWebview) findViewById(R.id.testweb);
        BaseWebview.loadUrls(CwebNetConfig.minePgge);
        BaseWebview.loadUrl("javascript:refresh()");
        BaseWebview.loadUrl("javascript:message('" + 10 + "')");
    }
}
