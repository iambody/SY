package com.cgbsoft.privatefund.mvc.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.cgbsoft.lib.base.mvc.BaseMvcActivity;
import com.cgbsoft.lib.base.webview.BaseWebview;
import com.cgbsoft.lib.base.webview.CwebNetConfig;
import com.cgbsoft.lib.utils.tools.NavigationUtils;
import com.cgbsoft.privatefund.R;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/7/27-21:18
 */
public class MembersAreaActivity extends BaseMvcActivity {

    BaseWebview memberareaWebview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_menberarea);
        memberareaWebview = (BaseWebview) findViewById(R.id.memberarea_webview);
        memberareaWebview.loadUrl(CwebNetConfig.membercenter);
    }

    public void toback(View V) {
        baseContext.finish();
    }

    public void torule(View V) {
        NavigationUtils.gotoWebActivity(baseContext, CwebNetConfig.adviserrules, "会员规则", false);
    }
}
