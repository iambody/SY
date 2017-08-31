package app.ndk.com.enter.mvp.ui.Load;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.cgbsoft.lib.AppInfStore;
import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.base.webview.WebViewConstant;
import com.cgbsoft.lib.contant.Contant;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.tools.UiSkipUtils;
import com.chenenyu.router.annotation.Route;

import app.ndk.com.enter.R;
import app.ndk.com.enter.mvp.ui.start.WelcomeActivity;
import app.privatefund.com.im.utils.RongConnect;
import io.rong.push.notification.PushNotificationMessage;

/**
 * desc  C端的启动页
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 17/4/7-17:03
 */
@Route(RouteConfig.GOTO_FIRST_ACTIVITY)
public class LoadCustomerActivity extends BaseActivity {

    @Override
    protected void configApp() {
        //需要在mainfeer 添加metdat数据 进行确保！！！！！（清除数据）！！！！！！
        SPreference.putString(this, Contant.CUR_LIVE_ROOM_NUM, "");
        AppInfStore.saveAdvise(baseActivity, false);
        if (AppManager.getIsLogin(getApplicationContext())) {
            RongConnect.initRongTokenConnect(AppManager.getUserId(getApplicationContext()));

//            Router.build(RouteConfig.GOTOCMAINHONE).go(LoadCustomerActivity.this);
//            UiSkipUtils.toNextActivity(baseActivity, WelcomeActivity.class);
            Intent intent = new Intent(this, WelcomeActivity.class);
            intent.putExtra("isloade", true);
            isRongMessagePush(intent);
            UiSkipUtils.toNextActivityWithIntent(this, intent);
            baseActivity.finish();
            return;
        }
        UiSkipUtils.toNextActivity(baseActivity, WelcomeActivity.class);
        baseActivity.finish();
    }

    private void isRongMessagePush(Intent intent) {
        PushNotificationMessage pushMessage = getIntent().getParcelableExtra(WebViewConstant.PUSH_MESSAGE_OBJECT_NAME);
        Uri uri = getIntent().getParcelableExtra(WebViewConstant.PUSH_MESSAGE_RONGYUN_URL_NAME);
        if (pushMessage != null && uri != null) {
            intent.putExtra(WebViewConstant.PUSH_MESSAGE_OBJECT_NAME, pushMessage);
            intent.putExtra(WebViewConstant.PUSH_MESSAGE_RONGYUN_URL_NAME, uri);
        }
    }

    @Override
    protected void initView(Bundle state) {
        setContentView(R.layout.activity_customer_loadcustomer);
    }
}
