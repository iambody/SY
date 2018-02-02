package com.cgbsoft.privatefund.public_fund;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.tools.ThreadUtils;
import com.cgbsoft.privatefund.R;
import com.chenenyu.router.annotation.Route;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by wangpeng on 18-1-29.
 * <p>
 * 绑定公募基金银行列表
 */
@Route(RouteConfig.GOTO_PUBLIC_FUND_BIND_BANK_CARD)
public class BindingBankCardOfPublicFundActivity extends BaseActivity<BindingBankCardOfPublicFundPresenter> implements View.OnClickListener {
    public static final String TAG_PARAMETER = "tag_parameter";

    public final static int SELECT_BANK = 100;

    private TextView mPayBankName; // 用于支付的银行名字
    private EditText mPankcardCode; // 银行卡号
    private EditText mPhoneCode; // 手机号
    private EditText mVerificationCode; // 验证码
    private Button getVerificationCode; // 获取验证码


    @Override
    protected int layoutID() {
        return R.layout.activity_binding_bankcard;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mPayBankName = (TextView) findViewById(R.id.tv_pay_bank_name);
        mPankcardCode = (EditText) findViewById(R.id.ev_bankcard_code);
        mPhoneCode = (EditText) findViewById(R.id.ev_phone_number);
        mVerificationCode = (EditText) findViewById(R.id.ev_verification_code_input);
        getVerificationCode = (Button) findViewById(R.id.bt_get_verification_code);

        bindView();
    }

    @Override
    protected BindingBankCardOfPublicFundPresenter createPresenter() {
        return new BindingBankCardOfPublicFundPresenter(this, null);
    }

    /**
     * 绑定View数据与监听
     */
    private void bindView() {
        // 该表标题
        ((TextView) findViewById(R.id.title_mid)).setText("绑定银行卡");
        // 获取验证码按钮
        findViewById(R.id.bt_get_verification_code).setOnClickListener(this);
        // 确认购买
        findViewById(R.id.bt_Confirm).setOnClickListener(this);

        // 选择银行
        findViewById(R.id.rl_select_bankcard).setOnClickListener(this);

        // 返回键
        findViewById(R.id.title_left).setOnClickListener(this);

    }


    private void bindBankInfo() {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_get_verification_code: // 获取验证码
                sendVerificationCode(getVerificationCode, 60);
                break;

            case R.id.bt_Confirm: // 确认购买

                break;

            case R.id.rl_select_bankcard: //选择银行卡
                startActivityForResult(new Intent(this, SelectBankCardActivity.class), SELECT_BANK);
                break;

            case R.id.title_left:// 返回键
                finish();
                break;

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (SELECT_BANK == requestCode) {
            mPayBankName.setText(data.getStringExtra(SelectBankCardActivity.CHANNEL_ID));
        }

    }


    /**
     * 发送验证码
     */
    private final static int TIME = R.id.bt_get_verification_code;
    private final static String LAST_VERIFICATION_TIME = "last_verification_time";

    /**
     * 获取验证码
     *
     * @param v
     * @param maxTime 最大秒数 默认60
     */
    public void sendVerificationCode(final View v, int maxTime) {
        if (v.getTag(TIME) == null || (Integer) v.getTag(TIME) == 0) {
            v.setTag(TIME, maxTime);
            final Timer timer = new Timer();
            v.setTag(timer);
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    int time = (int) v.getTag(TIME);
                    if (time > 1) {
                        v.setTag(TIME, --time);
                        final int finalTime = time;
                        ThreadUtils.runOnMainThread(new Runnable() {
                            @Override
                            public void run() {
                                getVerificationCode.setText(finalTime + "");
                            }
                        });
                    } else {
                        timer.cancel();
                        v.setTag(TIME, null);
                        ThreadUtils.runOnMainThread(new Runnable() {
                            @Override
                            public void run() {
                                getVerificationCode.setText("获取验证码");
                            }
                        });

                    }
                }
            }, 1000, 1000);

            getPresenter().getVerificationCodeFormServer("", "", "", "", "", new BasePublicFundPresenter.PreSenterCallBack<String>() {
                @Override
                public void even(String s) {
                    timer.cancel();
                }

                @Override
                public void field(String errorCode, String errorMsg) {
                    timer.cancel();
                }
            });
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        // 上次退出页面时,验证码的时间点
        if (SPreference.getString(BindingBankCardOfPublicFundActivity.this, LAST_VERIFICATION_TIME) != null) {
            long lastTime = Long.valueOf(SPreference.getString(BindingBankCardOfPublicFundActivity.this, LAST_VERIFICATION_TIME));
            int time = (int) (System.currentTimeMillis() - lastTime);
            if (time > 1000 && time < 60 * 1000) {
                sendVerificationCode(getVerificationCode, time / 1000);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (getVerificationCode.getTag(TIME) != null && (Integer) getVerificationCode.getTag(TIME) > 1) {
            SPreference.putString(BindingBankCardOfPublicFundActivity.this, LAST_VERIFICATION_TIME, "" + System.currentTimeMillis());
            getVerificationCode.setTag(TIME, null);
            Timer timer = (Timer) getVerificationCode.getTag();
            timer.cancel();
        }
    }

    /**
     *  开启页面
     * @param context
     * @param parms
     */
    /*public static void startPage(Context context,String parms){

    }*/
}
