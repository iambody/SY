package app.mall.com.mvp.ui;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.base.webview.WebViewConstant;
import com.cgbsoft.lib.utils.tools.NavigationUtils;
import com.iapppay.alpha.pay.channel.alipay.PayResult;

import app.mall.com.mvp.contract.PayResultContract;
import app.mall.com.mvp.presenter.PayResultPresenter;
import butterknife.BindView;
import qcloud.mall.R;
import qcloud.mall.R2;

public class RechargeResultActivity extends BaseActivity<PayResultPresenter> implements PayResultContract.View {


    private LinearLayout rechargeSuc;
    private LinearLayout disRecharge;
    private ImageView rechargeIcon;
    private TextView rechargeState;
    private TextView ydCount;
    private Button goMall;
    private TextView titleMid;

//    @BindView(R2.id.toolbar)
//    Toolbar toolbar;

    @Override
    protected int layoutID() {
        if (AppManager.isInvestor(this)){
            return R.layout.activity_recharge_result_c;
        }else {
            return R.layout.activity_recharge_result;
        }
    }

    @Override
    protected void after() {
        super.after();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    @Override
    protected void init(Bundle savedInstanceState) {
//        setSupportActionBar(toolbar);
//        toolbar.setNavigationIcon(com.cgbsoft.lib.R.drawable.ic_back_black_24dp);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleMid = (TextView) findViewById(R.id.title_mid);
        rechargeSuc = (LinearLayout) findViewById(R.id.recharge_suc);
        disRecharge = (LinearLayout) findViewById(R.id.disrecharge);
        rechargeIcon = (ImageView) findViewById(R.id.pay_state_icon);
        rechargeState = (TextView) findViewById(R.id.pay_state_text);
        ydCount = (TextView) findViewById(R.id.yd_count);
        TextView result = (TextView) findViewById(R.id.result);
        goMall = (Button) findViewById(R.id.go_mall);
        titleMid.setText("支付结果");
        boolean rechargeResult = getIntent().getBooleanExtra("rechargeResultState",false);
        if (rechargeResult){
            String rechargeCount = getIntent().getStringExtra("rechargeCount");
            disRecharge.setVisibility(View.GONE);
            rechargeSuc.setVisibility(View.VISIBLE);
            if (AppManager.isInvestor(this)){
                rechargeIcon.setBackgroundResource(R.drawable.icon_pay_completed);
            }else {
                rechargeIcon.setBackgroundResource(R.drawable.recharge_recharge_success);
            }
            rechargeState.setText("充值成功");
            rechargeState.setTextColor(0xffea1202);
            ydCount.setText(rechargeCount);
            goMall.setText("返回");
            goMall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavigationUtils.jumpNativePage(RechargeResultActivity.this,WebViewConstant.Navigation.LIFT_MALL_PAGE);
                }
            });


        }else {
            String rechargeResultDesc = getIntent().getStringExtra("rechargeResult");
            rechargeSuc.setVisibility(View.GONE);
            disRecharge.setVisibility(View.VISIBLE);
            if (AppManager.isInvestor(this)){
                rechargeIcon.setBackgroundResource(R.drawable.icon_pay_uncompleted);
            }else {
                rechargeIcon.setBackgroundResource(R.drawable.recharge_recharge_failure);
            }
            rechargeState.setText("充值失败");
            rechargeState.setTextColor(0xff222222);
            result.setText(rechargeResultDesc);
            goMall.setText("再次尝试");
            goMall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RechargeResultActivity.this.finish();
                }
            });
        }
    }

    @Override
    protected PayResultPresenter createPresenter() {
        return new PayResultPresenter(this, this);
    }



}
