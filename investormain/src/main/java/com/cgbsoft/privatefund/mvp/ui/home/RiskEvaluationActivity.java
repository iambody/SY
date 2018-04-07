package com.cgbsoft.privatefund.mvp.ui.home;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.base.webview.BaseWebNetConfig;
import com.cgbsoft.lib.base.webview.BaseWebview;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.constant.Constant;
import com.cgbsoft.lib.widget.dialog.DefaultDialog;
import com.cgbsoft.privatefund.R;
import com.chenenyu.router.annotation.Route;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;

/**
 * 风险承受能力评测 *
 */
@Route(RouteConfig.GOTO_APP_RISKEVALUATIONACTIVITY)
public class RiskEvaluationActivity extends BaseActivity {

//    @BindView(R.id.toolbar)
//    protected Toolbar toolbar;
    @BindView(R.id.iv_back)
    ImageView ivBack;

    @BindView(R.id.title_mid)
    protected TextView titleMid;

    @BindView(R.id.webview)
    protected BaseWebview mWebview;

    private String url = "";

    @Override
    protected void before() {
        super.before();
        url = BaseWebNetConfig.evaluation;

    }

    @Override
    protected int layoutID() {
        return R.layout.activity_risk_evaluation;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
//        toolbar.setNavigationIcon(com.cgbsoft.lib.R.drawable.ic_back_black_24dp);
//        toolbar.setNavigationOnClickListener(v -> {
//            new DefaultDialog(RiskEvaluationActivity.this, "当前问卷填写内容将不会保存，确定要返回吗?", "取消", "确定") {
//                @Override
//                public void left() {
//                    this.dismiss();
//                }
//
//                @Override
//                public void right() {
//                    this.dismiss();
//                    RiskEvaluationActivity.this.finish();
//                }
//            }.show();
//        });
        titleMid.setText(getString(R.string.risk_title));
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DefaultDialog(RiskEvaluationActivity.this, "当前问卷填写内容将不会保存，确定要返回吗?", "取消", "确定") {
                    @Override
                    public void left() {
                        this.dismiss();
                    }

                    @Override
                    public void right() {
                        this.dismiss();
                        RiskEvaluationActivity.this.finish();
                    }
                }.show();
            }
        });
//        mWebview.setClick(new CWebClient.WebviewOnClick() {
//            @Override
//            public void onClick(String result) {
//                if(result.startsWith(WebViewConstant.AppCallBack.TOC_RISKTEST)){
//
//                }
//            }
//        });
    }

    @Override
    protected BasePresenterImpl createPresenter() {
        return null;
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        MobclickAgent.onPageEnd(Constant.SXY_FXWJ);
    }

    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart(Constant.SXY_FXWJ);
        String isEvaluated = SPreference.getUserInfoData(this).getToC().getIsEvaluated();
        String phoneNumber =SPreference.getUserInfoData(this).getPhoneNum();
        if ("2".equals(isEvaluated)) {
            url = url + "?param=%7B%22name%22%3A%22ssss%22%2C%22phoneNumber%22%3A%221399029292%22%7D";
        } else if ((!TextUtils.isEmpty(phoneNumber))&&phoneNumber.length()>0) {
            url = url + "?param=%7B%22phoneNumber%22%3A%22132213%22%7D";
        }
        mWebview.loadUrl(url);
    }
}
