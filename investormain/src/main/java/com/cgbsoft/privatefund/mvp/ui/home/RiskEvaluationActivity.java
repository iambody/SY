package com.cgbsoft.privatefund.mvp.ui.home;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.TextView;

import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.base.webview.BaseWebview;
import com.cgbsoft.lib.base.webview.CwebNetConfig;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.widget.dialog.DefaultDialog;
import com.cgbsoft.privatefund.R;

import butterknife.BindView;


/**
 * 风险承受能力评测 *
 */
public class RiskEvaluationActivity extends BaseActivity {


    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.title_mid)
    protected TextView titleMid;

    @BindView(R.id.webview)
    protected BaseWebview mWebview;

    private String url = "";

    @Override
    protected void before() {
        super.before();
        url = CwebNetConfig.riskEvaluationQuestion;
    }

    @Override
    protected int layoutID() {
        return R.layout.activity_risk_evaluation;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        toolbar.setNavigationIcon(com.cgbsoft.lib.R.drawable.ic_back_black_24dp);
        toolbar.setNavigationOnClickListener(v -> {
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
        });
        titleMid.setText(getString(R.string.risk_evaluating_title));
    }

    @Override
    protected BasePresenterImpl createPresenter() {
        return null;
    }


    protected void onResume() {
        super.onResume();
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
