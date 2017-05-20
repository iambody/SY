package com.cgbsoft.privatefund.widget.mvc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.privatefund.R;
import com.chenenyu.router.annotation.Route;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 风险承受能力评测结果
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

    @BindView(R.id.risk_result_type)
    TextView type;

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
        titleMid.setText(R.string.risk_evaluating_title);
        toolbar.setNavigationIcon(com.cgbsoft.lib.R.drawable.ic_back_black_24dp);
        toolbar.setNavigationOnClickListener(v -> finish());
        initView();
    }

    @Override
    protected BasePresenterImpl createPresenter() {
        return null;
    }

    private void initView() {
        Intent intent = getIntent();
        String level = intent.getStringExtra("level");
        int value = Integer.valueOf(level);
        String str = "";
        switch (value) {
            case 1:
                str = "[ 保守型 ]";
//                image.setBackgroundResource(visitor ? R.drawable.baoshou_nor : R.drawable.risk_result_conservative);
                break;
            case 2:
                str = "[ 稳健型 ]";
//                image.setBackgroundResource(visitor ? R.drawable.ic_wenjian_nor : R.drawable.risk_result_steady);
                break;
            case 3:
                str = "[ 平衡型 ]";
//                image.setBackgroundResource(visitor ? R.drawable.pingheng_nor : R.drawable.risk_result_balance);
                break;
            case 4:
                str = "[ 成长型 ]";
//                image.setBackgroundResource(visitor ? R.drawable.chengzhang_nor : R.drawable.risk_result_grow);
                break;
            case 5:
                str = "[ 进取型 ]";
//                image.setBackgroundResource(visitor ? R.drawable.jinqu_nor : R.drawable.risk_result_radical);
                break;
        }

        restart.setTextColor(visitor ? getResources().getColor(R.color.orange) : getResources().getColor(R.color.red_new));
        type.setTextColor(visitor ? getResources().getColor(R.color.orange) : getResources().getColor(R.color.red_new));
        type.setText(str);
        resultinfo.setText(getString(R.string.risk_result_info) + "\n" + str);
//        commit.setBackgroundResource(visitor ? R.drawable.c_common_bg_btn : R.drawable.common_red_bg_btn);

//        UserInfo user = MApplication.getUser();
//        user.setRiskEvaluationIdnum(riskEvaluationIdnum);
//        user.setRiskEvaluationName(riskEvaluationName);
//        user.setRiskEvaluationPhone(riskEvaluationPhone);
//        UserInfoC toC = user.getToC();
//        toC.setIsEvaluated(1);
//        user.setToC(toC);
//        MApplication.setUser(user);
    }

    @Override
    protected void onDestroy() {
//        EventBus.getDefault().post(new RefreshRiskComment());
        super.onDestroy();
    }

    @OnClick(R.id.risk_result_restart)
    public void onRestartCommit() {
//        Intent intent = new Intent(this, RiskEvaluationActivity.class);
//        startActivity(intent);
//        finish();
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
                str = "[ 保守型 ]";
//                image.setBackgroundResource(R.drawable.risk_result_conservative);
                break;
            case 2:
                str = "[ 稳健型 ]";
//                image.setBackgroundResource(R.drawable.risk_result_steady);
                break;
            case 3:
                str = "[ 平衡型 ]";
//                image.setBackgroundResource(R.drawable.risk_result_balance);
                break;
            case 4:
                str = "[ 成长型 ]";
//                image.setBackgroundResource(R.drawable.risk_result_grow);
                break;
            case 5:
                str = "[ 进取型 ]";
//                image.setBackgroundResource(R.drawable.risk_result_radical);
                break;
        }

        type.setText(str);
        resultinfo.setText(getString(R.string.risk_result_info) + str);

//        JSONObject jsonObj = new JSONObject();
//        try {
//            jsonObj.put("uid", SPreference.getUserId(this));
//            jsonObj.put("answer", result);
//            jsonObj.put("riskEvaluationName", riskEvaluationName);
//            jsonObj.put("riskEvaluationIdnum", riskEvaluationIdnum);
//            jsonObj.put("riskEvaluationPhone", riskEvaluationPhone);
//        } catch (Exception e) {
//        }
//        new RiskEvaluationTask(context).start(jsonObj.toString(), new HttpResponseListener() {
//
//            public void onResponse(JSONObject response) {
//                try {
//                    Log.i("", "resopnse=" + response.toString());
//                    String valueStr = response.getString("type");
//                    int value = Integer.valueOf(valueStr);
//                    String str = "";
//                    switch (value) {
//                        case 1:
//                            str = "[ 保守型 ]";
//                            image.setBackgroundResource(R.drawable.risk_result_conservative);
//                            break;
//                        case 2:
//                            str = "[ 稳健型 ]";
//                            image.setBackgroundResource(R.drawable.risk_result_steady);
//                            break;
//                        case 3:
//                            str = "[ 平衡型 ]";
//                            image.setBackgroundResource(R.drawable.risk_result_balance);
//                            break;
//                        case 4:
//                            str = "[ 成长型 ]";
//                            image.setBackgroundResource(R.drawable.risk_result_grow);
//                            break;
//                        case 5:
//                            str = "[ 进取型 ]";
//                            image.setBackgroundResource(R.drawable.risk_result_radical);
//                            break;
//                    }
//
//                    type.setText(str);
//                    resultinfo.setText(getString(R.string.risk_result_info) + str);
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
 //           }
//
//            public void onErrorResponse(String error, int statueCode) {
//                try {
//                    Toast.makeText(RiskResultActivity.this, "风险承受能力评测失败", Toast.LENGTH_SHORT).show();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
    }

}
