package app.ndk.com.enter.mvp.ui.Load;

import android.os.Bundle;

import com.cgbsoft.lib.AppInfStore;
import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.listener.listener.GestureManager;
import com.cgbsoft.lib.utils.tools.UiSkipUtils;
import com.chenenyu.router.Router;

import app.ndk.com.enter.R;
import app.ndk.com.enter.mvp.ui.start.WelcomeActivity;
import app.privatefund.com.im.utils.RongConnect;

/**
 * desc  C端的启动页
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 17/4/7-17:03
 */
public class LoadCustomerActivity extends BaseActivity {

    @Override
    protected void configApp() {
        //需要在mainfeer 添加metdat数据 进行确保！！！！！（清除数据）！！！！！！
        AppInfStore.saveAdvise(baseActivity, false);
        if (AppManager.getIsLogin(getApplicationContext())) {
            RongConnect.initRongTokenConnect(AppManager.getUserId(getApplicationContext()));
            if (GestureManager.intercepterGestureActivity(this, AppManager.getUserInfo(this), false)) { // 手势密码验证
                finish();
                return;
            }
            Router.build(RouteConfig.GOTOCMAINHONE).go(LoadCustomerActivity.this);
            LoadCustomerActivity.this.finish();
            return;
        }
        UiSkipUtils.toNextActivity(baseActivity, WelcomeActivity.class);
        baseActivity.finish();
    }

    @Override
    protected void initView(Bundle state) {
        setContentView(R.layout.activity_customer_loadcustomer);
    }
}
