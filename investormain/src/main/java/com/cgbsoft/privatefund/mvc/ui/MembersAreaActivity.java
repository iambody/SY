package com.cgbsoft.privatefund.mvc.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.cgbsoft.lib.base.mvc.BaseMvcActivity;
import com.cgbsoft.lib.base.webview.BaseWebview;
import com.cgbsoft.lib.base.webview.CwebNetConfig;
import com.cgbsoft.lib.utils.constant.Constant;
import com.cgbsoft.lib.utils.constant.RxConstant;
import com.cgbsoft.lib.utils.rxjava.RxBus;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.tools.NavigationUtils;
import com.cgbsoft.privatefund.R;
import com.umeng.analytics.MobclickAgent;

import rx.Observable;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/7/27-21:18
 */
public class MembersAreaActivity extends BaseMvcActivity {

    BaseWebview memberareaWebview;
    TextView titleView;
    private Observable<String> moidfyTitleObservable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_menberarea);
        memberareaWebview = (BaseWebview) findViewById(R.id.memberarea_webview);
        memberareaWebview.loadUrl(CwebNetConfig.membercenter);
        titleView = (TextView) findViewById(R.id.title);
        moidfyTitleObservable = RxBus.get().register(RxConstant.WEBVIEW_MODIFY_TITLE, String.class);
        moidfyTitleObservable.subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String titleName) {
                if (!TextUtils.isEmpty(titleName)) {
                    titleView.setText(titleName);
                }
            }

            @Override
            protected void onRxError(Throwable error) {
            }
        });
    }

    public void toback(View V) {
        baseContext.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart(Constant.SXY_HY);
        memberareaWebview.loadUrl("javascript:refresh()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        MobclickAgent.onPageEnd(Constant.SXY_HY);
    }

    public void torule(View V) {
        NavigationUtils.gotoWebActivity(baseContext, CwebNetConfig.adviserrules, "会员规则", false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (moidfyTitleObservable != null) {
            RxBus.get().unregister(RxConstant.WEBVIEW_MODIFY_TITLE, moidfyTitleObservable);
        }
    }
}
