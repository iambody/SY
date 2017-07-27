package com.cgbsoft.privatefund.mvp.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.base.model.TypeNameEntity;
import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.utils.net.ApiBusParam;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.privatefund.R;
import com.chenenyu.router.annotation.Route;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 风险承受能力评测结果
 *
 * @author chenlong
 */
@Route("investornmain_riskresultactivity")
public class RiskResultActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.title_mid)
    TextView titleMid;

    @BindView(R.id.risk_result_restart)
    TextView restart;

    @BindView(R.id.risk_result_info)
    TextView resultinfo;

    @BindView(R.id.risk_result_img)
    ImageView image;

    @BindView(R.id.risk_result_commit)
    Button commit;

    String result = "";
    String riskEvaluationName = "";
    String riskEvaluationIdnum = "";
    String riskEvaluationPhone = "";
    private boolean visitor = true;

    @Override
    protected int layoutID() {
        return R.layout.activity_risk_result_to;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(com.cgbsoft.lib.R.drawable.ic_back_black_24dp);
        toolbar.setNavigationOnClickListener(v -> finish());
        titleMid.setText(R.string.risk_evaluating_title);
        initView();
    }

    @Override
    protected BasePresenterImpl createPresenter() {
        return null;
    }

    private void initView() {
        Intent intent = getIntent();
        int value = -1;
        if (null != getIntent().getExtras() && getIntent().getExtras().containsKey("level")) {
            String level = intent.getStringExtra("level");
            value = Integer.valueOf(level);
        }
        String str = "";
        switch (value) {
            case 1:
                str = "[ 保守型 ]";
                image.setBackgroundResource(visitor ? R.drawable.ic_baoshou_nor : R.drawable.ic_risk_result_conservative);
                break;
            case 2:
                str = "[ 稳健型 ]";
                image.setBackgroundResource(visitor ? R.drawable.ic_wenjian_nor : R.drawable.ic_risk_result_steady);
                break;
            case 3:
                str = "[ 平衡型 ]";
                image.setBackgroundResource(visitor ? R.drawable.ic_pingheng_nor : R.drawable.ic_risk_result_balance);
                break;
            case 4:
                str = "[ 成长型 ]";
                image.setBackgroundResource(visitor ? R.drawable.ic_chengzhang_nor : R.drawable.ic_risk_result_grow);
                break;
            case 5:
                str = "[ 进取型 ]";
                image.setBackgroundResource(visitor ? R.drawable.ic_jinqu_nor : R.drawable.ic_risk_result_radical);
                break;
            default:
                break;
        }
        restart.setTextColor(visitor ? getResources().getColor(R.color.app_golden) : getResources().getColor(R.color.red_new));
        resultinfo.setText(getString(R.string.risk_result_info) + "\n" + str);
        commit.setBackgroundResource(visitor ? R.drawable.golden_shape_sel_btn1 : R.drawable.selector_common_red_bg_btn);
    }

    @Override
    protected void onDestroy() {
//        EventBus.getDefault().post(new RefreshRiskComment());
        super.onDestroy();
    }

    @OnClick(R.id.risk_result_restart)
    public void onRestartCommit() {
        Intent intent = new Intent(this, RiskEvaluationActivity.class);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.risk_result_commit)
    public void onResultCommit() {
        finish();
    }

    private void riskEvaluation() {
        String valueStr = "2";
        int value = Integer.valueOf(valueStr);
        String str = "";
        switch (value) {
            case 1:
                image.setBackgroundResource(R.drawable.ic_risk_result_conservative);
                break;
            case 2:
                image.setBackgroundResource(R.drawable.ic_risk_result_steady);
                break;
            case 3:
                image.setBackgroundResource(R.drawable.ic_risk_result_balance);
                break;
            case 4:
                image.setBackgroundResource(R.drawable.ic_risk_result_grow);
                break;
            case 5:
                image.setBackgroundResource(R.drawable.ic_risk_result_radical);
                break;
        }

        resultinfo.setText(getString(R.string.risk_result_info) + str);
        commitRistResult();
    }

    private void commitRistResult() {
        ApiClient.commitRistResult(ApiBusParam.riskEvalutionParams(AppManager.getUserId(this), result, riskEvaluationName, riskEvaluationIdnum, riskEvaluationPhone)).subscribe(new RxSubscriber<TypeNameEntity.Result>() {
            @Override
            protected void onEvent(TypeNameEntity.Result result) {
                updateView(result.type);
            }

            @Override
            protected void onRxError(Throwable error) {
                Toast.makeText(RiskResultActivity.this, "风险承受能力评测失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateView(String values) {
        int value = Integer.valueOf(values);
        String str = "";
        switch (value) {
            case 1:
                image.setBackgroundResource(R.drawable.ic_risk_result_conservative);
                break;
            case 2:
                image.setBackgroundResource(R.drawable.ic_risk_result_steady);
                break;
            case 3:
                image.setBackgroundResource(R.drawable.ic_risk_result_balance);
                break;
            case 4:
                image.setBackgroundResource(R.drawable.ic_risk_result_grow);
                break;
            case 5:
                image.setBackgroundResource(R.drawable.ic_risk_result_radical);
                break;
        }
        resultinfo.setText(getString(R.string.risk_result_info) + str);
    }
}
