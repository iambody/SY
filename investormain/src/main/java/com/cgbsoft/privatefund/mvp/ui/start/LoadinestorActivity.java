package com.cgbsoft.privatefund.mvp.ui.start;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.cgbsoft.lib.utils.tools.UiSkipUtils;
import com.cgbsoft.privatefund.mvp.ui.home.MainPageActivity;
import com.chenenyu.router.RouteTable;
import com.chenenyu.router.Router;

import java.util.Map;

import app.ndk.com.enter.mvp.ui.Load.LoadCustomerActivity;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/5/4-15:53
 */
public class LoadinestorActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Router.build("investornmain_mainpageactivity").go(LoadinestorActivity.this);
//        UiSkipUtils.toNextActivity(this,LoadCustomerActivity.class);
    }
}
